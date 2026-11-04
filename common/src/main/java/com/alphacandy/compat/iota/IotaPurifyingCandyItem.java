package com.alphacandy.compat.iota;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * EN: Purifying candy item that removes Iota state, restores attributes, and returns essence in Survival mode.
 * ES: Caramelo purificador que elimina el estado Iota, restaura atributos y devuelve esencia en modo Supervivencia.
 */
public class IotaPurifyingCandyItem extends Item {

    /** EN: Creates the item that removes Iota state. / ES: Crea el objeto que elimina el estado Iota. */
    public IotaPurifyingCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    /** EN: Requires an owned Iota Pokémon, restores its attributes, plays feedback and consumes the candy; Survival also receives Iota Essence. / ES: Requiere un Pokémon Iota propio, restaura sus atributos, reproduce sonido y consume el caramelo; en Supervivencia entrega esencia Iota. */
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        // EN: Ignore client-side prediction and non-Pokémon entities.
        // ES: Ignora la predicción del cliente y entidades que no sean Pokémon.
        if (!(target instanceof PokemonEntity pokemonEntity)
                || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();

        // EN: Require the player to be the owner of the Pokémon.
        // ES: Requiere que el jugador sea el dueño del Pokémon.
        if (pokemon.getOwnerUUID() == null
                || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        // EN: Leave the candy untouched when the target has no Iota state. / ES: Conserva el caramelo si el objetivo no tiene estado Iota.
        if (!IotaCompat.isIota(pokemon)) {
            return InteractionResult.FAIL;
        }

        // EN: Remove Iota state and restore original scale and attributes.
        // ES: Elimina el estado Iota y restaura la escala y atributos originales.
        IotaCompat.removeIota(pokemonEntity);

        // EN: Play level-up sound as positive purification feedback.
        // ES: Reproduce sonido de nivelación como confirmación de purificación.
        player.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.NEUTRAL,
                0.8f,
                1.0f
        );

        // EN: Consume one candy outside Creative mode and refund one Iota Essence.
        // ES: Consume un caramelo fuera de Creativo y devuelve una Esencia Iota.
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);

            ItemStack essence = new ItemStack(
                    AlphaCandyMod.IOTA_ESSENCE.get()
            );

            if (!player.addItem(essence)) {
                player.drop(essence, false);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
