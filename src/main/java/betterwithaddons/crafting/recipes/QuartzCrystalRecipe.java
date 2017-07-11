package betterwithaddons.crafting.recipes;

import betterwithaddons.item.ModItems;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.tile.TileEntityCookingPot;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Random;

public class QuartzCrystalRecipe extends CauldronRecipe {
    public QuartzCrystalRecipe(ItemStack output, ItemStack secondaryOutput, Object... inputs) {
        super(output, secondaryOutput, inputs);
    }

    @Override
    public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {
        NonNullList<ItemStack> result = NonNullList.create();
        Random random = world.rand;

        //Sand piles and Soulsand piles will stew into quartz over time, but only if the cauldron has a lid blocking the top.
        //Unsafe lids should break/pop off over time
        //Soulsand additionally accumulates soul value. The cauldron lid must be opened to let the souls escape.
        //Cauldron filled with 27 or more souls will fail violently.
        //Crystallizing piles cannot stack.
        BlockPos pos = tile.getPos();
        BlockPos lidpos = pos.up();
        IBlockState state = world.getBlockState(pos);
        IBlockState lidstate = world.getBlockState(lidpos);

        int lidstrength = getLidStrength(world,lidpos,lidstate);
        int totalsouls = 0;

        boolean lidOn = lidstrength > -1;

        //if(lidstrength >= 2)
        for(int i = 0; i < inv.getSlots(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);

            if(stack.isEmpty())
                continue;

            if(stack.getItem() == BWMItems.SAND_PILE || stack.getItem() == ModItems.soulSandPile)
            {
                boolean producesSouls = stack.getItem() == ModItems.soulSandPile;
                NBTTagCompound compound = stack.getTagCompound();
                if(compound != null && compound.hasKey("QuartzCrystal"))
                {
                    int growth = compound.getInteger("QuartzCrystalGrowth");
                    int souls = compound.getInteger("QuartzSouls");

                    if(lidOn) {
                        if (growth >= 20)//TODO: configurable
                        {
                            result.add(new ItemStack(Items.QUARTZ, stack.getCount()));
                            stack.shrink(1);
                        } else {
                            compound.setInteger("QuartzCrystalGrowth", growth + 1);
                            if (producesSouls) {
                                totalsouls += souls;
                                compound.setInteger("QuartzSouls", souls + 1);
                            }
                        }
                    } else if(producesSouls) {
                        compound.setInteger("QuartzSouls", 0);
                    }
                    inv.setStackInSlot(i,stack);
                }
                else
                {

                    ItemStack crystal = new ItemStack(stack.getItem(),1,stack.getMetadata());
                    NBTTagCompound newcompound = new NBTTagCompound();
                    newcompound.setInteger("QuartzCrystal",random.nextInt());
                    newcompound.setInteger("QuartzCrystalGrowth",0);
                    newcompound.setInteger("QuartzSouls",0);
                    crystal.setTagCompound(newcompound);
                    result.add(crystal);
                    stack.shrink(1);
                    inv.setStackInSlot(i,stack);
                }
            }
        }

        if(totalsouls > 27)
        {
            world.setBlockToAir(pos);
            world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2.0f, true);
        }

        if(lidstrength == 0)
            world.setBlockToAir(lidpos);
        if(lidstrength > 0 && lidstrength < 3)
            world.destroyBlock(lidpos,lidstrength > 1);

        return result;
    }

    private int getLidStrength(World world, BlockPos pos, IBlockState state)
    {
        Block block = state.getBlock();
        //Check the shape first. Trapdoors have holes but whatever.
        if(state.isSideSolid(world,pos,EnumFacing.DOWN) || (block instanceof BlockTrapDoor && !state.getValue(BlockTrapDoor.OPEN) && state.getValue(BlockTrapDoor.HALF) == BlockTrapDoor.DoorHalf.BOTTOM))
        {
            Material material = state.getMaterial();
            float hardness = state.getBlockHardness(world,pos);

            //Material checks next
            if(material.isLiquid() || material.isReplaceable())
                return 0; //just replace with air
            else if(hardness < 1.5f || material.getCanBurn())
                return 1; //destroy without drop to prevent easy solutions to acquiring piles for the reaction
            else if(hardness < 5.0f)
                return 2; //destroy with drop
            else
                return 3; //stable
        }

        return -1; //don't even remove this block
    }
}
