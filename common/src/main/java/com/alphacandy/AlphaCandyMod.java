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

/**
 * EN: Main entry point and deferred registration holder for the Alpha Candy mod.
 * ES: Punto de entrada principal y contenedor de registros diferidos del mod Alpha Candy.
 */
public final class AlphaCandyMod {

    /** EN: Shared namespace used by item and resource identifiers. / ES: Espacio de nombres común para identificadores de objetos y recursos. */
    public static final String MOD_ID = "alphacandy";

    /** EN: Deferred item registry shared by both loader modules. / ES: Registro diferido de objetos compartido por ambos cargadores. */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MOD_ID, Registries.ITEM);

    /** EN: Registers the seasoning used to add essence to recipes. / ES: Registra el condimento que añade esencia a las recetas. */
    public static final RegistrySupplier<Item> alpha_essence =
            ITEMS.register("alpha_essence",
                    () -> new AlphaEssenceItem(new Item.Properties()));

    /** EN: Registers the chance-based Alpha conversion candy. / ES: Registra el caramelo de conversión Alfa con probabilidad. */
    public static final RegistrySupplier<Item> ALPHA_CANDY =
            ITEMS.register("alpha_candy",
                    () -> new AlphaCandyItem(new Item.Properties()));

    /** EN: Registers the guaranteed, enhanced Alpha candy. / ES: Registra el caramelo Alfa avanzado, que garantiza la conversión. */
    public static final RegistrySupplier<Item> ALPHA_ADVANCED_CANDY =
            ITEMS.register("alpha_advanced_candy",
                    () -> new AlphaAdvancedCandyItem(new Item.Properties()));

    /** EN: Registers the candy that removes Alpha status. / ES: Registra el caramelo que elimina el estado Alfa. */
    public static final RegistrySupplier<Item> PURIFYING_CANDY =
            ITEMS.register("purifying_candy",
                    () -> new PurifyingCandyItem(new Item.Properties()));

    // EN: These suppliers are assigned during init before the deferred registry is committed.
    // ES: Estos proveedores se asignan durante init antes de confirmar el registro diferido.
    public static RegistrySupplier<Item> IOTA_ESSENCE;
    public static RegistrySupplier<Item> IOTA_CANDY;
    public static RegistrySupplier<Item> IOTA_PURIFYING_CANDY;
    public static RegistrySupplier<Item> IOTA_ADVANCED_CANDY;

    /** EN: Prevents instances; this class only exposes static registration state. / ES: Evita instancias; esta clase solo expone registros estáticos. */
    private AlphaCandyMod() {
    }

    /**
     * EN: Loads settings, registers optional Iota variants and all seasoning processors,
     * then commits item registration and adds the creative tab.
     * ES: Carga los ajustes, registra variantes Iota opcionales y procesadores de condimentos;
     * después confirma los objetos y registra la pestaña creativa.
     */
    public static void init() {
        // EN: Make persisted values available before item interactions can occur.
        // ES: Deja disponibles los valores guardados antes de que puedan usarse los objetos.
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

        // EN: Recipe processor identifiers must match the JSON recipe definitions.
        // ES: Los identificadores de procesador deben coincidir con los de las recetas JSON.
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

        // EN: Register all deferred items and creative tab entries with the game.
        // ES: Registra todos los objetos diferidos y entradas de la pestaña creativa en el juego.
        ITEMS.register();
        AlphaCandyCreativeTab.register();
    }
}
