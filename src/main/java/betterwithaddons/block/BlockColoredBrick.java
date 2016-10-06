package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IHasVariants;
import net.minecraft.block.BlockColored;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemCloth;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 17.09.2016.
 */
public class BlockColoredBrick extends BlockColored implements IHasVariants {
    public BlockColoredBrick()
    {
        super(Material.ROCK);

        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.setUnlocalizedName("bricks_stained");

        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, "bricks_stained"));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        GameRegistry.register(new ItemCloth(this).setRegistryName(this.getRegistryName()).setHasSubtypes(true));
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
}
