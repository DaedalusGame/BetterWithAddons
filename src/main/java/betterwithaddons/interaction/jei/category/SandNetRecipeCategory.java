package betterwithaddons.interaction.jei.category;

import betterwithaddons.lib.Reference;
import mezz.jei.api.IGuiHelper;
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
