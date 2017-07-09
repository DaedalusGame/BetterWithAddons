package betterwithaddons.config;

import betterwithaddons.interaction.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfiguration {
    Configuration configuration;
    ConfigOptionBool BWM_Enabled = new ConfigOptionBool("interaction.BetterWithMods", "Enabled", InteractionBWM.ENABLED);
    ConfigOptionBool BWM_MillClay = new ConfigOptionBool("interaction.BetterWithMods", "MillClay", InteractionBWM.MILL_CLAY, "Hardened clay can be milled into bricks, saving fuel and allowing dyed bricks.");
    ConfigOptionBool BWM_ChorusInCauldron = new ConfigOptionBool("interaction.BetterWithMods", "ChorusInCauldron", InteractionBWM.CHORUS_IN_CAULDRON, "Chorus fruit (and Midori) can only be popped in a cauldron.");
    ConfigOptionBool BWM_ButcherBlocks = new ConfigOptionBool("interaction.BetterWithMods", "ButcherBlocks", InteractionBWM.BUTCHER_BLOCKS, "Striking an enemy on chopping blocks will bloody them and bestow a short strenth buff.");
    ConfigOptionBool BWM_CauldronsExplode = new ConfigOptionBool("interaction.BetterWithMods", "CauldronsExplode", InteractionBWM.CAULDRONS_EXPLODE, "Cooking hellfire or any other explosive in a stoked cauldron or crucible will result in a violent explosion.");

    ConfigOptionBool Quark_Enabled = new ConfigOptionBool("interaction.Quark", "Enabled", InteractionQuark.ENABLED);
    ConfigOptionBool Quark_MidoriBlocksNeedChunks = new ConfigOptionBool("interaction.Quark", "Enabled", InteractionQuark.MIDORI_BLOCKS_NEED_CHUNKS, "Midori blocks require popped Midori chunks.");

    ConfigOptionBool BWA_StoneBricksNeedSmelting = new ConfigOptionBool("addons.BetterWithAddons", "StoneBricksNeedSmelting", InteractionBWA.STONEBRICKS_NEED_SMELTING, "Stonebricks need two extra steps in crafting.");
    ConfigOptionInteger BWA_LureTreeRadius = new ConfigOptionInteger("addons.BetterWithAddons", "LureTreeRadius", InteractionBWA.RADIUS, "Radius in which the tree can spawn mobs.");
    ConfigOptionInteger BWA_LureTreeTime = new ConfigOptionInteger("addons.BetterWithAddons", "LureTreeTime", InteractionBWA.MAXCHARGE, "Time it takes for the tree to do one spawning cycle.");
    ConfigOptionInteger BWA_LureTreeMaxFood = new ConfigOptionInteger("addons.BetterWithAddons", "LureTreeMaxFood", InteractionBWA.MAXFOOD, "How much food the tree can hold.");
    ConfigOptionBool BWA_ObviousStorms = new ConfigOptionBool("addons.BetterWithAddons", "ObviousStorms", InteractionBWA.OBVIOUS_STORMS, "Storms in dry biomes are much more obvious.");
    ConfigOptionBool BWA_ObviousSandStorms = new ConfigOptionBool("addons.BetterWithAddons", "ObviousSandStorms", InteractionBWA.OBVIOUS_SAND_STORMS, "Storms in sandy biomes are even more obvious.");
    ConfigOptionInteger BWA_ObviousDustParticles = new ConfigOptionInteger("addons.BetterWithAddons", "ObviousDustParticles", InteractionBWA.DUST_PARTICLES, "How many dust particles are kicked up every tick.");
    ConfigOptionInteger BWA_ObviousAirParticles = new ConfigOptionInteger("addons.BetterWithAddons", "ObviousAirParticles", InteractionBWA.AIR_PARTICLES, "How many wind particles are generated every tick.");
    ConfigOptionBool BWA_GatedAqueducts = new ConfigOptionBool("addons.BetterWithAddons", "GatedAqueducts", InteractionBWA.GATED_AQUEDUCTS, "Aqueducts require white stone to craft. This means you need to go to the end to transport water over long distances without power usage.");
    ConfigOptionInteger BWA_MaxAqueductLength = new ConfigOptionInteger("addons.BetterWithAddons", "MaxAqueductLength", InteractionBWA.AQUEDUCT_MAX_LENGTH, "How long aqueducts can be.");
    ConfigOptionStringList BWA_AqueductBiomes = new ConfigOptionStringList("addons.BetterWithAddons", "AqueductBiomes", InteractionBWA.AQUEDUCT_BIOME_STRINGS, "Aqueducts can only draw water from sources in specific biomes.");
    ConfigOptionBool BWA_AqueductBiomeWhitelist = new ConfigOptionBool("addons.BetterWithAddons", "AqueductBiomesIsWhitelist", InteractionBWA.AQUEDUCT_BIOMES_IS_WHITELIST, "Whether aqueduct biomes should be whitelisted or blacklisted.");

    ConfigOptionBool BWA_ArmorShardRender = new ConfigOptionBool("addons.BetterWithAddons", "ArmorShardRender", InteractionBWA.ARMOR_SHARD_RENDER, "Enables or disables the custom armor shard renderer, for when it causes crashes.");
    ConfigOptionDouble BWA_LegendariumMinDamage = new ConfigOptionDouble("addons.BetterWithAddons", "LegendariumDamageMin", InteractionBWA.LEGENDARIUM_MIN_DAMAGE, "How much durability the artifact you're turning in can have at max. (As a factor of max durability; 0.1 means 1/10 of max durability)");
    ConfigOptionInteger BWA_LegendariumDamagePad = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumDamagePad", InteractionBWA.LEGENDARIUM_DAMAGE_PAD, "How much durability more than the minimum the artifact can have to still be considered broken. (As a static value)");
    ConfigOptionInteger BWA_LegendariumPosterRange = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumPosterRange", InteractionBWA.LEGENDARIUM_POSTER_RANGE, "How far away Display Frames are recognized. (in blocks; as a cubic radius)");
    ConfigOptionInteger BWA_LegendariumMinQueueSize = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumMinQueueSize", InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE, "How many artifacts must be in the Hall of Legends to take one out.");
    ConfigOptionInteger BWA_LegendariumTurnInDelay = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumTurnInDelay", InteractionBWA.LEGENDARIUM_TURN_IN_DELAY, "How long until the next artifact can be turned in. (in ticks; 1000 ticks is one Minecraft hour)");

    ConfigOptionBool BWR_Enabled = new ConfigOptionBool("addons.BetterWithRenewables","Enabled",InteractionBWR.ENABLED);
    ConfigOptionBool BWR_RedstoneSynthesis = new ConfigOptionBool("addons.BetterWithRenewables","RedstoneSynthesis",InteractionBWR.REDSTONE_SYNTHESIS,"Allows redstone to be farbricated from concentrated hellfire and gold.");
    ConfigOptionBool BWR_RedstoneSynthesisEarly = new ConfigOptionBool("addons.BetterWithRenewables","RedstoneSynthesisEarly",InteractionBWR.REDSTONE_SYNTHESIS_EARLY,"Allows redstone to be synthesized earlier to create Hibachis.");
    ConfigOptionInteger BWR_RedstonePerSynthesis = new ConfigOptionInteger("addons.BetterWithRenewables","RedstonePerSynthesis",InteractionBWR.REDSTONE_PER_SYNTHESIS,"How much redstone is obtained per bar of concentrated hellfire.");
    ConfigOptionBool BWR_HellfireEarly = new ConfigOptionBool("addons.BetterWithRenewables","HellfireEarly",InteractionBWR.HELLFIRE_EARLY,"Allows hellfire dust to be created earlier in the tech tree.");
    ConfigOptionBool BWR_BoilingBushes = new ConfigOptionBool("addons.BetterWithRenewables","BoilingBushes",InteractionBWR.BOILING_BUSHES,"Allows dead bushes to be created from oak saplings.");
    ConfigOptionBool BWR_WeavingWebs = new ConfigOptionBool("addons.BetterWithRenewables","WeavingWebs",InteractionBWR.WEAVING_WEBS,"Allows webs to be created from string and slimeballs.");
    ConfigOptionBool BWR_LapisFromWool = new ConfigOptionBool("addons.BetterWithRenewables","LapisFromWool",InteractionBWR.LAPIS_FROM_WOOL,"Allows lapis to be created from blue wool and clay.");
    ConfigOptionBool BWR_DiamondSynthesis = new ConfigOptionBool("addons.BetterWithRenewables","DiamondSynthesis",InteractionBWR.DIAMOND_SYNTHESIS,"Allows diamonds to be fabricated from ghast tears.");
    ConfigOptionBool BWR_DiamondRecovery = new ConfigOptionBool("addons.BetterWithRenewables","DiamondRecovery",InteractionBWR.DIAMOND_RECOVERY,"Allows diamond ingots to be taken apart into diamonds and iron ingots using strong alkaline.");
    ConfigOptionBool BWR_GoldGrinding = new ConfigOptionBool("addons.BetterWithRenewables","GoldGrinding",InteractionBWR.GOLD_GRINDING,"Allows only tools and armor made from gold to be milled into nuggets at a lower efficiency than when melted in a Crucible.");
    ConfigOptionInteger BWR_GoldPerIngot = new ConfigOptionInteger("addons.BetterWithRenewables","GoldPerIngot",InteractionBWR.GOLD_PER_INGOT,"Gold nuggets returned when grinding gold tools or armor in a millstone.");
    ConfigOptionBool BWR_NetherrackSynthesis = new ConfigOptionBool("addons.BetterWithRenewables","NetherrackSynthesis",InteractionBWR.NETHERRACK_SYNTHESIS,"Allows netherrack to be farbricated from a usable medium, a hellborn plant and some residents from the nether.");
    ConfigOptionBool BWR_SoulsandInfusion = new ConfigOptionBool("addons.BetterWithRenewables","SoulsandInfusion",InteractionBWR.SOULSAND_INFUSION,"Allows netherrack to be fabricated from dung and experience.");
    ConfigOptionBool BWR_BlazeGolems = new ConfigOptionBool("addons.BetterWithRenewables","BlazeGolems",InteractionBWR.BLAZE_GOLEMS,"Allows blazes to be created from a golem-like shape with appropriate blocks.");

    ConfigOptionBool CondensedOutputs_Enabled = new ConfigOptionBool("addons.CondensedOutputs", "Enabled", InteractionCondensedOutputs.ENABLED);
    ConfigOptionBool CondensedOutputs_LoseBinder = new ConfigOptionBool("addons.CondensedOutputs", "LoseBinder", InteractionCondensedOutputs.LOSE_BINDER, "When uncrafting condensed materials, the binding material is not returned.");

    ConfigOptionBool EriottoMod_Enabled = new ConfigOptionBool("addons.EriottoMod", "Enabled", InteractionEriottoMod.ENABLED);
    ConfigOptionBool EriottoMod_GrassDropsSeeds = new ConfigOptionBool("addons.EriottoMod", "GrassDropsSeeds", InteractionEriottoMod.GRASS_DROPS_SEEDS, "Rice and Rush seeds can be gotten from breaking grass.");

    ConfigOptionBool DecoAddon_Enabled = new ConfigOptionBool("addons.DecoAddon", "Enabled", InteractionDecoAddon.ENABLED);
    ConfigOptionBool DecoAddon_WoodColoring = new ConfigOptionBool("addons.DecoAddon", "WoodColoring", InteractionDecoAddon.WOOD_COLORING, "Vanilla planks can be bleached or stained.");
    ConfigOptionBool DecoAddon_AlternateWroughtBars = new ConfigOptionBool("addons.DecoAddon", "AlternateWroughtBars", InteractionDecoAddon.ALTERNATE_WROUGHT_BARS, "Wrought bars are made at a ratio of 1 iron ingot per bar instead of 1/2 an iron ingot per bar.");
    ConfigOptionBool DecoAddon_ChiselBricksInAnvil = new ConfigOptionBool("addons.DecoAddon", "ChiselBricksInAnvil", InteractionDecoAddon.CHISEL_BRICKS_IN_ANVIL, "Chiseled Stone Bricks can only be crafted on a Steel Anvil.");
    ConfigOptionBool DecoAddon_GlassPaneRebalance = new ConfigOptionBool("addons.DecoAddon", "GlassPaneRebalance", InteractionDecoAddon.GLASS_PANE_REBALANCE, "Glass becomes neatly divisable into two glass panes per block.");
    ConfigOptionBool DecoAddon_GlassFurnace = new ConfigOptionBool("addons.DecoAddon", "GlassFurnace", InteractionDecoAddon.GLASS_FURNACE, "Glass chunks can be smelted in a furnace.");
    ConfigOptionBool DecoAddon_CheaperBottles = new ConfigOptionBool("addons.DecoAddon", "CheaperBottles", InteractionDecoAddon.CHEAPER_BOTTLES, "Glass bottles are made from half as much glass as normal.");
    ConfigOptionBool DecoAddon_RecycleBottles = new ConfigOptionBool("addons.DecoAddon", "RecycleBottles", InteractionDecoAddon.RECYCLE_BOTTLES, "Glass bottles can melted into chunks in a crucible. This allows you to make glass from a witch farm.");

    ConfigOptionBool BTWTweak_Enabled = new ConfigOptionBool("addons.BTWTweak", "Enabled", InteractionBTWTweak.ENABLED);
    //ConfigOptionBool BTWTweak_KilnDoubling = new ConfigOptionBool("addons.BTWTweak", "KilnDoubling", InteractionBTWTweak.KILN_DOUBLING, "Ores can be smelted into double the amount of output in a kiln.");
    ConfigOptionBool BTWTweak_SoftWoods = new ConfigOptionBool("addons.BTWTweak", "SoftWoods", InteractionBTWTweak.SOFT_WOODS, "Spruce, Jungle and Dark Oak logs are much easier to break.");
    ConfigOptionBool BTWTweak_HardStumps = new ConfigOptionBool("addons.BTWTweak", "HardStumps", InteractionBTWTweak.HARD_STUMPS, "Makes treefarms great ag- I mean, makes logs with dirt underneath them much harder to break to encourage leaving awesome stumps.");
    ConfigOptionDouble BTWTweak_HardStumpModifier = new ConfigOptionDouble("addons.BTWTweak", "HardStumpsModifier", InteractionBTWTweak.HARD_STUMPS_MODIFIER, "Speed modifier for harvesting logs. 0.5 is half speed.");
    ConfigOptionBool BTWTweak_SawRecycling = new ConfigOptionBool("addons.BTWTweak", "SawRecycling", InteractionBTWTweak.SAW_RECYCLING, "Many wooden blocks can be recycled by putting them infront of a saw, at a bit of a loss.");
    ConfigOptionBool BTWTweak_EggIncubation = new ConfigOptionBool("addons.BTWTweak", "EggIncubation", InteractionBTWTweak.EGG_INCUBATION, "Allows eggs to be incubated into chicken by placing them on a Block of Padding with a lit Light Block above.");
    ConfigOptionBool BTWTweak_SlipperyWhenWet = new ConfigOptionBool("addons.BTWTweak", "SlipperyWhenWet", InteractionBTWTweak.SLIPPERY_WHEN_WET, "Water running over blocks of soap will make them slippery.");


    public void preInit(FMLPreInitializationEvent event)
    {
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        configuration.load();

        configuration.getCategory("interaction").setComment("Enable or disable mod interactions.");

        InteractionBWM.ENABLED = BWM_Enabled.init(configuration);
        InteractionBWM.MILL_CLAY = BWM_MillClay.init(configuration);
        InteractionBWM.CHORUS_IN_CAULDRON = BWM_ChorusInCauldron.init(configuration);
        InteractionBWM.BUTCHER_BLOCKS = BWM_ButcherBlocks.init(configuration);
        InteractionBWM.CAULDRONS_EXPLODE = BWM_CauldronsExplode.init(configuration);
        InteractionQuark.ENABLED = Quark_Enabled.init(configuration);
        InteractionQuark.MIDORI_BLOCKS_NEED_CHUNKS = Quark_MidoriBlocksNeedChunks.init(configuration);

        configuration.getCategory("addons").setComment("Configure individual addons.");

        InteractionBWA.STONEBRICKS_NEED_SMELTING = BWA_StoneBricksNeedSmelting.init(configuration);
        InteractionBWA.RADIUS = BWA_LureTreeRadius.init(configuration);
        InteractionBWA.MAXCHARGE = BWA_LureTreeTime.init(configuration);
        InteractionBWA.MAXFOOD = BWA_LureTreeMaxFood.init(configuration);
        InteractionBWA.OBVIOUS_STORMS = BWA_ObviousStorms.init(configuration);
        InteractionBWA.OBVIOUS_SAND_STORMS = BWA_ObviousSandStorms.init(configuration);
        InteractionBWA.DUST_PARTICLES = BWA_ObviousDustParticles.init(configuration);
        InteractionBWA.AIR_PARTICLES = BWA_ObviousAirParticles.init(configuration);
        InteractionBWA.GATED_AQUEDUCTS = BWA_GatedAqueducts.init(configuration);
        InteractionBWA.AQUEDUCT_MAX_LENGTH = BWA_MaxAqueductLength.init(configuration);
        InteractionBWA.AQUEDUCT_BIOME_STRINGS = BWA_AqueductBiomes.init(configuration);
        InteractionBWA.AQUEDUCT_BIOMES_IS_WHITELIST = BWA_AqueductBiomeWhitelist.init(configuration);
        InteractionBWA.ARMOR_SHARD_RENDER = BWA_ArmorShardRender.init(configuration);
        InteractionBWA.LEGENDARIUM_MIN_DAMAGE = BWA_LegendariumMinDamage.init(configuration);
        InteractionBWA.LEGENDARIUM_DAMAGE_PAD = BWA_LegendariumDamagePad.init(configuration);
        InteractionBWA.LEGENDARIUM_POSTER_RANGE = BWA_LegendariumPosterRange.init(configuration);
        InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE = BWA_LegendariumMinQueueSize.init(configuration);
        InteractionBWA.LEGENDARIUM_TURN_IN_DELAY = BWA_LegendariumTurnInDelay.init(configuration);

        InteractionEriottoMod.ENABLED = EriottoMod_Enabled.init(configuration);
        InteractionEriottoMod.GRASS_DROPS_SEEDS = EriottoMod_GrassDropsSeeds.init(configuration);

        InteractionDecoAddon.ENABLED = DecoAddon_Enabled.init(configuration);
        InteractionDecoAddon.WOOD_COLORING = DecoAddon_WoodColoring.init(configuration);
        InteractionDecoAddon.ALTERNATE_WROUGHT_BARS = DecoAddon_AlternateWroughtBars.init(configuration);
        InteractionDecoAddon.CHISEL_BRICKS_IN_ANVIL = DecoAddon_ChiselBricksInAnvil.init(configuration);
        InteractionDecoAddon.GLASS_PANE_REBALANCE = DecoAddon_GlassPaneRebalance.init(configuration);
        InteractionDecoAddon.GLASS_FURNACE = DecoAddon_GlassFurnace.init(configuration);
        InteractionDecoAddon.CHEAPER_BOTTLES = DecoAddon_CheaperBottles.init(configuration);
        InteractionDecoAddon.RECYCLE_BOTTLES = DecoAddon_RecycleBottles.init(configuration);

        InteractionCondensedOutputs.ENABLED = CondensedOutputs_Enabled.init(configuration);
        InteractionCondensedOutputs.LOSE_BINDER = CondensedOutputs_LoseBinder.init(configuration);

        InteractionBTWTweak.ENABLED = BTWTweak_Enabled.init(configuration);
        //InteractionBTWTweak.KILN_DOUBLING = BTWTweak_KilnDoubling.init(configuration);
        InteractionBTWTweak.SOFT_WOODS = BTWTweak_SoftWoods.init(configuration);
        InteractionBTWTweak.HARD_STUMPS = BTWTweak_HardStumps.init(configuration);
        InteractionBTWTweak.HARD_STUMPS_MODIFIER = BTWTweak_HardStumpModifier.init(configuration);
        InteractionBTWTweak.SAW_RECYCLING = BTWTweak_SawRecycling.init(configuration);
        InteractionBTWTweak.EGG_INCUBATION = BTWTweak_EggIncubation.init(configuration);
        InteractionBTWTweak.SLIPPERY_WHEN_WET = BTWTweak_SlipperyWhenWet.init(configuration);

        InteractionBWR.ENABLED = BWR_Enabled.init(configuration);
        InteractionBWR.REDSTONE_SYNTHESIS = BWR_RedstoneSynthesis.init(configuration);
        InteractionBWR.REDSTONE_SYNTHESIS_EARLY = BWR_RedstoneSynthesisEarly.init(configuration);
        InteractionBWR.REDSTONE_PER_SYNTHESIS = BWR_RedstonePerSynthesis.init(configuration);
        InteractionBWR.HELLFIRE_EARLY = BWR_HellfireEarly.init(configuration);
        InteractionBWR.BOILING_BUSHES = BWR_BoilingBushes.init(configuration);
        InteractionBWR.WEAVING_WEBS = BWR_WeavingWebs.init(configuration);
        InteractionBWR.LAPIS_FROM_WOOL = BWR_LapisFromWool.init(configuration);
        InteractionBWR.DIAMOND_SYNTHESIS = BWR_DiamondSynthesis.init(configuration);
        InteractionBWR.DIAMOND_RECOVERY = BWR_DiamondRecovery.init(configuration);
        InteractionBWR.GOLD_GRINDING = BWR_GoldGrinding.init(configuration);
        InteractionBWR.GOLD_PER_INGOT = BWR_GoldPerIngot.init(configuration);
        InteractionBWR.NETHERRACK_SYNTHESIS = BWR_NetherrackSynthesis.init(configuration);
        InteractionBWR.SOULSAND_INFUSION = BWR_SoulsandInfusion.init(configuration);
        InteractionBWR.BLAZE_GOLEMS = BWR_BlazeGolems.init(configuration);

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }

    public void init(FMLInitializationEvent event)
    {

    }
}
