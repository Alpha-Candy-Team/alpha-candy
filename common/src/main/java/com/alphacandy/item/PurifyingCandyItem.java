package com.alphacandy.item;

import com.alphacandy.AlphaCandyMod;
import com.alphacandy.config.ModConfig;
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

public final class PurifyingCandyItem extends Item {
    public PurifyingCandyItem(Properties properties) {
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

        if (!pokemon.isAlpha()) {
            return InteractionResult.FAIL;
        }

        pokemon.setAlpha(false);
        pokemon.setFriendship(250, true);

        if (ModConfig.rewardEssenceOnPurifyingCandy && !player.getAbilities().instabuild) {
            int amount = ModConfig.purifyingCandyEssenceMin;
            ItemStack essenceStack = new ItemStack(AlphaCandyMod.alpha_essence.get(), amount);

            if (!player.getInventory().add(essenceStack)) {
                player.drop(essenceStack, false);
            }
        }

        if (ModConfig.alphaCandyHealAmount > 0) {
            pokemonEntity.heal(ModConfig.alphaCandyHealAmount);
        }

        player.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 0.6f, 1.2f);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }
}
