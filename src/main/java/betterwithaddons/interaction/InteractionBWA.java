package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class InteractionBWA implements IInteraction {
    public static boolean STONEBRICKS_NEED_SMELTING = false;
    public static int RADIUS = 6;
    public static int MAXCHARGE = 600;
    public static int MAXFOOD = 5000;
    public static int FOODGLOWSTONE = 450;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setEnabled(boolean active) {

    }

    @Override
    public List<IInteraction> getDependencies() {
        return null;
    }

    @Override
    public List<IInteraction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
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

    @Override
    public void postInit() {

    }
}
