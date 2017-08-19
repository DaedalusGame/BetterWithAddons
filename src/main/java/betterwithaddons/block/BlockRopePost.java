package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityRopePost;
import betterwithaddons.tileentity.TileEntityRopeSideways;
import betterwithaddons.util.ItemUtil;
import betterwithmods.api.block.PropertyObject;
import betterwithmods.common.BWMBlocks;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import java.util.List;

public class BlockRopePost extends BlockFence {
    public static final PropertyBool HAS_PLANKS = PropertyBool.create("has_planks");
    public static final PropertyBool HAS_POST = PropertyBool.create("has_post");
    public static final PropertyObject<ItemStack> HELD_PLANKS = new PropertyObject<>("held_planks", ItemStack.class);
    public static final PropertyObject<IBlockAccess> HELD_WORLD = new PropertyObject<>("held_world", IBlockAccess.class);
    public static final PropertyObject<BlockPos> HELD_POS = new PropertyObject<>("held_pos", BlockPos.class);
    public static final PropertyObject<IBlockState> HELD_STATE = new PropertyObject<>("held_state", IBlockState.class);

    private static AxisAlignedBB AABB_POST = new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D);
    private static AxisAlignedBB AABB_PLATFORM = new AxisAlignedBB(0, 0.75, 0, 1, 1, 1);

    public BlockRopePost(String name) {
        super(Material.WOOD, MapColor.WOOD);

        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.setUnlocalizedName(name);
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        setDefaultState(blockState.getBaseState().withProperty(HAS_PLANKS,false).withProperty(HAS_POST,false));
        this.setHardness(0.5F);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        if(state.getValue(HAS_PLANKS))
            return FULL_BLOCK_AABB;
        else
            return AABB_POST;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        if(state.getValue(HAS_PLANKS)) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_POST);
            addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_PLATFORM);
        }
        else
            addCollisionBoxToList(pos, entityBox, collidingBoxes, PILLAR_AABB);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityRopePost();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        if(blockState.getValue(HAS_PLANKS))
            return 2.0F;
        else if(blockState.getValue(HAS_POST)) {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TileEntityRopePost) {
                IBlockState state = ((TileEntityRopePost) te).getFenceState();
                return state.getBlockHardness(worldIn,pos);
            }
        }

        return super.getBlockHardness(blockState, worldIn, pos);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if(state.getValue(HAS_PLANKS))
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof TileEntityRopePost && !world.isRemote) {
                TileEntityRopePost tile = (TileEntityRopePost) te;
                spawnAsEntity(world,pos,tile.getPlanks());
                tile.setPlanks(ItemStack.EMPTY);
                return false;
            }
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileEntityRopePost)
        {
            NonNullList<ItemStack> drops = NonNullList.create();
            drops.add(((TileEntityRopePost) te).getPlanks());
            IBlockState fenceState = ((TileEntityRopePost) te).getFenceState();
            if(fenceState != null)
                fenceState.getBlock().getDrops(drops, worldIn, pos, fenceState, 0);
            for(ItemStack drop : drops)
                spawnAsEntity(worldIn,pos,drop);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(new ItemStack(BWMBlocks.ROPE));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn == null)
            return false;

        TileEntity te = worldIn.getTileEntity(pos);
        ItemStack stack = playerIn.getHeldItem(hand);

        if(te instanceof TileEntityRopePost && !playerIn.isSneaking() && ItemUtil.matchesOreDict(stack,"plankWood"))
        {
            TileEntityRopePost tile = (TileEntityRopePost) te;
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
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(NORTH,hasRope(worldIn,pos,EnumFacing.NORTH))
                .withProperty(SOUTH,hasRope(worldIn,pos,EnumFacing.SOUTH))
                .withProperty(EAST,hasRope(worldIn,pos,EnumFacing.EAST))
                .withProperty(WEST,hasRope(worldIn,pos,EnumFacing.WEST));
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
        if (te instanceof TileEntityRopePost) {
            TileEntityRopePost tile = (TileEntityRopePost) te;
            IExtendedBlockState withplanks = ((IExtendedBlockState) state)
                    .withProperty(HELD_WORLD, world)
                    .withProperty(HELD_POS,pos)
                    .withProperty(HELD_STATE,tile.getFenceState())
                    .withProperty(HELD_PLANKS,tile.getPlanks());
            return withplanks;
        } else {
            return state;
        }
    }

    private boolean hasRope(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        IBlockState state1 = world.getBlockState(pos.offset(facing));

        return (/*BlockRopeSideways.canFastenBlock(state1.getBlock()) ||*/ (state1.getBlock() == ModBlocks.ropeSideways && state1.getValue(BlockRopeSideways.SHAPE).has(facing.getAxis())));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(HAS_PLANKS) ? 1 : 0) | (state.getValue(HAS_POST) ? 2 : 0);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HAS_PLANKS,(meta & 1) > 0).withProperty(HAS_POST,(meta & 2) > 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] { NORTH, SOUTH, EAST, WEST, HAS_PLANKS, HAS_POST }, new IUnlistedProperty[]{HELD_PLANKS, HELD_WORLD, HELD_POS, HELD_STATE});
    }

    public void placeFencePost(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos,getDefaultState());
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityRopePost)
            ((TileEntityRopePost) te).setFenceState(state);
    }
}
