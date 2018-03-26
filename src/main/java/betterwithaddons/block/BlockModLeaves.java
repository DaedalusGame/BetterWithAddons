package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockModLeaves extends BlockLeaves {
    protected ItemStack sapling;

    public ModWoods woodVariant;
    private boolean disabled;

    public BlockModLeaves(ModWoods variant)
    {
        super();
        woodVariant = variant;

        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "leaves_"+woodVariant.getName()));
        this.setUnlocalizedName("leaves_"+woodVariant.getName());
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, Boolean.valueOf(true)).withProperty(DECAYABLE, Boolean.valueOf(true)));
    }

    public void setSapling(ItemStack sapling) {
        this.sapling = sapling;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { CHECK_DECAY, DECAYABLE });
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(DECAYABLE, Boolean.valueOf((meta & 1) == 0)).withProperty(CHECK_DECAY, Boolean.valueOf((meta & 2) > 0));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = 0;
        if (!((Boolean)state.getValue(DECAYABLE)).booleanValue())
        {
            meta |= 1;
        }
        if (((Boolean)state.getValue(CHECK_DECAY)).booleanValue())
        {
            meta |= 2;
        }
        return meta;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getStateFromMeta(meta).withProperty(CHECK_DECAY, Boolean.valueOf(false)).withProperty(DECAYABLE, Boolean.valueOf(false));
    }

    @Override
    protected void dropApple(World worldIn, BlockPos pos, IBlockState state, int chance) {
        return;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack)
    {
        if (!worldIn.isRemote && !stack.isEmpty() && stack.getItem() == Items.SHEARS)
        {
            player.addStat(StatList.getBlockStats(this));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    @Override
    protected int getSaplingDropChance(IBlockState state)
    {
        return 20;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return sapling != null ? sapling.getItem() : null;
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return sapling != null ? sapling.getMetadata() : 0;
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return Blocks.LEAVES.getBlockLayer();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return Blocks.LEAVES.isOpaqueCube(state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return Blocks.LEAVES.shouldSideBeRendered(state, world, pos, side);
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this)
        {
            ret.add(new ItemStack(this));
        }
        return ret;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }
}
