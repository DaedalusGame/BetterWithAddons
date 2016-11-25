package betterwithaddons.interaction.jei.category;

import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
import mezz.jei.util.Translator;
import net.minecraft.util.ResourceLocation;

public class WaterNetRecipeCategory extends NetRecipeCategory {
    public WaterNetRecipeCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    protected ResourceLocation getBackgroundResource() {
        return new ResourceLocation(Reference.MOD_ID, "textures/gui/jei/waternet.png");
    }

    @Override
    public String getUid() {
        return "bwa.waternet";
    }

    @Override
    public String getTitle() {
        return Translator.translateToLocal("inv.waternet.name");
    }
}
