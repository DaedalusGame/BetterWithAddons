package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ColorHandlers;
import betterwithaddons.block.IColorable;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityTea;
import betterwithaddons.util.IHasVariants;
import betterwithaddons.util.TeaType;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockCropTea extends BlockCrops implements IHasVariants, IColorable {
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 8);

    public BlockCropTea()
    {
        super();

        this.setUnlocalizedName("crop_tea");
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "crop_tea"));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        checkAndDropBlock(world, pos, state);
        if (world.getLightFromNeighbors(pos.up()) >= 6) {
            int meta = getAge(state);
            float chance = getGrowthChance(this, world, pos);
            TileEntity tile = world.getTileEntity(pos);

            if (meta < 7) {
                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int)(25.0F / chance) + 1) == 0))
                {
                    world.setBlockState(pos, withAge(meta + 1));
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
                }
            } else if (meta == 7) {
                DimensionType dimension = world.provider.getDimensionType();
                TeaType grownType = null;
                if (rand.nextInt(100) < 2) { //Die off
                    world.setBlockState(pos, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
                } else if (dimension == DimensionType.NETHER) {
                    grownType = TeaType.NETHER;
                } else if (dimension == DimensionType.THE_END) {
                    grownType = TeaType.END;
                } else {
                    grownType = TeaType.getByLocation(world, pos, rand);
                }

                if (grownType != null && tile instanceof TileEntityTea) {
                    if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, true))
                    {
                        ((TileEntityTea) tile).setType(grownType);
                        world.setBlockState(pos, withAge(8));
                        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
                    }
                } else
                    world.setBlockState(pos, Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH));
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BUSH_AABB;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    protected Item getSeed()
    {
        return Item.getItemFromBlock(ModBlocks.tea);
    }

    @Override
    protected Item getCrop() {
        return Items.AIR;
    }

    @Override
    protected PropertyInteger getAgeProperty() {
        return AGE;
    }

    public TeaType getType(IBlockAccess world, BlockPos pos, IBlockState state)
    {
        if(world == null || pos == null)
            return TeaType.WHITE;
        TileEntity tile = world.getTileEntity(pos);
        return getType(tile);
    }

    public TeaType getType(TileEntity tile)
    {
        return tile instanceof TileEntityTea ? ((TileEntityTea) tile).getType() : TeaType.WHITE;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        player.addStat(StatList.getBlockStats(this));
        player.addExhaustion(0.005F);
        int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        boolean silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;

        NonNullList<ItemStack> items = NonNullList.create();
        getDrops(items, worldIn, pos, state, te, 0);
        float chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortuneLevel, 1.0f, silkTouch, player);

        harvesters.set(player);

        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
            items.stream().filter(item -> chance >= 1.0f || worldIn.rand.nextFloat() <= chance).forEach(item -> spawnAsEntity(worldIn, pos, item));
        }

        harvesters.set(null);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        getDrops(drops,world,pos,state,null,fortune);
    }

    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, TileEntity tile, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        int age = getAge(state);
        if(age >= getMaxAge()) {
            TeaType type = getType(tile);
            if (rand.nextInt(3) == 0 && type != TeaType.END && type != TeaType.NETHER)
                type = TeaType.WHITE;
            drops.add(ModItems.teaLeaves.getStack(type, rand.nextInt(3) + 1));
            for (int i = 0; i < 3 + fortune; ++i)
                if (rand.nextInt(2) == 0)
                    drops.add(new ItemStack(this.getSeed(), 1, 0));
        }
        drops.add(new ItemStack(getSeed(),1));
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots) {
            NonNullList<ItemStack> drops = NonNullList.create();
            getDrops(drops, worldIn, pos, state, worldIn.getTileEntity(pos), fortune);
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(drops, worldIn, pos, state, fortune, chance, false, harvesters.get());

            for (ItemStack drop : drops) {
                if (worldIn.rand.nextFloat() <= chance) {
                    spawnAsEntity(worldIn, pos, drop);
                }
            }
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTea();
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        return Lists.newArrayList(new ModelResourceLocation(Reference.MOD_ID+":seed_tea", "inventory"));
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }

    @Override
    public IBlockColor getBlockColor() {
        return ColorHandlers.TEA_COLORING;
    }

    @Override
    public IItemColor getItemColor() {
        return null;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return getAge(state) < getMaxAge();
    }

    @Override
    protected int getBonemealAgeIncrease(World worldIn) {
        return 1;
    }
}
