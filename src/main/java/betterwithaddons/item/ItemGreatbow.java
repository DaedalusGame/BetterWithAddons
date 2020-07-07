package betterwithaddons.item;

import betterwithaddons.entity.EntityGreatarrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemGreatbow extends ItemBow {
    public static final String TAG_LIGHTNING_CHARGE = "LightningCharge";
    private boolean disabled;

    public ItemGreatbow() {
        super();
        this.setMaxDamage(576);

        addPropertyOverride(new ResourceLocation("minecraft:pull"), new IItemPropertyGetter(){
            @SideOnly(value=Side.CLIENT)
            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0F;
                } else {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return !itemstack.isEmpty() && itemstack.getItem() instanceof ItemGreatbow ? (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 60.0F : 0.0F;
                }
            }
        });
    }

    @Override
    protected ItemStack findAmmo(EntityPlayer player) {
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

        return ItemStack.EMPTY;
    }

    @Override
    protected boolean isArrow(@Nullable ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof ItemGreatarrow;
    }

    @Override
    public boolean canContinueUsing(ItemStack oldStack, ItemStack newStack){
        // Ignore durability changes
        if(ItemStack.areItemsEqualIgnoreDurability(oldStack, newStack)) return true;
        return super.canContinueUsing(oldStack, newStack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if(ItemStack.areItemsEqualIgnoreDurability(oldStack, newStack)) return false;
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return getCharge(stack) >= 3 || super.hasEffect(stack);
    }

    private int getCharge(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound != null)
            return compound.getInteger(TAG_LIGHTNING_CHARGE);
        return 0;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count) {
        World world = player.world;
        int time = getMaxItemUseDuration(stack) - count;
        if(!world.isRemote && world.canSeeSky(player.getPosition().up()) && player.rotationPitch < -55 /*&& world.isThundering()*/ && usingChargedAmmo(player)) {
            NBTTagCompound compound = stack.getTagCompound();
            if(compound == null)
                compound = new NBTTagCompound();
            int charges = compound.getInteger(TAG_LIGHTNING_CHARGE);
            if(time > 50 && time % 10 == 0) {
                if(charges < 3) {
                    charges++;
                    compound.setInteger(TAG_LIGHTNING_CHARGE, charges);
                    double slide = (double)charges / 3;
                    double angle = itemRand.nextDouble() * Math.PI * 2;
                    double radius = MathHelper.clampedLerp(40, 0, slide);
                    double dx = Math.sin(angle) * radius;
                    double dz = Math.cos(angle) * radius;
                    double y = world.getHeight((int)(player.posX + dx),(int)(player.posZ + dz));
                    world.addWeatherEffect(new EntityLightningBolt(world, player.posX + dx, y, player.posZ + dz, charges >= 3));
                    stack.setTagCompound(compound);
                }
            }
        }
    }

    private boolean usingChargedAmmo(EntityLivingBase player) {
        if(player instanceof EntityPlayer) {
            ItemStack ammo = findAmmo((EntityPlayer) player);
            Item item = ammo.getItem();
            if(item instanceof ItemGreatarrow)
                return ((ItemGreatarrow) item).canChargeFire();
        }
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        NBTTagCompound compound = stack.getTagCompound();
        if(compound == null)
            compound = new NBTTagCompound();
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
                    itemstack = new ItemStack(ModItems.GREATARROW);
                }
                if ((double)(f = ItemGreatbow.getArrowVelocity(i)) >= 0.1) {
                    boolean flag1 = entityplayer.capabilities.isCreativeMode || itemstack.getItem() instanceof ItemGreatarrow && ((ItemGreatarrow)itemstack.getItem()).isInfinite(itemstack, stack, entityplayer);
                    if (!worldIn.isRemote) {
                        int enchpower = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        int enchpunch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        ItemGreatarrow itemarrow = (ItemGreatarrow)(itemstack.getItem() instanceof ItemGreatarrow ? itemstack.getItem() : ModItems.GREATARROW);
                        EntityGreatarrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
                        entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0f, f * 4.5f, 0.5f);
                        if (f == 1.0f) {
                            entityarrow.setIsCritical(true);
                        }

                        entityarrow.setDamage(entityarrow.getDamage() + (double)enchpower * 0.5 + 0.5);
                        entityarrow.setKnockbackStrength(enchpunch);
                        entityarrow.setBlockBreakPower(entityarrow.getBlockBreakPower() * f * (enchpower+1.0f));
                        if(compound.getInteger(TAG_LIGHTNING_CHARGE) >= 3) {
                            entityarrow.setCharged(true);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            entityarrow.setFire(100);
                        }
                        stack.damageItem(1, entityplayer);
                        if (flag1) {
                            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }
                        worldIn.spawnEntity(entityarrow);
                    }
                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_SKELETON_DEATH, SoundCategory.NEUTRAL, 1.0f, 1.0f / (itemRand.nextFloat() * 0.4f + 2.2f) - f * 0.2f);
                    if (!flag1 && !flag) {
                        itemstack.shrink(1);
                        if (itemstack.getCount() == 0) {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }
                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
        compound.removeTag(TAG_LIGHTNING_CHARGE);
        if(compound.hasNoTags())
            compound = null;
        stack.setTagCompound(compound);
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
