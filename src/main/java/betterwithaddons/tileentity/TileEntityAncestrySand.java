package betterwithaddons.tileentity;

import betterwithaddons.block.EriottoMod.BlockAncestrySand;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.entity.EntitySpirit;
import betterwithaddons.interaction.InteractionEriottoMod;
import betterwithaddons.item.ModItems;
import betterwithmods.api.tile.IMechanicalPower;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.mechanical.BlockMechMachines;
import betterwithmods.common.blocks.mechanical.tile.TileEntityFilteredHopper;
import betterwithmods.util.InvUtils;
import betterwithmods.util.MechanicalUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class TileEntityAncestrySand extends TileEntityBase implements ITickable, IMechanicalPower {
    private int spirits = 0;
    private int nextCheck = 0;
    private List<EntitySpirit> attractedSpirits = new ArrayList<>();
    private boolean shouldSync;

    public int getSpirits()
    {
        return spirits;
    }

    public void setSpirits(int n)
    {
        spirits = Math.min(n, InteractionEriottoMod.MAX_SPIRITS);
        if(spirits <= 0)
            world.setBlockState(pos, Blocks.SOUL_SAND.getDefaultState());
        shouldSync = true;
    }

    public void addSpirits(int n) { setSpirits(spirits + n); }

    public void consumeSpirits(int n)
    {
        setSpirits(spirits - n);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setInteger("Spirits",spirits);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        spirits = compound.getInteger("Spirits");
    }

    public void fillBottles()
    {
        IBlockState hopper = world.getBlockState(pos.down());

        if(spirits <= InteractionEriottoMod.SPIRIT_PER_BOTTLE)
            return;

        if(hopper.getBlock() != BWMBlocks.SINGLE_MACHINES || hopper.getValue(BlockMechMachines.TYPE) != BlockMechMachines.EnumType.HOPPER)
            return;

        boolean isOn = false;
        IBlockState state = world.getBlockState(pos);
        isOn = calculateInput() > 0;

        if(isOn) {
            TileEntity te = world.getTileEntity(pos.down());

            if (te instanceof TileEntityFilteredHopper) {
                TileEntityFilteredHopper tileHopper = (TileEntityFilteredHopper) te;
                IItemHandler handler = tileHopper.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
                ItemStack stack = new ItemStack(ModItems.ancestryBottle);
                ItemStack consumed = new ItemStack(Items.GLASS_BOTTLE);
                if (tileHopper.getFilterStack().getItem() == Item.getItemFromBlock(Blocks.SOUL_SAND) && InvUtils.canInsert(handler, stack, 1) && InvUtils.getFirstOccupiedStackOfItem(handler, consumed) >= 0) {
                    InvUtils.consumeItemsInInventory(handler, consumed, 1, false);
                    InvUtils.insert(handler, stack, false);
                    consumeSpirits(InteractionEriottoMod.SPIRIT_PER_BOTTLE);
                }
            }
        }
    }

    @Override
    public void update() {
        if(world.isRemote)
            return;

        float maxdist = 8.0f;
        AxisAlignedBB aabb = new AxisAlignedBB(pos);

        if(nextCheck-- < 0)
        {
            attractedSpirits = world.getEntitiesWithinAABB(EntitySpirit.class,aabb.grow(maxdist - 0.5));
            nextCheck = 50;
        }

        Vec3d middleOfBlock = new Vec3d(pos).addVector(0.5,0.5,0.5);

        //Can we even do this or is this awful?
        if(spirits < InteractionEriottoMod.MAX_SPIRITS)
        for(EntitySpirit spirit : attractedSpirits)
        {
            double spiritdist = spirit.getDistanceSq(middleOfBlock.x,middleOfBlock.y,middleOfBlock.z);

            if(spiritdist < 1.2f)
            {
                if(spirits < InteractionEriottoMod.MAX_SPIRITS) {
                    int consume = Math.min(InteractionEriottoMod.MAX_SPIRITS - spirits,spirit.xpValue);
                    addSpirits(consume);
                    spirit.xpValue -= consume;
                    if(spirit.xpValue <= 0)
                        spirit.setDead();
                }
                continue;
            }

            if(spiritdist > maxdist * maxdist)
                continue;

            double dx = (middleOfBlock.x - spirit.posX) / 8.0D;
            double dy = (middleOfBlock.y - spirit.posY) / 8.0D;
            double dz = (middleOfBlock.z - spirit.posZ) / 8.0D;
            double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
            double d5 = 1.0D - dist;

            if (d5 > 0.0D)
            {
                d5 = d5 * d5;
                spirit.motionX += dx / dist * d5 * 0.1D;
                spirit.motionY += dy / dist * d5 * 0.1D;
                spirit.motionZ += dz / dist * d5 * 0.1D;
            }
        }

        fillBottles();

        if(shouldSync)
        {
            syncTE();
            shouldSync = false;
        }
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        return -1;
    }

    @Override
    public int getMechanicalInput(EnumFacing facing) {
        return MechanicalUtil.getPowerOutput(world, pos.offset(facing), facing.getOpposite());
    }

    @Override
    public int getMaximumInput(EnumFacing facing) {
        return 1;
    }

    @Override
    public int getMinimumInput(EnumFacing facing) {
        return 0;
    }

    @Override
    public Block getBlock() {
        return ModBlocks.ancestrySand;
    }

    @Override
    public World getBlockWorld() {
        return getWorld();
    }

    @Override
    public BlockPos getBlockPos() {
        return getPos();
    }
}
