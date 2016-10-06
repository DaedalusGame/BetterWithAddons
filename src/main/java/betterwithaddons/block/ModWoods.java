package betterwithaddons.block;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Christian on 12.09.2016.
 */
public enum ModWoods implements IStringSerializable
{
    MULBERRY,SAKURA;

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }
    @Override
    public String toString()
    {
        return this.getName();
    }
}