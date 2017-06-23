package betterwithaddons.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigOptionDouble {
    String name;
    String section;
    String description;
    double defaultValue;
    Property property;

    public ConfigOptionDouble(String section, String name, double defaultValue, String description) {
        this.name = name;
        this.section = section;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public ConfigOptionDouble(String section, String name, double defaultValue) {
        this(section,name, defaultValue, null);
    }

    public double init(Configuration configuration)
    {
        property = configuration.get(section, name, defaultValue);
        property.setComment(description);
        return property.getDouble();
    }

    public double getValue()
    {
        return property.getDouble();
    }
}
