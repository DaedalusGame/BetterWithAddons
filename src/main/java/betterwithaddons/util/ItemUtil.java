package betterwithaddons.util;

import com.google.common.collect.Lists;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemUtil
{
	public static boolean matchesOreDict(ItemStack stack, String oreDictName)
	{
		if(stack.isEmpty()) return false;
		int checkid = OreDictionary.getOreID(oreDictName);
		for (int id:OreDictionary.getOreIDs(stack)) {
			if(id == checkid) return true;
		}
		return false;
	}

	public static boolean areItemStackContentEqual(ItemStack is1, ItemStack is2)
	{
		if (is1.isEmpty() || is2.isEmpty())
		{
			return false;
		}

		if (is1.getItem() != is2.getItem())
		{
			return false;
		}

		if (!ItemStack.areItemStackTagsEqual(is1, is2))
		{
			return false;
		}

		return is1.getItemDamage() == is2.getItemDamage();
	}

	public static boolean areOreDictionaried(ItemStack is1, ItemStack is2)
	{
		if (is1.isEmpty() || is2.isEmpty())
		{
			return false;
		}
		int[] ids1 = OreDictionary.getOreIDs(is1);
		int[] ids2 = OreDictionary.getOreIDs(is2);

		for (int id1 : ids1)
		{
			for (int id2 : ids2)
			{
				if (id1 == id2)
				{
					return true;
				}
			}
		}

		return false;
	}

	public static boolean isTool(Item item)
	{
		return item instanceof ItemTool || item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemShears || item instanceof ItemBow || item instanceof ItemHoe;
	}

	public static boolean consumeItem(List<EntityItem> inv, Ingredient ingredient)
	{
		int amount = ingredient instanceof IHasSize ? ((IHasSize) ingredient).getSize() : 1;
		for (EntityItem ent : inv) {
			ItemStack item = ent.getItem();
			if(ingredient.apply(item))
				amount -= consumeItem(ent,amount);
		}
		return amount <= 0;
	}

	public static int consumeItem(EntityItem item, int n)
	{
		ItemStack entstack = item.getItem().copy();

		int removed = Math.min(n,entstack.getCount());
		entstack.shrink(removed);
		if(entstack.getCount() <= 0)
			item.setDead();
		else
			item.setItem(entstack);

		return removed;
	}

	public static NBTTagList serializePotionEffects(List<PotionEffect> effects) {
		NBTTagList potionList = new NBTTagList();
		for (PotionEffect effect : effects)
			potionList.appendTag(effect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
		return potionList;
	}

	public static List<PotionEffect> deserializePotionEffects(NBTTagList potionList) {
		ArrayList<PotionEffect> effects = new ArrayList<>();
		for (int i = 0; i < potionList.tagCount(); ++i)
			effects.add(PotionEffect.readCustomPotionEffectFromNBT(potionList.getCompoundTagAt(i)));
		return effects;
	}

	@SideOnly(Side.CLIENT)
	public static void addPotionEffectTooltip(List<PotionEffect> list, List<String> lores, float durationFactor)
	{
		List<Tuple<String, AttributeModifier>> attributeModifiers = Lists.newArrayList();

		if (list.isEmpty())
		{
			String s = I18n.translateToLocal("effect.none").trim();
			lores.add(TextFormatting.GRAY + s);
		}
		else
		{
			for (PotionEffect potioneffect : list)
			{
				String s1 = I18n.translateToLocal(potioneffect.getEffectName()).trim();
				Potion potion = potioneffect.getPotion();
				Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

				if (!map.isEmpty())
				{
					for (Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet())
					{
						AttributeModifier attributemodifier = entry.getValue();
						AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
						attributeModifiers.add(new Tuple((entry.getKey()).getName(), attributemodifier1));
					}
				}

				if (potioneffect.getAmplifier() > 0)
				{
					s1 = s1 + " " + I18n.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
				}

				if (potioneffect.getDuration() > 20)
				{
					s1 = s1 + " (" + Potion.getPotionDurationString(potioneffect, durationFactor) + ")";
				}

				if (potion.isBadEffect())
				{
					lores.add(TextFormatting.RED + s1);
				}
				else
				{
					lores.add(TextFormatting.BLUE + s1);
				}
			}
		}

		if (!attributeModifiers.isEmpty())
		{
			lores.add("");
			lores.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));

			for (Tuple<String, AttributeModifier> tuple : attributeModifiers)
			{
				AttributeModifier attributemodifier2 = tuple.getSecond();
				double d0 = attributemodifier2.getAmount();
				double d1;

				if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2)
				{
					d1 = attributemodifier2.getAmount();
				}
				else
				{
					d1 = attributemodifier2.getAmount() * 100.0D;
				}

				if (d0 > 0.0D)
				{
					lores.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)tuple.getFirst())));
				}
				else if (d0 < 0.0D)
				{
					d1 = d1 * -1.0D;
					lores.add(TextFormatting.RED + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)tuple.getFirst())));
				}
			}
		}
	}
}
