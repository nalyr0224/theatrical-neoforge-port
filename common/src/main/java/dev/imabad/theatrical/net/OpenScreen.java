package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.TheatricalClient;
import dev.imabad.theatrical.TheatricalScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record OpenScreen(BlockPos pos, TheatricalScreen screen) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, OpenScreen> CODEC = StreamCodec.ofMember(OpenScreen::write, OpenScreen::new);

    public OpenScreen(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readEnum(TheatricalScreen.class));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.OPEN_SCREEN_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeEnum(screen);
    }

    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> TheatricalClient.handleOpenScreen(this));
    }
}