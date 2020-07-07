package betterwithaddons.interaction.jei;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.gui.GuiDryingBox;
import betterwithaddons.client.gui.GuiInfuser;
import betterwithaddons.client.gui.GuiSoakingBox;
import betterwithaddons.client.gui.GuiTatara;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.crafting.recipes.*;
import betterwithaddons.crafting.recipes.infuser.InfuserRecipe;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import betterwithaddons.interaction.jei.category.*;
import betterwithaddons.interaction.jei.wrapper.*;
import betterwithaddons.item.ModItems;
import com.google.common.collect.Lists;
import crafttweaker.mc1120.recipes.MCRecipeShaped;
import crafttweaker.mc1120.recipes.MCRecipeShapeless;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShaped;
import crafttweaker.mods.jei.recipeWrappers.CraftingRecipeWrapperShapeless;
import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapedRecipesWrapper;
import mezz.jei.plugins.vanilla.crafting.ShapelessRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@mezz.jei.api.JEIPlugin
public class BWAJEIPlugin implements IModPlugin {
    public static IJeiHelpers HELPER;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(ModItems.TEA_LEAVES);
        subtypeRegistry.useNbtForSubtypes(ModItems.TEA_SOAKED);
        subtypeRegistry.useNbtForSubtypes(ModItems.TEA_WILTED);
        subtypeRegistry.useNbtForSubtypes(ModItems.TEA_POWDER);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        HELPER = registry.getJeiHelpers();

        IJeiHelpers helper = registry.getJeiHelpers();
        IGuiHelper guiHelper = helper.getGuiHelper();

        registry.addRecipeCategories(new TataraRecipeCategory(guiHelper),
                new FireNetRecipeCategory(guiHelper),
                new WaterNetRecipeCategory(guiHelper),
                new SandNetRecipeCategory(guiHelper),
                new SoakingBoxRecipeCategory(guiHelper),
                new DryingBoxRecipeCategory(guiHelper),
                new SpindleRecipeCategory(guiHelper),
                new PackingRecipeCategory(guiHelper),
                new InfuserRecipeCategory(guiHelper),
                new TransmutationRecipeCategory(guiHelper),
                new NabeCategory(guiHelper));
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        IJeiHelpers helper = registry.getJeiHelpers();
        IGuiHelper guiHelper = helper.getGuiHelper();

        registry.handleRecipes(SpindleRecipe.class, SpindleRecipeWrapper::new, SpindleRecipeCategory.UID);
        registry.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, SandNetRecipeCategory.UID);
        registry.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, WaterNetRecipeCategory.UID);
        registry.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, FireNetRecipeCategory.UID);
        registry.handleRecipes(CherryBoxRecipe.class, CherryBoxRecipeWrapper::new, SoakingBoxRecipeCategory.UID);
        registry.handleRecipes(CherryBoxRecipe.class, CherryBoxRecipeWrapper::new, DryingBoxRecipeCategory.UID);
        registry.handleRecipes(SmeltingRecipe.class, SmeltingRecipeWrapper::new, TataraRecipeCategory.UID);
        registry.handleRecipes(TransmutationRecipe.class, TransmutationRecipeWrapper::new, TransmutationRecipeCategory.UID);
        registry.handleRecipes(PackingRecipe.class, PackingRecipeWrapper::new, PackingRecipeCategory.UID);
        registry.handleRecipes(NabeRecipeVisual.class, NabeWrapper::new, NabeCategory.UID);

        registry.handleRecipes(InfuserRecipe.class, recipe -> new InfuserRecipeWrapper(getCraftingRecipeWrapper(helper, recipe.internal),recipe.getRecipeRequiredSpirit()), InfuserRecipeCategory.UID);

        registry.addRecipes(CraftingManagerSpindle.getInstance().getRecipes(),SpindleRecipeCategory.UID);
        registry.addRecipes(CraftingManagerSandNet.getInstance().getRecipes(),SandNetRecipeCategory.UID);
        registry.addRecipes(CraftingManagerWaterNet.getInstance().getRecipes(),WaterNetRecipeCategory.UID);
        registry.addRecipes(CraftingManagerFireNet.getInstance().getRecipes(),FireNetRecipeCategory.UID);
        registry.addRecipes(CraftingManagerDryingBox.instance().getRecipes(),DryingBoxRecipeCategory.UID);
        registry.addRecipes(CraftingManagerSoakingBox.instance().getRecipes(),SoakingBoxRecipeCategory.UID);
        registry.addRecipes(CraftingManagerTatara.instance().getRecipes(),TataraRecipeCategory.UID);
        registry.addRecipes(CraftingManagerInfuser.getInstance().getRecipeList(),InfuserRecipeCategory.UID);
        registry.addRecipes(CraftingManagerInfuserTransmutation.getInstance().getRecipes(),TransmutationRecipeCategory.UID);
        registry.addRecipes(CraftingManagerPacking.getInstance().getRecipes(),PackingRecipeCategory.UID);
        registry.addRecipes(CraftingManagerNabe.getInstance().getVisualRecipes(),NabeCategory.UID);

        registry.addRecipeCatalyst(new ItemStack(ModBlocks.TATARA), TataraRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.CHERRY_BOX, 1, 0), SoakingBoxRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.CHERRY_BOX, 1, 1), DryingBoxRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.NETTED_SCREEN), WaterNetRecipeCategory.UID, SandNetRecipeCategory.UID, FireNetRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.SPINDLE), SpindleRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(Blocks.PISTON), PackingRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSER), InfuserRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.INFUSER), TransmutationRecipeCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.NABE), NabeCategory.UID);

        registry.addRecipeClickArea(GuiTatara.class, 78, 32, 28, 23, TataraRecipeCategory.UID);
        registry.addRecipeClickArea(GuiSoakingBox.class, 78, 32, 28, 23, SoakingBoxRecipeCategory.UID);
        registry.addRecipeClickArea(GuiDryingBox.class, 78, 32, 28, 23, DryingBoxRecipeCategory.UID);
        registry.addRecipeClickArea(GuiInfuser.class, 94, 35, 19, 16, InfuserRecipeCategory.UID);
    }

    private IRecipeWrapper getCraftingRecipeWrapper(IJeiHelpers helper, IRecipe recipe)
    {
        if(recipe instanceof ShapedOreRecipe)
            return new ShapedOreRecipeWrapper(helper, (ShapedOreRecipe) recipe);
        if(recipe instanceof ShapedRecipes)
            return new ShapedRecipesWrapper(helper, (ShapedRecipes) recipe);
        if(recipe instanceof ShapelessOreRecipe || recipe instanceof ShapelessRecipes)
            return new ShapelessRecipeWrapper<>(helper, recipe);
        if(recipe instanceof MCRecipeShaped)
            return new CraftingRecipeWrapperShaped((MCRecipeShaped) recipe);
        if(recipe instanceof MCRecipeShapeless)
            return new CraftingRecipeWrapperShapeless((MCRecipeShapeless) recipe);
        return null;
    }

    public static List<ItemStack> flatExpand(Ingredient ingredient) {
        return expand(ingredient).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static List<ItemStack> flatExpand(List<Ingredient> ingredients) {
        return expand(ingredients).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static List<List<ItemStack>> expand(Ingredient ingredient) {
        return expand(Lists.newArrayList(ingredient));
    }

    public static List<List<ItemStack>> expand(List<Ingredient> ingredients) {
        IStackHelper stackHelper = HELPER.getStackHelper();
        return stackHelper.expandRecipeItemStackInputs(ingredients);
    }
}
