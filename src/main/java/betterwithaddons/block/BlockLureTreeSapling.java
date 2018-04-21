package betterwithaddons.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLureTreeSapling extends BlockModSapling {
    public BlockLureTreeSapling() {
        super(ModWoods.LURETREE);
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.generateTree(worldIn, pos, state, rand);

        BlockPos checkpos = pos.up();
        IBlockState checkstate = worldIn.getBlockState(checkpos);

        if(checkstate.getBlock() == ModBlocks.LURETREE_LOG)
        {
            worldIn.setBlockState(checkpos,ModBlocks.LURETREE_FACE.getDefaultState().withProperty(BlockLureTree.FACING, EnumFacing.getHorizontal(rand.nextInt(4))).withProperty(BlockLureTree.ACTIVE,true));
        }

        worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 0.6F,
                ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F));
    }
}
