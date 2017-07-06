package betterwithaddons.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

import java.util.Random;

public class RandomBlockTickEvent extends BlockEvent {
    Random rand;

    public RandomBlockTickEvent(World world, BlockPos pos, IBlockState state, Random rand)
    {
        super(world,pos,state);
        this.rand = rand;
    }

    public Random getRandom() {
        return rand;
    }
}
