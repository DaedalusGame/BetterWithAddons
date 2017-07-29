package betterwithaddons.potion.effects;

import betterwithaddons.potion.PotionBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
