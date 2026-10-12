package wardrobe;

import java.util.Set;

public final class Trousers extends ClothingItem {
    public Trousers(int id, String name, String color, Set<Season> seasons,
                  Set<Weather> weather, int minTemperature, int maxTemperature) {
        super(id, name, color, seasons, weather, minTemperature, maxTemperature);
    }

    @Override
    public Category getCategory() { return Category.TROUSERS; }

    @Override
    public String getWearAdvice() {
        return "Wear as a lower-body layer and pair with suitable shoes.";
    }
}
