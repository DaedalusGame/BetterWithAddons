package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.world.WorldGenBigTrees;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;
import java.util.Random;

public class BlockModSapling extends BlockBush implements IGrowable, IPlantable {
    protected IBlockState leaves = Blocks.AIR.getDefaultState();
    protected IBlockState log = Blocks.AIR.getDefaultState();
    protected boolean isBig = false;

    protected static final AxisAlignedBB BOUNDING_BOX = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
    public ModWoods woodVariant;

    public BlockModSapling(ModWoods variant)
    {
        woodVariant = variant;

        this.setHardness(0.6F);
        this.setHarvestLevel("axe", 0);
        this.setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));

        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "sapling_"+woodVariant.getName()));
        this.setUnlocalizedName("sapling_"+woodVariant.getName());
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public BlockModSapling setLeaves(IBlockState leaves) {
        this.leaves = leaves;
        return this;
    }

    public BlockModSapling setLog(IBlockState log) {
        this.log = log;
        return this;
    }

    public BlockModSapling setBig(boolean big)
    {
        this.isBig = big;
        return this;
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal(this.getUnlocalizedName()+".name");
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDING_BOX;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }

    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        WorldGenerator worldgenerator = isBig ? new WorldGenBigTrees(true,log,leaves,this) : new WorldGenTrees(true,3,log,leaves,false);
        int i = 0;
        int j = 0;

        IBlockState iblockstate2 = Blocks.AIR.getDefaultState();

        worldIn.setBlockState(pos, iblockstate2, 4);

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j)))
        {
            worldIn.setBlockState(pos, state, 4);
        }
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(this));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(STAGE,meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {STAGE});
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return (double)worldIn.rand.nextFloat() < 0.45D;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        if ((state.getValue(STAGE)).intValue() == 0)
        {
            worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
        }
        else
        {
            this.generateTree(worldIn, pos, state, rand);
        }
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
}
