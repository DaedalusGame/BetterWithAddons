package betterwithaddons.interaction;

import betterwithaddons.interaction.minetweaker.*;
import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.Loader;

import java.util.List;

public class InteractionMinetweaker extends Interaction {
    final String modid = "crafttweaker";

    @Override
    public boolean isActive() {
        return Loader.isModLoaded(modid);
    }

    @Override
    public void setEnabled(boolean active) {}

    @Override
    public List<Interaction> getDependencies() {
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        MineTweakerAPI.registerClass(DryingBox.class);
        MineTweakerAPI.registerClass(SoakingBox.class);
        MineTweakerAPI.registerClass(SandNet.class);
        MineTweakerAPI.registerClass(WaterNet.class);
        MineTweakerAPI.registerClass(FireNet.class);
        MineTweakerAPI.registerClass(Spindle.class);
        MineTweakerAPI.registerClass(Tatara.class);
        MineTweakerAPI.registerClass(SoftWoods.class);
        MineTweakerAPI.registerClass(LureTree.class);
        MineTweakerAPI.registerClass(Condensed.class);
    }

    @Override
    public void postInit() {

    }
}
