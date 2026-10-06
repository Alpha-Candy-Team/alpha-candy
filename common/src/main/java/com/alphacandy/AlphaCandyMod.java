package com.alphacandy;

import com.alphacandy.config.ModConfig;
import com.alphacandy.item.AlphaCandyItem;
import com.alphacandy.item.AlphaEssenceItem;
import com.alphacandy.item.PurifyingCandyItem;
import com.alphacandy.cooking.AlphaEssenceSeasoningProcessor;
import com.alphacandy.creative.AlphaCandyCreativeTab;
import com.cobblemon.mod.common.item.crafting.SeasoningProcessor;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public final class AlphaCandyMod {
    public static final String MOD_ID = "alphacandy";

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> alpha_essence =
            ITEMS.register("alpha_essence", () -> new AlphaEssenceItem(new Item.Properties()));

    public static final RegistrySupplier<Item> ALPHA_CANDY =
            ITEMS.register("alpha_candy", () -> new AlphaCandyItem(new Item.Properties()));

    public static final RegistrySupplier<Item> PURIFYING_CANDY =
            ITEMS.register("purifying_candy", () -> new PurifyingCandyItem(new Item.Properties()));

    private AlphaCandyMod() {}

    public static void init() {
        ModConfig.load();

        SeasoningProcessor.Companion.getProcessors().put(
                AlphaEssenceSeasoningProcessor.INSTANCE.getType(),
                AlphaEssenceSeasoningProcessor.INSTANCE
        );

        ITEMS.register();
        AlphaCandyCreativeTab.register();
    }
}
