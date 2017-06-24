package betterwithaddons.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigOptionLong {
    String name;
    String section;
    String description;
    long defaultValue;
    Property property;

    public ConfigOptionLong(String section, String name, long defaultValue, String description) {
        this.name = name;
        this.section = section;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public ConfigOptionLong(String section, String name, long defaultValue) {
        this(section,name, defaultValue, null);
    }

    public long init(Configuration configuration)
    {
        property = configuration.get(section, name, defaultValue);
        property.setComment(description);
        return property.getLong();
    }

    public long getValue()
    {
        return property.getLong();
    }
}
