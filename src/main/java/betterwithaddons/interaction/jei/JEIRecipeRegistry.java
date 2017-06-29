package betterwithaddons.interaction.jei;

import betterwithaddons.crafting.NetRecipe;
import betterwithaddons.crafting.manager.CraftingManagerCherryBox;
import betterwithaddons.crafting.manager.CraftingManagerNet;
import betterwithaddons.crafting.manager.CraftingManagerSpindle;
import betterwithaddons.crafting.manager.CraftingManagerTatara;
import betterwithaddons.interaction.jei.wrapper.CherryBoxRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.NetRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.SpindleRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.TataraRecipeWrapper;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class JEIRecipeRegistry {
    public static List<TataraRecipeWrapper> getTataraRecipes(CraftingManagerTatara manager) {
        return manager.getSmeltingList().entrySet().stream().map(entry -> new TataraRecipeWrapper(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public static List<NetRecipeWrapper> getNetRecipes(CraftingManagerNet manager) {
        List<NetRecipeWrapper> list = Lists.newArrayList();
        for (NetRecipe recipe : manager.getRecipes()) {
            list.add(new NetRecipeWrapper(recipe));
        }
        return list;
    }

    public static List<CherryBoxRecipeWrapper> getCherryBoxRecipes(CraftingManagerCherryBox manager) {
        return manager.getWorkingList().entrySet().stream().map(entry -> new CherryBoxRecipeWrapper(entry.getKey(), entry.getValue(), manager.getType())).collect(Collectors.toList());
    }

    public static List<SpindleRecipeWrapper> getSpindleRecipes(CraftingManagerSpindle manager) {
        return manager.getRecipes().stream().map(SpindleRecipeWrapper::new).collect(Collectors.toList());
    }
}