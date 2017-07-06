package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWR;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class RenewablesHandler {
    @SubscribeEvent
    public void meltHellfire(RandomBlockTickEvent event)
    {
        World world = event.getWorld();
        IBlockState state = event.getState();
        BlockPos pos = event.getPos();
        Random rand = event.getRandom();

        if(!InteractionBWR.MELT_HELLFIRE || state.getBlock() != BWMBlocks.AESTHETIC || state.getValue(BlockAesthetic.blockType) != BlockAesthetic.EnumType.HELLFIRE)
            return;

        int sources = 0;
        boolean hasSource = false;

        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++)
                {
                    IBlockState lavaState = world.getBlockState(pos.add(x,y,z));

                    if(lavaState.getMaterial() != Material.LAVA) continue;
                    sources++;
                    hasSource |= lavaState.getValue(BlockLiquid.LEVEL) == 0;
                }

        if(hasSource && rand.nextInt(10) < sources)
        {
            world.setBlockState(pos, Blocks.LAVA.getDefaultState());
            for(int i = 0; i < 3; i++)
                world.playEvent(2004, pos, 0);
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
        }
    }
}
