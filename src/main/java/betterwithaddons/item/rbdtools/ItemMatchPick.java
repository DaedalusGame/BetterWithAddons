package betterwithaddons.item.rbdtools;

import betterwithaddons.util.ItemUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMatchPick extends ItemPickaxeConvenient {
    public ItemMatchPick(ToolMaterial material) {
        super(material);
    }

    @Override
    public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return ItemUtil.matchesOreDict(stack, "torch");
    }

}
