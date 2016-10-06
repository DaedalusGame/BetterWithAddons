package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockBase extends Block
{
    protected BlockBase(String name, Material materialIn)
    {
        super(materialIn);

        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        GameRegistry.register(new ItemBlockMeta(this).setRegistryName(this.getRegistryName()));
    }

    protected BlockBase(String name, Material materialIn, Class<? extends ItemBlock> itemBlock)
    {
        super(materialIn);

        this.setUnlocalizedName(name);
        this.setRegistryName(new ResourceLocation(Reference.MOD_ID, name));
        this.setCreativeTab(BetterWithAddons.instance.creativeTab);

        GameRegistry.register(this);
        try
        {
            GameRegistry.register(itemBlock.getConstructor(Block.class).newInstance(this).setRegistryName(this.getRegistryName()));
        }
        catch (Exception e)
        {
            System.out.println("Error Registering ItemBlock for " + name);
            e.printStackTrace();
        }
    }
}
