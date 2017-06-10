package betterwithaddons.interaction;

import betterwithaddons.item.ModItems;
import betterwithaddons.util.IDisableable;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.List;

public class InteractionCondensedOutputs extends Interaction {
    public static boolean ENABLED = true;
    public static boolean LOSE_BINDER = false;

    public ItemStack bagStack;
    public ItemStack crateStack;
    public ItemStack congealedStack;
    public ItemStack boltStack;
    public ItemStack bundleStack;

    public InteractionCondensedOutputs()
    {
        associatedItems = new IDisableable[] {
            ModItems.materialBundle, ModItems.materialBolt, ModItems.materialCongealed, ModItems.materialBag, ModItems.materialCrate
        };
    }

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
    }

    @Override
    public List<Interaction> getDependencies() {
        return Arrays.asList(new Interaction[]{ ModInteractions.bwm });
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        bagStack = betterwithmods.common.items.ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH,1);
        crateStack = new ItemStack(Blocks.PLANKS,1);
        congealedStack = new ItemStack(Items.SLIME_BALL,1);
        boltStack = new ItemStack(BWMBlocks.WOOD_MOULDING,1);
        bundleStack = betterwithmods.common.items.ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS,1);

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
        addBaggingRecipe(ModItems.materialBag.getMaterial("cocoa"),new ItemStack(Items.DYE,1,EnumDyeColor.BROWN.getDyeDamage()));
        addBaggingRecipe(ModItems.materialBag.getMaterial("redstone"),new ItemStack(Items.REDSTONE));
        addBaggingRecipe(ModItems.materialBag.getMaterial("glowstone"),new ItemStack(Items.GLOWSTONE_DUST));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sugar"),new ItemStack(Items.SUGAR));
        addBaggingRecipe(ModItems.materialBag.getMaterial("gunpowder"),new ItemStack(Items.GUNPOWDER));
        addBaggingRecipe(ModItems.materialBag.getMaterial("flour"),new ItemStack(BWMBlocks.RAW_PASTRY,1, BlockRawPastry.EnumType.BREAD.getMetadata()));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sulfur"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BRIMSTONE));
        addBaggingRecipe(ModItems.materialBag.getMaterial("nitre"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NITER));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sawdust"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));
        addBaggingRecipe(ModItems.materialBag.getMaterial("sawdust_soul"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST));
        addBaggingRecipe(ModItems.materialBag.getMaterial("potash"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH));
        addBaggingRecipe(ModItems.materialBag.getMaterial("hellfire"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));

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

        addRollupRecipe(ModItems.materialBolt.getMaterial("fabric"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH));
        addRollupRecipe(ModItems.materialBolt.getMaterial("vine"),new ItemStack(Blocks.VINE));
        addRollupRecipe(ModItems.materialBolt.getMaterial("paper"),new ItemStack(Items.PAPER));
        addRollupRecipe(ModItems.materialBolt.getMaterial("leather"),new ItemStack(Items.LEATHER));
        addRollupRecipe(ModItems.materialBolt.getMaterial("scoured_leather"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER));
        addRollupRecipe(ModItems.materialBolt.getMaterial("tanned_leather"),ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER));
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
        material8.setCount(8);
        CauldronManager.getInstance().addRecipe(output,new Object[]{material8,congealedStack.copy()});
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
        outmaterial.setCount(8);
        GameRegistry.addShapedRecipe(output,"aaa","aba","aaa",'a',material,'b',frame);
        GameRegistry.addShapelessRecipe(outmaterial,output);
    }
}
