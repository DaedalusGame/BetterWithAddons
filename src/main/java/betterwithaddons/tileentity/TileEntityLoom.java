package betterwithaddons.tileentity;

import betterwithaddons.block.BlockLoom;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.interaction.InteractionCondensedOutputs;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityLoom extends TileEntityBase implements IMechanicalPower, ITickable {
    private int spinupProcess = 0;
    private int power = 0;

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return -1;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);

        if(state.getBlock() != getBlock())
            return 0;

        if(facing != state.getValue(BlockLoom.FACING))
            return MechanicalUtil.getPowerOutput(world, pos.offset(facing), facing.getOpposite());
        return 0;
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public BlockLoom getBlock() {
        return ModBlocks.loom;
    }

    @Override
    public World getBlockWorld() {
        return getWorld();
    }

    @Override
    public BlockPos getBlockPos() {
        return getPos();
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setInteger("ProcessTime",spinupProcess);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        spinupProcess = compound.getInteger("ProcessTime");
    }

    @Override
    public void update() {
        if(this.getBlockWorld().isRemote)
            return;

        power = calculateInput();

        getBlock().setActive(world,pos,power > 0);

        if(power > 0) {
            spinupProcess++;

            if (spinupProcess > InteractionCondensedOutputs.SPINUP_TIME) {
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == getBlock())
                    getBlock().spinUpAllAttached(world, pos, state);
                spinupProcess = 0;
            }
        }
    }
}
