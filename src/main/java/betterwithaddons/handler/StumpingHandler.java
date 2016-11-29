package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBTWTweak;
import betterwithaddons.util.BlockMeta;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

public class StumpingHandler {
    private static ArrayList<WoodHardness> SOFT_WOODS = new ArrayList<>();

    private static class WoodHardness extends BlockMeta
    {
        public float hardness;

        public WoodHardness(Block block, int meta, float hardness) {
            super(block, meta);
            this.hardness = hardness;
        }
    }

    public static void addSoftWood(Block block, int meta, float hardness)
    {
        SOFT_WOODS.add(new WoodHardness(block,meta,hardness));
    }

    @SubscribeEvent
    public void breakBlock(PlayerEvent.BreakSpeed breakEvent)
    {
        World world = breakEvent.getEntity().getEntityWorld();
        BlockPos breakpos = breakEvent.getPos();
        IBlockState state = world.getBlockState(breakpos);
        Block block = state.getBlock();

        float speed = breakEvent.getNewSpeed();
        float hardness = 1.5f;
        float multiplier = 1;
        boolean issoft = false;

        if(InteractionBTWTweak.HARD_STUMPS) {
            if(block instanceof BlockLog && !issoft) {
                IBlockState bottomstate = world.getBlockState(breakpos.down());
                if (bottomstate.getMaterial() == Material.GROUND)
                    multiplier = 10;
            }
        }

        if(InteractionBTWTweak.SOFT_WOODS) {
            int meta = block.damageDropped(state);

            for (WoodHardness wood : SOFT_WOODS) {
                if (wood.matches(block, meta)) {
                    hardness = wood.hardness;
                    break;
                }
            }
        }

        float hardnessMultiplier = (hardness * multiplier) / state.getBlockHardness(world,breakpos);

        breakEvent.setNewSpeed(speed / hardnessMultiplier);
    }
}
