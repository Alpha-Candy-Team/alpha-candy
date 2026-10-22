package com.alphacandy.platform.fabric;

import net.fabricmc.loader.api.FabricLoader;

public final class IotaPlatformImpl {

    private IotaPlatformImpl() {
    }

    public static boolean isIotasLoaded() {
        return FabricLoader.getInstance().isModLoaded("cobblemon-iotas");
    }
}
