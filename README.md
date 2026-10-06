Alpha Candy

Small Cobblemon addon for Minecraft 1.21.1 that adds a chance-based Alpha Candy system using Cobblemon's Cooking Pot seasoning system.

Development target
Minecraft 1.21.1
Java 21
Cobblemon 1.8.1
Fabric + NeoForge
Architectury
Features
Alpha Candy

Alpha Candy can be crafted in the Cobblemon Cooking Pot.

The candy has an internal Essence level that is determined when it is crafted:

Alpha Essence	Alpha chance
0	1%
1	33%
2	65%
3	97%

The default values can be changed through the mod configuration.

The Essence level is stored directly in the Alpha Candy, so the candy keeps its configured chance after being crafted, moved, stored, or reloaded.

Alpha Essence

Alpha Essence is registered as a Cobblemon Cooking Pot seasoning.

Each occupied seasoning slot containing Alpha Essence adds 1 Essence level.
The amount of items in a slot does not affect the level.
A stack of 64 Alpha Essence in one slot counts as 1 Essence.
A maximum of 3 Essence levels can be applied to a candy.
Each occupied Essence slot consumes one Alpha Essence when the recipe is completed.

Alpha Essence is therefore used during crafting rather than being consumed when the Alpha Candy is used.

Purifying Candy

Purifying Candy removes the Alpha status from a Pokémon and restores its friendship.

In Survival mode, it can also reward Alpha Essence according to the configured settings.

Creative mode does not provide the Survival-mode Essence reward.

Recipes
Alpha Candy

Alpha Candy is prepared in the Cobblemon Cooking Pot using:

Sugar
EXP Candy S
Tamato Berry
Optional Alpha Essence seasoning

The number of Alpha Essence seasoning slots determines the Essence level stored in the resulting candy.

Purifying Candy

Purifying Candy is prepared in the Cobblemon Cooking Pot using:

Moomoo Milk
Aspear Berry
Pecha Berry
Rawst Berry
Lum Berry
Alpha Candy
Configuration

The mod includes configurable values for the Alpha Candy system, including:

Base Alpha chance
Chance increase per Alpha Essence
Maximum Essence level
Purifying Candy Essence rewards
Purifying Candy healing behaviour

The default Alpha chance configuration is:

Base chance: 1%
Essence boost: +32% per Essence
Maximum Essence level: 3

This results in:

0 Essence → 1%
1 Essence → 33%
2 Essence → 65%
3 Essence → 97%
Compatibility

This project is specifically targeted at:

Minecraft 1.21.1
Cobblemon 1.8.1

Both Fabric and NeoForge are supported.

The Cobblemon integration is kept in a small, dedicated area where possible so future Cobblemon versions can be supported through separate compatibility changes rather than assuming that one version will work with every Cobblemon API revision.

Version

Current version:

Alpha Candy 1.0.0

Target:

Minecraft 1.21.1 / Cobblemon 1.8.1