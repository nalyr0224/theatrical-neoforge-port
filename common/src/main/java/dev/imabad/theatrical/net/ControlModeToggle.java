package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.blockentities.control.BasicLightingDeskBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public record ControlModeToggle(BlockPos blockPos) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ControlModeToggle> CODEC = StreamCodec.ofMember(ControlModeToggle::write, ControlModeToggle::new);

    public ControlModeToggle(FriendlyByteBuf buf){
        this(buf.readBlockPos());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.CONTROL_MODE_TOGGLE_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(blockPos);
        if(be instanceof BasicLightingDeskBlockEntity lightingDeskBlock){
            lightingDeskBlock.toggleMode();
        }
    }
}