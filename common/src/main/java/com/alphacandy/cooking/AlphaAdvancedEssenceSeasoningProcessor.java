package com.alphacandy.cooking;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;

import java.util.List;

public final class AlphaAdvancedEssenceSeasoningProcessor implements SeasoningProcessor {

    public static final AlphaAdvancedEssenceSeasoningProcessor INSTANCE = new AlphaAdvancedEssenceSeasoningProcessor();

    private static final String TYPE = "alphacandy:advanced_essence";
    private static final String ESSENCE_LEVEL = "alphacandy:advanced_essence_level";

    private AlphaAdvancedEssenceSeasoningProcessor() {
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void apply(ItemStack result, List<ItemStack> seasoning) {
        int essenceLevel = 3;

        for (ItemStack stack : seasoning) {
            if (stack.is(AlphaCandyMod.alpha_essence.get())) {
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
        return seasoning.is(AlphaCandyMod.alpha_essence.get());
    }
}
