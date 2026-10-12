# Wardrobe CLI

A small English-language Java command-line application for organising clothing, generating complete outfits for a season, weather condition and temperature, and saving favorite outfits during a session.

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

Menu options also list items alphabetically, search by name or color, filter by category and remove an item by its ID. Conditions are entered manually; there is no weather service.

Choose `7` to generate a complete outfit. Enter a season, weather condition and temperature. The generator selects one suitable shirt, trousers and pair of shoes. Below 15 C, a suitable coat is also required; at 15 C and above, the coat is omitted. This threshold is a simple application rule. Each category uses its first suitable item alphabetically, with ID breaking name ties. If required items are missing, the application lists their categories instead of producing an incomplete outfit.

Choose `8` to give the last generated outfit a name and save it as a favorite; choose `9` to view favorites. A failed generation clears the last generated outfit. Favorites retain their clothing items even if those items are later removed from the wardrobe: they represent saved combinations. Favorite names may repeat, and favorites appear in the order saved.

Choose `0` to exit. All clothing and favorites are kept in memory and are lost on exit.

## Project structure

`ClothingItem` is an abstract, immutable model with validated fields. `Shirt`, `Trousers`, `Shoes` and `Coat` share its suitability logic and provide their own wear advice.

`Wardrobe` stores clothing and favorite outfits and handles search, filtering and sorting. `Outfit` contains a named, immutable combination of clothing items and validates that required categories are present without duplicates. `OutfitGenerator` uses the wardrobe to filter items and build a complete outfit. `WardrobeCli` manages terminal interaction and input validation. `Main` starts the application. Seasons, weather conditions and categories use enums.

Items are stored in a `HashMap` keyed by ID for direct lookup during removal. An `ArrayList` provides sorted or filtered results and stores favorites in insertion order. A `HashSet` removes duplicate seasons and weather conditions during input and detects duplicate categories when validating outfits. Clothing items store immutable copies of their sets; outfits store immutable copies of their lists. Search, filtering and generation use straightforward loops.

An item matches when its season and weather condition both match, and the temperature falls within its inclusive minimum and maximum limits.

## Testing

Run the dependency-free checks after installing the JDK:

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/wardrobe/*.java tests/wardrobe/*.java
java -cp out wardrobe.WardrobeTest
java -cp out wardrobe.OutfitTest
```

The checks cover temperature boundaries, season and weather matching, search, category filtering, alphabetical sorting, type-specific advice, invalid temperature ranges, defensive copies and deletion. Outfit checks cover the 15 C coat rule, deterministic selection, missing categories, complete-outfit validation, favorite snapshots and the CLI generate/save/view flow.
