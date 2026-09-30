package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.items.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.UUID;

public record ConfigureConfigurationCard(UUID network, int dmxAddress, int dmxUniverse, boolean autoIncrement, boolean universeEnabled, boolean addressEnabled) implements CustomPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, ConfigureConfigurationCard> CODEC = StreamCodec.ofMember(ConfigureConfigurationCard::write, ConfigureConfigurationCard::new);

    public ConfigureConfigurationCard(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readInt(), buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TheatricalNet.CONFIGURE_CONFIGURATION_CARD_TYPE;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(network);
        buf.writeInt(dmxAddress);
        buf.writeInt(dmxUniverse);
        buf.writeBoolean(autoIncrement);
        buf.writeBoolean(universeEnabled);
        buf.writeBoolean(addressEnabled);
    }

    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Player player = context.getPlayer();
            ItemStack itemStack = null;
            if(player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.CONFIGURATION_CARD.get())){
                itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            } else if(player.getItemInHand(InteractionHand.OFF_HAND).is(Items.CONFIGURATION_CARD.get())){
                itemStack = player.getItemInHand(InteractionHand.OFF_HAND);
            }
            if(itemStack != null){
                CompoundTag dataTag = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
                dataTag.putUUID("network", network);
                dataTag.putInt("dmxUniverse", dmxUniverse);
                dataTag.putInt("dmxAddress", dmxAddress);
                dataTag.putBoolean("autoIncrement", autoIncrement);
                dataTag.putBoolean("universeEnabled", universeEnabled);
                dataTag.putBoolean("addressEnabled", addressEnabled);
                itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
            }
        });
    }
}