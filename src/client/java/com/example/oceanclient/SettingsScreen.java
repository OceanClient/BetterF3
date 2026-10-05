package com.example.oceanclient;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class SettingsScreen extends Screen {

    private final Screen parent;
    private EditBox nameField;
    private int red;
    private int green;
    private int blue;

    public SettingsScreen(Screen parent) {
        super(Component.literal("Settings"));
        this.parent = parent;
        int color = BetterF3Config.get().guiColor;
        this.red = (color >> 16) & 0xFF;
        this.green = (color >> 8) & 0xFF;
        this.blue = color & 0xFF;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y0 = this.height / 2 - 70;

        nameField = new EditBox(this.font, cx - 100, y0, 200, 20, Component.literal("GUI Name"));
        nameField.setMaxLength(32);
        nameField.setValue(BetterF3Config.get().guiName);
        this.addRenderableWidget(nameField);

        this.addRenderableWidget(new ColorSlider(cx - 100, y0 + 30, 200, 20, "Red",   () -> red,   v -> red = v));
        this.addRenderableWidget(new ColorSlider(cx - 100, y0 + 54, 200, 20, "Green", () -> green, v -> green = v));
        this.addRenderableWidget(new ColorSlider(cx - 100, y0 + 78, 200, 20, "Blue",  () -> blue,  v -> blue = v));

        this.addRenderableWidget(Button.builder(Component.literal("Save"), b -> {
            BetterF3Config cfg = BetterF3Config.get();
            String typed = nameField.getValue().trim();
            cfg.guiName = typed.isEmpty() ? "betterf3" : typed;
            cfg.guiColor = 0xFF000000 | (red << 16) | (green << 8) | blue;
            cfg.save();
            this.minecraft.setScreen(parent);
        }).bounds(cx - 100, y0 + 124, 95, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Back"), b -> {
            this.minecraft.setScreen(parent);
        }).bounds(cx + 5, y0 + 124, 95, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);

        int cx = this.width / 2;
        int y0 = this.height / 2 - 70;

        g.drawCenteredString(this.font, this.title, cx, y0 - 22, 0xFFFFFFFF);

        // Live preview of the chosen color
        int preview = 0xFF000000 | (red << 16) | (green << 8) | blue;
        int py = y0 + 104;
        g.fill(cx - 100, py, cx + 100, py + 14, preview);
        g.drawString(this.font, "Preview", cx - 96, py + 3, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    // A simple 0-255 slider that reads/writes one color channel.
    private static class ColorSlider extends AbstractSliderButton {
        private final String label;
        private final IntSupplier getter;
        private final IntConsumer setter;

        ColorSlider(int x, int y, int w, int h, String label, IntSupplier getter, IntConsumer setter) {
            super(x, y, w, h, Component.empty(), getter.getAsInt() / 255.0);
            this.label = label;
            this.getter = getter;
            this.setter = setter;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            if (getter == null) return; // guard in case the parent ctor calls this early
            this.setMessage(Component.literal(label + ": " + getter.getAsInt()));
        }

        @Override
        protected void applyValue() {
            if (setter == null) return;
            setter.accept((int) Math.round(this.value * 255.0));
        }
    }
}
