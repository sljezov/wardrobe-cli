package wardrobe;

import java.util.HashSet;
import java.util.Set;

/** Dependency-free checks; failures throw even when Java assertions are disabled. */
public final class WardrobeTest {
    public static void main(String[] args) {
        Wardrobe wardrobe = new Wardrobe();
        Set<Season> seasons = new HashSet<>(Set.of(Season.SPRING));
        ClothingItem coat = wardrobe.add(Category.COAT, "Rain jacket", "Blue", seasons,
                Set.of(Weather.RAINY), 5, 18);
        seasons.clear();
        check(coat.getSeasons().contains(Season.SPRING), "Defensive copy of seasons");
        check(coat.isSuitable(Season.SPRING, Weather.RAINY, 5), "Inclusive minimum");
        check(coat.isSuitable(Season.SPRING, Weather.RAINY, 18), "Inclusive maximum");
        check(!coat.isSuitable(Season.SPRING, Weather.RAINY, 19), "Temperature mismatch");
        check(!coat.isSuitable(Season.WINTER, Weather.RAINY, 10), "Season mismatch");
        check(!coat.isSuitable(Season.SPRING, Weather.SUNNY, 10), "Weather mismatch");
        check(wardrobe.search("BLUE").size() == 1, "Case-insensitive color search");
        check(wardrobe.search("jacket").size() == 1, "Partial name search");
        check(wardrobe.filterByCategory(Category.SHIRT).isEmpty(), "Category filtering");
        ClothingItem shirt = wardrobe.add(Category.SHIRT, "A shirt", "white", Set.of(Season.SUMMER),
                Set.of(Weather.SUNNY), 18, 35);
        check(wardrobe.listByName().get(0) == shirt, "Alphabetical sorting");
        check(!coat.getWearAdvice().equals(shirt.getWearAdvice()), "Polymorphic advice");
        check(wardrobe.recommend(Season.SPRING, Weather.RAINY, 10).equals(java.util.List.of(coat)),
                "Recommendations");
        try {
            wardrobe.add(Category.SHOES, "Boots", "black", Set.of(Season.WINTER),
                    Set.of(Weather.SNOWY), 10, -10);
            throw new AssertionError("Reversed range must fail");
        } catch (IllegalArgumentException expected) {
            check(wardrobe.listByName().size() == 2, "Invalid add leaves wardrobe unchanged");
        }
        wardrobe.listByName().clear();
        check(wardrobe.listByName().size() == 2, "List does not expose storage");
        check(wardrobe.remove(coat.getId()), "Remove existing item");
        check(!wardrobe.remove(coat.getId()), "Remove missing item");
        System.out.println("All wardrobe checks passed.");
    }

    private static void check(boolean condition, String description) {
        if (!condition) {
            throw new AssertionError(description);
        }
    }
}
