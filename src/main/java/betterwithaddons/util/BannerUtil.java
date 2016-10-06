package betterwithaddons.util;

import net.minecraft.block.BlockBanner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Christian on 03.08.2016.
 */
public class BannerUtil {
    public static ItemStack getBannerItemFromBlock(World world, BlockPos pos)
    {
        IBlockState blockstate = world.getBlockState(pos);
        if (blockstate.getBlock() instanceof BlockBanner) {
            BlockBanner bannerblock = (BlockBanner) blockstate.getBlock();
            return bannerblock.getItem(world, pos, blockstate);
        }
        return null;
    }

    public static boolean isSameBanner(ItemStack bannerA, ItemStack bannerB)
    {
        if(bannerA.getItem() instanceof ItemBanner && bannerB.getItem() instanceof ItemBanner) {
            boolean baseequal = ItemBanner.getBaseColor(bannerA) == ItemBanner.getBaseColor(bannerB);
            NBTTagList patternsA = null;
            NBTTagList patternsB = null;

            if(bannerA.hasTagCompound() && bannerA.getTagCompound().hasKey("BlockEntityTag", 10)) {
                NBTTagCompound compound = bannerA.getTagCompound().getCompoundTag("BlockEntityTag");
                if (compound.hasKey("Patterns")) {
                    patternsA = compound.getTagList("Patterns", 10);
                }
            }
            if(bannerB.hasTagCompound() && bannerB.getTagCompound().hasKey("BlockEntityTag", 10)) {
                NBTTagCompound compound = bannerB.getTagCompound().getCompoundTag("BlockEntityTag");
                if (compound.hasKey("Patterns")) {
                    patternsB = compound.getTagList("Patterns", 10);
                }
            }

            //this is shitty.
            boolean bothnull = (patternsA == null || patternsA.tagCount() == 0) && (patternsB == null || patternsB.tagCount() == 0);

            return baseequal && (bothnull || patternsA.equals(patternsB));
        }

        return false;
    }

    public static boolean isSameBanner(ItemStack banner, Entity bannerHolder)
    {
        if(bannerHolder instanceof EntityLivingBase) {
            ItemStack helmet = ((EntityLivingBase) bannerHolder).getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if(helmet != null && helmet.getItem() instanceof ItemBanner)
            {
                return isSameBanner(banner,helmet);
            }
        }

        return false;
    }
}
