package betterwithaddons.tileentity;

import betterwithaddons.block.EriottoMod.BlockNettedScreen;
import betterwithaddons.block.EriottoMod.BlockSlat;
import betterwithaddons.crafting.manager.CraftingManagerFireNet;
import betterwithaddons.crafting.manager.CraftingManagerNet;
import betterwithaddons.crafting.manager.CraftingManagerSandNet;
import betterwithaddons.crafting.manager.CraftingManagerWaterNet;
import betterwithaddons.crafting.recipes.NetRecipe;
import betterwithaddons.util.ItemUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityNettedScreen extends TileEntityBase implements ITickable
{
    public TileEntityNettedScreen() {
    }

    @Override
    public void update()
    {
        if (!this.world.isRemote)
        {
            captureDroppedItems();
        }
    }

    public BlockNettedScreen.SifterType getSifterType()
    {
        IBlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof BlockNettedScreen)
            return ((BlockNettedScreen)state.getBlock()).getSifterType(world,pos);
        return BlockNettedScreen.SifterType.NONE;
    }

    private CraftingManagerNet getCraftingManager(BlockNettedScreen.SifterType type)
    {
        switch(type)
        {
            case WATER:
                return CraftingManagerWaterNet.getInstance();
            case SAND:
                return CraftingManagerSandNet.getInstance();
            case FIRE:
                return CraftingManagerFireNet.getInstance();
        }

        return null;
    }

    public List<EntityItem> getCaptureItems(World worldIn, BlockPos pos) {
        return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1.5D, pos.getZ() + 1D), EntitySelectors.IS_ALIVE);
    }

    private void handleSandCase(EntityItem item) {
        ItemStack sandstack = item.getItem().copy();
        sandstack.shrink(this.increaseSandCount(sandstack.getCount()));
        item.setItem(sandstack);
        if(sandstack.getCount() < 0)
            item.setDead();
    }

    private boolean canHoldSand()
    {
        IBlockState state = world.getBlockState(pos.down());
        return state.getBlock() instanceof BlockSlat;
    }

    private int getSandCount()
    {
        IBlockState state = world.getBlockState(pos.down());
        return state.getBlock() instanceof BlockSlat ? state.getValue(BlockSlat.SANDFILL) : 0;
    }

    private int increaseSandCount(int n)
    {
        IBlockState state = world.getBlockState(pos.down());
        Block slatblock = state.getBlock();
        if(slatblock instanceof BlockSlat) {
            int currsand = getSandCount();
            int added = Math.min(n, 8 - currsand);
            world.setBlockState(pos.down(),slatblock.getDefaultState().withProperty(BlockSlat.SANDFILL,currsand+added));
            return added;
        }
        return 0;
    }

    private boolean captureDroppedItems() {
        List<EntityItem> items = this.getCaptureItems(world, pos);
        if (items.size() > 0) {
            for (EntityItem item : items) {
                ItemStack stack = item.getItem();
                if (stack.getItem() == Item.getItemFromBlock(Blocks.SAND))
                    handleSandCase(item);
            }
            CraftingManagerNet manager = getCraftingManager(getSifterType());
            if(manager != null) {
                NetRecipe recipe = manager.getMostValidRecipe(items, getSandCount());
                if (recipe != null) {
                    List<ItemStack> ret = recipe.getOutput();
                    if (ret != null && ret.size() > 0) {
                        ItemUtil.consumeItem(items,recipe.input);
                        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

                        for (int i = 0; i < ret.size(); i++) {
                            ItemStack item = ret.get(i);
                            if(item.isEmpty())
                                continue;
                            EntityItem result = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + (i % 2 == 0 ? 1.2f : -0.5f), pos.getZ() + 0.5f, item.copy());
                            result.setDefaultPickupDelay();
                            if (!world.isRemote) {
                                world.spawnEntity(result);
                            }
                        }

                        if (recipe.getSandRequired() > 0)
                            increaseSandCount(-recipe.getSandRequired());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
    }
}
