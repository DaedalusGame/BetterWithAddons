package betterwithaddons.interaction.jei.wrapper;

import com.google.common.collect.Lists;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by Christian on 25.09.2016.
 */
public class TataraRecipeWrapper extends BlankRecipeWrapper {
    public List<ItemStack> inputs = Lists.newArrayList(), outputs = Lists.newArrayList();

    public TataraRecipeWrapper(ItemStack input, ItemStack output) {
        inputs.add(input);
        outputs.add(output);
    }

    @Nonnull
    @Override
    public List<ItemStack> getInputs() {
        return inputs;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs() {
        return outputs;
    }
}
