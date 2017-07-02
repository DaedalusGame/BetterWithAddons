package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockCherryBox;
import betterwithaddons.crafting.manager.CraftingManagerDryingBox;
import betterwithaddons.crafting.recipes.CherryBoxRecipe;
import betterwithaddons.interaction.jei.category.DryingBoxRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import com.google.common.collect.Lists;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

@ZenClass(DryingBox.clazz)
public class DryingBox extends CherryBox {
    public static final String clazz = "mods.betterwithaddons.DryingBox";

    @ZenMethod
    public static void add(IItemStack output, IItemStack input) {
        CherryBoxRecipe recipe = new CherryBoxRecipe(BlockCherryBox.CherryBoxType.DRYING,InputHelper.toObject(input),InputHelper.toStack(output));
        MineTweakerAPI.apply(new Add("DryingBox", DryingBoxRecipeCategory.UID, CraftingManagerDryingBox.instance(), Lists.newArrayList(recipe)));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        List<CherryBoxRecipe> recipes = CraftingManagerDryingBox.instance().findRecipeForRemoval(InputHelper.toStack(input));
        MineTweakerAPI.apply(new Remove("DryingBox", DryingBoxRecipeCategory.UID, CraftingManagerDryingBox.instance(), recipes));
    }
}
