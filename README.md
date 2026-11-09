# Alpha Candy

Alpha Candy is a Cobblemon addon for **Minecraft 1.21.1** that adds candies for transforming Pokémon into Alpha Pokémon, purifying them, and applying advanced Alpha effects.

## Compatibility

- **Minecraft:** 1.21.1
- **Cobblemon:** 1.8.1 or newer, within the supported Minecraft version
- **Loaders:** Fabric and NeoForge
- **Java:** 21 or newer
- **Optional integration:** Iotas

## Features

### Alpha Candy and Alpha Essence

Use Alpha Candy on an owned Pokémon to attempt an Alpha transformation. The chance is based on the candy's Essence level, which is set when cooking it in Cobblemon's Cooking Pot. Each occupied Alpha Essence seasoning slot counts as one level, up to level 3; stack size does not affect the level. Essence is consumed when the recipe is made.

Default chances are **1%, 33%, 65%, and 97%** for Essence levels 0–3. The base chance and per-level boost are configurable. Pokémon species on the configured blacklist cannot be transformed.

### Advanced Alpha Candy

Advanced Alpha Candy guarantees the Alpha transformation, raises the Pokémon's level by one, maximizes its friendship, and applies the Advanced Alpha mark. Pokémon with this mark emit violet particles from their eyes. It also sets **Attack** and **Special Attack** IVs to 31 and raises the other IVs to random values from 20 to 31. Existing IVs are never lowered.

### Purifying Candy

Purifying Candy removes Alpha status and the Advanced Alpha mark. Configuration controls whether it rewards Alpha Essence, the reward range, and the healing amount.

### Iotas integration

When Iotas is installed, the mod adds compatible Iota candy variants. These can apply or remove Iota state and handle its marks, scale, and movement speed alongside Alpha transformations. Advanced Iota Candy sets **Speed** IV to 31, raises **Defense** and **Special Defense** IVs to random values from 20 to 31, and raises **HP**, **Attack**, and **Special Attack** IVs to random values from 10 to 20. Existing IVs are never lowered. Iotas is optional.

### Sounds

Successful transformations use the level-up sound. Purification and failed regular candy attempts use the fire-extinguish sound.

## Recipes

Recipes are prepared in Cobblemon's Cooking Pot.

### Alpha Candy

- Sugar
- EXP Candy S
- Tamato Berry
- Optional Alpha Essence seasoning (up to 3 occupied slots)

### Purifying Candy

- Moomoo Milk
- Aspear Berry
- Pecha Berry
- Rawst Berry
- Lum Berry
- Alpha Candy

## Configuration

The mod writes its settings to `config/alphacandy/main.json`. Settings include:

- Base Alpha conversion chance and boost per Essence level
- Maximum Essence level
- Species blacklist
- Purifying Candy Essence reward toggle and minimum/maximum reward
- Healing amount when using a candy (0 disables healing)

## Building from source

Use Java 21 and the included Gradle wrapper. From the repository root:

```powershell
.\gradlew.bat clean build
```

The platform jars are produced in `fabric/build/libs/` and `neoforge/build/libs/`.

## License

Alpha Candy is distributed under the **Alpha Candy Non-Commercial License**. See the repository's license file for the full terms.

## Version

**Alpha Candy 1.2.2**
