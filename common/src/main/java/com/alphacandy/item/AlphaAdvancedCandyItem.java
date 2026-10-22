package com.alphacandy.item;

import com.alphacandy.config.ModConfig;
import com.alphacandy.compat.iota.IotaCompat;
import com.cobblemon.mod.common.api.mark.Mark;
import com.cobblemon.mod.common.api.mark.Marks;
import com.cobblemon.mod.common.api.pokemon.experience.CandyExperienceSource;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
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

public final class AlphaAdvancedCandyItem extends Item {

    public static final String ACTIVE =
            "alphacandy_advanced_alpha_active";
    public static final String ORIGINAL_SCALE =
            "alphacandy_advanced_alpha_original_scale";

    private static final ResourceLocation ADVANCED_ALPHA_MARK_ID =
            ResourceLocation.parse("alphacandy:mark_advanced_alpha");

    public AlphaAdvancedCandyItem(Properties properties) {
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

        if (pokemon.getOwnerUUID() == null
                || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        String speciesId = pokemon.getSpecies()
                .getResourceIdentifier().toString();

        if (ModConfig.speciesBlacklist.contains(speciesId)
                || pokemon.isAlpha()) {
            return InteractionResult.FAIL;
        }

        Mark advancedAlphaMark =
                Marks.getByIdentifier(ADVANCED_ALPHA_MARK_ID);

        if (advancedAlphaMark == null) {
            return InteractionResult.FAIL;
        }

        CompoundTag data = pokemon.getPersistentData();

        pokemon.setAlpha(true);

        // Preserve the scale data required by the existing purifier/Iota compatibility.
        boolean iotaWasActive = IotaCompat.isIota(pokemon);
        float originalScale = iotaWasActive
                ? IotaCompat.getOriginalScale(pokemon)
                : pokemon.getScaleModifier();

        data.putFloat(ORIGINAL_SCALE, originalScale);
        data.putBoolean(ACTIVE, true);

        // Only manage the custom Advanced Alpha mark.
        Set<Mark> marks = new HashSet<>(pokemon.getMarks());
        if (marks.add(advancedAlphaMark)) {
            pokemon.setMarks(marks);
        }
        pokemon.setActiveMark(advancedAlphaMark);
        pokemon.updateAspects();

        // Maximum friendship and one level, unless already at the configured cap.
        pokemon.setFriendship(250, true);
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.FAIL;
        }
        if (pokemon.canLevelUpFurther()) {
            pokemon.levelUp(new CandyExperienceSource(serverPlayer, stack));
        }

        player.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.ENDER_DRAGON_GROWL,
                SoundSource.NEUTRAL,
                0.8f,
                0.8f
        );

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}