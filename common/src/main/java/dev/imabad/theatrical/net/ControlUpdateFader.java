package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.blockentities.control.BasicLightingDeskBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public record ControlUpdateFader(BlockPos blockPos, int fader, int value) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ControlUpdateFader> CODEC = StreamCodec.ofMember(ControlUpdateFader::write, ControlUpdateFader::new);

    public ControlUpdateFader(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.UPDATE_CONSOLE_FADER_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(fader);
        buf.writeInt(value);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(blockPos);
        if(be instanceof BasicLightingDeskBlockEntity lightingDeskBlock){
            lightingDeskBlock.setFader(fader, value);
        }
    }
}