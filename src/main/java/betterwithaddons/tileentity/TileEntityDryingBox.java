package betterwithaddons.tileentity;

import betterwithaddons.crafting.manager.CraftingManagerCherryBox;
import betterwithaddons.crafting.manager.CraftingManagerDryingBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class TileEntityDryingBox extends TileEntityCherryBox {
    @Override
    public boolean isWorking()
    {
        return isValidStructure() && worldObj.canSeeSky(pos) && worldObj.isDaytime();
    }

    @Override
    public CraftingManagerCherryBox getManager() {
        return CraftingManagerDryingBox.instance();
    }

    @Override
    public boolean isValidStructure()
    {
        boolean hasDeadBush = false;

        //DON'T ASK QUESTIONS YOU'RE NOT READY
        for (int z = -1; z <= 1; z++)
            for (int x = -1; x <= 1; x++)
            {
                IBlockState upperstate = worldObj.getBlockState(pos.add(x,+1,z));
                if(isDeadBush(upperstate))
                    hasDeadBush = true;
                if((z != x || x != 0) && !isSand(worldObj.getBlockState(pos.add(x,0,z))))
                    return false;
            }

        return hasDeadBush && isSandstone(worldObj.getBlockState(pos.down()));
    }

    public boolean isSand(IBlockState state)
    {
        return state.getBlock() == Blocks.SAND;
    }

    public boolean isSandstone(IBlockState state) { return state.getBlock() == Blocks.SANDSTONE || state.getBlock() == Blocks.RED_SANDSTONE; }

    public boolean isDeadBush(IBlockState state)
    {
        return state.getBlock() == Blocks.DEADBUSH;
    }
}
