package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockModUnbaked;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.handler.ButcherHandler;
import betterwithaddons.handler.FallingPlatformHandler;
import betterwithaddons.handler.HardcoreWoolHandler;
import betterwithaddons.item.ModItems;
import betterwithaddons.tileentity.TileEntityAqueductWater;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.blockmeta.managers.KilnManager;
import betterwithmods.common.registry.blockmeta.recipe.KilnRecipe;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import betterwithmods.common.registry.bulk.manager.MillManager;
import betterwithmods.common.registry.bulk.manager.StokedCauldronManager;
import betterwithmods.common.registry.bulk.manager.StokedCrucibleManager;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import betterwithmods.common.registry.bulk.recipes.MillRecipe;
import betterwithmods.common.registry.bulk.recipes.StokedCauldronRecipe;
import betterwithmods.common.registry.bulk.recipes.StokedCrucibleRecipe;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.HCPiles;
import betterwithmods.module.hardcore.hchunger.HCHunger;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockPlanks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

public class InteractionBWM extends Interaction {
    final String modid = "betterwithmods";
    public static boolean ENABLED = true;
    public static boolean MILL_CLAY = true;
    public static boolean CHORUS_IN_CAULDRON = true;
    public static boolean BUTCHER_BLOCKS = true;
    public static boolean FALLING_PLATFORMS = false;
    public static boolean CAULDRONS_EXPLODE = true;
    public static boolean HARDCORE_SHEARING = true;
    public static int WOOL_MULTIPLIER = 1;
    public static boolean DYE_IN_CAULDRON = true;

    @Override
    public boolean isActive() {
        return ENABLED && Loader.isModLoaded(modid);
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
    }

    @Override
    public List<Interaction> getDependencies() {
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        if (BUTCHER_BLOCKS)
            MinecraftForge.EVENT_BUS.register(new ButcherHandler());
        if (FALLING_PLATFORMS)
            MinecraftForge.EVENT_BUS.register(new FallingPlatformHandler());
        if (HARDCORE_SHEARING)
            MinecraftForge.EVENT_BUS.register(new HardcoreWoolHandler());
    }

    public void addCauldronExplosion() {
        ItemStack explosion = new ItemStack(ModItems.explosion);

        StokedCauldronRecipe cauldronRecipe = new StokedCauldronRecipe(explosion, ItemStack.EMPTY, new Object[]{"listAllExplosives"}) {
            @Override
            public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {
                return explodeCauldron(world, tile, inv);
            }
        };
        StokedCrucibleRecipe crucibleRecipe = new StokedCrucibleRecipe(explosion, ItemStack.EMPTY, new Object[]{"listAllExplosives"}) {
            @Override
            public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {
                return explodeCauldron(world, tile, inv);
            }
        };

        cauldronRecipe.setPriority(100);
        crucibleRecipe.setPriority(100);

        StokedCauldronManager.getInstance().addRecipe(cauldronRecipe);
        StokedCrucibleManager.getInstance().addRecipe(crucibleRecipe);
    }

