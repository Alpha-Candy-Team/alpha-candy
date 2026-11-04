package com.alphacandy.compat.iota;

import com.cobblemon.mod.common.api.mark.Mark;
import com.cobblemon.mod.common.api.mark.Marks;
import com.cobblemon.mod.common.api.pokemon.feature.StringSpeciesFeature;
import com.cobblemon.mod.common.api.pokemon.feature.ChoiceSpeciesFeatureProvider;
import com.cobblemon.mod.common.api.pokemon.feature.SpeciesFeatures;
import com.cobblemon.mod.common.api.pokemon.feature.GlobalSpeciesFeatures;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Pokemon;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.HashSet;
import java.util.Set;

/**
 * EN: Compatibility layer managing Cobblemon Iota species features, scale adjustments, and mark lifecycle.
 * ES: Capa de compatibilidad que gestiona las características de especie Iota, ajustes de escala y ciclo de vida de marcas en Cobblemon.
 */
public final class IotaCompat {

    // EN: Cobblemon feature and mark IDs used to represent Iota state. / ES: IDs de característica y marca de Cobblemon que representan el estado Iota.
    private static final String IOTA_FEATURE = "iota";

    /** EN: Cobblemon Iota mark identifier. / ES: Identificador de la marca Iota de Cobblemon. */
    private static final ResourceLocation IOTA_MARK_ID =
            ResourceLocation.parse("cobblemon:mark_iota");

    /** EN: Cobblemon Mini mark identifier. / ES: Identificador de la marca Mini de Cobblemon. */
    private static final ResourceLocation MINI_MARK_ID =
            ResourceLocation.parse("cobblemon:mark_mini");

    // EN: Persistent keys preserve pre-transformation scale and movement speed. / ES: Estas claves persistentes guardan la escala y velocidad previas a la transformación.
    private static final String ORIGINAL_SCALE =
            "alphacandy_iota_original_scale";

    private static final String ORIGINAL_SPEED =
            "alphacandy_iota_original_speed";

    /** EN: Static-only compatibility helper. / ES: Auxiliar estático de compatibilidad. */
    private IotaCompat() {
    }

    /** EN: Registers the global feature if necessary, then confirms both feature and mark exist. / ES: Registra la característica global si hace falta y comprueba que existan característica y marca. */
    public static boolean isIotaDataAvailable(Pokemon pokemon) {
        ensureGlobalIotaFeature();

        return SpeciesFeatures.getFeature(IOTA_FEATURE) != null
                && Marks.getByIdentifier(IOTA_MARK_ID) != null;
    }

