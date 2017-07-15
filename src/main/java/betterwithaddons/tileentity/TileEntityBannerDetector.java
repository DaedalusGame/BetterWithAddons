package betterwithaddons.tileentity;

import betterwithaddons.block.BlockBannerDetector;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.util.BannerUtil;
import betterwithaddons.util.InventoryUtil;
import com.google.common.base.Predicate;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class TileEntityBannerDetector extends TileEntityBase implements ITickable
{
    boolean powered;
    int maxdist;

    //TODO: Gross
    InventoryBasic bannerInventory;

    public TileEntityBannerDetector() {
        bannerInventory = new InventoryBasic("tile.bannerDetector", false, 1);
        maxdist = 7;
    }

    @Override
    public void update()
    {
        if (!this.world.isRemote)
        {
            boolean newPowered = checkSupposedPoweredState();

            if (newPowered != powered)
            {
                powered = newPowered;
                this.syncTE();
                this.world.notifyNeighborsOfStateChange(pos, ModBlocks.bannerDetector, true);
            }
        }
    }

    private boolean checkSupposedPoweredState()
    {
        final ItemStack filter = bannerInventory.getStackInSlot(0);

        IBlockState blockState = world.getBlockState(pos);
        EnumFacing facing = blockState.getValue(BlockBannerDetector.FACING);

        boolean detected = false;
        BlockPos lastPos = null;

        if(!filter.isEmpty() && filter.getItem() instanceof ItemBanner) {
            for (int i = 1; i <= maxdist; i++) {
                BlockPos nextPos = new BlockPos(pos.offset(facing, i));
                lastPos = nextPos;

                if (world.isBlockLoaded(nextPos)) {
                    IBlockState nextState = world.getBlockState(nextPos);
                    if (nextState.isOpaqueCube()) break;
                    detected = checkBanner(nextPos,filter,BlockBanner.class);
                    if(!detected) detected = checkBanner(nextPos.up(), filter, BlockBanner.BlockBannerHanging.class);
                    if(!detected) detected = checkBanner(nextPos.down(), filter, BlockBanner.BlockBannerStanding.class);

                    if(detected) break;
                }
            }

            if (!detected && maxdist > 0) {
                List<Entity> entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos, lastPos.add(1,1,1)), new Predicate<Entity>() {
                    @Override
                    public boolean apply(Entity input) {
                        return BannerUtil.isSameBanner(filter, input);
                    }
                });
                detected = entityList != null && entityList.size() > 0;
            }
        }

        return detected;
    }

    private boolean checkBanner(BlockPos pos, ItemStack filter, Class type)
    {
        IBlockState state = world.getBlockState(pos);
        if (type.isInstance(state.getBlock())) {
            BlockBanner bannerblock = (BlockBanner) state.getBlock();
            ItemStack banner = bannerblock.getItem(world, pos, state);
            return BannerUtil.isSameBanner(filter,banner);
        }

        return false;
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("powered", powered);

        NBTTagCompound inventoryCompound = new NBTTagCompound();
        InventoryUtil.writeInventoryToCompound(inventoryCompound, bannerInventory);
        compound.setTag("inventory", inventoryCompound);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound)
    {
        powered = compound.getBoolean("powered");

        NBTTagCompound inventoryCompound = compound.getCompoundTag("inventory");

        if (inventoryCompound != null)
        {
            InventoryUtil.readInventoryFromCompound(inventoryCompound, bannerInventory);
        }
    }

    public boolean isPowered()
    {
        return powered;
    }

    public IInventory getInventory()
    {
        return bannerInventory;
    }
}
