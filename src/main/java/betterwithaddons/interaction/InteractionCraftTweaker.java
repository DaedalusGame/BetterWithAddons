package betterwithaddons.interaction;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import net.minecraftforge.fml.common.Loader;

import java.util.LinkedList;
import java.util.List;

public class InteractionCraftTweaker extends Interaction {
    final String modid = "crafttweaker";

    private static List<IAction> LATE_REMOVALS = new LinkedList<>();
    private static List<IAction> LATE_ADDITIONS = new LinkedList<>();

    @Override
    boolean isActive() {
        return Loader.isModLoaded(modid);
    }

    @Override
    void loadComplete() {
        try {
            LATE_REMOVALS.forEach(CraftTweakerAPI::apply);
            LATE_ADDITIONS.forEach(CraftTweakerAPI::apply);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getName() {
        return "interaction.CraftTweaker";
    }
}
