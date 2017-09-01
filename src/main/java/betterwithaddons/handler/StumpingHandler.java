package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBTWTweak;
import betterwithaddons.interaction.minetweaker.SoftWoods;
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

    public static ArrayList<WoodHardness> getSoftWoods()
    {
        return SOFT_WOODS;
    }

    public static class WoodHardness extends BlockMeta
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

    public static WoodHardness getSoftWood(Block block, int meta)
    {
        for (WoodHardness wood : SOFT_WOODS) {
            if (wood.matches(block, meta)) {
                return wood;
            }
        }

        return null;
    }

    @SubscribeEvent
    public void breakBlock(PlayerEvent.BreakSpeed breakEvent)
    {
        World world = breakEvent.getEntity().getEntityWorld();
        BlockPos breakpos = breakEvent.getPos();
        IBlockState state = world.getBlockState(breakpos);
        Block block = state.getBlock();

        float speed = breakEvent.getNewSpeed();
        float multiplier = 1f;

        if(InteractionBTWTweak.HARD_STUMPS) {
            if(block instanceof BlockLog && isRooted(state)) {
                IBlockState bottomstate = world.getBlockState(breakpos.down());
                Material material = bottomstate.getMaterial();
                if (material == Material.GROUND || material == Material.GRASS)
                    multiplier = (float)InteractionBTWTweak.HARD_STUMPS_MODIFIER;
            }
        }

        if(InteractionBTWTweak.SOFT_WOODS) {
            WoodHardness wood = getSoftWood(block,block.damageDropped(state));

            if(wood != null)
                multiplier = wood.hardness / state.getBlockHardness(world,breakpos);
        }

        breakEvent.setNewSpeed(speed * multiplier);
    }

    private boolean isRooted(IBlockState state)
    {
        if(state.getPropertyKeys().contains(BlockLog.LOG_AXIS))
            return state.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.Y;
        else
            return true;
    }
}
