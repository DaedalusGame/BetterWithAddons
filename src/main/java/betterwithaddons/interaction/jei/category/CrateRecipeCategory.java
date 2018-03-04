package betterwithaddons.interaction.jei.category;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.lib.Reference;
import betterwithmods.common.blocks.mechanical.BlockMechMachines;
import betterwithmods.module.compat.jei.wrapper.BulkRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class CrateRecipeCategory extends BlankRecipeCategory<BulkRecipeWrapper> {

    public static final String UID = "bwa.crating";
    @Nonnull
    private final IDrawable background;

    public CrateRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(getBackgroundResource(), 0, 0, 82, 54);
    }

    private ResourceLocation getBackgroundResource() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/spindle.png");
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.crating.name");
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        super.drawExtras(minecraft);

        String text = Translator.translateToLocal("inv.crating.throw.name");
        int x = 41 - minecraft.fontRenderer.getStringWidth(text)/2;
        minecraft.fontRenderer.drawString(text,x,0, Color.gray.getRGB());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BulkRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 4, 18);
        guiItemStacks.init(1, false, 60, 18);
        guiItemStacks.init(2, false, 32, 18-8);
        guiItemStacks.init(3, false, 32, 18+8);

        List<List<ItemStack>> input = ingredients.getInputs(ItemStack.class);
        guiItemStacks.set(0, input.get(0));
        guiItemStacks.set(1, recipeWrapper.getRecipe().getOutput());
        guiItemStacks.set(2, BlockMechMachines.getStack(BlockMechMachines.EnumType.HOPPER));
        guiItemStacks.set(3, new ItemStack(ModBlocks.box));
    }

}
