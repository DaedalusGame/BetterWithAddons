package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import betterwithaddons.crafting.NetRecipe;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

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
        List<ItemStack> inputs = new ArrayList<>();
        Object obj = recipe.getInput();

        if(obj instanceof ItemStack)
        {
            ItemStack stack = (ItemStack)obj;
            if(!stack.isEmpty() && stack.getItem() != null)
                inputs.add(stack.copy());
        }
        else if(obj instanceof List) {
            inputs.addAll((List<ItemStack>)obj);
        }

        return inputs;
    }

    public List<ItemStack> getOutputs()
    {
        List<ItemStack> outputs = new ArrayList<ItemStack>();
        for (ItemStack stack : recipe.getOutput()) {
            if(!stack.isEmpty()) {
                outputs.add(stack.copy());
            }
        }

        return outputs;
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
