package betterwithaddons.handler;

import betterwithaddons.util.PulleyUtil;
import betterwithmods.common.BWMBlocks;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

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

        if (!world.isRemote && block == BWMBlocks.ANCHOR && !PulleyUtil.isAnchorSupported(world, anchorpos)) {
            EnumFacing facing = blockstate.getValue(DirUtils.FACING);

            HashSet<BlockPos> platformBlocks = new HashSet<>();
            boolean success = PulleyUtil.findFallingPlatformPart(world,anchorpos,platformBlocks);
            if(success)
                for (BlockPos plat : platformBlocks) {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double)plat.getX() + 0.5D, (double)plat.getY(), (double)plat.getZ() + 0.5D, world.getBlockState(plat));
                    entityfallingblock.setHurtEntities(true);
                    world.spawnEntity(entityfallingblock);
                }
        }
    }
}
