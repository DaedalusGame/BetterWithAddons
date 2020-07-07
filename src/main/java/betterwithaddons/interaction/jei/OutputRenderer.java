package betterwithaddons.interaction.jei;

import mezz.jei.Internal;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.gui.ingredients.GuiIngredient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.Rectangle;
import java.util.List;

public class OutputRenderer<T> implements IIngredientRenderer<T> {
    IIngredientRenderer<T> renderer;
    ChangeHandler handler;

    public OutputRenderer(Class<T> clazz, ChangeHandler handler) {
        this.renderer = Internal.getIngredientRegistry().getIngredientRenderer(clazz);
        this.handler = handler;
    }

    @Override
    public void render(Minecraft minecraft, int xPosition, int yPosition, @Nullable T ingredient) {
        handler.update();
        renderer.render(minecraft, xPosition, yPosition, ingredient);
    }

    @Override
    public List<String> getTooltip(Minecraft minecraft, T ingredient, ITooltipFlag tooltipFlag) {
        return renderer.getTooltip(minecraft, ingredient, tooltipFlag);
    }

    @Override
    public FontRenderer getFontRenderer(Minecraft minecraft, T ingredient) {
        return renderer.getFontRenderer(minecraft, ingredient);
    }
}
