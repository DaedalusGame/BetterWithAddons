package betterwithaddons.block;

import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockEcksieSapling extends BlockBase implements IGrowable, IPlantable, IHasVariants {
    IBlockState[] treeLeaves;

    public static PropertyInteger TYPE = PropertyInteger.create("type",0,15);

    protected BlockEcksieSapling(String name,IBlockState[] leaves) {
        super(name, Material.WOOD);
        treeLeaves = leaves;
        this.setTickRandomly(true);
        this.setHardness(0.5f);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return getDefaultState().withProperty(TYPE,meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if(worldIn.isAirBlock(pos.up()))
            worldIn.setBlockState(pos.up(),getLeaf(state));
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        IBlockState soil = worldIn.getBlockState(pos.down());
        return soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            this.checkAndDropBlock(worldIn, pos, state);
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
            {
                this.grow(worldIn, rand, pos, state);
            }

        }
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return false;
    }

    private IBlockState getLeaf(IBlockState state)
    {
        return treeLeaves[state.getValue(TYPE)];
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        IBlockState leaf = getLeaf(state);

        int attempts = worldIn.rand.nextInt(8)+1;
        while(attempts > 0) {
            BlockPos.MutableBlockPos seeker = new BlockPos.MutableBlockPos(pos.up());
            for (int dist = 0; dist < 7; dist++) {
                IBlockState seekstate = worldIn.getBlockState(seeker);
                if (seekstate != leaf) {
                    if(worldIn.isAirBlock(seeker) || seekstate.getBlock().isReplaceable(worldIn,seeker)) {
                        worldIn.setBlockState(seeker, leaf);
                        if (dist < 2) attempts--;
                    }
                    break;
                }
                int xoff = worldIn.rand.nextInt(3) - 1;
                int zoff = xoff == 0 ? worldIn.rand.nextInt(2) * 2 - 1 : 0;
                seeker.setPos(seeker.getX() + xoff, seeker.getY(), seeker.getZ() + zoff);
            }
            attempts--;
        }
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return getDefaultState();
        return state;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        if(!disabled)
        for(int i = 0; i < 16; ++i) {
            if(i < treeLeaves.length && treeLeaves[i] != null)
                list.add(new ItemStack(this,1,i));
        }
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        for(int i = 0; i < 16; ++i) {
            if(i < treeLeaves.length && treeLeaves[i] != null)
                rlist.add(new ModelResourceLocation(getRegistryName(), "type="+i));
        }

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
