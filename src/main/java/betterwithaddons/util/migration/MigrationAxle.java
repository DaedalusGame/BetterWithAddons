package betterwithaddons.util.migration;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAxle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MigrationAxle extends Migration {
    EnumFacing.Axis axis;

    public MigrationAxle() {
        super("axle");
    }

    @Override
    public void placeBlock(World world, BlockPos pos) {
        world.setBlockState(pos, BWMBlocks.AXLE.getDefaultState().withProperty(BlockAxle.AXIS,axis));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        compound.setInteger("axis",axis.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        axis = EnumFacing.Axis.values()[compound.getInteger("axis")];
    }
}
