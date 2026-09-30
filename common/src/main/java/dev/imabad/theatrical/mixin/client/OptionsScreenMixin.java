package dev.imabad.theatrical.mixin.client;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.client.gui.screen.ArtNetConfigurationScreen;
import dev.imabad.theatrical.net.artnet.RequestNetworks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {

    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @Shadow protected abstract Button openScreenButton(Component message, Supplier<Screen> screenSupplier);

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        if (Minecraft.getInstance().level != null) {
            NetworkManager.sendToServer(new RequestNetworks());
            this.addRenderableWidget(
                    this.openScreenButton(
                            Component.translatable("button.artnetconfig"),
                            () -> new ArtNetConfigurationScreen((OptionsScreen) (Object) this)
                    )
            );
        }
    }
}