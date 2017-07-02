package betterwithaddons.interaction.jei;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.gui.GuiDryingBox;
import betterwithaddons.client.gui.GuiSoakingBox;
import betterwithaddons.client.gui.GuiTatara;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.crafting.recipes.*;
import betterwithaddons.interaction.jei.category.*;
import betterwithaddons.interaction.jei.wrapper.CherryBoxRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.NetRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.SpindleRecipeWrapper;
import betterwithaddons.interaction.jei.wrapper.TataraRecipeWrapper;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
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
                new SpindleRecipeCategory(guiHelper)
        );

        reg.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, SandNetRecipeCategory.UID);
        reg.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, WaterNetRecipeCategory.UID);
        reg.handleRecipes(NetRecipe.class, NetRecipeWrapper::new, FireNetRecipeCategory.UID);
        reg.handleRecipes(SpindleRecipe.class, SpindleRecipeWrapper::new, SpindleRecipeCategory.UID);
        reg.handleRecipes(CherryBoxRecipe.class, CherryBoxRecipeWrapper::new, SoakingBoxRecipeCategory.UID);
        reg.handleRecipes(CherryBoxRecipe.class, CherryBoxRecipeWrapper::new, DryingBoxRecipeCategory.UID);
        reg.handleRecipes(SmeltingRecipe.class, TataraRecipeWrapper::new, TataraRecipeCategory.UID);

        reg.addRecipes(CraftingManagerSandNet.getInstance().getRecipes(),SandNetRecipeCategory.UID);
        reg.addRecipes(CraftingManagerWaterNet.getInstance().getRecipes(),WaterNetRecipeCategory.UID);
        reg.addRecipes(CraftingManagerFireNet.getInstance().getRecipes(),FireNetRecipeCategory.UID);
        reg.addRecipes(CraftingManagerSpindle.getInstance().getRecipes(),SpindleRecipeCategory.UID);
        reg.addRecipes(CraftingManagerDryingBox.instance().getRecipes(),DryingBoxRecipeCategory.UID);
        reg.addRecipes(CraftingManagerSoakingBox.instance().getRecipes(),SoakingBoxRecipeCategory.UID);
        reg.addRecipes(CraftingManagerTatara.instance().getRecipes(),TataraRecipeCategory.UID);

        //reg.addRecipes(JEIRecipeRegistry.getTataraRecipes(CraftingManagerTatara.instance()));
        //reg.addRecipes(JEIRecipeRegistry.getNetRecipes(CraftingManagerFireNet.getInstance()));
        //reg.addRecipes(JEIRecipeRegistry.getNetRecipes(CraftingManagerSandNet.getInstance()));
        //reg.addRecipes(JEIRecipeRegistry.getNetRecipes(CraftingManagerWaterNet.getInstance()));
        //reg.addRecipes(JEIRecipeRegistry.getCherryBoxRecipes(CraftingManagerSoakingBox.instance()));
        //reg.addRecipes(JEIRecipeRegistry.getCherryBoxRecipes(CraftingManagerDryingBox.instance()));
        //reg.addRecipes(JEIRecipeRegistry.getSpindleRecipes(CraftingManagerSpindle.getInstance()));

        reg.addRecipeCatalyst(new ItemStack(ModBlocks.tatara), TataraRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.cherrybox, 1, 0), SoakingBoxRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.cherrybox, 1, 1), DryingBoxRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.nettedScreen), WaterNetRecipeCategory.UID, SandNetRecipeCategory.UID, FireNetRecipeCategory.UID);
        reg.addRecipeCatalyst(new ItemStack(ModBlocks.spindle), SpindleRecipeCategory.UID);

        reg.addRecipeClickArea(GuiTatara.class, 78, 32, 28, 23, TataraRecipeCategory.UID);
        reg.addRecipeClickArea(GuiSoakingBox.class, 78, 32, 28, 23, SoakingBoxRecipeCategory.UID);
        reg.addRecipeClickArea(GuiDryingBox.class, 78, 32, 28, 23, DryingBoxRecipeCategory.UID);
    }
}
