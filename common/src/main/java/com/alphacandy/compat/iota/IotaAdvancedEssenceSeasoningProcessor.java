package com.alphacandy.compat.iota;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import java.util.List;

public final class IotaAdvancedEssenceSeasoningProcessor implements SeasoningProcessor {
    public static final IotaAdvancedEssenceSeasoningProcessor INSTANCE =
            new IotaAdvancedEssenceSeasoningProcessor();

    private static final String TYPE = "alphacandy:iota_advanced_essence";
    private static final String ESSENCE_LEVEL = "alphacandy:iota_essence_level";

    private IotaAdvancedEssenceSeasoningProcessor() {}

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public void apply(ItemStack result, List<ItemStack> seasoning) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(ESSENCE_LEVEL, 3);
        result.set(DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.of(tag));
    }

    @Override
    public boolean consumesItem(ItemStack seasoning) {
        return AlphaCandyMod.IOTA_ESSENCE != null
                && seasoning.is(AlphaCandyMod.IOTA_ESSENCE.get());
    }
}
