package betterwithaddons.interaction;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class InteractionHelper {
    public static ItemStack findItem(String modid, String name, int amt, int meta)
    {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(modid,name)), amt, meta);
    }

    public static ItemStack findBlock(String modid, String name, int amt, int meta)
    {
        return new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modid,name)), amt, meta);
    }
}
