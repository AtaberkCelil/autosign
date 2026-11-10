package com.example.autosign;

import net.fabricmc.api.ClientModInitializer;

public class AutoSignClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoSignConfig.load();
    }
}
