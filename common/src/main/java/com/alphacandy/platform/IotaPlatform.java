package com.alphacandy.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

public final class IotaPlatform {

    private IotaPlatform() {
    }

    @ExpectPlatform
    public static boolean isIotasLoaded() {
        throw new AssertionError();
    }
}
