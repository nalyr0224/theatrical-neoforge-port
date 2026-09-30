package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.blockentities.interfaces.ArtNetInterfaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

public record UpdateArtNetInterface(BlockPos pos, String ipAddress, int dmxUniverse) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, UpdateArtNetInterface> CODEC = StreamCodec.ofMember(UpdateArtNetInterface::write, UpdateArtNetInterface::new);

    public UpdateArtNetInterface(FriendlyByteBuf buf){
        this(buf.readBlockPos(), buf.readUtf(), buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.UPDATE_ARTNET_INTERFACE_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeUtf(ipAddress);
        buf.writeInt(dmxUniverse);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(pos);
        if(be instanceof ArtNetInterfaceBlockEntity artNetInterfaceBlockEntity){
//            if(artNetInterfaceBlockEntity.getOwnerUUID().equals(context.getPlayer().getUUID())){
//                artNetInterfaceBlockEntity.updateConfig(ipAddress, dmxUniverse);
//            }
        }
    }
}