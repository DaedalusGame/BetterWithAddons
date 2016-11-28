package betterwithaddons.interaction;

import betterwithaddons.item.ModItems;
import betterwithmods.BWMBlocks;
import betterwithmods.api.BWMRecipeHelper;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class InteractionDecoAddon implements IInteraction {
    public static boolean ENABLED = true;
    public static boolean WOOD_COLORING = true;

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
        BWMRecipeHelper.addMillRecipe(ModItems.materialDeco.getMaterial("hemp_oil"),new Object[]{new ItemStack(BWMBlocks.HEMP,1),new ItemStack(Items.GLASS_BOTTLE,1)});
        BWMRecipeHelper.addCauldronRecipe(ModItems.materialDeco.getMaterial("wood_bleach",4),new Object[]{ModItems.materialDeco.getMaterial("hemp_oil",4),new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage())});
        BWMRecipeHelper.addCauldronRecipe(ModItems.materialDeco.getMaterial("wood_stain",4),new Object[]{ModItems.materialDeco.getMaterial("hemp_oil",4),new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage())});

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

    public void addStainingRecipe(ItemStack lighter, ItemStack darker)
    {
        BWMRecipeHelper.addCauldronRecipe(darker,new ItemStack(Items.GLASS_BOTTLE,1),new Object[]{ModItems.materialDeco.getMaterial("wood_stain"),lighter});
        BWMRecipeHelper.addCauldronRecipe(lighter,new ItemStack(Items.GLASS_BOTTLE,1),new Object[]{ModItems.materialDeco.getMaterial("wood_bleach"),darker});
    }
}
