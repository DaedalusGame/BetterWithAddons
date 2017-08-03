package betterwithaddons.interaction;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class ModInteractions {
    public static ArrayList<Interaction> LIST = new ArrayList<>();

    public static InteractionBWA bwa;
    public static InteractionBWM bwm;
    public static InteractionQuark quark;
    public static InteractionJEI jei;
    public static InteractionEriottoMod eriottoMod;
    public static InteractionCondensedOutputs condensedOutputs;
    public static InteractionDecoAddon decoAddon;
    public static InteractionBTWTweak btwTweak;
    public static InteractionMinetweaker minetweaker;
    public static InteractionBWR betterWithRenewables;
    public static InteractionWheat betterWithWheat;

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
    {
        LIST.stream().filter(Interaction::isActive).forEach(interaction -> interaction.modifyRecipes(event));
    }

    public static void preInit(FMLPreInitializationEvent event) {
        bwa = (InteractionBWA) addInteraction(new InteractionBWA());
        bwm = (InteractionBWM) addInteraction(new InteractionBWM());
        quark = (InteractionQuark) addInteraction(new InteractionQuark());
        jei = (InteractionJEI) addInteraction(new InteractionJEI());
        eriottoMod = (InteractionEriottoMod) addInteraction(new InteractionEriottoMod());
        condensedOutputs = (InteractionCondensedOutputs) addInteraction(new InteractionCondensedOutputs());
        decoAddon = (InteractionDecoAddon) addInteraction(new InteractionDecoAddon());
        btwTweak = (InteractionBTWTweak) addInteraction(new InteractionBTWTweak());
        betterWithRenewables = (InteractionBWR) addInteraction(new InteractionBWR());
        betterWithWheat = (InteractionWheat) addInteraction(new InteractionWheat());
        minetweaker = (InteractionMinetweaker) addInteraction(new InteractionMinetweaker());

        validate();

        LIST.stream().filter(Interaction::isActive).forEach(Interaction::preInit);
    }

    public static void preInitEnd(FMLPreInitializationEvent event) {
        LIST.stream().filter(Interaction::isActive).forEach(Interaction::preInitEnd);
    }

    public static void init(FMLInitializationEvent event) {
        LIST.stream().filter(Interaction::isActive).forEach(Interaction::init);
    }

    public static void postInit(FMLPostInitializationEvent event) {
        LIST.stream().filter(Interaction::isActive).forEach(Interaction::postInit);
    }

    private static Interaction addInteraction(Interaction interaction) {
        LIST.add(interaction);
        return interaction;
    }

    private static void validate() {
        for (Interaction interaction: LIST) {
            if(interaction.getDependencies() != null)
            for(Interaction dependency: interaction.getDependencies())
            {
                if(!dependency.isActive())
                {
                    interaction.setEnabled(false);
                    break;
                }
            }

            if(interaction.getIncompatibilities() != null)
            for(Interaction incompatibility: interaction.getIncompatibilities())
            {
                if(incompatibility.isActive())
                {
                    interaction.setEnabled(false);
                    break;
                }
            }
        }
    }
}
