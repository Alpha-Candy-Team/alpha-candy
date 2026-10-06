package com.alphacandy.item;

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

public final class AlphaCandyItem extends Item {
    private static final String ESSENCE_LEVEL = "alphacandy:essence_level";

    public AlphaCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!(target instanceof PokemonEntity pokemonEntity) || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();

        if (pokemon.getOwnerUUID() == null || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        String speciesId = pokemon.getSpecies().getResourceIdentifier().toString();
        if (ModConfig.speciesBlacklist.contains(speciesId)) {
            return InteractionResult.FAIL;
        }

        if (pokemon.isAlpha()) {
            return InteractionResult.FAIL;
        }

        int essenceLevel = getEssenceLevel(stack);

        double chance = Math.min(1.0,
                ModConfig.defaultCandyAlphaChance + essenceLevel * ModConfig.essenceBoost);

        if (player.level().random.nextDouble() <= chance) {
            pokemon.setAlpha(true);

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

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    private static int getEssenceLevel(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);

        if (customData == null) {
            return 0;
        }

        CompoundTag tag = customData.copyTag();
        return Math.max(0, Math.min(3, tag.getInt(ESSENCE_LEVEL)));
    }
}
