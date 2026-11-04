package com.alphacandy.platform.neoforge;

import net.neoforged.fml.ModList;

/**
 * EN: NeoForge-specific implementation for checking if Cobblemon Iotas is loaded.
 * ES: Implementación específica para NeoForge para comprobar si Cobblemon Iotas está cargado.
 */
public final class IotaPlatformImpl {

    /** EN: Utility class; instances are unnecessary. / ES: Clase auxiliar que no necesita instancias. */
    private IotaPlatformImpl() {
    }

    /** EN: Uses NeoForge's mod list to detect the Iotas mod ID. / ES: Usa la lista de mods de NeoForge para detectar el ID de Iotas. */
    public static boolean isIotasLoaded() {
        return ModList.get().isLoaded("cobblemon_iotas");
    }
}
