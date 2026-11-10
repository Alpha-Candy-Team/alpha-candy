package com.alphacandy.client;

import com.cobblemon.mod.common.api.pokemon.aspect.AspectParticleMapKt;
import com.cobblemon.mod.common.api.pokemon.aspect.ParticleData;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

/** Registers Alpha Candy's Snowstorm eye effect with Cobblemon's aspect particle system. */
public final class AdvancedAlphaEyeParticles {
    private static final ResourceLocation PARTICLE = ResourceLocation.fromNamespaceAndPath(
            "alphacandy", "advanced_alpha_eyes");

    private AdvancedAlphaEyeParticles() {}

    public static void register() {
        AspectParticleMapKt.getAspectParticleMap().put("alphacandy:mark_advanced_alpha",
                new ParticleData.SnowstormParticle(PARTICLE, 1.0, 1, locatorStates -> {
                    var eyes = new ArrayList<String>();
                    locatorStates.keySet().stream()
                            .filter(name -> name.toLowerCase(java.util.Locale.ROOT).contains("eye"))
                            .forEach(eyes::add);
                    return eyes;
                }));
    }
}
