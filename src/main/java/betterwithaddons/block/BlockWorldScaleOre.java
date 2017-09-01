package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockWorldScaleOre extends BlockBase implements IHasVariants {
    public static final PropertyInteger CRACKED = PropertyInteger.create("cracked",0,5);

    public BlockWorldScaleOre() {
        super("world_scale_ore", Material.ROCK);

        this.setHardness(15.0F).setResistance(500.0F);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isBroken(IBlockState state)
    {
        return state.getValue(CRACKED) >= 4;
    }

    public boolean isBlock(IBlockState state) {return state.getValue(CRACKED) >= 5;}

    public void addCracks(World worldIn, BlockPos pos, IBlockState state, int n)
    {
        if(state.getBlock() instanceof BlockWorldScaleOre && !isBroken(state)) {
            worldIn.setBlockState(pos, state.withProperty(CRACKED, Math.max(Math.min(state.getValue(CRACKED) + n,5),0)), 2);
            worldIn.playEvent(2001, pos, Block.getStateId(state));
        }
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;

        if(isBroken(state))
        if(!isBlock(state))
            drops.add(new ItemStack(ModItems.worldShard,rand.nextInt(2)+3));
        else
            super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this,1,isBlock(state) ? 1 : 0);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(CRACKED,meta == 0 ? 0 : 5);
    }

    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(CRACKED, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(CRACKED);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { CRACKED });
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(tab.equals(BetterWithAddons.instance.creativeTab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(this.getRegistryName(), "cracked=0"));
        rlist.add(new ModelResourceLocation(this.getRegistryName(), "cracked=5"));

        return rlist;
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if(willHarvest)
            if(player.isCreative() || (isBroken(state) && exposedToElements(world,pos)))
                return super.removedByPlayer(state,world,pos,player,willHarvest);

        return false;
    }

    public boolean exposedToElements(World world, BlockPos pos) {
        Biome biome = world.getBiome(pos);
        boolean isHot = biome.getTempCategory() == Biome.TempCategory.WARM;
        boolean isRaining = world.isRainingAt(pos);
        boolean isCold = biome.isSnowyBiome();
        boolean isHumid = biome.canRain();
        boolean isDaytime = world.isDaytime();
        boolean isWaterAbove = world.getBlockState(pos.up()).getMaterial() == Material.WATER;

        if(isHot && !isHumid && isDaytime) //TODO: Switch this out for a lens check maybe. That would push it back in the tech tree :/
            return true;
        if(isCold && isRaining)
            return true;
        if(isWaterAbove)
            return true;

        return false;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        float modifier = 1.0f;

        if(!exposedToElements(worldIn,pos))
            modifier = 15.0f;

        return super.getBlockHardness(blockState, worldIn, pos) * modifier;
    }

    @Override
    public String getVariantName(int meta) {
        return meta > 0 ? "broken" : null;
    }
}
