package betterwithaddons.block;

import betterwithaddons.tileentity.TileEntityMigration;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMigration extends BlockContainerBase {
    public BlockMigration(String name, Material materialIn) {
        super(name, materialIn);

        this.setBlockUnbreakable();
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMigration();
    }
}
