package dev.imabad.theatrical.neoforge;

import dev.imabad.theatrical.Theatrical;
import dev.imabad.theatrical.TheatricalClient;
import dev.imabad.theatrical.api.Fixture;
import dev.imabad.theatrical.fixtures.Fixtures;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// The 'bus' parameter is removed, replaced with 'modid'
@EventBusSubscriber(modid = Theatrical.MOD_ID, value = Dist.CLIENT)
public class TheatricalNeoForgeClient {

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional additionalEvent) {
        for(Fixture fixture : Fixtures.FIXTURES){
            if(fixture.getStaticModel() != null) {
                additionalEvent.register(new ModelResourceLocation(fixture.getStaticModel(), "standalone"));
            }
            if(fixture.hasPanModel() && fixture.getPanModel() != null) {
                additionalEvent.register(new ModelResourceLocation(fixture.getPanModel(), "standalone"));
            }
            if(fixture.hasTiltModel() && fixture.getTiltModel() != null) {
                additionalEvent.register(new ModelResourceLocation(fixture.getTiltModel(), "standalone"));
            }
        }
    }

    @SubscribeEvent
    public static void onClient(FMLClientSetupEvent event){
        TheatricalClient.init();
        NeoForge.EVENT_BUS.addListener((RenderLevelStageEvent renderLevelStageEvent) -> {
            if (renderLevelStageEvent.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
                TheatricalClient.renderWorldLastAfterTripwire(renderLevelStageEvent.getLevelRenderer());
            }
            if (renderLevelStageEvent.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
                TheatricalClient.renderWorldLast(
                        renderLevelStageEvent.getPoseStack(),
                        renderLevelStageEvent.getProjectionMatrix(),
                        renderLevelStageEvent.getCamera(),
                        renderLevelStageEvent.getPartialTick().getGameTimeDeltaPartialTick(false)
                );
            }
        });
    }
}