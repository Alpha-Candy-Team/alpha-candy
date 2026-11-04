package com.alphacandy.config;

import com.alphacandy.AlphaCandyMod;
import com.cobblemon.mod.common.pokemon.Pokemon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * EN: Manages mod configuration persistence, default values, and range validation.
 * ES: Gestiona la persistencia de la configuración del mod, valores por defecto y validación de rangos.
 */
public final class ModConfig {
    // EN: Pretty-printed serializer and per-mod configuration path. / ES: Serializador legible y ruta de configuración del mod.
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Path.of("config", AlphaCandyMod.MOD_ID, "main.json");

    /** EN: Base probability (0.0 - 1.0) for standard candy conversion. / ES: Probabilidad base (0.0 - 1.0) de conversión para el caramelo estándar. */
    public static double defaultCandyAlphaChance = 0.01;

    /** EN: Whether purifying candy awards essence upon use. / ES: Si el caramelo purificador entrega esencia al ser utilizado. */
    public static boolean rewardEssenceOnPurifyingCandy = true;

    /** EN: Minimum essence rewarded when purifying. / ES: Cantidad mínima de esencia otorgada al purificar. */
    public static int purifyingCandyEssenceMin = 1;

    /** EN: Maximum essence rewarded when purifying. / ES: Cantidad máxima de esencia otorgada al purificar. */
    public static int purifyingCandyEssenceMax = 1;

    /** EN: Entity heal amount applied on candy usage (0 to disable). / ES: Cantidad de curación a la entidad al usar un caramelo (0 para desactivar). */
    public static int alphaCandyHealAmount = 0;

    /** EN: Species identifiers excluded from candy transformations. / ES: Identificadores de especies excluidas de las transformaciones con caramelos. */
    public static List<String> speciesBlacklist = new ArrayList<>();

    /** EN: Set view of blacklisted species for fast O(1) lookups. / ES: Vista en conjunto de especies en lista negra para búsquedas rápidas O(1). */
    private static Set<String> speciesBlacklistSet = new HashSet<>();

    /** EN: Additional probability added per seasoning essence level. / ES: Probabilidad adicional sumada por cada nivel de esencia de condimento. */
    public static double essenceBoost = 0.32;

    /** EN: Maximum seasoning essence levels supported per candy. / ES: Máximo nivel de condimento de esencia soportado por caramelo. */
    public static int maxEssencePerCandy = 3;

    /** EN: Prevents instantiation of configuration utility class. / ES: Evita la instanciación de la clase auxiliar de configuración. */
    private ModConfig() {
    }

    /**
     * EN: Creates the config directory, loads and validates saved values, or writes defaults on first launch.
     * ES: Crea la carpeta, carga y valida los valores guardados o escribe los valores iniciales en el primer arranque.
     */
    public static void load() {
        try {
            // EN: Ensure the parent directory exists before reading or writing the file.
            // ES: Asegura que exista la carpeta antes de leer o escribir el archivo.
            Files.createDirectories(PATH.getParent());
            if (Files.exists(PATH)) {
                Data data = GSON.fromJson(Files.readString(PATH), Data.class);
                if (data != null) {
                    // EN: Clamp probabilities and keep reward bounds internally consistent.
                    // ES: Limita las probabilidades y mantiene coherentes los límites de recompensa.
                    defaultCandyAlphaChance = clamp(data.defaultCandyAlphaChance, 0.0, 1.0);
                    rewardEssenceOnPurifyingCandy = data.rewardEssenceOnPurifyingCandy;
                    purifyingCandyEssenceMin = Math.max(0, data.purifyingCandyEssenceMin);
                    purifyingCandyEssenceMax = Math.max(purifyingCandyEssenceMin, data.purifyingCandyEssenceMax);
                    alphaCandyHealAmount = Math.max(0, data.alphaCandyHealAmount);
                    speciesBlacklist = data.speciesBlacklist == null ? new ArrayList<>() : new ArrayList<>(data.speciesBlacklist);
                    speciesBlacklistSet = new HashSet<>(speciesBlacklist);
                    essenceBoost = clamp(data.essenceBoost, 0.0, 1.0);
                    maxEssencePerCandy = Math.max(0, data.maxEssencePerCandy);
                }
            } else {
                save();
            }
            // EN: Keep defaults active if malformed or inaccessible config data is encountered.
            // ES: Conserva los valores predeterminados si la configuración no se puede leer.
        } catch (IOException | RuntimeException ignored) {
            // Defaults remain active if the config cannot be read.
            speciesBlacklistSet = new HashSet<>(speciesBlacklist);
        }
    }

    /**
     * EN: Checks whether the given Pokémon belongs to a blacklisted species with zero allocations if the blacklist is empty.
     * ES: Comprueba si el Pokémon indicado pertenece a una especie en la lista negra, sin asignaciones si la lista está vacía.
     */
    public static boolean isSpeciesBlacklisted(Pokemon pokemon) {
        if (speciesBlacklistSet.isEmpty()) {
            return false;
        }
        return speciesBlacklistSet.contains(pokemon.getSpecies().getResourceIdentifier().toString());
    }

    /** EN: Serializes current static settings to the config file. / ES: Serializa los ajustes actuales en el archivo. */
    public static void save() throws IOException {
        // EN: Copy fields into a plain data object for Gson serialization.
        // ES: Copia los campos a un objeto de datos sencillo para serializarlos con Gson.
        Data data = new Data();
        data.defaultCandyAlphaChance = defaultCandyAlphaChance;
        data.rewardEssenceOnPurifyingCandy = rewardEssenceOnPurifyingCandy;
        data.purifyingCandyEssenceMin = purifyingCandyEssenceMin;
        data.purifyingCandyEssenceMax = purifyingCandyEssenceMax;
        data.alphaCandyHealAmount = alphaCandyHealAmount;
        data.speciesBlacklist = speciesBlacklist;
        data.essenceBoost = essenceBoost;
        data.maxEssencePerCandy = maxEssencePerCandy;

        speciesBlacklistSet = new HashSet<>(speciesBlacklist);

        Files.writeString(PATH, GSON.toJson(data),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /** EN: Bounds a value inclusively. / ES: Limita un valor incluyendo ambos extremos. */
    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * EN: Data transfer object used for Gson serialization of configuration values.
     * ES: Objeto de transferencia de datos usado por Gson para la serialización de valores de configuración.
     */
    private static final class Data {
        // EN: Gson maps these defaults to absent keys in older config files. / ES: Gson usa estos valores si faltan claves en configuraciones antiguas.
        double defaultCandyAlphaChance = 0.01;
        boolean rewardEssenceOnPurifyingCandy = true;
        int purifyingCandyEssenceMin = 1;
        int purifyingCandyEssenceMax = 1;
        int alphaCandyHealAmount = 0;
        List<String> speciesBlacklist = new ArrayList<>();
        double essenceBoost = 0.32;
        int maxEssencePerCandy = 3;
    }
}
