package betterwithaddons.client.fx;

import betterwithaddons.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;


public class FXLightning extends Particle {

    public static final ResourceLocation particles = new ResourceLocation(Reference.MOD_ID, "textures/entity/lightning.png");

    public FXLightning(World world, double d, double d1, double d2, float size, float red, float green, float blue, boolean distanceLimit, float maxAge) {
        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
        particleRed = red;
        particleGreen = green;
        particleBlue = blue;
        particleAlpha = 0.5F; // So MC renders us on the alpha layer, value not actually used
        particleGravity = 0.1f;
        motionX = motionY = motionZ = 0;
        particleScale *= size;
        particleMaxAge = (int) ((Math.random() * 0.3D + 0.7D) * maxAge);

        setSize(0.01F, 0.01F);
        Entity renderentity = FMLClientHandler.instance().getClient().getRenderViewEntity();

        if (distanceLimit) {
            int visibleDistance = 50;
            if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics)
                visibleDistance = 25;

            if (renderentity == null || renderentity.getDistance(posX, posY, posZ) > visibleDistance)
                particleMaxAge = 0;
        }

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
    }

    @Override
    public void renderParticle(BufferBuilder wr, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(particles);
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        float f10 = 0.5F * particleScale;
        float f11 = (float) (prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float f12 = (float) (prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float f13 = (float) (prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
        int i = 0x00F000F0;//this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        int frame = this.particleAge % 4;
        float minx = 0.5f * frame % 2;
        float maxx = 0.5f * frame % 2 + 0.5f;
        float miny = 0.5f * frame / 2;
        float maxy = 0.5f * frame / 2 + 0.5f;
        wr.pos(f11 - rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 - rotationYZ * f10 - rotationXZ * f10).tex(maxx, maxy).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        wr.pos(f11 - rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 - rotationYZ * f10 + rotationXZ * f10).tex(maxx, miny).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        wr.pos(f11 + rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 + rotationYZ * f10 + rotationXZ * f10).tex(minx, miny).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        wr.pos(f11 + rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 + rotationYZ * f10 - rotationXZ * f10).tex(minx, maxy).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/particle/particles.png"));
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }

    @Override
    public int getFXLayer() {
        return 0;
    }
}