package betterwithaddons.config;

import betterwithaddons.interaction.InteractionBWM;
import betterwithaddons.interaction.InteractionCondensedOutputs;
import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.interaction.InteractionQuark;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfiguration {
    Configuration configuration;

    public void preInit(FMLPreInitializationEvent event)
    {
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        configuration.load();

        configuration.getCategory("interaction").setComment("Enable or disable mod interactions.");

        InteractionBWM.ENABLED = configuration.get("interaction.BetterWithMods", "Enabled", true).getBoolean();
        InteractionBWM.MILL_CLAY = configuration.get("interaction.BetterWithMods", "MillClay", true).getBoolean();
        InteractionQuark.ENABLED = configuration.get("interaction.Quark", "Enabled", true).getBoolean();

        configuration.getCategory("addons").setComment("Configure individual addons.");
        configuration.get("interaction.BetterWithMods", "MillClay", true).setComment("Hardened clay can be milled into bricks, saving fuel and allowing dyed bricks.");

        InteractionEriottoMod.ENABLED = configuration.get("addons.EriottoMod", "Enabled", true).getBoolean();
        InteractionEriottoMod.GRASS_DROPS_SEEDS = configuration.get("addons.EriottoMod", "GrassDropsSeeds", true).getBoolean();
        InteractionCondensedOutputs.ENABLED = configuration.get("addons.CondensedOutputs", "Enabled", true).getBoolean();
        InteractionCondensedOutputs.LOSE_BINDER = configuration.get("addons.CondensedOutputs", "LoseBinder", false).getBoolean();

        configuration.get("addons.CondensedOutputs", "LoseBinder", false).setComment("When uncrafting condensed materials, the binding material is not returned.");

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }
}
