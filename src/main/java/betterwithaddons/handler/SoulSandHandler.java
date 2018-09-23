package betterwithaddons.handler;

// Original Copyright Notice provided as required by the License.
// ==========================================================================
// Copyright (C)2013 by Aaron Suen <warr1024@gmail.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
// THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
// ---------------------------------------------------------------------------

import betterwithaddons.interaction.InteractionBWR;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SoulSandHandler {
    private LinkedList<EntityXPOrb> TrackedItems = new LinkedList<>();
    private LinkedList<EntityXPOrb> TrackedItemsAdd = new LinkedList<>();
    private Iterator<EntityXPOrb> TrackedItemsIterator;

    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event)
    {
        Entity entity = event.getEntity();

        if(entity instanceof EntityXPOrb)
        {
            int xp = ((EntityXPOrb) entity).getXpValue();

            if(xp > 0)
            {
                TrackedItemsAdd.add((EntityXPOrb)entity);
            }
        }
    }

    @SubscribeEvent
    public void xpTick(TickEvent.WorldTickEvent tickEvent)
    {
        World world = tickEvent.world;
        if(!world.isRemote) {
            handleXP();
        }
    }

    private void handleXP()
    {
        if(TrackedItemsIterator == null || !TrackedItemsIterator.hasNext())
        {
            TrackedItems.addAll(TrackedItemsAdd);
            TrackedItemsAdd.clear();
            TrackedItemsIterator = TrackedItems.iterator();
        }
        else
        {
            EntityXPOrb entity = TrackedItemsIterator.next();
            World world = entity.world;
            int xpValue = entity.getXpValue();
            BlockPos pos = entity.getPosition();
            boolean remove = false;
            if(entity.isDead)
                remove = true;
            else
            {
                IBlockState state = world.getBlockState(pos);

                if(state.getBlock() != BWMBlocks.AESTHETIC || state.getValue(BlockAesthetic.TYPE) != BlockAesthetic.EnumType.DUNG)
                    return;

                if(!isValidInfusionWall(world,pos.up(), EnumFacing.DOWN) ||
                        !isValidInfusionWall(world,pos.down(),EnumFacing.UP)||
                        !isValidInfusionWall(world,pos.south(),EnumFacing.NORTH)||
                        !isValidInfusionWall(world,pos.north(),EnumFacing.SOUTH)||
                        !isValidInfusionWall(world,pos.west(),EnumFacing.EAST)||
                        !isValidInfusionWall(world,pos.east(),EnumFacing.WEST))
                    return;

                AxisAlignedBB aabb = new AxisAlignedBB(pos);

                List<EntityXPOrb> orbs = world.getEntitiesWithinAABB(EntityXPOrb.class,aabb);
                int totalxp = 0;

                for (EntityXPOrb orb : orbs) {
                    totalxp += orb.getXpValue();
                    orb.setDead();
                }

                if(world.rand.nextInt(InteractionBWR.SOULSAND_INFUSION_THRESHOLD) < totalxp)
                {
                    world.setBlockState(pos, Blocks.SOUL_SAND.getDefaultState());
                    for(int i = 0; i < 3; i++)
                        world.playEvent(2004, pos, 0);

                    //TODO: burn everybody
                }
            }

            if(remove)
                TrackedItemsIterator.remove();
        }
    }

    private boolean isValidInfusionWall(World world, BlockPos pos, EnumFacing facing)
    {
        IBlockState state = world.getBlockState(pos);

        return state.getBlockHardness(world,pos) >= 5.0 && state.isSideSolid(world,pos,facing);
    }
}
