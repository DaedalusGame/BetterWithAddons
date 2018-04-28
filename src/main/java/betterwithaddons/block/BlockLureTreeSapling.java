package betterwithaddons.block;

import betterwithaddons.world.WorldGenAlicioTree;
import betterwithaddons.world.WorldGenBigTrees;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class BlockLureTreeSapling extends BlockModSapling {
    public BlockLureTreeSapling() {
        super(ModWoods.LURETREE);
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        WorldGenerator worldgenerator = new WorldGenAlicioTree(true,log,leaves,this);
        int i = 0;
        int j = 0;

        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j))) {
            worldIn.setBlockState(pos, state, 4);
        }
        else {
            worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                    SoundEvents.ENTITY_WITHER_AMBIENT, SoundCategory.PLAYERS, 0.6F,
                    ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F));
        }
    }
}
