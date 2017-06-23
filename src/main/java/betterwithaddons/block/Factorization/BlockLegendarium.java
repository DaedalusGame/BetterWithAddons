package betterwithaddons.block.Factorization;

import betterwithaddons.block.BlockContainerBase;
import betterwithaddons.tileentity.TileEntityLegendarium;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLegendarium extends BlockContainerBase {
    public BlockLegendarium() {
        super("legendarium", Material.ROCK);
        this.setHardness(12.0f);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityLegendarium();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = playerIn.getHeldItem(hand);
        if (playerIn.isSneaking() || heldItem.isEmpty())
            return false;
        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if (tileEntity instanceof TileEntityLegendarium) {
                ItemStack retain = ((TileEntityLegendarium) tileEntity).insertItem(heldItem);
                playerIn.setItemStackToSlot(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, retain);
                if (retain.isEmpty()) {
                    worldIn.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.PLAYERS, 1.0f,
                            ((worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.7F + 1.0F) * 1.0F);
                }
            }
        }
        return true;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        if (worldIn.isRemote || playerIn.capabilities.isCreativeMode)
        {
            return;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityLegendarium)
        {
            ItemStack retrieved = ((TileEntityLegendarium) tileEntity).retrieveItem();
            if (!retrieved.isEmpty())
                if (playerIn.inventory.addItemStackToInventory(retrieved))
                {
                    playerIn.inventory.markDirty();
                    if (playerIn.openContainer != null)
                        playerIn.openContainer.detectAndSendChanges();
                }
                else
                {
                    EntityItem ei = new EntityItem(playerIn.world, playerIn.posX, playerIn.posY, playerIn.posZ, retrieved);
                    ei.motionX = ei.motionY = ei.motionZ = 0D;
                    ei.setPickupDelay(0);
                    playerIn.world.spawnEntity(ei);
                }
        }
    }
}
