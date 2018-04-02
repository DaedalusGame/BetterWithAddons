package betterwithaddons.interaction;

import betterwithaddons.crafting.recipes.LapisRinsingRecipe;
import betterwithaddons.crafting.recipes.QuartzCrystalRecipe;
import betterwithaddons.handler.*;
import betterwithaddons.item.ModItems;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.crafting.HCDiamond;
import betterwithmods.module.hardcore.needs.HCTools;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.List;

public class InteractionBWR extends Interaction {
    public static boolean ENABLED = false;
    public static boolean REDSTONE_SYNTHESIS = true;
    public static boolean REDSTONE_SYNTHESIS_EARLY = true;
    public static boolean HELLFIRE_EARLY = true;
    public static boolean BOILING_BUSHES = true;
    public static boolean WEAVING_WEBS = true;
    public static boolean LAPIS_FROM_WOOL = true;
    public static boolean DIAMOND_SYNTHESIS = true;
    public static boolean DIAMOND_RECOVERY = true;
    public static boolean GOLD_GRINDING = true;
    public static boolean NETHERRACK_SYNTHESIS = true;
    public static boolean QUARTZ_GROWING = true;
    public static boolean EMERALD_PORTAL = true;

    public static boolean MELT_HELLFIRE = true;
    public static boolean DUNG_TO_DIRT = true;
    public static boolean SAND_TO_CLAY = true;
    public static boolean CROSSBREED_PLANTS = true;
    public static boolean CROSSBREED_ANIMALS = true;
    public static boolean REDSTONE_BOILING = true;
    public static boolean SOULSAND_INFUSION = true;
    public static boolean BLAZE_GOLEMS = true;
    public static boolean BLAZE_BREEDING = true;
    public static int BLAZE_BREEDING_DELAY = 6000;

    public static int GOLD_PER_INGOT = 1;
    public static int REDSTONE_PER_SYNTHESIS = 7;
    public static double REDSTONE_BOILING_CHANCE = 0.1;
    public static int QUARTZ_GROWING_THRESHOLD = 20;
    public static int SOULSAND_INFUSION_THRESHOLD = 50;
    public static int DUNG_TO_DIRT_THRESHOLD = 300;
    public static int DUNG_TO_DIRT_AMBIENT_TEMP = 26;
    public static int MELT_HELLFIRE_THRESHOLD = 10;
    public static int BLAZE_BREEDING_RANGE = 3;

