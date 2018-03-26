package betterwithaddons.potion.effects;

import betterwithaddons.potion.PotionBase;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class EffectBoss extends PotionBase
{
	ResourceLocation icon = new ResourceLocation("betterwithaddons:textures/gui/effects/boss.png");
	
	public EffectBoss()
	{
		super("boss", false, Color.BLACK.getRGB());
		
		this.setPotionName("Boss");
		this.setIconIndex(1,0);
	}
}
