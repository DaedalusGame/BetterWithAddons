package betterwithaddons.interaction.jei.wrapper;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class CherryBoxRecipeWrapper extends BlankRecipeWrapper {
    public List<ItemStack> inputs = Lists.newArrayList(), outputs = Lists.newArrayList();
    public CherryBoxType type;

    public CherryBoxRecipeWrapper(ItemStack input, ItemStack output, CherryBoxType boxType) {
        inputs.add(input);
        outputs.add(output);
        type = boxType;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class,getInputs());
        ingredients.setOutputs(ItemStack.class,getOutputs());
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }
}
