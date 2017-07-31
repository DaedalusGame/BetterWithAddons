package betterwithaddons.item;

import betterwithaddons.block.ColorHandlers;
import betterwithaddons.block.IColorable;
import betterwithaddons.client.models.ModelSamurai;
import betterwithaddons.lib.Reference;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;

public class ItemSamuraiArmor extends ItemArmor implements ISpecialArmor, IColorable {
    protected Map<EntityEquipmentSlot, ModelBiped> models = null;
    public final EntityEquipmentSlot type;
    private boolean disabled;

    public ItemSamuraiArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(ModItems.samuraiArmorMaterial, 0, equipmentSlotIn);
        this.type = equipmentSlotIn;
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModelForSlot(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot) {
        if(models == null)
            models = new EnumMap<>(EntityEquipmentSlot.class);

        return models.get(slot);
    }

    @SideOnly(Side.CLIENT)
    public ModelBiped provideArmorModelForSlot(ItemStack stack, EntityEquipmentSlot slot) {
        models.put(slot, new ModelSamurai(slot));
        return models.get(slot);
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped original) {
        ModelBiped model = getArmorModelForSlot(entityLiving, itemStack, armorSlot);
        if(model == null)
            model = provideArmorModelForSlot(itemStack, armorSlot);

        if(model != null) {
            model.setModelAttributes(original);
            return model;
        }

        return super.getArmorModel(entityLiving,itemStack,armorSlot,original);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.MOD_ID + ":textures/entity/samurai"+(type != null ? "_"+type : "")+".png";
    }

    @Override
    public String getUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        if(source.isUnblockable())
            return new ArmorProperties(0, 0, 0);
        return new ArmorProperties(0, damageReduceAmount / 25D, armor.getMaxDamage() + 1 - armor.getItemDamage());
    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }

    public boolean hasColor(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        return nbttagcompound != null && nbttagcompound.hasKey("display", 10) ? nbttagcompound.getCompoundTag("display").hasKey("color", 3) : false;
    }

    /**
     * Return the color for the specified armor ItemStack.
     */
    public int getColor(ItemStack stack)
    {

        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null)
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
            {
                return nbttagcompound1.getInteger("color");
            }
        }

        return 10046744;
    }

    /**
     * Remove the color from the specified armor ItemStack.
     */
    public void removeColor(ItemStack stack)
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound != null)
        {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

            if (nbttagcompound1.hasKey("color"))
            {
                nbttagcompound1.removeTag("color");
            }
        }
    }

    /**
     * Sets the color of the specified armor ItemStack
     */
    public void setColor(ItemStack stack, int color)
    {
        NBTTagCompound nbttagcompound = stack.getTagCompound();

        if (nbttagcompound == null)
        {
            nbttagcompound = new NBTTagCompound();
            stack.setTagCompound(nbttagcompound);
        }

        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

        if (!nbttagcompound.hasKey("display", 10))
        {
            nbttagcompound.setTag("display", nbttagcompound1);
        }

        nbttagcompound1.setInteger("color", color);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return damageReduceAmount;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
        stack.damageItem(damage,entity);
    }

    @Override
    public IBlockColor getBlockColor() {
        return null;
    }

    @Override
    public IItemColor getItemColor() {
        return ColorHandlers.ARMOR_COLORING;
    }
}
