package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IDisableable;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BlockColoredBrick extends BlockColored implements IHasVariants, IDisableable {
    private boolean disabled;

    public BlockColoredBrick()
    {
        super(Material.ROCK);

        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName("bricks_stained");

        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "bricks_stained"));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        //GameRegistry.register(this);
        //GameRegistry.register(new ItemCloth(this).setRegistryName(this.getRegistryName()).setHasSubtypes(true));
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> rlist = new ArrayList<ModelResourceLocation>();

        EnumDyeColor[] dyes = EnumDyeColor.values();
        int len = dyes.length;

        for(int i = 0; i < len; ++i) {
            EnumDyeColor dye = dyes[i];
            rlist.add(new ModelResourceLocation(getRegistryName(), "color="+dye.getName()));
        }

        return rlist;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        if(!disabled)
            super.getSubBlocks(itemIn, tab, list);
    }
}
