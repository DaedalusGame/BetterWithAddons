package betterwithaddons.item;

import betterwithaddons.block.ColorHandlers;
import betterwithaddons.block.IColorable;
import betterwithaddons.util.ItemUtil;
import betterwithaddons.util.NabeResultPoison;
import betterwithaddons.util.NabeResultTea;
import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemTeaCup extends Item implements IColorable {

    public static final int MAX_STACK_SIZE = 8;

    public ItemTeaCup()
    {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(MAX_STACK_SIZE);
        this.addPropertyOverride(new ResourceLocation("fillmodel"), (stack, worldIn, entityIn) -> isFilled(stack) ? 1 : 0);
    }

    public ItemStack getEmpty()
    {
        return new ItemStack(this);
    }

    public ItemStack getFilled(NabeResultTea liquid)
    {
        ItemStack filled = new ItemStack(this);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("type",liquid.isCeremonial ? "ceremonial" : "tea");
        compound.setInteger("color",liquid.getColor());
        compound.setString("teaType",liquid.mainType.getName());
        compound.setInteger("strength",liquid.strength);
        compound.setInteger("sugar",liquid.sugar);
        compound.setInteger("milk",liquid.milk);
        compound.setTag("effects",ItemUtil.serializePotionEffects(liquid.effects));
        filled.setTagCompound(compound);

        return filled;
    }

    public ItemStack getFilled(NabeResultPoison liquid)
    {
        ItemStack filled = new ItemStack(this);
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("type","poison");
        compound.setInteger("color",liquid.getColor());
        compound.setTag("effects",ItemUtil.serializePotionEffects(Lists.newArrayList(new PotionEffect(MobEffects.POISON,1000))));
        filled.setTagCompound(compound);

        return filled;
    }

    public static boolean isFilled(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        return compound != null && compound.hasKey("type");
    }

    public List<PotionEffect> getEffects(ItemStack stack) {
        NBTTagCompound compound = stack.getTagCompound();
        return compound != null ? ItemUtil.deserializePotionEffects(compound.getTagList("effects",10)) : new ArrayList<>();
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab))
        {
            items.add(getEmpty());
        }
    }

    private void affectEntity(EntityLivingBase entity, PotionEffect effect) {
        if(effect.getPotion().isInstant())
            effect.getPotion().affectEntity(null,null,entity,effect.getAmplifier(),1.0f);
        else
            entity.addPotionEffect(effect);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer)entityLiving : null;

        if (!worldIn.isRemote)
        {
            List<PotionEffect> effects = getEffects(stack);
            effects.forEach(effect -> affectEntity(entityLiving,effect));
        }

        if (entityplayer instanceof EntityPlayerMP)
        {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entityplayer, stack);
        }

        if (entityplayer != null)
        {
            entityplayer.addStat(StatList.getObjectUseStats(this));
        }

        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode)
        {
            stack.shrink(1);

            if (stack.isEmpty())
            {
                return getEmpty();
            }

            if (entityplayer != null)
            {
                entityplayer.inventory.addItemStackToInventory(getEmpty());
            }
        }

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 35;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if(!isFilled(stack))
            return new ActionResult<>(EnumActionResult.PASS, stack);
        playerIn.setActiveHand(handIn);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {

        if(isFilled(stack)) {
            NBTTagCompound compound = stack.getTagCompound();
            assert compound != null;
            String type = compound.getString("type");
            String teaType = compound.getString("teaType");
            int strength = compound.getInteger("strength");
            int sugar = compound.getInteger("sugar");
            int milk = compound.getInteger("milk");
            return I18n.translateToLocalFormatted("item.teacup."+type+".name", I18n.translateToLocal("tea.strength."+strength), I18n.translateToLocal("tea.sugar."+sugar), I18n.translateToLocal("tea.milk."+milk), I18n.translateToLocal("tea.type."+teaType));
        }
        else
            return I18n.translateToLocal("item.teacup.empty.name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        List<PotionEffect> effects = getEffects(stack);
        if(!effects.isEmpty())
            ItemUtil.addPotionEffectTooltip(effects, tooltip, 1.0F);
    }

    @Override
    public IBlockColor getBlockColor() {
        return null;
    }

    @Override
    public IItemColor getItemColor() {
        return ColorHandlers.TEACUP_COLORING;
    }
}
