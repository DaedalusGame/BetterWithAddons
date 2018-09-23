package betterwithaddons.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemFoodBowl extends ItemFood {
    public ItemFoodBowl(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

    public ItemFoodBowl(int amount, boolean isWolfFood) {
        super(amount, isWolfFood);
    }

    @Override
    @Nonnull
    public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World worldIn, EntityLivingBase entityLiving)
    {
        super.onItemUseFinish(stack, worldIn, entityLiving);
        return new ItemStack(Items.BOWL);
    }
}
