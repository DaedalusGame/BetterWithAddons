package betterwithaddons.util;

import betterwithaddons.item.ItemTeaCup;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class NabeResultTea extends NabeResult {
    public int doses;
    public int maxDoses;
    public ArrayList<PotionEffect> effects = new ArrayList<>();
    public TeaType mainType;
    public int strength; //5 levels
    public int milk; //4 levels
    public int sugar; //4 levels
    public boolean isCeremonial;

    public NabeResultTea() {
        super(new ResourceLocation(Reference.MOD_ID,"blocks/nabe_liquid"),0);
    }

    public NabeResultTea(int color, int doses, int maxDoses)
    {
        super(new ResourceLocation(Reference.MOD_ID,"blocks/nabe_liquid"),color);
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
    public StackResult take(ItemStack container) {
        if(doses <= 0)
            return new StackResult(false,container);
        if(container.getItem() == ModItems.TEA_CUP && !ItemTeaCup.isFilled(container)) {
            container.shrink(1);
            doses--;
            return new StackResult(true,container,ModItems.TEA_CUP.getFilled(this));
        }
        return new StackResult(false,container);
    }

    @Override
    public NabeResult copy() {
        NabeResultTea result = new NabeResultTea(color, doses, maxDoses);
        result.mainType = mainType;
        result.strength = strength;
        result.milk = milk;
        result.sugar = sugar;
        result.effects.addAll(effects);
        return result;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setString("teaType",mainType.getName());
        compound.setInteger("doses",doses);
        compound.setInteger("maxDoses",maxDoses);
        compound.setInteger("strength",strength);
        compound.setInteger("milk",milk);
        compound.setInteger("sugar",sugar);
        compound.setTag("effects", ItemUtil.serializePotionEffects(effects));
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        mainType = TeaType.getType(compound.getString("teaType"));
        doses = compound.getInteger("doses");
        maxDoses = compound.getInteger("maxDoses");
        strength = compound.getInteger("strength");
        milk = compound.getInteger("milk");
        sugar = compound.getInteger("sugar");
        effects.addAll(ItemUtil.deserializePotionEffects(compound.getTagList("effects", 10)));
    }
}
