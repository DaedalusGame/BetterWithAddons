package betterwithaddons.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Created by Christian on 26.09.2016.
 */
public class ItemWakizashi extends ItemSword {
    public ItemWakizashi()
    {
        super(ModItems.tamahaganeToolMaterial);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        boolean hit = super.hitEntity(stack,target,attacker);

        if(hit && target.getHealth() <= target.getMaxHealth() * 0.25f)
            target.attackEntityFrom(DamageSource.causeMobDamage(attacker).setDamageBypassesArmor(), 6.0f);

        return hit;
    }
}
