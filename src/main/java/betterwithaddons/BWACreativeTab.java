package betterwithaddons;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
    public Item getTabIconItem()
    {
        return Items.BEETROOT;
    }

}