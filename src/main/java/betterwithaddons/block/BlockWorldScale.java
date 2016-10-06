package betterwithaddons.block;

import betterwithaddons.util.IHasVariants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 02.08.2016.
 */
public class BlockWorldScale extends BlockBase implements IHasVariants {
    public static final PropertyBool CRACKED = PropertyBool.create("cracked");

    public BlockWorldScale() {
        super("world_scale", Material.ROCK);

        this.setHardness(5.0F).setResistance(500.0F);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public void setCracked(World worldIn, BlockPos pos, IBlockState state, boolean cracked)
    {
        if(state.getBlock() instanceof BlockWorldScale && state.getValue(CRACKED) != cracked) {
            worldIn.setBlockState(pos, state.withProperty(CRACKED, cracked), 2);
            worldIn.playEvent(2001, pos, Block.getStateId(state));
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(CRACKED, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(CRACKED) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { CRACKED });
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        rlist.add(new ModelResourceLocation(this.getRegistryName(), "cracked=false"));

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
