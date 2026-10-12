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
        return listByName().stream().filter(item ->
                item.getName().toLowerCase(Locale.ROOT).contains(text)
                        || item.getColor().toLowerCase(Locale.ROOT).contains(text)).toList();
    }

    public List<ClothingItem> filterByCategory(Category category) {
        return listByName().stream().filter(item -> item.getCategory() == category).toList();
    }

    public List<ClothingItem> recommend(Season season, Weather weather, int temperature) {
        return listByName().stream()
                .filter(item -> item.isSuitable(season, weather, temperature)).toList();
    }
}
