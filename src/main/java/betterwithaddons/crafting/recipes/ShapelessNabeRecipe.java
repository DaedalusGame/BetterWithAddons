package betterwithaddons.crafting.recipes;

import betterwithaddons.tileentity.TileEntityNabe;
import betterwithaddons.util.NabeResult;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class ShapelessNabeRecipe implements INabeRecipe {
    public int time;
    List<Ingredient> ingredients;
    NabeResult result;
    ResourceLocation name;

    public ShapelessNabeRecipe(ResourceLocation name, NabeResult result, List<Ingredient> ingredients, int time) {
        this.name = name;
        this.ingredients = ingredients;
        this.result = result;
        this.time = time;
    }

    @Override
    public int getBoilingTime(TileEntityNabe tile) {
        return time;
    }

    @Override
    public boolean isValidItem(ItemStack stack) {
        return ingredients.stream().anyMatch(ingredient -> ingredient.apply(stack));
    }

    @Override
    public boolean matches(TileEntityNabe tile, List<ItemStack> stacks) {
        HashSet<Ingredient> ingredients = new HashSet<>(this.ingredients);
        for (ItemStack stack : stacks) {
            Optional<Ingredient> found = ingredients.stream().filter(x -> x.apply(stack)).findFirst();
            if (found.isPresent())
                ingredients.remove(found.get());
            else
                return false;
        }

        return ingredients.isEmpty();
    }

    @Override
    public NabeResult craft(TileEntityNabe tile, List<ItemStack> stacks) {
        HashSet<Ingredient> ingredients = new HashSet<>(this.ingredients);
        ItemStackHandler inventory = tile.inventory;
        for(int i = 0; i < inventory.getSlots(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            final ItemStack finalStack = stack;
            Optional<Ingredient> found = ingredients.stream().filter(x -> x.apply(finalStack)).findFirst();
            if (found.isPresent()) {
                ingredients.remove(found.get());
                stack = tile.consumeItem(stack);
                inventory.setStackInSlot(i,stack);
            }
        }
        return result.copy();
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return ingredients.size();
    }
}
