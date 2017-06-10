package betterwithaddons.item;

import betterwithaddons.handler.AssortedHandler;
import betterwithaddons.util.IDisableable;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemWorldScale extends Item implements IDisableable {

    private boolean disabled;

    public ItemWorldScale() {
        this.addPropertyOverride(new ResourceLocation("shine"), new IItemPropertyGetter() {
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                if (entityIn == null && !stack.isOnItemFrame()) {
                    return 0.0F;
                } else {
                    boolean flag = entityIn != null;
                    Entity entity = flag ? entityIn : stack.getItemFrame();

                    if (worldIn == null) {
                        worldIn = entity.world;
                    }

                    float d0;
                    BlockPos pos0 = entity.getPosition();

                    if (worldIn.provider.getDimension() == 0 && AssortedHandler.doScaleQuarriesExist()) {
                        float meatcontent = 0F;

                        //TODO: This is hacky, rework this
                        for (int i = 0; i < AssortedHandler.ScaleQuarries.length; i++) {
                            BlockPos pos = AssortedHandler.ScaleQuarries[i];
                            meatcontent += getMeatContent(pos0, pos);
                        }

                        if (meatcontent > 1.0 / (10 * 10))
                            d0 = 7;
                        else if (meatcontent > 1.0 / (20 * 20))
                            d0 = 6;
                        else if (meatcontent > 1.0 / (100 * 100))
                            d0 = 5;
                        else if (meatcontent > 1.0 / (250 * 250))
                            d0 = 4;
                        else if (meatcontent > 1.0 / (500 * 500))
                            d0 = 3;
                        else if (meatcontent > 1.0 / (750 * 750))
                            d0 = 2;
                        else if (meatcontent > 1.0 / (1000 * 1000))
                            d0 = 1;
                        else
                            d0 = 0;
                    } else {
                        d0 = 0;
                    }

                    return d0;
                }
            }

            private double getMeatContent(BlockPos pos0, BlockPos pos) {
                long dist = sqrDistance(pos.getX() - pos0.getX(), pos.getY() - pos0.getY(), pos.getZ() - pos0.getZ());
                return dist > 0 ? 1.0 / dist : 1.0;
            }

            long sqrDistance(long x, long y, long z) {
                return x * x + y * y + z * z;
            }
        });
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        //TODO: Set this up so it has an enchantment glint if we're closer than x blocks (is this possible????)
        return false;
    }

    @Override
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        if(!disabled)
            super.getSubItems(itemIn, tab, subItems);
    }
}
