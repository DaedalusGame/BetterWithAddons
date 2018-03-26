package betterwithaddons.crafting.recipes;

import betterwithaddons.block.EriottoMod.BlockNettedScreen.SifterType;
import com.google.common.collect.Lists;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class NetRecipe
{
    public ArrayList<ItemStack> outputs = new ArrayList<ItemStack>();
    public Ingredient input = Ingredient.EMPTY;
    public int sandrequired = 0;

    public SifterType type = SifterType.NONE;

    public NetRecipe(SifterType type, Ingredient input, int sand, ItemStack... outputs)
    {
        this.sandrequired = sand;
        this.type = type;
        this.input = input;

        for (ItemStack out: outputs) {
            if(out.isEmpty())
                this.outputs.add(ItemStack.EMPTY);
            else
                this.outputs.add(out.copy());
        }
    }

    public ArrayList<ItemStack> getInput()
    {
        return Lists.newArrayList(input.getMatchingStacks());
    }

    public ArrayList<ItemStack> getOutput()
    {
        return this.outputs;
    }

    public int getSandRequired()
    {
        return this.sandrequired;
    }

    public SifterType getType() {
        return type;
    }

    public boolean matchesInput(EntityItem ent)
    {
        return matchesInput(ent.getItem());
    }

    public boolean matchesInput(ItemStack item)
    {
        return input.apply(item);
    }

    public boolean matches(List<EntityItem> inv)
    {
        for (EntityItem ent: inv) {
            if(matchesInput(ent))
                return true;
        }
        return false;
    }
}