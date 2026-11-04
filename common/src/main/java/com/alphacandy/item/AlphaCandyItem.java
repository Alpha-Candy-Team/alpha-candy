package com.alphacandy.item;

import com.alphacandy.AlphaCandyMod;
import com.alphacandy.config.ModConfig;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * EN: Chance-based Alpha candy item that attempts to transform a Pokémon into an Alpha variant.
 * ES: Caramelo Alfa basado en probabilidad que intenta transformar un Pokémon en variante Alfa.
 */
public final class AlphaCandyItem extends Item {
    // EN: Component key written by the cooking seasoning processor. / ES: Clave escrita por el procesador de condimentos.
    private static final String ESSENCE_LEVEL = "alphacandy:essence_level";

    /** EN: Creates the item with the supplied Minecraft properties. / ES: Crea el objeto con las propiedades de Minecraft recibidas. */
    public AlphaCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    /**
     * EN: Applies a chance-based Alpha conversion after checking server side, target type,
     * ownership, blacklist and existing Alpha status. Plays success/failure feedback and consumes one candy outside Creative.
     * ES: Intenta convertir al Pokémon en Alfa tras validar servidor, tipo, propietario,
     * lista de especies y estado Alfa. Reproduce el resultado y consume un caramelo fuera de Creativo.
     */
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        // EN: Ignore unrelated entities and let the client wait for server authority.
        // ES: Ignora entidades ajenas y deja que el servidor determine el resultado.
        if (!(target instanceof PokemonEntity pokemonEntity) || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();

        // EN: Only the Pokémon owner may use this item on it.
        // ES: Solo el propietario puede usar este objeto sobre su Pokémon.
        if (pokemon.getOwnerUUID() == null || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        // EN: Disallowed species and already-Alpha Pokémon are not changed.
        // ES: No se modifican las especies bloqueadas ni los Pokémon que ya son Alfa.
        if (pokemon.isAlpha() || ModConfig.isSpeciesBlacklisted(pokemon)) {
            return InteractionResult.FAIL;
        }

        // EN: Essence stored in the crafted stack increases the configured base chance.
        // ES: La esencia guardada en el objeto cocinado aumenta la probabilidad base configurada.
        int essenceLevel = getEssenceLevel(stack);

        double chance = stack.is(AlphaCandyMod.ALPHA_ADVANCED_CANDY.get())
                ? 1.0
                : Math.min(1.0, ModConfig.defaultCandyAlphaChance + essenceLevel * ModConfig.essenceBoost);

        // EN: A single random roll determines conversion; play distinct sounds for each outcome.
        // ES: Una tirada aleatoria determina la conversión; cada resultado tiene un sonido distinto.
        if (player.level().random.nextDouble() <= chance) {
            pokemon.setAlpha(true);

            player.level().playSound(
                    null,
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    SoundEvents.PLAYER_LEVELUP,
                    SoundSource.NEUTRAL,
                    0.8f,
                    0.8f
            );
        } else {
            player.level().playSound(
                    null,
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.NEUTRAL,
                    0.5f,
                    1.2f
            );
        }

        // EN: Creative players keep the item; other players consume one.
        // ES: En Creativo se conserva el objeto; en los demás modos se consume uno.
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * EN: Reads the custom data component and clamps its essence level to 0..3; missing data means level zero.
     * ES: Lee el componente de datos y limita la esencia a 0..3; si falta, devuelve nivel cero.
     */
    private static int getEssenceLevel(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        if (customData == null) {
            return 0;
        }

        CompoundTag tag = customData.copyTag();
        return Math.max(0, Math.min(3, tag.getInt(ESSENCE_LEVEL)));
    }
}
