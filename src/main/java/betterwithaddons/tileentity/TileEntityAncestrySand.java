package betterwithaddons.tileentity;

import betterwithaddons.entity.EntitySpirit;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAncestrySand extends TileEntityBase implements ITickable {
    private int spirits = 0;
    private int nextCheck = 0;
    private List<EntitySpirit> attractedSpirits = new ArrayList<>();

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setInteger("Spirits",spirits);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        spirits = compound.getInteger("Spirits");
    }

    @Override
    public void update() {
        if(world.isRemote)
            return;

        float maxdist = 8.0f;
        AxisAlignedBB aabb = new AxisAlignedBB(pos);

        if(nextCheck-- < 0)
        {
            attractedSpirits = world.getEntitiesWithinAABB(EntitySpirit.class,aabb.expandXyz(maxdist - 0.5));
            nextCheck = 100;
        }

        Vec3d middleOfBlock = new Vec3d(pos).addVector(0.5,0.5,0.5);

        //Can we even do this or is this awful?
        if(spirits < 128)
        for(EntitySpirit spirit : attractedSpirits)
        {
            double spiritdist = spirit.getDistanceSq(middleOfBlock.xCoord,middleOfBlock.yCoord,middleOfBlock.zCoord);

            if(spiritdist < 1.0f)
            {
                if(spirits < 128) {
                    int consume = Math.min(128 - spirits,spirit.xpValue);
                    spirits += consume;
                    spirit.xpValue -= consume;
                    if(spirit.xpValue <= 0)
                        spirit.setDead();
                }
                continue;
            }

            if(spiritdist > maxdist * maxdist)
                continue;

            double dx = (middleOfBlock.xCoord - spirit.posX) / 8.0D;
            double dy = (middleOfBlock.yCoord - spirit.posY) / 8.0D;
            double dz = (middleOfBlock.zCoord - spirit.posZ) / 8.0D;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double d5 = 1.0D - dist;

            if (d5 > 0.0D)
            {
                d5 = d5 * d5;
                spirit.motionX += dx / dist * d5 * 0.1D;
                spirit.motionY += dy / dist * d5 * 0.1D;
                spirit.motionZ += dz / dist * d5 * 0.1D;
            }
        }
    }
}
