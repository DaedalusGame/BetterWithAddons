package betterwithaddons.tileentity;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityAqueductWater extends TileEntityBase {
    private int distanceFromSource;

    public int getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(int distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
        this.markDirty();
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setInteger("Distance", this.distanceFromSource);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        this.distanceFromSource = compound.getInteger("Distance");
    }

    public static int getMinDistance(World world, BlockPos checkpos)
    {
        int minDistance = 1000000;

        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            BlockPos neighborpos = checkpos.offset(facing);
            IBlockState state = world.getBlockState(neighborpos);

            if(state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0)
            {
                minDistance = Math.min(minDistance,0);
            }
            else
            {
                TileEntity te = world.getTileEntity(neighborpos);

                if (te instanceof TileEntityAqueductWater) {
                    minDistance = Math.min(minDistance,((TileEntityAqueductWater) te).getDistanceFromSource());
                }
            }
        }

        return minDistance;
    }
}
