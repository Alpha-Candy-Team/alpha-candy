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

public class IotaPurifyingCandyItem extends Item {

    public IotaPurifyingCandyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(
            ItemStack stack,
            Player player,
            LivingEntity target,
            InteractionHand hand
    ) {
        if (!(target instanceof PokemonEntity pokemonEntity)
                || player.level().isClientSide()) {
            return InteractionResult.PASS;
        }

        Pokemon pokemon = pokemonEntity.getPokemon();

        if (pokemon.getOwnerUUID() == null
                || !pokemon.getOwnerUUID().equals(player.getUUID())) {
            return InteractionResult.FAIL;
        }

        if (!IotaCompat.isIota(pokemon)) {
            return InteractionResult.FAIL;
        }

        IotaCompat.removeIota(pokemonEntity);

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
