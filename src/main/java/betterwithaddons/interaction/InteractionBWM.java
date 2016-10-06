package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.item.ModItems;
import betterwithmods.api.BWMRecipeHelper;
import betterwithmods.craft.SawInteraction;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class InteractionBWM implements IInteraction {
    final String modid = "betterwithmods";

    @Override
    public boolean isActive() {
        return Loader.isModLoaded(modid);
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        if(!isActive())
            return;

        ItemStack arrowhead = ModItems.material.getMaterial("arrowhead");
        ItemStack haft = InteractionHelper.findItem(modid,"material",1,38);
        ItemStack glue = InteractionHelper.findItem(modid,"material",1,12);
        ItemStack string = InteractionHelper.findItem(modid,"rope",1,0);
        ItemStack feather = new ItemStack(Items.FEATHER);
        ItemStack bow = new ItemStack(Items.BOW);
        String oreIronIngot = "ingotIron";
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.material.getMaterial("arrowhead")," o ","ooo","o o",'o',"nuggetSoulforgedSteel"));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.greatarrow,1),"a","b","c",'a',arrowhead,'b',haft,'c',feather);
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatbow,1),"bac","ed ","bac",'a',oreIronIngot,'b',haft,'c',string,'d',bow,'e',glue));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatbow,1)," bc","b c"," bc",'b',haft,'c',string));
        BWMRecipeHelper.addStokedCauldronRecipe(new ItemStack(ModBlocks.pcbblock),null,new Object[] { new ItemStack(Items.FERMENTED_SPIDER_EYE), new ItemStack(Blocks.STONEBRICK), "dustPotash" });
        BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.cookedBeetroot),null,new Object[] { new ItemStack(Items.BEETROOT) });
        BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.cookedCarrot),null,new Object[] { new ItemStack(Items.CARROT) });
        BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.cookedPotato),null,new Object[] { new ItemStack(Items.POTATO) });
        BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.cookedEgg),null,new Object[] { new ItemStack(Items.EGG) });
        BWMRecipeHelper.addCauldronRecipe(ModItems.material.getMaterial("bone_ingot"),null,new Object[] { new ItemStack(Items.BONE,2),new ItemStack(Items.DYE,8,15) });
        BWMRecipeHelper.addCauldronRecipe(ModItems.material.getMaterial("midori_popped"),null,new Object[] { ModItems.material.getMaterial("midori") });
        BWMRecipeHelper.addCauldronRecipe(new ItemStack(Items.CHORUS_FRUIT_POPPED),null,new Object[] { new ItemStack(Items.CHORUS_FRUIT,1) });
        BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.meatballs),null,new Object[] { new ItemStack(ModItems.groundMeat,3) });
        BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.chocolate),new ItemStack(Items.BUCKET),new Object[] { new ItemStack(Items.DYE,1,3),new ItemStack(Items.SUGAR),new ItemStack(Items.MILK_BUCKET) });
        BWMRecipeHelper.addMillRecipe(new ItemStack(ModItems.groundMeat,3),null,new Object[] { new ItemStack(Items.BEEF) });
        BWMRecipeHelper.addMillRecipe(new ItemStack(ModItems.groundMeat,2),null,new Object[] { new ItemStack(Items.MUTTON) });
        BWMRecipeHelper.addMillRecipe(new ItemStack(ModItems.groundMeat,1),null,new Object[] { new ItemStack(Items.CHICKEN) });
        BWMRecipeHelper.addMillRecipe(new ItemStack(ModItems.groundMeat,3),null,new Object[] { new ItemStack(Items.PORKCHOP) });
        BWMRecipeHelper.addMillRecipe(new ItemStack(ModItems.groundMeat,1),null,new Object[] { new ItemStack(Items.RABBIT) });

        BWMRecipeHelper.addMillRecipe(new ItemStack(ModBlocks.worldScale,1),null,new Object[] { new ItemStack(ModBlocks.worldScaleOre,1,1) });

        EnumDyeColor[] dyes = EnumDyeColor.values();
        int len = dyes.length;

        for(int i = 0; i < len; ++i) {
            EnumDyeColor dye = dyes[i];
            ItemStack brick = new ItemStack(ModItems.stainedBrick, 1, dye.getMetadata());
            BWMRecipeHelper.addMillRecipe(new ItemStack(ModItems.stainedBrick, 4, dye.getMetadata()),null,new Object[] { new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata()) });
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.coloredBrick, 1, dye.getMetadata()),"bb","bb",'b',brick);
        }

        BetterWithAddons.instance.removeSmeltingRecipe(new ItemStack(Items.CHORUS_FRUIT_POPPED));

        fixRecipes();
    }

    @Override
    public void postInit() {
    }

    public void fixRecipes()
    {
        /*ItemStack ingotSoulforgedSteel = InteractionHelper.findItem(modid,"material",1,14);
        ItemStack ingotIron = new ItemStack(Items.IRON_INGOT,1);
        ItemStack nuggedSoulforgedSteel = InteractionHelper.findItem(modid,"material",9,31);
        ItemStack nuggetIron = InteractionHelper.findItem(modid,"material",9,30);
        registerCompressRecipe(nuggedSoulforgedSteel,ingotSoulforgedSteel,"nuggetSoulforgedSteel","ingotSoulforgedSteel");
        registerCompressRecipe(nuggetIron,ingotIron,"nuggetIron","ingotIron");*/
    }

    private static void registerCompressRecipe(ItemStack small, ItemStack big, String oreSmall, String oreBig) {
        // ingot -> block
        GameRegistry.addRecipe(new ShapedOreRecipe(big, "###", "###", "###", '#', oreSmall));
        // block -> 9 ingot
        small = small.copy();
        small.stackSize = 9;
        GameRegistry.addRecipe(new ShapelessOreRecipe(small, oreBig));
    }
}
