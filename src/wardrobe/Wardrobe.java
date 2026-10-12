package wardrobe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Owns the collection and allocates IDs only after successful validation. */
public final class Wardrobe {
    private final Map<Integer, ClothingItem> items = new HashMap<>();
    private final List<Outfit> favorites = new ArrayList<>();
    private int nextId = 1;

    public ClothingItem add(Category category, String name, String color,
                            Set<Season> seasons, Set<Weather> weather,
                            int minTemperature, int maxTemperature) {
        ClothingItem item = switch (category) {
            case SHIRT -> new Shirt(nextId, name, color, seasons, weather, minTemperature, maxTemperature);
            case TROUSERS -> new Trousers(nextId, name, color, seasons, weather, minTemperature, maxTemperature);
            case SHOES -> new Shoes(nextId, name, color, seasons, weather, minTemperature, maxTemperature);
            case COAT -> new Coat(nextId, name, color, seasons, weather, minTemperature, maxTemperature);
        };
        items.put(nextId++, item);
        return item;
    }

    public boolean remove(int id) { return items.remove(id) != null; }

    public List<ClothingItem> listByName() {
        List<ClothingItem> result = new ArrayList<>(items.values());
        result.sort(Comparator.comparing(ClothingItem::getName, String.CASE_INSENSITIVE_ORDER)
                .thenComparingInt(ClothingItem::getId));
        return result;
    }

    public List<ClothingItem> search(String query) {
        String text = query.trim().toLowerCase(Locale.ROOT);
        List<ClothingItem> result = new ArrayList<>();
        for (ClothingItem item : listByName()) {
            if (item.getName().toLowerCase(Locale.ROOT).contains(text)
                    || item.getColor().toLowerCase(Locale.ROOT).contains(text)) {
                result.add(item);
            }
        }
        return result;
    }

    public List<ClothingItem> filterByCategory(Category category) {
        List<ClothingItem> result = new ArrayList<>();
        for (ClothingItem item : listByName()) {
            if (item.getCategory() == category) {
                result.add(item);
            }
        }
        return result;
    }

    public List<ClothingItem> recommend(Season season, Weather weather, int temperature) {
        List<ClothingItem> result = new ArrayList<>();
        for (ClothingItem item : listByName()) {
            if (item.isSuitable(season, weather, temperature)) {
                result.add(item);
            }
        }
        return result;
    }

    public void saveFavorite(String name, Outfit outfit) {
        favorites.add(new Outfit(name, outfit.getItems()));
    }

    public List<Outfit> getFavorites() {
        return new ArrayList<>(favorites);
    }
}
