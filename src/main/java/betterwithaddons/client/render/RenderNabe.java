package betterwithaddons.client.render;

import betterwithaddons.tileentity.TileEntityNabe;
import betterwithaddons.util.NabeResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNabe extends TileEntitySpecialRenderer<TileEntityNabe> {
    @Override
    public void render(TileEntityNabe tile, double x, double y, double z, float partialTicks, int destroyStage, float transparency) {
        NabeResult fill = tile.getFill();
        float ratio = fill.getFillRatio();
        if (ratio > 0) {
            int c = fill.getColor();
            int blue = c & 0xFF;
            int green = (c >> 8) & 0xFF;
            int red = (c >> 16) & 0xFF;
            int alpha = (c >> 24) & 0xFF;

            ResourceLocation texture = fill.getTexture();
            TextureAtlasSprite sprite = texture != null ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString()) : null;

            if (sprite != null) {
                float minU = sprite.getInterpolatedU(0.0f);
                float maxU = sprite.getInterpolatedU(16.0f);
                float minV = sprite.getInterpolatedV(0.0f);
                float maxV = sprite.getInterpolatedV(16.0f);

                FluidStack fluidStack = fill.getFluid();
                int light = fluidStack != null ? fluidStack.getFluid().getLuminosity(fluidStack) : 0;
                int i = getWorld().getCombinedLight(tile.getPos(), light);
                int lightx = i >> 0x10 & 0xFFFF;
                int lighty = i & 0xFFFF;

                GlStateManager.disableCull();
                GlStateManager.disableLighting();
                GlStateManager.enableBlend();
                GlStateManager.enableAlpha();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Tessellator tess = Tessellator.getInstance();
                BufferBuilder buffer = tess.getBuffer();
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
                buffer.pos(x + 0.1, y + 0.125 + 0.8125 * ratio, z + 0.1).tex(minU, minV).lightmap(lightx, lighty).color(red, green, blue, alpha).endVertex();
                buffer.pos(x + 0.9, y + 0.125 + 0.8125 * ratio, z + 0.1).tex(maxU, minV).lightmap(lightx, lighty).color(red, green, blue, alpha).endVertex();
                buffer.pos(x + 0.9, y + 0.125 + 0.8125 * ratio, z + 0.9).tex(maxU, maxV).lightmap(lightx, lighty).color(red, green, blue, alpha).endVertex();
                buffer.pos(x + 0.1, y + 0.125 + 0.8125 * ratio, z + 0.9).tex(minU, maxV).lightmap(lightx, lighty).color(red, green, blue, alpha).endVertex();
                tess.draw();

                GlStateManager.disableBlend();
                GlStateManager.enableLighting();
            }
        }

    }
}
