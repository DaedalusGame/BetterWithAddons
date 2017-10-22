package betterwithaddons.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRedstoneEmitter extends BlockBase {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockRedstoneEmitter() {
        super("redstone_emitter", Material.WOOD);
        this.setHardness(2.0F).setResistance(1.0F);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
        this.setDefaultState(getDefaultState().withProperty(ACTIVE,false));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,FACING,ACTIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING).getIndex() << 1) | (state.getValue(ACTIVE) ? 1 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING,EnumFacing.getFront((meta >> 1) & 7)).withProperty(ACTIVE,(meta & 1) == 1);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING,EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
        {
            state = state.cycleProperty(ACTIVE);
            worldIn.setBlockState(pos, state, 3);
            float pitch = state.getValue(ACTIVE) ? 0.6F : 0.5F;
            worldIn.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, pitch);
            worldIn.notifyNeighborsOfStateChange(pos, this, false);
        }

        return true;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return state.getValue(ACTIVE) && state.getValue(FACING) == side.getOpposite() ? 15 : 0;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING,rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withProperty(FACING,mirrorIn.mirror(state.getValue(FACING)));
    }
}
