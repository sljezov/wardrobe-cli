package wardrobe;

import java.util.Objects;
import java.util.Set;

/** Common, validated state shared by all clothing types. */
public abstract class ClothingItem {
    private final int id;
    private final String name;
    private final String color;
    private final Set<Season> seasons;
    private final Set<Weather> weatherConditions;
    private final int minTemperature;
    private final int maxTemperature;

    protected ClothingItem(int id, String name, String color, Set<Season> seasons,
                           Set<Weather> weatherConditions, int minTemperature,
                           int maxTemperature) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        if (minTemperature < -60 || maxTemperature > 60 || minTemperature > maxTemperature) {
            throw new IllegalArgumentException("Temperature range must be ordered and within -60 to 60 C.");
        }
        this.id = id;
        this.name = requireText(name, "Name");
        this.color = requireText(color, "Color");
        this.seasons = Set.copyOf(Objects.requireNonNull(seasons));
        this.weatherConditions = Set.copyOf(Objects.requireNonNull(weatherConditions));
        if (this.seasons.isEmpty() || this.weatherConditions.isEmpty()) {
            throw new IllegalArgumentException("Choose at least one season and weather condition.");
        }
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }
        return value.trim();
    }

    public final int getId() { return id; }
    public final String getName() { return name; }
    public final String getColor() { return color; }
    public final Set<Season> getSeasons() { return seasons; }
    public final Set<Weather> getWeatherConditions() { return weatherConditions; }
    public final int getMinTemperature() { return minTemperature; }
    public final int getMaxTemperature() { return maxTemperature; }

    public final boolean isSuitable(Season season, Weather weather, int temperature) {
        return seasons.contains(season) && weatherConditions.contains(weather)
                && temperature >= minTemperature && temperature <= maxTemperature;
    }

    public abstract Category getCategory();
    public abstract String getWearAdvice();

    @Override
    public final String toString() {
        return String.format("#%d | %s | %s | %s | seasons: %s | weather: %s | %d to %d C",
                id, getCategory(), name, color, seasons.stream().sorted().toList(),
                weatherConditions.stream().sorted().toList(), minTemperature, maxTemperature);
    }
}
