package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockAqueduct;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.handler.*;
import betterwithaddons.item.ModItems;
import betterwithaddons.tileentity.TileEntityAqueductWater;
import betterwithaddons.tileentity.TileEntityLureTree;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.CrucibleManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.common.registry.steelanvil.SteelCraftingManager;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.gameplay.MetalReclaming;
import betterwithmods.module.hardcore.HCDiamond;
import betterwithmods.module.hardcore.HCOres;
import net.minecraft.block.BlockQuartz;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
        MinecraftForge.EVENT_BUS.register(new AssortedHandler());
        MinecraftForge.EVENT_BUS.register(new ToolShardRepairHandler());
        //MinecraftForge.EVENT_BUS.register(new TerratorialHandler()); //TODO: Make this do something
        MinecraftForge.EVENT_BUS.register(new ElytraUpdriftHandler());
        MinecraftForge.EVENT_BUS.register(new HarvestHandler());
        if(OBVIOUS_SAND_STORMS || OBVIOUS_STORMS)
            MinecraftForge.EVENT_BUS.register(new StormHandler());
        if(GRASS_TO_CLAY || GRASS_TO_SAND) {
            PatientiaHandler.addCustomBlock(Blocks.GRASS);
            MinecraftForge.EVENT_BUS.register(new GrassHandler());
        }
    }

    @Override
    public void init() {
        if(PatientiaHandler.shouldRegister())
            MinecraftForge.EVENT_BUS.register(new PatientiaHandler());

        if(!ModInteractions.bwm.isActive()) //add recipes even if better with mods isn't installed.
        {
            String oreArrowhead = "ingotIron";
            if(OreDictionary.doesOreNameExist("ingotSteel"))
                oreArrowhead = "ingotSteel";
            String oreHaft = "stickWood";
            String oreGlue = "slimeball";
            String oreString = "string";
            ItemStack bow = new ItemStack(Items.BOW);
            ItemStack feather = new ItemStack(Items.FEATHER);
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatarrow,1)," a "," b ","cbc",'a',oreArrowhead,'b',oreHaft,'c',feather));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatbow,1),"bac","ed ","bac",'a',oreArrowhead,'b',oreHaft,'c',oreString,'d',bow,'e',oreGlue));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.pcbblock),new ItemStack(Blocks.STONEBRICK),new ItemStack(Items.FERMENTED_SPIDER_EYE));

            GameRegistry.addSmelting(ModItems.material.getMaterial("midori"),ModItems.material.getMaterial("midori_popped"),0.1f);

            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScale,1)," i ","iai"," i ",'a',new ItemStack(ModBlocks.worldScaleOre,0,1),'i',new ItemStack(Items.IRON_INGOT));
        }

        Object diamondMaterial = "gemDiamond";
        if(ModuleLoader.isFeatureEnabled(HCDiamond.class))
            diamondMaterial = "ingotDiamond";

        if(CONVENIENT_TOOLS_PRE_END) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ironSpade), "m", "t", "s", 'm', "ingotIron", 't', new ItemStack(Items.IRON_SHOVEL), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ironMatchPick), "ftc", " s ", " s ", 'f', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL), 'c', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE), 't', new ItemStack(Items.IRON_PICKAXE), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ironMachete), "  m", " m ", "t  ", 'm', "ingotIron", 't', new ItemStack(Items.IRON_SWORD)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ironKukri), "  m", " m ", "t  ", 'm', "ingotIron", 't', new ItemStack(Items.IRON_AXE)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ironCarpenterSaw), "mmt", 'm', "ingotIron", 't', new ItemStack(Items.IRON_AXE), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ironMasonPick), "mt", " s", " s", 'm', "ingotIron", 't', new ItemStack(Items.IRON_PICKAXE), 's', "stickWood"));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.goldSpade), "m", "t", "s", 'm', "ingotGold", 't', new ItemStack(Items.GOLDEN_SHOVEL), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.goldMatchPick), "ftc", " s ", " s ", 'f', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL), 'c', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE), 't', new ItemStack(Items.GOLDEN_PICKAXE), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.goldMachete), "  m", " m ", "t  ", 'm', "ingotGold", 't', new ItemStack(Items.GOLDEN_SWORD)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.goldKukri), "  m", " m ", "t  ", 'm', "ingotGold", 't', new ItemStack(Items.GOLDEN_AXE)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.goldCarpenterSaw), "mmt", 'm', "ingotGold", 't', new ItemStack(Items.GOLDEN_AXE), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.goldMasonPick), "mt", " s", " s", 'm', "ingotGold", 't', new ItemStack(Items.GOLDEN_PICKAXE), 's', "stickWood"));

            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.diamondSpade), "m", "t", "s", 'm', diamondMaterial, 't', new ItemStack(Items.DIAMOND_SHOVEL), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.diamondMatchPick), "ftc", " s ", " s ", 'f', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL), 'c', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE), 't', new ItemStack(Items.DIAMOND_PICKAXE), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.diamondMachete), "  m", " m ", "t  ", 'm', diamondMaterial, 't', new ItemStack(Items.DIAMOND_SWORD)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.diamondKukri), "  m", " m ", "t  ", 'm', diamondMaterial, 't', new ItemStack(Items.DIAMOND_AXE)));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.diamondCarpenterSaw), "mmt", 'm', diamondMaterial, 't', new ItemStack(Items.DIAMOND_AXE), 's', "stickWood"));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.diamondMasonPick), "mt", " s", " s", 'm', diamondMaterial, 't', new ItemStack(Items.DIAMOND_PICKAXE), 's', "stickWood"));
        }

        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.steelSpade),"x","x","i","i",'x',"ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.steelMatchPick),"xxx","nic"," i "," i ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT),'n',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL),'c',"ingotConcentratedHellfire");
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.steelMachete),"   x","  x "," x  ","i   ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.steelKukri),"xx","x ","xx"," i",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.steelCarpenterSaw),"xxxi","x x ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.steelMasonPick),"xxxx"," i  "," i  "," i  ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));

        if(ModuleLoader.isFeatureEnabled(MetalReclaming.class) && MetalReclaming.reclaimCount > 0) {
            int reclaimCount = MetalReclaming.reclaimCount;

            ItemStack ingotIron = new ItemStack(Items.IRON_INGOT);
            ItemStack nuggetIron = new ItemStack(Items.field_191525_da);
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

        TileEntityLureTree.addTreeFood(new ItemStack(Items.GLOWSTONE_DUST),450);

        //TODO: Make this more sensible holy shit
        TileEntityAqueductWater.reloadBiomeList();

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.legendarium,1) ,"gsg","qqq","gqg",'g',new ItemStack(Items.GOLD_INGOT),'q',new ItemStack(Blocks.QUARTZ_BLOCK,1, BlockQuartz.EnumType.CHISELED.getMetadata()),'s', new ItemStack(Items.NETHER_STAR));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.artifactFrame,1) ,"gsg","gqg","ggg",'g',new ItemStack(Items.GOLD_NUGGET),'q',new ItemStack(Blocks.WOOL,1,EnumDyeColor.PURPLE.getMetadata()),'s', new ItemStack(Items.SIGN));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.pondBase),new ItemStack(Blocks.SAND),new ItemStack(Items.CLAY_BALL));

        if(GATED_AQUEDUCTS)
        {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.WHITESTONE_BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(ModBlocks.whiteBrick));
        }
        else {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.STONE_BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(Blocks.STONEBRICK));
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(Blocks.BRICK_BLOCK));
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.QUARTZ.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(Blocks.QUARTZ_BLOCK));
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.WHITESTONE_BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(ModBlocks.whiteBrick));
        }
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.STONE_BRICKS.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(Blocks.STONEBRICK));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.BRICKS.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(Blocks.BRICK_BLOCK));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.QUARTZ.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(Blocks.QUARTZ_BLOCK));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.WHITESTONE_BRICKS.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(ModBlocks.whiteBrick));

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScaleOre,1,1) ,"aa ","aaa"," aa",'a',new ItemStack(ModItems.worldShard));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScaleActive,1)," d ","iae"," d ",'a',new ItemStack(ModBlocks.worldScale),'i',new ItemStack(Items.IRON_PICKAXE),'e',new ItemStack(Items.IRON_AXE),'d',new ItemStack(Items.DIAMOND));

        GameRegistry.addSmelting(Items.CARROT,new ItemStack(ModItems.bakedCarrot),0.35f);
        GameRegistry.addSmelting(Items.BEETROOT,new ItemStack(ModItems.bakedBeetroot),0.35f);
        GameRegistry.addSmelting(Blocks.BROWN_MUSHROOM,new ItemStack(ModItems.bakedMushroom),0.35f);
        GameRegistry.addSmelting(Blocks.RED_MUSHROOM,new ItemStack(ModItems.bakedAmanita),0.35f);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMelon),new ItemStack(Items.MELON),new ItemStack(Items.MELON),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMushroom),new ItemStack(Blocks.BROWN_MUSHROOM),new ItemStack(Blocks.BROWN_MUSHROOM),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieAmanita),new ItemStack(Blocks.RED_MUSHROOM),new ItemStack(Blocks.RED_MUSHROOM),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMeat),new ItemStack(ModItems.groundMeat),new ItemStack(ModItems.groundMeat),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lattice,2)," a ","aaa"," a ",'a',new ItemStack(Blocks.IRON_BARS));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elytraMagma,1),"aa","aa",'a',ModItems.material.getMaterial("ender_cream"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bannerDetector,1),"aaa","o r","aaa",'a',new ItemStack(Blocks.COBBLESTONE),'o',new ItemStack(Items.ENDER_EYE),'r',new ItemStack(Items.REDSTONE));

        if(STONEBRICKS_NEED_SMELTING) {
            BetterWithAddons.removeCraftingRecipe(new ItemStack(Blocks.STONEBRICK, 4));
            GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONEBRICK, 1), "aa", "aa", 'a', ModItems.material.getMaterial("stone_brick"));
            GameRegistry.addSmelting(Blocks.STONE, ModItems.material.getMaterial("stone_brick", 4), 0.1f);
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

    }
}