    @Override
    protected String getName() {
        return "addons.BetterWithRenewables";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled","Whether the Better With Renewables module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.", ENABLED);
        REDSTONE_SYNTHESIS = loadPropBool("RedstoneSynthesis","Allows redstone to be farbricated from concentrated hellfire and gold.", REDSTONE_SYNTHESIS);
        REDSTONE_SYNTHESIS_EARLY = loadPropBool("RedstoneSynthesisEarly","Allows redstone to be synthesized earlier to create Hibachis.", REDSTONE_SYNTHESIS_EARLY);
        REDSTONE_PER_SYNTHESIS = loadPropInt("RedstonePerSynthesis","How much redstone is obtained per bar of concentrated hellfire.", REDSTONE_PER_SYNTHESIS);
        HELLFIRE_EARLY = loadPropBool("HellfireEarly","Allows hellfire dust to be created earlier in the tech tree.", HELLFIRE_EARLY);
        BOILING_BUSHES = loadPropBool("BoilingBushes","Allows dead bushes to be created from oak saplings.", BOILING_BUSHES);
        WEAVING_WEBS = loadPropBool("WeavingWebs","Allows webs to be created from string and slimeballs.", WEAVING_WEBS);
        LAPIS_FROM_WOOL = loadPropBool("LapisFromWool","Allows lapis to be created from blue wool and clay.", LAPIS_FROM_WOOL);
        DIAMOND_SYNTHESIS = loadPropBool("DiamondSynthesis","Allows diamonds to be fabricated from ghast tears.", DIAMOND_SYNTHESIS);
        DIAMOND_RECOVERY = loadPropBool("DiamondRecovery","Allows diamond ingots to be taken apart into diamonds and iron ingots using strong alkaline.", DIAMOND_RECOVERY);
        GOLD_GRINDING = loadPropBool("GoldGrinding","Allows only tools and armor made from gold to be milled into nuggets at a lower efficiency than when melted in a Crucible.", GOLD_GRINDING);
        GOLD_PER_INGOT = loadPropInt("GoldPerIngot","Gold nuggets returned when grinding gold tools or armor in a millstone.", GOLD_PER_INGOT);
        NETHERRACK_SYNTHESIS = loadPropBool("NetherrackSynthesis","Allows netherrack to be farbricated from a usable medium, a hellborn plant and some residents from the nether.", NETHERRACK_SYNTHESIS);
        SOULSAND_INFUSION = loadPropBool("SoulsandInfusion","Allows netherrack to be fabricated from dung and experience.", SOULSAND_INFUSION);
        BLAZE_GOLEMS = loadPropBool("BlazeGolems","Allows blazes to be created from a golem-like shape with appropriate blocks.", BLAZE_GOLEMS);
        BLAZE_BREEDING = loadPropBool("BlazeBreeding","Allows blazes to replicate in fire when fed an appropriate item.", BLAZE_BREEDING);
        CROSSBREED_PLANTS = loadPropBool("PlantBreeding","Allows plants to be crossbreed from other plants.", CROSSBREED_PLANTS);
        CROSSBREED_ANIMALS = loadPropBool("AnimalBreeding","Allows animals to be crossbreed from other animals. Disgusting.", CROSSBREED_ANIMALS);
        QUARTZ_GROWING = loadPropBool("QuartzSynthesis","Allows quartz to be grown from silica in appropriate conditions.", QUARTZ_GROWING);
        DUNG_TO_DIRT = loadPropBool("DungToDirt","Allows dung to be turned into dirt by rinsing acids out.", DUNG_TO_DIRT);
        SAND_TO_CLAY = loadPropBool("SandToClay","Allows sand to be turned into clay by adding acidic substances.", SAND_TO_CLAY);
        MELT_HELLFIRE = loadPropBool("MeltHellfire","Allows Blocks of Hellfire to be melted into lava by proximity to it.", MELT_HELLFIRE);
        REDSTONE_BOILING = loadPropBool("BoilRedstone","Allows redstone to be 'boiled' into glowstone by exposure to focused sunlight.", REDSTONE_BOILING);
        EMERALD_PORTAL = loadPropBool("EmeraldPortal","Allows portals to be made from emerald blocks and sacrifice.", EMERALD_PORTAL);
        doesNotNeedRestart(() -> {
            DUNG_TO_DIRT_THRESHOLD = loadPropInt("DungToDirtThreshold","The chance for a block of dung to turn into dirt from rinsing. The chance is rand(n) < heat", DUNG_TO_DIRT_THRESHOLD);
            DUNG_TO_DIRT_AMBIENT_TEMP = loadPropInt("DungToDirtAmbientTemp","Amount of ambient temperature is added to the heat value.", DUNG_TO_DIRT_AMBIENT_TEMP);

            BLAZE_BREEDING_DELAY = loadPropInt("BlazeBreedingDelay","Delay between successfully breeding blazes.", BLAZE_BREEDING_DELAY);
            BLAZE_BREEDING_RANGE = loadPropInt("BlazeBreedingRange","Range in blocks to look for fire to birth new blazes in.", BLAZE_BREEDING_RANGE);

            REDSTONE_BOILING_CHANCE = loadPropDouble("BoilRedstoneChance","Chance for redstone to boil into glowstone.", REDSTONE_BOILING_CHANCE, 0, 1);
            QUARTZ_GROWING_THRESHOLD = loadPropInt("QuartzSynthesisThreshold","Affects the time sand piles must be cooked to make Quartz.", QUARTZ_GROWING_THRESHOLD);
            SOULSAND_INFUSION_THRESHOLD = loadPropInt("SoulsandInfusionThreshold","Amount of exp that must be pushed into a block of dung to create soulsand.", SOULSAND_INFUSION_THRESHOLD);
            MELT_HELLFIRE_THRESHOLD = loadPropInt("MeltHellfireThreshold","The chance for a block of hellfire to melt into a block of lava. The chance is rand(n) < adjacent_lava_blocks", MELT_HELLFIRE_THRESHOLD);
        });

    }

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
    }

    @Override
    public List<Interaction> getDependencies() {
        return Arrays.asList(new Interaction[]{ ModInteractions.bwm });
    }

    @Override
    public void preInit() {
        if(MELT_HELLFIRE)
            PatientiaHandler.addCustomBlock(BWMBlocks.AESTHETIC);
        if(CROSSBREED_PLANTS) {
            PatientiaHandler.addCustomBlock(BWMBlocks.FERTILE_FARMLAND);
            PatientiaHandler.addCustomBlock(BWMBlocks.PLANTER);
            PatientiaHandler.addCustomBlock(Blocks.SOUL_SAND);
            PatientiaHandler.addCustomBlock(Blocks.END_STONE);
            MinecraftForge.EVENT_BUS.register(new PlantCrossbreedHandler());
        }
        if(CROSSBREED_ANIMALS)
            MinecraftForge.EVENT_BUS.register(new AnimalCrossbreedHandler());
        if(REDSTONE_BOILING)
            MinecraftForge.EVENT_BUS.register(new RedstoneBoilHandler());
        if(SOULSAND_INFUSION)
            MinecraftForge.EVENT_BUS.register(new SoulSandHandler());
        if(EMERALD_PORTAL)
            MinecraftForge.EVENT_BUS.register(new PortalHandler());
        MinecraftForge.EVENT_BUS.register(new RenewablesHandler());
        RenewablesHandler.registerCapability();
    }

    @Override
    void init() {
        PlantCrossbreedHandler.initialize();
        AnimalCrossbreedHandler.initialize();

        OreDictionary.registerOre("listAllBlazeFoods",Items.COAL);
        OreDictionary.registerOre("listAllBlazeFoods",new ItemStack(Items.COAL,1,1)); //Charcoal
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COAL_DUST));
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL));
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL));

        OreDictionary.registerOre("pileSand",BWMItems.SAND_PILE);
        OreDictionary.registerOre("pileSand",BWMItems.RED_SAND_PILE);
        OreDictionary.registerOre("pileSand",ModItems.soulSandPile);

        int axeAmt = HCTools.changeAxeRecipe ? 2 : 3;

        //Tanning Leather with dung blocks
        //TODO: This is hacky.
        CauldronManager.getInstance().addRecipe(
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER),
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG,8),new Object[]{
                        ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER),
                        new ItemStack(BWMBlocks.AESTHETIC,1, BlockAesthetic.EnumType.DUNG.getMeta())});
        CauldronManager.getInstance().addRecipe(
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT,2),
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG,8),new Object[]{
                        ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT,2),
                        new ItemStack(BWMBlocks.AESTHETIC,1, BlockAesthetic.EnumType.DUNG.getMeta())});

        //Redstone synthesis
        ItemStack smallRedstone = new ItemStack(Items.REDSTONE, REDSTONE_PER_SYNTHESIS);
        ItemStack bigRedstone = new ItemStack(Items.REDSTONE, REDSTONE_PER_SYNTHESIS * 9);
        if(REDSTONE_SYNTHESIS) {
            StokedCrucibleRecipe smallRecipe = new StokedCrucibleRecipe(smallRedstone, ItemStack.EMPTY, new Object[]{new OreStack("nuggetGold", 1), new OreStack("ingotConcentratedHellfire", 1)});
            StokedCrucibleRecipe mediumRecipe = new StokedCrucibleRecipe(smallRedstone, new ItemStack(Items.GOLD_NUGGET,8), new Object[]{new OreStack("ingotGold", 1), new OreStack("ingotConcentratedHellfire", 1)});
            StokedCrucibleRecipe bigRecipe = new StokedCrucibleRecipe(bigRedstone, ItemStack.EMPTY, new Object[]{new OreStack("ingotGold", 1), new OreStack("ingotConcentratedHellfire", 9)});
            smallRecipe.setPriority(108);
            mediumRecipe.setPriority(109);
            bigRecipe.setPriority(110);
            StokedCrucibleManager.getInstance().addRecipe(smallRecipe);
            StokedCrucibleManager.getInstance().addRecipe(mediumRecipe);
            StokedCrucibleManager.getInstance().addRecipe(bigRecipe);
        }

        if(REDSTONE_SYNTHESIS_EARLY) {
            MillManager.getInstance().addRecipe(2, smallRedstone,new Object[]{new OreStack("nuggetGold", 3), new OreStack("ingotConcentratedHellfire", 1)});
            MillManager.getInstance().addRecipe(2, bigRedstone,new Object[]{new OreStack("ingotGold", 3), new OreStack("ingotConcentratedHellfire", 9)});
        }

        ItemStack hellfireDust = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST);
        if(HELLFIRE_EARLY)
        {
            MillRecipe withSawDust = new MillRecipe(2, hellfireDust.copy(),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST),new Object[]{new OreStack("dustNetherrack", 1),new OreStack("dustWood", 1)});
            MillRecipe withoutSawDust = new MillRecipe(2, hellfireDust.copy(),ItemStack.EMPTY,new Object[]{new OreStack("dustNetherrack", 1)});
            withSawDust.setPriority(11);
            withoutSawDust.setPriority(10);
            MillManager.getInstance().addRecipe(withSawDust);
            MillManager.getInstance().addRecipe(withoutSawDust);
        }

        if(BOILING_BUSHES)
            StokedCauldronManager.getInstance().addRecipe(new ItemStack(Blocks.DEADBUSH),new Object[]{new ItemStack(Blocks.SAPLING)});

        if(WEAVING_WEBS)
            CauldronManager.getInstance().addRecipe(new ItemStack(Blocks.WEB),new Object[]{new OreStack("string",3),new OreStack("slimeball",1)});

        if(DIAMOND_RECOVERY) {
            if(ModuleLoader.isFeatureEnabled(HCDiamond.class))
                addDiamondRecovery(new OreStack("ingotDiamond",1),1);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_SWORD),2);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_PICKAXE),3);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_AXE),axeAmt);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_SHOVEL),1);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_HOE),2);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_HELMET),5);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_CHESTPLATE),8);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_LEGGINGS),7);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_BOOTS),4);
            addDiamondRecovery(new ItemStack(ModItems.diamondSpade),2);
            addDiamondRecovery(new ItemStack(ModItems.diamondMatchPick),3);
            addDiamondRecovery(new ItemStack(ModItems.diamondMachete),4);
            addDiamondRecovery(new ItemStack(ModItems.diamondKukri),axeAmt+2);
            addDiamondRecovery(new ItemStack(ModItems.diamondCarpenterSaw),axeAmt+2);
            addDiamondRecovery(new ItemStack(ModItems.diamondMasonPick),4);
        }

        if(LAPIS_FROM_WOOL)
        {
            addLapisRinsing(EnumDyeColor.BLUE,1,EnumDyeColor.WHITE);
            addLapisRinsing(EnumDyeColor.LIGHT_BLUE,2,EnumDyeColor.WHITE);
            addLapisRinsing(EnumDyeColor.CYAN,2,EnumDyeColor.GREEN);
            addLapisRinsing(EnumDyeColor.PURPLE,2,EnumDyeColor.RED);
            addLapisRinsing(EnumDyeColor.MAGENTA,4,EnumDyeColor.RED,EnumDyeColor.PINK);
        }

        if(GOLD_GRINDING)
        {
            addGoldGrinding(new ItemStack(Items.GOLDEN_SWORD),2);
            addGoldGrinding(new ItemStack(Items.GOLDEN_PICKAXE),3);
            addGoldGrinding(new ItemStack(Items.GOLDEN_AXE),axeAmt);
            addGoldGrinding(new ItemStack(Items.GOLDEN_SHOVEL),1);
            addGoldGrinding(new ItemStack(Items.GOLDEN_HOE),2);
            addGoldGrinding(new ItemStack(Items.GOLDEN_HELMET),5);
            addGoldGrinding(new ItemStack(Items.GOLDEN_CHESTPLATE),8);
            addGoldGrinding(new ItemStack(Items.GOLDEN_LEGGINGS),7);
            addGoldGrinding(new ItemStack(Items.GOLDEN_BOOTS),4);
            addGoldGrinding(new ItemStack(ModItems.goldSpade),2);
            addGoldGrinding(new ItemStack(ModItems.goldMatchPick),3);
            addGoldGrinding(new ItemStack(ModItems.goldMachete),4);
            addGoldGrinding(new ItemStack(ModItems.goldKukri),axeAmt+2);
            addGoldGrinding(new ItemStack(ModItems.goldCarpenterSaw),axeAmt+2);
            addGoldGrinding(new ItemStack(ModItems.goldMasonPick),4);

        }

        if(NETHERRACK_SYNTHESIS)
        {
            ItemStack soulUrn = new ItemStack(BWMBlocks.URN, 1, BlockUrn.EnumType.FULL.getMeta());
            CauldronManager.getInstance().addRecipe(new ItemStack(Blocks.NETHERRACK,8),new Object[]{new ItemStack(Blocks.COBBLESTONE,8),new ItemStack(Items.NETHER_WART,8),soulUrn});
            CauldronManager.getInstance().addRecipe(
                    new ItemStack(Blocks.NETHERRACK),
                    ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST),
                    new Object[]{
                            new ItemStack(Blocks.COBBLESTONE),
                            new ItemStack(Items.NETHER_WART),
                            ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST)
                    });
        }

        if(DIAMOND_SYNTHESIS)
            StokedCrucibleManager.getInstance().addRecipe(new ItemStack(Items.DIAMOND), hellfireDust.copy(),new Object[]{new ItemStack(Items.GHAST_TEAR),new ItemStack(Items.DYE, 1, EnumDyeColor.CYAN.getDyeDamage()),new OreStack("dustNetherrack", 1)});

        if(QUARTZ_GROWING) {
            CauldronManager.getInstance().addRecipe(new QuartzCrystalRecipe(new ItemStack(Items.QUARTZ), ItemStack.EMPTY, new Object[]{new OreStack("pileSand",1)}));
        }
    }

    private void addDiamondRecovery(Object input, int output)
    {
        ItemStack ironReturn = ItemStack.EMPTY;
        if(ModuleLoader.isFeatureEnabled(HCDiamond.class))
            ironReturn = new ItemStack(Items.IRON_INGOT,output);

        StokedCauldronRecipe diamondRecipe = new StokedCauldronRecipe(new ItemStack(Items.DIAMOND,output),ironReturn,new Object[]{input,new OreStack("ingotConcentratedHellfire", output),new OreStack("dustPotash", output*8)});
        diamondRecipe.setPriority(110);
        StokedCauldronManager.getInstance().addRecipe(diamondRecipe);
    }

    private void addGoldGrinding(ItemStack input, int output)
    {
        int ingots = (output * GOLD_PER_INGOT) / 9;
        int nuggets = (output * GOLD_PER_INGOT) % 9;

        ItemStack output1 = new ItemStack(Items.GOLD_INGOT,ingots);
        ItemStack output2 = new ItemStack(Items.GOLD_NUGGET,nuggets);
        if(output1.isEmpty()) {
            output1 = output2;
            output2 = ItemStack.EMPTY;
        }
        MillManager.getInstance().addRecipe(0,output1,output2,new Object[]{input});
    }

    private void addLapisRinsing(EnumDyeColor input, int quantity, EnumDyeColor... outputs)
    {
        ItemStack wool1 = new ItemStack(Blocks.WOOL,(quantity * 8) / outputs.length,outputs[0].getMetadata());
        ItemStack wool2 = outputs.length == 1 ? ItemStack.EMPTY : new ItemStack(Blocks.WOOL,(quantity * 8) / outputs.length,outputs[1].getMetadata());

        LapisRinsingRecipe recipe = new LapisRinsingRecipe(wool1,wool2,new Object[]{new ItemStack(Blocks.WOOL,quantity * 8,input.getMetadata()),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP,quantity * 2)});
        StokedCauldronManager.getInstance().addRecipe(recipe);
    }
}
