package dev.imabad.theatrical.compat;

import foundry.veil.api.client.render.VeilRenderSystem;
import foundry.veil.api.client.render.light.data.PointLightData;
import foundry.veil.api.client.render.light.renderer.LightRenderHandle;
import foundry.veil.api.client.render.light.renderer.LightRenderer;

import dev.imabad.theatrical.api.DynamicLightProvider;
import dev.imabad.theatrical.blockentities.light.BaseLightBlockEntity;
import net.minecraft.core.BlockPos;
import org.joml.Vector3f;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class VeilCompat {

    // The map now stores LightRenderHandles instead of PointLights
    private static final Map<BlockPos, LightRenderHandle<PointLightData>> pos2LightHandle = new ConcurrentHashMap<>();
    private static final ReentrantReadWriteLock lightSourcesLock = new ReentrantReadWriteLock();

    public static void setup() {}

    public static void addLight(DynamicLightProvider dynamicLightProvider) {
        if (!ModCompat.VEIL) return;
        lightSourcesLock.writeLock().lock();
        try {
            // Guard against null positions before raytracing finishes
            if (dynamicLightProvider.getLightPos() == null) return;

            Vector3f lightPos = dynamicLightProvider.getLightPos();

            PointLightData lightData = new PointLightData()
                    .setPosition(lightPos.x, lightPos.y, lightPos.z)
                    .setRadius(dynamicLightProvider.getLightSpread())
                    .setColor(dynamicLightProvider.getLightColour());

            LightRenderer lightRenderer = VeilRenderSystem.renderer().getLightRenderer();
            if (lightRenderer != null) {
                LightRenderHandle handle = lightRenderer.addLight(lightData);
                if (handle != null) {
                    pos2LightHandle.put(dynamicLightProvider.getOwnerPos(), handle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lightSourcesLock.writeLock().unlock();
        }
    }

    public static void removeLight(BlockPos fixturePos) {
        if (!ModCompat.VEIL) return;
        lightSourcesLock.writeLock().lock();
        try {
            LightRenderHandle handle = pos2LightHandle.remove(fixturePos);
            if (handle != null) {
                LightRenderer lightRenderer = VeilRenderSystem.renderer().getLightRenderer();
                if (lightRenderer != null) {
                    handle.free();
                }
            }
        } finally {
            lightSourcesLock.writeLock().unlock();
        }
    }

    public static void worldClose() {
        if (!ModCompat.VEIL) return;
        lightSourcesLock.writeLock().lock();
        try {
            LightRenderer lightRenderer = VeilRenderSystem.renderer().getLightRenderer();
            if (lightRenderer != null) {
                pos2LightHandle.values().forEach(LightRenderHandle::free);
            }
            pos2LightHandle.clear();
        } finally {
            lightSourcesLock.writeLock().unlock();
        }
    }

    public static void handleLightUpdate(BaseLightBlockEntity light) {
        if (!ModCompat.VEIL) return;
        LightRenderHandle handle = pos2LightHandle.get(light.getBlockPos());
        if (handle != null) {
            // Get the data from the handle, update it, and mark it as changed
            PointLightData lightData = (PointLightData) handle.getLightData();
            if (lightData != null) {
                Vector3f lightPos = light.getLightPos();
                lightData.setPosition(lightPos.x, lightPos.y, lightPos.z);
                lightData.setRadius(light.getLightSpread());
                lightData.setColor(light.isLightEnabled() ? light.getLightColour() : 0);

                // Some data-driven ECS systems require you to tell the handle it updated
                handle.markDirty();
            }
        }
    }
}