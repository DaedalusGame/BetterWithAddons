package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockWhiteBrick;
import betterwithaddons.block.BlockWhiteBrick.EnumType;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.gameplay.AnvilRecipes;
import betterwithmods.module.tweaks.MossGeneration;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
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
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;

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

    @Override
    protected String getName() {
        return "addons.DecoAddon";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled", "Whether the Deco Addon module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.", ENABLED);
        WOOD_COLORING = loadPropBool("WoodColoring", "Vanilla planks can be bleached or stained.", WOOD_COLORING);
        ALTERNATE_WROUGHT_BARS = loadPropBool("AlternateWroughtBars", "Wrought bars are made at a ratio of 1 iron ingot per bar instead of 1/2 an iron ingot per bar.", ALTERNATE_WROUGHT_BARS);
        CHISEL_BRICKS_IN_ANVIL = loadPropBool("ChiselBricksInAnvil", "Chiseled Stone Bricks can only be crafted on a Steel Anvil.", CHISEL_BRICKS_IN_ANVIL);
        GLASS_PANE_REBALANCE = loadPropBool("GlassPaneRebalance", "Glass becomes neatly divisable into two glass panes per block.", GLASS_PANE_REBALANCE);
        GLASS_FURNACE = loadPropBool("GlassFurnace", "Glass chunks can be smelted in a furnace.", GLASS_FURNACE);
        CHEAPER_BOTTLES = loadPropBool("CheaperBottles", "Glass bottles are made from half as much glass as normal.", CHEAPER_BOTTLES);
        RECYCLE_BOTTLES = loadPropBool("RecycleBottles", "Glass bottles can melted into chunks in a crucible. This allows you to make glass from a witch farm.", RECYCLE_BOTTLES);

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
        return Arrays.asList(new Interaction[]{ModInteractions.bwm});
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
        if (CHEAPER_BOTTLES)
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation("minecraft", "glass_bottle"));
        if (CHISEL_BRICKS_IN_ANVIL)
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation("minecraft", "chiseled_stonebrick"));
    }

    @Override
    public void init() {
        BWRegistry.MILLSTONE.addMillRecipe(Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(BWMBlocks.HEMP, 1)),Ingredient.fromItem(Items.GLASS_BOTTLE)),Lists.newArrayList(ModItems.materialDeco.getMaterial("hemp_oil")));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(ModItems.materialDeco.getMaterial("hemp_oil", 4)),new OreIngredient("dyeWhite")),Lists.newArrayList(ModItems.materialDeco.getMaterial("wood_bleach", 4)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(ModItems.materialDeco.getMaterial("hemp_oil", 4)),new OreIngredient("dyeBlack")),Lists.newArrayList(ModItems.materialDeco.getMaterial("wood_stain", 4)));

        ItemStack chandelierLight = new ItemStack(Blocks.TORCH); //TODO: candles
        ItemStack ironLanternLight = ModItems.materialDeco.getMaterial("hemp_oil");
        ItemStack woodLanternLight = ModItems.materialDeco.getMaterial("hemp_oil"); //TODO: fireflies, candles

         if (ALTERNATE_WROUGHT_BARS)
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID, "wrought_bars"), new ItemStack(ModBlocks.wroughtBars, 8), "bbbb", "bbbb", 'b', new ItemStack(Blocks.IRON_BARS));
        else
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID, "wrought_bars"), new ItemStack(ModBlocks.wroughtBars, 10), "b b ", "bbbb", "b b ", "b b ", 'b', "ingotIron");
        AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID, "chandelier"), new ItemStack(ModBlocks.chandelier), " ss ", " bb ", "tbbt", "tbbt", 's', "stone", 'b', "nuggetGold", 't', "blockCandle");

        ItemStack whiteBrick = new ItemStack(ModBlocks.whiteBrick, 1, EnumType.DEFAULT.getMetadata());
        ItemStack whiteBrick_mossy = new ItemStack(ModBlocks.whiteBrick, 1, EnumType.MOSSY.getMetadata());
        ItemStack whiteBrick_cracked = new ItemStack(ModBlocks.whiteBrick, 1, EnumType.CRACKED.getMetadata());

        MossGeneration.addBlockConversion(ModBlocks.whiteBrick, ModBlocks.whiteBrick.getDefaultState().withProperty(BlockWhiteBrick.VARIANT, EnumType.MOSSY));

        if (CHISEL_BRICKS_IN_ANVIL) {
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID, "stone_brick_chiseled"), new ItemStack(Blocks.STONEBRICK, 3, BlockStoneBrick.EnumType.CHISELED.getMetadata()), "bbbb", "b  b", "b  b", "bbbb", 'b', new ItemStack(Blocks.STONEBRICK, 1, BlockStoneBrick.EnumType.DEFAULT.getMetadata()));
            AnvilRecipes.addSteelShapedRecipe(new ResourceLocation(Reference.MOD_ID, "white_brick_chiseled"), new ItemStack(ModBlocks.whiteBrick, 3, EnumType.CHISELED.getMetadata()), "bbbb", "b  b", "b  b", "bbbb", 'b', new ItemStack(ModBlocks.whiteBrick, 1, EnumType.DEFAULT.getMetadata()));
        }
        FurnaceRecipes.instance().addSmeltingRecipe(whiteBrick, whiteBrick_cracked, 0.1f);

        BWRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(Ingredient.fromStacks(new ItemStack(Blocks.GRAVEL)),Ingredient.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE))),Lists.newArrayList(new ItemStack(ModBlocks.pavement)));

        int glasspanein = GLASS_PANE_REBALANCE ? 2 : 8;
        int glassout = GLASS_PANE_REBALANCE ? 1 : 3;

        if (GLASS_PANE_REBALANCE) {
            modifyPaneRecipe();
            BWRegistry.CRUCIBLE.remove(BWRegistry.CRUCIBLE.getRecipes().stream().filter(recipe -> recipe.getOutputs().stream().anyMatch(stack -> stack.getItem() == ItemMaterial.getItemFromBlock(Blocks.GLASS) && stack.getCount() == 3)).findFirst().orElse(null));
        }

        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(BWMItems.SAND_PILE, 1),ModItems.materialDeco.getMaterial("glass_chunk"));
        BWRegistry.CRUCIBLE.addStokedRecipe(ModItems.materialDeco.getMaterial("glass_chunk", 4),new ItemStack(Blocks.GLASS, 1));
        if (GLASS_FURNACE) {
            GameRegistry.addSmelting(new ItemStack(BWMItems.SAND_PILE, 1), ModItems.materialDeco.getMaterial("glass_chunk"), 0.02f);
            GameRegistry.addSmelting(ModItems.materialDeco.getMaterial("glass_chunk", 4), new ItemStack(Blocks.GLASS, 1), 0.05f);
        }

        if (RECYCLE_BOTTLES)
            BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Items.GLASS_BOTTLE, 1),ModItems.materialDeco.getMaterial("glass_chunk", CHEAPER_BOTTLES ? 2 : 4));

        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(Blocks.GLASS_PANE, glasspanein),new ItemStack(Blocks.GLASS, glassout));
        EnumDyeColor[] dyes = EnumDyeColor.values();
        int len = dyes.length;

        for (int i = 0; i < len; ++i) {
            EnumDyeColor dye = dyes[i];
            ItemStack glass = new ItemStack(Blocks.STAINED_GLASS, glassout, dye.getMetadata());
            ItemStack glasspane = new ItemStack(Blocks.STAINED_GLASS_PANE, glasspanein, dye.getMetadata());
            BWRegistry.CRUCIBLE.addStokedRecipe(glasspane,glass);
        }

        if (WOOD_COLORING) {
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

    public void modifyPaneRecipe() {
        for (Iterator<IRecipe> craftingIterator = CraftingManager.REGISTRY.iterator(); craftingIterator.hasNext(); ) {
            IRecipe recipe = craftingIterator.next();
            ItemStack output = recipe.getRecipeOutput();
            Block block = Block.getBlockFromItem(output.getItem());
            if (block == Blocks.GLASS_PANE || block == Blocks.STAINED_GLASS_PANE) {
                output.setCount((output.getCount() * 3) / 4);
            }
        }
    }

    public void addStainingRecipe(ItemStack lighter, ItemStack darker) {
        ItemStack emptyBottle = new ItemStack(Items.GLASS_BOTTLE, 1);
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(lighter), Ingredient.fromStacks(ModItems.materialDeco.getMaterial("wood_stain"))),Lists.newArrayList(darker, emptyBottle));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(darker), Ingredient.fromStacks(ModItems.materialDeco.getMaterial("wood_bleach"))),Lists.newArrayList(lighter, emptyBottle));
    }
}
