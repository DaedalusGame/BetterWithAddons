package betterwithaddons.interaction.jei.category;

import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Christian on 26.09.2016.
 */
public class FireNetRecipeCategory extends NetRecipeCategory {
    public FireNetRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    protected ResourceLocation getBackgroundResource() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/firenet.png");
    }

    @Override
    public String getUid() {
        return "bwa.firenet";
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.firenet.name");
    }
}
