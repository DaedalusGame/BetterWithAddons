package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.ChangeHandler;
import betterwithaddons.interaction.jei.OutputRenderer;
import betterwithaddons.interaction.jei.wrapper.SmeltingRecipeWrapper;
import betterwithaddons.lib.Reference;
import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class TransmutationRecipeCategory extends BlankRecipeCategory<SmeltingRecipeWrapper> {
    public static final String UID = "bwa.infuser.transmutation";
    @Nonnull
    private final IDrawable background;

    public TransmutationRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(getBackgroundResource(), 0, 0, 139, 75);
    }

    private ResourceLocation getBackgroundResource() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/transmutation.png");
    }

    @Override
    public String getUid() {
        return UID;
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.infuser.name");
    }

    @Override
    public String getModName() {
        return Reference.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        super.drawExtras(minecraft);

        String text = Translator.translateToLocal("inv.infuser.throw.name");
        int x = 41 - minecraft.fontRenderer.getStringWidth(text)/2;
        minecraft.fontRenderer.drawString(text,x,20, new Color(64,0,0).getRGB());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SmeltingRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 31, 29);


        IGuiIngredient<ItemStack> inputSlot = guiItemStacks.getGuiIngredients().get(0);
        ChangeHandler handler = new ChangeHandler() {
            @Override
            public void recalculate() {
                ItemStack input = inputSlot.getDisplayedIngredient();
                List<ItemStack> outputs = recipeWrapper.getRecipe().getOutput(Lists.newArrayList(input),null);
                if(outputs.size() > 0)
                    guiItemStacks.set(1, outputs.get(0));
            }
        };
        handler.add(inputSlot);

        guiItemStacks.init(1, false, new OutputRenderer<>(ItemStack.class, handler), 107, 29, 16, 16, 0, 0);

        guiItemStacks.set(0, recipeWrapper.getInputs());
        guiItemStacks.set(1, recipeWrapper.getOutputs());
    }
}
