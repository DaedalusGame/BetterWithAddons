package betterwithaddons.interaction.minetweaker;

import betterwithaddons.block.EriottoMod.BlockNettedScreen;
import betterwithaddons.crafting.manager.CraftingManagerSandNet;
import betterwithaddons.crafting.recipes.NetRecipe;
import betterwithaddons.interaction.jei.category.SandNetRecipeCategory;
import com.blamejared.mtlib.helpers.InputHelper;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass(SandNet.clazz)
public class SandNet extends Net {
    public static final String clazz = "mods.betterwithaddons.SandNet";

    @ZenMethod
    public static void add(IItemStack[] outputs, @NotNull IIngredient input, int sand) {
        NetRecipe r = new NetRecipe(BlockNettedScreen.SifterType.SAND,InputHelper.toObject(input),sand,InputHelper.toStacks(outputs));
        MineTweakerAPI.apply(new Add("SandNet", SandNetRecipeCategory.UID, CraftingManagerSandNet.getInstance(),r));
    }

    @ZenMethod
    public static void remove(IItemStack input)
    {
        MineTweakerAPI.apply(new Remove("SandNet", SandNetRecipeCategory.UID, CraftingManagerSandNet.getInstance(),InputHelper.toStack(input)));
    }
}
