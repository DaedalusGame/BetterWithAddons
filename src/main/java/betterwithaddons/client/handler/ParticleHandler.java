package betterwithaddons.client.handler;

import betterwithaddons.client.fx.ParticleRenderDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by Christian on 04.10.2016.
 */
public class ParticleHandler {
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        ParticleRenderDispatcher.dispatch();
    }
}
