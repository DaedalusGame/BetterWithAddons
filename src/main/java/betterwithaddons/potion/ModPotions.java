package betterwithaddons.potion;

import betterwithaddons.potion.effects.EffectBoss;
import betterwithaddons.potion.effects.EffectCannonball;
import betterwithaddons.potion.effects.EffectElectrified;
import betterwithaddons.potion.effects.EffectTarred;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class ModPotions
{
	@GameRegistry.ObjectHolder("betterwithaddons:boss")
	public static EffectBoss boss;
	@GameRegistry.ObjectHolder("betterwithaddons:cannonball")
	public static EffectCannonball cannonball;
	@GameRegistry.ObjectHolder("betterwithaddons:electrified")
	public static EffectElectrified electrified;
	@GameRegistry.ObjectHolder("betterwithaddons:tarred")
	public static EffectTarred tarred;

	public static void preInit(FMLPreInitializationEvent event)
	{
	}

	@SubscribeEvent
	public static void registerPotions(RegistryEvent.Register<Potion> event) {
		event.getRegistry().register(new EffectBoss());
		event.getRegistry().register(new EffectCannonball());
		event.getRegistry().register(new EffectElectrified());
		event.getRegistry().register(new EffectTarred());
	}
}
