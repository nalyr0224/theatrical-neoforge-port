package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.blockentities.interfaces.RedstoneInterfaceBlockEntity;
import dev.imabad.theatrical.blockentities.light.BaseDMXConsumerLightBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public record UpdateDMXFixture(BlockPos pos, int dmxAddress, int dmxUniverse) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, UpdateDMXFixture> CODEC = StreamCodec.ofMember(UpdateDMXFixture::write, UpdateDMXFixture::new);

    public UpdateDMXFixture(FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readInt(), buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.UPDATE_DMX_FIXTURE_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(dmxAddress);
        buf.writeInt(dmxUniverse);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(pos);
        if(be instanceof BaseDMXConsumerLightBlockEntity dmxConsumerLightBlock){
            dmxConsumerLightBlock.setChannelStartPoint(dmxAddress);
            dmxConsumerLightBlock.setUniverse(dmxUniverse);
        } else if(be instanceof RedstoneInterfaceBlockEntity redstoneInterfaceBlockEntity){
            redstoneInterfaceBlockEntity.setChannelStartPoint(dmxAddress);
            redstoneInterfaceBlockEntity.setUniverse(dmxUniverse);
        }
    }
}