package betterwithaddons.config;

import betterwithaddons.interaction.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfiguration {
    Configuration configuration;
    ConfigOption BWM_Enabled = new ConfigOption("interaction.BetterWithMods", "Enabled", InteractionBWM.ENABLED);
    ConfigOption BWM_MillClay = new ConfigOption("interaction.BetterWithMods", "MillClay", InteractionBWM.MILL_CLAY, "Hardened clay can be milled into bricks, saving fuel and allowing dyed bricks.");
    ConfigOption BWM_ChorusInCauldron = new ConfigOption("interaction.BetterWithMods", "ChorusInCauldron", InteractionBWM.CHORUS_IN_CAULDRON, "Chorus fruit (and Midori) can only be popped in a cauldron.");

    ConfigOption Quark_Enabled = new ConfigOption("interaction.Quark", "Enabled", InteractionQuark.ENABLED);
    ConfigOption Quark_MidoriBlocksNeedChunks = new ConfigOption("interaction.Quark", "Enabled", InteractionQuark.MIDORI_BLOCKS_NEED_CHUNKS, "Midori blocks require popped Midori chunks.");

    ConfigOption BWA_StoneBricksNeedSmelting = new ConfigOption("addons.BetterWithAddons", "Enabled", InteractionBWA.STONEBRICKS_NEED_SMELTING, "Stonebricks need two extra steps in crafting.");

    ConfigOption CondensedOutputs_Enabled = new ConfigOption("addons.CondensedOutputs", "Enabled", InteractionCondensedOutputs.ENABLED);
    ConfigOption CondensedOutputs_LoseBinder = new ConfigOption("addons.CondensedOutputs", "LoseBinder", InteractionCondensedOutputs.LOSE_BINDER, "When uncrafting condensed materials, the binding material is not returned.");

    ConfigOption EriottoMod_Enabled = new ConfigOption("addons.EriottoMod", "Enabled", InteractionEriottoMod.ENABLED);
    ConfigOption EriottoMod_GrassDropsSeeds = new ConfigOption("addons.EriottoMod", "GrassDropsSeeds", InteractionEriottoMod.GRASS_DROPS_SEEDS, "Rice and Rush seeds can be gotten from breaking grass.");

    public void preInit(FMLPreInitializationEvent event)
    {
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        configuration.load();

        configuration.getCategory("interaction").setComment("Enable or disable mod interactions.");

        InteractionBWM.ENABLED = BWM_Enabled.init(configuration);
        InteractionBWM.MILL_CLAY = BWM_MillClay.init(configuration);
        InteractionBWM.CHORUS_IN_CAULDRON = BWM_ChorusInCauldron.init(configuration);
        InteractionQuark.ENABLED = Quark_Enabled.init(configuration);
        InteractionQuark.MIDORI_BLOCKS_NEED_CHUNKS = Quark_MidoriBlocksNeedChunks.init(configuration);

        configuration.getCategory("addons").setComment("Configure individual addons.");

        InteractionBWA.STONEBRICKS_NEED_SMELTING = BWA_StoneBricksNeedSmelting.init(configuration);
        InteractionEriottoMod.ENABLED = EriottoMod_Enabled.init(configuration);
        InteractionEriottoMod.GRASS_DROPS_SEEDS = EriottoMod_GrassDropsSeeds.init(configuration);
        InteractionCondensedOutputs.ENABLED = CondensedOutputs_Enabled.init(configuration);
        InteractionCondensedOutputs.LOSE_BINDER = CondensedOutputs_LoseBinder.init(configuration);

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }
}
