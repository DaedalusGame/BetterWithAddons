package betterwithaddons.handler;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAnchor;
import betterwithmods.common.blocks.tile.TileEntityPulley;
import betterwithmods.module.GlobalConfig;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FallingPlatformHandler {
    @SubscribeEvent
    public void blockNeighborUpdate(BlockEvent.NeighborNotifyEvent notifyEvent) {
        World world = notifyEvent.getWorld();
        dropPlatform(world, notifyEvent.getPos());
    }

    private void dropPlatform(World world, BlockPos pos) {
        BlockPos anchorpos = pos.down();
        IBlockState blockstate = world.getBlockState(anchorpos);
        Block block = blockstate.getBlock();

        if (!world.isRemote && block == BWMBlocks.ANCHOR && !isAnchorSupported(world, anchorpos)) {
            EnumFacing facing = blockstate.getValue(DirUtils.FACING);

            HashSet<BlockPos> platformBlocks = new HashSet<>();
            boolean success = findPlatformPart(world,anchorpos,platformBlocks);
            if(success)
                for (BlockPos plat : platformBlocks) {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double)plat.getX() + 0.5D, (double)plat.getY(), (double)plat.getZ() + 0.5D, world.getBlockState(plat));
                    entityfallingblock.setHurtEntities(true);
                    world.spawnEntity(entityfallingblock);
                }
        }
    }

    private boolean isAnchorSupported(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block topblock = world.getBlockState(pos.up()).getBlock();
        EnumFacing facing = state.getValue(DirUtils.FACING).getOpposite();
        IBlockState sidingblock = world.getBlockState(pos.offset(facing));

        return topblock == BWMBlocks.ROPE || (sidingblock.isSideSolid(world,pos,facing.getOpposite()) && !TileEntityPulley.isValidPlatform(sidingblock.getBlock()));
    }

    private boolean findPlatformPart(World world, BlockPos pos, HashSet<BlockPos> set) {
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
                if (!findPlatformPart(world, q, set))
                    fails.add(q);
            }
        });

        return fails.isEmpty();
    }

    private boolean isValidPlatformPart(World world,BlockPos pos,IBlockState state)
    {
        Block block = state.getBlock();

        return TileEntityPulley.isValidPlatform(block) || (block == BWMBlocks.ANCHOR && !isAnchorSupported(world,pos));
    }
}
