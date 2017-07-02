package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerSpindle;
import betterwithaddons.crafting.recipes.SpindleRecipe;
import betterwithaddons.interaction.jei.category.SpindleRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(Spindle.clazz)
public class Spindle {
    public static final String clazz = "mods.betterwithaddons.Spindle";

    @ZenMethod
    public static void add(IItemStack[] outputs, @NotNull IIngredient input, boolean consumesSpindle) {
        SpindleRecipe r = new SpindleRecipe(consumesSpindle, InputHelper.toObject(input), InputHelper.toStacks(outputs));
        MineTweakerAPI.apply(new Add(r));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        MineTweakerAPI.apply(new Remove(InputHelper.toStack(input)));
    }

    public static class Add extends BaseListAddition<SpindleRecipe>
    {
        public Add(SpindleRecipe spindleRecipe) {
            super("Spindle", CraftingManagerSpindle.getInstance().getRecipes(), Lists.newArrayList(spindleRecipe));
        }

        @Override
        protected String getRecipeInfo(SpindleRecipe recipe) {
            return recipe.getInput().toString();
        }

        @Override
        public String getJEICategory(SpindleRecipe recipe) {
            return SpindleRecipeCategory.UID;
        }
    }

    public static class Remove extends BaseListRemoval<SpindleRecipe>
    {
        protected Remove(ItemStack input) {
            super("Spindle", CraftingManagerSpindle.getInstance().getRecipes(), CraftingManagerSpindle.getInstance().findRecipeForRemoval(input));
        }

        @Override
        protected String getRecipeInfo(SpindleRecipe recipe) {
            return recipe.getInput().toString();
        }

        @Override
        public String getJEICategory(SpindleRecipe recipe) {
            return SpindleRecipeCategory.UID;
        }
    }
}
