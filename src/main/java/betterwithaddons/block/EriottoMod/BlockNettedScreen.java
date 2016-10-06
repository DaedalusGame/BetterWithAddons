package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockContainerBase;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.tileentity.TileEntityNettedScreen;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import static betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType.*;

/**
 * Created by Christian on 18.09.2016.
 */
public class BlockNettedScreen extends BlockContainerBase {
    public BlockNettedScreen()
    {
        super("netted_screen",Material.WOOD);
        this.setHardness(1.0F);
        this.setResistance(0.2F);
        this.setHarvestLevel("axe", 0);
    }

    public enum SifterType implements IStringSerializable
    {
        NONE, WATER, SAND, FIRE;

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

    public SifterType getSifterType(World world, BlockPos pos)
    {
        IBlockState uppertype = null;
        IBlockState lowertype = null;
        IBlockState bottom = world.getBlockState(pos.down());

        for (int z = -1; z <= 1; z++)
        for (int x = -1; x <= 1; x++)
        {
            if(z != x || x != 0)
            {
                IBlockState upperstate = world.getBlockState(pos.add(x,0,z));
                IBlockState lowerstate = world.getBlockState(pos.add(x,-1,z));
                if(uppertype == null) uppertype = upperstate;
                else if(!uppertype.equals(upperstate)) return NONE;
                if(lowertype == null) lowertype = lowerstate;
                else if(!lowertype.equals(lowerstate)) return NONE;
            }
        }

        if(!(uppertype.getBlock() instanceof BlockSlat))
            return NONE;
        if(lowertype.getBlock() == ModBlocks.sakuraPlanks && bottom.getMaterial() == Material.WATER)
            return WATER;
        if(lowertype.getBlock() == Blocks.STONEBRICK && (bottom.getMaterial() == Material.LAVA || bottom.getMaterial() == Material.FIRE))
            return FIRE;
        if(lowertype.getMaterial() == Material.WATER && bottom.getBlock() instanceof BlockSlat)
            return SAND;

        return NONE;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityNettedScreen();
    }

    /*@Override
    public int tickRate(World world) {
        return 10;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRemote)
            return;

        captureDroppedItems(world,pos);
    }*/

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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
