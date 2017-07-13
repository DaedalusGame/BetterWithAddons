package betterwithaddons.block.EriottoMod;

import betterwithaddons.block.BlockContainerBase;
import betterwithaddons.tileentity.TileEntityAncestrySand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAncestrySand extends BlockContainerBase {
    public BlockAncestrySand() {
        super("ancestry_sand", Material.SAND);
        this.setSoundType(SoundType.SAND);
        this.setHardness(0.5F);
    }

    @Override
    public MapColor getMapColor(IBlockState state) {
        return MapColor.NETHERRACK;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityAncestrySand();
    }
}
