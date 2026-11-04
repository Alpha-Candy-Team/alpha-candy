package com.alphacandy.compat.iota;

import com.alphacandy.config.ModConfig;
import com.cobblemon.mod.common.api.mark.Mark;
import com.cobblemon.mod.common.api.mark.Marks;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
 * EN: Guaranteed, deterministic advanced Iota candy that transforms a Pokémon into an Iota variant.
 * ES: Caramelo Iota avanzado garantizado y determinista que transforma a un Pokémon en variante Iota.
 */
public final class IotaAdvancedCandyItem extends Item {

    // EN: Records whether this candy added Mini so purification can preserve naturally existing marks. / ES: Registra si este caramelo añadió Mini para conservar marcas que ya existían.
    public static final String ADDED_MINI =
            "alphacandy_iota_advanced_added_mini";

    /** EN: Identifier for Cobblemon's Mini mark. / ES: Identificador de la marca Mini de Cobblemon. */
    public static final ResourceLocation MINI_MARK_ID =
            ResourceLocation.parse("cobblemon:mark_mini");

    /** EN: Creates the deterministic advanced Iota candy. / ES: Crea el caramelo Iota avanzado de efecto garantizado. */
    public IotaAdvancedCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    /**
     * EN: Validates target, Iota support, ownership and eligibility; temporarily adds Mini when absent,
     * applies Iota, rolls back that mark on failure, then consumes the item on success.
     * ES: Valida objetivo, compatibilidad Iota, propietario y elegibilidad; añade Mini si falta,
     * aplica Iota, revierte la marca si falla y consume el objeto si tiene éxito.
     */
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        // EN: Ignore client-side prediction and non-Pokémon targets.
        // ES: Ignora la predicción del cliente y los objetivos que no sean Pokémon.
        if (!(target instanceof PokemonEntity entity)
                || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = entity.getPokemon();

        // EN: This advanced variant requires Cobblemon's Iota feature and mark. / ES: Esta variante avanzada requiere la característica y marca Iota de Cobblemon.
        if (!IotaCompat.isIotaDataAvailable(pokemon)) {
            return InteractionResult.FAIL;
        }

        // EN: Prevent use by players who do not own this Pokémon.
        // ES: Impide el uso a jugadores que no sean propietarios del Pokémon.
        if (pokemon.getOwnerUUID() == null
                || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        // EN: Do not add another Iota state or change disallowed species. / ES: No añade otro estado Iota ni modifica especies bloqueadas.
        if (IotaCompat.isIota(pokemon) || ModConfig.isSpeciesBlacklisted(pokemon)) {
            return InteractionResult.FAIL;
        }

        Mark miniMark = Marks.getByIdentifier(MINI_MARK_ID);

        if (miniMark == null) {
            return InteractionResult.FAIL;
        }

        // EN: Check if Mini existed beforehand using direct iteration to avoid stream overhead.
        // ES: Comprueba si Mini ya existía mediante iteración directa para evitar la sobrecarga de streams.
        boolean alreadyMini = false;
        for (Mark mark : pokemon.getMarks()) {
            if (MINI_MARK_ID.equals(mark.getIdentifier())) {
                alreadyMini = true;
                break;
            }
        }

        CompoundTag data = pokemon.getPersistentData();

        // EN: Attach Mini mark as artificial only if the Pokémon did not already have it.
        // ES: Registra Mini como artificial solo si el Pokémon no la tenía previamente.
        if (!alreadyMini) {
            Set<Mark> marks = new HashSet<>(pokemon.getMarks());
            marks.add(miniMark);
            pokemon.setMarks(marks);
            data.putBoolean(ADDED_MINI, true);
        }

        // EN: Deterministic transformation: guaranteed success with no random chance roll.
        // ES: Transformación determinista: éxito garantizado sin tirada de probabilidad aleatoria.
        IotaCompat.makeIota(entity);

        // EN: Undo this candy's Mini mark if Iota conversion did not take effect. / ES: Revierte la marca Mini añadida por el caramelo si no se aplicó la conversión Iota.
        if (!IotaCompat.isIota(pokemon)) {
            // EN: Roll back Mini if the transformation could not be completed.
            // ES: Revertir Mini si la transformación no pudo completarse.
            if (!alreadyMini) {
                Set<Mark> marks = new HashSet<>(pokemon.getMarks());
                marks.removeIf(mark ->
                        MINI_MARK_ID.equals(mark.getIdentifier()));
                pokemon.setMarks(marks);
                data.remove(ADDED_MINI);
                pokemon.updateAspects();
            }

            return InteractionResult.FAIL;
        }

        // EN: Play dramatic sound effect upon successful transformation.
        // ES: Reproduce un efecto de sonido tras completar la transformación con éxito.
        player.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.ENDER_DRAGON_GROWL,
                SoundSource.NEUTRAL,
                0.8f,
                1.0f
        );

        // EN: Consume one candy outside Creative mode.
        // ES: Consume un caramelo fuera del modo Creativo.
        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
