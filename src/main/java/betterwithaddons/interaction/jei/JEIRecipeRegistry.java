package betterwithaddons.interaction.jei;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.crafting.CraftingManagerCherryBox;
import betterwithaddons.crafting.CraftingManagerNet;
import betterwithaddons.crafting.CraftingManagerTatara;
import betterwithaddons.crafting.NetRecipe;
import betterwithaddons.interaction.jei.wrapper.CherryBoxRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.NetRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.TataraRecipeWrapper;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Christian on 25.09.2016.
 */
public class JEIRecipeRegistry {
    public static List<TataraRecipeWrapper> getTataraRecipes(CraftingManagerTatara manager) {
        return manager.getSmeltingList().entrySet().stream().map(entry -> new TataraRecipeWrapper(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static List<NetRecipeWrapper> getNetRecipes(CraftingManagerNet manager) {
        List<NetRecipeWrapper> list = Lists.newArrayList();
        for (NetRecipe recipe: manager.getRecipes()) {
            list.add(new NetRecipeWrapper(recipe));
        }
        return list;
    }

    public static List<CherryBoxRecipeWrapper> getCherryBoxRecipes(CraftingManagerCherryBox manager) {
        return manager.getWorkingList().entrySet().stream().map(entry -> new CherryBoxRecipeWrapper(entry.getKey(), entry.getValue(), manager.getType())).collect(Collectors.toList());
    }
}