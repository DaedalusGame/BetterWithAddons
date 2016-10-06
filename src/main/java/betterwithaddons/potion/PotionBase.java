package betterwithaddons.potion;

import betterwithaddons.lib.Reference;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;

public class PotionBase extends Potion
{

	protected PotionBase(String name, boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);

		this.setRegistryName(new ResourceLocation(Reference.MOD_ID,name));
		GameData.getPotionRegistry().register(this);
	}
}
