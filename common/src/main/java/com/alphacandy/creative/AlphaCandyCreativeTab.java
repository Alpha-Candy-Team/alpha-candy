package com.alphacandy.creative;

import com.alphacandy.AlphaCandyMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public final class AlphaCandyCreativeTab {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(AlphaCandyMod.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> ALPHA_CANDY_TAB =
            TABS.register("alpha_candy", () -> CreativeModeTab.builder(
                    CreativeModeTab.Row.TOP,
                    0
            )
                    .title(Component.translatable("itemGroup.alphacandy.alpha_candy"))
                    .icon(() -> new ItemStack(AlphaCandyMod.ALPHA_CANDY.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(AlphaCandyMod.ALPHA_CANDY.get());
                        output.accept(AlphaCandyMod.alpha_essence.get());
                        output.accept(AlphaCandyMod.PURIFYING_CANDY.get());
                    })
                    .build());

    private AlphaCandyCreativeTab() {
    }

    public static void register() {
        TABS.register();
    }
}