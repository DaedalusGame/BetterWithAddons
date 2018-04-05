package betterwithaddons.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IBlockMatcher {
    boolean apply(World world, BlockPos pos, @Nullable IBlockState state);
}
