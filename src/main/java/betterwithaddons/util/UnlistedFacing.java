package betterwithaddons.util;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedFacing implements IUnlistedProperty<EnumFacing>
{
    String name;

    private UnlistedFacing(String name)
    {
        this.name = name;
    }

    public static UnlistedFacing create(String name)
    {
        return new UnlistedFacing(name);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean isValid(EnumFacing value)
    {
        return true;
    }

    @Override
    public Class<EnumFacing> getType()
    {
        return EnumFacing.class;
    }

    @Override
    public String valueToString(EnumFacing value)
    {
        return value.getName();
    }
}