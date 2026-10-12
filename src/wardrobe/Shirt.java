package wardrobe;

import java.util.Set;

public final class Shirt extends ClothingItem {
    public Shirt(int id, String name, String color, Set<Season> seasons,
                  Set<Weather> weather, int minTemperature, int maxTemperature) {
        super(id, name, color, seasons, weather, minTemperature, maxTemperature);
    }

    @Override
    public Category getCategory() { return Category.SHIRT; }

    @Override
    public String getWearAdvice() {
        return "Wear as an upper-body layer; add a coat if needed.";
    }
}
