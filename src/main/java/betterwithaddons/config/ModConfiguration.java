package betterwithaddons.config;

import betterwithaddons.interaction.ModInteractions;
import betterwithmods.module.ModuleLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ModConfiguration {
    public static Configuration configuration;
    private static boolean needsRestart = true;

    public static int loadPropInt(String propName, String category, String desc, int default_, int min, int max) {
        Property prop = ModuleLoader.config.get(category, propName, default_, desc, min, max);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getInt(default_);
    }

    public static int loadPropInt(String propName, String category, String desc, int default_) {
        Property prop = ModuleLoader.config.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getInt(default_);
    }

    public static double loadPropDouble(String propName, String category, String desc, double default_) {
        Property prop = ModuleLoader.config.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getDouble(default_);
    }

    public static double loadPropDouble(String propName, String category, String desc, double default_, double min, double max) {
        Property prop = ModuleLoader.config.get(category, propName, default_, desc, min, max);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getDouble(default_);
    }

    public static boolean loadPropBool(String propName, String category, String desc, boolean default_) {
        Property prop = ModuleLoader.config.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);

        return prop.getBoolean(default_);
    }

    public static String[] loadPropStringList(String propName, String category, String desc, String[] default_) {
        Property prop = ModuleLoader.config.get(category, propName, default_);
        prop.setComment(desc);
        setNeedsRestart(prop);
        return prop.getStringList();
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
        configuration = new Configuration(event.getSuggestedConfigurationFile());
        configuration.load();

        configuration.getCategory("interaction").setComment("Enable or disable mod interactions.");
        configuration.getCategory("addons").setComment("Configure individual addons.");

        ModInteractions.setupConfig();

        if (configuration.hasChanged())
            configuration.save();
    }

    public void init(FMLInitializationEvent event)
    {

    }
}
