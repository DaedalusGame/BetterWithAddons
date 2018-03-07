package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGreatarrow extends Item {
    public ItemGreatarrow() {
        super();
    }

    public EntityGreatarrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityGreatarrow entityarrow = new EntityGreatarrow(worldIn, shooter);
        entityarrow.setArrowStack(stack);
        return entityarrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        return false;
    }

    public void hitBlock(EntityGreatarrow arrow,BlockPos pos, IBlockState state, boolean destroyed) {
        //NOOP
    }

    public void hitBlockFinal(EntityGreatarrow arrow) {
        //NOOP
    }

    public void hitEntity(EntityGreatarrow arrow, Entity entity) {
        //NOOP
    }
}