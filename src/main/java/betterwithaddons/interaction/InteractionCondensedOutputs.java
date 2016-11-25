package betterwithaddons.interaction;

import betterwithaddons.item.ModItems;
import betterwithmods.BWMBlocks;
import betterwithmods.api.BWMRecipeHelper;
import betterwithmods.items.ItemMaterial;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import scala.actors.threadpool.Arrays;

import java.util.List;

public class InteractionCondensedOutputs implements IInteraction {
    public static boolean ENABLED = true;
    public static boolean LOSE_BINDER = false;

    public ItemStack bagStack;
    public ItemStack crateStack;
    public ItemStack congealedStack;
    public ItemStack boltStack;
    public ItemStack bundleStack;

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
        bagStack = betterwithmods.items.ItemMaterial.getMaterial("hemp_cloth",1);
        crateStack = new ItemStack(Blocks.PLANKS,1);
        congealedStack = new ItemStack(Items.SLIME_BALL,1);
        boltStack = new ItemStack(BWMBlocks.WOOD_MOULDING,1);
        bundleStack = betterwithmods.items.ItemMaterial.getMaterial("hemp_fibers",1);

        if(!LOSE_BINDER) {
            ModItems.materialBag.setContainer(bagStack);
            ModItems.materialCrate.setContainer(crateStack);
            ModItems.materialCongealed.setContainer(congealedStack);
            ModItems.materialBolt.setContainer(boltStack);
            ModItems.materialBundle.setContainer(bundleStack);
        }
    }

    @Override
    public void init() {
        addBaggingRecipe(ModItems.materialBag.getMaterial("seed"),new ItemStack(Items.WHEAT_SEEDS));
        addBaggingRecipe(ModItems.materialBag.getMaterial("seed_hemp"),new ItemStack(BWMBlocks.HEMP));
        addBaggingRecipe(ModItems.materialBag.getMaterial("seed_melon"),new ItemStack(Items.MELON_SEEDS));
        addBaggingRecipe(ModItems.materialBag.getMaterial("seed_pumpkin"),new ItemStack(Items.PUMPKIN_SEEDS));
        addBaggingRecipe(ModItems.materialBag.getMaterial("seed_beets"),new ItemStack(Items.BEETROOT_SEEDS));
        addBaggingRecipe(ModItems.materialBag.getMaterial("redstone"),new ItemStack(Items.REDSTONE));
        addBaggingRecipe(ModItems.materialBag.getMaterial("glowstone"),new ItemStack(Items.GLOWSTONE_DUST));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sugar"),new ItemStack(Items.SUGAR));
        addBaggingRecipe(ModItems.materialBag.getMaterial("gunpowder"),new ItemStack(Items.GUNPOWDER));
        addBaggingRecipe(ModItems.materialBag.getMaterial("flour"),ItemMaterial.getMaterial("flour"));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sulfur"),ItemMaterial.getMaterial("brimstone"));
        addBaggingRecipe(ModItems.materialBag.getMaterial("nitre"),ItemMaterial.getMaterial("niter"));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sawdust"),ItemMaterial.getMaterial("sawdust"));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sawdust_soul"),ItemMaterial.getMaterial("soul_dust"));
        addBaggingRecipe(ModItems.materialBag.getMaterial("potash"),ItemMaterial.getMaterial("potash"));
        addBaggingRecipe(ModItems.materialBag.getMaterial("hellfire"),ItemMaterial.getMaterial("hellfire_dust"));

        addCratingRecipe(ModItems.materialCrate.getMaterial("pork"),new ItemStack(Items.COOKED_PORKCHOP));
        addCratingRecipe(ModItems.materialCrate.getMaterial("pork_raw"),new ItemStack(Items.PORKCHOP));
        addCratingRecipe(ModItems.materialCrate.getMaterial("steak"),new ItemStack(Items.COOKED_BEEF));
        addCratingRecipe(ModItems.materialCrate.getMaterial("steak_raw"),new ItemStack(Items.BEEF));
        addCratingRecipe(ModItems.materialCrate.getMaterial("chicken"),new ItemStack(Items.COOKED_CHICKEN));
        addCratingRecipe(ModItems.materialCrate.getMaterial("chicken_raw"),new ItemStack(Items.CHICKEN));
        addCratingRecipe(ModItems.materialCrate.getMaterial("mutton"),new ItemStack(Items.COOKED_MUTTON));
        addCratingRecipe(ModItems.materialCrate.getMaterial("mutton_raw"),new ItemStack(Items.MUTTON));
        addCratingRecipe(ModItems.materialCrate.getMaterial("rabbit"),new ItemStack(Items.COOKED_RABBIT));
        addCratingRecipe(ModItems.materialCrate.getMaterial("rabbit_raw"),new ItemStack(Items.RABBIT));
        addCratingRecipe(ModItems.materialCrate.getMaterial("egg"),new ItemStack(Items.EGG));
        addCratingRecipe(ModItems.materialCrate.getMaterial("slime"),new ItemStack(Items.SLIME_BALL));
        addCratingRecipe(ModItems.materialCrate.getMaterial("enderpearl"),new ItemStack(Items.ENDER_PEARL));

        addCongealingRecipe(ModItems.materialCongealed.getMaterial("mushroom"),new ItemStack(Blocks.BROWN_MUSHROOM));
        addCongealingRecipe(ModItems.materialCongealed.getMaterial("amanita"),new ItemStack(Blocks.RED_MUSHROOM));
        addCongealingRecipe(ModItems.materialCongealed.getMaterial("bone"),new ItemStack(Items.BONE));
        addCongealingRecipe(ModItems.materialCongealed.getMaterial("flesh"),new ItemStack(Items.ROTTEN_FLESH));
        addCongealingRecipe(ModItems.materialCongealed.getMaterial("eye"),new ItemStack(Items.SPIDER_EYE));
        addCongealingRecipe(ModItems.materialCongealed.getMaterial("wart"),new ItemStack(Items.NETHER_WART));

        addRollupRecipe(ModItems.materialBolt.getMaterial("fabric"),betterwithmods.items.ItemMaterial.getMaterial("hemp_cloth"));
        addRollupRecipe(ModItems.materialBolt.getMaterial("vine"),new ItemStack(Blocks.VINE));
        addRollupRecipe(ModItems.materialBolt.getMaterial("paper"),new ItemStack(Items.PAPER));
        addRollupRecipe(ModItems.materialBolt.getMaterial("leather"),new ItemStack(Items.LEATHER));
        addRollupRecipe(ModItems.materialBolt.getMaterial("scoured_leather"),betterwithmods.items.ItemMaterial.getMaterial("scoured_leather"));
        addRollupRecipe(ModItems.materialBolt.getMaterial("tanned_leather"),betterwithmods.items.ItemMaterial.getMaterial("tanned_leather"));
        addRollupRecipe(ModItems.materialBolt.getMaterial("string"),new ItemStack(Items.STRING));

        addBundlingRecipe(ModItems.materialBundle.getMaterial("feather"),new ItemStack(Items.FEATHER));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("blazerods"),new ItemStack(Items.BLAZE_ROD));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("arrows"),new ItemStack(Items.ARROW));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("oak"),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.OAK.getMetadata()));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("birch"),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("spruce"),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("jungle"),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("acacia"),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.ACACIA.getMetadata()));
        addBundlingRecipe(ModItems.materialBundle.getMaterial("darkoak"),new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.DARK_OAK.getMetadata()));
    }

    @Override
    public void postInit() {

    }

    private void addBaggingRecipe(ItemStack output, ItemStack material)
    {
        addCondensingRecipe(output, material, bagStack);
    }

    private void addCratingRecipe(ItemStack output, ItemStack material)
    {
        addCondensingRecipe(output, material, crateStack);
    }

    private void addCongealingRecipe(ItemStack output, ItemStack material)
    {
        addCondensingRecipe(output, material, congealedStack);

        ItemStack material8 = material.copy();
        material8.stackSize = 8;
        BWMRecipeHelper.addCauldronRecipe(output,new Object[]{material8,congealedStack.copy()});
    }

    private void addRollupRecipe(ItemStack output, ItemStack material)
    {
        addCondensingRecipe(output, material, boltStack);
    }

    private void addBundlingRecipe(ItemStack output, ItemStack material)
    {
        addCondensingRecipe(output, material, bundleStack);
    }

    private void addCondensingRecipe(ItemStack output, ItemStack material, ItemStack frame)
    {
        ItemStack outmaterial = material.copy();
        outmaterial.stackSize = 8;
        GameRegistry.addShapedRecipe(output,"aaa","aba","aaa",'a',material,'b',frame);
        GameRegistry.addShapelessRecipe(outmaterial,output);
    }
}
