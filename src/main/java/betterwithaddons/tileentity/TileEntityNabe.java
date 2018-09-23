package betterwithaddons.tileentity;

import betterwithaddons.block.EriottoMod.BlockNabe;
import betterwithaddons.crafting.manager.CraftingManagerNabe;
import betterwithaddons.crafting.recipes.INabeRecipe;
import betterwithaddons.util.InventoryUtil;
import betterwithaddons.util.NabeResult;
import betterwithaddons.util.StackResult;
import betterwithmods.common.registry.heat.BWMHeatRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TileEntityNabe extends TileEntityBase implements ITickable {
    public ItemStackHandler inventory = createItemStackHandler();
    public int fireIntensity;
    private NabeResult result = new NabeResult(FluidRegistry.getFluidStack("water",0));
    private int boilingTime = 0;
    public Random random = new Random();

    public ItemStackHandler createItemStackHandler() {
        return new SimpleItemStackHandler(this, true, 6) {
            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }
        };
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setTag("inventory",inventory.serializeNBT());
        compound.setTag("fill",result.serializeNBT());
        compound.setInteger("boilTime",boilingTime);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        inventory = createItemStackHandler();
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        result = NabeResult.deserializeNBT(compound.getCompoundTag("fill"));
        boilingTime = compound.getInteger("boilTime");
    }

    public int getFireIntensity() {
        return BWMHeatRegistry.getHeat(world,pos.down());
    }

    @Override
    public void update() {
        if (world == null || pos == null)
            return;
        IBlockState state = world.getBlockState(pos);
        IBlockState upstate = world.getBlockState(pos.up());

        if (!(state.getBlock() instanceof BlockNabe))
            return;
        if (world.isRemote && boilingTime > 0)
        {
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,pos.getX()+random.nextDouble(),pos.getY()+1.0,pos.getZ()+random.nextDouble(),0,0.001,0);
            //TODO: Smonk
            return;
        }


        entityCollision();

        if (this.fireIntensity != getFireIntensity()) {
            validateFireIntensity();
        }

        if (!result.isFull() && isWater(upstate)) {
            resetWater();
        }

        if (hasWater() && this.fireIntensity > 0 && !isWater(upstate)) {
            List<ItemStack> compressedStacks = getCompressedStacks();
            INabeRecipe recipe = CraftingManagerNabe.getInstance().getMostValidRecipe(this, compressedStacks);
            if(recipe != null) {
                boilingTime += this.fireIntensity;
                if(boilingTime >= recipe.getBoilingTime(this))
                {
                    result = recipe.craft(this, compressedStacks);
                    ejectMisfits();
                    boilingTime = 0;
                }
                markDirty();
                syncTE();
            }
            else
                boilingTime = 0;
        }
        else
            boilingTime = Math.max(boilingTime-1,0);
    }

    private boolean hasWater() {
        FluidStack fluidStack = result.getFluid();
        return result.isFull() && fluidStack != null && fluidStack.getFluid() == FluidRegistry.WATER;
    }

    private List<ItemStack> getCompressedStacks()
    {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for(int i = 0; i < inventory.getSlots(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i).copy();
            if(stack.isEmpty())
                continue;
            stacks.stream().filter(existing -> existing.isItemEqual(stack)).forEach(existing -> {
                int count = Math.min(stack.getCount(), existing.getMaxStackSize() - existing.getCount());
                existing.grow(count);
                stack.shrink(count);
            });
            if(!stack.isEmpty())
                stacks.add(stack);
        }
        return stacks;
    }

    private void resetWater() {
        result = new NabeResult(FluidRegistry.getFluidStack("water",NabeResult.MAX_FLUID_FILL));
        markDirty();
        syncTE();
    }

    private boolean isWater(IBlockState upstate) {
        return upstate.getMaterial() == Material.WATER;
    }

    private void validateFireIntensity() {
        fireIntensity = BWMHeatRegistry.getHeat(world,pos.down());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    public int countIngredients() {
        int count = 0;
        for (int i = 0; i < inventory.getSlots(); i++)
            if (!inventory.getStackInSlot(i).isEmpty())
                count++;
        return count;
    }

    public void onBlockActivated(EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack originalItem = playerIn.getHeldItem(hand);
        StackResult retrieved = result.take(originalItem.copy());

        if (!retrieved.isSuccess()) {
            if (!result.isFull()) { //Try to reset water if not full
                retrieved = putWater(originalItem);
            } else if (hasWater() && CraftingManagerNabe.getInstance().isValidItem(originalItem)) { //Otherwise try to insert it
                retrieved = putItem(originalItem);
            }
        }

        if (retrieved.isSuccess()) {
            if (!retrieved.getReturnStack().isEmpty()) {
                InventoryUtil.addItemToPlayer(playerIn, retrieved.getReturnStack());
            }
            EntityEquipmentSlot slot = hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND;
            playerIn.setItemStackToSlot(slot, retrieved.getOriginalStack());
        }

        markDirty();
        syncTE();
    }

    private void ejectMisfits() {
        for(int i = 0; i < inventory.getSlots(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if(!CraftingManagerNabe.getInstance().isValidItem(stack))
            {
                inventory.setStackInSlot(i,ItemStack.EMPTY);
                ejectStack(stack);
            }
        }
    }

    private void ejectStack(ItemStack stack) {
        float xEject = random.nextFloat() * 0.5f - 0.25f;
        float zEject = random.nextFloat() * 0.5f - 0.25f;

        float xOff = 0.5F;
        float yOff = 1.2f;
        float zOff = 0.5F;

        EntityItem item = new EntityItem(this.getWorld(), pos.getX() + xOff, pos.getY() + yOff, pos.getZ() + zOff, stack);

        item.motionX = xEject * 0.1f;
        item.motionY = 0.1f;
        item.motionZ = zEject * 0.1f;
        item.setDefaultPickupDelay();
        this.getWorld().spawnEntity(item);
    }

    private boolean captureDroppedItems() {
        List<EntityItem> items = this.getCaptureItems(getWorld(), getPos());
        SoundEvent sound = null;
        if (items.size() > 0) {
            for (EntityItem item : items) {
                if(CraftingManagerNabe.getInstance().isValidItem(item.getItem()) && putDropInInventoryAllSlots(inventory, item))
                    sound = SoundEvents.ENTITY_ITEM_PICKUP;
                if(!result.isFull() && putDropWaterFill(item))
                    sound = SoundEvents.ITEM_BUCKET_FILL;
            }
            if (sound != null) {
                this.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.PLAYERS, 0.2F, ((getWorld().rand.nextFloat() - getWorld().rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                return true;
            }
        }
        return false;
    }

    private boolean putDropWaterFill(EntityItem item) {
        ItemStack stack = item.getItem();
        StackResult result = putWater(stack.copy());
        item.setItem(result.getOriginalStack());
        ejectStack(result.getReturnStack());
        return result.isSuccess();
    }

    public StackResult putItem(ItemStack stack) {
        ItemStack originalItem = stack.copy();
        ItemStack oneItem = originalItem.splitStack(1);
        ItemStack returnStack = attemptToInsert(inventory, oneItem);
        if(returnStack.isEmpty()) {
            return new StackResult(true, originalItem, returnStack);
        }
        return new StackResult(false,stack);
    }

    private StackResult putWater(ItemStack stack) {
        ItemStack original = stack.copy();
        ItemStack container = original.splitStack(1);
        IFluidHandlerItem handler = FluidUtil.getFluidHandler(container);

        if(handler != null) {
            FluidStack drainResource = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
            FluidStack fluid = handler.drain(drainResource,false);
            if(fluid != null && fluid.getFluid() == FluidRegistry.WATER && fluid.amount >= Fluid.BUCKET_VOLUME) {
                handler.drain(drainResource,true);
                resetWater();
                return new StackResult(true,original,handler.getContainer());
            }
        }

        return new StackResult(false,stack);
    }

    private void entityCollision() {
        boolean flag = false;
        if (!isFull()) {
            flag = captureDroppedItems();
        }
        if (flag) {
            this.markDirty();
        }
    }

    private boolean isFull() {
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }

    public static boolean putDropInInventoryAllSlots(IItemHandler inv, EntityItem entityItem) {
        boolean putAll = false;
        if (entityItem == null) {
            return false;
        } else {
            ItemStack itemstack = entityItem.getItem().copy();
            ItemStack leftovers = attemptToInsert(inv, itemstack);
            if (!leftovers.isEmpty() && leftovers.getCount() != 0) {
                entityItem.setItem(leftovers);
            } else {
                putAll = true;
                entityItem.setDead();
            }
            return putAll;
        }
    }

    public static ItemStack attemptToInsert(IItemHandler inv, ItemStack stack) {
        ItemStack leftover = stack;
        for (int slot = 0; slot < inv.getSlots(); slot++) {
            leftover = inv.insertItem(slot, leftover, false);
            if (leftover.isEmpty())
                break;
        }
        return leftover;
    }

    public List<EntityItem> getCaptureItems(World worldIn, BlockPos pos) {
        return worldIn.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1.5D, pos.getZ() + 1D), EntitySelectors.IS_ALIVE);
    }

    public ItemStack consumeItem(ItemStack stack) {
        if(stack.getItem().hasContainerItem(stack))
            return stack.getItem().getContainerItem(stack);
        stack.shrink(1);
        return stack;
    }

    public NabeResult getFill() {
        return result;
    }
}
