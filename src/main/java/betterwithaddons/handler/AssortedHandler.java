package betterwithaddons.handler;

import betterwithaddons.block.BetterRedstone.BlockPCB;
import betterwithaddons.block.BlockLattice;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.interaction.InteractionBTWTweak;
import betterwithaddons.item.ModItems;
import betterwithaddons.potion.ModPotions;
import betterwithaddons.util.BannerUtil;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockAnchor;
import betterwithmods.common.blocks.BlockLight;
import betterwithmods.common.blocks.tile.TileEntityPulley;
import betterwithmods.module.GlobalConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.*;

public class AssortedHandler {
    static Random rng = new Random();

    private HashMap<UUID, BossInfoServer> BossList = new HashMap<>();
    private final int BossCleanupThreshold = 10;
    private final float HardnessThreshold = 5.0f;

    public static final int ScaleQuarryAmt = 5;
    public static final int ScaleQuarryMinDist = 200;
    public static final int ScaleQuarryMaxDist = 3000;
    public static final int ScaleQuarryFuzzyness = 500;
    public static final int ScaleQuarrySize = 16;
    public static final int ScaleQuarryMinDepth = 0;
    public static final int ScaleQuarryMaxDepth = 32;
    public static BlockPos[] ScaleQuarries = new BlockPos[ScaleQuarryAmt];

    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        EntityLivingBase living = event.getEntityLiving();
        if (living == null || living.world == null) return;
        Random rand = living.world.rand;

