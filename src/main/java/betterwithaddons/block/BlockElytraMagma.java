package betterwithaddons.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by Christian on 02.08.2016.
 */
public class BlockElytraMagma extends BlockBase {
    public BlockElytraMagma() {
        super("elytra_magma", Material.ROCK);
        this.setLightLevel(0.2f);
        this.setTickRandomly(true);
    }

    @Override
    public MapColor getMapColor(IBlockState iBlockState) {
        return MapColor.PURPLE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getPackedLightmapCoords(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        return 15728880;
    }

    @Override
    public void onEntityWalk(World world, BlockPos blockPos, Entity entity) {
        if (!entity.isImmuneToFire() && entity instanceof EntityLivingBase) {
            entity.attackEntityFrom(DamageSource.magic, 1.0f);
        }
        super.onEntityWalk(world, blockPos, entity);
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState iBlockState, Random random) {
        BlockPos blockPos2 = blockPos.up();
        world.spawnParticle(EnumParticleTypes.SPELL_MOB, (double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 0.25, (double)blockPos2.getZ() + 0.5, 0.1, 0.1, 0.1, new int[0]);
    }

    @Override
    public boolean func_189872_a(IBlockState iBlockState, Entity entity) {
        return entity.isImmuneToFire();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
