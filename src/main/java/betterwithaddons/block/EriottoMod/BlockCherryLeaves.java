package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockModLeaves;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.block.ModWoods;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by Christian on 04.10.2016.
 */
public class BlockCherryLeaves extends BlockModLeaves {

    public BlockCherryLeaves(ModWoods variant) {
        super(variant);

        setTickRandomly(true);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
        super.randomDisplayTick(stateIn, world, pos, rand);

        if(world.isAirBlock(pos.down()) && rand.nextFloat() < 0.05f) {
            BetterWithAddons.proxy.makeLeafFX(pos.getX()+rand.nextDouble(),pos.getY(),pos.getZ()+rand.nextDouble(),1.0f,0.5f,1.0f,0.1f,0.0f,0.0f,0.0f,1000.0f);
        }
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        super.randomTick(world, pos, state, random);

        if(random.nextFloat() < 0.05f)
        {
            for(int i = 1; i <= 50; i++)
            {
                BlockPos checkpos = pos.down(i);
                IBlockState checkstate = world.getBlockState(checkpos);
                if(world.isSideSolid(checkpos, EnumFacing.UP))
                {
                    if(world.isAirBlock(checkpos.up()) || checkstate.getBlock().isReplaceable(world,checkpos.up()))
                        world.setBlockState(checkpos.up(), ModBlocks.sakuraLeafPile.getDefaultState(), 3);
                    break;
                }
            }
        }
    }
}
