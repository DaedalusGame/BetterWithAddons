package betterwithaddons.interaction.jei;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.client.gui.GuiDryingBox;
import betterwithaddons.client.gui.GuiSoakingBox;
import betterwithaddons.client.gui.GuiTatara;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.interaction.jei.category.*;
import betterwithaddons.interaction.jei.handler.CherryBoxRecipeHandler;
import betterwithaddons.interaction.jei.handler.NetRecipeHandler;
import betterwithaddons.interaction.jei.handler.SpindleRecipeHandler;
import betterwithaddons.interaction.jei.handler.TataraRecipeHandler;
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


        reg.addRecipeHandlers(
                new TataraRecipeHandler(),
                new NetRecipeHandler(),
                new CherryBoxRecipeHandler(),
                new SpindleRecipeHandler()
        );

        reg.addRecipes(JEIRecipeRegistry.getTataraRecipes(CraftingManagerTatara.instance()));
        reg.addRecipes(JEIRecipeRegistry.getNetRecipes(CraftingManagerFireNet.getInstance()));
        reg.addRecipes(JEIRecipeRegistry.getNetRecipes(CraftingManagerSandNet.getInstance()));
        reg.addRecipes(JEIRecipeRegistry.getNetRecipes(CraftingManagerWaterNet.getInstance()));
        reg.addRecipes(JEIRecipeRegistry.getCherryBoxRecipes(CraftingManagerSoakingBox.instance()));
        reg.addRecipes(JEIRecipeRegistry.getCherryBoxRecipes(CraftingManagerDryingBox.instance()));
        reg.addRecipes(JEIRecipeRegistry.getSpindleRecipes(CraftingManagerSpindle.getInstance()));

        reg.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.tatara), "bwa.tatara");
        reg.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.cherrybox, 1, 0), "bwa.soakingbox");
        reg.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.cherrybox, 1, 1), "bwa.dryingbox");
        reg.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.nettedScreen), "bwa.waternet", "bwa.sandnet", "bwa.firenet");
        reg.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.spindle), "bwa.spindle");

        reg.addRecipeClickArea(GuiTatara.class, 78, 32, 28, 23, "bwa.tatara");
        reg.addRecipeClickArea(GuiSoakingBox.class, 78, 32, 28, 23, "bwa.soakingbox");
        reg.addRecipeClickArea(GuiDryingBox.class, 78, 32, 28, 23, "bwa.dryingbox");
    }
}