    /** EN: Promotes a resource feature to the global feature registry using compatible reflective lookup. / ES: Promueve una característica de recursos al registro global mediante búsqueda reflectiva compatible. */
    private static boolean ensureGlobalIotaFeature() {
        // EN: A code-registered feature is already ready for every species. / ES: Una característica registrada en código ya está disponible para todas las especies.
        if (GlobalSpeciesFeatures.getCodeFeature(IOTA_FEATURE) != null) {
            return true;
        }

        var provider = SpeciesFeatures.getFeature(IOTA_FEATURE);

        // EN: Only choice providers can create the true/false string feature used here. / ES: Solo un proveedor de elección puede crear la característica de texto true/false requerida.
        if (!(provider instanceof ChoiceSpeciesFeatureProvider)) {
            return false;
        }

        var resourceProvider =
                SpeciesFeatures.getResourceFeature(IOTA_FEATURE);

        // EN: Remove the per-resource registration before adding the global entry. / ES: Elimina el registro por recurso antes de añadir la entrada global.
        if (resourceProvider != null) {
            SpeciesFeatures.unregister(IOTA_FEATURE);
        }

        // EN: Find the two-argument register overload matching this provider type. / ES: Busca el método register de dos argumentos compatible con este proveedor.
        try {
            Class<?> globalFeatures = Class.forName(
                    "com.cobblemon.mod.common.api.pokemon.feature.GlobalSpeciesFeatures"
            );

            java.lang.reflect.Method register = java.util.Arrays.stream(
                    globalFeatures.getMethods()
            )
                    .filter(method -> method.getName().equals("register"))
                    .filter(method -> method.getParameterCount() == 2)
                    .filter(method -> method.getParameterTypes()[0] == String.class)
                    .filter(method -> method.getParameterTypes()[1].isInstance(provider))
                    .findFirst()
                    .orElseThrow();

            register.invoke(null, IOTA_FEATURE, provider);
            return true;

        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "No se pudo registrar globalmente la característica Iota",
                    exception
            );
        }
    }

    /** EN: Gets the Pokémon feature or creates a false-valued instance and attaches it. / ES: Obtiene la característica o crea una instancia falsa y la añade al Pokémon. */
    private static StringSpeciesFeature getOrCreateIotaFeature(Pokemon pokemon) {
        StringSpeciesFeature feature = pokemon.getFeature(IOTA_FEATURE);

        if (feature != null) {
            return feature;
        }

        if (!(SpeciesFeatures.getFeature(IOTA_FEATURE)
                instanceof ChoiceSpeciesFeatureProvider provider)) {
            return null;
        }

        feature = provider.fromString("false");

        if (feature == null) {
            return null;
        }

        // EN: Persist the newly created feature in the Pokémon feature collection. / ES: Guarda la característica recién creada en la colección del Pokémon.
        java.util.List<com.cobblemon.mod.common.api.pokemon.feature.SpeciesFeature> features =
                new java.util.ArrayList<>(pokemon.getFeatures());

        features.add(feature);
        pokemon.setFeatures(features);

        return feature;
    }

    /** EN: Treats only a case-insensitive true feature value as Iota. / ES: Considera Iota solo si el valor de la característica es true, sin distinguir mayúsculas. */
    public static boolean isIota(Pokemon pokemon) {
        StringSpeciesFeature feature = pokemon.getFeature(IOTA_FEATURE);
        return feature != null
                && "true".equalsIgnoreCase(feature.getValue());
    }

    /**
     * EN: Saves original scale and speed once, enables feature and mark, applies the Iota scale/speed,
     * and refreshes visual aspects. Missing prerequisites leave the Pokémon unchanged.
     * ES: Guarda escala y velocidad originales una sola vez, activa característica y marca, aplica
     * escala/velocidad Iota y actualiza aspectos visuales. Si faltan requisitos, no cambia el Pokémon.
     */
    public static void makeIota(PokemonEntity entity) {
        Pokemon pokemon = entity.getPokemon();

        StringSpeciesFeature feature = getOrCreateIotaFeature(pokemon);
        Mark iotaMark = Marks.getByIdentifier(IOTA_MARK_ID);

        // EN: Do not overwrite restoration data or reapply the transformation. / ES: No sobrescribe datos de restauración ni repite la transformación.
        if (feature == null || iotaMark == null || isIota(pokemon)) {
            return;
        }

        CompoundTag data = pokemon.getPersistentData();

        // EN: Preserve the natural scale before changing it to the Iota scale. / ES: Conserva la escala natural antes de aplicar la escala Iota.
        if (!data.contains(ORIGINAL_SCALE)) {
            data.putFloat(ORIGINAL_SCALE, pokemon.getScaleModifier());
        }

        AttributeInstance speed =
                entity.getAttribute(Attributes.MOVEMENT_SPEED);

        // EN: Preserve the entity's base speed so purification can restore it. / ES: Conserva la velocidad base para poder restaurarla al purificar.
        if (speed != null && !data.contains(ORIGINAL_SPEED)) {
            data.putDouble(ORIGINAL_SPEED, speed.getBaseValue());
        }

        // EN: Update Cobblemon feature state and keep the Iota mark in the Pokémon mark set. / ES: Actualiza la característica de Cobblemon y añade la marca Iota al conjunto del Pokémon.
        feature.setValue("true");
        pokemon.markFeatureDirty(feature);

        Set<Mark> marks = new HashSet<>(pokemon.getMarks());
        marks.add(iotaMark);
        pokemon.setMarks(marks);

        if (pokemon.getActiveMark() == null) {
            pokemon.setActiveMark(iotaMark);
        }

        // EN: Keep scale consistent with the captured original size.
        // ES: Mantener una escala coherente con el tamaño original registrado.
        float naturalSize = data.getFloat(ORIGINAL_SCALE);
        pokemon.setScaleModifier(calculateIotaScale(pokemon, naturalSize));

        // EN: Boost movement speed attribute for the Iota state.
        // ES: Incrementa el atributo de velocidad de movimiento para el estado Iota.
        if (speed != null) {
            speed.setBaseValue(1.2);
        }

        pokemon.updateAspects();
    }

    /** EN: Clears Iota state and marks, restores saved attributes (including Advanced Alpha scale interactions), then refreshes aspects. / ES: Limpia estado y marcas Iota, restaura atributos (incluida la interacción con Alfa avanzado) y actualiza aspectos. */
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

        // EN: Remove a Mini mark only when the advanced Iota candy added it. / ES: Elimina Mini solo si la añadió el caramelo Iota avanzado.
        if (data.getBoolean(IotaAdvancedCandyItem.ADDED_MINI)) {
            Set<Mark> marks = new HashSet<>(pokemon.getMarks());
            marks.removeIf(mark -> MINI_MARK_ID.equals(mark.getIdentifier()));
            pokemon.setMarks(marks);

            Mark active = pokemon.getActiveMark();
            if (active != null && MINI_MARK_ID.equals(active.getIdentifier())) {
                pokemon.setActiveMark(null);
            }

            data.remove(IotaAdvancedCandyItem.ADDED_MINI);
        }

        float restoreScale;

        // EN: Restore the Advanced Alpha enlarged scale when both transformations were active. / ES: Restaura la escala aumentada de Alfa avanzado si ambas transformaciones estaban activas.
        if (data.contains(ORIGINAL_SCALE)) {
            restoreScale = data.getFloat(ORIGINAL_SCALE);

            // EN: If Advanced Alpha remains active, restore its scale rather than any intermediate scale.
            // ES: Si Alfa avanzado sigue activo, restaura su escala, no la escala intermedia que Iota pudo haber guardado.
            if (data.getBoolean("alphacandy_advanced_alpha_active")) {
                float alphaOriginal = data.getFloat(
                        "alphacandy_advanced_alpha_original_scale"
                );
                restoreScale = alphaOriginal * 1.5f;
            }

            data.remove(ORIGINAL_SCALE);
        } else {
            // EN: Natural Iota without a recorded original scale.
            // ES: Iota natural sin escala original registrada.
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

    /** EN: Returns saved natural scale, or current scale if no snapshot exists. / ES: Devuelve la escala natural guardada o la actual si no hay copia. */
    public static float getOriginalScale(Pokemon pokemon) {
        CompoundTag data = pokemon.getPersistentData();

        return data.contains(ORIGINAL_SCALE)
                ? data.getFloat(ORIGINAL_SCALE)
                : pokemon.getScaleModifier();
    }

    /** EN: Reapplies derived Iota scale only when the Pokémon is Iota and has a saved natural scale. / ES: Reaplica la escala Iota solo si el Pokémon es Iota y tiene guardada su escala natural. */
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

    /** EN: Replaces the saved natural scale after Alpha purification, then recalculates Iota scale. / ES: Actualiza la escala natural tras purificar Alfa y recalcula la escala Iota. */
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

    /** EN: Chooses a size multiplier based on natural size tier and whether Mini is marked. / ES: Elige un multiplicador según el tramo de tamaño natural y la presencia de Mini. */
    private static float calculateIotaScale(
            Pokemon pokemon,
            float naturalSize
    ) {
        // EN: Check for Mini mark using direct iteration to avoid stream pipeline allocation.
        // ES: Comprueba la marca Mini usando iteración directa para evitar la reserva de streams.
        boolean isMini = false;
        for (Mark mark : pokemon.getMarks()) {
            if (MINI_MARK_ID.equals(mark.getIdentifier())) {
                isMini = true;
                break;
            }
        }

        if (naturalSize < 1.0f) {
            return isMini ? 0.5f : 0.6666f;
        } else if (naturalSize < 2.0f) {
            return isMini ? 0.375f : 0.5f;
        } else {
            return isMini ? 0.25f : 0.3333f;
        }
    }
}
