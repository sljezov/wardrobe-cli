# Wardrobe CLI

A small English-language Java command-line application for organising clothing and finding items suitable for a season, weather condition and temperature.

## Requirements and running

Install a JDK version 17 or newer. Both `java -version` and `javac -version` should work. No external libraries, database or build tool are required.

If Java was installed with Homebrew on an Apple Silicon Mac but the commands cannot find a runtime, enable it in your current terminal first:

```sh
export PATH="/opt/homebrew/opt/openjdk/bin:$PATH"
```

From the project directory on macOS or Linux:

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/wardrobe/*.java
java -cp out wardrobe.Main
```

On Windows PowerShell:

```powershell
New-Item -ItemType Directory -Force out
javac -encoding UTF-8 -d out (Get-ChildItem src/wardrobe/*.java).FullName
java -cp out wardrobe.Main
```

## Using the application

The wardrobe starts empty. Choose `1` to add a shirt, trousers, shoes or coat. Enter a name, color, one or more seasons and weather conditions, then a temperature range in Celsius. Enum values are case-insensitive; separate multiple values with commas.

Example: add a `COAT` named `Rain jacket`, color `blue`, seasons `SPRING, AUTUMN`, weather `RAINY, WINDY`, minimum `5`, maximum `18`. Choose `5` and enter `SPRING`, `RAINY`, `10` to see the jacket and its coat-specific advice.

Menu options also list items alphabetically, search by name or color, filter by category and remove an item by its ID. Choose `0` to exit. All data is kept in memory and is lost on exit. Suggestions list matching items, not complete outfits. Conditions are entered manually; there is no weather service.

## Project structure

`ClothingItem` is an abstract, immutable model with validated fields. `Shirt`, `Trousers`, `Shoes` and `Coat` share its suitability logic and provide their own wear advice.

`Wardrobe` stores items and handles search, filtering and sorting. `WardrobeCli` manages terminal interaction and input validation. `Main` starts the application. Seasons, weather conditions and categories use enums.

Items are stored in a `HashMap` keyed by ID. An `ArrayList` provides sorted results, and a `HashSet` removes duplicate seasons and weather conditions during input. Clothing items store immutable copies of these sets.

An item matches when its season and weather condition both match, and the temperature falls within its inclusive minimum and maximum limits.

## Testing

Run the dependency-free checks after installing the JDK:

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/wardrobe/*.java tests/wardrobe/*.java
java -cp out wardrobe.WardrobeTest
```

The checks cover temperature boundaries, season and weather matching, search, category filtering, alphabetical sorting, type-specific advice, invalid temperature ranges, defensive copies and deletion.
