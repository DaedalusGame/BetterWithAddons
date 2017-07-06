package betterwithaddons.block;

import betterwithaddons.interaction.InteractionBWR;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDung extends BlockBase {
    public BlockDung() {
        super("dung", Material.GROUND);
        this.setSoundType(SoundType.GROUND);
        this.setHardness(1.0F);
        this.setResistance(0.5F);
        this.setTickRandomly(true);
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote || !InteractionBWR.DUNG_TO_DIRT)
            return;

        IBlockState water = world.getBlockState(pos.up());

        if (water.getMaterial() != Material.WATER) {
            return;
        }

        int fireintensity = 26; //Ambient

        for (int i = 1; i <= 3; i++) {
            if (!world.isBlockNormalCube(pos.down(i), true)) {
                fireintensity += getCurrentFireIntensity(world, pos.down(i));
                break;
            }
        }

        if (random.nextInt(300) < fireintensity) {
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            //TODO: Should we add minetweaker compat for other things to happen here?
            if (InteractionBWR.SAND_TO_CLAY && isSand(world, pos.down())) {
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
                world.setBlockState(pos.down(), Blocks.CLAY.getDefaultState());
            }
        }
    }

    public boolean isSand(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        return block == Blocks.SAND;
    }

    public int getCurrentFireIntensity(World world, BlockPos pos) {
        int fireFactor = 0;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos target = pos.add(x, 0, z);
                IBlockState targetState = world.getBlockState(target);
                Block block = targetState.getBlock();
                int meta = targetState.getBlock().damageDropped(targetState);
                if (BWMHeatRegistry.get(block, meta) != null)
                    fireFactor += BWMHeatRegistry.get(block, meta).value;
            }
        }

        return fireFactor;
    }
}
