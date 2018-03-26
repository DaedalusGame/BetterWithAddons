package betterwithaddons.item;

import betterwithaddons.entity.EntityArtifactFrame;
import betterwithaddons.util.IHasVariants;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class ItemArtifactFrame extends Item implements IHasVariants {
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos blockpos = pos.offset(facing);

        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(blockpos, facing, itemstack))
        {
            EntityHanging entityhanging = this.createEntity(worldIn, blockpos, facing);

            if (entityhanging != null && entityhanging.onValidSurface())
            {
                if (!worldIn.isRemote)
                {
                    entityhanging.playPlaceSound();
                    worldIn.spawnEntity(entityhanging);
                }

                itemstack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
        return new EntityArtifactFrame(worldIn, pos, clickedSide);
    }

    @Override
    public List<ModelResourceLocation> getVariantModels() {
        ArrayList<ModelResourceLocation> resourceLocations = new ArrayList<>();
        resourceLocations.add(new ModelResourceLocation(getRegistryName()+"_item", "inventory"));
        resourceLocations.add(new ModelResourceLocation(getRegistryName(), "normal"));
        resourceLocations.add(new ModelResourceLocation(getRegistryName(), "map"));
        return resourceLocations;
    }

    @Override
    public String getVariantName(int meta) {
        return null;
    }
}
