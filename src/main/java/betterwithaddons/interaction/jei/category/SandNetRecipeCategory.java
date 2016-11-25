package betterwithaddons.interaction.jei.category;

import betterwithaddons.interaction.jei.wrapper.NetRecipeWrapper;
import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

public class SandNetRecipeCategory extends NetRecipeCategory {
    public SandNetRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    protected ResourceLocation getBackgroundResource() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/sandnet.png");
    }

    @Override
    public String getUid() {
        return "bwa.sandnet";
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.sandnet.name");
    }
}
