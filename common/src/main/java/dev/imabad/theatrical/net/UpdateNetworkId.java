package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.api.dmx.BelongsToNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public record UpdateNetworkId(BlockPos blockPos, UUID networkId) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, UpdateNetworkId> CODEC = StreamCodec.ofMember(UpdateNetworkId::write, UpdateNetworkId::new);

    public UpdateNetworkId(FriendlyByteBuf friendlyByteBuf){
        this(friendlyByteBuf.readBlockPos(), friendlyByteBuf.readUUID());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.UPDATE_NETWORK_ID_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeUUID(networkId);
    }

    public void handle(NetworkManager.PacketContext context) {
        BlockEntity be = context.getPlayer().level().getBlockEntity(blockPos);
        if(be instanceof BelongsToNetwork belongsToNetwork){
            belongsToNetwork.setNetworkId(networkId);
        }
    }
}