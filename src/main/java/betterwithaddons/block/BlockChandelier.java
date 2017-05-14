package betterwithaddons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockChandelier extends BlockBase {
    public BlockChandelier() {
        super("chandelier", Material.IRON);
        setHardness(2.0f);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn,pos);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        checkAndDrop(worldIn,pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        double x = (double)pos.getX();
        double y = (double)pos.getY() + (1.0D / 16.0D) * 14.0D;
        double z = (double)pos.getZ();

        double x1 = (1.0D / 16.0D) * 2.0D;
        double z1 = (1.0D / 16.0D) * 8.0D;
        double x2 = (1.0D / 16.0D) * 14.0D;

        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + x1, y, z + z1, 0.0D, 0.0D, 0.0D, new int[0]);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, x + x1, y, z + z1, 0.0D, 0.0D, 0.0D, new int[0]);
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + x2, y, z + z1, 0.0D, 0.0D, 0.0D, new int[0]);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, x + x2, y, z + z1, 0.0D, 0.0D, 0.0D, new int[0]);

        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + z1, y, z + x1, 0.0D, 0.0D, 0.0D, new int[0]);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, x + z1, y, z + x1, 0.0D, 0.0D, 0.0D, new int[0]);
        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + z1, y, z + x2, 0.0D, 0.0D, 0.0D, new int[0]);
        worldIn.spawnParticle(EnumParticleTypes.FLAME, x + z1, y, z + x2, 0.0D, 0.0D, 0.0D, new int[0]);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos frompos) {
        super.neighborChanged(state, worldIn, pos, blockIn,frompos);
        checkAndDrop(worldIn,pos);
    }

    public void checkAndDrop(World worldIn, BlockPos pos)
    {
        if (!worldIn.isRemote && !canBlockStay(worldIn, pos) && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)))
        {
            EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, worldIn.getBlockState(pos));
            entityfallingblock.shouldDropItem = true;
            entityfallingblock.setHurtEntities(true);
            worldIn.spawnEntity(entityfallingblock);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        IBlockState top = worldIn.getBlockState(pos.up());
        return top.isSideSolid(worldIn,pos, EnumFacing.DOWN);
    }
}
