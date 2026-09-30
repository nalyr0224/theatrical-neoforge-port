package dev.imabad.theatrical.net.artnet;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.Theatrical;
import dev.imabad.theatrical.api.dmx.DMXConsumer;
import dev.imabad.theatrical.networks.TheatricalNetwork;
import dev.imabad.theatrical.networks.TheatricalNetworkData;
import dev.imabad.theatrical.net.TheatricalNet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.UUID;

public record SendArtNetData(UUID networkId, int universe, byte[] artNetData) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, SendArtNetData> CODEC = StreamCodec.ofMember(SendArtNetData::write, SendArtNetData::new);

    public SendArtNetData(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readInt(), buf.readByteArray());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.SEND_ARTNET_TO_SERVER_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(networkId);
        buf.writeInt(universe);
        buf.writeByteArray(artNetData);
    }

    public void handle(NetworkManager.PacketContext context) {
        Level level = context.getPlayer().level();
        if(level.getServer() != null) {
            TheatricalNetwork network = TheatricalNetworkData.getInstance(level.getServer().overworld()).getNetwork(networkId);
            UUID uuid = context.getPlayer().getUUID();
            if(network != null) {
                if (network.members().isMember(uuid) && network.members().canSendDMX(uuid)) {
                    Collection<DMXConsumer> consumers = network.dmx().getConsumers(universe);
                    if(consumers != null) {
                        consumers.forEach(consumer -> {
                            consumer.consume(artNetData);
                        });
                    }
                } else {
                    Theatrical.LOGGER.info("{} tried to send ArtNet data to a network ({}) that they don't have permissions for", context.getPlayer().getName().getString(), network.name());
                }
            } else {
                Theatrical.LOGGER.info("{} tried to send ArtNet data to a network that doesn't exist.", context.getPlayer().getName().getString());
            }
        }
    }
}