package betterwithaddons.client.render;

import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityInfuser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderInfuser extends TileEntitySpecialRenderer<TileEntityInfuser>
{
    /** The texture for the book above the enchantment table. */
    private static final ResourceLocation TEXTURE_ORB = new ResourceLocation(Reference.MOD_ID,"textures/entity/infuser.png");

    @Override
    public void render(TileEntityInfuser te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        IBlockState state = te.getWorld().getBlockState(te.getPos());
        int packedLightMap = state.getPackedLightmapCoords(te.getWorld(),te.getPos());

        float currentTime = te.getSpirits();
        float start = 0f;
        float delta = 1f;
        float duration = InteractionEriottoMod.SOULSAND_MAX_SPIRITS;

        int blockx = te.getPos().getX();
        int blocky = te.getPos().getY();
        int blockz = te.getPos().getZ();

        currentTime = currentTime / duration - 1;
        float soulVal = -delta * (currentTime*currentTime*currentTime*currentTime - 1) + start;
        float t = (1 - partialTicks) * te.activeGlowLast + partialTicks * te.activeGlow;
        float activeVal = soulVal * t;

        double sineVal = 1.0 * t * t;

        this.bindTexture(TEXTURE_ORB);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 0.5F, y+1.1f, z + 0.5F);
        float animTime = te.animLife + partialTicks;
        float angleTime = animTime / 20f;
        GlStateManager.rotate(animTime / 0.5f, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(animTime / 0.3f, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(animTime / 0.7f, 0.0F, 0.0F, 1.0F);

        double movex = Math.sin(angleTime / ((blockx * 37) % 10 + 1));
        double movey = Math.sin(angleTime / ((blocky * 7) % 10 + 1));
        double movez = Math.sin(angleTime / ((blockz * 101) % 10 + 1));
        double movedist = MathHelper.clampedLerp(0.0,Math.sin(angleTime / 5) * 0.2,sineVal);

        GlStateManager.translate(movex * movedist, movey * movedist, movez * movedist);

        //GlStateManager.rotate((float)(-Math.atan2((double)f3, (double)f1)) * (180F / (float)Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);

        int r = (int)MathHelper.clampedLerp(100,255,activeVal);
        int g = (int)MathHelper.clampedLerp(100,0,activeVal);
        int b = (int)MathHelper.clampedLerp(100,0,activeVal);

        int lightmapfull = 0x00F000F0;

        int lightmapx1 = (packedLightMap >> 4) & 0xF;
        int lightmapy1 = (packedLightMap >> 20) & 0xF;
        int lightmapx2 = (lightmapfull >> 4) & 0xF;
        int lightmapy2 = (lightmapfull >> 20) & 0xF;
        int lightmapv = (int)MathHelper.clampedLerp(lightmapx1,lightmapx2,activeVal) << 4;
        int lightmapu = (int)MathHelper.clampedLerp(lightmapy1,lightmapy2,activeVal) << 4;

        double pi = Math.PI;
        double twopi = Math.PI * 2;

        double size = 0.2;
        int detail_ver = 8;
        int detail_hor = detail_ver * 2;
        for (int u = 0; u < detail_hor; u++)
            for (int v = 0; v < detail_ver; v++)
        {
            double unorm1 = u / (double)detail_hor;
            double vnorm1 = v / (double)detail_ver;
            double unorm2 = (u+1) / (double)detail_hor;
            double vnorm2 = (v+1) / (double)detail_ver;

            bufferbuilder.pos(Math.sin(twopi * unorm1) * Math.sin(pi * vnorm1) * size, Math.cos(pi * vnorm1) * size, Math.cos(twopi * unorm1) * Math.sin(pi * vnorm1) * size).tex(unorm1, vnorm1).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
            bufferbuilder.pos(Math.sin(twopi * unorm2) * Math.sin(pi * vnorm1) * size, Math.cos(pi * vnorm1) * size, Math.cos(twopi * unorm2) * Math.sin(pi * vnorm1) * size).tex(unorm2, vnorm1).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
            bufferbuilder.pos(Math.sin(twopi * unorm2) * Math.sin(pi * vnorm2) * size, Math.cos(pi * vnorm2) * size,  Math.cos(twopi * unorm2) * Math.sin(pi * vnorm2) * size).tex(unorm2, vnorm2).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
            bufferbuilder.pos(Math.sin(twopi * unorm1) * Math.sin(pi * vnorm2) * size, Math.cos(pi * vnorm2) * size, Math.cos(twopi * unorm1) * Math.sin(pi * vnorm2) * size).tex(unorm1, vnorm2).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableCull();
        GlStateManager.shadeModel(7424);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableCull();
        GlStateManager.shadeModel(7425);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
        bufferbuilder.pos(0, 12 / 16.0 - 0.01, 0).tex(0.0, 0.0).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
        bufferbuilder.pos(0, 12 / 16.0 - 0.01, 1).tex(1.0, 0.0).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
        bufferbuilder.pos(1, 12 / 16.0 - 0.01, 1).tex(1.0, 1.0).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
        bufferbuilder.pos(1, 12 / 16.0 - 0.01, 0).tex(0.0, 1.0).lightmap(lightmapu,lightmapv).color(r, b, g, 255).endVertex();
        tessellator.draw();
        GlStateManager.popMatrix();
    }
}