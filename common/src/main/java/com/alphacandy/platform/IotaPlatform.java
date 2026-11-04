package com.alphacandy.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * EN: Architectury platform abstraction for detecting optional mod integrations like Cobblemon Iotas.
 * ES: Abstracción de plataforma Architectury para detectar integraciones opcionales como Cobblemon Iotas.
 */
public final class IotaPlatform {

    /** EN: Utility facade implemented separately by each loader. / ES: Fachada auxiliar implementada por separado en cada cargador. */
    private IotaPlatform() {
    }

    @ExpectPlatform
    /** EN: Architectury replaces this body with the active loader implementation. / ES: Architectury sustituye este cuerpo por la implementación del cargador activo. */
    public static boolean isIotasLoaded() {
        throw new AssertionError();
    }
}
