package betterwithaddons.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISpecialMeasuringBehavior {
    boolean isFull(World world, BlockPos pos, IBlockState state);

    boolean isEmpty(World world, BlockPos pos, IBlockState state);

    int getDelay(World world, BlockPos pos, IBlockState state);
}
