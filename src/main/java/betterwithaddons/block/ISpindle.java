package betterwithaddons.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISpindle {
    void spinUp(World world, BlockPos pos, IBlockState state, EnumFacing dir);
}
