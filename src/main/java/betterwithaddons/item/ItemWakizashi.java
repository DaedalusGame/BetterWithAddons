package betterwithaddons.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

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
