package com.alphacandy.compat.iota;

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
 * EN: Chance-based Iota candy item that attempts to transform a Pokémon into an Iota variant.
 * ES: Caramelo Iota basado en probabilidad que intenta transformar un Pokémon en variante Iota.
 */
public final class IotaCandyItem extends Item {

    // EN: Custom-data key populated by the Iota seasoning processor. / ES: Clave de datos personalizados escrita por el procesador de condimentos Iota.
    private static final String ESSENCE_LEVEL = "alphacandy:iota_essence_level";

    /** EN: Creates the chance-based Iota candy. / ES: Crea el caramelo Iota basado en probabilidad. */
    public IotaCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    /**
     * EN: Checks server side, Iota support, ownership and eligibility; rolls the configured chance,
     * applies Iota on success, plays feedback, and consumes one item outside Creative.
     * ES: Comprueba servidor, compatibilidad Iota, propietario y elegibilidad; calcula la probabilidad,
     * aplica Iota si tiene éxito, reproduce sonido y consume un objeto fuera de Creativo.
     */
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        // EN: Ignore client-side prediction and non-Pokémon entities.
        // ES: Ignora la predicción del cliente y entidades que no sean Pokémon.
        if (!(target instanceof PokemonEntity pokemonEntity) || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();

        // EN: Feature data must be available before accessing or changing Iota state. / ES: Los datos de característica deben estar disponibles antes de leer o cambiar Iota.
        if (!IotaCompat.isIotaDataAvailable(pokemon)) {
            return InteractionResult.FAIL;
        }

        // EN: Only the owner of the Pokémon may use this candy on it.
        // ES: Solo el propietario del Pokémon puede usar este caramelo sobre él.
        if (pokemon.getOwnerUUID() == null || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        // EN: A Pokémon already transformed into an Iota or on the blacklist cannot be transformed.
        // ES: Un Pokémon ya transformado en Iota o en la lista negra no puede transformarse.
        if (IotaCompat.isIota(pokemon) || ModConfig.isSpeciesBlacklisted(pokemon)) {
            return InteractionResult.FAIL;
        }

        // EN: Essence levels raise the base chance; cap the final probability at one. / ES: La esencia aumenta la probabilidad base; el resultado se limita a uno.
        int essenceLevel = getEssenceLevel(stack);

        double chance = stack.is(AlphaCandyMod.IOTA_ADVANCED_CANDY.get())
                ? 1.0
                : Math.min(1.0, ModConfig.defaultCandyAlphaChance + essenceLevel * ModConfig.essenceBoost);

        // EN: Roll random chance to determine if transformation succeeds.
        // ES: Realiza una tirada aleatoria para determinar si la transformación tiene éxito.
        if (player.level().random.nextDouble() <= chance) {
            // EN: Apply Iota status and play success sound.
            // ES: Aplica el estado Iota y reproduce el sonido de éxito.
            IotaCompat.makeIota(pokemonEntity);

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
            // EN: Play failure feedback sound when conversion fails.
            // ES: Reproduce sonido de fallo cuando la conversión no tiene éxito.
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

        // EN: Consume one candy outside Creative mode.
        // ES: Consume un caramelo fuera del modo Creativo.
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    /**
     * EN: Reads the essence level from the stack custom data, clamping it to 0..3 (defaults to 0 if absent).
     * ES: Lee el nivel de esencia guardado en los datos del objeto, limitándolo a 0..3 (por defecto 0 si falta).
     */
    private static int getEssenceLevel(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        if (customData == null) {
            return 0;
        }

        CompoundTag tag = customData.copyTag();

        return Math.max(
                0,
                Math.min(3, tag.getInt(ESSENCE_LEVEL))
        );
    }
}
