package betterwithaddons.block.Factorization;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockBase;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockPondBase extends BlockBase implements IHasVariants {
    private static final int FULLY_DRIED = 2;
    public static final PropertyInteger DRYSTATE = PropertyInteger.create("drystate", 0, FULLY_DRIED);

    public BlockPondBase() {
        super("pond_base", Material.ROCK);

        setResistance(8.0F);
        this.setTickRandomly(true);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        switch(stack.getMetadata()) {
            case 0:
                tooltip.add(I18n.format("tooltip.pond_creation.name"));
                break;
            case 1:
                tooltip.add(I18n.format("tooltip.salt_creation.name"));
        }
        super.addInformation(stack, player, tooltip, advanced);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return isDried(state) ? 1 : 0;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        if(tab.equals(BetterWithAddons.instance.creativeTab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(DRYSTATE,meta == 0 ? 0 : FULLY_DRIED);
    }

    public boolean isDried(IBlockState state) {
        return state.getValue(DRYSTATE) == FULLY_DRIED;
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return isDried(state) ? "pickaxe" : "shovel";
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return isDried(state) ? Material.CLAY : Material.ROCK;
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return isDried(state) ? MapColor.CLAY : MapColor.ADOBE;
    }

    @Override
    public float getBlockHardness(IBlockState state, World worldIn, BlockPos pos) {
        return isDried(state) ? 2.1f : 1.5f;
    }

    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return isDried(state) ? SoundType.STONE : SoundType.SAND;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(isDried(state))
        {
            worldIn.scheduleUpdate(pos, this, 10);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DRYSTATE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(DRYSTATE,meta);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DRYSTATE);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if(isDried(state))
            fillPond(worldIn, pos);
        else if(isExposedToSun(worldIn,pos))
            worldIn.setBlockState(pos,state.withProperty(DRYSTATE,state.getValue(DRYSTATE)+1));
    }

    public void fillPond(World worldIn, BlockPos pos) {
        IBlockState waterState = worldIn.getBlockState(pos.up());
        Block block = waterState.getBlock();

        if(block != ModBlocks.brine) {
            if(waterState.getMaterial() == Material.WATER) {
                if (waterState.getValue(BlockLiquid.LEVEL) < 5 && isValidOnAllSides(worldIn, pos.up(), waterState)) {
                    worldIn.setBlockState(pos.up(), ModBlocks.brine.getDefaultState(), 3);
                    worldIn.notifyNeighborsOfStateChange(pos, this, false);
                }
                worldIn.scheduleUpdate(pos, this, 10);
            }
        }
    }

    public boolean isValidOnAllSides(World world, BlockPos pos, IBlockState thisstate)
    {
        int thislevel = thisstate.getValue(BlockLiquid.LEVEL);
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            BlockPos sidepos = pos.offset(facing);
            IBlockState sidestate = world.getBlockState(sidepos);
            if(sidestate.getBlock() != ModBlocks.brine)
            if(sidestate.getMaterial() == Material.WATER) {
                if (sidestate.getValue(BlockLiquid.LEVEL) > thislevel)
                    return false;
            }
            else if(!sidestate.isSideSolid(world,sidepos,facing.getOpposite()))
                return false;
        }
        return true;
    }

    public boolean isExposedToSun(World world, BlockPos pos) {
        BlockPos checkpos = pos.up();
        Biome biome = world.getBiome(checkpos);

        return world.isDaytime() && !world.isRaining() && !world.isThundering() && world.provider.hasSkyLight() && !BiomeDictionary.hasType(biome, BiomeDictionary.Type.COLD) && world.canSeeSky(checkpos); //TODO: configurable check for biome?
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<>();

        rlist.add(new ModelResourceLocation(getRegistryName(),"drystate=0"));
        rlist.add(new ModelResourceLocation(getRegistryName(),"drystate="+FULLY_DRIED));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        switch(meta)
        {
            case(0):return "wet";
            case(1):return "dry";
        }

        return null;
    }
}
