package betterwithaddons.client.render;

import betterwithaddons.entity.EntitySpirit;
import betterwithaddons.interaction.InteractionEriottoMod;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class RenderSpirit extends Render<EntitySpirit>
{
    private static final ResourceLocation EXPERIENCE_ORB_TEXTURES = new ResourceLocation("textures/entity/experience_orb.png");

    public RenderSpirit(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntitySpirit entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (!this.renderOutlines)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y, (float)z);
            this.bindEntityTexture(entity);
            RenderHelper.enableStandardItemLighting();
            int i = entity.getTextureBySpirits();
            float f = (float)(i % 4 * 16 + 0) / 64.0F;
            float f1 = (float)(i % 4 * 16 + 16) / 64.0F;
            float f2 = (float)(i / 4 * 16 + 0) / 64.0F;
            float f3 = (float)(i / 4 * 16 + 16) / 64.0F;
            float f4 = 1.0F;
            float f5 = 0.5F;
            float f6 = 0.25F;
            int j = entity.getBrightnessForRender();
            int k = j % 65536;
            int l = j / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k, (float)l);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float f8 = 255.0F;
            float f9 = ((float)entity.xpColor + partialTicks) / 2.0F;
            float glow = MathHelper.sin(f9 + 0.0F);
            l = (int)((glow * 0.25F + 0.75F) * 255.0F);
            Color colorLow = InteractionEriottoMod.SPIRIT_COLOR_LOW;
            Color colorHigh = InteractionEriottoMod.SPIRIT_COLOR_HIGH;
            int r = (int)MathHelper.clampedLerp(colorLow.getRed(),colorHigh.getRed(), glow * 0.5F + 0.5F);
            int g = (int)MathHelper.clampedLerp(colorLow.getGreen(),colorHigh.getGreen(), glow * 0.5F + 0.5F);
            int b = (int)MathHelper.clampedLerp(colorLow.getBlue(),colorHigh.getBlue(), glow * 0.5F + 0.5F);
            int a = (int)MathHelper.clampedLerp(colorLow.getAlpha(),colorHigh.getAlpha(), glow * 0.5F + 0.5F);
            int i1 = 255;
            int j1 = (int)(((MathHelper.sin(f9 + 4.1887903F) + 1.0F) * 0.5F + 0.5F) * 255.0F);
            GlStateManager.translate(0.0F, 0.1F, 0.0F);
            GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            float f7 = 0.3F;
            GlStateManager.scale(0.3F, 0.3F, 0.3F);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
            vertexbuffer.pos(-0.5D, -0.25D, 0.0D).tex((double)f, (double)f3).color(r, g, b, a).normal(0.0F, 1.0F, 0.0F).endVertex();
            vertexbuffer.pos(0.5D, -0.25D, 0.0D).tex((double)f1, (double)f3).color(r, g, b, a).normal(0.0F, 1.0F, 0.0F).endVertex();
            vertexbuffer.pos(0.5D, 0.75D, 0.0D).tex((double)f1, (double)f2).color(r, g, b, a).normal(0.0F, 1.0F, 0.0F).endVertex();
            vertexbuffer.pos(-0.5D, 0.75D, 0.0D).tex((double)f, (double)f2).color(r, g, b, a).normal(0.0F, 1.0F, 0.0F).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntitySpirit entity)
    {
        return EXPERIENCE_ORB_TEXTURES;
    }
}