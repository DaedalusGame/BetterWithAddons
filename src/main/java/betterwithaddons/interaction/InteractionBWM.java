package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.handler.ButcherHandler;
import betterwithaddons.handler.FallingPlatformHandler;
import betterwithaddons.item.ModItems;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.List;

public class InteractionBWM extends Interaction {
    final String modid = "betterwithmods";
    public static boolean ENABLED = true;
    public static boolean MILL_CLAY = true;
    public static boolean CHORUS_IN_CAULDRON = true;
    public static boolean BUTCHER_BLOCKS = true;
    public static boolean FALLING_PLATFORMS = false;

    @Override
    public boolean isActive() {
        return ENABLED && Loader.isModLoaded(modid);
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
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
        if(BUTCHER_BLOCKS)
            MinecraftForge.EVENT_BUS.register(new ButcherHandler());
        if(FALLING_PLATFORMS)
            MinecraftForge.EVENT_BUS.register(new FallingPlatformHandler());
    }

    @Override
    public void init() {
        if(!isActive())
            return;

        BlockBUD.addBlacklistBlock(ModBlocks.pcbwire);

        ItemStack arrowhead = ModItems.material.getMaterial("arrowhead");
        ItemStack haft = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT);
        ItemStack string = new ItemStack(BWMBlocks.ROPE);
        ItemStack feather = new ItemStack(Items.FEATHER);
        String oreIronIngot = "ingotIron";
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.material.getMaterial("arrowhead")," o ","ooo","o o",'o',"nuggetSoulforgedSteel"));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.greatarrow,1),"a","b","c",'a',arrowhead,'b',haft,'c',feather);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatbow,1)," bc","b c"," bc",'b',haft,'c',string));
        StokedCauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.pcbblock),ItemStack.EMPTY,new Object[] { new ItemStack(Items.FERMENTED_SPIDER_EYE), new ItemStack(Blocks.STONEBRICK), "dustPotash" });
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedBeetroot),ItemStack.EMPTY,new Object[] { new ItemStack(Items.BEETROOT) });
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedCarrot),ItemStack.EMPTY,new Object[] { new ItemStack(Items.CARROT) });
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedPotato),ItemStack.EMPTY,new Object[] { new ItemStack(Items.POTATO) });
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedEgg),ItemStack.EMPTY,new Object[] { new ItemStack(Items.EGG) });
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedClownfish),ItemStack.EMPTY,new Object[] { new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()) });
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedPuffer),ItemStack.EMPTY,new Object[] { new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()) });

        CauldronManager.getInstance().addRecipe(ModItems.material.getMaterial("bone_ingot"),ItemStack.EMPTY,new Object[] { new ItemStack(Items.BONE,2),new ItemStack(Items.DYE,8,15) });
        CauldronManager.getInstance().addRecipe(ModItems.material.getMaterial("midori_popped"),ItemStack.EMPTY,new Object[] { ModItems.material.getMaterial("midori") });
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.meatballs),ItemStack.EMPTY,new Object[] { new ItemStack(ModItems.groundMeat,3) });
        MillManager.getInstance().addRecipe(0,new ItemStack(ModItems.groundMeat,3),ItemStack.EMPTY,new Object[] { new ItemStack(Items.BEEF) });
        MillManager.getInstance().addRecipe(0,new ItemStack(ModItems.groundMeat,2),ItemStack.EMPTY,new Object[] { new ItemStack(Items.MUTTON) });
        MillManager.getInstance().addRecipe(0,new ItemStack(ModItems.groundMeat,1),ItemStack.EMPTY,new Object[] { new ItemStack(Items.CHICKEN) });
        MillManager.getInstance().addRecipe(0,new ItemStack(ModItems.groundMeat,3),ItemStack.EMPTY,new Object[] { new ItemStack(Items.PORKCHOP) });
        MillManager.getInstance().addRecipe(0,new ItemStack(ModItems.groundMeat,1),ItemStack.EMPTY,new Object[] { new ItemStack(Items.RABBIT) });

        MillManager.getInstance().addRecipe(0,new ItemStack(ModBlocks.worldScale,1),ItemStack.EMPTY,new Object[] { new ItemStack(ModBlocks.worldScaleOre,1,1) });

        //Bark
        ModBlocks.mulberryLog.barkStack = ModItems.materialJapan.getMaterial("bark_mulberry");
        ModBlocks.sakuraLog.barkStack = ModItems.materialJapan.getMaterial("bark_sakura");

        //Thorn Vines
        ItemStack rosebush = new ItemStack(Blocks.DOUBLE_PLANT, 4, BlockDoublePlant.EnumPlantType.ROSE.getMeta());
        ItemStack thornrose = ModItems.material.getMaterial("thornrose",2);
        ItemStack soulurn = new ItemStack(BWMBlocks.URN,1,BlockUrn.EnumUrnType.FULL.getMeta());
        ItemStack cactus = new ItemStack(Blocks.CACTUS,1);
        ItemStack dung = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG,1);
        CauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.thornrose),ItemStack.EMPTY,new Object[] {cactus,rosebush,dung,soulurn});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.thornrose),ItemStack.EMPTY,new Object[] {cactus,thornrose,dung,soulurn});

        //Alicio Sapling
        ItemStack wheat = new ItemStack(Items.WHEAT,16);
        ItemStack flesh = new ItemStack(Items.ROTTEN_FLESH,4);
        ItemStack red = new ItemStack(Items.DYE,8,EnumDyeColor.RED.getDyeDamage());
        ItemStack tree = new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.BIRCH.getMetadata());
        CauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.luretreeSapling),ItemStack.EMPTY,new Object[] {tree,wheat,red,flesh});

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.chute,1),"s s"," p ","mgm",'s',new ItemStack(BWMBlocks.WOOD_SIDING),'m',new ItemStack(BWMBlocks.WOOD_MOULDING),'g',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR),'p',new ItemStack(Blocks.WOODEN_PRESSURE_PLATE));

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling,1,0),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.OAK.getMetadata()),new ItemStack(BWMBlocks.URN,1,BlockUrn.EnumUrnType.FULL.getMeta()));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling,1,1),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.BIRCH.getMetadata()),new ItemStack(BWMBlocks.URN,1,BlockUrn.EnumUrnType.FULL.getMeta()));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling,1,2),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.SPRUCE.getMetadata()),new ItemStack(BWMBlocks.URN,1,BlockUrn.EnumUrnType.FULL.getMeta()));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling,1,3),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.JUNGLE.getMetadata()),new ItemStack(BWMBlocks.URN,1,BlockUrn.EnumUrnType.FULL.getMeta()));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling,1,4),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.ACACIA.getMetadata()),new ItemStack(BWMBlocks.URN,1,BlockUrn.EnumUrnType.FULL.getMeta()));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling,1,5),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.DARK_OAK.getMetadata()),new ItemStack(BWMBlocks.URN,1,BlockUrn.EnumUrnType.FULL.getMeta()));


        if(MILL_CLAY) {
            MillManager.getInstance().addRecipe(0,new ItemStack(Items.BRICK, 4),ItemStack.EMPTY,new Object[] { new ItemStack(Blocks.HARDENED_CLAY, 1) });

            EnumDyeColor[] dyes = EnumDyeColor.values();
            int len = dyes.length;

            for (int i = 0; i < len; ++i) {
                EnumDyeColor dye = dyes[i];
                ItemStack brick = new ItemStack(ModItems.stainedBrick, 1, dye.getMetadata());
                MillManager.getInstance().addRecipe(0,new ItemStack(ModItems.stainedBrick, 4, dye.getMetadata()), ItemStack.EMPTY, new Object[]{new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata())});
                GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.coloredBrick, 1, dye.getMetadata()), "bb", "bb", 'b', brick);
            }
        }

        if(!CHORUS_IN_CAULDRON)
            GameRegistry.addSmelting(ModItems.material.getMaterial("midori"),ModItems.material.getMaterial("midori_popped"),0.1f);

        fixRecipes();
    }

    @Override
    public void postInit() {
        //Fixes baked stuff showing up in the cauldron
        removeCauldronRecipe(new ItemStack(Items.BAKED_POTATO));
        removeCauldronRecipe(new ItemStack(ModItems.bakedCarrot));
        removeCauldronRecipe(new ItemStack(ModItems.bakedBeetroot));

        if(CHORUS_IN_CAULDRON)
            BetterWithAddons.instance.removeSmeltingRecipe(new ItemStack(Items.CHORUS_FRUIT_POPPED));
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
        small.setCount(9);
        GameRegistry.addRecipe(new ShapelessOreRecipe(small, oreBig));
    }

    private static void removeCauldronRecipe(ItemStack output)
    {
        CauldronManager.getInstance().getRecipes().removeIf(r -> r.getOutput().isItemEqual(output));
    }
}
