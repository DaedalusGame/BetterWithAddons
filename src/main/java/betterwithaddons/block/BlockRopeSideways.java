package betterwithaddons.block;

import betterwithaddons.tileentity.TileEntityRopeSideways;
import betterwithaddons.util.BoundingUtil;
import betterwithaddons.util.ItemUtil;
import betterwithmods.api.block.PropertyObject;
import betterwithmods.common.BWMBlocks;
import betterwithmods.util.InvUtils;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;

public class BlockRopeSideways extends BlockContainerBase {
    private static AxisAlignedBB AABB_X = new AxisAlignedBB(0, 0.75, 0.4375, 1, 0.875, 0.5625);
    private static AxisAlignedBB AABB_Z = new AxisAlignedBB(0.4375, 0.75, 0, 0.5625, 0.875, 1);
    private static AxisAlignedBB AABB_CROSS = new AxisAlignedBB(0, 0.75, 0, 1, 0.875, 1);
    private static AxisAlignedBB AABB_PLATFORM = new AxisAlignedBB(0, 0.75, 0, 1, 1, 1);

    public static HashSet<Block> FASTENABLE_BLOCKS = new HashSet<>();

    public static final IProperty<EnumRopeShape> SHAPE = PropertyEnum.create("shape", EnumRopeShape.class);
    public static final PropertyBool HAS_PLANKS = PropertyBool.create("has_planks");
    public static final PropertyObject<ItemStack> HELD_PLANKS = new PropertyObject<>("held_planks", ItemStack.class);

    private static ImmutableList<AxisAlignedBB> CROSS_BOUNDS = ImmutableList.of(
            new AxisAlignedBB(0, 0.75, 0.4375, 1, 0.875, 0.5625),
            new AxisAlignedBB(0.4375, 0.75, 0, 0.5625, 0.875, 1)
    );

    protected BlockRopeSideways(String name) {
        super(name, Material.WOOD);
        setDefaultState(getDefaultState().withProperty(HAS_PLANKS,false));
        this.setHardness(0.5F);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        if(blockState.getValue(HAS_PLANKS))
            return 2.0F;

        return super.getBlockHardness(blockState, worldIn, pos);
    }

    public static void addFastenableBlock(Block block)
    {
        FASTENABLE_BLOCKS.add(block);
    }

