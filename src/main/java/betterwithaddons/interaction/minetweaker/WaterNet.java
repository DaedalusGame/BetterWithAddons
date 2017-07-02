package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockNettedScreen;
import betterwithaddons.crafting.manager.CraftingManagerWaterNet;
import betterwithaddons.crafting.recipes.NetRecipe;
import betterwithaddons.interaction.jei.category.WaterNetRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(WaterNet.clazz)
public class WaterNet extends Net {
    public static final String clazz = "mods.betterwithaddons.WaterNet";

    @ZenMethod
    public static void add(IItemStack[] outputs, @NotNull IIngredient input) {
        NetRecipe r = new NetRecipe(BlockNettedScreen.SifterType.WATER, InputHelper.toObject(input),0,InputHelper.toStacks(outputs));
        MineTweakerAPI.apply(new Add("WaterNet", WaterNetRecipeCategory.UID, CraftingManagerWaterNet.getInstance(),r));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        MineTweakerAPI.apply(new Remove("WaterNet", WaterNetRecipeCategory.UID, CraftingManagerWaterNet.getInstance(),InputHelper.toStack(input)));
    }
}
