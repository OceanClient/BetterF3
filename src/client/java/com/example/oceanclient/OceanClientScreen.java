package com.example.oceanclient;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class OceanClientScreen extends Screen {

    private static final int PANEL_WIDTH = 220;
    private static final int PANEL_HEIGHT = 140;

    public OceanClientScreen() {
        super(Component.literal("betterf3"));
    }

    @Override
    protected void init() {
        int left = (this.width - PANEL_WIDTH) / 2;
        int top = (this.height - PANEL_HEIGHT) / 2;

        // Settings "gear" button in the top-right corner of the panel
        this.addRenderableWidget(Button.builder(Component.literal("\u2699"), button -> {
            this.minecraft.setScreen(new SettingsScreen(this));
        }).bounds(left + PANEL_WIDTH - 24, top + 6, 18, 18).build());

        // Close button near the bottom of the panel
        this.addRenderableWidget(Button.builder(Component.literal("Close"), button -> {
            this.onClose();
        }).bounds(left + PANEL_WIDTH / 2 - 50, top + PANEL_HEIGHT - 28, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);

        BetterF3Config cfg = BetterF3Config.get();
        int left = (this.width - PANEL_WIDTH) / 2;
        int top = (this.height - PANEL_HEIGHT) / 2;

        // Panel body (dark, semi-transparent so text stays readable)
        g.fill(left, top, left + PANEL_WIDTH, top + PANEL_HEIGHT, 0xC0101018);
        // Title bar painted in the chosen GUI color
        g.fill(left, top, left + PANEL_WIDTH, top + 24, cfg.guiColor);

        // GUI name on the title bar, auto-contrasted so it's always readable
        g.drawString(this.font, cfg.guiName, left + 8, top + 8, contrastColor(cfg.guiColor));
    }

    private static int contrastColor(int argb) {
        int r = (argb >> 16) & 0xFF;
        int gc = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        double luminance = 0.299 * r + 0.587 * gc + 0.114 * b;
        return luminance > 140 ? 0xFF000000 : 0xFFFFFFFF;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
