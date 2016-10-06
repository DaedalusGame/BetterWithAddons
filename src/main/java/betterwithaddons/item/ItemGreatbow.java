package betterwithaddons.item;

import javax.annotation.Nullable;

import betterwithaddons.entity.EntityGreatarrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGreatbow extends ItemBow {
    public ItemGreatbow() {
        super();
        this.setMaxDamage(384);

        addPropertyOverride(new ResourceLocation("minecraft:pull"), new IItemPropertyGetter(){
            @SideOnly(value=Side.CLIENT)
            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return itemstack != null && itemstack.getItem() instanceof ItemGreatbow ? (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 60.0F : 0.0F;
                }
            }
        });
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = player.inventory.getStackInSlot(i);
            if (!this.isArrow(itemstack)) continue;
            return itemstack;
        }

        return null;
    }

    @Override
    protected boolean isArrow(@Nullable ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemGreatarrow;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmo(entityplayer);
            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            if ((i = ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer)entityLiving, i, itemstack != null || flag)) < 0) {
                return;
            }
            if (itemstack != null || flag) {
                float f;
                if (itemstack == null) {
                    itemstack = new ItemStack(ModItems.greatarrow);
                }
                if ((double)(f = ItemGreatbow.getArrowVelocity(i)) >= 0.1) {
                    boolean flag1;
                    boolean bl = flag1 = entityplayer.capabilities.isCreativeMode || itemstack.getItem() instanceof ItemGreatarrow && ((ItemGreatarrow)itemstack.getItem()).isInfinite(itemstack, stack, entityplayer);
                    if (!worldIn.isRemote) {
                        int enchpower = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        int enchpunch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        ItemGreatarrow itemarrow = (ItemGreatarrow)(itemstack.getItem() instanceof ItemGreatarrow ? itemstack.getItem() : ModItems.greatarrow);
                        EntityGreatarrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
                        entityarrow.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0f, f * 4.5f, 0.5f);
                        if (f == 1.0f) {
                            entityarrow.setIsCritical(true);
                        }

                        entityarrow.setDamage(entityarrow.getDamage() * 2.0 + (double)enchpower * 0.5 + 0.5);
                        entityarrow.setKnockbackStrength(enchpunch);
                        entityarrow.setBlockBreakPower(entityarrow.getBlockBreakPower() * f * (enchpower+1.0f));

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            entityarrow.setFire(100);
                        }
                        stack.damageItem(1, entityplayer);
                        if (flag1) {
                            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }
                        worldIn.spawnEntityInWorld(entityarrow);
                    }
                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_SKELETON_DEATH, SoundCategory.NEUTRAL, 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 2.2f) - f * 0.2f);
                    if (!flag1) {
                        --itemstack.stackSize;
                        if (itemstack.stackSize == 0) {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }
                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    public static float getArrowVelocity(int charge) {
        float f = (float)charge / 60.0f;
        if ((f = (f * f + f * 0.1f) / 1.1f) > 1.0f) {
            f = 1.0f;
        }
        return f;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getItemEnchantability() {
        return 1;
    }
}