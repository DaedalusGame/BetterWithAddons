package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.crafting.recipes.NetRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NetRecipeWrapper extends BlankRecipeWrapper {
    public NetRecipe recipe;
    public SifterType type;

    public NetRecipeWrapper(NetRecipe recipe) {
        this.recipe = recipe;
        this.type = recipe.getType();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class,getInputs());
        ingredients.setOutputs(ItemStack.class,getOutputs());
    }

    public List<ItemStack> getInputs()
    {
        List<ItemStack> inputs = getInputWithoutSand();

        int sandrequired = recipe.getSandRequired();
        if(sandrequired > 0)
            inputs.add(new ItemStack(Blocks.SAND,sandrequired));

        return inputs;
    }

    public List<ItemStack> getInputWithoutSand()
    {
        return recipe.getInput().stream().filter(stack -> !stack.isEmpty()).map(ItemStack::copy).collect(Collectors.toList());
    }

    public List<ItemStack> getOutputs()
    {
        return recipe.getOutput().stream().filter(stack -> !stack.isEmpty()).map(ItemStack::copy).collect(Collectors.toList());
    }

    @Nonnull
    public List<ItemStack> getSandInput()
    {
        List<ItemStack> outputs = new ArrayList<ItemStack>();
        int sandrequired = recipe.getSandRequired();
        if(sandrequired > 0)
            outputs.add(new ItemStack(Blocks.SAND,sandrequired));
        return outputs;
    }

    @Nonnull
    public List<ItemStack> getUpperOutputs()
    {
        List<ItemStack> outputs = new ArrayList<ItemStack>();
        int i = 0;
        for (ItemStack stack : recipe.getOutput()) {
            if(i++ % 2 == 0 && !stack.isEmpty())
                outputs.add(stack.copy());
        }
        return outputs;
    }

    @Nonnull
    public List<ItemStack> getLowerOutputs()
    {
        List<ItemStack> outputs = new ArrayList<ItemStack>();
        int i = 0;
        for (ItemStack stack : recipe.getOutput()) {
            if(i++ % 2 == 1 && stack != null)
                outputs.add(stack.copy());
        }
        return outputs;
    }
}