        if (living instanceof EntityShulker) {
            if (rand.nextFloat() < 1.0f) {
                event.getEntityLiving().entityDropItem(ModItems.material.getMaterial("ender_cream", 1 + rand.nextInt(2)), 0);
            }
        }
    }


    @SubscribeEvent
    public void blockNeighborUpdate(BlockEvent.NeighborNotifyEvent notifyEvent) {
        World world = notifyEvent.getWorld();
        makeRedstonePCB(world, notifyEvent.getPos());
    }

    private void makeRedstonePCB(World world, BlockPos pos) {
        IBlockState blockstate = world.getBlockState(pos);
        Block block = blockstate.getBlock();
        Block bottomblock = world.getBlockState(pos.down()).getBlock();
        if (!world.isRemote && block instanceof BlockRedstoneWire && bottomblock instanceof BlockPCB) {
            world.setBlockState(pos, ModBlocks.pcbwire.getDefaultState());
        }
    }

    /*@SubscribeEvent
    public void onArtifactInteract(PlayerInteractEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState blockstate = world.getBlockState(pos);
        EntityPlayer player = event.getEntityPlayer();

        if (blockstate.getBlock() instanceof BlockQuartz) {
            ItemStack stack = player.getHeldItemMainhand();
            Item item = stack.getItem();

            if (!stack.isEmpty() && (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemArmor || item instanceof ItemBow)) {
                player.swingArm(EnumHand.MAIN_HAND);
                player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ModItems.brokenArtifact.makeFrom(stack));

                if (!event.getWorld().isRemote) {
                    event.setCanceled(true);
                }
            }
        }
    }*/

    @SubscribeEvent
    public void beginTrack(PlayerEvent.StartTracking trackEvent) {
        if (!trackEvent.getEntityPlayer().getEntityWorld().isRemote) {
            Entity target = trackEvent.getTarget();
            EntityPlayerMP player = (EntityPlayerMP) trackEvent.getEntityPlayer();

            UUID uuid = target.getUniqueID();

            if (BossList.containsKey(uuid)) {
                BossInfoServer bossInfo = BossList.get(uuid);
                bossInfo.addPlayer(player);
            }
        }
    }

    @SubscribeEvent
    public void endTrack(PlayerEvent.StopTracking trackEvent) {
        if (!trackEvent.getEntityPlayer().getEntityWorld().isRemote) {
            Entity target = trackEvent.getTarget();
            EntityPlayerMP player = (EntityPlayerMP) trackEvent.getEntityPlayer();

            UUID uuid = target.getUniqueID();

            if (BossList.containsKey(uuid)) {
                BossInfoServer bossInfo = BossList.get(uuid);
                bossInfo.removePlayer(player);
            }
        }
    }

    @SubscribeEvent
    public void worldTick(TickEvent.WorldTickEvent tickEvent) {
        World world = tickEvent.world;
        WorldScaleData.getInstance(world).cleanup();
        if (!world.isRemote) {
            if (ScaleQuarryAmt > 0 && world.provider.getDimension() == 0)
                if (!doScaleQuarriesExist())
                    generateScaleQuarries(world.getSeed());
                else if (world.rand.nextInt(100) == 0)
                    fillScaleQuarries(world, world.rand);
        }
    }

    public static boolean doScaleQuarriesExist() {
        return ScaleQuarryAmt > 0 && ScaleQuarries[0] != null;
    }

    private void generateScaleQuarries(long seed) {
        Random rand = new Random(seed);

        for (int i = 0; i < ScaleQuarryAmt; i++) {
            float dist = rand.nextInt(ScaleQuarryMaxDist - ScaleQuarryMinDist) + ScaleQuarryMinDist;
            float ang = (float) Math.toRadians(i * (360f / ScaleQuarryAmt));
            float x = (float) (dist * Math.sin(ang));
            float z = (float) (dist * Math.cos(ang));

            x += rand.nextInt(ScaleQuarryFuzzyness) - rand.nextInt(ScaleQuarryFuzzyness);
            z += rand.nextInt(ScaleQuarryFuzzyness) - rand.nextInt(ScaleQuarryFuzzyness);
            int y = ScaleQuarryMinDepth + rand.nextInt(ScaleQuarryMaxDepth - ScaleQuarryMinDepth);

            ScaleQuarries[i] = new BlockPos(x, y, z);
            //System.out.print("Scale quarry at "+x+","+y+","+z);
        }
    }

    private void fillScaleQuarries(World world, Random rand) {
        BlockPos root = ScaleQuarries[rand.nextInt(ScaleQuarryAmt)];

        if (!world.isBlockLoaded(root))
            return;

        int maxdist = rand.nextInt(ScaleQuarrySize) + 1;
        BlockPos.MutableBlockPos seeker = new BlockPos.MutableBlockPos(root);
        for (int attempts = 0; attempts < maxdist; attempts++) {
            int xoff = rand.nextInt(3) - 1;
            int zoff = xoff == 0 ? rand.nextInt(3) - 1 : 0;
            int yoff = zoff == 0 ? rand.nextInt(2) * 2 - 1 : 0;
            seeker.setPos(seeker.getX() + xoff, seeker.getY() + yoff, seeker.getZ() + zoff);
            if (world.isAirBlock(seeker))
                return;
            else if (world.getBlockState(seeker).getBlock() == Blocks.STONE) {
                if (rand.nextInt(maxdist - attempts) == 0) {
                    world.setBlockState(seeker, ModBlocks.worldScaleOre.getDefaultState(), 2);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    public void breakBlock(PlayerEvent.BreakSpeed breakEvent) {
        World world = breakEvent.getEntity().getEntityWorld();
        Entity entity = breakEvent.getEntity();
        WorldScaleData scaledata = WorldScaleData.getInstance(world);
        BlockPos worldscale = scaledata.getNearbyScale(breakEvent.getPos());
        ItemStack banner;
        BlockPos breakpos = breakEvent.getPos();
        IBlockState blockstate = world.getBlockState(breakpos);
        /*if(entity instanceof EntityPlayer) {
			ItemStack stack = breakEvent.getEntityPlayer().getHeldItemMainhand();
			if (toolShouldntBreak(stack) && breakEvent.getState().getBlockHardness(world,breakEvent.getPos()) > 0.0f) {
				breakEvent.setNewSpeed(0f);
				return;
			}
		}*/
        float hardnessmod = 1.0f;

        if (!(blockstate.getBlock() instanceof BlockLattice) && isNextToHardener(world, breakpos)) {
            hardnessmod = 2.0f;
        }

        float hardness = blockstate.getBlockHardness(world, breakEvent.getPos()) * hardnessmod;
        float newspeed = breakEvent.getNewSpeed() * (1.0f / hardnessmod);

        if (worldscale != null && !(banner = BannerUtil.getBannerItemFromBlock(world, worldscale.up())).isEmpty()) {
            if (!BannerUtil.isSameBanner(banner, entity)) {
                newspeed = breakEvent.getOriginalSpeed() * (Math.max(0f, HardnessThreshold - hardness) / HardnessThreshold) * 0.3333f * (1.0f / hardnessmod);
            }
        }

        breakEvent.setNewSpeed(newspeed);
    }

    private boolean isNextToHardener(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos nextpos = pos.offset(facing);
            IBlockState state = world.getBlockState(nextpos);
            if (state.getBlock() instanceof BlockLattice)
                return true;
        }

        return false;
    }

    @SubscribeEvent
    public void livingTick(LivingEvent.LivingUpdateEvent updateEvent) {
        final EntityLivingBase entity = updateEvent.getEntityLiving();
        World world = entity.getEntityWorld();
        UUID uuid = entity.getUniqueID();
        BlockPos pos = entity.getPosition();

        if (!world.isRemote) {
            if (entity.isPotionActive(ModPotions.boss)) {
                if (!BossList.containsKey(uuid)) {
                    BossInfoServer displayData = (BossInfoServer) new BossInfoServer(entity.getDisplayName(), Color.PURPLE, Overlay.PROGRESS).setDarkenSky(false);
                    BossList.put(uuid, displayData);
                    List<EntityPlayerMP> entities = world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos).expand(24, 24, 24));
                    if (entities != null)
                        for (EntityPlayerMP ply : entities) {
                            displayData.addPlayer(ply);
                        }
                } else {
                    BossInfoServer bossInfo = BossList.get(uuid);
                    bossInfo.setPercent(entity.getHealth() / entity.getMaxHealth());
                }

            } else if (world.getMinecraftServer().getTickCounter() % BossCleanupThreshold == 0 && BossList.containsKey(uuid)) {
                BossInfoServer bossInfo = BossList.get(uuid);
                for (EntityPlayerMP ply : bossInfo.getPlayers()) {
                    bossInfo.removePlayer(ply);
                }
                BossList.remove(uuid);
            }
        }
    }

    @SubscribeEvent
    public void livingAttacked(LivingAttackEvent event) {
        if (!event.getEntityLiving().world.isRemote) {
            if (!event.isCanceled() && event.getAmount() > 0) {
                EntityLivingBase living = event.getEntityLiving();

                if (living.isPotionActive(ModPotions.cannonball) && (event.getSource().isExplosion() || event.getSource() == DamageSource.FALL)) {
                    if (event.getSource() == DamageSource.FALL) //No you don't get to have superbuffs that make you immune to creepers and falldamage.
                        living.removePotionEffect(ModPotions.cannonball);
                    event.setCanceled(true);
                }
            }
        }
    }


    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState blockstate = world.getBlockState(pos);
        EntityPlayer player = event.getEntityPlayer();

        if (blockstate.getBlock() instanceof BlockBanner) {
            EnumHand hand = EnumHand.MAIN_HAND;
            ItemStack stack = player.getHeldItemMainhand();
            BlockBanner bannerblock = (BlockBanner) blockstate.getBlock();

            if (stack.isEmpty() && player.isSneaking() && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
                ItemStack bannerstack = bannerblock.getItem(world, pos, blockstate);
                player.swingArm(hand);

                world.removeTileEntity(pos);
                world.setBlockToAir(pos);

                player.setItemStackToSlot(EntityEquipmentSlot.HEAD, bannerstack);

                if (!event.getWorld().isRemote) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