    public static boolean canFastenBlock(Block block)
    {
        return FASTENABLE_BLOCKS.contains(block);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if(state.getValue(HAS_PLANKS))
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof TileEntityRopeSideways && !world.isRemote) {
                TileEntityRopeSideways tile = (TileEntityRopeSideways) te;
                spawnAsEntity(world,pos,tile.getPlanks());
                tile.setPlanks(ItemStack.EMPTY);
                return false;
            }
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(BWMBlocks.ROPE));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityRopeSideways)
        {
            InvUtils.ejectStackWithOffset(worldIn, pos, ((TileEntityRopeSideways) te).getPlanks());
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn == null)
            return false;

        TileEntity te = worldIn.getTileEntity(pos);
        ItemStack stack = playerIn.getHeldItem(hand);

        if(te instanceof TileEntityRopeSideways && !playerIn.isSneaking() && ItemUtil.matchesOreDict(stack,"plankWood"))
        {
            TileEntityRopeSideways tile = (TileEntityRopeSideways) te;
            if(worldIn.isRemote)
                return true;
            else
            {
                if(tile.getPlanks().isEmpty()) {
                    tile.setPlanks(stack.splitStack(1));
                }

                return true;
            }
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        boolean supportedx = isSupported(worldIn,pos,state,Axis.X);
        boolean supportedz = isSupported(worldIn,pos,state,Axis.Z);
        switch (state.getValue(SHAPE))
        {
            case X:
                if(!supportedx) dropBlock(worldIn,pos,state,null);
                break;
            case Z:
                if(!supportedz) dropBlock(worldIn,pos,state,null);
                break;
            case CROSS:
                if(!supportedx && !supportedz)
                    dropBlock(worldIn,pos,state,null);
                if(!supportedx)
                    dropBlock(worldIn,pos,state,EnumRopeShape.Z);
                else if(!supportedz)
                    dropBlock(worldIn,pos,state,EnumRopeShape.X);
                break;
        }
    }

    private void dropBlock(World world, BlockPos pos, IBlockState state, EnumRopeShape remainingRope)
    {
        dropBlockAsItem(world, pos, state, 0);
        if(remainingRope == null)
        world.setBlockToAir(pos);
        else
            world.setBlockState(pos,state.withProperty(SHAPE,remainingRope));
    }

    private boolean isSupported(World world, BlockPos pos, IBlockState state, Axis axis)
    {
        EnumRopeShape shape = state.getValue(SHAPE);

        EnumFacing forward = EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE,axis);
        EnumFacing back = EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE,axis);

        IBlockState state1 = world.getBlockState(pos.offset(forward));
        IBlockState state2 = world.getBlockState(pos.offset(back));

        return (canFastenBlock(state1.getBlock()) || (state1.getBlock() == this && state1.getValue(SHAPE).has(shape))) &&
                (canFastenBlock(state2.getBlock()) || (state2.getBlock() == this && state2.getValue(SHAPE).has(shape)));
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityRopeSideways();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return blockState.getValue(HAS_PLANKS) ? AABB_PLATFORM : NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return state.getValue(HAS_PLANKS) ? AABB_PLATFORM : state.getValue(SHAPE).getAABB();
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Vec3d start, @Nonnull Vec3d end) {
        if(state.getValue(SHAPE) == EnumRopeShape.CROSS && !state.getValue(HAS_PLANKS))
            return BoundingUtil.raytraceMultiAABB(CROSS_BOUNDS, pos, start, end);
        return super.collisionRayTrace(state,world,pos,start,end);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, EnumRopeShape.byMetadata(meta & 3)).withProperty(HAS_PLANKS, (meta & 4) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(SHAPE).getMetadata() | (state.getValue(HAS_PLANKS) ? 4 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[] { SHAPE, HAS_PLANKS }, new IUnlistedProperty[]{HELD_PLANKS});
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        ItemStack planks = ItemStack.EMPTY;
        if (te instanceof TileEntityRopeSideways) {
            TileEntityRopeSideways tile = (TileEntityRopeSideways) te;
            planks = tile.getPlanks();
        }
        return ((IExtendedBlockState) state).withProperty(HELD_PLANKS, planks);
    }

    public enum EnumRopeShape implements IStringSerializable {
        X(0, "x", AABB_X, Axis.X),
        Z(1, "z", AABB_Z, Axis.Z),
        CROSS(2, "cross", AABB_CROSS, null);

        private static final EnumRopeShape[] VALUES = values();

        private final int meta;
        private final String name;
        private final AxisAlignedBB aabb;
        private final Axis axis;

        @Override
        public String getName() {
            return this.name;
        }

        public int getMetadata() {
            return this.meta;
        }

        public AxisAlignedBB getAABB() {
            return this.aabb;
        }

        public Axis getAxis() {
            return this.axis;
        }

        EnumRopeShape(int metaIn, String nameIn, AxisAlignedBB aabbIn, Axis axis) {
            this.meta = metaIn;
            this.name = nameIn;
            this.aabb = aabbIn;
            this.axis = axis;
        }

        public static EnumRopeShape byMetadata(int meta) {
            if (meta < 0 || meta >= VALUES.length) {
                meta = 0;
            }

            return VALUES[meta];
        }

        public EnumRopeShape add(EnumRopeShape otherShape)
        {
            return this != otherShape ? CROSS : otherShape;
        }

        public boolean has(EnumRopeShape otherShape)
        {
            return otherShape != null && (this == CROSS || otherShape == CROSS || this == otherShape);
        }

        public boolean has(Axis axis)
        {
            return axis.isHorizontal() && (this == CROSS || this.axis == axis);
        }

        public String toString() {
            return this.name;
        }
    }
}
