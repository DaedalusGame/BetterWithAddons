package betterwithaddons.block.Factorization;

import betterwithaddons.block.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMatcher extends BlockBase {
    public static final IProperty<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);

    protected BlockMatcher() {
        super("block_matcher", Material.ROCK);
    }

    //Taken from Factorization
    private byte match(World world, BlockPos pos, EnumFacing axis) {
        // 0: Not at all similar
        // 1: Same solidity
        // 2: Same material
        // 3: Same block
        // 4: Same blockstate
        BlockPos frontpos = pos.offset(axis);
        IBlockState frontbs = world.getBlockState(frontpos);
        Block frontBlock = frontbs.getBlock();
        boolean frontAir = frontBlock.isAir(frontbs, world, frontpos);

        BlockPos backpos = pos.offset(axis.getOpposite());
        IBlockState backbs = world.getBlockState(backpos);
        Block backBlock = backbs.getBlock();
        boolean backAir = backBlock.isAir(backbs, world, backpos);

        if (frontAir && backAir) return 0;

        if (frontBlock == backBlock) {
            if (sameState(frontbs, backbs)) return 4;
            return 3;
        }
        if (frontbs.getBlockHardness(world,frontpos) == backbs.getBlockHardness(world,backpos)) {
            return 2;
        }
        if (frontbs.getBlockHardness(world,frontpos) == backbs.getBlockHardness(world,backpos)) {
            return 1;
        }
        return 0;
    }

    public static boolean sameState(IBlockState abs, IBlockState bbs) {
        return abs == bbs;
    }
}
