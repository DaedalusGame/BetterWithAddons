package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.GuiIds;
import betterwithaddons.tileentity.TileEntityChute;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Random;

public class BlockChute extends BlockContainerBase implements IMechanicalBlock {
    public static final PropertyBool ISACTIVE = PropertyBool.create("ison");

    protected BlockChute() {
        super("chute", Material.WOOD);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityChute();
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean gettingPower = this.isInputtingMechPower(world, pos);
        boolean isOn = isMechanicalOn(world, pos);

        if (isOn != gettingPower)
            setMechanicalOn(world, pos, gettingPower);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            playerIn.openGui(BetterWithAddons.instance, GuiIds.CHUTE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!isCurrentStateValid(world, pos)) {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
        }

        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            ((TileEntityChute) world.getTileEntity(pos)).setOutputBlocked(facing,false);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null) {
            if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                InvUtils.ejectInventoryContents(world, pos, tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
                world.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(world, pos, state);
    }

    public int tickRate(World world) {
        return 10;
    }

    public boolean isCurrentStateValid(World world, BlockPos pos) {
        boolean gettingPower = isInputtingMechPower(world, pos);
        boolean isOn = isMechanicalOn(world, pos);
        return isOn == gettingPower;
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
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
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
    public boolean isInputtingMechPower(World world, BlockPos blockPos) {
        return MechanicalUtil.isBlockPoweredByAxle(world, blockPos, this) || MechanicalUtil.isPoweredByCrank(world, blockPos);
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return enumFacing == EnumFacing.DOWN;
    }

    @Override
    public void overpower(World world, BlockPos blockPos) {

    }

    @Override
    public boolean isMechanicalOn(IBlockAccess world, BlockPos pos) {
        return isMechanicalOnFromState(world.getBlockState(pos));
    }

    @Override
    public void setMechanicalOn(World world, BlockPos pos, boolean isOn) {
        if (isOn != world.getBlockState(pos).getValue(ISACTIVE)) {
            world.setBlockState(pos, world.getBlockState(pos).withProperty(ISACTIVE, isOn));
        }
    }

    @Override
    public boolean isMechanicalOnFromState(IBlockState state) {
        return state.getValue(ISACTIVE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ISACTIVE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ISACTIVE,(meta & 1) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(ISACTIVE) ? 1 : 0;
    }
}
