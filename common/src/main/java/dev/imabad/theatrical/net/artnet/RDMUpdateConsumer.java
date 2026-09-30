package dev.imabad.theatrical.net.artnet;

import ch.bildspur.artnet.rdm.RDMDeviceId;
import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.Theatrical;
import dev.imabad.theatrical.blockentities.interfaces.RedstoneInterfaceBlockEntity;
import dev.imabad.theatrical.blockentities.light.BaseDMXConsumerLightBlockEntity;
import dev.imabad.theatrical.networks.TheatricalNetwork;
import dev.imabad.theatrical.networks.TheatricalNetworkData;
import dev.imabad.theatrical.net.TheatricalNet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public record RDMUpdateConsumer(UUID networkId, int universe, RDMDeviceId dmxDevice, int newAddress) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, RDMUpdateConsumer> CODEC = StreamCodec.ofMember(RDMUpdateConsumer::write, RDMUpdateConsumer::new);

    public RDMUpdateConsumer(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readInt(), new RDMDeviceId(buf.readByteArray(6)), buf.readInt());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.RDM_UPDATE_FIXTURE_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(networkId);
        buf.writeInt(universe);
        buf.writeByteArray(dmxDevice.toBytes());
        buf.writeInt(newAddress);
    }

    public void handle(NetworkManager.PacketContext context) {
        Level level = context.getPlayer().level();
        if(level.getServer() != null ) {
            TheatricalNetwork network = TheatricalNetworkData.getInstance(level.getServer().overworld()).getNetwork(networkId);
            if(network == null || !network.members().canSendDMX(context.getPlayer().getUUID())) {
                Theatrical.LOGGER.info("{} tried to send an RDM update for a network that doesn't exist or isn't part of", context.getPlayer().getName().getString());
                return;
            }
            BlockPos consumerPos = network.dmx().getConsumerPos(universe, dmxDevice);
            if(consumerPos != null){
                BlockEntity be = context.getPlayer().level().getBlockEntity(consumerPos);
                if(be instanceof BaseDMXConsumerLightBlockEntity dmxConsumerLightBlock){
                    dmxConsumerLightBlock.setChannelStartPoint(newAddress);
                } else if(be instanceof RedstoneInterfaceBlockEntity redstoneInterfaceBlockEntity){
                    redstoneInterfaceBlockEntity.setChannelStartPoint(newAddress);
                }
            }
        }
    }
}