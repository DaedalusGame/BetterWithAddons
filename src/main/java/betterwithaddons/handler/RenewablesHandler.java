package betterwithaddons.handler;

import betterwithaddons.interaction.InteractionBWR;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RenewablesHandler {
    @SubscribeEvent
    public void dungRinsing(RandomBlockTickEvent event) {
        World world = event.getWorld();
        IBlockState state = event.getState();
        BlockPos pos = event.getPos();
        Random random = event.getRandom();

        if (world.isRemote || !InteractionBWR.DUNG_TO_DIRT || state.getBlock() != BWMBlocks.AESTHETIC || state.getValue(BlockAesthetic.blockType) != BlockAesthetic.EnumType.DUNG)
            return;

        IBlockState water = world.getBlockState(pos.up());

        if (water.getMaterial() != Material.WATER) {
            return;
        }

        int fireIntensity = 26; //Ambient

        for (int i = 1; i <= 3; i++) {
            if (!world.isBlockNormalCube(pos.down(i), true)) {
                fireIntensity += getCurrentFireIntensity(world, pos.down(i));
                break;
            }
        }

        if (random.nextInt(300) < fireIntensity) {
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
            //TODO: Should we add minetweaker compat for other things to happen here?
            if (InteractionBWR.SAND_TO_CLAY && isSand(world, pos.down())) {
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F);
                world.setBlockState(pos.down(), Blocks.CLAY.getDefaultState());
            }
        }
    }

    public boolean isSand(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        return block == Blocks.SAND;
    }

    public int getCurrentFireIntensity(World world, BlockPos pos) {
        int fireFactor = 0;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos target = pos.add(x, 0, z);
                IBlockState targetState = world.getBlockState(target);
                Block block = targetState.getBlock();
                int meta = targetState.getBlock().damageDropped(targetState);
                if (BWMHeatRegistry.get(block, meta) != null)
                    fireFactor += BWMHeatRegistry.get(block, meta).value;
            }
        }

        return fireFactor;
    }

    @SubscribeEvent
    public void meltHellfire(RandomBlockTickEvent event)
    {
        World world = event.getWorld();
        IBlockState state = event.getState();
        BlockPos pos = event.getPos();
        Random rand = event.getRandom();

        if(world.isRemote || !InteractionBWR.MELT_HELLFIRE || state.getBlock() != BWMBlocks.AESTHETIC || state.getValue(BlockAesthetic.blockType) != BlockAesthetic.EnumType.HELLFIRE)
            return;

        int sources = 0;
        boolean hasSource = false;

        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++)
                for (int z = -1; z <= 1; z++)
                {
                    IBlockState lavaState = world.getBlockState(pos.add(x,y,z));

                    if(lavaState.getMaterial() != Material.LAVA) continue;
                    sources++;
                    hasSource |= lavaState.getValue(BlockLiquid.LEVEL) == 0;
                }

        if(hasSource && rand.nextInt(10) < sources)
        {
            world.setBlockState(pos, Blocks.LAVA.getDefaultState());
            for(int i = 0; i < 3; i++)
                world.playEvent(2004, pos, 0);
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
        }
    }

    @SubscribeEvent
    public void makeBlazeGolem(BlockEvent.NeighborNotifyEvent event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState head = event.getState();

        if(!world.isRemote && InteractionBWR.BLAZE_GOLEMS && head.getBlock() == Blocks.SKULL)
        {
            TileEntity te = world.getTileEntity(pos);
            if(!(te instanceof TileEntitySkull) || ((TileEntitySkull) te).getSkullType() != 0)
                return;

            BlockPos body = pos.down();
            BlockPos feet = pos.down(2);

            if(world.getBlockState(body).getBlock() == Blocks.GOLD_BLOCK &&
                    world.getBlockState(body.north()).getBlock() == Blocks.IRON_BARS &&
                    world.getBlockState(body.south()).getBlock() == Blocks.IRON_BARS &&
                    world.getBlockState(body.east()).getBlock() == Blocks.IRON_BARS &&
                    world.getBlockState(body.west()).getBlock() == Blocks.IRON_BARS &&
                    world.getBlockState(feet).getMaterial() == Material.FIRE)
            {
                world.setBlockToAir(pos);
                world.setBlockToAir(body);
                world.setBlockToAir(body.north());
                world.setBlockToAir(body.south());
                world.setBlockToAir(body.east());
                world.setBlockToAir(body.west());
                world.setBlockToAir(feet);

                spawnArtificalBlaze(world,feet,true);
            }
        }
    }

    @SubscribeEvent
    public void feedBlaze(LivingEvent.LivingUpdateEvent event)
    {
        if(!InteractionBWR.BLAZE_BREEDING)
            return;

        Entity entity = event.getEntity();
        World world = entity.world;
        Random random = world.rand;
        BlockPos pos = entity.getPosition();

        if(!world.isRemote && entity instanceof EntityBlaze)
        {
            EntityBlaze blaze = (EntityBlaze) entity;
            ArtificialBlaze blazeCap = blaze.getCapability(ARTIFICIAL_BLAZE_CAP,null);

            blazeCap.breedingDelay--;

            if(blazeCap.breedingDelay > 0)
                return;

            if(blazeCap.isArtificial)
            {
                blaze.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE,60000));
            }

            BlockPos checkPos = pos.add(random.nextInt(7)-3,random.nextInt(7)-3,random.nextInt(7)-3);

            if(!BiomeDictionary.hasType(world.getBiome(checkPos), BiomeDictionary.Type.NETHER)) {
                blazeCap.breedingDelay = 6000;
                return;
            }

            if(world.getBlockState(checkPos).getMaterial() != Material.FIRE)
                return;

            if(!consumeNearbyBlazeFood(world,pos))
                return;

            spawnArtificalBlaze(world,checkPos,blazeCap.isArtificial);
            world.setBlockToAir(checkPos);

            blazeCap.breedingDelay = 6000;
        }
    }

    public boolean consumeNearbyBlazeFood(World world,BlockPos pos)
    {
        AxisAlignedBB aabb = new AxisAlignedBB(pos);
        aabb.expand(2.0,2.0,2.0);

        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,aabb);

        for(EntityItem item : items)
        {
            ItemStack stack = item.getEntityItem();
            if(isBlazeFood(stack) && !item.isDead && !item.cannotPickup())
            {
                stack.shrink(1);
                if(stack.isEmpty())
                    item.setDead();

                return true;
            }
        }

        return false;
    }

    public boolean isBlazeFood(ItemStack stack)
    {
        return ItemUtil.matchesOreDict(stack,"listAllBlazeFoods");
    }

    public void spawnArtificalBlaze(World world, BlockPos pos, boolean artificial)
    {
        EntityBlaze blaze = new EntityBlaze(world);
        blaze.setPosition(pos.getX()+0.5,pos.getY()+0.5,pos.getZ()+0.5);
        blaze.getCapability(ARTIFICIAL_BLAZE_CAP,null).isArtificial = artificial;
        world.spawnEntity(blaze);

        for(int i = 0; i < 6; i++)
            world.playEvent(2004, pos.up(i % 3), 0);
    }

    public static final ResourceLocation ARTIFICIAL_BLAZE = new ResourceLocation(Reference.MOD_ID, "artificial_blaze");
    @CapabilityInject(ArtificialBlaze.class)
    public static Capability<ArtificialBlaze> ARTIFICIAL_BLAZE_CAP;

    public static void registerCapability()
    {
        CapabilityManager.INSTANCE.register(ArtificialBlaze.class, new Capability.IStorage<ArtificialBlaze>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<ArtificialBlaze> capability, ArtificialBlaze instance, EnumFacing side) {
                return instance.serializeNBT();
            }

            @Override
            public void readNBT(Capability<ArtificialBlaze> capability, ArtificialBlaze instance, EnumFacing side, NBTBase nbt) {
                instance.deserializeNBT((NBTTagCompound)nbt);
            }
        },ArtificialBlaze::new);
    }

    public static class ArtificialBlaze implements ICapabilitySerializable<NBTTagCompound>
    {
        public boolean isArtificial = false;
        public int breedingDelay = 0;

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == ARTIFICIAL_BLAZE_CAP;
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
            nbt.setInteger("BreedingDelay", breedingDelay);
            nbt.setBoolean("IsArtificial", isArtificial);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt)
        {
            breedingDelay = nbt.getInteger("BreedingDelay");
            isArtificial = nbt.getBoolean("IsArtificial");
        }
    }

    @SubscribeEvent
    public void blazeAttachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        Entity entity = event.getObject();

        if(entity instanceof EntityBlaze)
        {
            event.addCapability(ARTIFICIAL_BLAZE,new ArtificialBlaze());
        }
    }

    @SubscribeEvent
    public void blazeDespawn(LivingSpawnEvent.AllowDespawn event)
    {
        Entity entity = event.getEntity();

        if(entity instanceof EntityBlaze && entity.getCapability(ARTIFICIAL_BLAZE_CAP,null).isArtificial)
        {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    @SideOnly(Side.CLIENT)
    public void renderQuartzTooltip(ItemTooltipEvent event)
    {
        ItemStack stack = event.getItemStack();
        if(stack == null)
            return;
        NBTTagCompound compound = stack.getTagCompound();

        if(!stack.isEmpty() && compound != null && compound.hasKey("QuartzCrystal"))
        {
            int growth = compound.getInteger("QuartzCrystalGrowth");
            int souls = compound.getInteger("QuartzSouls");
            event.getToolTip().add(TextFormatting.LIGHT_PURPLE+getSaturationString(growth,false));
            if(souls > 0)
                event.getToolTip().add(TextFormatting.DARK_RED+getSoulString(souls,27,false));
        }
    }

    private String getSaturationString(int growth, boolean verbose) {
        float percentage = growth / 20f;

        if(verbose)
            return I18n.format("tooltip.quartz.verbose",(int)(percentage * 100));
        else
        {
            float epsilon = 0.00001f;
            if(percentage > 1.0f - epsilon)
                return I18n.format("tooltip.quartz.almostready");
            else if(percentage > 0.75f - epsilon)
                return I18n.format("tooltip.quartz.readysoon");
            else if(percentage > 0.50f - epsilon)
                return I18n.format("tooltip.quartz.halfway");
            else if(percentage > 0.25f - epsilon)
                return I18n.format("tooltip.quartz.longway");
            else
                return I18n.format("tooltip.quartz.starting");
        }
    }

    private String getSoulString(int souls, int maxsouls, boolean verbose) {
        if(verbose)
            return I18n.format("tooltip.souls.verbose",souls);
        else
        {
            if(souls >= maxsouls)
                return I18n.format("tooltip.souls.danger");
            else if(souls >= maxsouls / 2)
                return I18n.format("tooltip.souls.half");
            else if(souls >= 3)
                return I18n.format("tooltip.souls.three");
            else if(souls >= 2)
                return I18n.format("tooltip.souls.two");
            else if(souls >= 1)
                return I18n.format("tooltip.souls.one");
            else
                return null;
        }
    }
}
