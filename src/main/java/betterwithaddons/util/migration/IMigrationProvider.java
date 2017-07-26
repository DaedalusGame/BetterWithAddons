package betterwithaddons.util.migration;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMigrationProvider {
    Migration getMigrationData(World world, BlockPos pos);
}
