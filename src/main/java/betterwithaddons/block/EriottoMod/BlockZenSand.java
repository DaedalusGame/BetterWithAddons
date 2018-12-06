package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BlockZenSand extends BlockFalling {
    protected static final AxisAlignedBB SOUL_SAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
    public static final PropertyEnum<SandDirection> SHAPE = PropertyEnum.create("shape", SandDirection.class);
    boolean shouldFall;
    boolean shouldSlow;
    Supplier<IBlockState> baseStateDelegate;

    public BlockZenSand(String name, Supplier<IBlockState> baseStateDelegate)
    {
        super();
        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
        this.setSoundType(SoundType.SAND);
        this.setHarvestLevel("shovel", 0);
        this.baseStateDelegate = baseStateDelegate;
    }

    public BlockZenSand setShouldFall() {
        this.shouldFall = true;
        return this;
    }

    public BlockZenSand setShouldSlow() {
        this.shouldSlow = true;
        return this;
    }

    public IBlockState getBaseState()
    {
        return baseStateDelegate.get();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return getBaseState().getBlock().getItemDropped(getBaseState(),rand,fortune);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getBaseState().getBlock().damageDropped(getBaseState());
    }

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return shouldSlow ? SOUL_SAND_AABB : FULL_BLOCK_AABB;
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if(shouldSlow) {
            entityIn.motionX *= 0.4D;
            entityIn.motionZ *= 0.4D;
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        tooltip.add(I18n.format("tooltip.zen_sand"));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z) {
        ItemStack heldItem = player.getHeldItem(hand);

        if(heldItem.getItem() instanceof ItemHoe && facing == EnumFacing.UP && world.isAirBlock(pos.up())) {
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(heldItem, player, world, pos);
            if (hook != 0) return hook > 0;

            EnumFacing clickedFacing = getClickedSegment(x, z);

            if(clickedFacing == EnumFacing.UP)
                return false;
            else {
                SandDirection sandDirection = state.getValue(SHAPE);
                EnumFacing dir1 = sandDirection.direction1;
                EnumFacing dir2 = sandDirection.direction2;

                boolean south = z > 0.5;
                boolean east = x > 0.5;

                switch(sandDirection) {

                    case NORTH_SOUTH:
                        world.setBlockState(pos,state.withProperty(SHAPE, SandDirection.getByDirections(east ? EnumFacing.EAST : EnumFacing.WEST,south ? EnumFacing.NORTH : EnumFacing.SOUTH)),3);
                        break;
                    case EAST_WEST:
                        world.setBlockState(pos,state.withProperty(SHAPE, SandDirection.getByDirections(east ? EnumFacing.WEST : EnumFacing.EAST,south ? EnumFacing.SOUTH : EnumFacing.NORTH)),3);
                        break;
                    default: //Straighten
                        if(dir1.getAxis() == clickedFacing.getAxis())
                            world.setBlockState(pos,state.withProperty(SHAPE, SandDirection.getByDirections(dir1,clickedFacing)),3);
                        else
                            world.setBlockState(pos,state.withProperty(SHAPE, SandDirection.getByDirections(dir2,clickedFacing)),3);
                        break;
                }

                return true;
            }
        }

        return false;
    }

    public EnumFacing getClickedSegment(float x, float z) {
        x -= 0.5f;
        z -= 0.5f;

        boolean isEast = x > 0 && Math.abs(z) < Math.abs(x);
        boolean isWest = x <= 0 && Math.abs(z) < Math.abs(x);
        boolean isSouth = z > 0 && Math.abs(x) <= Math.abs(z);
        boolean isNorth = z <= 0 && Math.abs(x) <= Math.abs(z);

        EnumFacing block1facing = EnumFacing.UP;

        if(isEast) block1facing = EnumFacing.EAST;
        if(isWest) block1facing = EnumFacing.WEST;
        if(isSouth) block1facing = EnumFacing.SOUTH;
        if(isNorth) block1facing = EnumFacing.NORTH;
        return block1facing;
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (!worldIn.isRemote && entityIn.canTrample(worldIn, this, pos, fallDistance)) // Forge: Move logic to Entity#canTrample
        {
            worldIn.setBlockState(pos,getBaseState());
        }

        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid())
        {
            worldIn.setBlockState(pos,getBaseState());
        }
    }

    @Override
    public void updateTick(World worldIn, @Nonnull BlockPos pos, IBlockState state, Random rand)
    {
        if(shouldFall)
        {
            checkFallable(worldIn,pos);
        }
    }

    private void checkFallable(World worldIn, BlockPos pos)
    {
        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0)
        {
            worldIn.setBlockState(pos,getBaseState(),2);
            worldIn.immediateBlockTick(pos,getBaseState(),worldIn.rand);
        }
    }

    @Override
    public int getDustColor(IBlockState state) {
        Block block = getBaseState().getBlock();
        return block instanceof BlockFalling ? ((BlockFalling) block).getDustColor(getBaseState()) : super.getDustColor(state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, SHAPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(SHAPE).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(SHAPE, SandDirection.byMetadata(meta));
    }

    public static enum SandDirection implements IStringSerializable {
        NORTH_SOUTH(0, "north_south", EnumFacing.NORTH, EnumFacing.SOUTH),
        EAST_WEST(1, "east_west", EnumFacing.EAST, EnumFacing.WEST),
        SOUTH_EAST(2, "south_east", EnumFacing.SOUTH, EnumFacing.EAST),
        SOUTH_WEST(3, "south_west", EnumFacing.SOUTH, EnumFacing.WEST),
        NORTH_WEST(4, "north_west", EnumFacing.NORTH, EnumFacing.WEST),
        NORTH_EAST(5, "north_east", EnumFacing.NORTH, EnumFacing.EAST);

        private static final SandDirection[] META_LOOKUP = new SandDirection[values().length];
        private final int meta;
        private final String name;
        private final EnumFacing direction1;
        private final EnumFacing direction2;

        SandDirection(int meta, String name, EnumFacing direction1, EnumFacing direction2)
        {
            this.meta = meta;
            this.name = name;
            this.direction1 = direction1;
            this.direction2 = direction2;
        }

        public String getName()
        {
            return this.name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public EnumFacing getDirection1() {
            return direction1;
        }

        public EnumFacing getDirection2() {
            return direction2;
        }

        public String toString()
        {
            return this.name;
        }

        public static SandDirection getByDirections(EnumFacing dir) {
            return getByDirections(dir,dir);
        }

        public static SandDirection getByDirections(EnumFacing dir1, EnumFacing dir2) {
            if(dir1 == dir2)
                return dir1.getAxis() == EnumFacing.Axis.X ? EAST_WEST : NORTH_SOUTH;

            for (SandDirection dir : values()) {
                if((dir1 == dir.direction1 && dir2 == dir.direction2) || (dir1 == dir.direction2 && dir2 == dir.direction1))
                    return dir;
            }

            return SandDirection.NORTH_SOUTH;
        }

        public static SandDirection byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        static
        {
            for (SandDirection dir : values())
            {
                META_LOOKUP[dir.getMetadata()] = dir;
            }
        }
    }
}
