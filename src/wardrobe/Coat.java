package wardrobe;

import java.util.Set;

public final class Coat extends ClothingItem {
    public Coat(int id, String name, String color, Set<Season> seasons,
                  Set<Weather> weather, int minTemperature, int maxTemperature) {
        super(id, name, color, seasons, weather, minTemperature, maxTemperature);
    }

    @Override
    public Category getCategory() { return Category.COAT; }

    @Override
    public String getWearAdvice() {
        return "Wear as an outer layer over your other clothes.";
    }
}
