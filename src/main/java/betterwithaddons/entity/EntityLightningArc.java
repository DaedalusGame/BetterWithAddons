package betterwithaddons.entity;

import betterwithaddons.util.MiscUtil;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityLightningArc extends Entity {
    double minAngle;
    double maxAngle;
    double range;
    int duration;

    public EntityLightningArc(World worldIn) {
        super(worldIn);
        this.noClip = true;
        this.isImmuneToFire = true;
    }

    public EntityLightningArc(World worldIn, double x, double y, double z) {
        this(worldIn);
        this.setPosition(x, y, z);
    }

    public void setup(double minAngle, double maxAngle, double range, int duration) {
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
        this.range = range;
        this.duration = duration;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(ticksExisted > duration)
            setDead();

        if(!world.isRemote) {
            double slide = (double) ticksExisted / duration;
            double arcRadius = MathHelper.clampedLerp(0, range, slide);
            double arcLength = Math.abs(MiscUtil.angleDistance(minAngle, maxAngle)) * arcRadius;
            for (int i = 0; i < arcLength; i += 4.0) {
                double arcAngle = MiscUtil.lerpAngle(minAngle, maxAngle, rand.nextDouble());
                double dx = Math.sin(arcAngle) * arcRadius;
                double dz = Math.cos(arcAngle) * arcRadius;
                double y = world.getHeight((int) (posX + dx), (int) (posZ + dz));
                world.addWeatherEffect(new EntityLightningBolt(world, posX + dx, y, posZ + dz, false));
            }
        }
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        minAngle = compound.getDouble("MinAngle");
        maxAngle = compound.getDouble("MaxAngle");
        range = compound.getDouble("Range");
        duration = compound.getInteger("Duration");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setDouble("MinAngle", minAngle);
        compound.setDouble("MaxAngle", maxAngle);
        compound.setDouble("Range", range);
        compound.setInteger("Duration", duration);
    }

    public EnumPushReaction getPushReaction() {
        return EnumPushReaction.IGNORE;
    }
}
