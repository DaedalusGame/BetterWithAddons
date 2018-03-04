package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerInfuser;
import betterwithaddons.crafting.manager.CraftingManagerInfuserTransmutation;
import betterwithaddons.crafting.recipes.infuser.InfuserRecipe;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import betterwithaddons.interaction.InteractionCraftTweaker;
import betterwithaddons.util.IngredientCraftTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.recipes.MCRecipeBase;
import crafttweaker.mc1120.recipes.MCRecipeManager;
import crafttweaker.mc1120.recipes.MCRecipeShaped;
import crafttweaker.mc1120.recipes.MCRecipeShapeless;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.List;

@ZenRegister
@ZenClass(Infuser.clazz)
public class Infuser {
    public static final String clazz = "mods.betterwithaddons.Infuser";

    @ZenMethod
    public void addShaped(IItemStack output, IIngredient[][] ingredients, int spirits, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        InteractionCraftTweaker.LATE_ADDITIONS.add(new AddShaped(output, ingredients, spirits, function, action, false, false));
    }

    @ZenMethod
    public void addShapedMirrored(IItemStack output, IIngredient[][] ingredients, int spirits, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        InteractionCraftTweaker.LATE_ADDITIONS.add(new AddShaped(output, ingredients, spirits, function, action, true, false));
    }

    @ZenMethod
    public void addShapeless(IItemStack output, IIngredient[] ingredients, int spirits, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        InteractionCraftTweaker.LATE_ADDITIONS.add(new AddShapeless(output, ingredients, spirits, function, action, false));
    }

    @ZenMethod
    public void addTransmutation(IItemStack output, IIngredient input, int spirits) {
        InteractionCraftTweaker.LATE_ADDITIONS.add(new AddTransmutation(output, input, spirits));
    }

    @ZenMethod
    public void removeAll() {
        InteractionCraftTweaker.LATE_REMOVALS.add(new RemoveAll());
    }

    @ZenMethod
    public void remove(IItemStack output) {
        InteractionCraftTweaker.LATE_REMOVALS.add(new Remove(InputHelper.toStack(output)));
    }

    @ZenMethod
    public void removeTransmutation(IItemStack output) {
        InteractionCraftTweaker.LATE_REMOVALS.add(new RemoveTransmutation(InputHelper.toStack(output)));
    }

    public static class BaseAdd implements IAction {
        protected MCRecipeBase recipe;
        protected IItemStack output;
        protected boolean isShaped;
        protected String name;
        protected int requiredSpirits;

        private BaseAdd(MCRecipeBase recipe, IItemStack output, int spirits, boolean isShaped) {
            this.recipe = recipe;
            this.output = output;
            this.isShaped = isShaped;
            this.requiredSpirits = spirits;
            if(recipe.hasTransformers()) {
                MCRecipeManager.transformerRecipes.add(recipe);
            }
        }

        public IItemStack getOutput() {
            return this.output;
        }

        public void setOutput(IItemStack output) {
            this.output = output;
        }

        public void apply() {
            CraftingManagerInfuser.getInstance().addRecipe(new InfuserRecipe(recipe,requiredSpirits));
        }

        public String describe() {
            return this.output != null?"Adding " + (this.isShaped?"shaped":"shapeless") + " ancestral infuser recipe for " + this.output.getDisplayName() + " with name " + this.name:"Trying to add " + (this.isShaped?"shaped":"shapeless") + " ancestral infuser recipe without correct output";
        }

        public MCRecipeBase getRecipe() {
            return this.recipe;
        }
    }

    private static class AddShapeless extends BaseAdd {
        public AddShapeless(IItemStack output, IIngredient[] ingredients, int spirits, @Optional IRecipeFunction function, @Optional IRecipeAction action, boolean hidden) {
            super(new MCRecipeShapeless(ingredients, output, function, action, hidden), output, spirits, false);
        }
    }

    private static class AddShaped extends BaseAdd {
        public AddShaped(IItemStack output, IIngredient[][] ingredients, int spirits, IRecipeFunction function, IRecipeAction action, boolean mirrored, boolean hidden) {
            super(new MCRecipeShaped(ingredients, output, function, action, mirrored, hidden), output, spirits, true);
        }
    }

    private static class AddTransmutation implements IAction {
        protected IItemStack output;
        protected IIngredient input;
        protected int requiredSpirits;

        public AddTransmutation(IItemStack output, IIngredient input, int spirits)
        {
            this.output = output;
            this.input = input;
            this.requiredSpirits = spirits;
        }

        @Override
        public void apply() {
            CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipeCT(input,requiredSpirits,output));
        }

        @Override
        public String describe() {
            return "Adding ancestral infuser transmutation for "+output.getDisplayName();
        }
    }

    public static class Remove implements IAction {
        ItemStack output;

        public Remove(ItemStack output) {
            this.output = output;
        }

        public void apply() {
            List<InfuserRecipe> toRemove = CraftingManagerInfuser.getInstance().getRecipesByOutput(output);
            for (InfuserRecipe recipe : toRemove) {
                CraftingManagerInfuser.getInstance().removeRecipe(recipe);
            }
        }

        public String describe() {
            return "Removing ancestral infuser recipes for "+this.output.getDisplayName();
        }
    }

    public static class RemoveTransmutation implements IAction {
        ItemStack output;

        public RemoveTransmutation(ItemStack output) {
            this.output = output;
        }

        public void apply() {
            List<TransmutationRecipe> toRemove = CraftingManagerInfuserTransmutation.getInstance().findRecipeForRemoval(output);
            for (TransmutationRecipe recipe : toRemove) {
                CraftingManagerInfuserTransmutation.getInstance().removeRecipe(recipe);
            }
        }

        public String describe() {
            return "Removing ancestral transmutation recipes for "+this.output.getDisplayName();
        }
    }

    public static class RemoveAll implements IAction {
        public void apply() {
            CraftingManagerInfuser.getInstance().clearRecipes();
        }

        public String describe() {
            return "Removing all ancestral infuser recipes";
        }
    }

    public static class RemoveAllTransmutations implements IAction {
        public void apply() {
            CraftingManagerInfuserTransmutation.getInstance().clearRecipes();
        }

        public String describe() {
            return "Removing all ancestral transmutation recipes";
        }
    }

    public static class TransmutationRecipeCT extends TransmutationRecipe {
        IngredientCraftTweaker input;

        public TransmutationRecipeCT(IIngredient input, int requiredSpirit, IItemStack output) {
            super(input, requiredSpirit, InputHelper.toStack(output));
            this.input = new IngredientCraftTweaker(input);
        }

        @Override
        public boolean matchesInput(ItemStack item) {
            return input.apply(item);
        }

        @Override
        public List<ItemStack> getRecipeInputs() {
            return Arrays.asList(input.getMatchingStacks());
        }
    }
}
