package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockModUnbaked;
import betterwithaddons.block.BlockRopeSideways;
import betterwithaddons.block.BlockWeight;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.recipes.AdobeRecipe;
import betterwithaddons.crafting.recipes.FoodCombiningRecipe;
import betterwithaddons.handler.*;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityAqueductWater;
import betterwithaddons.tileentity.TileEntityLureTree;
import betterwithaddons.util.*;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.PulleyStructureManager;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.AnvilRecipes;
import betterwithmods.module.gameplay.MetalReclaming;
import betterwithmods.module.hardcore.crafting.HCDiamond;
import betterwithmods.module.hardcore.needs.HCCooking;
import betterwithmods.module.tweaks.CheaperAxes;
import betterwithmods.module.tweaks.EasyBreeding;
import betterwithmods.util.DirUtils;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;

public class InteractionBWA extends Interaction {
    public static boolean GATED_AQUEDUCTS = true;
    public static int AQUEDUCT_MAX_LENGTH = 128;
    public static String[] AQUEDUCT_BIOME_STRINGS = new String[0];
    public static boolean AQUEDUCT_BIOMES_IS_WHITELIST = true;
    public static String[] AQUEDUCT_SOURCE_WHITELIST = new String[] {
            "streams:river"
    };
    public static boolean AQUEDUCT_IS_TANK = false;
    public static int AQUEDUCT_WATER_AMOUNT = Fluid.BUCKET_VOLUME;
    public static int AQUEDUCT_SOURCES_MINIMUM = 0;
    public static int AQUEDUCT_SOURCES_SEARCH = 0;

    public static boolean GRASS_TO_CLAY = false;
    public static boolean GRASS_TO_SAND = false;

    public static boolean STONEBRICKS_NEED_SMELTING = false;

    public static boolean ARMOR_SHARD_RENDER = true;
    public static double LEGENDARIUM_MIN_DAMAGE = 0.1f;
    public static int LEGENDARIUM_DAMAGE_PAD = 24;
    public static int LEGENDARIUM_POSTER_RANGE = 16;
    public static int LEGENDARIUM_MIN_QUEUE_SIZE = 7;
    public static int LEGENDARIUM_TURN_IN_DELAY = 24000 * 5;

    public static int RADIUS = 6;
    public static int MAXCHARGE = 600;
    public static int MAXFOOD = 5000;

    public static boolean CONVENIENT_TOOLS_PRE_END = true;

    public static boolean ROTTEN_FOOD = false;
    public static long MEAT_ROT_TIME = RotHandler.ONE_DAY * 4;
    public static long FISH_ROT_TIME = RotHandler.ONE_DAY * 2;
    public static long FRUIT_ROT_TIME = RotHandler.ONE_DAY * 5;
    public static long MISC_ROT_TIME = RotHandler.ONE_DAY * 3;
    public static String[] ROTTEN_FOOD_BLACKLIST = new String[] {
            "minecraft:golden_apple",
            "minecraft:golden_carrot",
            "minecraft:rotten_flesh",
            "minecraft:spider_eye",
            "minecraft:poisonous_potato",
            "minecraft:chorus_fruit",
            "betterwithmods:kibble",
            "betterwithmods:creeper_oyster",
            "betterwithaddons:food_mushroom_baked",
            "betterwithaddons:food_amanita_baked",
            "betterwithaddons:food_fugu_sac",
            "betterwithaddons:rotten_food"
    };
    public static boolean ROTTEN_FOOD_COMBINING = true;
    public static int LEGENDARIUM_SHARD_COST = 35;
    public static double LEGENDARIUM_REPAIR_COST_MULTIPLIER = 0.5;
    public static boolean HORSES_IGNORE_GOLD = true;
    public static boolean HORSES_SET_HOME = true;
    public static boolean HORSES_BREED_HAYBALE_PLACED = true;
    public static boolean HORSES_BREED_HAYBALES = false;
    public static int ROPE_LIMIT = 30;


    @Override
    protected String getName() {
        return "addons.BetterWithAddons";
    }

