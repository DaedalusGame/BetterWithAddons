package betterwithaddons.block;

import betterwithaddons.tileentity.TileEntityPress;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPress extends BlockContainerBase {
    public BlockPress(String name, Material materialIn) {
        super(name, materialIn);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityPress();
    }
}
