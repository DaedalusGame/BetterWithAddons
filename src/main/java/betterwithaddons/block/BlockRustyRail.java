package betterwithaddons.block;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.block.BlockRail;
import net.minecraft.block.SoundType;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockRustyRail extends BlockRail {
    public BlockRustyRail()
    {
        super();
        setRegistryName(new ResourceLocation(Reference.MOD_ID,"rail_rusted"));
        setHardness(0.7F);
        setHarvestLevel("pickaxe",0);
        setSoundType(SoundType.METAL);
        setUnlocalizedName("rail_rusted");
        setCreativeTab(BetterWithAddons.instance.creativeTab);
    }

    @Override
    public float getRailMaxSpeed(World world, EntityMinecart cart, BlockPos pos) {
        return 0.2f;
    }
}