    public NonNullList<ItemStack> explodeCauldron(World world, TileEntity tile, ItemStackHandler inv) {
        float expSize = 0.0f;
        int blockAmt = 0;

        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty())
                continue;
            else if (ItemUtil.matchesOreDict(stack, "listAllExplosives"))
                if (stack.getItem() instanceof ItemBlock)
                    blockAmt += stack.getCount();
                else
                    expSize += stack.getCount() / 64f;
            inv.setStackInSlot(i, ItemStack.EMPTY);
        }

        expSize = blockAmt == 0 ? Math.max(expSize, 2.0f) : Math.max(expSize, 4.0f) + blockAmt;
        BlockPos pos = tile.getPos();
        world.setBlockToAir(pos);
        world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, expSize, true);

        return NonNullList.create();
    }

    public static NonNullList<ItemStack> convertShearedWool(List<ItemStack> sheared)
    {
        NonNullList<ItemStack> returnList = NonNullList.create();
        for(ItemStack stack : sheared)
        {
            if(stack.getItem() == Item.getItemFromBlock(Blocks.WOOL))
                returnList.add(new ItemStack(ModItems.wool,stack.getCount() * WOOL_MULTIPLIER,stack.getMetadata()));
            else
                returnList.add(stack);
        }
        return returnList;
    }

    public static void convertShearedWoolEntities(List<EntityItem> sheared) {
        for (EntityItem item : sheared) {
            ItemStack stack = item.getEntityItem();
            if(stack.getItem() == Item.getItemFromBlock(Blocks.WOOL))
                item.setEntityItemStack(new ItemStack(ModItems.wool,stack.getCount() * WOOL_MULTIPLIER,stack.getMetadata()));
        }
    }

    @Override
    public void init() {
        if (!isActive())
            return;

        String[] dyeOredictTags = new String[]{"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};

        if(HARDCORE_SHEARING)
        {
            BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(EntitySheep.class,(world, pos, entity, itemStack) -> {
                EntitySheep sheep = (EntitySheep) entity;
                if (sheep.isShearable(new ItemStack(Items.SHEARS), world, pos)) {
                    return convertShearedWool(sheep.onSheared(new ItemStack(Items.SHEARS), world, pos, 0));
                }
                return NonNullList.create();});

            BetterWithAddons.removeCraftingRecipe(new ItemStack(Blocks.WOOL));
            for (EnumDyeColor color : EnumDyeColor.values()) {
                ItemStack wool = ModItems.wool.getByColor(color);
                GameRegistry.addShapedRecipe(new ItemStack(Blocks.WOOL,1,color.getMetadata())," o ","oxo"," o ",'o',wool,'x',new ItemStack(BWMBlocks.AESTHETIC,1,BlockAesthetic.EnumType.WICKER.getMeta()));
            }
        }
        if(DYE_IN_CAULDRON) {
            //Dyeing
            for (EnumDyeColor color : EnumDyeColor.values())
                if (color != EnumDyeColor.WHITE) {
                    CauldronManager.getInstance().addRecipe(new ItemStack(Blocks.WOOL, 8, color.getMetadata()), new Object[]{new ItemStack(Blocks.WOOL, 8, EnumDyeColor.WHITE.getMetadata()), new OreStack("dye" + dyeOredictTags[color.ordinal()], 1)});
                }
            //Bleaching
            for (EnumDyeColor color : EnumDyeColor.values())
                if (color != EnumDyeColor.WHITE) {
                    CauldronManager.getInstance().addRecipe(new ItemStack(Blocks.WOOL, 8, EnumDyeColor.WHITE.getMetadata()), new Object[]{new ItemStack(Blocks.WOOL, 8, color.getMetadata()), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH)});
                }
        }

        //Temporary until we PR soulsand piles
        HCPiles.registerPile(Blocks.SOUL_SAND,new ItemStack(ModItems.soulSandPile,3));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.soulSandPile,4),new ItemStack(Blocks.SOUL_SAND));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.SOUL_SAND),new ItemStack(ModItems.soulSandPile),new ItemStack(ModItems.soulSandPile),new ItemStack(ModItems.soulSandPile),new ItemStack(ModItems.soulSandPile));

        OreDictionary.registerOre("listAllExplosives", new ItemStack(Blocks.TNT));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(Items.GUNPOWDER));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMItems.DYNAMITE));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMBlocks.MINING_CHARGE));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.HELLFIRE.getMeta()));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));

        if(CAULDRONS_EXPLODE)
            addCauldronExplosion();

        TileEntityAqueductWater.addWaterSource(BWMBlocks.TEMP_LIQUID_SOURCE);
        BlockBUD.addBlacklistBlock(ModBlocks.pcbwire);

        ItemStack arrowhead = ModItems.material.getMaterial("arrowhead");
        ItemStack haft = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT);
        ItemStack string = new ItemStack(BWMBlocks.ROPE);
        ItemStack feather = new ItemStack(Items.FEATHER);
        String oreIronIngot = "ingotIron";
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.material.getMaterial("arrowhead"), " o ", "ooo", "o o", 'o', "nuggetSoulforgedSteel"));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.greatarrow, 1), "a", "b", "c", 'a', arrowhead, 'b', haft, 'c', feather);
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatbow, 1), " bc", "b c", " bc", 'b', haft, 'c', string));
        StokedCauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.pcbblock), ItemStack.EMPTY, new Object[]{new ItemStack(Items.FERMENTED_SPIDER_EYE), new ItemStack(Blocks.STONEBRICK), "dustPotash"});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedBeetroot), ItemStack.EMPTY, new Object[]{new ItemStack(Items.BEETROOT)});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedCarrot), ItemStack.EMPTY, new Object[]{new ItemStack(Items.CARROT)});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedPotato), ItemStack.EMPTY, new Object[]{new ItemStack(Items.POTATO)});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedEgg), ItemStack.EMPTY, new Object[]{new ItemStack(Items.EGG)});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedClownfish), ItemStack.EMPTY, new Object[]{new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata())});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.cookedPuffer), ItemStack.EMPTY, new Object[]{new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())});

        CauldronManager.getInstance().addRecipe(ModItems.material.getMaterial("bone_ingot"), ItemStack.EMPTY, new Object[]{new ItemStack(Items.BONE, 2), new ItemStack(Items.DYE, 8, 15)});
        CauldronManager.getInstance().addRecipe(ModItems.material.getMaterial("midori_popped"), ItemStack.EMPTY, new Object[]{ModItems.material.getMaterial("midori")});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.meatballs, 1), ItemStack.EMPTY, new Object[]{new ItemStack(ModItems.groundMeat, 1)});
        /*MillManager.getInstance().addRecipe(0, new ItemStack(ModItems.groundMeat, 3), ItemStack.EMPTY, new Object[]{new ItemStack(Items.BEEF)});
        MillManager.getInstance().addRecipe(0, new ItemStack(ModItems.groundMeat, 2), ItemStack.EMPTY, new Object[]{new ItemStack(Items.MUTTON)});
        MillManager.getInstance().addRecipe(0, new ItemStack(ModItems.groundMeat, 1), ItemStack.EMPTY, new Object[]{new ItemStack(Items.CHICKEN)});
        MillManager.getInstance().addRecipe(0, new ItemStack(ModItems.groundMeat, 3), ItemStack.EMPTY, new Object[]{new ItemStack(Items.PORKCHOP)});
        MillManager.getInstance().addRecipe(0, new ItemStack(ModItems.groundMeat, 1), ItemStack.EMPTY, new Object[]{new ItemStack(Items.RABBIT)});*/

        MillManager.getInstance().addRecipe(0, new ItemStack(ModBlocks.worldScale, 1), ItemStack.EMPTY, new Object[]{new ItemStack(ModBlocks.worldScaleOre, 1, 1)});

        //Bark
        ModBlocks.mulberryLog.barkStack = ModItems.materialJapan.getMaterial("bark_mulberry");
        ModBlocks.sakuraLog.barkStack = ModItems.materialJapan.getMaterial("bark_sakura");

        //Thorn Vines
        ItemStack rosebush = new ItemStack(Blocks.DOUBLE_PLANT, 4, BlockDoublePlant.EnumPlantType.ROSE.getMeta());
        ItemStack thornrose = ModItems.material.getMaterial("thornrose", 2);
        ItemStack soulUrn = new ItemStack(BWMBlocks.URN, 1, BlockUrn.EnumUrnType.FULL.getMeta());
        ItemStack cactus = new ItemStack(Blocks.CACTUS, 1);
        ItemStack dung = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG, 1);
        ItemStack midori = ModItems.material.getMaterial("midori",8);
        CauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.thornrose), ItemStack.EMPTY, new Object[]{cactus, rosebush, dung, soulUrn});
        CauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.thornrose), ItemStack.EMPTY, new Object[]{midori, thornrose, dung, soulUrn});
        MillManager.getInstance().addRecipe(0,new ItemStack(Items.DYE,1, EnumDyeColor.GREEN.getDyeDamage()),new Object[]{ ModItems.material.getMaterial("midori",1) });

        //Alicio Sapling
        ItemStack wheat = new ItemStack(Items.WHEAT, 16);
        ItemStack flesh = new ItemStack(Items.ROTTEN_FLESH, 4);
        ItemStack red = new ItemStack(Items.DYE, 8, EnumDyeColor.RED.getDyeDamage());
        ItemStack tree = new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata());
        CauldronManager.getInstance().addRecipe(new ItemStack(ModBlocks.luretreeSapling), ItemStack.EMPTY, new Object[]{tree, wheat, red, flesh});

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.chute, 1), "s s", " p ", "mgm", 's', new ItemStack(BWMBlocks.WOOD_SIDING), 'm', new ItemStack(BWMBlocks.WOOD_MOULDING), 'g', ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR), 'p', new ItemStack(Blocks.WOODEN_PRESSURE_PLATE));

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 0), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.getMetadata()), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 1), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 2), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 3), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 4), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.ACACIA.getMetadata()), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 5), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 6), new ItemStack(ModBlocks.sakuraSapling), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 7), new ItemStack(ModBlocks.mulberrySapling), soulUrn);
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.ecksieSapling, 1, 8), new ItemStack(ModBlocks.luretreeSapling), soulUrn);

        if (MILL_CLAY) {
            MillManager.getInstance().addRecipe(0, new ItemStack(Items.BRICK, 4), ItemStack.EMPTY, new Object[]{new ItemStack(Blocks.HARDENED_CLAY, 1)});

            EnumDyeColor[] dyes = EnumDyeColor.values();
            int len = dyes.length;

            for (int i = 0; i < len; ++i) {
                EnumDyeColor dye = dyes[i];
                ItemStack brick = new ItemStack(ModItems.stainedBrick, 1, dye.getMetadata());
                MillManager.getInstance().addRecipe(0, new ItemStack(ModItems.stainedBrick, 4, dye.getMetadata()), ItemStack.EMPTY, new Object[]{new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata())});
                GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.coloredBrick, 1, dye.getMetadata()), "bb", "bb", 'b', brick);
            }
        }

        if (!CHORUS_IN_CAULDRON)
            GameRegistry.addSmelting(ModItems.material.getMaterial("midori"), ModItems.material.getMaterial("midori_popped"), 0.1f);

        fixRecipes();
    }

    @Override
    public void postInit() {
        //Fixes baked stuff showing up in the cauldron
        removeCauldronRecipe(new ItemStack(Items.BAKED_POTATO));
        removeCauldronRecipe(new ItemStack(ModItems.bakedCarrot));
        removeCauldronRecipe(new ItemStack(ModItems.bakedBeetroot));

        if (CHORUS_IN_CAULDRON)
            BetterWithAddons.instance.removeSmeltingRecipe(new ItemStack(Items.CHORUS_FRUIT_POPPED));

        for(ItemStack stack : OreDictionary.getOres("listAllmeat")) {
            ItemStack groundMeat = new ItemStack(ModItems.groundMeat);
            ItemStack meatStack = stack.copy();
            meatStack.setCount(1);
            if(meatStack.getItem() instanceof ItemFood) {
                int amount = ((ItemFood) meatStack.getItem()).getHealAmount(meatStack) / ModItems.groundMeat.getHealAmount(groundMeat);
                groundMeat.setCount(Math.max(amount,1));
            }
            MillManager.getInstance().addRecipe(new MillRecipe(0, groundMeat, ItemStack.EMPTY, new Object[]{meatStack}));
        }
    }

    public void fixRecipes() {
        /*ItemStack ingotSoulforgedSteel = InteractionHelper.findItem(modid,"material",1,14);
        ItemStack ingotIron = new ItemStack(Items.IRON_INGOT,1);
        ItemStack nuggedSoulforgedSteel = InteractionHelper.findItem(modid,"material",9,31);
        ItemStack nuggetIron = InteractionHelper.findItem(modid,"material",9,30);
        registerCompressRecipe(nuggedSoulforgedSteel,ingotSoulforgedSteel,"nuggetSoulforgedSteel","ingotSoulforgedSteel");
        registerCompressRecipe(nuggetIron,ingotIron,"nuggetIron","ingotIron");*/
    }

    private static void registerCompressRecipe(ItemStack small, ItemStack big, String oreSmall, String oreBig) {
        // ingot -> block
        GameRegistry.addRecipe(new ShapedOreRecipe(big, "###", "###", "###", '#', oreSmall));
        // block -> 9 ingot
        small = small.copy();
        small.setCount(9);
        GameRegistry.addRecipe(new ShapelessOreRecipe(small, oreBig));
    }

    private static void removeCauldronRecipe(ItemStack output) {
        CauldronManager.getInstance().getRecipes().removeIf(r -> r.getOutput().isItemEqual(output));
    }
}
