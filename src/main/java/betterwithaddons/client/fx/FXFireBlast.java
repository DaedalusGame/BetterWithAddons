package betterwithaddons.client.fx;

import betterwithaddons.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

public class FXFireBlast extends Particle {
    public static final ResourceLocation particles = new ResourceLocation(Reference.MOD_ID,"textures/entity/fire_blast.png");

    public FXFireBlast(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, float size, int time) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0, 0, 0);

        motionX = motionY = motionZ = 0;
        particleMaxAge = time;
        particleScale *= size;

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
        int frame = (int)MathHelper.clampedLerp(0,10,(float)this.particleAge / this.particleMaxAge);
        float sizex = 96f / 512f;
        float sizey = 96f / 512f;
        float minx = sizex * (frame % 5);
        float maxx = sizex * (frame % 5) + sizex;
        float miny = sizey * (frame / 5);
        float maxy = sizey * (frame / 5) + sizey;
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
