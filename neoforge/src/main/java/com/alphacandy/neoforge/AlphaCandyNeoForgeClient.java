package com.alphacandy.neoforge;

import com.alphacandy.AlphaCandyMod;
import com.alphacandy.client.AdvancedAlphaEyeLayer;
import com.alphacandy.client.AdvancedAlphaEyeParticles;
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = AlphaCandyMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class AlphaCandyNeoForgeClient {
    private AlphaCandyNeoForgeClient() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(AdvancedAlphaEyeParticles::register);
    }

    @SubscribeEvent
    public static void addPokemonLayer(EntityRenderersEvent.AddLayers event) {
        for (var type : event.getEntityTypes()) {
            var renderer = event.getRenderer(type);
            if (renderer instanceof PokemonRenderer pokemonRenderer) {
                @SuppressWarnings({"rawtypes", "unchecked"})
                var parent = (RenderLayerParent) pokemonRenderer;
                pokemonRenderer.addLayer(new AdvancedAlphaEyeLayer(parent));
            }
        }
    }
}
