package betterwithaddons.block;

import net.minecraft.util.IStringSerializable;

public enum ModWoods implements IStringSerializable
{
    MULBERRY,SAKURA,LURETREE;

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