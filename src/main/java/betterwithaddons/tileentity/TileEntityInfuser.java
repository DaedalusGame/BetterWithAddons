package betterwithaddons.tileentity;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.manager.CraftingManagerInfuserTransmutation;
import betterwithaddons.crafting.recipes.infuser.TransmutationRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class TileEntityInfuser extends TileEntityBase implements ITickable {
    public TileEntityInfuser()
    {
        super();
    }

    public int animLife = 0;
    public float activeGlow = 0.0f;
    public float activeGlowLast = 0.0f;

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {

    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {

    }

    public TileEntityAncestrySand getBottomSand() //TODO: Capability support? Addon addons?????
    {
        TileEntity te = world.getTileEntity(pos.down());
        if(te instanceof TileEntityAncestrySand)
            return (TileEntityAncestrySand) te;
        return null;
    }

    public boolean isValid()
    {
        return ModBlocks.INFUSER.isValid(world,pos);
    }

    public int getSpirits()
    {
        TileEntityAncestrySand sand = getBottomSand();
        if(sand != null)
            return sand.getSpirits();
        return 0;
    }

    public boolean consumeSpirits(int n)
    {
        TileEntityAncestrySand sand = getBottomSand();
        if(sand != null && sand.getSpirits() >= n) {
            sand.consumeSpirits(n);
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void update() {
        boolean valid = isValid();

        animLife++;
        activeGlowLast = activeGlow;
        activeGlow = MathHelper.clamp(activeGlow + (valid ? 0.05f : -0.05f), 0, 1);

        if(world.isRemote)
            return;

        AxisAlignedBB aabb = new AxisAlignedBB(pos).offset(0,0.5,0);

        List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,aabb);

        int spirits = getSpirits();

        if(valid && spirits > 0)
        for (EntityItem item : items) {
            if(item.cannotPickup())
                continue;

            ItemStack stack = item.getItem();
            TransmutationRecipe recipe = CraftingManagerInfuserTransmutation.getInstance().getSmeltingRecipe(stack,spirits);

            if(recipe != null)
            {
                int consumed = recipe.getRequiredSpirit(stack);
                ItemStack output = recipe.getOutput(stack);
                if(output.isEmpty())
                    continue;

                if(recipe.getInputCount() == stack.getCount())
                    stack = output;
                else {
                    EntityItem result = new EntityItem(world, item.posX, item.posY, item.posZ, output.copy());
                    result.setDefaultPickupDelay();
                    world.spawnEntity(result);
                    stack.shrink(recipe.getInputCount());
                }

                item.setItem(stack);
                if(stack.isEmpty())
                    item.setDead();
                consumeSpirits(consumed);
                break;
            }
        }
    }
}
