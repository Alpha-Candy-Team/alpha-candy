package com.alphacandy.fabric;

import com.alphacandy.AlphaCandyMod;
import net.fabricmc.api.ModInitializer;

/**
 * EN: Fabric loader entrypoint that delegates initialization to common mod logic.
 * ES: Punto de entrada del cargador Fabric que delega la inicialización a la lógica común del mod.
 */
public final class AlphaCandyFabric implements ModInitializer {
    @Override
    /** EN: Entrypoint called by Fabric; delegates setup to common mod logic. / ES: Punto de entrada del cargador Fabric que delega la preparación al código común. */
    public void onInitialize() {
        AlphaCandyMod.init();
    }
}
