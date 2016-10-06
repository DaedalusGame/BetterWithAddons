package betterwithaddons.crafting;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.util.InventoryUtil;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 18.09.2016.
 */
public abstract class CraftingManagerNet
{
    private SifterType craftType;
    private List<NetRecipe> recipes;

    public CraftingManagerNet(SifterType craftType)
    {
        this.craftType = craftType;
        this.recipes = new ArrayList<NetRecipe>();
    }

    public void addRecipe(ItemStack[] outputs, ItemStack input, int sand)
    {
        addRecipe(outputs, (Object)input, sand);
    }

    public void addRecipe(ItemStack[] outputs, OreStack input, int sand)
    {
        addRecipe(outputs, (Object)input, sand);
    }

    public void addRecipe(ItemStack[] outputs, Object input, int sand)
    {
        recipes.add(createRecipe(outputs, input, sand));
    }

    public boolean removeRecipe(ItemStack[] outputs, Object input)
    {
        NetRecipe recipe = createRecipe(outputs, input, 0);
        int matchingIndex = getMatchingRecipeIndex(recipe);

        if(matchingIndex >= 0)
        {
            this.recipes.remove(matchingIndex);
            return true;
        }
        return false;
    }

    private int getMatchingRecipeIndex(NetRecipe recipe)
    {
        for(int i = 0; i < this.recipes.size(); i++)
        {
            NetRecipe tempRecipe = this.recipes.get(i);
            if(tempRecipe.matches(recipe))
                return i;
        }
        return -1;
    }

    private boolean containsIngredient(Object input, ItemStack stack)
    {
        if(input instanceof ItemStack)
        {
            if(ItemStack.areItemsEqual((ItemStack)input, stack) || (((ItemStack)input).getItemDamage() == OreDictionary.WILDCARD_VALUE && stack.getItem() == ((ItemStack)input).getItem()))
                return true;
        }
        else if(input instanceof OreStack)
        {
            if(InventoryUtil.listContains(stack, ((OreStack)input).getOres()))
                return true;
        }
        return false;
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
        ArrayList<NetRecipe> validrecipes = new ArrayList<NetRecipe>();

        for (NetRecipe recipe: recipes) {
            if(recipe.matches(inv) && recipe.getSandRequired() <= sand)
            {
                validrecipes.add(recipe);
            }
        }

        return validrecipes;
    }

    /*public ItemStack[] craftItem(List<EntityItem> inv, int sand)
    {
        NetRecipe recipe = getMostValidRecipe(inv, sand);
        if(recipe != null) {
            ItemStack[] ret = new ItemStack[1];
            if(recipe.getOutput() == null) {
                return null;
            }
            ret = recipe.getOutput().toArray(ret);
            ItemUtil.consumeItem(inv,recipe.getInput());
            return ret;
        }
        return null;
    }*/

    private NetRecipe createRecipe(ItemStack[] outputs, Object input, int sand)
    {
        return new NetRecipe(craftType, input, sand, outputs);
    }

    public List<NetRecipe> getRecipes()
    {
        return this.recipes;
    }

    //Lazy way of ensuring the ore dictionary entries were properly implemented.
    public void refreshRecipes()
    {
        List<NetRecipe> recipes = getRecipes();
        if(!recipes.isEmpty())
        {
            this.recipes = new ArrayList<NetRecipe>();
            for(NetRecipe r : recipes) {
                this.recipes.add(createRecipe(r.getOutput().toArray(new ItemStack[0]), r.input, r.getSandRequired()));
            }
        }
    }
}
