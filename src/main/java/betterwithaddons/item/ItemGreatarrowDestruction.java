package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class ItemGreatarrowDestruction extends ItemGreatarrow {
    @Override
    public void hitEntity(EntityGreatarrow arrow, Entity entity) {
        if(!(entity instanceof EntityLivingBase))
            return;

        if(!entity.world.isRemote) {
            EntityLivingBase living = (EntityLivingBase) entity;
            for (ItemStack armor : living.getArmorInventoryList()) {
                if(!armor.isEmpty())
                    armor.damageItem(50, living);
            }
        }
    }
}
