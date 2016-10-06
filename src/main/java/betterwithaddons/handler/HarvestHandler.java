package betterwithaddons.handler;

import betterwithaddons.block.BlockElytraMagma;
import betterwithaddons.block.BlockWorldScaleOre;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Christian on 10.08.2016.
 */
public class HarvestHandler {
    @SubscribeEvent
    public void playerTick(BlockEvent.BreakEvent breakEvent)
    {
        World world = breakEvent.getWorld();
        BlockPos pos = breakEvent.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block;

        if((block = state.getBlock()) instanceof BlockWorldScaleOre)
        {
            if(state.getValue(BlockWorldScaleOre.CRACKED) < 5) {
                ((BlockWorldScaleOre)block).addCracks(world,pos,state,1);
                breakEvent.setCanceled(true);
            }
        }
    }
}
