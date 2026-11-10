package com.alphacandy.client;

import com.alphacandy.item.AlphaAdvancedCandyItem;
import com.cobblemon.mod.common.client.entity.PokemonClientDelegate;
import com.cobblemon.mod.common.client.render.pokemon.AlphaEyeRendererKt;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.cobblemon.mod.common.api.mark.Mark;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import com.cobblemon.mod.common.client.render.models.blockbench.pokemon.PosablePokemonEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashSet;
import java.util.Set;

/** Client render layer that reuses Cobblemon's locator based alpha eye renderer. */
public final class AdvancedAlphaEyeLayer extends RenderLayer<PokemonEntity, PosablePokemonEntityModel> {
    public AdvancedAlphaEyeLayer(RenderLayerParent<PokemonEntity, PosablePokemonEntityModel> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffers, int packedLight,
                       PokemonEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!hasAdvancedAlpha(entity)) return;
        if (!(entity.getDelegate() instanceof PokemonClientDelegate delegate)) return;

        // Cobblemon's aspect particle system is tick based; call it at 2 Hz while rendered.
        if (entity.tickCount % 10 == 0) {
            var entityData = entity.getEntityData();
            var aspectsKey = PokemonEntity.Companion.getASPECTS();
            Set<String> entityAspects = entityData.get(aspectsKey);
            Set<String> particleAspects = new HashSet<>(entityAspects);
            particleAspects.add("alphacandy:mark_advanced_alpha");
            entityData.set(aspectsKey, particleAspects);
            try {
                delegate.spawnAspectParticle();
            } finally {
                entityData.set(aspectsKey, entityAspects);
            }
        }

        Set<String> oldAspects = delegate.getCurrentAspects();
        Set<String> renderAspects = new HashSet<>(oldAspects);
        renderAspects.add("alpha_eyes");
        delegate.setCurrentAspects(renderAspects);
        try {
            AlphaEyeRendererKt.doAlphaEyeRendering(entity, partialTick, poseStack, buffers);
        } finally {
            delegate.setCurrentAspects(oldAspects);
        }
    }

    private static boolean hasAdvancedAlpha(PokemonEntity entity) {
        Pokemon pokemon = entity.getPokemon();
        if (pokemon == null) return false;
        if (pokemon.getActiveMark() != null
                && AlphaAdvancedCandyItem.ADVANCED_ALPHA_MARK_ID.equals(pokemon.getActiveMark().getIdentifier())) return true;
        for (Mark mark : pokemon.getMarks()) {
            if (AlphaAdvancedCandyItem.ADVANCED_ALPHA_MARK_ID.equals(mark.getIdentifier())) return true;
        }
        return false;
    }
}
