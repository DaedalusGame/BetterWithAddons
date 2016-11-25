package betterwithaddons.handler;

import betterwithaddons.block.BlockElytraMagma;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ElytraUpdriftHandler {
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent tickEvent)
    {
        EntityPlayer player = tickEvent.player;
        World world = player.getEntityWorld();
        BlockPos pos = player.getPosition();

        if(player.isElytraFlying())
        {
            for(int i = 0; i < 24; i++)
            {
                IBlockState state = world.getBlockState(pos.down(i));
                if(state.getBlock() instanceof BlockElytraMagma)
                {
                    double yVel = player.posY - player.prevPosY;
                    double distanceDim = 1 - i / 24;
                    double wantedVel = 1 * distanceDim;
                    player.addVelocity(0,Math.max(Math.min(wantedVel - yVel,0.1),0),0);
                    break;
                }
                if(state.isFullBlock())
                    break;
            }
        }
    }
}
