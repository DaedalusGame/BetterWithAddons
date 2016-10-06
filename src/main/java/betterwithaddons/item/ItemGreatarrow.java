package betterwithaddons.item;

/**
 * Created by Christian on 31.07.2016.
 */

import betterwithaddons.entity.EntityGreatarrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGreatarrow extends Item {
    public ItemGreatarrow() {
        super();
    }

    public EntityGreatarrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityGreatarrow entityarrow = new EntityGreatarrow(worldIn, shooter);
        return entityarrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        return false;
    }
}