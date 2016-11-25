package betterwithaddons.crafting.manager;

import betterwithaddons.block.EriottoMod.BlockCherryBox.CherryBoxType;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

public abstract class CraftingManagerCherryBox {
    private final Map<ItemStack, ItemStack> workingList = Maps.newHashMap();

    public CraftingManagerCherryBox() {
    }

    public CherryBoxType getType() {
        return CherryBoxType.NONE;
    }

    public void addWorkingRecipe(ItemStack input, ItemStack output) {
        if(this.getWorkResult(input) != null) {
            FMLLog.info("Ignored cherrybox recipe with conflicting input: " + input + " = " + output, new Object[0]);
        } else {
            this.workingList.put(input, output);
        }
    }

    @Nullable
    public ItemStack getWorkResult(ItemStack input) {
        Iterator var2 = this.workingList.entrySet().iterator();

        Map.Entry entry;
        do {
            if(!var2.hasNext()) {
                return null;
            }

            entry = (Map.Entry)var2.next();
        } while(!this.compareItemStacks(input, (ItemStack)entry.getKey()));

        return (ItemStack)entry.getValue();
    }

    private boolean compareItemStacks(ItemStack p_compareItemStacks_1_, ItemStack p_compareItemStacks_2_) {
        return p_compareItemStacks_2_.getItem() == p_compareItemStacks_1_.getItem() && (p_compareItemStacks_2_.getMetadata() == OreDictionary.WILDCARD_VALUE || p_compareItemStacks_2_.getMetadata() == p_compareItemStacks_1_.getMetadata());
    }

    public Map<ItemStack, ItemStack> getWorkingList() {
        return this.workingList;
    }
}
