package betterwithaddons.client.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;

public final class ParticleRenderDispatcher {

    public static int wispFxCount = 0;
    // Called from LightningHandler.onRenderWorldLast since that was
    // already registered. /shrug
    public static void dispatch() {
        Tessellator tessellator = Tessellator.getInstance();

        Profiler profiler = Minecraft.getMinecraft().mcProfiler;

        //GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        //GlStateManager.depthMask(false);
        //GlStateManager.enableBlend();
        //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        //GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
        //GlStateManager.enableLighting();

        //profiler.startSection("leaf");
        //FXLeafParticle.dispatchQueuedRenders(tessellator);
        //profiler.endSection();

        //GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        //GlStateManager.disableBlend();
        //GlStateManager.depthMask(true);
        //GL11.glPopAttrib();
    }
}