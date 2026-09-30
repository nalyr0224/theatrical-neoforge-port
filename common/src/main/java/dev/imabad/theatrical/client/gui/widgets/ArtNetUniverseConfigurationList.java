package dev.imabad.theatrical.client.gui.widgets;

import dev.imabad.theatrical.client.gui.screen.ArtNetConfigurationScreen;
import dev.imabad.theatrical.config.UniverseConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.function.Consumer;

public class ArtNetUniverseConfigurationList extends ObjectSelectionList<ArtNetUniverseConfigurationList.Entry> implements LayoutElement {

    private final ArtNetConfigurationScreen parent;

    public ArtNetUniverseConfigurationList(Minecraft minecraft, ArtNetConfigurationScreen screen, int width, int height, Component title) {
        // 1.21.1 Constructor: (Minecraft, width, listHeight, yPosition, itemHeight)
        // We calculate listHeight as the old y1 (height - 51) minus y0 (32) = height - 83.
        super(minecraft, width, height - 83, 32, 30);
        this.parent = screen;

        // Note: In 1.21.1, setRenderBackground, setRenderHeader, and setRenderTopAndBottom
        // were removed from ObjectSelectionList. Rendering is now handled by the parent Screen!
    }

    public void setEntries(Map<Integer, UniverseConfig> configs){
        this.clearEntries();
        configs.forEach((key, value) -> addEntry(new Entry(parent, key, value)));
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getX() + this.getRowWidth() + 6;
    }

    @Override
    public int getRowWidth() {
        return this.width - 10;
    }

    @Override
    public void setX(int x) {
        // Replaces deprecated x0 field
        super.setX(x);
    }

    @Override
    public void setY(int y) {
        // Replaces deprecated y0/y1 fields
        super.setY(y);
    }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
    }

    // @Environment(EnvType.CLIENT) and imports removed for Architectury/NeoForge
    public static class Entry extends ObjectSelectionList.Entry<Entry> implements AutoCloseable {

        private final ArtNetConfigurationScreen parent;
        private final UniverseConfig config;
        private final int networkUniverse;

        public Entry(ArtNetConfigurationScreen parent, int networkUniverse, UniverseConfig config) {
            this.parent = parent;
            this.config = config;
            this.networkUniverse = networkUniverse;
        }

        @Override
        public Component getNarration() {
            return Component.empty();
        }

        public void close() {
        }

        public UniverseConfig getConfig() {
            return config;
        }

        public int getNetworkUniverse() {
            return networkUniverse;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            Font font = Minecraft.getInstance().font;
            guiGraphics.drawString(font, Component.translatable("screen.artnetconfig.entry.universe", networkUniverse), left, top + 1, 16777215);
            // guiGraphics.drawString(font, Component.translatable("screen.artnetconfig.entry.subnet", config.getSubnet()),  left, top + 1, 16777215 );
            // guiGraphics.drawString(font, Component.translatable("screen.artnetconfig.entry.universe", config.getUniverse()),  left, top + 4 + font.lineHeight, 16777215 );
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.parent.setSelected(this);
            return false;
        }
    }
}