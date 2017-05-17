package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockExtraGrass extends BlockGrass implements IColorable, IHasVariants {
    @Override
    public IBlockColor getBlockColor() {
        return ColorHandlers.GRASS_COLORING;
    }

    @Override
    public IItemColor getItemColor() {
        return ColorHandlers.BLOCK_ITEM_COLORING;
    }

    // add properties (note we also inherit the SNOWY property from BlockGrass)
    public static enum ExtraGrassType implements IStringSerializable
    {
        FARM, CLAY, SAND, REDSAND;
        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
        @Override
        public String toString()
        {
            return this.getName();
        }
    };
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", ExtraGrassType.class);
    @Override
    protected BlockStateContainer createBlockState() {return new BlockStateContainer(this, new IProperty[] { SNOWY, VARIANT });}

    public BlockExtraGrass()
    {
        super();

        // set some defaults
        this.setHardness(0.6F);
        this.setHarvestLevel("shovel", 0);
        this.setSoundType(SoundType.PLANT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)).withProperty(VARIANT, ExtraGrassType.FARM));

        this.setUnlocalizedName("extra_grass");
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "extra_grass"));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(getRegistryName(), "snowy=false,variant="+ExtraGrassType.FARM.getName()));
        rlist.add(new ModelResourceLocation(getRegistryName(), "snowy=false,variant="+ExtraGrassType.CLAY.getName()));
        rlist.add(new ModelResourceLocation(getRegistryName(), "snowy=false,variant="+ExtraGrassType.SAND.getName()));
        rlist.add(new ModelResourceLocation(getRegistryName(), "snowy=false,variant="+ExtraGrassType.REDSAND.getName()));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return ExtraGrassType.values()[meta].getName();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, NonNullList<ItemStack> stacks) {
        stacks.add(new ItemStack(item,1,0));
        stacks.add(new ItemStack(item,1,1));
        stacks.add(new ItemStack(item,1,2));
        stacks.add(new ItemStack(item,1,3));
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        // only one property in meta to worry about, the variant, so just map according to integer index in BOPGrassType
        return this.getDefaultState().withProperty(VARIANT, ExtraGrassType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        // only one property in meta to worry about, the variant, so just map according to integer index in BOPGrassType
        return ((ExtraGrassType) state.getValue(VARIANT)).ordinal();
    }

    public boolean canSustainPlantType(IBlockAccess world, BlockPos pos, EnumPlantType plantType)
    {

        IBlockState state = world.getBlockState(pos);

        switch ((ExtraGrassType) state.getValue(VARIANT))
        {
            case FARM:
                if (plantType == EnumPlantType.Crop) {return true;}
                break;
            case SAND:
                if (plantType == EnumPlantType.Desert) {return true;}
                break;

            default: break;
        }

        switch (plantType)
        {
            // support plains and cave plants
            case Plains: case Cave:
                return true;
            // support beach plants if there's water alongside
            case Beach:
                return (
                        (!world.isAirBlock(pos.east()) && world.getBlockState(pos.east()).getMaterial() == Material.WATER) ||
                                (!world.isAirBlock(pos.west()) && world.getBlockState(pos.west()).getMaterial() == Material.WATER) ||
                                (!world.isAirBlock(pos.north()) && world.getBlockState(pos.north()).getMaterial() == Material.WATER) ||
                                (!world.isAirBlock(pos.south()) && world.getBlockState(pos.south()).getMaterial() == Material.WATER)
                );
            // don't support nether plants, water plants, or crops (require farmland), or anything else by default
            default:
                return false;
        }
    }

    @Override
    public boolean canSustainPlant(IBlockState blockstate, IBlockAccess world, BlockPos blockpos, EnumFacing facing, IPlantable plantable) {
        return canSustainPlantType(world,blockpos,plantable.getPlantType(world,blockpos));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getStateFromMeta(meta);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        switch ((ExtraGrassType) state.getValue(VARIANT))
        {
            case SAND: case REDSAND:
                if (world.getBiomeForCoordsBody(pos).getTemperature() >= 2.0f)
                {
                    //world.setBlockState(pos, this.getDirtBlockState(state));
                }
                break;

            default:
                break;
        }
        this.spreadGrass(world, pos, state, rand, 40, 1, 3, 1);

    }

    // spread grass to suitable nearby blocks
    // tries - number of times to try and spread to a random nearby block
    // xzSpread - how far can the grass spread in the x and z directions
    // downSpread - how far can the grass spread downwards
    // upSpread - how far can the grass spread upwards
    public void spreadGrass(World world, BlockPos pos, IBlockState state, Random rand, int tries, int xzSpread, int downSpread, int upSpread)
    {

        // if this block is covered, then turn it back to dirt (IE kill the grass)
        if (world.getLightFromNeighbors(pos.up()) < 4 && world.getBlockState(pos.up()).getBlock().getLightOpacity(world.getBlockState(pos.up())) > 2)
        {
            world.setBlockState(pos, getDirtBlockState(state));
        }
        else
        {
            // if there's enough light from above, spread the grass randomly to nearby blocks of the correct dirt type
            if (world.getLightFromNeighbors(pos.up()) >= 9)
            {
                for (int i = 0; i < tries; ++i)
                {
                    // pick a random nearby position, and get the block, block state, and block above
                    BlockPos pos1 = pos.add(rand.nextInt(xzSpread * 2 + 1) - xzSpread, rand.nextInt(downSpread + upSpread + 1) - downSpread, rand.nextInt(xzSpread * 2 + 1) - xzSpread);
                    IBlockState target = world.getBlockState(pos1);
                    Block blockAboveTarget = world.getBlockState(pos1.up()).getBlock();

                    // see if this type of grass can spread to the target block
                    IBlockState targetGrass = spreadsToGrass(state, target);
                    if (targetGrass == null) {continue;}

                    // if there's enough light, turn the block to the relevant grass block
                    if (world.getLightFromNeighbors(pos1.up()) >= 4 && blockAboveTarget.getLightOpacity(target) <= 2)
                    {
                        world.setBlockState(pos1, targetGrass);
                    }
                }
            }
        }
    }

    // by default, getPickBlock uses damageDropped to determine the metadata of the block picked. This
    // doesn't suit our case as the block dropped has a different metadata configuration from this block
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)));
    }

    public boolean isClay(IBlockState state)
    {
        return state.getValue(VARIANT) == ExtraGrassType.CLAY;
    }

    @Override
    public int quantityDropped(IBlockState state, int n, Random random) {
        return isClay(state) ? 4 : 1;
    }

    // you need a silk touch tool to pick up grass-like blocks - by default they drop the corresponding 'dirt' type
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return isClay(state) ? Items.CLAY_BALL : Item.getItemFromBlock(getDirtBlock(state));
    }

    // goes hand in hand with getItemDropped() above to determine precisely what is dropped
    @Override
    public int damageDropped(IBlockState state)
    {
        return isClay(state) ? 0 : getDirtBlockMeta(state);
    }

    public static IBlockState getDirtBlockState(IBlockState state)
    {
        switch ((ExtraGrassType) state.getValue(VARIANT))
        {
            case CLAY:
                return Blocks.CLAY.getDefaultState();
            case SAND:
                return Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND);
            case REDSAND:
                return Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
            default:
                return Blocks.DIRT.getStateFromMeta(BlockDirt.DirtType.DIRT.getMetadata());
        }
    }

    public static Block getDirtBlock(IBlockState state)
    {
        return getDirtBlockState(state).getBlock();
    }

    public static int getDirtBlockMeta(IBlockState state)
    {
        return getDirtBlock(state).getMetaFromState(getDirtBlockState(state));
    }

    public static IBlockState spreadsToGrass(IBlockState source, IBlockState target) {

        if (target.getBlock() == Blocks.DIRT && target.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT)
        {
            return Blocks.GRASS.getDefaultState();
        }

        if (target.getBlock() == Blocks.CLAY)
        {
            return ModBlocks.grass.getDefaultState().withProperty(BlockExtraGrass.VARIANT, ExtraGrassType.CLAY);
        }

        if (target.getBlock() == Blocks.SAND)
        {
            if(target.getValue(BlockSand.VARIANT) == BlockSand.EnumType.SAND)
                return ModBlocks.grass.getDefaultState().withProperty(BlockExtraGrass.VARIANT, ExtraGrassType.SAND);
            if(target.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND)
                return ModBlocks.grass.getDefaultState().withProperty(BlockExtraGrass.VARIANT, ExtraGrassType.REDSAND);
        }

        return null;
    }
}
