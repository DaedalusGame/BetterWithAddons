package betterwithaddons.block.Factorization;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityBrine;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockBrine extends BlockFluidClassic {
    public static final Material MATERIAL_BRINE = new MaterialLiquid(MapColor.SILVER);

    public static final PropertyInteger STATE = PropertyInteger.create("state", 0, 2);

    public BlockBrine() {
        super(FluidRegistry.WATER,MATERIAL_BRINE);

        String name = "brine";

        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 5));
    }

    @Override
    public Material getMaterial(IBlockState state) {
        if(state.getValue(STATE) == 0)
            return Material.WATER;
        else
            return MATERIAL_BRINE;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBrine();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STATE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(STATE,meta).withProperty(LEVEL,getLiquidLevel(meta));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[] { LEVEL, STATE }, FLUID_RENDER_PROPS.toArray(new IUnlistedProperty<?>[0]));
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand)
    {
        if(state.getValue(STATE) > 0)
            return;

        int quantaRemaining = quantaPerBlock - state.getValue(LEVEL);

        // Flow vertically if possible
        if (canDisplace(world, pos.up(densityDir)))
        {
            flowIntoBlock(world, pos.up(densityDir), 1);
            return;
        }

        // Flow outward if possible
        int flowMeta = quantaPerBlock - quantaRemaining + 1;
        if (flowMeta >= quantaPerBlock)
        {
            return;
        }

        if (isSourceBlock(world, pos) || !isFlowingVertically(world, pos))
        {
            if (world.getBlockState(pos.down(densityDir)).getBlock() == this)
            {
                flowMeta = 1;
            }
            boolean flowTo[] = getOptimalFlowDirections(world, pos);

            if (flowTo[0]) flowIntoBlock(world, pos.add(-1, 0,  0), flowMeta);
            if (flowTo[1]) flowIntoBlock(world, pos.add( 1, 0,  0), flowMeta);
            if (flowTo[2]) flowIntoBlock(world, pos.add( 0, 0, -1), flowMeta);
            if (flowTo[3]) flowIntoBlock(world, pos.add( 0, 0,  1), flowMeta);
        }
    }

    /*@Override
    public int getQuantaValue(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.AIR)
        {
            return 0;
        }

        if (state.getBlock() != this)
        {
            return -1;
        }

        int quantaRemaining = quantaPerBlock - state.getValue(LEVEL);
        return quantaRemaining;
    }*/

    @Override
    protected void flowIntoBlock(World world, BlockPos pos, int meta)
    {
        if (meta < 0) return;
        if (displaceIfPossible(world, pos) && world.getBlockState(pos).getMaterial() != Material.WATER)
        {
            world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState().withProperty(LEVEL, meta), 3);
        }
    }

    @Override
    public boolean canCollideCheck(@Nonnull IBlockState state, boolean fullHit) {
        return fullHit && state.getValue(LEVEL) == 0;
    }

    @Override
    public int getMaxRenderHeightMeta() {
        return -1;
    }

    public int getLiquidLevel(int state)
    {
        switch(state)
        {
            case(0):return 5;
            case(1):return 6;
            case(2):return 7;
            default:return -1;
        }
    }

    public IBlockState getNextState(IBlockState state)
    {
        int brinestate = state.getValue(STATE);
        switch(brinestate)
        {
            case(0):
            case(1):
                return getDefaultState().withProperty(STATE,brinestate+1).withProperty(LEVEL,getLiquidLevel(brinestate+1));
            //case(2):
            //    return ModBlocks.saltLayer.getDefaultState();
        }

        return null;
    }

    public boolean isValidOnAllSides(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            if(!isValidBlock(world,pos.offset(facing),facing,state))
                return false;
        }
        return true;
    }

    public boolean isValidBlock(World world, BlockPos pos, EnumFacing facing, IBlockState thisstate)
    {
        IBlockState state = world.getBlockState(pos);
        if(state.isSideSolid(world,pos,facing.getOpposite()))
            return true;
        /*else if((state.getBlock() == ModBlocks.brine || state.getMaterial() == Material.WATER) && state.getValue(LEVEL) >= thisstate.getValue(LEVEL))
            return true;
        else if(state.getBlock() == ModBlocks.saltLayer)
            return true;*/

        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        return;
    }

    @Override
    public int place(World world, BlockPos pos, @Nonnull FluidStack fluidStack, boolean doPlace) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(World world, BlockPos pos, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canDrain(World world, BlockPos pos) {
        return false;
    }
}
