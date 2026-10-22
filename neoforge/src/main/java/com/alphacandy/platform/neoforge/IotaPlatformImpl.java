package com.alphacandy.platform.neoforge;

import net.neoforged.fml.ModList;

public final class IotaPlatformImpl {

    private IotaPlatformImpl() {
    }

    public static boolean isIotasLoaded() {
        return ModList.get().isLoaded("cobblemon_iotas");
    }
}
