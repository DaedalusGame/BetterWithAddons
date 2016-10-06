package betterwithaddons.block;

import betterwithaddons.tileentity.TileEntityWorldScaleActive;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 02.08.2016.
 */
public class BlockWorldScaleActive extends BlockContainerBase implements IHasVariants {
    public BlockWorldScaleActive() {
        super("world_scale_active", Material.ROCK);

        this.setHardness(5.0F).setResistance(500.0F);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World world, BlockPos blockpos, IBlockState blockstate) {
        ((TileEntityWorldScaleActive) world.getTileEntity(blockpos)).unclaimAllChunks();

        super.breakBlock(world, blockpos, blockstate);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityWorldScaleActive();
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(this.getRegistryName(), "normal"));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
