package betterwithaddons.interaction.jei.category;

import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

public class DryingBoxRecipeCategory extends CherryBoxRecipeCategory {
    public DryingBoxRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    protected ResourceLocation getBackgroundResource() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/dryingbox.png");
    }

    @Override
    public String getUid() {
        return "bwa.dryingbox";
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.dryingbox.name");
    }
}
