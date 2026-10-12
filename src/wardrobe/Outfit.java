package wardrobe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** A named, complete outfit; its clothing items are immutable. */
public final class Outfit {
    private final String name;
    private final List<ClothingItem> items;

    public Outfit(String name, List<ClothingItem> items) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Outfit name cannot be empty.");
        }
        this.name = name.trim();
        this.items = List.copyOf(items);
        Set<Category> categories = new HashSet<>();
        for (ClothingItem item : this.items) {
            if (!categories.add(item.getCategory())) {
                throw new IllegalArgumentException("An outfit can contain only one item per category.");
            }
        }
        if (!categories.containsAll(Set.of(Category.SHIRT, Category.TROUSERS, Category.SHOES))) {
            throw new IllegalArgumentException("An outfit needs a shirt, trousers and shoes.");
        }
    }

    public String getName() { return name; }

    public List<ClothingItem> getItems() { return items; }
}
