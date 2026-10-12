package wardrobe;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

/** Handles terminal input and output; collection logic lives in Wardrobe. */
public final class WardrobeCli {
    private final Wardrobe wardrobe;
    private final Scanner input;

    public WardrobeCli(Wardrobe wardrobe, Scanner input) {
        this.wardrobe = wardrobe;
        this.input = input;
    }

    public void run() {
        System.out.println("Welcome to Wardrobe CLI! Data is kept only during this session.");
        try {
            boolean running = true;
            while (running) {
                System.out.println("\n1. Add clothing\n2. List clothing (sorted by name)\n3. Search name or color"
                        + "\n4. Filter by category\n5. Suggest clothing for the weather\n6. Remove clothing\n0. Exit");
                try {
                    switch (readInt("Choose an option: ", 0, 6)) {
                        case 1 -> addClothing();
                        case 2 -> show(wardrobe.listByName(), false);
                        case 3 -> show(wardrobe.search(readText("Search: ")), false);
                        case 4 -> show(wardrobe.filterByCategory(readEnum("Category", Category.class)), false);
                        case 5 -> suggest();
                        case 6 -> System.out.println(wardrobe.remove(readInt("Item ID: ", 1, Integer.MAX_VALUE))
                                ? "Item removed." : "No item with that ID.");
                        case 0 -> running = false;
                        default -> throw new IllegalStateException("Unexpected menu option.");
                    }
                } catch (IllegalArgumentException exception) {
                    System.out.println("Could not add item: " + exception.getMessage());
                }
            }
        } catch (EndOfInputException exception) {
            System.out.println("\nInput closed.");
        }
        System.out.println("Goodbye!");
    }

    private void addClothing() {
        Category category = readEnum("Category", Category.class);
        String name = readText("Name: ");
        String color = readText("Color: ");
        Set<Season> seasons = readSet("Seasons", Season.class);
        Set<Weather> weather = readSet("Weather conditions", Weather.class);
        int min = readInt("Minimum temperature (C): ", -60, 60);
        int max = readInt("Maximum temperature (C): ", min, 60);
        ClothingItem item = wardrobe.add(category, name, color, seasons, weather, min, max);
        System.out.println("Added: " + item);
    }

    private void suggest() {
        Season season = readEnum("Season", Season.class);
        Weather weather = readEnum("Weather", Weather.class);
        int temperature = readInt("Temperature (C): ", -60, 60);
        System.out.println("Matching items (based on the conditions you entered):");
        show(wardrobe.recommend(season, weather, temperature), true);
    }

    private void show(List<ClothingItem> items, boolean includeAdvice) {
        if (items.isEmpty()) {
            System.out.println("No matching clothing items.");
        }
        for (ClothingItem item : items) {
            System.out.println(item);
            if (includeAdvice) {
                // Dynamic dispatch: the concrete subclass provides its own advice.
                System.out.println("  " + item.getWearAdvice());
            }
        }
    }

    private String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!input.hasNextLine()) {
                throw new EndOfInputException();
            }
            String value = input.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Please enter a non-empty value.");
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(readText(prompt));
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException exception) {
                // Invalid numeric input is retried without terminating the menu.
            }
            System.out.printf("Enter a whole number from %d to %d.%n", min, max);
        }
    }

    private <E extends Enum<E>> E readEnum(String label, Class<E> type) {
        while (true) {
            String value = readText(label + " " + List.of(type.getEnumConstants()) + ": ");
            try {
                return Enum.valueOf(type, value.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception) {
                System.out.println("Choose one of the listed values (case does not matter).");
            }
        }
    }

    private <E extends Enum<E>> Set<E> readSet(String label, Class<E> type) {
        while (true) {
            String value = readText(label + " " + List.of(type.getEnumConstants())
                    + " (comma-separated): ");
            Set<E> values = new HashSet<>();
            try {
                for (String part : value.split(",", -1)) {
                    values.add(Enum.valueOf(type, part.trim().toUpperCase(Locale.ROOT)));
                }
                return values;
            } catch (IllegalArgumentException exception) {
                System.out.println("Use listed values separated by commas, for example SPRING, SUMMER.");
            }
        }
    }

    private static final class EndOfInputException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
