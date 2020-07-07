package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.interaction.InteractionEriottoMod;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.util.List;

public class InfuserRecipeWrapper extends BlankRecipeWrapper {
    IRecipeWrapper innerWrapper;
    int requiredSpirits;

    public InfuserRecipeWrapper(IRecipeWrapper innerWrapper, int requiredSpirits)
    {
        super();
        this.innerWrapper = innerWrapper;
        this.requiredSpirits = requiredSpirits;
    }

    public IRecipeWrapper getInner() {
        return innerWrapper;
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        if(mouseX >= 78 && mouseY >= 30 && mouseX <= 96 && mouseY <= 44)
            return Lists.newArrayList(I18n.format("inv.infuser.cost.description",requiredSpirits));

        return super.getTooltipStrings(mouseX, mouseY);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        int foregroundcolor = InteractionEriottoMod.SPIRIT_GUI_COLOR_HIGH.getRGB();
        int backgroundcolor = InteractionEriottoMod.SPIRIT_GUI_COLOR_LOW.getRGB();

        String costString = I18n.format("inv.infuser.cost.name",requiredSpirits);

        if(requiredSpirits > 0) {
            int drawoffsetX = 87 - minecraft.fontRenderer.getStringWidth(costString) / 2;
            int drawoffsetY = 33;

            minecraft.fontRenderer.drawString(costString, drawoffsetX, drawoffsetY+1, backgroundcolor);
            minecraft.fontRenderer.drawString(costString, drawoffsetX+1, drawoffsetY+1, backgroundcolor);
            minecraft.fontRenderer.drawString(costString, drawoffsetX+1, drawoffsetY+1, backgroundcolor);
            minecraft.fontRenderer.drawString(costString, drawoffsetX, drawoffsetY, foregroundcolor);
        }

        super.drawInfo(minecraft, recipeWidth, recipeHeight, mouseX, mouseY);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        if(innerWrapper != null)
            innerWrapper.getIngredients(ingredients);
    }
}
