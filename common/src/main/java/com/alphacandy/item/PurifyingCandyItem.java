package com.alphacandy.item;

import com.alphacandy.AlphaCandyMod;
import com.alphacandy.compat.iota.IotaCompat;
import com.alphacandy.config.ModConfig;
import com.cobblemon.mod.common.api.mark.Mark;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * EN: Purifying candy item that removes Alpha status, restores attributes/scales, and optionally rewards essence.
 * ES: Caramelo purificador que elimina el estado Alfa, restaura atributos/escalas y opcionalmente entrega esencia.
 */
public final class PurifyingCandyItem extends Item {

    /** EN: Creates the item that removes Alpha status. / ES: Crea el objeto que elimina el estado Alfa. */
    public PurifyingCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    /**
     * EN: Validates ownership and Alpha status, removes Alpha and the custom mark, restores compatible scales,
     * resets friendship, optionally rewards essence/healing, plays feedback and consumes the candy.
     * ES: Valida propietario y estado Alfa, elimina Alfa y la marca, restaura escalas compatibles,
     * restablece amistad, entrega esencia/curación opcionales, reproduce sonido y consume el caramelo.
     */
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        // EN: Only server-side interactions with owned Pokémon can change persistent state. / ES: Solo interacciones del servidor con Pokémon propios cambian datos persistentes.
        if (!(target instanceof PokemonEntity pokemonEntity)
                || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();

        if (pokemon.getOwnerUUID() == null
                || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        if (!pokemon.isAlpha()) {
            return InteractionResult.FAIL;
        }

        // EN: Capture whether Advanced Alpha changed this Pokémon and the scale to restore. / ES: Guarda si estaba activo Alfa avanzado y qué escala debe restaurarse.
        CompoundTag data = pokemon.getPersistentData();
        boolean advanced = data.getBoolean(
                AlphaAdvancedCandyItem.ACTIVE
        );

        float originalScale = data.getFloat(
                AlphaAdvancedCandyItem.ORIGINAL_SCALE
        );

        // EN: Clear Alpha first, then remove only the mod's Advanced Alpha mark. / ES: Primero elimina Alfa y después quita únicamente la marca Alfa avanzado del mod.
        pokemon.setAlpha(false);

        // EN: Remove the custom Advanced Alpha mark and deselect it if active. / ES: Elimina la marca personalizada Alfa Avanzado y la deselecciona si estaba activa.
        Set<Mark> updatedMarks = new HashSet<>(pokemon.getMarks());
        boolean removedAdvancedMark = updatedMarks.removeIf(mark ->
                AlphaAdvancedCandyItem.ADVANCED_ALPHA_MARK_ID.equals(mark.getIdentifier()));

        if (removedAdvancedMark) {
            Mark activeMark = pokemon.getActiveMark();
            if (activeMark != null
                    && AlphaAdvancedCandyItem.ADVANCED_ALPHA_MARK_ID.equals(activeMark.getIdentifier())) {
                pokemon.setActiveMark(null);
            }

            pokemon.setMarks(updatedMarks);
            pokemon.updateAspects();
        }

        // EN: Preserve the existing Iota scale compatibility behavior. / ES: Conserva el comportamiento de compatibilidad con la escala Iota.
        if (IotaCompat.isIota(pokemon)) {
            if (advanced) {
                IotaCompat.updateOriginalScaleAfterAlphaPurification(
                        pokemon, originalScale
                );
            } else {
                IotaCompat.refreshIotaScale(pokemon);
            }
        } else if (advanced) {
            pokemon.setScaleModifier(originalScale);
        }

        // EN: Remove restoration keys after their values have been applied. / ES: Elimina las claves de restauración después de aplicar sus valores.
        if (advanced) {
            data.remove(AlphaAdvancedCandyItem.ACTIVE);
            data.remove(AlphaAdvancedCandyItem.ORIGINAL_SCALE);
        }

        pokemon.setFriendship(250, true);

        // EN: Reward only outside Creative mode; drop the item if inventory insertion fails. / ES: Recompensa solo fuera de Creativo; suelta la esencia si no cabe en el inventario.
        if (ModConfig.rewardEssenceOnPurifyingCandy
                && !player.getAbilities().instabuild) {
            int amount = ModConfig.purifyingCandyEssenceMin;
            ItemStack essenceStack = new ItemStack(
                    AlphaCandyMod.alpha_essence.get(), amount
            );

            if (!player.addItem(essenceStack)) {
                player.drop(essenceStack, false);
            }
        }

        // EN: Apply optional healing to the spawned Pokémon entity. / ES: Aplica la curación opcional a la entidad Pokémon activa.
        if (ModConfig.alphaCandyHealAmount > 0) {
            pokemonEntity.heal(ModConfig.alphaCandyHealAmount);
        }

        // EN: Play level-up sound as positive purification feedback.
        // ES: Reproduce sonido de nivelación como confirmación de purificación.
        player.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.NEUTRAL,
                0.6f,
                1.2f
        );

        // EN: Consume one candy outside Creative mode.
        // ES: Consume un caramelo fuera del modo Creativo.
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
