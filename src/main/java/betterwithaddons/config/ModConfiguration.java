package betterwithaddons.config;

import betterwithaddons.interaction.ModInteractions;
import betterwithaddons.lib.Reference;
import betterwithmods.BWMod;
import betterwithmods.module.ModuleLoader;
import com.google.common.collect.Sets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

public class ModConfiguration {
    public static Configuration configuration;
    private static boolean needsRestart = true;

    public static int loadPropInt(String propName, String category, String desc, int default_, int min, int max) {
        Property prop = configuration.get(category, propName, default_, desc, min, max);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getInt(default_);
    }

    public static int loadPropInt(String propName, String category, String desc, int default_) {
        Property prop = configuration.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getInt(default_);
    }

    public static double loadPropDouble(String propName, String category, String desc, double default_) {
        Property prop = configuration.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getDouble(default_);
    }

    public static double loadPropDouble(String propName, String category, String desc, double default_, double min, double max) {
        Property prop = configuration.get(category, propName, default_, desc, min, max);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getDouble(default_);
    }

    public static boolean loadPropBool(String propName, String category, String desc, boolean default_) {
        Property prop = configuration.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getBoolean(default_);
    }

    public static String[] loadPropStringList(String propName, String category, String desc, String[] default_) {
        Property prop = configuration.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);
        return prop.getStringList();
    }

    public static HashSet<String> loadPropStringSet(String propName, String category, String desc, String[] default_) {
        Property prop = configuration.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);
        return Sets.newHashSet(prop.getStringList());
    }

    public static void doesNotNeedRestart(Runnable op)
    {
        needsRestart = false;
        op.run();
        needsRestart = true;
    }

    private static void setNeedsRestart(Property prop) {
        if (needsRestart)
            prop.setRequiresMcRestart(true);
    }

    public void preInit(FMLPreInitializationEvent event)
    {
        configuration = new Configuration(event.getSuggestedConfigurationFile(),true);

        configuration.load();

        configuration.getCategory("interaction").setComment("Enable or disable mod interactions.");
        configuration.getCategory("addons").setComment("Configure individual addons.");

        MinecraftForge.EVENT_BUS.register(this);

        loadModuleConfig();
    }

    public void loadModuleConfig()
    {
        ModInteractions.setupConfig();

        if (configuration.hasChanged())
            configuration.save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.getModID().equals(Reference.MOD_ID))
            loadModuleConfig();
    }
}
