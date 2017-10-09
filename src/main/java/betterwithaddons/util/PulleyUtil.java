package betterwithaddons.util;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.registry.PulleyStructureManager;
import betterwithmods.module.GlobalConfig;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class PulleyUtil {
    public static boolean isAnchorSupported(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block topblock = world.getBlockState(pos.up()).getBlock();
        EnumFacing facing = state.getValue(DirUtils.FACING).getOpposite();
        IBlockState sidingblock = world.getBlockState(pos.offset(facing));

        return topblock == BWMBlocks.ROPE || (sidingblock.isSideSolid(world,pos,facing.getOpposite()) && !PulleyStructureManager.isPulleyBlock(sidingblock));
    }

    public static boolean findPlatformPart(World world, BlockPos pos, HashSet<BlockPos> set)
    {
        if (set.size() > GlobalConfig.maxPlatformBlocks)
            return false;

        if (!isValidPlatformPart(world,pos,world.getBlockState(pos)))
            return true;

        set.add(pos);

        List<BlockPos> fails = new ArrayList<>();

        Arrays.asList(pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()).forEach(q -> {
            if (fails.isEmpty() && !set.contains(q)) {
                if (!findPlatformPart(world, q, set))
                    fails.add(q);
            }
        });

        return fails.isEmpty();
    }

    public static boolean findFallingPlatformPart(World world, BlockPos pos, HashSet<BlockPos> set) {
        if (set.size() > GlobalConfig.maxPlatformBlocks)
            return false;

        if(world.getBlockState(pos).getBlock() == BWMBlocks.ANCHOR && isAnchorSupported(world,pos))
            return false;

        if (!isValidPlatformPart(world,pos,world.getBlockState(pos)))
            return true;

        BlockPos blockCheck = pos.down();
        IBlockState stateBlocking = world.getBlockState(blockCheck);

        if (!(world.isAirBlock(blockCheck) || stateBlocking.getBlock().isReplaceable(world, blockCheck) || isValidPlatformPart(world,blockCheck,stateBlocking))
                && !set.contains(blockCheck)) {
            return false;
        }

        set.add(pos);

        List<BlockPos> fails = new ArrayList<>();

        Arrays.asList(pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west()).forEach(q -> {
            if (fails.isEmpty() && !set.contains(q)) {
                if (!findFallingPlatformPart(world, q, set))
                    fails.add(q);
            }
        });

        return fails.isEmpty();
    }

    public static boolean isValidPlatformPart(World world,BlockPos pos,IBlockState state)
    {
        Block block = state.getBlock();

        return PulleyStructureManager.isPulleyBlock(state) || (block == BWMBlocks.ANCHOR && !isAnchorSupported(world,pos));
    }
}
