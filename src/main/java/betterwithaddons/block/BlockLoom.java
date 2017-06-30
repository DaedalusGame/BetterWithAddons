package betterwithaddons.block;

import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockLoom extends BlockBase implements IMechanicalBlock {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final  PropertyBool ISACTIVE = PropertyBool.create("ison");

    public BlockLoom() {
        super("loom", Material.WOOD);
        this.setHardness(2.0F).setResistance(1.0F);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
        this.setTickRandomly(true);
    }

    public EnumFacing getFacing(World world, BlockPos pos)
    {
        return world.getBlockState(pos).getValue(FACING);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING,EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ISACTIVE,(meta & 1) > 0).withProperty(FACING,EnumFacing.getFront((meta >> 1) & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(ISACTIVE) ? 1 : 0) | (state.getValue(FACING).getIndex() << 1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ISACTIVE, FACING);
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side != base_state.getValue(FACING);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        world.scheduleBlockUpdate(pos, this, 5, 5);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, 5, 5);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        //NOOP
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean powered = isInputtingMechPower(world, pos);
        boolean isOn = isBlockOn(world, pos);

        if (isOn != powered) {
            setMechanicalOn(world, pos, powered);
        } else if (powered) {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
        }
    }

    @Override
    public int tickRate(World world) {
        return 5;
    }

    public boolean isBlockOn(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(ISACTIVE);
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        return MechanicalUtil.isBlockPoweredByAxle(world, pos, this) || MechanicalUtil.isPoweredByCrank(world, pos);
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        return world.getBlockState(pos).getValue(FACING) != facing;
    }

    @Override
    public void overpower(World world, BlockPos blockPos) {
        //TODO: break it
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isBlockOn(world,pos);
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean b) {
        if(isMechanicalOn(world,pos) != b)
            world.setBlockState(pos,world.getBlockState(pos).withProperty(ISACTIVE,b));
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState iBlockState) {
        return iBlockState.getValue(ISACTIVE);
    }
}
