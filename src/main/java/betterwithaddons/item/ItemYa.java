package betterwithaddons.item;

/**
 * Created by Christian on 31.07.2016.
 */

import betterwithaddons.entity.EntityGreatarrow;
import betterwithaddons.entity.EntityYa;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemYa extends Item {
    public ItemYa() {
        super();
    }

    public EntityYa createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
        EntityYa entityarrow = new EntityYa(worldIn, shooter);
        return entityarrow;
    }

    public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
        return false;
    }
}