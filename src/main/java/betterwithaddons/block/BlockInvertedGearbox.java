package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityInvertedGearbox;
import betterwithmods.common.blocks.EnumTier;
import betterwithmods.common.blocks.mechanical.BlockGearbox;
import betterwithmods.util.DirUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class BlockInvertedGearbox extends BlockGearbox {
    protected BlockInvertedGearbox(String name) {
        super(1,EnumTier.WOOD);
        this.setUnlocalizedName(name);
        this.setRegistryName(Reference.MOD_ID, name);
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean[] dirs = new boolean[6];
        EnumFacing gearfacing = this.getFacing(world, pos);
        for (int i = 0; i < 6; i++) {
            EnumFacing facing = EnumFacing.getFront(i);
            dirs[i] = MechanicalUtil.isAxle(world, pos.offset(facing), facing.getOpposite()) && gearfacing != facing && gearfacing != facing.getOpposite();
        }
        return state.withProperty(DirUtils.DOWN, dirs[0]).withProperty(DirUtils.UP, dirs[1]).withProperty(DirUtils.NORTH, dirs[2]).withProperty(DirUtils.SOUTH, dirs[3]).withProperty(DirUtils.WEST, dirs[4]).withProperty(DirUtils.EAST, dirs[5]);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {}

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityInvertedGearbox();
    }
}
