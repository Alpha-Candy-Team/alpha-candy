package com.alphacandy.compat.iota;

import com.cobblemon.mod.common.api.mark.Mark;
import com.cobblemon.mod.common.api.mark.Marks;
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashSet;
import java.util.Set;

public final class IotaCompat {

    private static final String IOTA_FEATURE = "iota";

    private static final ResourceLocation IOTA_MARK_ID =
            ResourceLocation.parse("cobblemon:mark_iota");

    private static final String ORIGINAL_SCALE =
            "alphacandy_iota_original_scale";

    private static final String ORIGINAL_SPEED =
            "alphacandy_iota_original_speed";

    private IotaCompat() {
    }

    public static boolean isIota(Pokemon pokemon) {
        StringSpeciesFeature feature = pokemon.getFeature(IOTA_FEATURE);
        return feature != null
                && "true".equalsIgnoreCase(feature.getValue());
    }

    public static void makeIota(PokemonEntity entity) {
        Pokemon pokemon = entity.getPokemon();

        StringSpeciesFeature feature = pokemon.getFeature(IOTA_FEATURE);
        Mark iotaMark = Marks.getByIdentifier(IOTA_MARK_ID);

        if (feature == null || iotaMark == null || isIota(pokemon)) {
            return;
        }

        CompoundTag data = pokemon.getPersistentData();

        if (!data.contains(ORIGINAL_SCALE)) {
            data.putFloat(ORIGINAL_SCALE, pokemon.getScaleModifier());
        }

        AttributeInstance speed =
                entity.getAttribute(Attributes.MOVEMENT_SPEED);

        if (speed != null && !data.contains(ORIGINAL_SPEED)) {
            data.putDouble(ORIGINAL_SPEED, speed.getBaseValue());
        }

        feature.setValue("true");
        pokemon.markFeatureDirty(feature);

        Set<Mark> marks = new HashSet<>(pokemon.getMarks());
        marks.add(iotaMark);
        pokemon.setMarks(marks);

        if (pokemon.getActiveMark() == null) {
            pokemon.setActiveMark(iotaMark);
        }

        // Mantener una escala coherente con el tamaño original registrado.
        float naturalSize = data.getFloat(ORIGINAL_SCALE);
        pokemon.setScaleModifier(calculateIotaScale(pokemon, naturalSize));

        if (speed != null) {
            speed.setBaseValue(1.2);
        }

        pokemon.updateAspects();
    }

    public static void removeIota(PokemonEntity entity) {
        Pokemon pokemon = entity.getPokemon();

        StringSpeciesFeature feature = pokemon.getFeature(IOTA_FEATURE);

        if (feature != null) {
            feature.setValue("false");
            pokemon.markFeatureDirty(feature);
        }

        Set<String> forcedAspects =
                new HashSet<>(pokemon.getForcedAspects());

        forcedAspects.remove("iota-true");
        pokemon.setForcedAspects(forcedAspects);

        Mark iotaMark = Marks.getByIdentifier(IOTA_MARK_ID);

        if (iotaMark != null) {
            Set<Mark> remainingMarks = new HashSet<>(pokemon.getMarks());
            remainingMarks.remove(iotaMark);
            pokemon.setMarks(remainingMarks);

            if (iotaMark.equals(pokemon.getActiveMark())) {
                pokemon.setActiveMark(null);
            }
        }

        CompoundTag data = pokemon.getPersistentData();

        // Quitar Mini solo si la añadió el Caramelo Iota Avanzado.
        if (data.getBoolean("alphacandy_iota_advanced_added_mini")) {
            ResourceLocation miniId =
                    ResourceLocation.parse("cobblemon:mark_mini");

            Set<Mark> marks = new HashSet<>(pokemon.getMarks());
            marks.removeIf(mark -> miniId.equals(mark.getIdentifier()));
            pokemon.setMarks(marks);

            Mark active = pokemon.getActiveMark();
            if (active != null && miniId.equals(active.getIdentifier())) {
                pokemon.setActiveMark(null);
            }

            data.remove("alphacandy_iota_advanced_added_mini");
        }

        float restoreScale;

        if (data.contains(ORIGINAL_SCALE)) {
            restoreScale = data.getFloat(ORIGINAL_SCALE);

            // Si Alfa avanzado sigue activo, restaurar su escala, no la
            // escala intermedia que Iota pudo haber guardado.
            if (data.getBoolean("alphacandy_advanced_alpha_active")) {
                float alphaOriginal = data.getFloat(
                        "alphacandy_advanced_alpha_original_scale"
                );
                restoreScale = alphaOriginal * 1.5f;
            }

            data.remove(ORIGINAL_SCALE);
        } else {
            // Iota natural sin escala original registrada.
            restoreScale = 0.95f + (float) Math.random() * 0.1f;
        }

        pokemon.setScaleModifier(restoreScale);

        AttributeInstance speed =
                entity.getAttribute(Attributes.MOVEMENT_SPEED);

        if (speed != null && data.contains(ORIGINAL_SPEED)) {
            speed.setBaseValue(data.getDouble(ORIGINAL_SPEED));
            data.remove(ORIGINAL_SPEED);
        }

        pokemon.updateAspects();
    }

    public static float getOriginalScale(Pokemon pokemon) {
        CompoundTag data = pokemon.getPersistentData();

        return data.contains(ORIGINAL_SCALE)
                ? data.getFloat(ORIGINAL_SCALE)
                : pokemon.getScaleModifier();
    }

    public static void refreshIotaScale(Pokemon pokemon) {
        if (!isIota(pokemon)) {
            return;
        }

        CompoundTag data = pokemon.getPersistentData();

        if (!data.contains(ORIGINAL_SCALE)) {
            return;
        }

        pokemon.setScaleModifier(
                calculateIotaScale(pokemon, data.getFloat(ORIGINAL_SCALE))
        );
        pokemon.updateAspects();
    }

    public static void updateOriginalScaleAfterAlphaPurification(
            Pokemon pokemon,
            float originalScale
    ) {
        if (!isIota(pokemon)) {
            return;
        }

        CompoundTag data = pokemon.getPersistentData();
        data.putFloat(ORIGINAL_SCALE, originalScale);
        refreshIotaScale(pokemon);
    }

    private static float calculateIotaScale(
            Pokemon pokemon,
            float naturalSize
    ) {
        ResourceLocation miniMarkId =
                ResourceLocation.parse("cobblemon:mark_mini");

        boolean isMini = pokemon.getMarks().stream()
                .anyMatch(mark -> miniMarkId.equals(mark.getIdentifier()));

        if (naturalSize < 1.0f) {
            return isMini ? 0.5f : 0.6666f;
        } else if (naturalSize < 2.0f) {
            return isMini ? 0.375f : 0.5f;
        } else {
            return isMini ? 0.25f : 0.3333f;
        }
    }}
