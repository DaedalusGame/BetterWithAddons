package betterwithaddons.util;

import betterwithaddons.item.ItemTeaCup;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class NabeResultPoison extends NabeResult {
    int doses;
    int maxDoses;

    public NabeResultPoison() {
        super(new ResourceLocation(Reference.MOD_ID,"blocks/nabe_liquid"), new Color(50,100,50).getRGB());
    }

    public NabeResultPoison(int doses, int maxDoses) {
        super(new ResourceLocation(Reference.MOD_ID,"blocks/nabe_liquid"), new Color(50,100,50).getRGB());
        this.doses = doses;
        this.maxDoses = maxDoses;
    }

    @Override
    public float getFillRatio() {
        return doses / (float)maxDoses;
    }

    @Override
    public boolean isFull() {
        return doses >= maxDoses;
    }

    @Override
    public ItemStack take(ItemStack container) {
        if(doses >= 1 && container.getItem() == ModItems.ya) {
            int consumed = Math.min(container.getCount(),doses);
            doses -= consumed;
            container.shrink(consumed);
            return new ItemStack(ModItems.yaPoisoned,consumed);
        }
        if(doses >= 4 && container.getItem() == ModItems.teaCup && !ItemTeaCup.isFilled(container)) {
            container.shrink(1);
            doses -= 4;
            return ModItems.teaCup.getFilled(this);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NabeResult copy() {
        return new NabeResultPoison(doses,maxDoses);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("doses",doses);
        compound.setInteger("maxDoses",maxDoses);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        doses = compound.getInteger("doses");
        maxDoses = compound.getInteger("maxDoses");
    }
}
