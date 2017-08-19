package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.*;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.recipes.FoodCombiningRecipe;
import betterwithaddons.handler.*;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityAqueductWater;
import betterwithaddons.tileentity.TileEntityLureTree;
import betterwithaddons.util.ISpecialMeasuringBehavior;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.blockmeta.managers.KilnManager;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.AnvilRecipes;
import betterwithmods.module.gameplay.MetalReclaming;
import betterwithmods.module.hardcore.HCDiamond;
import betterwithmods.module.hardcore.hchunger.HCHunger;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;

public class InteractionBWA extends Interaction {
    public static boolean GATED_AQUEDUCTS = true;
    public static int AQUEDUCT_MAX_LENGTH = 128;
    public static String[] AQUEDUCT_BIOME_STRINGS = new String[0];
    public static boolean AQUEDUCT_BIOMES_IS_WHITELIST = true;

    public static boolean OBVIOUS_STORMS = false;
    public static boolean OBVIOUS_SAND_STORMS = false;
    public static int DUST_PARTICLES = 2;
    public static int AIR_PARTICLES = 3;

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

        MinecraftForge.EVENT_BUS.register(new AssortedHandler());
        MinecraftForge.EVENT_BUS.register(new ToolShardRepairHandler());
        //MinecraftForge.EVENT_BUS.register(new TerratorialHandler()); //TODO: Make this do something
        MinecraftForge.EVENT_BUS.register(new ElytraUpdriftHandler());
        MinecraftForge.EVENT_BUS.register(new HarvestHandler());
        if(GRASS_TO_CLAY || GRASS_TO_SAND) {
            PatientiaHandler.addCustomBlock(Blocks.GRASS);
            MinecraftForge.EVENT_BUS.register(new GrassHandler());
        }
        if(STONEBRICKS_NEED_SMELTING)
            BetterWithAddons.removeCraftingRecipe(new ItemStack(Blocks.STONEBRICK, 4));
    }

    @Override
    void preInitClient() {
        if(OBVIOUS_SAND_STORMS || OBVIOUS_STORMS)
            MinecraftForge.EVENT_BUS.register(new StormHandler());
        if(ROTTEN_FOOD)
            MinecraftForge.EVENT_BUS.register(new RotHandler());
    }

    @Override
    public void init() {
        if(PatientiaHandler.shouldRegister())
            MinecraftForge.EVENT_BUS.register(new PatientiaHandler());

        ModItems.bowls.setContainer(new ItemStack(Items.BOWL));

        ModBlocks.luretreeSapling.setLeaves(ModBlocks.luretreeLeaves.getDefaultState()).setLog(ModBlocks.luretreeLog.getDefaultState()).setBig(true);
        ModBlocks.luretreeLeaves.setSapling(new ItemStack(ModBlocks.luretreeSapling));

        OreDictionary.registerOre("foodSalt", ModItems.bowls.getMaterial("salt"));

        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_spade"),new ItemStack(ModItems.steelSpade),"x","x","i","i",'x',"ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_matchpick"),new ItemStack(ModItems.steelMatchPick),"xxx","nic"," i "," i ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT),'n',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL),'c',"ingotConcentratedHellfire");
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_machete"),new ItemStack(ModItems.steelMachete),"   x","  x "," x  ","i   ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_kukri"),new ItemStack(ModItems.steelKukri),"xx","x ","xx"," i",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_carpentersaw"),new ItemStack(ModItems.steelCarpenterSaw),"xxxi","x x ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"steel_masonpick"),new ItemStack(ModItems.steelMasonPick),"xxxx"," i  "," i  "," i  ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));

        if(ModuleLoader.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            int reclaimCount = MetalReclaming.reclaimCount;

            ItemStack ingotIron = new ItemStack(Items.IRON_INGOT);
            ItemStack nuggetIron = new ItemStack(Items.IRON_NUGGET);
            ItemStack ingotGold = new ItemStack(Items.GOLD_INGOT);
            ItemStack nuggetGold = new ItemStack(Items.GOLD_NUGGET);
            ItemStack ingotSteel = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.INGOT_STEEL);
            ItemStack nuggetSteel = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NUGGET_STEEL);

            addReclaimRecipe(new ItemStack(ModItems.ironSpade),ingotIron,nuggetIron,reclaimCount*2);
            addReclaimRecipe(new ItemStack(ModItems.ironMatchPick),ingotIron,nuggetIron,reclaimCount*3);
            addReclaimRecipe(new ItemStack(ModItems.ironMachete),ingotIron,nuggetIron,reclaimCount*4);
            addReclaimRecipe(new ItemStack(ModItems.ironKukri),ingotIron,nuggetIron,reclaimCount*5);
            addReclaimRecipe(new ItemStack(ModItems.ironCarpenterSaw),ingotIron,nuggetIron,reclaimCount*5);
            addReclaimRecipe(new ItemStack(ModItems.ironMasonPick),ingotIron,nuggetIron,reclaimCount*4);

            addReclaimRecipe(new ItemStack(ModItems.goldSpade),ingotGold,nuggetGold,reclaimCount*2);
            addReclaimRecipe(new ItemStack(ModItems.goldMatchPick),ingotGold,nuggetGold,reclaimCount*3);
            addReclaimRecipe(new ItemStack(ModItems.goldMachete),ingotGold,nuggetGold,reclaimCount*4);
            addReclaimRecipe(new ItemStack(ModItems.goldKukri),ingotGold,nuggetGold,reclaimCount*5);
            addReclaimRecipe(new ItemStack(ModItems.goldCarpenterSaw),ingotGold,nuggetGold,reclaimCount*5);
            addReclaimRecipe(new ItemStack(ModItems.goldMasonPick),ingotGold,nuggetGold,reclaimCount*4);

            addReclaimRecipe(new ItemStack(ModItems.steelSpade),ingotSteel,nuggetSteel,9*2);
            addReclaimRecipe(new ItemStack(ModItems.steelMatchPick),ingotSteel,nuggetSteel,9*3);
            addReclaimRecipe(new ItemStack(ModItems.steelMachete),ingotSteel,nuggetSteel,9*3);
            addReclaimRecipe(new ItemStack(ModItems.steelKukri),ingotSteel,nuggetSteel,9*5);
            addReclaimRecipe(new ItemStack(ModItems.steelCarpenterSaw),ingotSteel,nuggetSteel,9*5);
            addReclaimRecipe(new ItemStack(ModItems.steelMasonPick),ingotSteel,nuggetSteel,9*4);

            if(ModuleLoader.isFeatureEnabled(HCDiamond.class))
            {
                ItemStack ingotDiamond = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_INGOT);
                ItemStack nuggetDiamond = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DIAMOND_NUGGET);
                addReclaimRecipe(new ItemStack(ModItems.diamondSpade),ingotDiamond,nuggetDiamond,9*2);
                addReclaimRecipe(new ItemStack(ModItems.diamondMatchPick),ingotDiamond,nuggetDiamond,9*3);
                addReclaimRecipe(new ItemStack(ModItems.diamondMachete),ingotDiamond,nuggetDiamond,9*4);
                addReclaimRecipe(new ItemStack(ModItems.diamondKukri),ingotDiamond,nuggetDiamond,9*5);
                addReclaimRecipe(new ItemStack(ModItems.diamondCarpenterSaw),ingotDiamond,nuggetDiamond,9*5);
                addReclaimRecipe(new ItemStack(ModItems.diamondMasonPick),ingotDiamond,nuggetDiamond,9*4);
            }
        }

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

        TileEntityLureTree.addTreeFood(new ItemStack(Items.GLOWSTONE_DUST),450);

        //TODO: Make this more sensible holy shit
        TileEntityAqueductWater.reloadBiomeList();

        GameRegistry.addSmelting(Items.CARROT,new ItemStack(ModItems.bakedCarrot),0.35f);
        GameRegistry.addSmelting(Items.BEETROOT,new ItemStack(ModItems.bakedBeetroot),0.35f);
        GameRegistry.addSmelting(Blocks.BROWN_MUSHROOM,new ItemStack(ModItems.bakedMushroom),0.35f);
        GameRegistry.addSmelting(Blocks.RED_MUSHROOM,new ItemStack(ModItems.bakedAmanita),0.35f);

        boolean hchunger = ModuleLoader.isFeatureEnabled(HCHunger.class);

        KilnManager.INSTANCE.addRecipe(ModBlocks.unbaked, BlockModUnbaked.EnumType.MELON.getMetadata(), new ItemStack(ModItems.pieMelon,hchunger ? 1 : 2));
        KilnManager.INSTANCE.addRecipe(ModBlocks.unbaked, BlockModUnbaked.EnumType.MEAT.getMetadata(), new ItemStack(ModItems.pieMeat,hchunger ? 1 : 2));
        KilnManager.INSTANCE.addRecipe(ModBlocks.unbaked, BlockModUnbaked.EnumType.MUSHROOM.getMetadata(), new ItemStack(ModItems.pieMushroom,hchunger ? 1 : 2));
        KilnManager.INSTANCE.addRecipe(ModBlocks.unbaked, BlockModUnbaked.EnumType.AMANITA.getMetadata(), new ItemStack(ModItems.pieAmanita,hchunger ? 1 : 2));

        if(STONEBRICKS_NEED_SMELTING) {
            GameRegistry.addSmelting(Blocks.STONE, ModItems.material.getMaterial("stone_brick", 4), 0.1f);
        }

        CauldronManager.getInstance().addRecipe(new ItemStack(BWMItems.FERTILIZER), ItemStack.EMPTY, new Object[]{new ItemStack(ModItems.rottenFood)});

        BlockRopeSideways.addFastenableBlock(ModBlocks.scaffold);
        BlockRopeSideways.addFastenableBlock(ModBlocks.ropePost);
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

        StokedCrucibleManager.getInstance().addRecipe(ingotStack,nuggetStack,new Object[]{input});
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
            else if(ItemUtil.matchesOreDict(stack,"listAllmeat") || ItemUtil.matchesOreDict(stack,"listAllmeatcooked"))
                RotHandler.addRottingItem(stack,MEAT_ROT_TIME,"meat",new ItemStack(Items.ROTTEN_FLESH));
            else if(isFruit(stack))
                RotHandler.addRottingItem(stack,FRUIT_ROT_TIME,"fruit",new ItemStack(ModItems.rottenFood));
            else
                RotHandler.addRottingItem(stack,MISC_ROT_TIME);
        }

        RotHandler.addRottingItem(new ItemStack(Items.CAKE, 1, OreDictionary.WILDCARD_VALUE));
        RotHandler.addRottingItem(new ItemStack(Items.FISH,1,ItemFishFood.FishType.SALMON.getMetadata()),FISH_ROT_TIME,"fish",new ItemStack(Items.ROTTEN_FLESH));
        RotHandler.addRottingItem(new ItemStack(Items.FISH,1,ItemFishFood.FishType.CLOWNFISH.getMetadata()),FISH_ROT_TIME,"fish",new ItemStack(Items.ROTTEN_FLESH));
        RotHandler.addRottingItem(new ItemStack(Items.FISH,1,ItemFishFood.FishType.PUFFERFISH.getMetadata()),FISH_ROT_TIME,"fish",new ItemStack(Items.ROTTEN_FLESH));
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
