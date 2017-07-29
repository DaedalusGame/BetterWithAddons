package betterwithaddons.potion;

import betterwithaddons.potion.effects.EffectBoss;
import betterwithaddons.potion.effects.EffectCannonball;
import betterwithaddons.potion.effects.EffectElectrified;
import betterwithaddons.potion.effects.EffectTarred;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModPotions
{
	public static EffectBoss boss;
	public static EffectCannonball cannonball;
	public static EffectElectrified electrified;
	public static EffectTarred tarred;

	public static void preInit(FMLPreInitializationEvent event)
	{
		boss = new EffectBoss();
		cannonball = new EffectCannonball();
		electrified = new EffectElectrified();
		tarred = new EffectTarred();
	}
}
