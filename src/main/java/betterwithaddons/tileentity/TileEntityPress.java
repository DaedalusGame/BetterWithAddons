package betterwithaddons.tileentity;

import betterwithmods.api.tile.IMechanicalPower;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityPress extends TileEntityBase implements IMechanicalPower {
    private int progress;

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public Block getBlock() {
        return blockType;
    }

    @Override
    public World getBlockWorld() {
        return world;
    }

    @Override
    public BlockPos getBlockPos() {
        return pos;
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {

    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {

    }
}
