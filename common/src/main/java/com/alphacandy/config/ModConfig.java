package com.alphacandy.config;

import com.alphacandy.AlphaCandyMod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Path.of("config", AlphaCandyMod.MOD_ID, "main.json");

    public static double defaultCandyAlphaChance = 0.01;
    public static boolean rewardEssenceOnPurifyingCandy = true;
    public static int purifyingCandyEssenceMin = 1;
    public static int purifyingCandyEssenceMax = 1;
    public static int alphaCandyHealAmount = 0;
    public static List<String> speciesBlacklist = new ArrayList<>();

    public static double essenceBoost = 0.32;
    public static int maxEssencePerCandy = 3;

    public static void load() {
        try {
            Files.createDirectories(PATH.getParent());
            if (Files.exists(PATH)) {
                Data data = GSON.fromJson(Files.readString(PATH), Data.class);
                if (data != null) {
                    defaultCandyAlphaChance = clamp(data.defaultCandyAlphaChance, 0.0, 1.0);
                    rewardEssenceOnPurifyingCandy = data.rewardEssenceOnPurifyingCandy;
                    purifyingCandyEssenceMin = Math.max(0, data.purifyingCandyEssenceMin);
                    purifyingCandyEssenceMax = Math.max(purifyingCandyEssenceMin, data.purifyingCandyEssenceMax);
                    alphaCandyHealAmount = Math.max(0, data.alphaCandyHealAmount);
                    speciesBlacklist = data.speciesBlacklist == null ? new ArrayList<>() : new ArrayList<>(data.speciesBlacklist);
                    essenceBoost = clamp(data.essenceBoost, 0.0, 1.0);
                    maxEssencePerCandy = Math.max(0, data.maxEssencePerCandy);
                }
            } else {
                save();
            }
        } catch (IOException | RuntimeException ignored) {
            // Defaults remain active if the config cannot be read.
        }
    }

    public static void save() throws IOException {
        Data data = new Data();
        data.defaultCandyAlphaChance = defaultCandyAlphaChance;
        data.rewardEssenceOnPurifyingCandy = rewardEssenceOnPurifyingCandy;
        data.purifyingCandyEssenceMin = purifyingCandyEssenceMin;
        data.purifyingCandyEssenceMax = purifyingCandyEssenceMax;
        data.alphaCandyHealAmount = alphaCandyHealAmount;
        data.speciesBlacklist = speciesBlacklist;
        data.essenceBoost = essenceBoost;
        data.maxEssencePerCandy = maxEssencePerCandy;

        Files.writeString(PATH, GSON.toJson(data),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    private static final class Data {
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
