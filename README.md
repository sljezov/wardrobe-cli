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

## Assignment requirements and design

| Requirement | Implementation and explanation |
| --- | --- |
| At least four custom classes | `ClothingItem`, `Shirt`, `Trousers`, `Shoes`, `Coat`, `Wardrobe`, `WardrobeCli`, `Main` |
| Inheritance | The four clothing classes extend `ClothingItem`, sharing validated fields and suitability logic. |
| Abstraction | `ClothingItem` is abstract; each concrete type implements category and wear advice. |
| Encapsulation | Fields are private. Clothing is immutable; getters expose immutable sets. The wardrobe exposes no mutable internal map. |
| Polymorphism | The CLI loops over `ClothingItem` references and calls `getWearAdvice()`. The runtime subclass determines the advice. |
| Primitive and reference types | `int` for IDs and temperatures, `boolean` for decisions, `String`, enums and object references for descriptive data. |
| `HashMap` | Stores items by unique ID, allowing efficient lookup/removal without scanning the entire collection. |
| `ArrayList` | A separate list supports alphabetical sorting without modifying the map. |
| `HashSet` | Collects distinct seasons and weather conditions from input, removing duplicates. Items store immutable copies. |
| Search/filter/sort | Case-insensitive name/color search, category filter, weather suitability filter and alphabetical sorting with an ID tie-breaker. |
| Responsibility separation | Models validate clothing; `Wardrobe` manages and queries items; `WardrobeCli` handles terminal interaction; `Main` starts the app. |
| Invalid input | Empty text, unknown enum values, invalid numbers and out-of-range temperatures are retried. Reversed temperature ranges are prevented. Missing IDs show a message. End-of-input exits cleanly. |

Temperature suitability includes both endpoints and requires the selected season **and** weather condition to match. This deliberately simple rule is easy to explain and test. Clothing subclasses differ in wear advice, while matching stays consistent across all types.

## Validation

Run the dependency-free checks after installing the JDK:

```sh
mkdir -p out
javac -encoding UTF-8 -d out src/wardrobe/*.java tests/wardrobe/*.java
java -cp out wardrobe.WardrobeTest
```

Also manually try an empty wardrobe, add an item, search using uppercase text, filter by another category, request matching/non-matching recommendations, remove an unknown ID, and enter invalid menu values. Send end-of-input during item creation to verify clean shutdown.

Validated with Homebrew OpenJDK 27: compilation with `-Xlint:all` completed without warnings and all wardrobe checks passed. CLI smoke checks also passed for adding, recommending, searching, deleting, invalid menu input and end-of-input during item creation.

## Git and GitHub submission

Git is initialised in this project. GitHub publishing is a separate step.

Authenticate from your own terminal, then initialise the repository:

```sh
gh auth login --hostname github.com --git-protocol https --web
git init
git branch -M main
```

The following five commits group the delivered files into meaningful changes. They describe the content being added; they do not claim an earlier development history. These commands document how the initial commits are organised; do not repeat them if the commits already exist:

```sh
git add .gitignore src/wardrobe/Category.java src/wardrobe/Season.java src/wardrobe/Weather.java src/wardrobe/ClothingItem.java
git commit -m "add validated abstract clothing model and enums"
git add src/wardrobe/Shirt.java src/wardrobe/Trousers.java src/wardrobe/Shoes.java src/wardrobe/Coat.java
git commit -m "implement clothing subclasses with individual wear advice"
git add src/wardrobe/Wardrobe.java
git commit -m "add wardrobe storage search filtering and sorting"
git add src/wardrobe/Main.java src/wardrobe/WardrobeCli.java
git commit -m "add interactive cli with validated input and recommendations"
git add README.md tests/
git commit -m "document usage and add wardrobe validation checks"
```

Create and push a GitHub repository, choosing its visibility yourself. For example, if a public repository is suitable for submission:

```sh
gh repo create wardrobe-cli --public --source=. --remote=origin --push
gh repo view --web
```

Submit the resulting repository URL. Since wardrobe management is an original topic, confirm it with your teacher as required by the assignment.
