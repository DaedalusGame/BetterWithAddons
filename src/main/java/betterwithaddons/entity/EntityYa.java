package betterwithaddons.entity;

import betterwithaddons.item.ModItems;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityYa extends EntityArrow {
    public EntityYa(World worldIn) {
        super(worldIn);
    }

    public EntityYa(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntityYa(World worldIn, IPosition pos) {
        super(worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(ModItems.ya);
    }
}