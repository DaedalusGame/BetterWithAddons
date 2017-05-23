package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.item.ModItems;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InteractionDecoAddon implements IInteraction {
    public static boolean ENABLED = true;
    public static boolean WOOD_COLORING = true;

    public static boolean GLASS_PANE_REBALANCE = true;
    public static boolean GLASS_FURNACE = false;
    public static boolean CHEAPER_BOTTLES = true;
    public static boolean RECYCLE_BOTTLES = true;

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
        return Arrays.asList(new IInteraction[]{ ModInteractions.bwm });
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
        MillManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("hemp_oil"), new Object[]{new ItemStack(BWMBlocks.HEMP, 1), new ItemStack(Items.GLASS_BOTTLE, 1)});
        CauldronManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("wood_bleach",4),new Object[]{ModItems.materialDeco.getMaterial("hemp_oil",4),new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())});
        CauldronManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("wood_stain",4),new Object[]{ModItems.materialDeco.getMaterial("hemp_oil",4),new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage())});

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.paperWall),"pwp","ppp","pwp",'p',new ItemStack(Items.PAPER),'w',new ItemStack(BWMBlocks.WOOD_MOULDING,1, OreDictionary.WILDCARD_VALUE));

        ItemStack chandelierLight = new ItemStack(Blocks.TORCH); //TODO: candles
        ItemStack ironLanternLight = ModItems.materialDeco.getMaterial("hemp_oil");
        ItemStack woodLanternLight = ModItems.materialDeco.getMaterial("hemp_oil"); //TODO: fireflies, candles

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.paperLantern),"pwp","wtw","pwp",'p',new ItemStack(Items.PAPER),'w',new ItemStack(BWMBlocks.WOOD_MOULDING,1, OreDictionary.WILDCARD_VALUE),'t',woodLanternLight);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.wroughtLantern)," w ","wtw"," w ",'w',new ItemStack(ModBlocks.wroughtBars),'t',ironLanternLight);
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.wroughtBars,6),"bbb","bbb",'b',new ItemStack(Blocks.IRON_BARS)); //TODO: both anvil recipes
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.chandelier)," b ","tbt","tbt",'b',new ItemStack(Items.GOLD_NUGGET),'t',chandelierLight); //TODO: anvil recipe
        StokedCrucibleManager.getInstance().addRecipe(new ItemStack(ModBlocks.pavement),new ItemStack[]{new ItemStack(Blocks.GRAVEL), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE)});

        int glasspanein = GLASS_PANE_REBALANCE ? 2 : 8;
        int glassout = GLASS_PANE_REBALANCE ? 1 : 3;

        if(GLASS_PANE_REBALANCE) {
            modifyPaneRecipe();
            StokedCrucibleManager.getInstance().removeRecipe(new ItemStack(Blocks.GLASS,3),new ItemStack(Blocks.GLASS_PANE,8));
        }

        StokedCrucibleManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("glass_chunk"),new ItemStack[]{new ItemStack(BWMItems.SAND_PILE,1)});
        StokedCrucibleManager.getInstance().addRecipe(new ItemStack(Blocks.GLASS,1),new ItemStack[]{ModItems.materialDeco.getMaterial("glass_chunk",4)});
        if(GLASS_FURNACE)
        {
            GameRegistry.addSmelting(new ItemStack(BWMItems.SAND_PILE,1),ModItems.materialDeco.getMaterial("glass_chunk"),0.02f);
            GameRegistry.addSmelting(ModItems.materialDeco.getMaterial("glass_chunk",4),new ItemStack(Blocks.GLASS,1),0.05f);
        }
        GameRegistry.addShapelessRecipe(ModItems.materialDeco.getMaterial("glass_chunk",4),new ItemStack(Blocks.GLASS,1));
        if(CHEAPER_BOTTLES) {
            BetterWithAddons.removeCraftingRecipe(new ItemStack(Items.GLASS_BOTTLE,3));
            GameRegistry.addShapedRecipe(new ItemStack(Items.GLASS_BOTTLE,3)," # ","# #","###",'#',ModItems.materialDeco.getMaterial("glass_chunk",1));
        }

        if(RECYCLE_BOTTLES)
            StokedCrucibleManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("glass_chunk",CHEAPER_BOTTLES ? 2 : 4),new ItemStack[]{new ItemStack(Items.GLASS_BOTTLE,1)});

        StokedCrucibleManager.getInstance().addRecipe(new ItemStack(Blocks.GLASS,glassout),new ItemStack[]{new ItemStack(Blocks.GLASS_PANE,glasspanein)});
        EnumDyeColor[] dyes = EnumDyeColor.values();
        int len = dyes.length;

        for (int i = 0; i < len; ++i) {
            EnumDyeColor dye = dyes[i];
            ItemStack glass = new ItemStack(Blocks.STAINED_GLASS, glassout, dye.getMetadata());
            ItemStack glasspane = new ItemStack(Blocks.STAINED_GLASS_PANE, glasspanein, dye.getMetadata());
            StokedCrucibleManager.getInstance().addRecipe(glass,new ItemStack[]{glasspane});
        }

        if(WOOD_COLORING) {
            ItemStack birch = new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.BIRCH.getMetadata());
            ItemStack oak = new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.OAK.getMetadata());
            ItemStack jungle_wood = new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.JUNGLE.getMetadata());
            ItemStack acacia = new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.ACACIA.getMetadata());
            ItemStack spruce = new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.SPRUCE.getMetadata());
            ItemStack darkoak = new ItemStack(Blocks.PLANKS, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata());

            addStainingRecipe(birch, oak);
            addStainingRecipe(oak, jungle_wood);
            addStainingRecipe(jungle_wood, acacia);
            addStainingRecipe(acacia, spruce);
            addStainingRecipe(spruce, darkoak);
        }
    }

    @Override
    public void postInit() {
    }

    public void modifyPaneRecipe()
    {
        List<IRecipe> craftingList = CraftingManager.getInstance().getRecipeList();
        for(Iterator<IRecipe> craftingIterator = craftingList.iterator(); craftingIterator.hasNext(); ) {
            IRecipe recipe = craftingIterator.next();
            ItemStack output = recipe.getRecipeOutput();
            Block block = Block.getBlockFromItem(output.getItem());
            if(block == Blocks.GLASS_PANE || block == Blocks.STAINED_GLASS_PANE) {
                output.setCount((output.getCount() * 3) / 4);
            }
        }
    }

    public void addStainingRecipe(ItemStack lighter, ItemStack darker)
    {
        CauldronManager.getInstance().addRecipe(darker,new ItemStack(Items.GLASS_BOTTLE,1),new Object[]{ModItems.materialDeco.getMaterial("wood_stain"),lighter});
        CauldronManager.getInstance().addRecipe(lighter,new ItemStack(Items.GLASS_BOTTLE,1),new Object[]{ModItems.materialDeco.getMaterial("wood_bleach"),darker});
    }
}
