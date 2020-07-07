package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerInfuser;
import betterwithaddons.crafting.manager.CraftingManagerInfuserTransmutation;
import betterwithaddons.crafting.recipes.infuser.InfuserRecipe;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import betterwithaddons.util.IngredientCraftTweaker;
import betterwithaddons.util.ItemUtil;
import com.google.common.collect.Lists;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.recipes.IRecipeAction;
import crafttweaker.api.recipes.IRecipeFunction;
import crafttweaker.mc1120.CraftTweaker;
import crafttweaker.mc1120.recipes.MCRecipeBase;
import crafttweaker.mc1120.recipes.MCRecipeManager;
import crafttweaker.mc1120.recipes.MCRecipeShaped;
import crafttweaker.mc1120.recipes.MCRecipeShapeless;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.*;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(Infuser.clazz)
public class Infuser {
    public static final String clazz = "mods.betterwithaddons.Infuser";

    public static class TransmutationFunction extends TransmutationRecipe {
        IModRecipeFunction function;

        public TransmutationFunction(IIngredient input, int requiredSpirit, IModRecipeFunction function) {
            super(new IngredientCraftTweaker(input), requiredSpirit, ItemStack.EMPTY);
        }

        @Override
        public List<ItemStack> getOutput(List<ItemStack> inputs, TileEntity tile) {
            IItemStack[] outputs = function.process(ItemUtil.getMarkedInputs(inputs, Lists.newArrayList(this.input)),getMachineInfo(tile));
            return Arrays.stream(outputs).map(CraftTweakerMC::getItemStack).collect(Collectors.toList());
        }

        private IMachineInfo getMachineInfo(TileEntity tile) {
            return IMachineInfo.create(tile);
        }
    }

    @ZenMethod
    public static void addShaped(IItemStack output, IIngredient[][] ingredients, int spirits, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        CraftTweaker.LATE_ACTIONS.add(new AddShaped(output, ingredients, spirits, function, action, false, false));
    }

    @ZenMethod
    public static void addShapedMirrored(IItemStack output, IIngredient[][] ingredients, int spirits, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        CraftTweaker.LATE_ACTIONS.add(new AddShaped(output, ingredients, spirits, function, action, true, false));
    }

    @ZenMethod
    public static void addShapeless(IItemStack output, IIngredient[] ingredients, int spirits, @Optional IRecipeFunction function, @Optional IRecipeAction action) {
        CraftTweaker.LATE_ACTIONS.add(new AddShapeless(output, ingredients, spirits, function, action, false));
    }

    @ZenMethod
    public static void addTransmutation(IItemStack output, IIngredient input, int spirits) {
        CraftTweaker.LATE_ACTIONS.add(new AddTransmutation(CraftTweakerMC.getItemStack(output), new IngredientCraftTweaker(input), spirits));
    }

    @ZenMethod
    public static void removeAll() {
        CraftTweaker.LATE_ACTIONS.add(new RemoveAll());
    }

    @ZenMethod
    public static void remove(IItemStack output) {
        CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void removeTransmutation(IItemStack output) {
        CraftTweaker.LATE_ACTIONS.add(new RemoveTransmutation(CraftTweakerMC.getItemStack(output)));
    }

    @ZenMethod
    public static void removeAllTransmutation() {
        CraftTweaker.LATE_ACTIONS.add(new RemoveAllTransmutations());
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
        protected ItemStack output;
        protected Ingredient input;
        protected int requiredSpirits;

        public AddTransmutation(ItemStack output, Ingredient input, int spirits)
        {
            this.output = output;
            this.input = input;
            this.requiredSpirits = spirits;
        }

        @Override
        public void apply() {
            CraftingManagerInfuserTransmutation.getInstance().addRecipe(new TransmutationRecipe(input,requiredSpirits,output));
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
}
