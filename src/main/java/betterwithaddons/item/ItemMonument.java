package betterwithaddons.item;

import betterwithaddons.util.IDisableable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemMonument extends Item implements IDisableable {
    private boolean disabled;

    public ItemMonument() {
        super();

        this.addPropertyOverride(new ResourceLocation("minecraft:blocking"), new IItemPropertyGetter(){

            @SideOnly(value= Side.CLIENT)
            @Override
            public float apply(ItemStack itemStack, @Nullable World world, @Nullable EntityLivingBase entityLivingBase) {
                return entityLivingBase != null && entityLivingBase.isHandActive() && entityLivingBase.getActiveItemStack() == itemStack ? 1.0f : 0.0f;
            }
        });
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer entityPlayer, World world, BlockPos blockPos, EnumHand enumHand, EnumFacing enumFacing, float f, float f2, float f3) {
        return super.onItemUse(entityPlayer, world, blockPos, enumHand, enumFacing, f, f2, f3);
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand enumHand) {
        entityPlayer.setActiveHand(enumHand);
        ItemStack itemStack = entityPlayer.getHeldItem(enumHand);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(!disabled)
        super.getSubItems(itemIn, tab, subItems);
    }
}