package com.example.oceanclient;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

public class OceanClient implements ClientModInitializer {
    public static final String MOD_ID = "oceanclient";

    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "main"));

    private static KeyMapping menuKey;

    @Override
    public void onInitializeClient() {
        BetterF3Config.get(); // load saved settings on startup

        menuKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.oceanclient.open_menu",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.consumeClick()) {
                client.setScreen(new OceanClientScreen());
            }
        });
    }
}
