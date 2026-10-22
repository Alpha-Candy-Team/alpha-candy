# Alpha Candy

Alpha Candy is a Cobblemon addon for **Minecraft 1.21.1**. It adds craftable candies that let you turn Pokémon into Alpha Pokémon, along with purification and Advanced Alpha features.

## Compatibility

- **Minecraft:** 1.21.1
- **Cobblemon:** 1.8.1 or newer, within the supported Minecraft version
- **Loaders:** Fabric and NeoForge
- **Architecture:** Architectury
- **Java:** 21 or newer

Iota compatibility is supported when the Iotas mod is installed. Iotas is optional and is not required for the standard Alpha Candy features.

## Features

### Alpha Candy

Alpha Candy lets you attempt to turn a Pokémon into an Alpha Pokémon. Its success chance is determined by the Essence level stored in the candy when it is crafted.

| Alpha Essence level | Alpha conversion chance |
| ---: | ---: |
| 0 | 1% |
| 1 | 33% |
| 2 | 65% |
| 3 | 97% |

These values are configurable. The chance is stored in the crafted candy, so it remains the same when the item is moved, stored, or reloaded.

### Alpha Essence

Alpha Essence is used as seasoning in Cobblemon's Cooking Pot.

- Each occupied Alpha Essence seasoning slot adds **1 Essence level**.
- The number of items in a slot does not matter: a stack of 64 counts as one occupied slot.
- Up to **3 Essence levels** can be applied.
- Each occupied slot consumes one Alpha Essence when the recipe completes.
- Essence is consumed during crafting, not when the candy is used.

### Advanced Alpha Candy

Advanced Alpha Candy guarantees the Alpha conversion rather than using the regular candy's chance.

When used, it also:
- Raises the Pokémon's level by **1**.
- Sets the Pokémon's friendship to its maximum.
- Applies the **Advanced Alpha** mark.

### Purifying Candy

Purifying Candy removes the Alpha status from a Pokémon and restores its friendship. It can also remove the Advanced Alpha mark.

- In Survival mode, using it can reward Alpha Essence according to the configuration.
- Creative mode does not provide the Survival-mode Essence reward.
- Essence rewards and healing behaviour can be configured.

### Iota compatibility

When the Iotas mod is installed, Alpha Candy includes compatibility for Iota-related features and its Advanced Alpha Candy variant. Iotas is optional.

## Recipes

Recipes are prepared in Cobblemon's Cooking Pot.

### Alpha Candy

Ingredients:
- Sugar
- EXP Candy S
- Tamato Berry
- Optional Alpha Essence seasoning (up to 3 occupied slots)

The number of occupied Alpha Essence slots determines the Essence level stored in the resulting candy.

### Purifying Candy

Ingredients:
- Moomoo Milk
- Aspear Berry
- Pecha Berry
- Rawst Berry
- Lum Berry
- Alpha Candy

## Configuration

The mod supports configuration for values including:

- Base Alpha conversion chance
- Chance increase per Alpha Essence
- Maximum Essence level
- Purifying Candy Essence rewards
- Purifying Candy healing behaviour

Default Alpha Candy chances:

| Essence level | Chance |
| ---: | ---: |
| 0 | 1% |
| 1 | 33% |
| 2 | 65% |
| 3 | 97% |

## Building from source

Use Java 21 and the included Gradle wrapper.

From the repository root:

```powershell
.\gradlew.bat clean build
```

Built mod jars are produced in the platform modules' `build/libs` directories:

- `fabric/build/libs/`
- `neoforge/build/libs/`

## License

Alpha Candy is distributed under the **Alpha Candy Non-Commercial License**. See the repository's license file for the full terms.

## Version

**Alpha Candy 1.1.0**
