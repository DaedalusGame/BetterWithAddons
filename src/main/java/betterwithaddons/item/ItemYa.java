package betterwithaddons.item;

import betterwithaddons.entity.EntityYa;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemYa extends Item {
    private boolean disabled;

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