package betterwithaddons.interaction.minetweaker;

import betterwithaddons.crafting.manager.CraftingManagerNabe;
import betterwithaddons.crafting.manager.CraftingManagerTatara;
import betterwithaddons.crafting.recipes.INabeRecipe;
import betterwithaddons.crafting.recipes.ShapelessNabeRecipe;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import betterwithaddons.crafting.recipes.TeaNabeRecipe;
import betterwithaddons.item.ItemTea;
import betterwithaddons.util.*;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.potions.IPotion;
import crafttweaker.api.potions.IPotionEffect;
import crafttweaker.mc1120.CraftTweaker;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ZenRegister
@ZenClass(Nabe.clazz)
public class Nabe {
    public static final String clazz = "mods.betterwithaddons.Nabe";

    private static List<Ingredient> getIngredientList(IIngredient[] input) {
        return Arrays.stream(input).map(IngredientCraftTweaker::new).collect(Collectors.toList());
    }

    private static List<PotionEffect> getPotionList(IPotionEffect[] positiveEffects) {
        return Arrays.stream(positiveEffects).map(CraftTweakerMC::getPotionEffect).collect(Collectors.toList());
    }

    @ZenMethod
    public static void addFluid(String name, ILiquidStack liquid, IIngredient[] input, int boilTime) {
        ShapelessNabeRecipe recipe = new ShapelessNabeRecipe(new ResourceLocation(CraftTweaker.MODID,name),new NabeResult(CraftTweakerMC.getLiquidStack(liquid)),getIngredientList(input),boilTime);
        CraftTweaker.LATE_ACTIONS.add(new Add(recipe));
    }

    @ZenMethod
    public static void addPoison(String name, int doses, IIngredient[] input, int boilTime) {
        ShapelessNabeRecipe recipe = new ShapelessNabeRecipe(new ResourceLocation(CraftTweaker.MODID,name),new NabeResultPoison(doses,doses),getIngredientList(input),boilTime);
        CraftTweaker.LATE_ACTIONS.add(new Add(recipe));
    }

    @ZenMethod
    public static void addTeaType(String name, int red, int green, int blue, int alpha) {
        CraftTweakerAPI.apply(new AddTeaType(name,new Color(red,green,blue,alpha)));
    }

    @ZenMethod
    public static void addTeaShape(String name, String shape, IPotionEffect[] positiveEffects, IPotionEffect[] negativeEffects) {
        CraftTweakerAPI.apply(new AddTeaShape(name, TeaType.ItemType.valueOf(shape), getPotionList(positiveEffects), getPotionList(negativeEffects)));
    }

    @ZenMethod
    public static void addTeaColor(String name, String shape, int red, int green, int blue, int alpha) {
        CraftTweakerAPI.apply(new AddTeaColor(name, TeaType.ItemType.valueOf(shape), new Color(red,green,blue,alpha)));
    }

    @ZenMethod
    public static void addOpposite(IPotion a, IPotion b) {
        CraftTweakerAPI.apply(new AddOpposite(CraftTweakerMC.getPotion(a),CraftTweakerMC.getPotion(b)));
    }

    @ZenMethod
    public static void remove(String name) {
        CraftTweaker.LATE_ACTIONS.add(new Remove(new ResourceLocation(name)));
    }

    @ZenMethod
    public static void removeTeaType(String name) {
        for (TeaType.ItemType shape : TeaType.ItemType.values()) {
            CraftTweakerAPI.apply(new RemoveTeaShape(name, shape));
        }
    }

    @ZenMethod
    public static void removeTeaShape(String name, String shape) {
        CraftTweakerAPI.apply(new RemoveTeaShape(name, TeaType.ItemType.valueOf(shape)));
    }

    @ZenMethod
    public static void removeCeremonialTeaRecipe() {
        CraftTweakerAPI.apply(new RemoveCeremonialTeaRecipe());
    }

    public static class Add implements IAction
    {
        INabeRecipe recipe;

        public Add(INabeRecipe recipe) {
            this.recipe = recipe;
        }

        @Override
        public void apply() {
            CraftingManagerNabe.getInstance().addRecipe(recipe);
        }

        @Override
        public String describe() {
            return "Adding Nabe recipe: "+recipe.toString();
        }
    }

    public static class Remove implements IAction
    {
        ResourceLocation name;

        public Remove(ResourceLocation name) {
            this.name = name;
        }

        @Override
        public void apply() {
            CraftingManagerNabe.getInstance().removeRecipe(name);
        }

        @Override
        public String describe() {
            return "Removing Nabe recipe: "+name.toString();
        }
    }

    public static class AddTeaType implements IAction {
        String name;
        Color color;

        public AddTeaType(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        @Override
        public void apply() {
            new TeaType(name,color);
        }

        @Override
        public String describe() {
            return "Adding new tea type "+name;
        }
    }

    public static class AddTeaShape implements IAction {
        String name;
        TeaType.ItemType shape;
        List<PotionEffect> positiveEffects;
        List<PotionEffect> negativeEffects;

        public AddTeaShape(String name, TeaType.ItemType shape, List<PotionEffect> positiveEffects, List<PotionEffect> negativeEffects) {
            this.name = name;
            this.shape = shape;
            this.positiveEffects = positiveEffects;
            this.negativeEffects = negativeEffects;
        }

        @Override
        public void apply() {
            TeaType type = TeaType.getType(name);
            if(type != null) {
                type.setHasType(shape);
                for (PotionEffect effect : positiveEffects)
                    type.addPositive(shape, effect);
                for (PotionEffect effect : negativeEffects)
                    type.addNegative(shape, effect);
            }
        }

        @Override
        public String describe() {
            return "Adding "+shape.name()+" to "+name;
        }
    }

    public static class AddTeaColor implements IAction {
        String name;
        TeaType.ItemType shape;
        Color color;

        public AddTeaColor(String name, TeaType.ItemType shape, Color color) {
            this.name = name;
            this.shape = shape;
            this.color = color;
        }

        @Override
        public void apply() {
            TeaType type = TeaType.getType(name);
            if(type != null) {
                type.setHasType(shape, color);
            }
        }

        @Override
        public String describe() {
            return "Setting color of "+shape.name()+" of "+name;
        }
    }

    public static class RemoveTeaShape implements IAction {
        String name;
        TeaType.ItemType shape;

        public RemoveTeaShape(String name, TeaType.ItemType shape) {
            this.name = name;
            this.shape = shape;
        }

        @Override
        public void apply() {
            TeaType type = TeaType.getType(name);
            if(type != null) {
                type.removeType(shape);
            }
        }

        @Override
        public String describe() {
            return "Removing "+shape.name()+" from "+name;
        }
    }

    public static class AddOpposite implements IAction {
        Potion potionA;
        Potion potionB;

        public AddOpposite(Potion potionA, Potion potionB) {
            this.potionA = potionA;
            this.potionB = potionB;
        }

        @Override
        public void apply() {
            TeaNabeRecipe.addOpposite(potionA,potionB);
        }

        @Override
        public String describe() {
            return "Adding "+potionA+" and "+potionB+" as opposites";
        }
    }

    public static class RemoveCeremonialTeaRecipe implements IAction {
        @Override
        public void apply() {
            TeaNabeRecipe.ENABLE_CEREMONIAL_RECIPE = false;
        }

        @Override
        public String describe() {
            return "Disabling ceremonial tea recipe";
        }
    }
}
