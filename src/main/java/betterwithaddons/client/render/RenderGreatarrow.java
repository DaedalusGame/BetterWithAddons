package betterwithaddons.client.render;

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.item.ItemGreatarrow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderGreatarrow extends RenderArrow {
    public static final IRenderFactory GREATARROW_RENDER = new IRenderFactory() {
        @Override
        public Render createRenderFor(RenderManager renderManager) {
            return new RenderGreatarrow(renderManager);
        }
    };

    public RenderGreatarrow(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        EntityGreatarrow arrow = (EntityGreatarrow) entity;
        ItemGreatarrow arrowType = arrow.getArrowType();
        if(arrowType != null)
            return arrowType.getEntityTexture(arrow);
        else
            return ItemGreatarrow.TEXTURE;
    }

    @Override
    public void doRender(EntityArrow t, double d, double d2, double d3, float f, float f2) {
        this.bindEntityTexture(t);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)d, (float)d2, (float)d3);
        GlStateManager.rotate(t.prevRotationYaw + (t.rotationYaw - t.prevRotationYaw) * f2 - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(t.prevRotationPitch + (t.rotationPitch - t.prevRotationPitch) * f2, 0.0f, 0.0f, 1.0f);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        boolean bl = false;
        float f3 = 0.0f;
        float f4 = 0.5f;
        float f5 = 0.0f;
        float f6 = 0.15625f;
        float f7 = 0.0f;
        float f8 = 0.15625f;
        float f9 = 0.15625f;
        float f10 = 0.3125f;
        float f11 = 0.05625f;
        GlStateManager.enableRescaleNormal();
        float f12 = (float)t.arrowShake - f2;
        if (f12 > 0.0f) {
            float f13 = (- MathHelper.sin(f12 * 3.0f)) * f12;
            GlStateManager.rotate(f13, 0.0f, 0.0f, 1.0f);
        }
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.05625f * 2f, 0.05625f * 2f, 0.05625f * 2f);
        GlStateManager.translate(-4.0f, 0.0f, 0.0f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(t));
        }
        GlStateManager.glNormal3f(0.05625f, 0.0f, 0.0f);
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-7.0, -2.0, -2.0).tex(0.0, 0.15625).endVertex();
        vertexBuffer.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.15625).endVertex();
        vertexBuffer.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.3125).endVertex();
        vertexBuffer.pos(-7.0, 2.0, -2.0).tex(0.0, 0.3125).endVertex();
        tessellator.draw();
        GlStateManager.glNormal3f(-0.05625f, 0.0f, 0.0f);
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        vertexBuffer.pos(-7.0, 2.0, -2.0).tex(0.0, 0.15625).endVertex();
        vertexBuffer.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.15625).endVertex();
        vertexBuffer.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.3125).endVertex();
        vertexBuffer.pos(-7.0, -2.0, -2.0).tex(0.0, 0.3125).endVertex();
        tessellator.draw();
        for (int i = 0; i < 4; ++i) {
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.glNormal3f(0.0f, 0.0f, 0.05625f);
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            vertexBuffer.pos(-8.0, -2.0, 0.0).tex(0.0, 0.0).endVertex();
            vertexBuffer.pos(8.0, -2.0, 0.0).tex(0.5, 0.0).endVertex();
            vertexBuffer.pos(8.0, 2.0, 0.0).tex(0.5, 0.15625).endVertex();
            vertexBuffer.pos(-8.0, 2.0, 0.0).tex(0.0, 0.15625).endVertex();
            tessellator.draw();
        }
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}