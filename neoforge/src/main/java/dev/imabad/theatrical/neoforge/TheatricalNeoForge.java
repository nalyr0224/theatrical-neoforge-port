package dev.imabad.theatrical.neoforge;

import dev.imabad.theatrical.Theatrical;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod(Theatrical.MOD_ID)
public class TheatricalNeoForge {

    public TheatricalNeoForge(IEventBus modEventBus) {
        Theatrical.init();
    }
}