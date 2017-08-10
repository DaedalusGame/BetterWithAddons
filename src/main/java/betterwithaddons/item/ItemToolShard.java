package betterwithaddons.item;

import betterwithaddons.block.ColorHandlers;
import betterwithaddons.block.IColorable;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemToolShard extends Item implements IColorable {
    public static final String INNER_TAG = "Inner";
    /*@CapabilityInject(ArtifactData.class)
    public static Capability<ArtifactData> DATA_CAP;

    public static class ArtifactData implements ICapabilitySerializable<NBTTagCompound>
    {
        public ItemStack inner = ItemStack.EMPTY;

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == DATA_CAP;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            return hasCapability(capability, facing) ? (T) this : null;
        }

        @Override
        public NBTTagCompound serializeNBT()
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("Inner", inner.serializeNBT());
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            inner = new ItemStack(nbt.getCompoundTag("Inner"));
        }
    }*/

    public ItemToolShard()
    {
        //registry stuff
        /*CapabilityManager.INSTANCE.register(ArtifactData.class, new Capability.IStorage<ArtifactData>()
        {
            @Override
            public NBTBase writeNBT(Capability<ArtifactData> capability, ArtifactData instance, EnumFacing side)
            {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<ArtifactData> capability, ArtifactData instance, EnumFacing side, NBTBase nbt)
            {
                instance.deserializeNBT((NBTTagCompound) nbt);
            }
        }, ArtifactData::new);*/
    }

    /*@Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ArtifactData();
    }*/

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        return;
    }

    public ItemStack getInnerStack(ItemStack stack)
    {
        if(stack.getItem() != this)
            return ItemStack.EMPTY;

        NBTTagCompound compound = stack.getTagCompound();
        return compound != null && compound.hasKey(INNER_TAG) ? new ItemStack(compound.getCompoundTag(INNER_TAG)) : ItemStack.EMPTY;
    }

    public void setInnerStack(ItemStack stack, ItemStack inner)
    {
        if(inner.isEmpty() || stack.getItem() != this)
            return;

        stack.setTagInfo(INNER_TAG, inner.serializeNBT());
    }

    public ItemStack makeFrom(ItemStack artifact)
    {
        ItemStack shard = new ItemStack(this);
        setInnerStack(shard,artifact);
        //if(artifact.hasDisplayName())
        //    shard.setStackDisplayName(artifact.getDisplayName());
        return shard;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        ItemStack inner = getInnerStack(stack);

        if(!inner.isEmpty())
        {
            return I18n.format(getUnlocalizedName(stack)+".name",inner.getDisplayName());
        }

        return super.getItemStackDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        ItemStack inner = getInnerStack(stack);

        if(!inner.isEmpty())
        {
            List<String> innerTooltip = inner.getTooltip(playerIn,false);
            innerTooltip.remove(0);
            tooltip.addAll(innerTooltip);
            //inner.getItem().addInformation(inner,playerIn,tooltip,advanced);
        }
        super.addInformation(stack, playerIn, tooltip, advanced);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        ItemStack inner = getInnerStack(stack);

        if(!inner.isEmpty())
        {
            return inner.getItem().hasEffect(inner);
        }
        return false;
    }

    @Override
    public IBlockColor getBlockColor() {
        return null;
    }

    @Override
    public IItemColor getItemColor() {
        return ColorHandlers.MIMIC_COLORING;
    }
}
