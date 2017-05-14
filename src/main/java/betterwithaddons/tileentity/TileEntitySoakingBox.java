package betterwithaddons.tileentity;

import betterwithaddons.crafting.manager.CraftingManagerCherryBox;
import betterwithaddons.crafting.manager.CraftingManagerSoakingBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class TileEntitySoakingBox extends TileEntityCherryBox {
    @Override
    public CraftingManagerCherryBox getManager() {
        return CraftingManagerSoakingBox.instance();
    }

    @Override
    public boolean isValidStructure()
    {
        //DON'T ASK QUESTIONS YOU'RE NOT READY
        for (int z = -1; z <= 1; z++)
            for (int x = -1; x <= 1; x++)
            {
                if((z != x || x != 0) && !isWater(world.getBlockState(pos.add(x,0,z))))
                    return false;
            }

        return isIce(world.getBlockState(pos.up())) && isWater(world.getBlockState(pos.down()));
    }

    public boolean isWater(IBlockState state)
    {
        return state.getMaterial() == Material.WATER;
    }

    public boolean isIce(IBlockState state)
    {
        return state.getBlock() == Blocks.ICE;
    }
}
