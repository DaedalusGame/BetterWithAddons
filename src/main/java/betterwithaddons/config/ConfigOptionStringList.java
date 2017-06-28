package betterwithaddons.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigOptionStringList {
    String name;
    String section;
    String description;
    String[] defaultValue;
    Property property;

    public ConfigOptionStringList(String section, String name, String[] defaultValue, String description) {
        this.name = name;
        this.section = section;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public ConfigOptionStringList(String section, String name, String[] defaultValue) {
        this(section,name, defaultValue, null);
    }

    public String[] init(Configuration configuration)
    {
        property = configuration.get(section, name, defaultValue);
        property.setComment(description);
        return property.getStringList();
    }

    public String[] getValue()
    {
        return property.getStringList();
    }
}
