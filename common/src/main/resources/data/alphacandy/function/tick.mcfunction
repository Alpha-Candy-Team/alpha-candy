scoreboard players add #ac_eye ac_eye_timer 1

# Run the entity selectors once per second instead of twice per second.
execute if score #ac_eye ac_eye_timer matches 20 run execute as @e[type=cobblemon:pokemon,nbt={Pokemon:{Marks:["alphacandy:mark_advanced_alpha"],PokemonOriginalTrainerType:"NONE"}}] run function alphacandy:eyes
execute if score #ac_eye ac_eye_timer matches 20 run execute as @e[type=cobblemon:pokemon,nbt={Pokemon:{ActiveMark:"alphacandy:mark_advanced_alpha",PokemonOriginalTrainerType:"PLAYER"}}] run function alphacandy:eyes

execute if score #ac_eye ac_eye_timer matches 20 run scoreboard players set #ac_eye ac_eye_timer 0
