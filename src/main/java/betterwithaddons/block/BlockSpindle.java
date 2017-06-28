package betterwithaddons.block;

import betterwithaddons.crafting.SpindleRecipe;
import betterwithaddons.crafting.manager.CraftingManagerSpindle;
import betterwithaddons.util.ItemUtil;
import betterwithmods.api.block.IAxle;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.util.DirUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockSpindle extends BlockBase implements IMechanicalBlock, IAxle {
    public static final PropertyBool ISACTIVE = PropertyBool.create("ison");
    public static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis",EnumFacing.Axis.class);

    private static final AxisAlignedBB X_AABB = new AxisAlignedBB(0.0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
    private static final AxisAlignedBB Y_AABB = new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
    private static final AxisAlignedBB Z_AABB = new AxisAlignedBB(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1.0F);

    public BlockSpindle() {
        super("spindle", Material.WOOD);
        this.setHardness(2.0F).setResistance(1.0F);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel("axe", 0);
        this.setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch(state.getValue(AXIS))
        {
            case X: return X_AABB;
            case Y: return Y_AABB;
            case Z: return Z_AABB;
            default: return null;
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(AXIS,facing.getAxis());
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ISACTIVE,(meta & 1) > 0).withProperty(AXIS,EnumFacing.Axis.values()[(meta >> 1) & 3]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ISACTIVE) ? 1 : 0 | (state.getValue(AXIS).ordinal() << 1);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ISACTIVE, AXIS);
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
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean powered = isInputtingMechPower(world, pos);
        boolean isOn = isBlockOn(world, pos);

        if (isOn != powered) {
            setMechanicalOn(world, pos, powered);
        } else if (powered) {
            if(!world.isRemote)
                spinUpBolt(world, pos, state);
            world.scheduleBlockUpdate(pos, this, tickRate(world) + rand.nextInt(6), 5);
        }
    }

    public boolean isBlockOn(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getValue(ISACTIVE);
    }

    private void spinUpBolt(World world, BlockPos pos, IBlockState state) {
        List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos,pos.add(1,1,1)), EntitySelectors.IS_ALIVE);
        EnumFacing.Axis axis = state.getValue(AXIS);

        if(list.size() > 0) {
            CraftingManagerSpindle manager = CraftingManagerSpindle.getInstance();
            SpindleRecipe recipe = manager.getMostValidRecipe(list);

            if(recipe != null) {
                ItemUtil.consumeItem(list, recipe.getInput());

                List<ItemStack> ret = recipe.getOutput();
                if (ret != null && ret.size() > 0) {
                    List<EnumFacing> validDirections = new ArrayList<>();
                    for (EnumFacing facing : EnumFacing.VALUES) {
                        if(facing.getAxis() == axis || facing == EnumFacing.UP)
                            continue;

                        IBlockState check = world.getBlockState(pos.offset(facing));
                        if (check.getBlock().isReplaceable(world, pos.offset(facing)) || world.isAirBlock(pos.offset(facing)))
                            validDirections.add(facing);
                    }

                    for (int i = 0; i < ret.size(); i++) {
                        ItemStack item = ret.get(i);
                        if (item.isEmpty())
                            continue;

                        EnumFacing ejectdir;
                        if(validDirections.isEmpty())
                            ejectdir = axis.isVertical() ? EnumFacing.HORIZONTALS[world.rand.nextInt(4)] : EnumFacing.DOWN;
                        else
                            ejectdir = validDirections.get(world.rand.nextInt(validDirections.size()));

                        EntityItem result = new EntityItem(world, pos.getX() + 0.5f + ejectdir.getFrontOffsetX() * 0.2f, pos.getY() + 0.1f, pos.getZ() + 0.5f + ejectdir.getFrontOffsetZ() * 0.2f, item.copy());
                        result.setVelocity(ejectdir.getFrontOffsetX()*0.2,0,ejectdir.getFrontOffsetZ()*0.2);
                        result.setDefaultPickupDelay();
                        if (!world.isRemote) {
                            world.spawnEntity(result);
                        }
                    }

                    if (recipe.consumesSpindle) {
                        world.setBlockToAir(pos);
                    }
                }
            }
        }
    }

    @Override
    public int tickRate(World world) {
        return 5;
    }

    @Override
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = getFacingFromAxis(state.getValue(AXIS));
        return MechanicalUtil.isBlockPoweredOnSide(world, pos, facing) || MechanicalUtil.isBlockPoweredOnSide(world, pos, facing.getOpposite());
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos pos) {
        return false;
    }

    //This may break in the n-dimensional update
    public EnumFacing getFacingFromAxis(EnumFacing.Axis axis)
    {
        switch(axis)
        {
            case X:
                return EnumFacing.EAST;
            case Y:
                return EnumFacing.UP;
            case Z:
                return EnumFacing.NORTH;
        }

        return null;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(AXIS).apply(dir);
    }

    @Override
    public void overpower(World world, BlockPos pos) {
        //You can't overpower from the bottom
    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isBlockOn(world,pos);
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {
        boolean active = world.getBlockState(pos).getValue(ISACTIVE);
        if (isOn != active)
            world.setBlockState(pos, world.getBlockState(pos).withProperty(ISACTIVE, isOn));
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return state.getValue(ISACTIVE);
    }

    //Pretend to be an axle so we can look proper on a gearbox.
    @Override
    public int getAxisAlignment(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return DirUtils.getLegacyAxis(state.getValue(AXIS));
    }

    @Override
    public boolean isAxleOrientedToFacing(IBlockAccess world, BlockPos pos, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        return facing.getAxis() == state.getValue(AXIS);
    }

    @Override
    public int getPowerLevel(IBlockAccess iBlockAccess, BlockPos blockPos) {
        return 0;
    }
}
