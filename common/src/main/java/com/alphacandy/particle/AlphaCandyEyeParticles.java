package com.alphacandy.particle;

import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * EN: Handles the registration of custom Pokémon eye particle effects with Cobblemon's aspect system.
 * ES: Gestiona el registro de efectos de partículas personalizados en los ojos de los Pokémon con el sistema de aspectos de Cobblemon.
 */
public final class AlphaCandyEyeParticles {

    /** EN: Identifier of the aspect attached to Pokémon with the Advanced Alpha mark. / ES: Identificador del aspecto asociado a los Pokémon con la marca Alfa Avanzado. */
    public static final String ASPECT_NAME = "advanced_alpha_eyes";

    /** EN: Resource location of the bedrock particle effect definition. / ES: Ubicación de recurso de la definición del efecto de partícula bedrock. */
    public static final ResourceLocation PARTICLE_ID =
            ResourceLocation.fromNamespaceAndPath("alphacandy", "advanced_alpha_eyes");

    /** EN: Prevents instantiation of utility class. / ES: Evita la instanciación de la clase auxiliar. */
    private AlphaCandyEyeParticles() {
    }

    /**
     * EN: Registers the golden eye particle definition into Cobblemon's aspect particle map via reflective lookup.
     * ES: Registra la definición de partículas doradas en los ojos dentro del mapa de partículas de aspectos de Cobblemon mediante búsqueda reflectiva.
     */
    public static void register() {
        try {
            Class<?> snowstormClass = Class.forName("com.cobblemon.mod.common.api.pokemon.aspect.ParticleData$SnowstormParticle");
            Class<?> locatorResolversClass = Class.forName("com.cobblemon.mod.common.api.pokemon.aspect.LocatorResolvers");
            Class<?> aspectParticleMapClass = Class.forName("com.cobblemon.mod.common.api.pokemon.aspect.AspectParticleMapKt");

            Object locatorResolversInstance = locatorResolversClass.getField("INSTANCE").get(null);
            Method containingMethod = locatorResolversClass.getMethod("containing", String.class);
            Object eyeLocator = containingMethod.invoke(locatorResolversInstance, "eye");

            Constructor<?> constructor = snowstormClass.getConstructors()[0];
            Object particleData = constructor.newInstance(PARTICLE_ID, 0.25, 1, eyeLocator);

            Method getMapMethod = aspectParticleMapClass.getMethod("getAspectParticleMap");
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) getMapMethod.invoke(null);

            try {
                // EN: Attempt direct addition into the map if mutable.
                // ES: Intenta añadir directamente al mapa si es modificable.
                map.put(ASPECT_NAME, particleData);
            } catch (UnsupportedOperationException ignored) {
                // EN: Fall back to replacing the static map field via reflection.
                // ES: Recurre a sustituir el campo estático del mapa mediante reflexión si es de solo lectura.
                replaceAspectMapField(aspectParticleMapClass, particleData);
            }
        } catch (Throwable ignored) {
            // EN: Silently continue if Cobblemon classes are not available.
            // ES: Continúa silenciosamente si las clases de Cobblemon no están disponibles.
        }
    }

    /**
     * EN: Replaces the internal static map in AspectParticleMapKt with an expanded copy.
     * ES: Sustituye el mapa estático interno en AspectParticleMapKt por una copia ampliada.
     */
    private static void replaceAspectMapField(Class<?> aspectParticleMapClass, Object particleData) {
        try {
            Field field = aspectParticleMapClass.getDeclaredField("aspectParticleMap");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<String, Object> original = (Map<String, Object>) field.get(null);
            Map<String, Object> updated = new LinkedHashMap<>(original);
            updated.put(ASPECT_NAME, particleData);

            try {
                field.set(null, updated);
            } catch (IllegalAccessException e) {
                // EN: If static final modification is blocked, use Unsafe.
                // ES: Si la modificación estática final está bloqueada, usa Unsafe.
                Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
                Object base = unsafe.staticFieldBase(field);
                long offset = unsafe.staticFieldOffset(field);
                unsafe.putObject(base, offset, updated);
            }
        } catch (Throwable ignored) {
            // Silently ignore if reflection is restricted
        }
    }
}
