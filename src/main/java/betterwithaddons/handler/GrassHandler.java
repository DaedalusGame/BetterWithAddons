package betterwithaddons.handler;

import betterwithaddons.block.BlockExtraGrass;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.interaction.InteractionBWA;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class GrassHandler {
    @SubscribeEvent
    public void grassGrowEvent(RandomBlockTickEvent event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos().up();
        IBlockState state = event.getState();
        Random random = event.getRandom();

        if (!world.isRemote && state.getBlock() == Blocks.GRASS)
        {
            if (world.getLightFromNeighbors(pos.up()) >= 9 && world.getBlockState(pos.up()).getLightOpacity(world, pos.up()) <= 2) {
                    for (int i = 0; i < 4; ++i) {
                        BlockPos growthpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);

                        if (growthpos.getY() >= 0 && growthpos.getY() < 256 && !world.isBlockLoaded(growthpos)) {
                            return;
                        }

                        IBlockState topstate = world.getBlockState(growthpos.up());
                        IBlockState growthstate = world.getBlockState(growthpos);

                        if(world.getLightFromNeighbors(growthpos.up()) >= 4 && topstate.getLightOpacity(world, pos.up()) <= 2) {
                            if (InteractionBWA.GRASS_TO_SAND && growthstate.getBlock() == Blocks.SAND) {
                                if(growthstate.getValue(BlockSand.VARIANT) == BlockSand.EnumType.SAND)
                                    world.setBlockState(growthpos, ModBlocks.grass.getDefaultState().withProperty(BlockExtraGrass.VARIANT,BlockExtraGrass.ExtraGrassType.SAND));
                                else
                                    world.setBlockState(growthpos, ModBlocks.grass.getDefaultState().withProperty(BlockExtraGrass.VARIANT,BlockExtraGrass.ExtraGrassType.REDSAND));
                            } else if(InteractionBWA.GRASS_TO_CLAY && growthstate.getBlock() == Blocks.CLAY)
                                world.setBlockState(growthpos, ModBlocks.grass.getDefaultState().withProperty(BlockExtraGrass.VARIANT,BlockExtraGrass.ExtraGrassType.CLAY));
                        }
                    }
            }
        }
    }
}
