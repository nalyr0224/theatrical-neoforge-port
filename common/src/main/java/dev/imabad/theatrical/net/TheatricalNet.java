package dev.imabad.theatrical.net;

import dev.architectury.networking.NetworkManager;
import dev.imabad.theatrical.Theatrical;
import dev.imabad.theatrical.net.artnet.*;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class TheatricalNet {

    // Server -> Client (S2C) Types
    public static final CustomPacketPayload.Type<NotifyNetworks> NOTIFY_NETWORKS_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "notify_networks"));
    public static final CustomPacketPayload.Type<OpenScreen> OPEN_SCREEN_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "open_screen"));

    // Client -> Server (C2S) Types
    public static final CustomPacketPayload.Type<ConfigureConfigurationCard> CONFIGURE_CONFIGURATION_CARD_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "configure_configuration_card"));
    public static final CustomPacketPayload.Type<ControlGo> CONTROL_GO_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "control_go"));
    public static final CustomPacketPayload.Type<ControlModeToggle> CONTROL_MODE_TOGGLE_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "control_mode_toggle"));
    public static final CustomPacketPayload.Type<ControlMoveStep> CONTROL_MOVE_STEP_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "control_move_step"));
    public static final CustomPacketPayload.Type<ControlUpdateFader> UPDATE_CONSOLE_FADER_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "update_console_fader"));
    public static final CustomPacketPayload.Type<RDMUpdateConsumer> RDM_UPDATE_FIXTURE_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "rdm_update_fixture"));
    public static final CustomPacketPayload.Type<RequestConsumers> REQUEST_CONSUMERS_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "request_consumers"));
    public static final CustomPacketPayload.Type<RequestNetworks> REQUEST_NETWORKS_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "request_networks"));
    public static final CustomPacketPayload.Type<UpdateArtNetInterface> UPDATE_ARTNET_INTERFACE_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "update_artnet_interface"));
    public static final CustomPacketPayload.Type<UpdateDMXFixture> UPDATE_DMX_FIXTURE_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "update_dmx_fixture"));
    public static final CustomPacketPayload.Type<UpdateFixturePosition> UPDATE_FIXTURE_POS_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "update_fixture_pos"));
    public static final CustomPacketPayload.Type<SendArtNetData> SEND_ARTNET_TO_SERVER_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "send_artnet_to_server"));
    public static final CustomPacketPayload.Type<UpdateNetworkId> UPDATE_NETWORK_ID_TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Theatrical.MOD_ID, "update_network_id"));

    public static void init() {
        // Register S2C Payloads
        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                NotifyConsumerChange.TYPE,
                NotifyConsumerChange.CODEC,
                NotifyConsumerChange::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                ListConsumers.TYPE,
                ListConsumers.CODEC,
                ListConsumers::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                NOTIFY_NETWORKS_TYPE,
                NotifyNetworks.CODEC,
                NotifyNetworks::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.s2c(),
                OPEN_SCREEN_TYPE,
                OpenScreen.CODEC,
                OpenScreen::handle
        );

        // Register C2S Payloads
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                CONFIGURE_CONFIGURATION_CARD_TYPE,
                ConfigureConfigurationCard.CODEC,
                ConfigureConfigurationCard::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                CONTROL_GO_TYPE,
                ControlGo.CODEC,
                ControlGo::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                CONTROL_MODE_TOGGLE_TYPE,
                ControlModeToggle.CODEC,
                ControlModeToggle::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                CONTROL_MOVE_STEP_TYPE,
                ControlMoveStep.CODEC,
                ControlMoveStep::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                UPDATE_CONSOLE_FADER_TYPE,
                ControlUpdateFader.CODEC,
                ControlUpdateFader::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                RDM_UPDATE_FIXTURE_TYPE,
                RDMUpdateConsumer.CODEC,
                RDMUpdateConsumer::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                REQUEST_CONSUMERS_TYPE,
                RequestConsumers.CODEC,
                RequestConsumers::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                REQUEST_NETWORKS_TYPE,
                RequestNetworks.CODEC,
                RequestNetworks::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                UPDATE_ARTNET_INTERFACE_TYPE,
                UpdateArtNetInterface.CODEC,
                UpdateArtNetInterface::handle
        );
        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                UPDATE_DMX_FIXTURE_TYPE,
                UpdateDMXFixture.CODEC,
                UpdateDMXFixture::handle
        );

        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                UPDATE_FIXTURE_POS_TYPE,
                UpdateFixturePosition.CODEC,
                UpdateFixturePosition::handle
        );

        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                SEND_ARTNET_TO_SERVER_TYPE,
                SendArtNetData.CODEC,
                SendArtNetData::handle
        );

        NetworkManager.registerReceiver(
                NetworkManager.c2s(),
                UPDATE_NETWORK_ID_TYPE,
                UpdateNetworkId.CODEC,
                UpdateNetworkId::handle
        );
    }
}