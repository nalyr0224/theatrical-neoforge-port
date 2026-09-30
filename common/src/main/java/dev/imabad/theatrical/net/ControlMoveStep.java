package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.blockentities.control.BasicLightingDeskBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public record ControlMoveStep(BlockPos blockPos, boolean forward) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ControlMoveStep> CODEC = StreamCodec.ofMember(ControlMoveStep::write, ControlMoveStep::new);

    public ControlMoveStep(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.CONTROL_MOVE_STEP_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeBoolean(forward);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(blockPos);
        if(be instanceof BasicLightingDeskBlockEntity lightingDeskBlock){
            if(forward) {
                lightingDeskBlock.moveForward();
            } else {
                lightingDeskBlock.moveBack();
            }
        }
    }
}