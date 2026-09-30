package dev.imabad.theatrical.net.artnet;

import ch.bildspur.artnet.rdm.RDMDeviceId;
import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.TheatricalClient;
import dev.imabad.theatrical.dmx.DMXDevice;
import dev.imabad.theatrical.net.TheatricalNet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NotifyConsumerChange(int universe, ChangeType changeType, DMXDevice dmxDevice) implements CustomPacketPayload {

    public static final Type<NotifyConsumerChange> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("theatrical", "notify_consumer_change"));

    public static final StreamCodec<RegistryFriendlyByteBuf, NotifyConsumerChange> CODEC = StreamCodec.of(
            (buf, packet) -> packet.write(buf),
            NotifyConsumerChange::new
    );

    public enum ChangeType {
        ADD,
        UPDATE,
        REMOVE
    }

    public NotifyConsumerChange(RegistryFriendlyByteBuf buf) {
        this(
                buf.readInt(),
                ChangeType.valueOf(buf.readUtf()),
                buf.readBoolean() ? new DMXDevice(
                        new RDMDeviceId(buf.readByteArray(6)),
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt(),
                        buf.readInt(),
                        buf.readUtf(),
                        buf.readResourceLocation()
                ) : null
        );
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(universe);
        buf.writeUtf(changeType.name());
        buf.writeBoolean(dmxDevice != null);
        if (dmxDevice != null) {
            buf.writeByteArray(dmxDevice.getDeviceId().toBytes());
            buf.writeInt(dmxDevice.getDmxStartAddress());
            buf.writeInt(dmxDevice.getDmxChannelCount());
            buf.writeInt(dmxDevice.getDeviceTypeId());
            buf.writeInt(dmxDevice.getActivePersonality());
            buf.writeUtf(dmxDevice.getModelName());
            buf.writeResourceLocation(dmxDevice.getFixtureID());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> TheatricalClient.handleConsumerChange(this));
    }

    public int getUniverse() { return universe; }
    public ChangeType getChangeType() { return changeType; }
    public DMXDevice getDmxDevice() { return dmxDevice; }
}