    @Override
    void setupConfig() {
        STONEBRICKS_NEED_SMELTING = loadPropBool("StoneBricksNeedSmelting", "Stonebricks need two extra steps in crafting.", STONEBRICKS_NEED_SMELTING);
        GATED_AQUEDUCTS = loadPropBool("GatedAqueducts", "Aqueducts require white stone to craft. This means you need to go to the end to transport water over long distances without power usage.", GATED_AQUEDUCTS);

        AQUEDUCT_BIOME_STRINGS = loadPropStringList("AqueductBiomes", "Aqueducts can only draw water from sources in specific biomes.", AQUEDUCT_BIOME_STRINGS);
        AQUEDUCT_BIOMES_IS_WHITELIST = loadPropBool("AqueductBiomesIsWhitelist", "Whether aqueduct biomes should be whitelisted or blacklisted.", AQUEDUCT_BIOMES_IS_WHITELIST);
        AQUEDUCT_SOURCE_WHITELIST = loadPropStringList("AqueductSources", "Sources Aqueducts can pull from other than real source blocks.", AQUEDUCT_SOURCE_WHITELIST);
        CONVENIENT_TOOLS_PRE_END = loadPropBool("ConvenientIronTools", "Convenient tools can be made from iron, gold and diamond pre-soulsteel.", CONVENIENT_TOOLS_PRE_END);

        ROTTEN_FOOD = loadPropBool("RottenFood", "Whether food will rot after a certain number of days has passed.", ROTTEN_FOOD);
        ROTTEN_FOOD_COMBINING = loadPropBool("RottenFoodCombining", "Whether food can be combined in the crafting grid to stack.", ROTTEN_FOOD_COMBINING);
        ROTTEN_FOOD_BLACKLIST = loadPropStringList("RottenFoodBlacklist", "These foods are excluded from rotting.", ROTTEN_FOOD_BLACKLIST);
        MEAT_ROT_TIME = loadPropInt("RottenMeatTime", "How long meat takes to rot. (In ticks)", (int) MEAT_ROT_TIME);
        FISH_ROT_TIME = loadPropInt("RottenFishTime", "How long fish takes to rot. (In ticks)", (int) FISH_ROT_TIME);
        FRUIT_ROT_TIME = loadPropInt("RottenFruitTime", "How long fruit takes to rot. (In ticks)", (int) FRUIT_ROT_TIME);
        MISC_ROT_TIME = loadPropInt("RottenMiscTime", "How long misc food takes to rot. (In ticks)", (int) MISC_ROT_TIME);

        HORSES_BREED_HAYBALES = loadPropBool("HorsesBreedHaybales", "Horeses can breed from eating dropped haybales.", HORSES_BREED_HAYBALES);

        ARMOR_SHARD_RENDER = loadPropBool("ArmorShardRender", "Enables or disables the custom armor shard renderer, for when it causes crashes.", ARMOR_SHARD_RENDER);

        doesNotNeedRestart(() -> {
            RADIUS = loadPropInt("LureTreeRadius", "Radius in which the tree can spawn mobs.", RADIUS);
            MAXCHARGE = loadPropInt("LureTreeTime", "Time it takes for the tree to do one spawning cycle.", MAXCHARGE);
            MAXFOOD = loadPropInt("LureTreeMaxFood", "How much food the tree can hold.", MAXFOOD);
            ROPE_LIMIT = loadPropInt("RopeBridgeLimit", "How far rope can be spanned horizontally between two posts or other attachments.", ROPE_LIMIT);

            HORSES_IGNORE_GOLD = loadPropBool("HorsesIgnoreGold", "Horses can't be fed golden food. It gives them a tummy ache.", HORSES_IGNORE_GOLD);
            HORSES_SET_HOME = loadPropBool("HorsesSetHome", "Horses set their home location if they're in a safe spot when dismounting.", HORSES_SET_HOME);
            HORSES_BREED_HAYBALE_PLACED = loadPropBool("HorsesBreedHaybalesPlaced", "Horses can breed from eating haybales placed in world.", HORSES_BREED_HAYBALE_PLACED);

            AQUEDUCT_MAX_LENGTH = loadPropInt("MaxAqueductLength", "How long aqueducts can be.", AQUEDUCT_MAX_LENGTH);
            AQUEDUCT_SOURCES_MINIMUM = loadPropInt("AqueductSourcesRequired", "How many connected water sources are required for an aqueduct to take from it.", AQUEDUCT_SOURCES_MINIMUM);
            AQUEDUCT_SOURCES_SEARCH = loadPropInt("AqueductSourcesSearch", "How many blocks will be checked for water sources. This should be a bit larger than the minimum amount of sources.", AQUEDUCT_SOURCES_SEARCH);
            AQUEDUCT_IS_TANK = loadPropBool("AqueductIsTank", "Aqueduct water counts as a fluid tank for modded pipes. Happy birthday Vyraal1", AQUEDUCT_IS_TANK);
            AQUEDUCT_WATER_AMOUNT = loadPropInt("AqueductWaterAmount", "How much water is contained in each block, set to less than 1000 to disallow picking up buckets.", AQUEDUCT_WATER_AMOUNT, 0, Integer.MAX_VALUE);

            LEGENDARIUM_MIN_DAMAGE = loadPropDouble("LegendariumDamageMin", "How much durability the artifact you're turning in can have at max. (As a factor of max durability; 0.1 means 1/10 of max durability)", LEGENDARIUM_MIN_DAMAGE);
            LEGENDARIUM_DAMAGE_PAD = loadPropInt("LegendariumDamagePad", "How much durability more than the minimum the artifact can have to still be considered broken. (As a static value)", LEGENDARIUM_DAMAGE_PAD);
            LEGENDARIUM_POSTER_RANGE = loadPropInt("LegendariumPosterRange", "How far away Display Frames are recognized. (in blocks; as a cubic radius)", LEGENDARIUM_POSTER_RANGE);
            LEGENDARIUM_MIN_QUEUE_SIZE = loadPropInt("LegendariumMinQueueSize", "How many artifacts must be in the Hall of Legends to take one out.", LEGENDARIUM_MIN_QUEUE_SIZE);
            LEGENDARIUM_TURN_IN_DELAY = loadPropInt("LegendariumTurnInDelay", "How long until the next artifact can be turned in. (in ticks; 1000 ticks is one Minecraft hour)", LEGENDARIUM_TURN_IN_DELAY);
            LEGENDARIUM_REPAIR_COST_MULTIPLIER = loadPropDouble("LegendariumRepairCostMultiplier", "When repairing a shard on an anvil, the repair cost is modified by this multiplier.", LEGENDARIUM_REPAIR_COST_MULTIPLIER);
            LEGENDARIUM_SHARD_COST = loadPropInt("LegendariumRepairCost", "How many levels it costs to repair a shard on an anvil.", LEGENDARIUM_SHARD_COST);
        });
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setEnabled(boolean active) {
    }

    @Override
    public List<Interaction> getDependencies() {
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        ConditionModule.MODULES.put("ConvenientToolsPreEnd", () -> CONVENIENT_TOOLS_PRE_END);
        ConditionModule.MODULES.put("StoneBricksNeedSmelting", () -> STONEBRICKS_NEED_SMELTING);
        ConditionModule.MODULES.put("GatedAqueducts", () -> GATED_AQUEDUCTS);

        if(HORSES_BREED_HAYBALES)
            EasyBreeding.REGISTRY.addPredicateEntry(new ResourceLocation(Reference.MOD_ID,"horse"),e -> e instanceof AbstractHorse && HorseFoodHandler.canHorseBreed((AbstractHorse) e)).addIngredient(Ingredient.fromStacks(new ItemStack(Blocks.HAY_BLOCK)));
        MinecraftForge.EVENT_BUS.register(new AssortedHandler());
        MinecraftForge.EVENT_BUS.register(new ToolShardRepairHandler());
        MinecraftForge.EVENT_BUS.register(new HorseFoodHandler());
        //MinecraftForge.EVENT_BUS.register(new TerratorialHandler()); //TODO: Make this do something
        MinecraftForge.EVENT_BUS.register(new ElytraUpdriftHandler());
        MinecraftForge.EVENT_BUS.register(new HarvestHandler());
        if(GRASS_TO_CLAY || GRASS_TO_SAND) {
            PatientiaHandler.addCustomBlock(Blocks.GRASS);
            MinecraftForge.EVENT_BUS.register(new GrassHandler());
        }
        if(STONEBRICKS_NEED_SMELTING)
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation("minecraft","stonebrick"));

        for(String s : AQUEDUCT_SOURCE_WHITELIST)
            TileEntityAqueductWater.addWaterSource(new ResourceLocation(s));

        VariableSegment.addVariableSupplier(new ResourceLocation(Reference.MOD_ID, "aqueduct_length"), () -> Integer.toString(AQUEDUCT_MAX_LENGTH));
    }

