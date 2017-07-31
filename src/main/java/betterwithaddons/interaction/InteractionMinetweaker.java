package betterwithaddons.interaction;

import betterwithaddons.interaction.minetweaker.*;
import crafttweaker.CraftTweakerAPI;
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
        CraftTweakerAPI.registerClass(DryingBox.class);
        CraftTweakerAPI.registerClass(SoakingBox.class);
        CraftTweakerAPI.registerClass(SandNet.class);
        CraftTweakerAPI.registerClass(WaterNet.class);
        CraftTweakerAPI.registerClass(FireNet.class);
        CraftTweakerAPI.registerClass(Spindle.class);
        CraftTweakerAPI.registerClass(Tatara.class);
        CraftTweakerAPI.registerClass(SoftWoods.class);
        CraftTweakerAPI.registerClass(LureTree.class);
        CraftTweakerAPI.registerClass(Condensed.class);
    }

    @Override
    public void postInit() {

    }
}
