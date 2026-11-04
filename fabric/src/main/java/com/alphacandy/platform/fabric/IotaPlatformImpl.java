package com.alphacandy.platform.fabric;

import net.fabricmc.loader.api.FabricLoader;

/**
 * EN: Fabric-specific implementation for checking if Cobblemon Iotas is loaded.
 * ES: Implementación específica para Fabric para comprobar si Cobblemon Iotas está cargado.
 */
public final class IotaPlatformImpl {

    /** EN: Utility class; instances are unnecessary. / ES: Clase auxiliar que no necesita instancias. */
    private IotaPlatformImpl() {
    }

    /** EN: Uses Fabric Loader to detect the Iotas mod ID. / ES: Usa Fabric Loader para detectar el ID del mod Iotas. */
    public static boolean isIotasLoaded() {
        return FabricLoader.getInstance().isModLoaded("cobblemon-iotas");
    }
}
