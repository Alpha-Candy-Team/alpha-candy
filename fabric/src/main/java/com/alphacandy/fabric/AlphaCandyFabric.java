package com.alphacandy.fabric;

import com.alphacandy.AlphaCandyMod;
import net.fabricmc.api.ModInitializer;

public final class AlphaCandyFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AlphaCandyMod.init();
    }
}
