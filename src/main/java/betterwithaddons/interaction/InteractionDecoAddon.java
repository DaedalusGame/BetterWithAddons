package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockWhiteBrick;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.module.gameplay.AnvilRecipes;
import betterwithmods.module.hardcore.needs.HCMovement;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class InteractionDecoAddon extends Interaction {
    public static boolean ENABLED = true;
    public static boolean WOOD_COLORING = true;

    public static boolean ALTERNATE_WROUGHT_BARS = true;

    public static boolean CHISEL_BRICKS_IN_ANVIL = true;
    public static boolean GLASS_PANE_REBALANCE = true;
    public static boolean GLASS_FURNACE = false;
    public static boolean CHEAPER_BOTTLES = true;
    public static boolean RECYCLE_BOTTLES = true;

    public InteractionDecoAddon()
    {
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
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        ConditionModule.MODULES.put("DecoAddon", this::isActive);
        ConditionModule.MODULES.put("ChiselBricksInAnvil", () -> CHISEL_BRICKS_IN_ANVIL);
        ConditionModule.MODULES.put("CheaperBottles", () -> CHEAPER_BOTTLES);
        if(CHEAPER_BOTTLES)
            BetterWithAddons.removeCraftingRecipe(new ItemStack(Items.GLASS_BOTTLE,3));
        if(CHISEL_BRICKS_IN_ANVIL)
            BetterWithAddons.removeCraftingRecipe(new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.CHISELED.getMetadata()));
    }

    @Override
    public void init() {
        MillManager.getInstance().addRecipe(0,ModItems.materialDeco.getMaterial("hemp_oil"), new Object[]{new ItemStack(BWMBlocks.HEMP, 1), new ItemStack(Items.GLASS_BOTTLE, 1)});
        CauldronManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("wood_bleach",4),new Object[]{ModItems.materialDeco.getMaterial("hemp_oil",4),new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())});
        CauldronManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("wood_stain",4),new Object[]{ModItems.materialDeco.getMaterial("hemp_oil",4),new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage())});

        //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.paperWall),"pwp","ppp","pwp",'p',new ItemStack(Items.PAPER),'w',new ItemStack(BWMBlocks.WOOD_MOULDING,1, OreDictionary.WILDCARD_VALUE));

        ItemStack chandelierLight = new ItemStack(Blocks.TORCH); //TODO: candles
        ItemStack ironLanternLight = ModItems.materialDeco.getMaterial("hemp_oil");
        ItemStack woodLanternLight = ModItems.materialDeco.getMaterial("hemp_oil"); //TODO: fireflies, candles

        //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.paperLantern),"pwp","wtw","pwp",'p',new ItemStack(Items.PAPER),'w',new ItemStack(BWMBlocks.WOOD_MOULDING,1, OreDictionary.WILDCARD_VALUE),'t',woodLanternLight);
        //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.wroughtLantern)," w ","wtw"," w ",'w',new ItemStack(ModBlocks.wroughtBars),'t',ironLanternLight);
        if(ALTERNATE_WROUGHT_BARS)
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"wrought_bars"),new ItemStack(ModBlocks.wroughtBars, 8), "bbbb", "bbbb", 'b', new ItemStack(Blocks.IRON_BARS));
        else
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"wrought_bars"),new ItemStack(ModBlocks.wroughtBars,10), "b b ", "bbbb", "b b ", "b b ", 'b', new ItemStack(Items.IRON_INGOT));
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"chandelier"),new ItemStack(ModBlocks.chandelier), " ss ", " bb ", "tbbt", "tbbt", 's', new ItemStack(Blocks.STONE),'b',new ItemStack(Items.GOLD_NUGGET),'t',chandelierLight);

        ItemStack whiteBrick = new ItemStack(ModBlocks.whiteBrick, 1, BlockWhiteBrick.EnumType.DEFAULT.getMetadata());
        ItemStack whiteBrick_mossy = new ItemStack(ModBlocks.whiteBrick, 1, BlockWhiteBrick.EnumType.MOSSY.getMetadata());
        ItemStack whiteBrick_cracked = new ItemStack(ModBlocks.whiteBrick, 1, BlockWhiteBrick.EnumType.CRACKED.getMetadata());

        //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.whiteBrick, 4, BlockWhiteBrick.EnumType.DEFAULT.getMetadata()),"bb","bb",'b',new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.WHITESTONE.getMeta()));
        if(CHISEL_BRICKS_IN_ANVIL) {
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"stone_brick_chiseled"),new ItemStack(Blocks.STONEBRICK, 3, BlockStoneBrick.EnumType.CHISELED.getMetadata()), "bbbb", "b  b", "b  b", "bbbb", 'b', new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.DEFAULT.getMetadata()));
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID,"white_brick_chiseled"),new ItemStack(ModBlocks.whiteBrick, 3, BlockWhiteBrick.EnumType.CHISELED.getMetadata()), "bbbb", "b  b", "b  b", "bbbb", 'b', new ItemStack(ModBlocks.whiteBrick, 1, BlockWhiteBrick.EnumType.DEFAULT.getMetadata()));
        }
        else
        {
            //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.whiteBrick, 2, BlockWhiteBrick.EnumType.CHISELED.getMetadata()),"b","b",'b',new ItemStack(ModBlocks.whiteBrick, 1, BlockWhiteBrick.EnumType.DEFAULT.getMetadata()));
        }
        FurnaceRecipes.instance().addSmeltingRecipe(whiteBrick,whiteBrick_cracked,0.1f);
        //GameRegistry.addShapelessRecipe(whiteBrick_mossy,whiteBrick,new ItemStack(Blocks.VINE,1));

        StokedCrucibleManager.getInstance().addRecipe(new ItemStack(ModBlocks.pavement),new ItemStack[]{new ItemStack(Blocks.GRAVEL), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE)});

        int glasspanein = GLASS_PANE_REBALANCE ? 2 : 8;
        int glassout = GLASS_PANE_REBALANCE ? 1 : 3;

        if(GLASS_PANE_REBALANCE) {
            modifyPaneRecipe();
            StokedCrucibleManager.getInstance().removeRecipe(new ItemStack(Blocks.GLASS,3),ItemStack.EMPTY,new ItemStack(Blocks.GLASS_PANE,8));
        }

        StokedCrucibleManager.getInstance().addRecipe(ModItems.materialDeco.getMaterial("glass_chunk"),new ItemStack[]{new ItemStack(BWMItems.SAND_PILE,1)});
        StokedCrucibleManager.getInstance().addRecipe(new ItemStack(Blocks.GLASS,1),new ItemStack[]{ModItems.materialDeco.getMaterial("glass_chunk",4)});
        if(GLASS_FURNACE)
        {
            GameRegistry.addSmelting(new ItemStack(BWMItems.SAND_PILE,1),ModItems.materialDeco.getMaterial("glass_chunk"),0.02f);
            GameRegistry.addSmelting(ModItems.materialDeco.getMaterial("glass_chunk",4),new ItemStack(Blocks.GLASS,1),0.05f);
        }
        //GameRegistry.addShapelessRecipe(ModItems.materialDeco.getMaterial("glass_chunk",4),new ItemStack(Blocks.GLASS,1));
        //if(CHEAPER_BOTTLES) {
            //BetterWithAddons.removeCraftingRecipe(new ItemStack(Items.GLASS_BOTTLE,3));
            //GameRegistry.addShapedRecipe(new ItemStack(Items.GLASS_BOTTLE,3)," # ","# #","###",'#',ModItems.materialDeco.getMaterial("glass_chunk",1));
        //}

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
        for(Iterator<IRecipe> craftingIterator = CraftingManager.REGISTRY.iterator(); craftingIterator.hasNext(); ) {
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
