package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockNettedScreen;
import betterwithaddons.crafting.manager.CraftingManagerSandNet;
import betterwithaddons.crafting.recipes.NetRecipe;
import betterwithaddons.interaction.jei.category.SandNetRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(SandNet.clazz)
public class SandNet extends Net {
    public static final String clazz = "mods.betterwithaddons.SandNet";

    @ZenMethod
    public static void add(IItemStack[] outputs, @NotNull IIngredient input, int sand) {
        NetRecipe r = new NetRecipe(BlockNettedScreen.SifterType.SAND,InputHelper.toObject(input),sand,InputHelper.toStacks(outputs));
        CraftTweakerAPI.apply(new Add("SandNet", CraftingManagerSandNet.getInstance(),r));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        CraftTweakerAPI.apply(new Remove("SandNet", CraftingManagerSandNet.getInstance(),InputHelper.toStack(input)));
    }
}
