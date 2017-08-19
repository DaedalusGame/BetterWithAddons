package betterwithaddons.block;

import betterwithaddons.util.ISpecialMeasuringBehavior;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Random;

public abstract class BlockWeight extends BlockBase {
    public static final HashMap<Block,ISpecialMeasuringBehavior> SPECIAL_BEHAVIOR = new HashMap<>();

    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    protected BlockWeight(String name) {
        super(name, Material.WOOD);
        this.setHardness(3.5F);
        this.setTickRandomly(true);
        this.setDefaultState(blockState.getBaseState().withProperty(ACTIVE,false));
    }

    public static void addSpecialMeasuringBehavior(Block block,ISpecialMeasuringBehavior behavior)
    {
        SPECIAL_BEHAVIOR.put(block,behavior);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if(fromPos.equals(pos.up()))
            worldIn.scheduleUpdate(pos,this,1);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        int nextMeasureCycle = measureBlock(worldIn,pos,worldIn.getBlockState(pos));

        worldIn.scheduleUpdate(pos,this,nextMeasureCycle);
    }

    public int measureBlock(World world, BlockPos pos, IBlockState state)
    {
        BlockPos checkpos = pos.up();
        IBlockState checkstate = world.getBlockState(checkpos);
        TileEntity te = world.getTileEntity(checkpos);

        boolean isFull = true;
        boolean isEmpty = true;
        int delay = 20;

        if(te != null)
        {
            boolean foundAnyMeasurable = false;

            for(EnumFacing facing : EnumFacing.VALUES) {
                if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing))
                {
                    IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,facing);
                    if(!InventoryUtil.isEmpty(inventory))
                        isEmpty = false;
                    if(!InventoryUtil.isFull(inventory))
                        isFull = false;

                    foundAnyMeasurable = true;
                }

                if(te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,facing))
                {
                    IFluidHandler tank = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,facing);
                    FluidStack fluid = tank.drain(Integer.MAX_VALUE,false);
                    if(fluid != null && tank.fill(fluid,false) > 0)
                        isFull = false;
                    if(fluid != null && fluid.amount != 0)
                        isEmpty = false;

                    foundAnyMeasurable = true;
                }
            }

            if(foundAnyMeasurable)
                delay = 1;
            else
                isFull = false;
        }
        else if(SPECIAL_BEHAVIOR.containsKey(checkstate.getBlock()))
        {
            ISpecialMeasuringBehavior behavior = SPECIAL_BEHAVIOR.get(checkstate.getBlock());

            isEmpty = behavior.isEmpty(world,checkpos,checkstate);
            isFull = behavior.isFull(world,checkpos,checkstate);

            delay = behavior.getDelay(world,checkpos,checkstate);
        }
        else
        {
            isFull = false;
        }

        boolean active = decideActivity(isEmpty,isFull);
        if(active != state.getValue(ACTIVE)) {
            world.setBlockState(pos, state.withProperty(ACTIVE, active));
            float f = active ? 0.6F : 0.5F;
            world.playSound(null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 1.0F, f);
        }

        return delay;
    }

    public abstract boolean decideActivity(boolean isEmpty, boolean isFull);

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(ACTIVE) ? 16 : 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(ACTIVE, meta > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(ACTIVE) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { ACTIVE });
    }
}
