package betterwithaddons.util;

import betterwithaddons.crafting.OreStack;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.*;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemUtil
{
	public static boolean matchesOreDict(ItemStack stack, String oreDictName)
	{
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

	public static boolean consumeItem(List<EntityItem> inv, Object stack)
	{
		if(stack instanceof ItemStack)
			return consumeItem(inv,(ItemStack)stack);
		if(stack instanceof OreStack)
			return consumeItem(inv,(OreStack)stack);
		return false;
	}

	public static boolean consumeItem(List<EntityItem> inv, ItemStack stack)
	{
		int remainder = stack.getCount();

		for (EntityItem ent: inv) {
			ItemStack item = ent.getEntityItem();
			if(stack.isItemEqual(item) || (stack.getMetadata() == OreDictionary.WILDCARD_VALUE && stack.getItem() == item.getItem()))
			{
				remainder -= consumeItem(ent,remainder);
			}
		}

		return remainder <= 0;
	}

	public static boolean consumeItem(List<EntityItem> inv, OreStack stack)
	{
		int remainder = stack.getStackSize();

		for (EntityItem ent: inv) {
			ItemStack item = ent.getEntityItem();
			if(stack.matches(item))
			{
				remainder -= consumeItem(ent,remainder);
			}
		}

		return remainder <= 0;
	}

	public static int consumeItem(EntityItem item, int n)
	{
		ItemStack entstack = item.getEntityItem().copy();

		int removed = Math.min(n,entstack.getCount());
		entstack.shrink(removed);
		if(entstack.getCount() <= 0)
			item.setDead();
		else
			item.setEntityItemStack(entstack);

		return removed;
	}

}
