package betterwithaddons.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigOptionInteger {
    String name;
    String section;
    String description;
    int defaultValue;
    Property property;

    public ConfigOptionInteger(String section, String name, int defaultValue, String description) {
        this.name = name;
        this.section = section;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public ConfigOptionInteger(String section, String name, int defaultValue) {
        this(section,name, defaultValue, null);
    }

    public int init(Configuration configuration)
    {
        property = configuration.get(section, name, defaultValue);
        property.setComment(description);
        return property.getInt();
    }

    public int getValue()
    {
        return property.getInt();
    }
}
