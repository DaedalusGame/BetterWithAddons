package betterwithaddons.interaction;

import betterwithaddons.handler.StumpingHandler;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWOreDictionary;
import betterwithmods.common.registry.BlockMetaRecipe;
import betterwithmods.common.registry.KilnInteraction;
import betterwithmods.common.registry.SawInteraction;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.tweaks.KilnSmelting;
import betterwithmods.util.InvUtils;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.List;

public class InteractionBTWTweak implements IInteraction {
    public static boolean ENABLED = true;
    public static boolean HARD_STUMPS = true;
    public static boolean SOFT_WOODS = true;
    public static boolean SAW_RECYCLING = true;
    public static boolean KILN_DOUBLING = true;

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
    }

    @Override
    public List<IInteraction> getDependencies() {
        return Arrays.asList(new IInteraction[]{ ModInteractions.bwm });
    }

    @Override
    public List<IInteraction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        if(HARD_STUMPS || SOFT_WOODS)
            MinecraftForge.EVENT_BUS.register(new StumpingHandler());
    }

    @Override
    public void init() {
        if(SOFT_WOODS)
        {
            StumpingHandler.addSoftWood(Blocks.LOG,BlockPlanks.EnumType.SPRUCE.getMetadata(),1.3f);
            StumpingHandler.addSoftWood(Blocks.LOG,BlockPlanks.EnumType.JUNGLE.getMetadata(),1.0f);
            StumpingHandler.addSoftWood(Blocks.LOG2,BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4,1.3f);
        }

        if(SAW_RECYCLING)
        {
            SawInteraction.INSTANCE.addRecipe(Blocks.BOOKSHELF,0,new ItemStack(BWMBlocks.WOOD_SIDING, 4, BlockPlanks.EnumType.OAK.getMetadata()),new ItemStack(Items.BOOK,3));
            SawInteraction.INSTANCE.addRecipe(Blocks.CHEST,0,new ItemStack(BWMBlocks.WOOD_SIDING, 6, BlockPlanks.EnumType.OAK.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.JUKEBOX,0,new ItemStack(BWMBlocks.WOOD_SIDING, 6, BlockPlanks.EnumType.OAK.getMetadata()),new ItemStack(Items.DIAMOND,1));
            SawInteraction.INSTANCE.addRecipe(Blocks.LADDER,0,new ItemStack(Items.STICK,2));
            SawInteraction.INSTANCE.addRecipe(Blocks.NOTEBLOCK,0,new ItemStack(BWMBlocks.WOOD_SIDING, 6, BlockPlanks.EnumType.OAK.getMetadata()),new ItemStack(Items.REDSTONE,1));
            SawInteraction.INSTANCE.addRecipe(Blocks.TRAPDOOR,0,new ItemStack(BWMBlocks.WOOD_SIDING, 2, BlockPlanks.EnumType.OAK.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.AXLE,0,new ItemStack(BWMBlocks.WOOD_CORNER, 2, BlockPlanks.EnumType.OAK.getMetadata()),new ItemStack(BWMBlocks.ROPE,1));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.BELLOWS,0,new ItemStack(BWMBlocks.WOOD_SIDING, 2, BlockPlanks.EnumType.OAK.getMetadata()), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT,3));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.GEARBOX,0,new ItemStack(BWMBlocks.WOOD_SIDING, 3, BlockPlanks.EnumType.OAK.getMetadata()), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR,3), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.SINGLE_MACHINES,4,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.OAK.getMetadata()), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR,1), new ItemStack(Blocks.WOODEN_PRESSURE_PLATE,1));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.PLATFORM,0,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(BWMBlocks.PANE,2,2));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.SINGLE_MACHINES,1,new ItemStack(BWMBlocks.WOOD_SIDING, 3, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.IRON_INGOT), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.SAW,0,new ItemStack(BWMBlocks.WOOD_SIDING, 1, BlockPlanks.EnumType.OAK.getMetadata()), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR), new ItemStack(Items.IRON_INGOT, 2));
            SawInteraction.INSTANCE.addRecipe(BWMBlocks.PUMP,0,new ItemStack(BWMBlocks.WOOD_SIDING, 3, BlockPlanks.EnumType.OAK.getMetadata()), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), new ItemStack(BWMBlocks.GRATE, 1, BlockPlanks.EnumType.OAK.getMetadata()));

            BlockPlanks.EnumType[] woodtypes = BlockPlanks.EnumType.values();
            int len = woodtypes.length;

            for(int i = 0; i < len; ++i) {
                BlockPlanks.EnumType woodtype = woodtypes[i];

                SawInteraction.INSTANCE.addRecipe(BWMBlocks.WOOD_BENCH,woodtype.getMetadata(),new ItemStack(BWMBlocks.WOOD_CORNER, 2, woodtype.getMetadata()));
                SawInteraction.INSTANCE.addRecipe(BWMBlocks.WOOD_TABLE,woodtype.getMetadata(),new ItemStack(BWMBlocks.WOOD_CORNER, 3, woodtype.getMetadata()));
            }

            SawInteraction.INSTANCE.addRecipe(Blocks.OAK_DOOR,0,new ItemStack(BWMBlocks.WOOD_SIDING, 4, BlockPlanks.EnumType.OAK.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.BIRCH_DOOR,0,new ItemStack(BWMBlocks.WOOD_SIDING, 4, BlockPlanks.EnumType.BIRCH.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.SPRUCE_DOOR,0,new ItemStack(BWMBlocks.WOOD_SIDING, 4, BlockPlanks.EnumType.SPRUCE.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.JUNGLE_DOOR,0,new ItemStack(BWMBlocks.WOOD_SIDING, 4, BlockPlanks.EnumType.JUNGLE.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.ACACIA_DOOR,0,new ItemStack(BWMBlocks.WOOD_SIDING, 4, BlockPlanks.EnumType.ACACIA.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.DARK_OAK_DOOR,0,new ItemStack(BWMBlocks.WOOD_SIDING, 4, BlockPlanks.EnumType.DARK_OAK.getMetadata()));

            SawInteraction.INSTANCE.addRecipe(Blocks.OAK_FENCE_GATE,0,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.OAK.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.BIRCH_FENCE_GATE,0,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.BIRCH.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.SPRUCE_FENCE_GATE,0,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.SPRUCE.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.JUNGLE_FENCE_GATE,0,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.JUNGLE.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.ACACIA_FENCE_GATE,0,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.ACACIA.getMetadata()));
            SawInteraction.INSTANCE.addRecipe(Blocks.DARK_OAK_FENCE_GATE,0,new ItemStack(BWMBlocks.WOOD_MOULDING, 3, BlockPlanks.EnumType.DARK_OAK.getMetadata()));
        }
    }

    @Override
    public void postInit() {

        if(KILN_DOUBLING && ModuleLoader.isFeatureEnabled(KilnSmelting.class))
        {
            for (ItemStack ore : BWOreDictionary.oreNames) {
                if(ore.getItem() instanceof ItemBlock)
                {
                    BlockMetaRecipe recipe = KilnInteraction.INSTANCE.getRecipe(ore);
                    List<ItemStack> outputs = recipe.getOutputs();
                    if(outputs.size() > 0)
                    {
                        ItemStack output = outputs.get(0).copy();
                        output.setCount(MathHelper.clamp(output.getCount() * 2,0,output.getMaxStackSize()));
                        outputs.set(0,output);
                    }
                }
            }
        }
    }
}
