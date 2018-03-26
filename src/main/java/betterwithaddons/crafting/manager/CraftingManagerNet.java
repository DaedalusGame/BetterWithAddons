package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.crafting.recipes.NetRecipe;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CraftingManagerNet
{
    private SifterType craftType;
    private List<NetRecipe> recipes;

    public CraftingManagerNet(SifterType craftType)
    {
        this.craftType = craftType;
        this.recipes = new ArrayList<>();
    }

    public void addRecipe(ItemStack[] outputs, Ingredient input, int sand) {
        recipes.add(createRecipe(outputs, input, sand));
    }

    public void addRecipe(ItemStack[] outputs, Ingredient input)
    {
        addRecipe(outputs,input,0);
    }

    public void addRecipe(NetRecipe recipe)
    {
        recipes.add(recipe);
    }

    public List<NetRecipe> findRecipeForRemoval(@Nonnull ItemStack input) {
        return recipes.stream().filter(recipe -> recipe.matchesInput(input)).collect(Collectors.toList());
    }

    public NetRecipe getMostValidRecipe(List<EntityItem> inv, int sand)
    {
        List<NetRecipe> recipes = getValidCraftingRecipes(inv,sand);

        if(recipes == null || recipes.size() == 0)
            return null;

        return recipes.get(0);
    }

    public List<NetRecipe> getValidCraftingRecipes(List<EntityItem> inv, int sand)
    {
        return recipes.stream().filter(recipe -> recipe.matches(inv) && recipe.getSandRequired() <= sand).collect(Collectors.toCollection(ArrayList::new));
    }

    protected NetRecipe createRecipe(ItemStack[] outputs, Ingredient input, int sand)
    {
        return new NetRecipe(craftType, input, sand, outputs);
    }

    public List<NetRecipe> getRecipes()
    {
        return this.recipes;
    }
}
