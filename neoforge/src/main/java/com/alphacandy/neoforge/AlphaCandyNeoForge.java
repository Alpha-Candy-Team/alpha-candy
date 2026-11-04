package com.alphacandy.neoforge;

import com.alphacandy.AlphaCandyMod;
import net.neoforged.fml.common.Mod;

/**
 * EN: NeoForge loader entrypoint that delegates initialization to common mod logic.
 * ES: Punto de entrada del cargador NeoForge que delega la inicialización a la lógica común del mod.
 */
@Mod(AlphaCandyMod.MOD_ID)
public final class AlphaCandyNeoForge {

    /** EN: NeoForge constructs this class to start the shared mod initialization. / ES: NeoForge crea esta clase para iniciar la inicialización común del mod. */
    public AlphaCandyNeoForge() {
        AlphaCandyMod.init();
    }
}
