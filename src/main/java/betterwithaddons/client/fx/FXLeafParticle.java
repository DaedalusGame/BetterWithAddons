package betterwithaddons.client.fx;

import betterwithaddons.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;
import java.util.Queue;

public class FXLeafParticle extends Particle {

    public static final ResourceLocation particles = new ResourceLocation(Reference.MOD_ID,"textures/entity/leaf.png");

    private static final Queue<FXLeafParticle> queuedRenders = new ArrayDeque<>();

    // Queue values
    private float partialTicks;
    private float rotationX;
    private float rotationZ;
    private float rotationYZ;
    private float rotationXY;
    private float rotationXZ;

    public FXLeafParticle(World world, double d, double d1, double d2, float size, float red, float green, float blue, boolean distanceLimit, float maxAge) {
        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
        particleRed = red;
        particleGreen = green;
        particleBlue = blue;
        particleAlpha = 0.5F; // So MC renders us on the alpha layer, value not actually used
        particleGravity = 0.1f;
        motionX = motionY = motionZ = 0;
        particleScale *= size;
        particleMaxAge = (int)((Math.random() * 0.3D + 0.7D) * maxAge);
        this.depthTest = true;

        setSize(0.01F, 0.01F);
        Entity renderentity = FMLClientHandler.instance().getClient().getRenderViewEntity();

        if(distanceLimit) {
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

    /*public static void dispatchQueuedRenders(Tessellator tessellator) {
        ParticleRenderDispatcher.wispFxCount = 0;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.75F);
        Minecraft.getMinecraft().renderEngine.bindTexture(particles);

        if(!queuedRenders.isEmpty()) {
            tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
            for(FXLeafParticle wisp : queuedRenders)
                wisp.renderQueued(tessellator);
            tessellator.draw();
        }

        queuedRenders.clear();
    }

    private void renderQueued(Tessellator tessellator) {
        ParticleRenderDispatcher.wispFxCount++;

        float f10 = 0.5F * particleScale;
        float f11 = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float f12 = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float f13 = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        int frame = (this.particleAge / 8) % 4;
        float minx = 0.25f * frame;
        float maxx = 0.25f * frame + 0.25f;
        float miny = 0;
        float maxy = 0.25f;
        tessellator.getBuffer().pos(f11 - rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 - rotationYZ * f10 - rotationXZ * f10).tex(maxx, maxy).color(particleRed, particleGreen, particleBlue, 0.5F).lightmap(j, k).endVertex();
        tessellator.getBuffer().pos(f11 - rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 - rotationYZ * f10 + rotationXZ * f10).tex(maxx, miny).color(particleRed, particleGreen, particleBlue, 0.5F).lightmap(j, k).endVertex();
        tessellator.getBuffer().pos(f11 + rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 + rotationYZ * f10 + rotationXZ * f10).tex(minx, miny).color(particleRed, particleGreen, particleBlue, 0.5F).lightmap(j, k).endVertex();
        tessellator.getBuffer().pos(f11 + rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 + rotationYZ * f10 - rotationXZ * f10).tex(minx, maxy).color(particleRed, particleGreen, particleBlue, 0.5F).lightmap(j, k).endVertex();
    }

    @Override
    public void renderParticle(VertexBuffer wr, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        this.partialTicks = partialTicks;
        this.rotationX = rotationX;
        this.rotationZ = rotationZ;
        this.rotationYZ = rotationYZ;
        this.rotationXY = rotationXY;
        this.rotationXZ = rotationXZ;

        if(depthTest)
            queuedRenders.add(this);
    }*/

    @Override
    public void renderParticle(VertexBuffer wr, Entity entity, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
        ParticleRenderDispatcher.wispFxCount++;

        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(particles);
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
        float f10 = 0.5F * particleScale;
        float f11 = (float)(prevPosX + (posX - prevPosX) * partialTicks - interpPosX);
        float f12 = (float)(prevPosY + (posY - prevPosY) * partialTicks - interpPosY);
        float f13 = (float)(prevPosZ + (posZ - prevPosZ) * partialTicks - interpPosZ);
        int i = this.getBrightnessForRender(partialTicks);
        int j = i >> 16 & 65535;
        int k = i & 65535;
        int frame = (this.particleAge / 8) % 4;
        float minx = 0.25f * frame;
        float maxx = 0.25f * frame + 0.25f;
        float miny = 0;
        float maxy = 0.25f;
        wr.pos(f11 - rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 - rotationYZ * f10 - rotationXZ * f10).tex(maxx, maxy).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        wr.pos(f11 - rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 - rotationYZ * f10 + rotationXZ * f10).tex(maxx, miny).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        wr.pos(f11 + rotationX * f10 + rotationXY * f10, f12 + rotationZ * f10, f13 + rotationYZ * f10 + rotationXZ * f10).tex(minx, miny).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        wr.pos(f11 + rotationX * f10 - rotationXY * f10, f12 - rotationZ * f10, f13 + rotationYZ * f10 - rotationXZ * f10).tex(minx, maxy).color(particleRed, particleGreen, particleBlue, 1.0F).lightmap(j, k).endVertex();
        Tessellator.getInstance().draw();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/particle/particles.png"));
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (particleAge++ >= particleMaxAge || this.isCollided)
            setExpired();

        motionY = Math.max(motionY - 0.04D * particleGravity,-0.1);
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
    }

    public void setGravity(float value) {
        particleGravity = value;
    }

    public void setSpeed(float mx, float my, float mz) {
        motionX = mx;
        motionY = my;
        motionZ = mz;
    }

    @Override
    public int getFXLayer() {
        return 0;
    }

    private boolean depthTest = true;
}