package dev.imabad.theatrical.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.function.Consumer;

public class BetterCheckbox extends AbstractButton {
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/checkbox.png");
    private Consumer<Boolean> onChange;
    private boolean selected;
    private final boolean showLabel;

    public BetterCheckbox(int x, int y, int width, int height, Component message, boolean selected) {
        super(x, y, width, height, message);
        this.selected = selected;
        this.showLabel = true;
    }

    public void setOnChange(Consumer<Boolean> onChange) {
        this.onChange = onChange;
    }

    public boolean selected() {
        return this.selected;
    }

    @Override
    public void onPress() {
        this.selected = !this.selected;
        if(onChange != null){
            onChange.accept(this.selected);
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.enableDepthTest();
        Font font = minecraft.font;
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();

        guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.width, this.height, this.isFocused() ? 20.0F : 0.0F, this.selected ? 20.0F : 0.0F, 20, 20, 64, 64);
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.showLabel) {
            guiGraphics.drawString(font, this.getMessage(), this.getX() + 24, this.getY() + (this.height - 8) / 2, 14737632 | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}