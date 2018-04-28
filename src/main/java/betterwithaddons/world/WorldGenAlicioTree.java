package betterwithaddons.world;

import betterwithaddons.block.BlockLureTree;
import betterwithaddons.block.ModBlocks;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraftforge.common.IPlantable;

import java.util.List;
import java.util.Random;

public class WorldGenAlicioTree extends WorldGenBigTrees
{
    public WorldGenAlicioTree(boolean notify, IBlockState log, IBlockState leaves, IPlantable sapling) {
        super(notify, log, leaves, sapling);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        boolean success = super.generate(worldIn, rand, position);

        if(success) {
            BlockPos checkpos = position.up();
            IBlockState checkstate = worldIn.getBlockState(checkpos);

            if (checkstate.getBlock() == ModBlocks.LURETREE_LOG) {
                worldIn.setBlockState(checkpos, ModBlocks.LURETREE_FACE.getDefaultState().withProperty(BlockLureTree.FACING, EnumFacing.getHorizontal(rand.nextInt(4))).withProperty(BlockLureTree.ACTIVE, true));
            }
        }

        return success;
    }
}
