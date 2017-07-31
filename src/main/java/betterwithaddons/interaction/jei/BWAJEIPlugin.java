package betterwithaddons.interaction.jei;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.gui.GuiDryingBox;
import betterwithaddons.client.gui.GuiInfuser;
import betterwithaddons.client.gui.GuiSoakingBox;
import betterwithaddons.client.gui.GuiTatara;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.crafting.recipes.*;
import betterwithaddons.crafting.recipes.infuser.ShapedInfuserRecipe;
import betterwithaddons.crafting.recipes.infuser.ShapelessInfuserRecipe;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import betterwithaddons.interaction.jei.category.*;
import betterwithaddons.interaction.jei.wrapper.*;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.plugins.vanilla.crafting.ShapedOreRecipeWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@mezz.jei.api.JEIPlugin
public class BWAJEIPlugin extends BlankModPlugin {
    @Override
    public void register(@Nonnull IModRegistry reg) {
        IJeiHelpers helper = reg.getJeiHelpers();
        IGuiHelper guiHelper = helper.getGuiHelper();

        reg.addRecipeCategories(
                new TataraRecipeCategory(guiHelper),
                new FireNetRecipeCategory(guiHelper),
                new WaterNetRecipeCategory(guiHelper),
                new SandNetRecipeCategory(guiHelper),
                new SoakingBoxRecipeCategory(guiHelper),
                new DryingBoxRecipeCategory(guiHelper),
                new SpindleRecipeCategory(guiHelper),
                new InfuserRecipeCategory(guiHelper),
                new TransmutationRecipeCategory(guiHelper)
        );

        reg.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, SandNetRecipeCategory.UID);
        reg.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, WaterNetRecipeCategory.UID);
        reg.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, FireNetRecipeCategory.UID);
        reg.handleRecipes(SpindleRecipe.class, SpindleRecipeWrapper::new, SpindleRecipeCategory.UID);
        reg.handleRecipes(CherryBoxRecipe.class, CherryBoxRecipeWrapper::new, SoakingBoxRecipeCategory.UID);
        reg.handleRecipes(CherryBoxRecipe.class, CherryBoxRecipeWrapper::new, DryingBoxRecipeCategory.UID);
        reg.handleRecipes(SmeltingRecipe.class, SmeltingRecipeWrapper::new, TataraRecipeCategory.UID);
        reg.handleRecipes(TransmutationRecipe.class, TransmutationRecipeWrapper::new, TransmutationRecipeCategory.UID);

        reg.handleRecipes(ShapedInfuserRecipe.class, recipe -> new InfuserRecipeWrapper(new ShapedOreRecipeWrapper(helper, recipe),recipe.getRecipeRequiredSpirit()), InfuserRecipeCategory.UID);
        //reg.handleRecipes(ShapelessInfuserRecipe.class, recipe -> new InfuserRecipeWrapper(new ShapelessOreRecipeWrapper(helper, recipe),recipe.getRecipeRequiredSpirit()), InfuserRecipeCategory.UID);

        reg.addRecipes(CraftingManagerSandNet.getInstance().getRecipes(),SandNetRecipeCategory.UID);
        reg.addRecipes(CraftingManagerWaterNet.getInstance().getRecipes(),WaterNetRecipeCategory.UID);
        reg.addRecipes(CraftingManagerFireNet.getInstance().getRecipes(),FireNetRecipeCategory.UID);
        reg.addRecipes(CraftingManagerSpindle.getInstance().getRecipes(),SpindleRecipeCategory.UID);
        reg.addRecipes(CraftingManagerDryingBox.instance().getRecipes(),DryingBoxRecipeCategory.UID);
        reg.addRecipes(CraftingManagerSoakingBox.instance().getRecipes(),SoakingBoxRecipeCategory.UID);
        reg.addRecipes(CraftingManagerTatara.instance().getRecipes(),TataraRecipeCategory.UID);
        reg.addRecipes(CraftingManagerInfuser.getInstance().getRecipeList(),InfuserRecipeCategory.UID);
        reg.addRecipes(CraftingManagerInfuserTransmutation.instance().getRecipes(),TransmutationRecipeCategory.UID);

        reg.addRecipeCatalyst(new ItemStack(ModBlocks.tatara), TataraRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.cherrybox, 1, 0), SoakingBoxRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.cherrybox, 1, 1), DryingBoxRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.nettedScreen), WaterNetRecipeCategory.UID, SandNetRecipeCategory.UID, FireNetRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.spindle), SpindleRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.infuser), InfuserRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.infuser), TransmutationRecipeCategory.UID);

        reg.addRecipeClickArea(GuiTatara.class, 78, 32, 28, 23, TataraRecipeCategory.UID);
        reg.addRecipeClickArea(GuiSoakingBox.class, 78, 32, 28, 23, SoakingBoxRecipeCategory.UID);
        reg.addRecipeClickArea(GuiDryingBox.class, 78, 32, 28, 23, DryingBoxRecipeCategory.UID);
        reg.addRecipeClickArea(GuiInfuser.class, 94, 35, 19, 16, InfuserRecipeCategory.UID);
    }
}
