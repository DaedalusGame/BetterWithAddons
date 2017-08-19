package betterwithaddons.item;

import betterwithmods.module.tweaks.Dung;
import betterwithmods.module.tweaks.Dung.DungProducer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemLaxative extends Item {
    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if(target.hasCapability(Dung.DUNG_PRODUCER_CAP,null))
        {
            DungProducer poopCapability = target.getCapability(Dung.DUNG_PRODUCER_CAP,null);
            if(poopCapability.nextPoop > 4000)
            {
                poopCapability.nextPoop = poopCapability.nextPoop / 3;
            }
            stack.shrink(1);
            return true;
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }
}
