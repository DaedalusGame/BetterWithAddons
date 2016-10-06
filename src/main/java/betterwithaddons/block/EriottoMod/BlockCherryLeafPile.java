package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockBase;
import betterwithaddons.block.ModWoods;
import betterwithaddons.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Christian on 02.10.2016.
 */
public class BlockCherryLeafPile extends BlockBase {
    protected static final AxisAlignedBB LEAFPILE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    public BlockCherryLeafPile()
    {
        super("leafpile_sakura", Material.LEAVES);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return LEAFPILE_AABB;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return Blocks.LEAVES.getFlammability(world, pos, face);
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return Blocks.LEAVES.getFireSpreadSpeed(world, pos, face);
    }

    @Override
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return Blocks.LEAVES.getBlockLayer();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        if(!world.isRemote) {
            if(!this.canPlaceBlockAt(world, pos)) {
                world.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.isSideSolid(pos.down(),EnumFacing.UP);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return world == null || world.getBlockState(pos.offset(side)).getBlock() != this;
    }
}
