package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.recipes.LapisRinsingRecipe;
import betterwithaddons.crafting.recipes.QuartzCrystalRecipe;
import betterwithaddons.handler.*;
import betterwithaddons.item.ModItems;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Arrays;
import java.util.List;

public class InteractionBWR extends Interaction {
    public static boolean ENABLED = true;
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

    public static boolean MELT_HELLFIRE = true;
    public static boolean DUNG_TO_DIRT = true;
    public static boolean SAND_TO_CLAY = true;
    public static boolean CROSSBREED_PLANTS = true;
    public static boolean CROSSBREED_ANIMALS = true;
    public static boolean REDSTONE_BOILING = true;
    public static boolean SOULSAND_INFUSION = true;
    public static boolean BLAZE_GOLEMS = true;
    public static boolean BLAZE_BREEDING = true;

    public static int GOLD_PER_INGOT = 1;
    public static int REDSTONE_PER_SYNTHESIS = 7;

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
        MinecraftForge.EVENT_BUS.register(new RenewablesHandler());
    }

    @Override
    void init() {
        PlantCrossbreedHandler.initialize();
        AnimalCrossbreedHandler.initialize();

        OreDictionary.registerOre("listAllBlazeFoods",Items.COAL);
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.COAL_DUST));
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CHARCOAL_DUST));
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL));
        OreDictionary.registerOre("listAllBlazeFoods",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL));

        //Tanning Leather with dung blocks
        //TODO: This is hacky.
        CauldronManager.getInstance().addRecipe(
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER),
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG,8),new Object[]{
                        ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER),
                        new ItemStack(ModBlocks.dung)});
        CauldronManager.getInstance().addRecipe(
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT,2),
                ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG,8),new Object[]{
                        ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER_CUT,2),
                        new ItemStack(ModBlocks.dung)});

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
            addDiamondRecovery(new OreStack("ingotDiamond",1),1);
            /*addDiamondRecovery(new ItemStack(Items.DIAMOND_SWORD),2);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_PICKAXE),3);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_AXE),3);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_SHOVEL),1);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_HOE),2);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_HELMET),5);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_CHESTPLATE),8);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_LEGGINGS),7);
            addDiamondRecovery(new ItemStack(Items.DIAMOND_BOOTS),4);*/
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
            addGoldGrinding(new ItemStack(Items.GOLDEN_AXE),3);
            addGoldGrinding(new ItemStack(Items.GOLDEN_SHOVEL),1);
            addGoldGrinding(new ItemStack(Items.GOLDEN_HOE),2);
            addGoldGrinding(new ItemStack(Items.GOLDEN_HELMET),5);
            addGoldGrinding(new ItemStack(Items.GOLDEN_CHESTPLATE),8);
            addGoldGrinding(new ItemStack(Items.GOLDEN_LEGGINGS),7);
            addGoldGrinding(new ItemStack(Items.GOLDEN_BOOTS),4);
        }

        if(NETHERRACK_SYNTHESIS)
        {
            ItemStack soulUrn = new ItemStack(BWMBlocks.URN, 1, BlockUrn.EnumUrnType.FULL.getMeta());
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
            CauldronManager.getInstance().addRecipe(new QuartzCrystalRecipe(new ItemStack(Items.QUARTZ), ItemStack.EMPTY, new Object[]{new ItemStack(BWMItems.SAND_PILE)}));
            CauldronManager.getInstance().addRecipe(new QuartzCrystalRecipe(new ItemStack(Items.QUARTZ), ItemStack.EMPTY, new Object[]{new ItemStack(ModItems.soulSandPile)}));
        }
    }

    private void addDiamondRecovery(Object input, int output)
    {
        StokedCauldronRecipe diamondRecipe = new StokedCauldronRecipe(new ItemStack(Items.DIAMOND,output),new ItemStack(Items.IRON_INGOT,output),new Object[]{input,new OreStack("ingotConcentratedHellfire", output),new OreStack("dustPotash", output*8)});
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
