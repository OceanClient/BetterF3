package com.example.oceanclient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BetterF3Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("betterf3.json");

    // Saved settings (these are what get written to/read from the json file)
    public String guiName = "betterf3";
    public int guiColor = 0xFF1E90FF; // ARGB, default is a bright ocean blue

    private static BetterF3Config instance;

    public static BetterF3Config get() {
        if (instance == null) {
            instance = load();
        }
        return instance;
    }

    private static BetterF3Config load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                BetterF3Config cfg = GSON.fromJson(Files.readString(CONFIG_PATH), BetterF3Config.class);
                if (cfg != null) {
                    if (cfg.guiName == null || cfg.guiName.isEmpty()) cfg.guiName = "betterf3";
                    return cfg;
                }
            }
        } catch (IOException | JsonSyntaxException ignored) {
            // unreadable or missing -> fall back to defaults
        }
        return new BetterF3Config();
    }

    public void save() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(this));
        } catch (IOException ignored) {
            // non-fatal; settings just won't persist this time
        }
    }
}
