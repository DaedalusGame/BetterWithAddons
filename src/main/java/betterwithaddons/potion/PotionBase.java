package betterwithaddons.potion;

import betterwithaddons.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionBase extends Potion
{
	ResourceLocation icon = new ResourceLocation("betterwithaddons:textures/gui/potions.png");

	protected PotionBase(String name, boolean isBadEffectIn, int liquidColorIn)
	{
		super(isBadEffectIn, liquidColorIn);

		this.setRegistryName(new ResourceLocation(Reference.MOD_ID,name));
		GameData.getPotionRegistry().register(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getStatusIconIndex() {
		Minecraft.getMinecraft().renderEngine.bindTexture(icon);

		return super.getStatusIconIndex();
	}
}
