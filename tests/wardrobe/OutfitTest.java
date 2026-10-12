package wardrobe;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/** Checks outfit rules and the complete CLI generate/save/view flow. */
public final class OutfitTest {
    public static void main(String[] args) {
        Wardrobe wardrobe = new Wardrobe();
        OutfitGenerator generator = new OutfitGenerator(wardrobe);
        expectMissing(generator, 10, "SHIRT", "TROUSERS", "SHOES", "COAT");
        add(wardrobe, Category.SHIRT, "Z shirt");
        ClothingItem first = add(wardrobe, Category.SHIRT, "A shirt");
        add(wardrobe, Category.TROUSERS, "Trousers");
        add(wardrobe, Category.SHOES, "Shoes");
        // An alphabetical first item that does not match must never be selected.
        wardrobe.add(Category.SHIRT, "0 summer shirt", "white", Set.of(Season.SUMMER),
                Set.of(Weather.SUNNY), 20, 35);
        Outfit warm = generator.generate(Season.SPRING, Weather.RAINY, 15);
        check(warm.getItems().size() == 3, "No coat at 15 C");
        check(warm.getItems().get(0) == first, "First alphabetical suitable shirt");
        expectMissing(generator, 14, "COAT");
        add(wardrobe, Category.COAT, "Rain jacket");
        Outfit cold = generator.generate(Season.SPRING, Weather.RAINY, 14);
        check(cold.getItems().size() == 4, "Coat below 15 C");
        expectMissing(generator, 31, "SHIRT", "TROUSERS", "SHOES");
        wardrobe.saveFavorite(" Rainy day ", cold);
        check(wardrobe.getFavorites().get(0).getName().equals("Rainy day"), "Named favorite");
        wardrobe.getFavorites().clear();
        check(wardrobe.getFavorites().size() == 1, "Favorites do not expose storage");
        wardrobe.remove(first.getId());
        check(wardrobe.getFavorites().get(0).getItems().contains(first), "Favorite retains snapshot");
        expectInvalid(() -> new Outfit(" ", cold.getItems()));
        expectInvalid(() -> new Outfit("Incomplete", List.of(first)));
        expectInvalid(() -> new Outfit("Duplicate", List.of(first, first)));
        try {
            cold.getItems().clear();
            throw new AssertionError("Outfit items must be immutable");
        } catch (UnsupportedOperationException expected) {
            // The getter cannot be used to break the complete-outfit invariant.
        }
        checkCli(wardrobe);
        System.out.println("All outfit checks passed.");
    }

    private static ClothingItem add(Wardrobe wardrobe, Category category, String name) {
        return wardrobe.add(category, name, "black", Set.of(Season.SPRING),
                Set.of(Weather.RAINY), 0, 30);
    }

    private static void expectMissing(OutfitGenerator generator, int temperature, String... categories) {
        try {
            generator.generate(Season.SPRING, Weather.RAINY, temperature);
            throw new AssertionError("Incomplete outfit must fail");
        } catch (IllegalStateException expected) {
            for (String category : categories) {
                check(expected.getMessage().contains(category), "Missing category: " + category);
            }
        }
    }

    private static void expectInvalid(Runnable action) {
        try {
            action.run();
            throw new AssertionError("Invalid outfit must fail");
        } catch (IllegalArgumentException expected) {
            // Expected domain validation.
        }
    }

    private static void checkCli(Wardrobe wardrobe) {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(output)) {
            System.setOut(capture);
            String commands = "8\n7\nspring\nrainy\n14\n8\nCLI favorite\n9\n"
                    + "7\nwinter\nsnowy\n-10\n8\n0\n";
            new WardrobeCli(wardrobe, new Scanner(commands)).run();
        } finally {
            System.setOut(original);
        }
        String text = output.toString();
        check(text.contains("OUTFIT: CLI favorite"), "Generate, save and view in CLI");
        check(text.contains("Could not generate outfit."), "CLI handles missing items");
        check(text.lastIndexOf("Generate an outfit first") > text.indexOf("Could not generate outfit."),
                "Failed generation clears previous outfit");
        check(wardrobe.getFavorites().size() == 2, "Only successful outfit saved");
    }

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
    }
}
