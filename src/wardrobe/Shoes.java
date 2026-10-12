package wardrobe;

import java.util.Set;

public final class Shoes extends ClothingItem {
    public Shoes(int id, String name, String color, Set<Season> seasons,
                  Set<Weather> weather, int minTemperature, int maxTemperature) {
        super(id, name, color, seasons, weather, minTemperature, maxTemperature);
    }

    @Override
    public Category getCategory() { return Category.SHOES; }

    @Override
    public String getWearAdvice() {
        return "Wear as footwear; check the sole grip before going outside.";
    }
}
