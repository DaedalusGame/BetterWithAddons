package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWR;
import betterwithmods.common.BWMBlocks;
import betterwithmods.util.DirUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class RedstoneBoilHandler {
    HashMap<World,WorldRedstoneScheduler> schedulers = new HashMap<>();

    @SubscribeEvent
    public void redstoneUpdate(BlockEvent.NeighborNotifyEvent event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = event.getState();

        if(state.getBlock() != Blocks.REDSTONE_WIRE)
            return;

        if(!willRedstoneBoil(world,pos))
            return;

        if(state.getValue(BlockRedstoneWire.POWER) > 0)
            return;

        scheduleRedstone(world,pos,9);
    }

    public void scheduleRedstone(World world, BlockPos pos, int delay)
    {
        if(!schedulers.containsKey(world))
            schedulers.put(world,new WorldRedstoneScheduler());

        schedulers.get(world).schedule(pos,world.getTotalWorldTime()+delay);
    }

    @SubscribeEvent
    public void redstoneTick(TickEvent.WorldTickEvent event)
    {
        World world = event.world;

        if(schedulers.containsKey(world))
            schedulers.get(world).tick(world);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void redstonePlace(BlockEvent.PlaceEvent event)
    {
        if(event.isCanceled())
            return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = event.getPlacedBlock();

        if(state.getBlock() == Blocks.REDSTONE_WIRE)
        {
            scheduleRedstone(world,pos,100);
        }
    }

    public boolean willRedstoneBoil(World world,BlockPos pos)
    {
        IBlockState lens = world.getBlockState(pos.up());
        return world.provider.hasSkyLight() && lens.getBlock() == BWMBlocks.LENS && lens.getValue(DirUtils.FACING) == EnumFacing.DOWN && world.isDaytime() && !world.isRaining() && world.canSeeSky(pos.up(2));
    }

    private void boilRedstone(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if(state.getBlock() != Blocks.REDSTONE_WIRE)
            return;

        if(state.getValue(BlockRedstoneWire.POWER) <= world.rand.nextInt(128))
            return;

        if(!willRedstoneBoil(world,pos))
            return;

        world.playEvent(2001, pos, Block.getStateId(state));

        if(world.rand.nextDouble() < InteractionBWR.REDSTONE_BOILING_CHANCE)
        {
            EntityItem result = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.1f, pos.getZ() + 0.5f, new ItemStack(Items.GLOWSTONE_DUST));
            result.setDefaultPickupDelay();
            world.spawnEntity(result);
            world.setBlockToAir(pos);

        }
        else {
            world.notifyNeighborsOfStateChange(pos,state.getBlock(),true);
        }
    }

    private class WorldRedstoneScheduler
    {
        TreeSet<ScheduledTick> sortedUpdates = new TreeSet<>();
        HashSet<ScheduledTick> updates = new HashSet<>();

        public void schedule(BlockPos pos, long tick)
        {
            ScheduledTick nexttick = new ScheduledTick(pos,tick);

            if(!updates.contains(nexttick))
            {
                updates.add(nexttick);
                sortedUpdates.add(nexttick);
            }
        }

        public void tick(World world)
        {
            long currenttick = world.getTotalWorldTime();

            if(sortedUpdates.size() != updates.size())
            {
                sortedUpdates.clear();
                updates.clear();
            }
            if(sortedUpdates.size() == 0) return;

            ScheduledTick nexttick = sortedUpdates.first();
            while(nexttick.tick <= currenttick)
            {
                boilRedstone(world, nexttick.getPos());
                updates.remove(nexttick);
                sortedUpdates.remove(nexttick);
                if(sortedUpdates.size() == 0) break;
                nexttick = sortedUpdates.first();
            }
        }

        protected class ScheduledTick implements Comparable<ScheduledTick>
        {
            private long tick;
            private BlockPos pos;

            protected ScheduledTick(BlockPos pos, long tick)
            {
                this.pos = pos;
                this.tick = tick;
            }

            public long getTick() {
                return tick;
            }

            public BlockPos getPos() {
                return pos;
            }

            @Override
            public boolean equals(Object obj) {
                if(obj instanceof ScheduledTick)
                {
                    return pos.equals(((ScheduledTick) obj).pos);
                }

                return false;
            }

            @Override
            public int hashCode() {
                return pos.hashCode();
            }

            @Override
            public int compareTo(ScheduledTick o) {
                if(tick != o.tick)
                    return Long.compare(tick,o.tick);
                else
                    return pos.compareTo(o.pos);
            }
        }
    }
}
