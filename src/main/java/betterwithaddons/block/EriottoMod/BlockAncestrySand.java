package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockContainerBase;
import betterwithaddons.tileentity.TileEntityAncestrySand;
import betterwithmods.api.block.IMechanicalBlock;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAncestrySand extends BlockContainerBase implements IMechanicalBlock {
    public static final PropertyBool ISACTIVE = PropertyBool.create("ison");

    public BlockAncestrySand() {
        super("ancestry_sand", Material.SAND);
        this.setSoundType(SoundType.SAND);
        this.setHardness(0.5F);
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

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.SOUL_SAND);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.isSneaking() || !playerIn.getHeldItem(hand).isEmpty())
            return super.onBlockActivated(worldIn,pos,state,playerIn,hand,facing,hitX,hitY,hitZ);

        TileEntity te = worldIn.getTileEntity(pos);
        if(!worldIn.isRemote && te instanceof TileEntityAncestrySand)
        {
            playerIn.sendStatusMessage(new TextComponentTranslation("tile.ancestry_sand.spirits",((TileEntityAncestrySand) te).getSpirits()),true);
        }

        return true;
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.NETHERRACK;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAncestrySand();
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        boolean gettingPower = this.isInputtingMechPower(world, pos);
        boolean isOn = isMechanicalOn(world, pos);

        if (isOn != gettingPower)
            setMechanicalOn(world, pos, gettingPower);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos other) {
        if (!isCurrentStateValid(world, pos)) {
            world.scheduleBlockUpdate(pos, this, tickRate(world), 5);
        }
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
    public boolean canOutputMechanicalPower() {
        return false;
    }

    @Override
    public boolean canInputMechanicalPower() {
        return true;
    }

    @Override
    public boolean isInputtingMechPower(World world, BlockPos blockPos) {
        return MechanicalUtil.isBlockPoweredByAxle(world, blockPos, this);
    }

    @Override
    public boolean isOutputtingMechPower(World world, BlockPos blockPos) {
        return false;
    }

    @Override
    public boolean canInputPowerToSide(IBlockAccess iBlockAccess, BlockPos blockPos, EnumFacing enumFacing) {
        return true;
    }

    @Override
    public void overpower(World world, BlockPos blockPos) {
        world.destroyBlock(blockPos,false);
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
}
