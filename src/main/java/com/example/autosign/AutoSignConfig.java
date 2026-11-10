package com.example.autosign;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AutoSignConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "autosign.json");

    public static class Data {
        public boolean enabled = true;
        public String defaultText = "Welcome!";
        public boolean autoCloseScreen = true;
    }

    public static Data INSTANCE = new Data();

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                INSTANCE = GSON.fromJson(reader, Data.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(INSTANCE, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Screen createScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Auto Sign Configuration"));

        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General Settings"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // on / off
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Enable Auto Sign"), INSTANCE.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> INSTANCE.enabled = newValue)
                .build());

        // text block
        general.addEntry(entryBuilder.startStrField(Component.literal("Sign Text"), INSTANCE.defaultText)
                .setDefaultValue("This is a automatic written sign!")
                .setTooltip(Component.literal("Text applied automatically when placing a sign."))
                .setSaveConsumer(newValue -> INSTANCE.defaultText = newValue)
                .build());

        // auto close
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Auto Close GUI"), INSTANCE.autoCloseScreen)
                .setDefaultValue(true)
                .setTooltip(Component.literal("Automatically close the sign edit menu after setting text."))
                .setSaveConsumer(newValue -> INSTANCE.autoCloseScreen = newValue)
                .build());

        builder.setSavingRunnable(AutoSignConfig::save);

        return builder.build();
    }
}
