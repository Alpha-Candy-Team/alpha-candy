package com.alphacandy;

import com.alphacandy.compat.iota.IotaAdvancedCandyItem;
import com.alphacandy.compat.iota.IotaAdvancedEssenceSeasoningProcessor;
import com.alphacandy.compat.iota.IotaCandyItem;
import com.alphacandy.compat.iota.IotaEssenceItem;
import com.alphacandy.compat.iota.IotaEssenceSeasoningProcessor;
import com.alphacandy.compat.iota.IotaPurifyingCandyItem;
import com.alphacandy.config.ModConfig;
import com.alphacandy.cooking.AlphaAdvancedEssenceSeasoningProcessor;
import com.alphacandy.cooking.AlphaEssenceSeasoningProcessor;
import com.alphacandy.creative.AlphaCandyCreativeTab;
import com.alphacandy.item.AlphaAdvancedCandyItem;
import com.alphacandy.item.AlphaCandyItem;
import com.alphacandy.item.AlphaEssenceItem;
import com.alphacandy.item.PurifyingCandyItem;
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
            ITEMS.register("alpha_essence",
                    () -> new AlphaEssenceItem(new Item.Properties()));

    public static final RegistrySupplier<Item> ALPHA_CANDY =
            ITEMS.register("alpha_candy",
                    () -> new AlphaCandyItem(new Item.Properties()));

    public static final RegistrySupplier<Item> ALPHA_ADVANCED_CANDY =
            ITEMS.register("alpha_advanced_candy",
                    () -> new AlphaAdvancedCandyItem(new Item.Properties()));

    public static final RegistrySupplier<Item> PURIFYING_CANDY =
            ITEMS.register("purifying_candy",
                    () -> new PurifyingCandyItem(new Item.Properties()));

    public static RegistrySupplier<Item> IOTA_ESSENCE;
    public static RegistrySupplier<Item> IOTA_CANDY;
    public static RegistrySupplier<Item> IOTA_PURIFYING_CANDY;
    public static RegistrySupplier<Item> IOTA_ADVANCED_CANDY;

    private AlphaCandyMod() {
    }

    public static void init() {
        ModConfig.load();

        IOTA_ESSENCE = ITEMS.register(
                "iota_essence",
                () -> new IotaEssenceItem(new Item.Properties())
        );

        IOTA_CANDY = ITEMS.register(
                "iota_candy",
                () -> new IotaCandyItem(new Item.Properties())
        );

        IOTA_PURIFYING_CANDY = ITEMS.register(
                "iota_purifying_candy",
                () -> new IotaPurifyingCandyItem(new Item.Properties())
        );

        IOTA_ADVANCED_CANDY = ITEMS.register(
                "iota_advanced_candy",
                () -> new IotaAdvancedCandyItem(new Item.Properties())
        );

        SeasoningProcessor.Companion.getProcessors().put(
                AlphaAdvancedEssenceSeasoningProcessor.INSTANCE.getType(),
                AlphaAdvancedEssenceSeasoningProcessor.INSTANCE
        );

        SeasoningProcessor.Companion.getProcessors().put(
                AlphaEssenceSeasoningProcessor.INSTANCE.getType(),
                AlphaEssenceSeasoningProcessor.INSTANCE
        );

        SeasoningProcessor.Companion.getProcessors().put(
                IotaAdvancedEssenceSeasoningProcessor.INSTANCE.getType(),
                IotaAdvancedEssenceSeasoningProcessor.INSTANCE
        );

        SeasoningProcessor.Companion.getProcessors().put(
                IotaEssenceSeasoningProcessor.INSTANCE.getType(),
                IotaEssenceSeasoningProcessor.INSTANCE
        );

        ITEMS.register();
        AlphaCandyCreativeTab.register();
    }
}
