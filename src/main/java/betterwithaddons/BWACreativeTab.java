package betterwithaddons;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BWACreativeTab extends CreativeTabs
{
    public BWACreativeTab()
    {
        super("bwa.name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem()
    {
        return new ItemStack(Items.BEETROOT);
    }

}