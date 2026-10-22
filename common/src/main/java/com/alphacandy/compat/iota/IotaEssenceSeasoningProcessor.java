package com.alphacandy.compat.iota;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class IotaEssenceSeasoningProcessor implements SeasoningProcessor {

    public static final IotaEssenceSeasoningProcessor INSTANCE =
            new IotaEssenceSeasoningProcessor();

    private static final String TYPE = "alphacandy:iota_essence_chance";
    private static final String ESSENCE_LEVEL = "alphacandy:iota_essence_level";

    private IotaEssenceSeasoningProcessor() {
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void apply(ItemStack result, List<ItemStack> seasoning) {
        int essenceLevel = 0;

        for (ItemStack stack : seasoning) {
            if (stack.is(AlphaCandyMod.IOTA_ESSENCE.get())) {
                essenceLevel++;
            }
        }

        essenceLevel = Math.min(3, essenceLevel);

        CompoundTag tag = new CompoundTag();
        tag.putInt(ESSENCE_LEVEL, essenceLevel);

        result.set(
                DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.of(tag)
        );
    }

    @Override
    public boolean consumesItem(ItemStack seasoning) {
        return seasoning.is(AlphaCandyMod.IOTA_ESSENCE.get());
    }
}
