package com.alphacandy.creative;

import com.alphacandy.AlphaCandyMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * EN: Registers and configures the creative mode tab for the Alpha Candy mod.
 * ES: Registra y configura la pestaña del modo creativo para el mod Alpha Candy.
 */
public final class AlphaCandyCreativeTab {

    // EN: Deferred registration binds the tab to the mod namespace and creative-tab registry. / ES: El registro diferido vincula la pestaña al espacio del mod y al registro creativo.
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(
                    AlphaCandyMod.MOD_ID,
                    Registries.CREATIVE_MODE_TAB
            );

    // EN: Defines the translated tab title, icon, and displayed item list. / ES: Define el título traducido, el icono y la lista de objetos mostrados.
    public static final RegistrySupplier<CreativeModeTab> ALPHA_CANDY_TAB =
            TABS.register(
                    "alpha_candy",
                    () -> CreativeModeTab.builder(
                            CreativeModeTab.Row.TOP,
                            0
                    )
                            .title(Component.translatable(
                                    "itemGroup.alphacandy.alpha_candy"
                            ))
                            .icon(() -> new ItemStack(
                                    AlphaCandyMod.ALPHA_CANDY.get()
                            ))
                            .displayItems((parameters, output) -> {
                                output.accept(AlphaCandyMod.ALPHA_CANDY.get());
                                output.accept(AlphaCandyMod.ALPHA_ADVANCED_CANDY.get());
                                output.accept(AlphaCandyMod.alpha_essence.get());
                                output.accept(AlphaCandyMod.PURIFYING_CANDY.get());

                                if (AlphaCandyMod.IOTA_ESSENCE != null
                                        && AlphaCandyMod.IOTA_CANDY != null
                                        && AlphaCandyMod.IOTA_PURIFYING_CANDY != null
                                        && AlphaCandyMod.IOTA_ADVANCED_CANDY != null) {

                                    output.accept(AlphaCandyMod.IOTA_CANDY.get());
                                    output.accept(AlphaCandyMod.IOTA_ADVANCED_CANDY.get());
                                    output.accept(AlphaCandyMod.IOTA_ESSENCE.get());
                                    output.accept(AlphaCandyMod.IOTA_PURIFYING_CANDY.get());
                                }
                            })
                            .build()
            );

    /** EN: Prevents instances of this static registration holder. / ES: Evita instancias de esta clase auxiliar de registro. */
    private AlphaCandyCreativeTab() {
    }

    /** EN: Registers the deferred tab with Minecraft. / ES: Registra la pestaña diferida en Minecraft. */
    public static void register() {
        TABS.register();
    }
}
