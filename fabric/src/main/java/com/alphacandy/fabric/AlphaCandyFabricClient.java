package com.alphacandy.fabric;

import com.alphacandy.client.AdvancedAlphaEyeLayer;
import com.alphacandy.client.AdvancedAlphaEyeParticles;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.minecraft.world.entity.EntityType;

public final class AlphaCandyFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AdvancedAlphaEyeParticles.register();
        LivingEntityFeatureRendererRegistrationCallback.EVENT.register((type, renderer, helper, context) -> {
            if (renderer instanceof PokemonRenderer pokemonRenderer) {
                @SuppressWarnings({"rawtypes", "unchecked"})
                var parent = (net.minecraft.client.renderer.entity.RenderLayerParent) pokemonRenderer;
                helper.register(new AdvancedAlphaEyeLayer(parent));
            }
        });
    }
}
