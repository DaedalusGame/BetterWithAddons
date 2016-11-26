package betterwithaddons.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigOption {
    String name;
    String section;
    String description;
    boolean defaultValue;
    Property property;

    public ConfigOption(String section, String name, boolean defaultValue, String description) {
        this.name = name;
        this.section = section;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public ConfigOption(String section, String name, boolean defaultValue) {
        this(section,name, defaultValue, null);
    }

    public boolean init(Configuration configuration)
    {
        property = configuration.get(section, name, defaultValue);
        property.setComment(description);
        return property.getBoolean();
    }

    public boolean getValue()
    {
        return property.getBoolean();
    }
}
