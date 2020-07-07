package betterwithaddons.client.fx;

import betterwithaddons.BetterWithAddons;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FXFireExplosion extends Particle {
    float radius;
    float size;
    int time;

    public FXFireExplosion(World worldIn, double posXIn, double posYIn, double posZIn, float size, float radius, int time, int count) {
        super(worldIn, posXIn, posYIn, posZIn);

        motionX = motionY = motionZ = 0;
        particleMaxAge = count * 2;

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        this.radius = radius;
        this.size = size;
        this.time = time;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(particleAge % 2 == 0) {
            Vec3d off = getRandomOffset(radius);
            BetterWithAddons.proxy.makeFireBlastFX(world, posX + off.x, posY + off.y, posZ + off.z, size, time);
        }
    }

    private Vec3d getRandomOffset(double radius) {
        while(true) {
            double dx = rand.nextDouble() * 2 - 1;
            double dy = rand.nextDouble() * 2 - 1;
            double dz = rand.nextDouble() * 2 - 1;
            if (dx != 0 || dy != 0 || dz != 0) {
                return new Vec3d(dx,dy,dz).normalize().scale(radius);
            }
        }
    }
}
