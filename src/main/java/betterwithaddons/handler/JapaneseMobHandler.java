package betterwithaddons.handler;

import betterwithaddons.entity.EntitySpirit;
import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class JapaneseMobHandler {
    public static final ResourceLocation JAPANESE_MOB = new ResourceLocation(Reference.MOD_ID, "japanese_mob");
    @CapabilityInject(JapaneseMob.class)
    public static Capability<JapaneseMob> JAPANESE_MOB_CAP;

    public static void registerCapability()
    {
        CapabilityManager.INSTANCE.register(JapaneseMob.class, new Capability.IStorage<JapaneseMob>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<JapaneseMob> capability, JapaneseMob instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<JapaneseMob> capability, JapaneseMob instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound)nbt);
            }
        }, JapaneseMob::new);
    }

    public static class JapaneseMob implements ICapabilitySerializable<NBTTagCompound>
    {
        public int spirits = 0;
        public int absorbDelay = 50;

        public JapaneseMob() {
        }

        public JapaneseMob(int spirits)
        {
            this.spirits = spirits;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == JAPANESE_MOB_CAP;
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
            nbt.setInteger("Spirits", spirits);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            spirits = nbt.getInteger("Spirits");
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void japaneseDeath(LivingDeathEvent event)
    {
        EntityLivingBase living = event.getEntityLiving();
        World world = living.world;
        if(!event.isCanceled() && !world.isRemote && living.hasCapability(JAPANESE_MOB_CAP,null))
        {
            EntityMob zombie = (EntityMob) living;
            JapaneseMob japaneseMob = living.getCapability(JAPANESE_MOB_CAP,null);
            int i = japaneseMob.spirits;

            while (i > 0)
            {
                int j = EntitySpirit.getSpiritSplit(i);
                i -= j;
                world.spawnEntity(new EntitySpirit(world, living.posX, living.posY, living.posZ, j));
            }

            zombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND,ItemStack.EMPTY);
            zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD,ItemStack.EMPTY);
            zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST,ItemStack.EMPTY);
            zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS,ItemStack.EMPTY);
            zombie.setItemStackToSlot(EntityEquipmentSlot.FEET,ItemStack.EMPTY);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void japaneseSpawn(LivingSpawnEvent.SpecialSpawn event)
    {
        EntityLivingBase living = event.getEntityLiving();
        if(!event.isCanceled() && living.hasCapability(JAPANESE_MOB_CAP,null))
        {
            EntityMob zombie = (EntityMob) living;
            zombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND,new ItemStack(ModItems.katana));
            zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD,new ItemStack(ModItems.samuraiHelm));
            zombie.setItemStackToSlot(EntityEquipmentSlot.CHEST,new ItemStack(ModItems.samuraiChestplate));
            zombie.setItemStackToSlot(EntityEquipmentSlot.LEGS,new ItemStack(ModItems.samuraiLeggings));
            zombie.setItemStackToSlot(EntityEquipmentSlot.FEET,new ItemStack(ModItems.samuraiBoots));
        }
    }

    @SubscribeEvent
    public void japaneseAttachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();
        World world = entity.getEntityWorld();
        Random random = world.rand;

        if(world.isRemote)
            return;

        if(entity instanceof EntityZombie || entity instanceof EntitySkeleton)
        {
            if(InteractionEriottoMod.JAPANESE_RANDOM_SPAWN && random.nextDouble() < InteractionEriottoMod.JAPANESE_RANDOM_SPAWN_CHANCE)
                event.addCapability(JAPANESE_MOB,new JapaneseMob(random.nextInt(4)+3));
        }
    }
}
