package betterwithaddons.tileentity;

import betterwithaddons.block.BlockRopePost;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRopePost extends TileEntityBase {
    ItemStack plankStack = ItemStack.EMPTY;
    IBlockState fenceState;

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

    public void setFenceState(IBlockState state)
    {
        fenceState = state;
        validateState();
        markDirty();
    }

    public IBlockState getFenceState() { return fenceState; }

    public void validateState()
    {
        if(world  == null)
            return;
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockRopePost)
        {
            if(state.getValue(BlockRopePost.HAS_PLANKS) == plankStack.isEmpty())
                world.setBlockState(pos,state.withProperty(BlockRopePost.HAS_PLANKS,!plankStack.isEmpty()));
            if(state.getValue(BlockRopePost.HAS_POST) == (fenceState == null))
                world.setBlockState(pos,state.withProperty(BlockRopePost.HAS_POST,fenceState != null));
        }
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        if (fenceState != null) {
            compound.setString("Fence", Block.REGISTRY.getNameForObject(fenceState.getBlock()).toString());
            compound.setInteger("FenceMeta", fenceState.getBlock().getMetaFromState(fenceState));
        }
        if(!plankStack.isEmpty())
            compound.setTag("Planks",plankStack.serializeNBT());
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        if(compound.hasKey("Fence") && compound.hasKey("FenceMeta"))
        {
            Block block = Block.getBlockFromName(compound.getString("Fence"));
            if(block != Blocks.AIR)
                fenceState = block.getStateFromMeta(compound.getInteger("FenceMeta"));
        }
        if(compound.hasKey("Planks"))
            setPlanks(new ItemStack(compound.getCompoundTag("Planks")));
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }
}
