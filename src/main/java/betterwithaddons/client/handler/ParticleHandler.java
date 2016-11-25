package betterwithaddons.client.handler;

import betterwithaddons.client.fx.ParticleRenderDispatcher;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ParticleHandler {
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        ParticleRenderDispatcher.dispatch();
    }
}
