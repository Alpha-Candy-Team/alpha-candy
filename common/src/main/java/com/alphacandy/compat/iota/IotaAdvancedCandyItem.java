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

public final class IotaAdvancedCandyItem extends Item {

    private static final String ADDED_MINI =
            "alphacandy_iota_advanced_added_mini";

    private static final ResourceLocation MINI_MARK_ID =
            ResourceLocation.parse("cobblemon:mark_mini");

    public IotaAdvancedCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        if (!(target instanceof PokemonEntity entity)
                || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = entity.getPokemon();

        if (!IotaCompat.isIotaDataAvailable(pokemon)) {
            return InteractionResult.FAIL;
        }

        if (pokemon.getOwnerUUID() == null
                || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        String speciesId = pokemon.getSpecies()
                .getResourceIdentifier().toString();

        if (ModConfig.speciesBlacklist.contains(speciesId)
                || IotaCompat.isIota(pokemon)) {
            return InteractionResult.FAIL;
        }

        Mark miniMark = Marks.getByIdentifier(MINI_MARK_ID);

        if (miniMark == null) {
            return InteractionResult.FAIL;
        }

        boolean alreadyMini = pokemon.getMarks().stream()
                .anyMatch(mark -> MINI_MARK_ID.equals(mark.getIdentifier()));

        CompoundTag data = pokemon.getPersistentData();

        // Registrar Mini como artificial solo si el Pokemon no la tenia.
        if (!alreadyMini) {
            Set<Mark> marks = new HashSet<>(pokemon.getMarks());
            marks.add(miniMark);
            pokemon.setMarks(marks);
            data.putBoolean(ADDED_MINI, true);
        }

        // Transformacion determinista: no hay ninguna tirada de probabilidad.
        IotaCompat.makeIota(entity);

        if (!IotaCompat.isIota(pokemon)) {
            // Revertir Mini si la transformacion no pudo completarse.
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

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}