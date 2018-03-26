package betterwithaddons.potion.effects;

import betterwithaddons.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class EffectCannonball extends PotionBase
{
	ResourceLocation icon = new ResourceLocation("betterwithaddons:textures/gui/effects/cannonball.png");

	public EffectCannonball()
	{
		super("cannonball", false, Color.pink.getRGB());
		
		this.setPotionName("Cannonball");
		this.setIconIndex(0,0);
	}
}
