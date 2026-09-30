package dev.imabad.theatrical.net.artnet;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.TheatricalClient;
import dev.imabad.theatrical.net.TheatricalNet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record NotifyNetworks(Map<UUID, String> networks) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, NotifyNetworks> CODEC = StreamCodec.ofMember(NotifyNetworks::write, NotifyNetworks::new);

    public NotifyNetworks(FriendlyByteBuf buf) {
        this(readNetworks(buf));
    }

    private static Map<UUID, String> readNetworks(FriendlyByteBuf buf) {
        Map<UUID, String> map = new HashMap<>();
        int count = buf.readInt();
        for(int i = 0; i < count; i++){
            String name = buf.readUtf();
            UUID uuid = buf.readUUID();
            map.put(uuid, name);
        }
        return map;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.NOTIFY_NETWORKS_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(networks.size());
        for (Map.Entry<UUID, String> entry : networks.entrySet()) {
            buf.writeUtf(entry.getValue());
            buf.writeUUID(entry.getKey());
        }
    }

    public void handle(NetworkManager.PacketContext context) {
        TheatricalClient.getArtNetManager().populateNetworks(networks);
    }
}