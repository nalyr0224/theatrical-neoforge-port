package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.blockentities.light.BaseLightBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public record UpdateFixturePosition(BlockPos pos, int tilt, int pan) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, UpdateFixturePosition> CODEC = StreamCodec.ofMember(UpdateFixturePosition::write, UpdateFixturePosition::new);

    public UpdateFixturePosition(FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.UPDATE_FIXTURE_POS_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(tilt);
        buf.writeInt(pan);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(pos);
        if(be instanceof BaseLightBlockEntity baseLightBlockEntity){
            baseLightBlockEntity.setPan(pan);
            baseLightBlockEntity.setTilt(tilt);
        }
    }
}