package betterwithaddons.interaction.jei.category;

import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Christian on 26.09.2016.
 */
public class SoakingBoxRecipeCategory extends CherryBoxRecipeCategory {
    public SoakingBoxRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    protected ResourceLocation getBackgroundResource() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/soakingbox.png");
    }

    @Override
    public String getUid() {
        return "bwa.soakingbox";
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.soakingbox.name");
    }
}
