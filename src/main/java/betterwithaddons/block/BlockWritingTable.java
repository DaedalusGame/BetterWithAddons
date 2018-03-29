package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.GuiIds;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IHasVariants;
import betterwithmods.common.blocks.BlockWoodTable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockWritingTable extends BlockWoodTable implements IHasVariants {
    protected BlockWritingTable() {
        setUnlocalizedName("writing_table");
        setRegistryName(new ResourceLocation(Reference.MOD_ID, "writing_table"));
        setCreativeTab(BetterWithAddons.instance.creativeTab);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote)
            playerIn.openGui(BetterWithAddons.instance, GuiIds.WRITING_TABLE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        String[] variants = getVariants();
        ArrayList<ModelResourceLocation> list = new ArrayList<>();
        for (String variant : variants)
            list.add(new ModelResourceLocation(getRegistryName(),variant));
        return list;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
