package betterwithaddons.config;

import betterwithaddons.interaction.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfiguration {
    Configuration configuration;
    ConfigOptionBool BWM_Enabled = new ConfigOptionBool("interaction.BetterWithMods", "Enabled", InteractionBWM.ENABLED, "Whether the Better With Mods compat module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
    ConfigOptionBool BWM_MillClay = new ConfigOptionBool("interaction.BetterWithMods", "MillClay", InteractionBWM.MILL_CLAY, "Hardened clay can be milled into bricks, saving fuel and allowing dyed bricks.");
    ConfigOptionBool BWM_ChorusInCauldron = new ConfigOptionBool("interaction.BetterWithMods", "ChorusInCauldron", InteractionBWM.CHORUS_IN_CAULDRON, "Chorus fruit (and Midori) can only be popped in a cauldron.");
    ConfigOptionBool BWM_ButcherBlocks = new ConfigOptionBool("interaction.BetterWithMods", "ButcherBlocks", InteractionBWM.BUTCHER_BLOCKS, "Striking an enemy on chopping blocks will bloody them and bestow a short strenth buff.");
    ConfigOptionBool BWM_CauldronsExplode = new ConfigOptionBool("interaction.BetterWithMods", "CauldronsExplode", InteractionBWM.CAULDRONS_EXPLODE, "Cooking hellfire or any other explosive in a stoked cauldron or crucible will result in a violent explosion.");
    ConfigOptionBool BWM_HardcoreShearing = new ConfigOptionBool("interaction.BetterWithMods", "HardcoreShearing", InteractionBWM.HARDCORE_SHEARING, "Sheep will only be sheared into wool items, which must be crafted into wool blocks. This does not work with machines that use onSheared, but should work with Fake Players.");
    ConfigOptionBool BWM_HardcorePacking = new ConfigOptionBool("interaction.BetterWithMods", "HardcorePacking", InteractionBWM.HARDCORE_PACKING, "Items can be compressed in world with a piston pushing them into an enclosed space.");
    ConfigOptionInteger BWM_WoolMultiplier = new ConfigOptionInteger("interaction.BetterWithMods", "WoolMultiplier", InteractionBWM.WOOL_MULTIPLIER, "Adjusts how much wool a sheep drops if Hardcore Shearing is enabled.");
    ConfigOptionBool BWM_DyeInCauldron = new ConfigOptionBool("interaction.BetterWithMods", "DyeInCauldron", InteractionBWM.DYE_IN_CAULDRON, "Wool can be dyed in batches of 8 in a cauldron and bleached with potash.");
    //ConfigOptionBool BWM_HiddenEnchantments = new ConfigOptionBool("addons.BetterWithMods", "HiddenEnchantments", InteractionBWM.HIDDEN_ENCHANTS, "Enchantments on enchanted books are hidden. (They still function as usual)");
    ConfigOptionStringList BWM_ShearWhitelist = new ConfigOptionStringList("interaction.BetterWithMods", "ShearWhitelist", InteractionBWM.SHEARS_WHITELIST, "Extra items that are functionally shears but don't extend ItemShears.");

    ConfigOptionBool Quark_Enabled = new ConfigOptionBool("interaction.Quark", "Enabled", InteractionQuark.ENABLED, "Whether the Quark compat module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
    ConfigOptionBool Quark_MidoriBlocksNeedChunks = new ConfigOptionBool("interaction.Quark", "MidoriBlocksNeedChunks", InteractionQuark.MIDORI_BLOCKS_NEED_CHUNKS, "Midori blocks require popped Midori chunks.");

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
    ConfigOptionBool BWA_ConvenientIronTools = new ConfigOptionBool("addons.BetterWithAddons", "ConvenientIronTools", InteractionBWA.CONVENIENT_TOOLS_PRE_END, "Convenient tools can be made from iron, gold and diamond pre-soulsteel.");

    ConfigOptionBool BWA_RottenFood = new ConfigOptionBool("addons.BetterWithAddons", "RottenFood", InteractionBWA.ROTTEN_FOOD, "Whether food will rot after a certain number of days has passed.");
    ConfigOptionBool BWA_RottenFoodCombining = new ConfigOptionBool("addons.BetterWithAddons", "RottenFoodCombining", InteractionBWA.ROTTEN_FOOD_COMBINING, "Whether food can be combined in the crafting grid to stack.");
    ConfigOptionStringList BWA_RottenFoodBlacklist = new ConfigOptionStringList("addons.BetterWithAddons", "RottenFoodBlacklist", InteractionBWA.ROTTEN_FOOD_BLACKLIST, "These foods are excluded from rotting.");
    ConfigOptionInteger BWA_RottenMeatTime = new ConfigOptionInteger("addons.BetterWithAddons", "RottenMeatTime", (int)InteractionBWA.MEAT_ROT_TIME, "How long meat takes to rot. (In ticks)");
    ConfigOptionInteger BWA_RottenFishTime = new ConfigOptionInteger("addons.BetterWithAddons", "RottenFishTime", (int)InteractionBWA.FISH_ROT_TIME, "How long fish takes to rot. (In ticks)");
    ConfigOptionInteger BWA_RottenFruitTime = new ConfigOptionInteger("addons.BetterWithAddons", "RottenFruitTime", (int)InteractionBWA.FRUIT_ROT_TIME, "How long fruit takes to rot. (In ticks)");
    ConfigOptionInteger BWA_RottenMiscTime = new ConfigOptionInteger("addons.BetterWithAddons", "RottenMiscTime", (int)InteractionBWA.MISC_ROT_TIME, "How long misc food takes to rot. (In ticks)");

    ConfigOptionBool BWA_ArmorShardRender = new ConfigOptionBool("addons.BetterWithAddons", "ArmorShardRender", InteractionBWA.ARMOR_SHARD_RENDER, "Enables or disables the custom armor shard renderer, for when it causes crashes.");
    ConfigOptionDouble BWA_LegendariumMinDamage = new ConfigOptionDouble("addons.BetterWithAddons", "LegendariumDamageMin", InteractionBWA.LEGENDARIUM_MIN_DAMAGE, "How much durability the artifact you're turning in can have at max. (As a factor of max durability; 0.1 means 1/10 of max durability)");
    ConfigOptionInteger BWA_LegendariumDamagePad = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumDamagePad", InteractionBWA.LEGENDARIUM_DAMAGE_PAD, "How much durability more than the minimum the artifact can have to still be considered broken. (As a static value)");
    ConfigOptionInteger BWA_LegendariumPosterRange = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumPosterRange", InteractionBWA.LEGENDARIUM_POSTER_RANGE, "How far away Display Frames are recognized. (in blocks; as a cubic radius)");
    ConfigOptionInteger BWA_LegendariumMinQueueSize = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumMinQueueSize", InteractionBWA.LEGENDARIUM_MIN_QUEUE_SIZE, "How many artifacts must be in the Hall of Legends to take one out.");
    ConfigOptionInteger BWA_LegendariumTurnInDelay = new ConfigOptionInteger("addons.BetterWithAddons", "LegendariumTurnInDelay", InteractionBWA.LEGENDARIUM_TURN_IN_DELAY, "How long until the next artifact can be turned in. (in ticks; 1000 ticks is one Minecraft hour)");

    ConfigOptionBool BWR_Enabled = new ConfigOptionBool("addons.BetterWithRenewables","Enabled",InteractionBWR.ENABLED,"Whether the Better With Renewables module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
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
    ConfigOptionBool BWR_BlazeBreeding = new ConfigOptionBool("addons.BetterWithRenewables","BlazeBreeding",InteractionBWR.BLAZE_BREEDING,"Allows blazes to replicate in fire when fed an appropriate item.");
    ConfigOptionBool BWR_PlantBreeding = new ConfigOptionBool("addons.BetterWithRenewables","PlantBreeding",InteractionBWR.CROSSBREED_PLANTS,"Allows plants to be crossbreed from other plants.");
    ConfigOptionBool BWR_AnimalBreeding = new ConfigOptionBool("addons.BetterWithRenewables","AnimalBreeding",InteractionBWR.CROSSBREED_ANIMALS,"Allows animals to be crossbreed from other animals. Disgusting.");
    ConfigOptionBool BWR_QuartzSynthesis = new ConfigOptionBool("addons.BetterWithRenewables","QuartzSynthesis",InteractionBWR.QUARTZ_GROWING,"Allows quartz to be grown from silica in appropriate conditions.");
    ConfigOptionBool BWR_DungToDirt = new ConfigOptionBool("addons.BetterWithRenewables","DungToDirt",InteractionBWR.DUNG_TO_DIRT,"Allows dung to be turned into dirt by rinsing acids out.");
    ConfigOptionBool BWR_SandToClay = new ConfigOptionBool("addons.BetterWithRenewables","SandToClay",InteractionBWR.SAND_TO_CLAY,"Allows sand to be turned into clay by adding acidic substances.");
    ConfigOptionBool BWR_MeltHellfire = new ConfigOptionBool("addons.BetterWithRenewables","MeltHellfire",InteractionBWR.MELT_HELLFIRE,"Allows Blocks of Hellfire to be melted into lava by proximity to it.");
    ConfigOptionBool BWR_RedstoneBoiling = new ConfigOptionBool("addons.BetterWithRenewables","BoilRedstone",InteractionBWR.REDSTONE_BOILING,"Allows redstone to be 'boiled' into glowstone by exposure to focused sunlight.");

    ConfigOptionBool CondensedOutputs_Enabled = new ConfigOptionBool("addons.CondensedOutputs", "Enabled", InteractionCondensedOutputs.ENABLED,"Whether the Condensed Outputs module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
    ConfigOptionBool CondensedOutputs_LoseBinder = new ConfigOptionBool("addons.CondensedOutputs", "LoseBinder", InteractionCondensedOutputs.LOSE_BINDER, "When uncrafting condensed materials, the binding material is not returned.");
    ConfigOptionInteger CondensedOutputs_SpindleTime = new ConfigOptionInteger("addons.CondensedOutputs", "SpindleTime", InteractionCondensedOutputs.SPINUP_TIME, "The amount of time in ticks it takes for the spindle to spin up once.");

    ConfigOptionBool Wheat_Enabled = new ConfigOptionBool("addons.BetterWithWheat", "Enabled", InteractionWheat.ENABLED, "Whether the Better With Wheat module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
    ConfigOptionBool Wheat_MillGrain = new ConfigOptionBool("addons.BetterWithWheat", "MillGrain", InteractionWheat.MILL_GRAIN, "Instead of milling wheat into flour, wheat seeds must be milled instead.");
    ConfigOptionBool Wheat_ReplaceWheatDrops = new ConfigOptionBool("addons.BetterWithWheat", "ReplaceWheatDrops", InteractionWheat.REPLACE_WHEAT_DROPS, "Wheat, when harvested, will drop wheat and hay instead.");
    ConfigOptionBool Wheat_ChangeHayBales = new ConfigOptionBool("addons.BetterWithWheat", "ChangeHayBales", InteractionWheat.CHANGE_HAY_BALES, "Haybales require hay instead of wheat.");
    ConfigOptionBool Wheat_ThreshWheat = new ConfigOptionBool("addons.BetterWithWheat", "ThreshWheat", InteractionWheat.THRESH_WHEAT, "Wheat can be threshed into seeds using shovels.");
    ConfigOptionBool Wheat_ThreshWheatMill = new ConfigOptionBool("addons.BetterWithWheat", "ThreshWheatMill", InteractionWheat.THRESH_WHEAT_MILL, "Wheat can be threshed into seeds using a millstone.");
    ConfigOptionBool Wheat_DigUpCrops = new ConfigOptionBool("addons.BetterWithWheat", "DigUpCrops", InteractionWheat.DIG_UP_CROPS, "Carrots and Potatoes cannot be instantly harvested, and instead have a breaktime and a preferred tool (hoe)");
    ConfigOptionBool Wheat_ChangeTextures = new ConfigOptionBool("addons.BetterWithWheat", "ChangeTextures", InteractionWheat.TEXTURE_CHANGES, "Changes textures of Wheat and Wheat Seeds.");

    ConfigOptionBool EriottoMod_Enabled = new ConfigOptionBool("addons.EriottoMod", "Enabled", InteractionEriottoMod.ENABLED, "Whether the Japanese Culture module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
    //ConfigOptionBool EriottoMod_GrassDropsSeeds = new ConfigOptionBool("addons.EriottoMod", "GrassDropsSeeds", InteractionEriottoMod.GRASS_DROPS_SEEDS, "Rice and Rush seeds can be gotten from breaking grass.");
    ConfigOptionBool EriottoMod_AlternateInfuserRecipe = new ConfigOptionBool("addons.EriottoMod", "AlternateInfuserRecipe", InteractionEriottoMod.ALTERNATE_INFUSER_RECIPE, "Hardcore Structures pushes the Enchanting Table behind some exploration. This enables an alternate recipe if you want to start japanese culture before finding a Desert Temple.");
    ConfigOptionBool EriottoMod_InfuserRepairs = new ConfigOptionBool("addons.EriottoMod", "InfuserRepairs", InteractionEriottoMod.INFUSER_REPAIRS, "Infusers can repair japanese weapons and armors.");
    ConfigOptionInteger EriottoMod_SpiritsPerBottle = new ConfigOptionInteger("addons.EriottoMod", "SpiritsPerBottle", InteractionEriottoMod.SPIRIT_PER_BOTTLE, "How much spirit is contained in one bottle.");
    ConfigOptionInteger EriottoMod_MaxSpirits = new ConfigOptionInteger("addons.EriottoMod", "MaxSpirits", InteractionEriottoMod.MAX_SPIRITS, "Maximum amount of spirit to be stored in Infused Soul Sand.");
    ConfigOptionBool EriottoMod_RandomJapaneseMobs = new ConfigOptionBool("addons.EriottoMod", "RandomJapaneseMobs", InteractionEriottoMod.JAPANESE_RANDOM_SPAWN, "Karate Zombies infused with ancestral spirit spawn randomly.");
    ConfigOptionInteger EriottoMod_RandomJapaneseWeight = new ConfigOptionInteger("addons.EriottoMod", "RandomJapaneseMobsWeight", InteractionEriottoMod.JAPANESE_RANDOM_SPAWN_WEIGHT, "Weight for a karate zombie to spawn.");

    ConfigOptionBool DecoAddon_Enabled = new ConfigOptionBool("addons.DecoAddon", "Enabled", InteractionDecoAddon.ENABLED, "Whether the Deco Addon module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
    ConfigOptionBool DecoAddon_WoodColoring = new ConfigOptionBool("addons.DecoAddon", "WoodColoring", InteractionDecoAddon.WOOD_COLORING, "Vanilla planks can be bleached or stained.");
    ConfigOptionBool DecoAddon_AlternateWroughtBars = new ConfigOptionBool("addons.DecoAddon", "AlternateWroughtBars", InteractionDecoAddon.ALTERNATE_WROUGHT_BARS, "Wrought bars are made at a ratio of 1 iron ingot per bar instead of 1/2 an iron ingot per bar.");
    ConfigOptionBool DecoAddon_ChiselBricksInAnvil = new ConfigOptionBool("addons.DecoAddon", "ChiselBricksInAnvil", InteractionDecoAddon.CHISEL_BRICKS_IN_ANVIL, "Chiseled Stone Bricks can only be crafted on a Steel Anvil.");
    ConfigOptionBool DecoAddon_GlassPaneRebalance = new ConfigOptionBool("addons.DecoAddon", "GlassPaneRebalance", InteractionDecoAddon.GLASS_PANE_REBALANCE, "Glass becomes neatly divisable into two glass panes per block.");
    ConfigOptionBool DecoAddon_GlassFurnace = new ConfigOptionBool("addons.DecoAddon", "GlassFurnace", InteractionDecoAddon.GLASS_FURNACE, "Glass chunks can be smelted in a furnace.");
    ConfigOptionBool DecoAddon_CheaperBottles = new ConfigOptionBool("addons.DecoAddon", "CheaperBottles", InteractionDecoAddon.CHEAPER_BOTTLES, "Glass bottles are made from half as much glass as normal.");
    ConfigOptionBool DecoAddon_RecycleBottles = new ConfigOptionBool("addons.DecoAddon", "RecycleBottles", InteractionDecoAddon.RECYCLE_BOTTLES, "Glass bottles can melted into chunks in a crucible. This allows you to make glass from a witch farm.");

    ConfigOptionBool BTWTweak_Enabled = new ConfigOptionBool("addons.BTWTweak", "Enabled", InteractionBTWTweak.ENABLED, "Whether the BTWTweak module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.");
    ConfigOptionBool BTWTweak_SawRecycling = new ConfigOptionBool("addons.BTWTweak", "SawRecycling", InteractionBTWTweak.SAW_RECYCLING, "Many wooden blocks can be recycled by putting them infront of a saw, at a bit of a loss.");
    ConfigOptionBool BTWTweak_EggIncubation = new ConfigOptionBool("addons.BTWTweak", "EggIncubation", InteractionBTWTweak.EGG_INCUBATION, "Allows eggs to be incubated into chicken by placing them on a Block of Padding with a lit Light Block above.");
    ConfigOptionBool BTWTweak_SlipperyWhenWet = new ConfigOptionBool("addons.BTWTweak", "SlipperyWhenWet", InteractionBTWTweak.SLIPPERY_WHEN_WET, "Water running over blocks of soap will make them slippery.");
    ConfigOptionBool BTWTweak_AshFertilizer = new ConfigOptionBool("addons.BTWTweak", "AshFertilizer", InteractionBTWTweak.ASH_FERTILIZER, "Potash is a valid fertilizer.");
    ConfigOptionBool BTWTweak_LogsSmeltToAsh = new ConfigOptionBool("addons.BTWTweak", "LogsSmeltToAsh", InteractionBTWTweak.LOGS_SMELT_TO_ASH, "Logs burn into ash in a furnace. This only works if they wouldn't burn into anything else.");
    ConfigOptionBool BTWTweak_ReplaceWritableBookRecipe = new ConfigOptionBool("addons.BTWTweak", "ReplaceWritableBookRecipe", InteractionBTWTweak.REPLACE_WRITABLE_BOOK_RECIPE, "Changes writable books to require the Ink and Quill item.");
    ConfigOptionBool BTWTweak_WoolRecycling = new ConfigOptionBool("addons.BTWTweak", "WoolRecycling", InteractionBTWTweak.WOOL_RECYCLING, "Wool can be rendered back into it's components. You might want to disable this if you use mods that violate Hardcore Shearing.");


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
        InteractionBWM.HARDCORE_SHEARING = BWM_HardcoreShearing.init(configuration);
        InteractionBWM.WOOL_MULTIPLIER = BWM_WoolMultiplier.init(configuration);
        InteractionBWM.DYE_IN_CAULDRON = BWM_DyeInCauldron.init(configuration);
        InteractionBWM.HARDCORE_PACKING = BWM_HardcorePacking.init(configuration);
        InteractionBWM.SHEARS_WHITELIST = BWM_ShearWhitelist.init(configuration);

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
        InteractionBWA.CONVENIENT_TOOLS_PRE_END = BWA_ConvenientIronTools.init(configuration);
        InteractionBWA.ROTTEN_FOOD = BWA_RottenFood.init(configuration);
        InteractionBWA.ROTTEN_FOOD_BLACKLIST = BWA_RottenFoodBlacklist.init(configuration);
        InteractionBWA.ROTTEN_FOOD_COMBINING = BWA_RottenFoodCombining.init(configuration);
        InteractionBWA.MEAT_ROT_TIME = BWA_RottenMeatTime.init(configuration);
        InteractionBWA.FISH_ROT_TIME = BWA_RottenFishTime.init(configuration);
        InteractionBWA.FRUIT_ROT_TIME = BWA_RottenFruitTime.init(configuration);
        InteractionBWA.MISC_ROT_TIME = BWA_RottenMiscTime.init(configuration);

        InteractionEriottoMod.ENABLED = EriottoMod_Enabled.init(configuration);
        //InteractionEriottoMod.GRASS_DROPS_SEEDS = EriottoMod_GrassDropsSeeds.init(configuration);
        InteractionEriottoMod.ALTERNATE_INFUSER_RECIPE = EriottoMod_AlternateInfuserRecipe.init(configuration);
        InteractionEriottoMod.INFUSER_REPAIRS = EriottoMod_InfuserRepairs.init(configuration);
        InteractionEriottoMod.JAPANESE_RANDOM_SPAWN = EriottoMod_RandomJapaneseMobs.init(configuration);
        InteractionEriottoMod.JAPANESE_RANDOM_SPAWN_WEIGHT = EriottoMod_RandomJapaneseWeight.init(configuration);
        InteractionEriottoMod.MAX_SPIRITS = EriottoMod_MaxSpirits.init(configuration);
        InteractionEriottoMod.SPIRIT_PER_BOTTLE = EriottoMod_SpiritsPerBottle.init(configuration);

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
        InteractionCondensedOutputs.SPINUP_TIME = CondensedOutputs_SpindleTime.init(configuration);

        InteractionBTWTweak.ENABLED = BTWTweak_Enabled.init(configuration);
        //InteractionBTWTweak.KILN_DOUBLING = BTWTweak_KilnDoubling.init(configuration);
        InteractionBTWTweak.SAW_RECYCLING = BTWTweak_SawRecycling.init(configuration);
        InteractionBTWTweak.EGG_INCUBATION = BTWTweak_EggIncubation.init(configuration);
        InteractionBTWTweak.SLIPPERY_WHEN_WET = BTWTweak_SlipperyWhenWet.init(configuration);
        InteractionBTWTweak.ASH_FERTILIZER = BTWTweak_AshFertilizer.init(configuration);
        InteractionBTWTweak.LOGS_SMELT_TO_ASH = BTWTweak_LogsSmeltToAsh.init(configuration);
        InteractionBTWTweak.REPLACE_WRITABLE_BOOK_RECIPE = BTWTweak_ReplaceWritableBookRecipe.init(configuration);
        InteractionBTWTweak.WOOL_RECYCLING = BTWTweak_WoolRecycling.init(configuration);

        InteractionWheat.ENABLED = Wheat_Enabled.init(configuration);
        InteractionWheat.TEXTURE_CHANGES = Wheat_ChangeTextures.init(configuration);
        InteractionWheat.MILL_GRAIN = Wheat_MillGrain.init(configuration);
        InteractionWheat.THRESH_WHEAT = Wheat_ThreshWheat.init(configuration);
        InteractionWheat.THRESH_WHEAT_MILL = Wheat_ThreshWheatMill.init(configuration);
        InteractionWheat.CHANGE_HAY_BALES = Wheat_ChangeHayBales.init(configuration);
        InteractionWheat.REPLACE_WHEAT_DROPS = Wheat_ReplaceWheatDrops.init(configuration);
        InteractionWheat.DIG_UP_CROPS = Wheat_DigUpCrops.init(configuration);

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
        InteractionBWR.BLAZE_BREEDING = BWR_BlazeBreeding.init(configuration);
        InteractionBWR.DUNG_TO_DIRT = BWR_DungToDirt.init(configuration);
        InteractionBWR.SAND_TO_CLAY = BWR_SandToClay.init(configuration);
        InteractionBWR.QUARTZ_GROWING = BWR_QuartzSynthesis.init(configuration);
        InteractionBWR.CROSSBREED_PLANTS = BWR_PlantBreeding.init(configuration);
        InteractionBWR.CROSSBREED_ANIMALS = BWR_AnimalBreeding.init(configuration);
        InteractionBWR.MELT_HELLFIRE = BWR_MeltHellfire.init(configuration);
        InteractionBWR.REDSTONE_BOILING = BWR_RedstoneBoiling.init(configuration);

        if (configuration.hasChanged())
        {
            configuration.save();
        }
    }

    public void init(FMLInitializationEvent event)
    {

    }
}
