package wardrobe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Filters by conditions, then chooses the first alphabetical match per category. */
public final class OutfitGenerator {
    private final Wardrobe wardrobe;

    public OutfitGenerator(Wardrobe wardrobe) {
        this.wardrobe = Objects.requireNonNull(wardrobe);
    }

    public Outfit generate(Season season, Weather weather, int temperature) {
        Objects.requireNonNull(season, "Season is required.");
        Objects.requireNonNull(weather, "Weather is required.");
        if (temperature < -60 || temperature > 60) {
            throw new IllegalArgumentException("Temperature must be within -60 to 60 C.");
        }
        List<ClothingItem> suitable = wardrobe.recommend(season, weather, temperature);
        List<ClothingItem> selected = new ArrayList<>();
        List<Category> missing = new ArrayList<>();
        for (Category category : Category.values()) {
            // A coat is required below 15 C and omitted at warmer temperatures.
            if (category == Category.COAT && temperature >= 15) {
                continue;
            }
            ClothingItem match = null;
            for (ClothingItem item : suitable) {
                if (item.getCategory() == category) {
                    match = item;
                    break;
                }
            }
            if (match == null) {
                missing.add(category);
            } else {
                selected.add(match);
            }
        }
        if (!missing.isEmpty()) {
            throw new IllegalStateException("No suitable items for: " + missing);
        }
        return new Outfit("Generated outfit", selected);
    }
}
