package betterwithaddons.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class ItemShinai extends ItemSword {
    public ItemShinai()
    {
        super(ModItems.bambooToolMaterial);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer attacker, Entity entity) {
        double xRatio = attacker.posX - entity.posX;
        double zRatio;

        for (zRatio = attacker.posZ - entity.posZ; xRatio * xRatio + zRatio * zRatio < 1.0E-4D; zRatio = (Math.random() - Math.random()) * 0.01D)
        {
            xRatio = (Math.random() - Math.random()) * 0.01D;
        }

        if(!entity.worldObj.isRemote && entity instanceof EntityLivingBase)
        {
            EntityLivingBase target = (EntityLivingBase) entity;
            target.knockBack(attacker,0.4F * attacker.getCooledAttackStrength(0),xRatio,zRatio);
            if(target.getHealth() <= 1.0f) {
                target.attackEntityFrom(DamageSource.causeMobDamage(attacker).setDamageBypassesArmor(), 1.0f);
            }

            int j = EnchantmentHelper.getFireAspectModifier(attacker);

            if (j > 0)
            {
                target.setFire(j * 4);
            }

            stack.damageItem(1, attacker);
        }

        return true;
    }
}
