package betterwithaddons.tileentity;

import betterwithaddons.block.BlockRopeSideways;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRopeSideways extends TileEntityBase {
    ItemStack plankStack = ItemStack.EMPTY;

    public void setPlanks(ItemStack stack)
    {
        plankStack = stack;
        validateState();
        markDirty();
    }

    public ItemStack getPlanks()
    {
        return plankStack;
    }

    public void validateState()
    {
        if(world == null)
            return;
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockRopeSideways)
        {
            if(state.getValue(BlockRopeSideways.HAS_PLANKS) == plankStack.isEmpty())
                world.setBlockState(pos,state.withProperty(BlockRopeSideways.HAS_PLANKS,!plankStack.isEmpty()));
        }
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        if(!plankStack.isEmpty())
            compound.setTag("Planks",plankStack.serializeNBT());
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        if(compound.hasKey("Planks"))
            setPlanks(new ItemStack(compound.getCompoundTag("Planks")));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }
}