    @Override
    void preInitClient() {
        if(ROTTEN_FOOD)
            MinecraftForge.EVENT_BUS.register(new RotHandler());
    }

    @Override
    void oreDictRegistration() {
        OreDictionary.registerOre("logWood", new ItemStack(ModBlocks.LURETREE_LOG));
    }

    @Override
    public void init() {
        if(PatientiaHandler.shouldRegister())
            MinecraftForge.EVENT_BUS.register(new PatientiaHandler());

        //ModItems.bowls.setContainer(new ItemStack(Items.BOWL));

        ModBlocks.LURETREE_SAPLING.setLeaves(ModBlocks.LURETREE_LEAVES.getDefaultState()).setLog(ModBlocks.LURETREE_LOG.getDefaultState()).setBig(true);
        ModBlocks.LURETREE_LEAVES.setSapling(new ItemStack(ModBlocks.LURETREE_SAPLING));

        //OreDictionary.registerOre("foodSalt", ModItems.bowls.getMaterial("salt"));

        ItemStack greatarrowhead = ModItems.MATERIAL.getMaterial("arrowhead");
        ItemStack haft = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT);

        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"greatbow"), new ItemStack(ModItems.GREATBOW), "si ", "s i", "s i", "si ", 'i', haft, 's', new ItemStack(BWMBlocks.ROPE));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"greatarrow_head"), greatarrowhead, " n ", "nnn", "nnn", "n n", 'n', "nuggetSoulforgedSteel");
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"greatarrow_lightning"), new ItemStack(ModItems.GREATARROW_LIGHTNING), "nxn", "nxn", " i ", " f ", 'n', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH), 'x', greatarrowhead, 'i', haft, 'f', new ItemStack(Items.FEATHER));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"greatarrow_destruction"), new ItemStack(ModItems.GREATARROW_DESTRUCTION), "n n", "nxn", " i ", " f ", 'n', "nuggetSoulforgedSteel", 'x', greatarrowhead, 'i', haft, 'f', new ItemStack(Items.FEATHER));

        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_spade"),new ItemStack(ModItems.STEEL_SPADE),"x","x","i","i",'x',"ingotSoulforgedSteel",'i', haft);
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_matchpick"),new ItemStack(ModItems.STEEL_MATCHPICK),"xxx","nic"," i "," i ",'x', "ingotSoulforgedSteel",'i', haft,'n',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL),'c',"ingotConcentratedHellfire");
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_machete"),new ItemStack(ModItems.STEEL_MACHETE),"   x","  x "," x  ","i   ",'x', "ingotSoulforgedSteel",'i', haft);
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_kukri"),new ItemStack(ModItems.STEEL_KUKRI),"xx","x ","xx"," i",'x', "ingotSoulforgedSteel",'i', haft);
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_carpentersaw"),new ItemStack(ModItems.STEEL_CARPENTER_SAW),"xxxi","x x ",'x', "ingotSoulforgedSteel",'i', haft);
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_masonpick"),new ItemStack(ModItems.STEEL_MASON_PICK),"xxxx"," i  "," i  "," i  ",'x', "ingotSoulforgedSteel",'i', haft);

        int axeAmt = ModuleLoader.isFeatureEnabled(CheaperAxes.class) ? 2 : 3;
        if(ModuleLoader.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            int reclaimCount = MetalReclaming.reclaimCount;

            ItemStack ingotIron = new ItemStack(Items.IRON_INGOT);
            ItemStack nuggetIron = new ItemStack(Items.IRON_NUGGET);
            ItemStack ingotGold = new ItemStack(Items.GOLD_INGOT);
            ItemStack nuggetGold = new ItemStack(Items.GOLD_NUGGET);
            ItemStack ingotSteel = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL);
            ItemStack nuggetSteel = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_STEEL);

            addReclaimRecipe(new ItemStack(ModItems.IRON_SPADE),ingotIron,nuggetIron,reclaimCount*2);
            addReclaimRecipe(new ItemStack(ModItems.IRON_MATCHPICK),ingotIron,nuggetIron,reclaimCount*3);
            addReclaimRecipe(new ItemStack(ModItems.IRON_MACHETE),ingotIron,nuggetIron,reclaimCount*4);
            addReclaimRecipe(new ItemStack(ModItems.IRON_KUKRI),ingotIron,nuggetIron,reclaimCount*(axeAmt+2));
            addReclaimRecipe(new ItemStack(ModItems.IRON_CARPENTER_SAW),ingotIron,nuggetIron,reclaimCount*(axeAmt+2));
            addReclaimRecipe(new ItemStack(ModItems.IRON_MASON_PICK),ingotIron,nuggetIron,reclaimCount*4);

            addReclaimRecipe(new ItemStack(ModItems.GOLD_SPADE),ingotGold,nuggetGold,reclaimCount*2);
            addReclaimRecipe(new ItemStack(ModItems.GOLD_MATCHPICK),ingotGold,nuggetGold,reclaimCount*3);
            addReclaimRecipe(new ItemStack(ModItems.GOLD_MACHETE),ingotGold,nuggetGold,reclaimCount*4);
            addReclaimRecipe(new ItemStack(ModItems.GOLD_KUKRI),ingotGold,nuggetGold,reclaimCount*(axeAmt+2));
            addReclaimRecipe(new ItemStack(ModItems.GOLD_CARPENTER_SAW),ingotGold,nuggetGold,reclaimCount*(axeAmt+2));
            addReclaimRecipe(new ItemStack(ModItems.GOLD_MASON_PICK),ingotGold,nuggetGold,reclaimCount*4);

            addReclaimRecipe(new ItemStack(ModItems.STEEL_SPADE),ingotSteel,nuggetSteel,9*2);
            addReclaimRecipe(new ItemStack(ModItems.STEEL_MATCHPICK),ingotSteel,nuggetSteel,9*3);
            addReclaimRecipe(new ItemStack(ModItems.STEEL_MACHETE),ingotSteel,nuggetSteel,9*3);
            addReclaimRecipe(new ItemStack(ModItems.STEEL_KUKRI),ingotSteel,nuggetSteel,9*5);
            addReclaimRecipe(new ItemStack(ModItems.STEEL_CARPENTER_SAW),ingotSteel,nuggetSteel,9*5);
            addReclaimRecipe(new ItemStack(ModItems.STEEL_MASON_PICK),ingotSteel,nuggetSteel,9*4);

            if(ModuleLoader.isFeatureEnabled(HCDiamond.class))
            {
                ItemStack ingotDiamond = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT);
                ItemStack nuggetDiamond = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET);
                addReclaimRecipe(new ItemStack(ModItems.DIAMOND_SPADE),ingotDiamond,nuggetDiamond,9*2);
                addReclaimRecipe(new ItemStack(ModItems.DIAMOND_MATCHPICK),ingotDiamond,nuggetDiamond,9*3);
                addReclaimRecipe(new ItemStack(ModItems.DIAMOND_MACHETE),ingotDiamond,nuggetDiamond,9*4);
                addReclaimRecipe(new ItemStack(ModItems.DIAMOND_KUKRI),ingotDiamond,nuggetDiamond,9*(axeAmt+2));
                addReclaimRecipe(new ItemStack(ModItems.DIAMOND_CARPENTER_SAW),ingotDiamond,nuggetDiamond,9*(axeAmt+2));
                addReclaimRecipe(new ItemStack(ModItems.DIAMOND_MASON_PICK),ingotDiamond,nuggetDiamond,9*4);
            }
        }

        BWRegistry.CAULDRON.addStokedRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.LURETREE_LOG),new ItemStack(ModBlocks.LURETREE_FACE)),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH,2)).setPriority(10);

        TileEntityLureTree.addTreeFood(new ItemStack(ModItems.ROTTEN_FOOD),250);
        TileEntityLureTree.addTreeFood(new ItemStack(Items.ROTTEN_FLESH),250);
        TileEntityLureTree.addTreeFood(new ItemStack(Items.GLOWSTONE_DUST),500);
        TileEntityLureTree.addTreeFood(ModItems.MATERIAL.getMaterial("thornrose"),1000);
        TileEntityLureTree.addTreeFood(new ItemStack(BWMItems.MYSTERY_MEAT),4000);

        //TODO: Make this more sensible holy shit
        TileEntityAqueductWater.reloadBiomeList();

        ISpecialMeasuringBehavior platformBehavior = new ISpecialMeasuringBehavior() {
            @Override
            public boolean isFull(World world, BlockPos pos, IBlockState state) {
                HashSet<BlockPos> platformBlocks = new HashSet<>();
                boolean success = PulleyUtil.findPlatformPart(world,pos,platformBlocks);
                if(success)
                {
                    return measurePlatform(world,platformBlocks) >= countPlatforms(world,platformBlocks);
                }

                return false;
            }

            @Override
            public boolean isEmpty(World world, BlockPos pos, IBlockState state) {
                HashSet<BlockPos> platformBlocks = new HashSet<>();
                boolean success = PulleyUtil.findPlatformPart(world,pos,platformBlocks);
                if(success)
                {
                    return measurePlatform(world,platformBlocks) == 0 && countPlatforms(world,platformBlocks) > 0;
                }

                return true;
            }

            @Override
            public int getDelay(World world, BlockPos pos, IBlockState state) {
                return 4;
            }
        };
        BlockWeight.addSpecialMeasuringBehavior(Blocks.CAULDRON, new ISpecialMeasuringBehavior() {
            @Override
            public boolean isFull(World world, BlockPos pos, IBlockState state) {
                return state.getValue(BlockCauldron.LEVEL) == 3;
            }

            @Override
            public boolean isEmpty(World world, BlockPos pos, IBlockState state) {
                return state.getValue(BlockCauldron.LEVEL) == 0;
            }

            @Override
            public int getDelay(World world, BlockPos pos, IBlockState state) {
                return 1;
            }
        });
        BlockWeight.addSpecialMeasuringBehavior(BWMBlocks.ANCHOR, new ISpecialMeasuringBehavior() {
            @Override
            public boolean isFull(World world, BlockPos pos, IBlockState state) {
                return false;
            }

            @Override
            public boolean isEmpty(World world, BlockPos pos, IBlockState state) {
                return state.getValue(DirUtils.FACING) != EnumFacing.UP;
            }

            @Override
            public int getDelay(World world, BlockPos pos, IBlockState state) {
                return 1;
            }
        });
        BlockWeight.addSpecialMeasuringBehavior(BWMBlocks.PLATFORM, platformBehavior);
        BlockWeight.addSpecialMeasuringBehavior(BWMBlocks.IRON_WALL, platformBehavior);

        //meme? idk
        ModBlocks.REDSTONE_EMITTER.getBlockState().getValidStates().forEach(PulleyStructureManager::registerPulleyBlock);

        GameRegistry.addSmelting(Items.CARROT,new ItemStack(ModItems.BAKED_CARROT),0.35f);
        GameRegistry.addSmelting(Items.BEETROOT,new ItemStack(ModItems.BAKED_BEETROOT),0.35f);
        GameRegistry.addSmelting(Blocks.BROWN_MUSHROOM,new ItemStack(ModItems.BAKED_MUSHROOM),0.35f);
        GameRegistry.addSmelting(Blocks.RED_MUSHROOM,new ItemStack(ModItems.BAKED_AMANITA),0.35f);

        boolean hchunger = ModuleLoader.isFeatureEnabled(HCCooking.class);

        BWRegistry.KILN.addStokedRecipe(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.MELON),new ItemStack(ModItems.PIE_MELON,hchunger ? 1 : 2));
        BWRegistry.KILN.addStokedRecipe(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.MEAT),new ItemStack(ModItems.PIE_MEAT,hchunger ? 1 : 2));
        BWRegistry.KILN.addStokedRecipe(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.MUSHROOM),new ItemStack(ModItems.PIE_MUSHROOM,hchunger ? 1 : 2));
        BWRegistry.KILN.addStokedRecipe(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.AMANITA),new ItemStack(ModItems.PIE_AMANITA,hchunger ? 1 : 2));

        GameRegistry.addSmelting(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.MELON),new ItemStack(ModItems.PIE_MELON),0.35f);
        GameRegistry.addSmelting(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.MEAT),new ItemStack(ModItems.PIE_MEAT),0.35f);
        GameRegistry.addSmelting(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.MUSHROOM),new ItemStack(ModItems.PIE_MUSHROOM),0.35f);
        GameRegistry.addSmelting(BlockModUnbaked.getStack(BlockModUnbaked.EnumType.AMANITA),new ItemStack(ModItems.PIE_AMANITA),0.35f);

        if(STONEBRICKS_NEED_SMELTING) {
            GameRegistry.addSmelting(Blocks.STONE, ModItems.MATERIAL.getMaterial("stone_brick", 4), 0.1f);
        }

        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromStacks(new ItemStack(ModItems.ROTTEN_FOOD)),new ItemStack(BWMItems.FERTILIZER));

        BlockRopeSideways.addFastenableBlock(ModBlocks.SCAFFOLD);
        BlockRopeSideways.addFastenableBlock(ModBlocks.ROPE_POST);

        BWRegistry.CAULDRON.addRecipe(new AdobeRecipe());
    }

    int countPlatforms(World world, HashSet<BlockPos> platforms)
    {
        int count = 0;

        for(BlockPos pos : platforms)
        {
            IBlockState state = world.getBlockState(pos);
            if(state.getBlock() == BWMBlocks.PLATFORM)
                count++;
        }

        return count;
    }

    int measurePlatform(World world, HashSet<BlockPos> platforms)
    {
        int detected = 0;
        int minx = 0,miny = 0,minz = 0;
        int maxx = 0,maxy = 0,maxz = 0;
        boolean first = true;
        AxisAlignedBB aabb = null;

        for(BlockPos pos : platforms)
        {
            IBlockState state = world.getBlockState(pos);
            if(PulleyStructureManager.isPulleyBlock(state))
            {
                if(first) {
                    minx = pos.getX();
                    miny = pos.getY();
                    minz = pos.getZ();
                    maxx = pos.getX() + 1;
                    maxy = pos.getY() + 1;
                    maxz = pos.getZ() + 1;
                    first = false;
                }
                else
                {
                    minx = Math.min(pos.getX(),minx);
                    miny = Math.min(pos.getY(),miny);
                    minz = Math.min(pos.getZ(),minz);
                    maxx = Math.max(pos.getX()+1,maxx);
                    maxy = Math.max(pos.getY()+1,maxy);
                    maxz = Math.max(pos.getZ()+1,maxz);
                }
            }
        }

        aabb = new AxisAlignedBB(minx,miny,minz,maxx,maxy,maxz).offset(0,1,0);

        for(Entity entity : world.getEntitiesWithinAABB(Entity.class,aabb, x -> isHeavyEntity(x) && x.onGround))
        {
            BlockPos pos = EntityUtil.getEntityFloor(entity,2);
            if(platforms.contains(pos))
                detected++;
        }

        return detected;
    }

    boolean isHeavyEntity(Entity entity)
    {
        return entity instanceof EntityLivingBase || entity instanceof EntityMinecart;
    }

    @Override
    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        if(ROTTEN_FOOD && ROTTEN_FOOD_COMBINING)
        {
            event.getRegistry().register(new FoodCombiningRecipe().setRegistryName(new ResourceLocation(Reference.MOD_ID,"food_combining")));
        }
    }

    public void addReclaimRecipe(ItemStack input, ItemStack ingot, ItemStack nugget, int nuggets)
    {
        int ingots = nuggets / 9;
        nuggets = nuggets % 9;

        input = new ItemStack(input.getItem(),input.getCount(),OreDictionary.WILDCARD_VALUE);

        ItemStack ingotStack = ingot.copy();
        ingotStack.setCount(ingots);
        ItemStack nuggetStack = nugget.copy();
        nuggetStack.setCount(nuggets);

        BWRegistry.CRUCIBLE.addStokedRecipe(Ingredient.fromStacks(input),Lists.newArrayList(ingotStack,nuggetStack));
    }

    @Override
    public void postInit() {
        HashSet<ResourceLocation> rotBlackList = new HashSet<>();
        for (String resname : ROTTEN_FOOD_BLACKLIST) {
            rotBlackList.add(new ResourceLocation(resname));
        }

        for (Item item : Item.REGISTRY) {
            if(!(item instanceof ItemFood)|| rotBlackList.contains(item.getRegistryName()))
                continue;

            ItemStack stack = new ItemStack(item);

            if(stack.isEmpty())
                continue;

            if(item instanceof ItemFishFood || ItemUtil.matchesOreDict(stack,"listAllfish"))
                RotHandler.addRottingItem(stack,FISH_ROT_TIME,"fish",new ItemStack(Items.ROTTEN_FLESH));
            else if(ItemUtil.matchesOreDict(stack,"foodMeat") || ItemUtil.matchesOreDict(stack,"listAllmeat") || ItemUtil.matchesOreDict(stack,"listAllmeatcooked"))
                RotHandler.addRottingItem(stack,MEAT_ROT_TIME,"meat",new ItemStack(Items.ROTTEN_FLESH));
            else if(isFruit(stack))
                RotHandler.addRottingItem(stack,FRUIT_ROT_TIME,"fruit",new ItemStack(ModItems.ROTTEN_FOOD));
            else
                RotHandler.addRottingItem(stack,MISC_ROT_TIME);
        }

        RotHandler.addRottingItem(new ItemStack(Items.CAKE, 1, OreDictionary.WILDCARD_VALUE));
        RotHandler.addRottingItem(new ItemStack(Items.FISH,1,ItemFishFood.FishType.SALMON.getMetadata()),FISH_ROT_TIME,"fish",new ItemStack(Items.ROTTEN_FLESH));
        RotHandler.addRottingItem(new ItemStack(Items.FISH,1,ItemFishFood.FishType.CLOWNFISH.getMetadata()),FISH_ROT_TIME,"fish",new ItemStack(Items.ROTTEN_FLESH));
        RotHandler.addRottingItem(new ItemStack(Items.FISH,1,ItemFishFood.FishType.PUFFERFISH.getMetadata()),FISH_ROT_TIME,"fish",new ItemStack(Items.ROTTEN_FLESH));

        //Salt Cluster grinding recipe (migration version, to be changed)
        List<ItemStack> saltDusts = OreDictionary.getOres("foodSalt");
        if(saltDusts != null && saltDusts.size() > 0)
        {
            ItemStack saltCluster = new ItemStack(ModItems.SALTS,1,0);
            ItemStack saltDust = saltDusts.get(0).copy();
            saltDust.setCount(3);
            BWRegistry.MILLSTONE.addMillRecipe(Ingredient.fromStacks(saltCluster),saltDust);
        }
    }

    private boolean isFruit(ItemStack stack) {
        int[] oreids = OreDictionary.getOreIDs(stack);
        for (int oreid : oreids) {
            if(OreDictionary.getOreName(oreid).startsWith("crop"))
                return true;
        }
        return false;
    }
}
