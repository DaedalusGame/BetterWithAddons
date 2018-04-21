package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockThornRose extends BlockBase implements IPlantable, IHasVariants {
    public static final int MAX_AGE = 9;

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, MAX_AGE);

    protected BlockThornRose() {
        super("thorn_rose", Material.WOOD);
        this.setHardness(4.0F);
        this.setHarvestLevel("axe", 0);
        this.setDefaultState(getDefaultState().withProperty(AGE, Integer.valueOf(0)));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
        this.setTickRandomly(true);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!this.canSurvive(worldIn, pos))
        {
            worldIn.destroyBlock(pos, true);
        }
        else
        {
            int age = state.getValue(AGE).intValue();
            int grownflowers = 0;
            EnumFacing stemfacing = getStem(worldIn,pos,rand);

            if(age < MAX_AGE) {
                for (int i = 0; i < 5; i++) {
                    EnumFacing growdir = EnumFacing.random(rand);
                    BlockPos checkpos = pos.offset(growdir);

                    if (ModBlocks.THORNS.canSurvive(worldIn, pos) && canPlaceBlockAt(worldIn, checkpos) && isGoodPlaceToGrow(worldIn, checkpos)) {
                        worldIn.setBlockState(pos, ModBlocks.THORNS.setStem(ModBlocks.THORNS.getDefaultState(),stemfacing), 2);
                        growFlower(worldIn, checkpos, age, growdir.getOpposite(), rand);
                        grownflowers++;
                    }

                    if (grownflowers >= 2)
                        break;
                }

                if (grownflowers == 0) {
                    EnumFacing growdir = EnumFacing.random(rand);
                    BlockPos checkpos = pos.offset(growdir);
                    if (ModBlocks.THORNS.canSurvive(worldIn, pos) && canPlaceBlockAt(worldIn, checkpos)) {
                        worldIn.setBlockState(pos, ModBlocks.THORNS.setStem(ModBlocks.THORNS.getDefaultState(),stemfacing), 2);
                        growFlower(worldIn, checkpos, age, growdir.getOpposite(), rand);
                        grownflowers++;
                    }
                }

                if (grownflowers == 0) {
                    if (age >= MAX_AGE - 1)
                        placeDeadFlower(worldIn, pos);
                    else
                        placeGrownFlower(worldIn, pos, age + 1);
                }
            }
            else if(hasAdjacentFlower(worldIn,pos))
            {
                if (ModBlocks.THORNS.canSurvive(worldIn, pos))
                    worldIn.setBlockState(pos, ModBlocks.THORNS.setStem(ModBlocks.THORNS.getDefaultState(),stemfacing), 2);
                else
                    worldIn.destroyBlock(pos, true);
            }
        }
    }

    private EnumFacing getStem(World world,BlockPos pos,Random rand)
    {
        ArrayList<EnumFacing> facings = new ArrayList<>();

        for (EnumFacing enumfacing : EnumFacing.VALUES) {
            BlockPos checkpos = pos.offset(enumfacing);
            IBlockState blockstate = world.getBlockState(checkpos);

            if(blockstate.getBlock() == ModBlocks.THORNS || isProperSoil(world,checkpos,enumfacing))
            {
                facings.add(enumfacing);
            }
        }

        return facings.isEmpty() ? EnumFacing.DOWN : facings.get(rand.nextInt(facings.size()));
    }

    private boolean hasAdjacentFlower(World world, BlockPos pos)
    {
        for (EnumFacing enumfacing : EnumFacing.VALUES) {
            BlockPos checkpos = pos.offset(enumfacing);
            IBlockState blockstate = world.getBlockState(checkpos);


            if(blockstate.getBlock() == this && blockstate.getValue(AGE) >= MAX_AGE)
            {
                return true;
            }
        }

        return false;
    }

    private boolean isGoodPlaceToGrow(World world, BlockPos pos)
    {
        boolean hassidewall = false;
        int vines = 0;
        for (EnumFacing enumfacing : EnumFacing.VALUES)
        {
            BlockPos checkpos = pos.offset(enumfacing);
            Block block = world.getBlockState(checkpos).getBlock();
            //if(world.isSideSolid(checkpos,enumfacing.getOpposite()) || ((world.isAirBlock(checkpos) || isVine(block)) && world.isSideSolid(checkpos.offset(enumfacing),enumfacing.getOpposite())))
            if(world.isSideSolid(checkpos,enumfacing.getOpposite()))
            {
                hassidewall = true;
            }
            if(isVine(block))
            {
                vines++;
            }
        }

        return canSurvive(world,pos) && hassidewall && vines <= 2;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> list = new ArrayList<>();
        int age = state.getValue(AGE);

        if(age == MAX_AGE)
            list.add(ModItems.MATERIAL.getMaterial("thornrose",1));
        else if(age == 0)
            list.add(new ItemStack(this,1));

        return list;
    }

    private boolean isVine(Block block)
    {
        return block == this || block == ModBlocks.THORNS;
    }

    private void growFlower(World world, BlockPos pos, int age, EnumFacing stem, Random rand)
    {
        if(age >= MAX_AGE-1) {
            if(rand.nextInt(5) == 0)
                placeDeadFlower(world, pos);
            else if (ModBlocks.THORNS.canPlaceBlockAt(world,pos))
                world.setBlockState(pos, ModBlocks.THORNS.setStem(ModBlocks.THORNS.getDefaultState(),stem), 2);
        }
        else if(rand.nextInt(5) == 0)
            placeGrownFlower(world,pos,age);
        else
            placeGrownFlower(world,pos,age+1);
    }

    private void placeGrownFlower(World world, BlockPos pos, int age)
    {
        world.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(age)), 2);
    }

    private void placeDeadFlower(World world, BlockPos pos)
    {
        world.setBlockState(pos, this.getDefaultState().withProperty(AGE, Integer.valueOf(MAX_AGE)), 2);
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if(state.getValue(AGE) < MAX_AGE && entityIn instanceof EntityLivingBase)
            entityIn.attackEntityFrom(DamageSource.CACTUS, 5.0F);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Desert;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return getDefaultState();
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && this.canSurvive(worldIn, pos);
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        if (!this.canSurvive(worldIn, pos))
        {
            worldIn.scheduleUpdate(pos, this, 1);
        }
    }

    public boolean canSurvive(World world, BlockPos pos)
    {
        int adjacentVines = 0;
        int adjacentSoil = 0;

        for (EnumFacing enumfacing : EnumFacing.VALUES)
        {
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock();

            if (isVine(block))
            {
                adjacentVines++;
            }
            else if(isProperSoil(world,blockpos,enumfacing))
            {
                adjacentSoil++;
            }
        }

        return (adjacentSoil > 0 || adjacentVines > 0) && adjacentVines <= 3;
    }

    public boolean isProperSoil(IBlockAccess world, BlockPos pos, EnumFacing facing)
    {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block == Blocks.SAND || block == Blocks.MOSSY_COBBLESTONE || (block == Blocks.STONEBRICK && state.getValue(BlockStoneBrick.VARIANT) == BlockStoneBrick.EnumType.MOSSY) || block.canSustainPlant(world.getBlockState(pos),world,pos,facing.getOpposite(),this);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE});
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(Reference.MOD_ID+":seed_thorns", "inventory"));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
