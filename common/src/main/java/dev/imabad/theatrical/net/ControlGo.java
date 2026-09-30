package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.blockentities.control.BasicLightingDeskBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public record ControlGo(BlockPos blockPos, int fadeInTicks, int fadeOutTicks) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ControlGo> CODEC = StreamCodec.ofMember(ControlGo::write, ControlGo::new);

    public ControlGo(FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.CONTROL_GO_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(fadeInTicks);
        buf.writeInt(fadeOutTicks);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(blockPos);
        if(be instanceof BasicLightingDeskBlockEntity lightingDeskBlock){
            if(!lightingDeskBlock.isRunMode()){
                lightingDeskBlock.setFadeInTicks(fadeInTicks);
                lightingDeskBlock.setFadeOutTicks(fadeOutTicks);
            }
            lightingDeskBlock.clickButton();
        }
    }
}