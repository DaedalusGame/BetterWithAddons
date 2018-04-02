package betterwithaddons.item;

import betterwithaddons.entity.EntityYa;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemPoisonedYa extends ItemYa {
    @Override
    public void hitEntity(EntityYa entityYa, EntityLivingBase living) {
        super.hitEntity(entityYa, living);
        World world = entityYa.getEntityWorld();
        if(world.rand.nextDouble() <= 0.70)
            living.addPotionEffect(new PotionEffect(MobEffects.POISON,1000));
    }
}
