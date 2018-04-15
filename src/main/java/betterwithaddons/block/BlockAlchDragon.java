package betterwithaddons.block;

import betterwithaddons.tileentity.TileEntityAlchDragon;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAlchDragon extends BlockContainerBase {
    public static final PropertyDirection FACING;
    public static final PropertyBool NODROP;
    protected static final AxisAlignedBB DEFAULT_AABB;
    protected static final AxisAlignedBB NORTH_AABB;
    protected static final AxisAlignedBB SOUTH_AABB;
    protected static final AxisAlignedBB WEST_AABB;
    protected static final AxisAlignedBB EAST_AABB;

    protected BlockAlchDragon() {
        super("alchemical_dragon",Material.CIRCUITS);
        this.setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH).withProperty(NODROP, Boolean.valueOf(false)));
    }

    @Override
    public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState p_isFullCube_1_) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState p_getBoundingBox_1_, IBlockAccess p_getBoundingBox_2_, BlockPos p_getBoundingBox_3_) {
        switch(p_getBoundingBox_1_.getValue(FACING)) {
            case UP:
            default:
                return DEFAULT_AABB;
            case SOUTH:
                return NORTH_AABB;
            case NORTH:
                return SOUTH_AABB;
            case EAST:
                return WEST_AABB;
            case WEST:
                return EAST_AABB;
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float p_onBlockPlaced_4_, float p_onBlockPlaced_5_, float p_onBlockPlaced_6_, int p_onBlockPlaced_7_, EntityLivingBase entity) {
        return this.getDefaultState().withProperty(FACING, facing).withProperty(NODROP, Boolean.valueOf(false));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
        if (world != null && pos != null) {
            if (player != null) {
                int inc = MathHelper.floor(player.rotationYaw * 16.0F / 360.0F + 0.5D) & 15;
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileEntityAlchDragon) {
                    ((TileEntityAlchDragon)te).setSkullRotation(inc);
                }
            }
            if (world.isRemote) {
                return;
            }
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if(player.capabilities.isCreativeMode) {
            state = state.withProperty(NODROP, Boolean.valueOf(true));
            world.setBlockState(pos, state, 4);
        }

        this.dropBlockAsItem(world, pos, state, 0);
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public void breakBlock(World p_breakBlock_1_, BlockPos p_breakBlock_2_, IBlockState p_breakBlock_3_) {
        super.breakBlock(p_breakBlock_1_, p_breakBlock_2_, p_breakBlock_3_);
    }

    @Override
    public IBlockState getStateFromMeta(int p_getStateFromMeta_1_) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(p_getStateFromMeta_1_ & 7)).withProperty(NODROP, Boolean.valueOf((p_getStateFromMeta_1_ & 8) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState p_getMetaFromState_1_) {
        byte i = 0;
        int i1 = i | ((EnumFacing)p_getMetaFromState_1_.getValue(FACING)).getIndex();
        if(((Boolean)p_getMetaFromState_1_.getValue(NODROP)).booleanValue()) {
            i1 |= 8;
        }

        return i1;
    }

    @Override
    public IBlockState withRotation(IBlockState p_withRotation_1_, Rotation p_withRotation_2_) {
        return p_withRotation_1_.withProperty(FACING, p_withRotation_2_.rotate((EnumFacing)p_withRotation_1_.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState p_withMirror_1_, Mirror p_withMirror_2_) {
        return p_withMirror_1_.withRotation(p_withMirror_2_.toRotation((EnumFacing)p_withMirror_1_.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, NODROP});
    }

    static {
        FACING = BlockDirectional.FACING;
        NODROP = PropertyBool.create("nodrop");
        DEFAULT_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.5D, 0.75D);
        NORTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.5D, 0.75D, 0.75D, 1.0D);
        SOUTH_AABB = new AxisAlignedBB(0.25D, 0.25D, 0.0D, 0.75D, 0.75D, 0.5D);
        WEST_AABB = new AxisAlignedBB(0.5D, 0.25D, 0.25D, 1.0D, 0.75D, 0.75D);
        EAST_AABB = new AxisAlignedBB(0.0D, 0.25D, 0.25D, 0.5D, 0.75D, 0.75D);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAlchDragon();
    }
}