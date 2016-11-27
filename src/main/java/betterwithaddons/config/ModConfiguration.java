package betterwithaddons.config;

import betterwithaddons.interaction.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfiguration {
    Configuration configuration;
    ConfigOptionBool BWM_Enabled = new ConfigOptionBool("interaction.BetterWithMods", "Enabled", InteractionBWM.ENABLED);
    ConfigOptionBool BWM_MillClay = new ConfigOptionBool("interaction.BetterWithMods", "MillClay", InteractionBWM.MILL_CLAY, "Hardened clay can be milled into bricks, saving fuel and allowing dyed bricks.");
    ConfigOptionBool BWM_ChorusInCauldron = new ConfigOptionBool("interaction.BetterWithMods", "ChorusInCauldron", InteractionBWM.CHORUS_IN_CAULDRON, "Chorus fruit (and Midori) can only be popped in a cauldron.");

    ConfigOptionBool Quark_Enabled = new ConfigOptionBool("interaction.Quark", "Enabled", InteractionQuark.ENABLED);
    ConfigOptionBool Quark_MidoriBlocksNeedChunks = new ConfigOptionBool("interaction.Quark", "Enabled", InteractionQuark.MIDORI_BLOCKS_NEED_CHUNKS, "Midori blocks require popped Midori chunks.");

    ConfigOptionBool BWA_StoneBricksNeedSmelting = new ConfigOptionBool("addons.BetterWithAddons", "StoneBricksNeedSmelting", InteractionBWA.STONEBRICKS_NEED_SMELTING, "Stonebricks need two extra steps in crafting.");
    ConfigOptionInteger BWA_LureTreeRadius = new ConfigOptionInteger("addons.BetterWithAddons", "LureTreeRadius", InteractionBWA.RADIUS, "Radius in which the tree can spawn mobs.");
    ConfigOptionInteger BWA_LureTreeTime = new ConfigOptionInteger("addons.BetterWithAddons", "LureTreeTime", InteractionBWA.MAXCHARGE, "Time it takes for the tree to do one spawning cycle.");
    ConfigOptionInteger BWA_LureTreeMaxFood = new ConfigOptionInteger("addons.BetterWithAddons", "LureTreeMaxFood", InteractionBWA.MAXFOOD, "How much food the tree can hold.");
    ConfigOptionInteger BWA_LureTreeFoodGlowstone = new ConfigOptionInteger("addons.BetterWithAddons", "LureTreeFoodGlowstone", InteractionBWA.FOODGLOWSTONE, "How much food is contained in one glowstone dust.");

    ConfigOptionBool CondensedOutputs_Enabled = new ConfigOptionBool("addons.CondensedOutputs", "Enabled", InteractionCondensedOutputs.ENABLED);
    ConfigOptionBool CondensedOutputs_LoseBinder = new ConfigOptionBool("addons.CondensedOutputs", "LoseBinder", InteractionCondensedOutputs.LOSE_BINDER, "When uncrafting condensed materials, the binding material is not returned.");

    ConfigOptionBool EriottoMod_Enabled = new ConfigOptionBool("addons.EriottoMod", "Enabled", InteractionEriottoMod.ENABLED);
    ConfigOptionBool EriottoMod_GrassDropsSeeds = new ConfigOptionBool("addons.EriottoMod", "GrassDropsSeeds", InteractionEriottoMod.GRASS_DROPS_SEEDS, "Rice and Rush seeds can be gotten from breaking grass.");

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
        InteractionBWA.RADIUS = BWA_LureTreeRadius.init(configuration);
        InteractionBWA.MAXCHARGE = BWA_LureTreeTime.init(configuration);
        InteractionBWA.MAXFOOD = BWA_LureTreeMaxFood.init(configuration);
        InteractionBWA.FOODGLOWSTONE = BWA_LureTreeFoodGlowstone.init(configuration);
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
