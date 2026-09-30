package dev.imabad.theatrical.net.artnet;

import ch.bildspur.artnet.rdm.RDMDeviceId;
import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.TheatricalClient;
import dev.imabad.theatrical.dmx.DMXDevice;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record ListConsumers(int universe, List<DMXDevice> dmxDevices) implements CustomPacketPayload {

    public static final Type<ListConsumers> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("theatrical", "list_consumers"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ListConsumers> CODEC = StreamCodec.of(
            (buf, packet) -> packet.write(buf),
            ListConsumers::new
    );

    public ListConsumers(RegistryFriendlyByteBuf buf) {
        this(
                buf.readInt(),
                readDmxDeviceList(buf)
        );
    }

    private static List<DMXDevice> readDmxDeviceList(RegistryFriendlyByteBuf buf) {
        int count = buf.readInt();
        List<DMXDevice> devices = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            devices.add(new DMXDevice(
                    new RDMDeviceId(buf.readByteArray(6)),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readInt(),
                    buf.readUtf(),
                    buf.readResourceLocation()
            ));
        }
        return devices;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(universe);
        buf.writeInt(dmxDevices.size());
        for (DMXDevice dmxDevice : dmxDevices) {
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
        context.queue(() -> TheatricalClient.handleListConsumers(this));
    }

    public int getUniverse() { return universe; }
    public List<DMXDevice> getDmxDevices() { return dmxDevices; }
}