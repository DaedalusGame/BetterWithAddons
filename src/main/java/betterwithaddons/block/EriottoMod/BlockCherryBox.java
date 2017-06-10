package betterwithaddons.block.EriottoMod;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockContainerBase;
import betterwithaddons.lib.GuiIds;
import betterwithaddons.tileentity.TileEntityDryingBox;
import betterwithaddons.tileentity.TileEntitySoakingBox;
import betterwithaddons.util.IHasVariants;
import betterwithmods.util.InvUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.List;

public class BlockCherryBox extends BlockContainerBase implements IHasVariants {
    public static final PropertyEnum VARIANT = PropertyEnum.create("variant", CherryBoxType.class);

    public enum CherryBoxType implements IStringSerializable
    {
        SOAKING, DRYING, NONE;
        @Override
        public String getName()
        {
            return this.name().toLowerCase();
        }
        @Override
        public String toString()
        {
            return this.getName();
        }
    }

    public BlockCherryBox()
    {
        super("cherrybox", Material.WOOD);
        this.setHardness(1.5F);
        this.setResistance(2.0F);
        this.setHarvestLevel("axe", 0);
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getStateFromMeta(meta);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(getRegistryName(), "variant="+CherryBoxType.SOAKING.getName()));
        rlist.add(new ModelResourceLocation(getRegistryName(), "variant="+CherryBoxType.DRYING.getName()));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return CherryBoxType.values()[meta].getName();
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tabs, NonNullList<ItemStack> stacks) {
        if(!disabled) {
            stacks.add(new ItemStack(item, 1, 0));
            stacks.add(new ItemStack(item, 1, 1));
        }
    }


    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        switch((CherryBoxType)state.getValue(VARIANT)) {
            case SOAKING: return new TileEntitySoakingBox();
            case DRYING: return new TileEntityDryingBox();
        }

        return null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            int guiId = -1;

            switch((CherryBoxType)state.getValue(VARIANT))
            {
                case SOAKING: guiId = GuiIds.SOAKING_BOX; break;
                case DRYING: guiId = GuiIds.DRYING_BOX; break;
            }

            if(guiId >= 0)
                playerIn.openGui(BetterWithAddons.instance, guiId, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
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

    @Override
    protected BlockStateContainer createBlockState() {return new BlockStateContainer(this, new IProperty[] { VARIANT });}

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        // only one property in meta to worry about, the variant, so just map according to integer index in BOPGrassType
        return this.getDefaultState().withProperty(VARIANT, CherryBoxType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        // only one property in meta to worry about, the variant, so just map according to integer index in BOPGrassType
        return ((CherryBoxType) state.getValue(VARIANT)).ordinal();
    }
}
