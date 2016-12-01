package betterwithaddons.interaction;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.ArmorDecorateRecipe;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.item.ModItems;
import betterwithmods.api.BWMRecipeHelper;
import betterwithmods.items.ItemMaterial;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class InteractionEriottoMod implements IInteraction {
    public static boolean ENABLED = true;
    public static boolean GRASS_DROPS_SEEDS = true;

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
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
        CraftingManagerSandNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModBlocks.ironSand,1)},new ItemStack(Blocks.IRON_BLOCK,1),8);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(Blocks.IRON_BLOCK,1),new ItemStack(Blocks.SAND,8)},new ItemStack(ModBlocks.ironSand,1),0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{null,new ItemStack(ModItems.sashimi,3)},new ItemStack(Items.FISH, 1) ,0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModItems.fuguSac,1),new ItemStack(ModItems.preparedPuffer,3)},new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),0);
        CraftingManagerFireNet.getInstance().addRecipe(new ItemStack[]{ModItems.materialJapan.getMaterial("iron_scales",27)},new ItemStack(ModBlocks.ironSand,1),0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{null,ModItems.materialJapan.getMaterial("washi",9)},ModItems.materialJapan.getMaterial("mulberry_sheet"),0);

        OreDictionary.registerOre("logWood",ModBlocks.sakuraLog);
        OreDictionary.registerOre("logWood",ModBlocks.mulberryLog);
        OreDictionary.registerOre("plankWood",ModBlocks.sakuraPlanks);
        OreDictionary.registerOre("plankWood",ModBlocks.mulberryPlanks);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sakuraPlanks,4),new ItemStack(ModBlocks.sakuraLog));
        GameRegistry.addShapedRecipe(new ItemStack(Items.PAPER),"aaa",'a',new ItemStack(ModBlocks.mulberryLog));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.mulberryPlanks),new ItemStack(ModBlocks.mulberryLog));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("mulberry_sheet"),"aa","aa",'a',ModItems.materialJapan.getMaterial("mulberry_paste"));

        if(ModInteractions.bwm.isActive()) {
            ItemStack dung = ItemMaterial.getMaterial("dung",1);
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sakuraSapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.PINK.getDyeDamage()),dung);
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.mulberrySapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()),dung);
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bamboo), new ItemStack(Items.REEDS, 1), new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()),dung);
            BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.preparedCookedPuffer),new Object[]{new ItemStack(ModItems.preparedPuffer)});
            BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.rice),new Object[]{ModItems.materialJapan.getMaterial("soaked_rice")});
        }
        else {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sakuraSapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.PINK.getDyeDamage()));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.mulberrySapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bamboo), new ItemStack(Items.REEDS, 1), new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
        }

        GameRegistry.addSmelting(new ItemStack(ModItems.preparedCookedPuffer),new ItemStack(ModItems.preparedPuffer),0.35f);
        GameRegistry.addSmelting(ModItems.materialJapan.getMaterial("soaked_rice"),new ItemStack(ModItems.rice),0.35f);

        if(GRASS_DROPS_SEEDS) {
            MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rice), 2);
            MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rush), 2);
        }

        GameRegistry.addShapedRecipe(new ItemStack(ModItems.riceBowl),"r","r","b",'r',ModItems.rice,'b',new ItemStack(Items.BOWL));

        addFoldingRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu_fold_1"),ModItems.materialJapan.getMaterial("hocho_tetsu_heated"));
        addFoldingRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu_fold_2"),ModItems.materialJapan.getMaterial("hocho_tetsu_fold_1"));
        addFoldingRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu_finished"),ModItems.materialJapan.getMaterial("hocho_tetsu_fold_2"));

        addFoldingRecipe(ModItems.materialJapan.getMaterial("tamahagane_folded"),ModItems.materialJapan.getMaterial("tamahagane_heated"));
        addFoldingRecipe(ModItems.materialJapan.getMaterial("tamahagane_finished"),ModItems.materialJapan.getMaterial("tamahagane_reheated"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("tamahagane_wrapped")," w ","wtw"," w ",'t',ModItems.materialJapan.getMaterial("tamahagane_folded"),'w',ModItems.materialJapan.getMaterial("washi"));

        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("lamellar"),"iii","sls","iii",'i',ModItems.materialJapan.getMaterial("iron_scales"),'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STRING));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("paper_lamellar"),"pwp","sws","pwp",'w',ModItems.materialJapan.getMaterial("washi"),'p',new ItemStack(Items.PAPER),'s',new ItemStack(Items.STRING));

        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("tsuka"),"lll","ssp",'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STICK),'p',new ItemStack(ModBlocks.sakuraPlanks));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("half_katana_blade"),"t","h","t",'t',ModItems.materialJapan.getMaterial("tamahagane_finished"),'h',ModItems.materialJapan.getMaterial("hocho_tetsu_finished"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("ya_head",3),"t  ","hh ","ttt",'t',ModItems.materialJapan.getMaterial("tamahagane_finished"),'h',ModItems.materialJapan.getMaterial("hocho_tetsu_finished"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("yumi_top")," b","bs","bl",'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STICK),'b',ModItems.materialJapan.getMaterial("bamboo_slats"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("yumi_bottom"),"bl","bs"," b",'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STICK),'b',ModItems.materialJapan.getMaterial("bamboo_slats"));

        GameRegistry.addShapedRecipe(new ItemStack(ModItems.katana),"l","l","w",'l',ModItems.materialJapan.getMaterial("half_katana_blade"),'w',ModItems.materialJapan.getMaterial("tsuka"));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.wakizashi),"l","l","w",'l',ModItems.materialJapan.getMaterial("tamahagane_finished"),'w',ModItems.materialJapan.getMaterial("tsuka"));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.tanto),"l","w",'l',ModItems.materialJapan.getMaterial("tamahagane_finished"),'w',ModItems.materialJapan.getMaterial("tsuka"));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.shinai),"l","l","w",'l',ModItems.materialJapan.getMaterial("bamboo_slats"),'w',ModItems.materialJapan.getMaterial("tsuka"));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.ya,12),"h","l","f",'h',ModItems.materialJapan.getMaterial("ya_head"),'l',ModItems.materialJapan.getMaterial("bamboo_slats"),'f',new ItemStack(Items.FEATHER));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.yumi)," ns","l s"," us",'n',ModItems.materialJapan.getMaterial("yumi_top"),'u',ModItems.materialJapan.getMaterial("yumi_bottom"),'l',new ItemStack(Items.LEATHER),'s',new ItemStack(Items.STRING));

        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("helmet_undecorated"),"lll","l l",'l',ModItems.materialJapan.getMaterial("lamellar"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("chest_undecorated"),"l l","lll","lll",'l',ModItems.materialJapan.getMaterial("lamellar"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("legs_undecorated"),"lll","l l","l l",'l',ModItems.materialJapan.getMaterial("lamellar"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("boots_undecorated"),"l l","l l",'l',ModItems.materialJapan.getMaterial("lamellar"));

        addArmorFinishRecipe(new ItemStack(ModItems.samuraiHelm),ModItems.materialJapan.getMaterial("helmet_undecorated"));
        addArmorFinishRecipe(new ItemStack(ModItems.samuraiChestplate),ModItems.materialJapan.getMaterial("chest_undecorated"));
        addArmorFinishRecipe(new ItemStack(ModItems.samuraiLeggings),ModItems.materialJapan.getMaterial("legs_undecorated"));
        addArmorFinishRecipe(new ItemStack(ModItems.samuraiBoots),ModItems.materialJapan.getMaterial("boots_undecorated"));

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.nettedScreen),"bsb","sss","bsb",'s',new ItemStack(Items.STRING),'b',ModItems.materialJapan.getMaterial("bamboo_slats"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bambooSlats),"bb","bb",'b',ModItems.materialJapan.getMaterial("bamboo_slats"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.tatara),"idi","g g","ini",'i',new ItemStack(Items.IRON_INGOT),'g',new ItemStack(Items.GOLD_INGOT),'d',new ItemStack(Items.DIAMOND),'n',new ItemStack(Blocks.NETHERRACK));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.cherrybox,1,0),"pxp","x x","pxp",'p',new ItemStack(ModBlocks.sakuraPlanks),'x',new ItemStack(Blocks.IRON_BARS));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.cherrybox,1,1),"pxp","p p","ppp",'p',new ItemStack(ModBlocks.sakuraPlanks),'x',new ItemStack(Blocks.GLASS_PANE));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.shoji,4),"bwb","wbw","bwb",'b',ModItems.materialJapan.getMaterial("bamboo_slats"),'w',ModItems.materialJapan.getMaterial("washi"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fusuma,4),"bwb","wpw","bwb",'b',ModItems.materialJapan.getMaterial("bamboo_slats"),'w',ModItems.materialJapan.getMaterial("washi"),'p',new ItemStack(ModBlocks.sakuraPlanks));

        GameRegistry.addSmelting(ModItems.materialJapan.getMaterial("rice_stalk"),ModItems.materialJapan.getMaterial("rice_ash"),0.1f);

        CraftingManagerSoakingBox.instance().addWorkingRecipe(new ItemStack(ModBlocks.bamboo),ModItems.materialJapan.getMaterial("soaked_bamboo"));
        CraftingManagerSoakingBox.instance().addWorkingRecipe(ModItems.materialJapan.getMaterial("rice"),ModItems.materialJapan.getMaterial("soaked_rice"));
        CraftingManagerSoakingBox.instance().addWorkingRecipe(new ItemStack(ModBlocks.mulberryLog),ModItems.materialJapan.getMaterial("soaked_mulberry"));

        CraftingManagerDryingBox.instance().addWorkingRecipe(ModItems.materialJapan.getMaterial("rice_stalk"),ModItems.materialJapan.getMaterial("rice_hay"));
        CraftingManagerDryingBox.instance().addWorkingRecipe(ModItems.materialJapan.getMaterial("soaked_mulberry"),ModItems.materialJapan.getMaterial("mulberry_paste"));
        CraftingManagerDryingBox.instance().addWorkingRecipe(ModItems.materialJapan.getMaterial("soaked_bamboo"),ModItems.materialJapan.getMaterial("bamboo_slats"));

        CraftingManagerTatara.instance().addSmeltingRecipe(new ItemStack(ModBlocks.ironSand),new ItemStack(ModBlocks.kera));
        CraftingManagerTatara.instance().addSmeltingRecipe(ModItems.materialJapan.getMaterial("tamahagane"),ModItems.materialJapan.getMaterial("tamahagane_heated"));
        CraftingManagerTatara.instance().addSmeltingRecipe(ModItems.materialJapan.getMaterial("tamahagane_wrapped"),ModItems.materialJapan.getMaterial("tamahagane_reheated"));
        CraftingManagerTatara.instance().addSmeltingRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu"),ModItems.materialJapan.getMaterial("hocho_tetsu_heated"));
    }

    private void addArmorFinishRecipe(ItemStack out, ItemStack in)
    {
        //ItemStack gold = new ItemStack(Items.GOLD_NUGGET);
        //ItemStack dye = new ItemStack(Items.DYE,1,14);
        //ItemStack washi = ModItems.materialJapan.getMaterial("washi");
        //GameRegistry.addShapelessRecipe(out,gold,gold,gold,dye,in,dye,washi,washi,washi);
        GameRegistry.addRecipe(new ArmorDecorateRecipe(out,in));
    }

    private void addFoldingRecipe(ItemStack out, ItemStack in)
    {
        GameRegistry.addShapedRecipe(out,"t","s",'t',in,'s',new ItemStack(Items.STICK));
    }

    @Override
    public void postInit() {

    }
}
