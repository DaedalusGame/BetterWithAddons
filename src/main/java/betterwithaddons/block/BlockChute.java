package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.GuiIds;
import betterwithaddons.tileentity.TileEntityChute;
import betterwithmods.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

public class BlockChute extends BlockContainerBase {
    protected BlockChute() {
        super("chute", Material.WOOD);
        this.setHardness(1.5F);
        this.setResistance(2.0F);
        this.setHarvestLevel("axe", 0);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityChute();
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
}
