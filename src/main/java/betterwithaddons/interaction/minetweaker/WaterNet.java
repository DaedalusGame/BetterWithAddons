package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockNettedScreen;
import betterwithaddons.crafting.manager.CraftingManagerWaterNet;
import betterwithaddons.crafting.recipes.NetRecipe;
import betterwithaddons.interaction.jei.category.WaterNetRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(WaterNet.clazz)
public class WaterNet extends Net {
    public static final String clazz = "mods.betterwithaddons.WaterNet";

    @ZenMethod
    public static void add(IItemStack[] outputs, @NotNull IIngredient input) {
        NetRecipe r = new NetRecipe(BlockNettedScreen.SifterType.WATER, InputHelper.toObject(input),0,InputHelper.toStacks(outputs));
        CraftTweakerAPI.apply(new Add("WaterNet", CraftingManagerWaterNet.getInstance(),r));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        CraftTweakerAPI.apply(new Remove("WaterNet", CraftingManagerWaterNet.getInstance(),InputHelper.toStack(input)));
    }
}
