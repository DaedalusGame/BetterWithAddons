package betterwithaddons.tileentity;

import betterwithaddons.crafting.manager.CraftingManagerCherryBox;
import betterwithaddons.crafting.manager.CraftingManagerDryingBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

public class TileEntityDryingBox extends TileEntityCherryBox {
    @Override
    public boolean isWorking()
    {
        return isValidStructure() && isValidBiome() && world.canSeeSky(pos) && world.isDaytime();
    }

    @Override
    public CraftingManagerCherryBox getManager() {
        return CraftingManagerDryingBox.instance();
    }

    public boolean isValidBiome()
    {
        if(world == null) return false;
        Biome biome = world.getBiome(pos);
        if(biome != null && biome.getTempCategory() == Biome.TempCategory.COLD)
            return false;
        return true;
    }

    public int getWorkSpeed()
    {
        if(world == null) return 0;
        Biome biome = world.getBiome(pos);
        if(biome == null) return 1;
        return biome.canRain() ? 1 : 2;
    }

    @Override
    public boolean isValidStructure()
    {
        boolean hasDeadBush = false;

        //DON'T ASK QUESTIONS YOU'RE NOT READY
        for (int z = -1; z <= 1; z++)
            for (int x = -1; x <= 1; x++)
            {
                IBlockState upperstate = world.getBlockState(pos.add(x,+1,z));
                if(isDeadBush(upperstate))
                    hasDeadBush = true;
                if((z != x || x != 0) && !isSand(world.getBlockState(pos.add(x,0,z))))
                    return false;
            }

        return hasDeadBush && isSandstone(world.getBlockState(pos.down()));
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
