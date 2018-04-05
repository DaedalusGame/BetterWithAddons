package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.manager.CraftingManagerPacking;
import betterwithaddons.handler.ButcherHandler;
import betterwithaddons.handler.FallingPlatformHandler;
import betterwithaddons.handler.HardcorePackingHandler;
import betterwithaddons.handler.HardcoreWoolHandler;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.IngredientSized;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.ModuleLoader;
import betterwithmods.module.hardcore.crafting.HCDiamond;
import betterwithmods.module.hardcore.needs.HCCooking;
import betterwithmods.module.hardcore.needs.HCPiles;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class InteractionBWM extends Interaction {
    final String modid = "betterwithmods";
    public static boolean ENABLED = true;
    public static boolean MILL_CLAY = true;
    public static boolean CHORUS_IN_CAULDRON = true;
    public static boolean BUTCHER_BLOCKS = true;
    public static boolean FALLING_PLATFORMS = false;
    public static boolean HARDCORE_SHEARING = true;
    public static boolean HARDCORE_PACKING = true;
    public static int WOOL_MULTIPLIER = 1;
    public static boolean DYE_IN_CAULDRON = true;
    public static boolean HIDDEN_ENCHANTS = false;

    public static String[] SHEARS_WHITELIST = new String[] {
            "tconstruct:kama",
            "tconstruct:scythe" //This is normally AoE and wlll lose the property
    };

    @Override
    protected String getName() {
        return "interaction.BetterWithMods";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled", "Whether the Better With Mods compat module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.", ENABLED);
        MILL_CLAY = loadPropBool("MillClay", "Hardened clay can be milled into bricks, saving fuel and allowing dyed bricks.", MILL_CLAY);
        CHORUS_IN_CAULDRON = loadPropBool("ChorusInCauldron", "Chorus fruit (and Midori) can only be popped in a cauldron.", CHORUS_IN_CAULDRON);
        BUTCHER_BLOCKS = loadPropBool("ButcherBlocks", "Striking an enemy on chopping blocks will bloody them and bestow a short strenth buff.", BUTCHER_BLOCKS);
        HARDCORE_SHEARING = loadPropBool("HardcoreShearing", "Sheep will only be sheared into wool items, which must be crafted into wool blocks. This does not work with machines that use onSheared, but should work with Fake Players.", HARDCORE_SHEARING);
        HARDCORE_PACKING = loadPropBool("HardcorePacking", "Items can be compressed in world with a piston pushing them into an enclosed space.", HARDCORE_PACKING);
        WOOL_MULTIPLIER = loadPropInt("WoolMultiplier", "Adjusts how much wool a sheep drops if Hardcore Shearing is enabled.", WOOL_MULTIPLIER);
        DYE_IN_CAULDRON = loadPropBool("DyeInCauldron", "Wool can be dyed in batches of 8 in a cauldron and bleached with potash.", DYE_IN_CAULDRON);
        SHEARS_WHITELIST = loadPropStringList("ShearWhitelist", "Extra items that are functionally shears but don't extend ItemShears.", SHEARS_WHITELIST);
    }

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
        if (HARDCORE_SHEARING) {
            MinecraftForge.EVENT_BUS.register(new HardcoreWoolHandler());
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation("minecraft","string_to_wool"));
        }
        if(HARDCORE_PACKING)
            MinecraftForge.EVENT_BUS.register(new HardcorePackingHandler());

        ConditionModule.MODULES.put("HardcoreDiamond", () -> ModuleLoader.isFeatureEnabled(HCDiamond.class));
        ConditionModule.MODULES.put("HardcoreShearing", () -> HARDCORE_SHEARING);
        ConditionModule.MODULES.put("HardcoreHunger", () -> ModuleLoader.isFeatureEnabled(HCCooking.class));

        HardcoreWoolHandler.EXTRA_SHEARS = new HashSet<>(Arrays.asList(SHEARS_WHITELIST)); //Populate the set of extra shears.
    }

    public static NonNullList<ItemStack> convertShearedWool(List<ItemStack> sheared)
    {
        NonNullList<ItemStack> returnList = NonNullList.create();
        returnList.addAll(sheared.stream().map(InteractionBWM::convertOneWool).collect(Collectors.toList()));
        return returnList;
    }

    public static void convertShearedWoolEntities(List<EntityItem> sheared) {
        for (EntityItem item : sheared) {
            ItemStack stack = item.getItem();
            item.setItem(convertOneWool(stack));
        }
    }

    public static ItemStack convertOneWool(ItemStack stack)
    {
        if(stack.getItem() == Item.getItemFromBlock(Blocks.WOOL))
            return new ItemStack(ModItems.wool,stack.getCount() * WOOL_MULTIPLIER,stack.getMetadata());
        else if(ItemUtil.matchesOreDict(stack,"blockWool"))
            return new ItemStack(ModItems.wool,stack.getCount() * WOOL_MULTIPLIER,0);
        else
            return stack;
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

            for (EnumDyeColor color : EnumDyeColor.values()) {
                ItemStack wool = ModItems.wool.getByColor(color);
            }
        }
        if(DYE_IN_CAULDRON) {
            //Dyeing
            ItemStack whiteWool = new ItemStack(Blocks.WOOL, 8, EnumDyeColor.WHITE.getMetadata());
            for (EnumDyeColor color : EnumDyeColor.values())
                if (color != EnumDyeColor.WHITE) {
                    BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(Ingredient.fromStacks(whiteWool),new OreIngredient("dye" + dyeOredictTags[color.ordinal()])),new ItemStack(Blocks.WOOL, 8, color.getMetadata()));
                }
            //Bleaching
            for (EnumDyeColor color : EnumDyeColor.values())
                if (color != EnumDyeColor.WHITE) {
                    BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(Ingredient.fromStacks(new ItemStack(Blocks.WOOL, 8, color.getMetadata())),Ingredient.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH))), whiteWool);
                }
        }

        //Temporary until we PR soulsand piles
        HCPiles.registerPile(Blocks.SOUL_SAND,new ItemStack(ModItems.soulSandPile,3));

        OreDictionary.registerOre("listAllExplosives", new ItemStack(Blocks.TNT));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(Items.GUNPOWDER));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMItems.DYNAMITE));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMBlocks.MINING_CHARGE));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.HELLFIRE.getMeta()));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));
        OreDictionary.registerOre("listAllExplosives", ModItems.materialBag.getMaterial("gunpowder"));
        OreDictionary.registerOre("listAllExplosives", ModItems.materialBag.getMaterial("hellfire"));

        OreDictionary.registerOre("listAllmeat", Items.RABBIT);
        OreDictionary.registerOre("listAllmeatcooked", ModItems.cookedClownfish);
        OreDictionary.registerOre("blockDung", BlockAesthetic.getStack(BlockAesthetic.EnumType.DUNG));

        OreDictionary.registerOre("book", Items.WRITTEN_BOOK);
        OreDictionary.registerOre("book", Items.BOOK);

        //Hardcore Packing
        if(HARDCORE_PACKING) {
            CraftingManagerPacking.getInstance().addRecipe(Blocks.DIRT.getDefaultState(), new ItemStack(Blocks.DIRT), IngredientSized.fromItem(BWMItems.DIRT_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SAND.getDefaultState(), new ItemStack(Blocks.SAND), IngredientSized.fromItem(BWMItems.SAND_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), new ItemStack(Blocks.SAND, 1, 1), IngredientSized.fromItem(BWMItems.RED_SAND_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.GRAVEL.getDefaultState(), new ItemStack(Blocks.GRAVEL), IngredientSized.fromItem(BWMItems.GRAVEL_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SOUL_SAND.getDefaultState(), new ItemStack(Blocks.SOUL_SAND), IngredientSized.fromItem(ModItems.soulSandPile, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.CLAY.getDefaultState(), new ItemStack(Blocks.CLAY), IngredientSized.fromItem(Items.CLAY_BALL, 4));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.NETHERCLAY), BlockAesthetic.getStack(BlockAesthetic.EnumType.NETHERCLAY), IngredientSized.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE, 4)));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SNOW.getDefaultState(), new ItemStack(Blocks.SNOW), IngredientSized.fromItem(Items.SNOWBALL, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.BRICK_BLOCK.getDefaultState(), new ItemStack(Blocks.BRICK_BLOCK), IngredientSized.fromItem(Items.BRICK, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.NETHER_BRICK.getDefaultState(), new ItemStack(Blocks.NETHER_BRICK), IngredientSized.fromItem(Items.NETHERBRICK, 4));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.FLINT), BlockAesthetic.getStack(BlockAesthetic.EnumType.FLINT), IngredientSized.fromItem(Items.FLINT, 9));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.DUNG), BlockAesthetic.getStack(BlockAesthetic.EnumType.DUNG), IngredientSized.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG, 9)));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.SOAP), BlockAesthetic.getStack(BlockAesthetic.EnumType.SOAP), IngredientSized.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP, 9)));
        }

        BlockBUD.addBlacklistBlock(ModBlocks.pcbwire);

        ItemStack arrowhead = ModItems.material.getMaterial("arrowhead");
        ItemStack haft = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT);
        ItemStack string = new ItemStack(BWMBlocks.ROPE);
        ItemStack feather = new ItemStack(Items.FEATHER);
        String oreIronIngot = "ingotIron";
        BWRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE),Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK)),new OreIngredient("dustPotash")),Lists.newArrayList(new ItemStack(ModBlocks.pcbblock)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.BEETROOT),new ItemStack(ModItems.cookedBeetroot));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.CARROT),new ItemStack(ModItems.cookedCarrot));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.POTATO),new ItemStack(ModItems.cookedPotato));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.EGG),new ItemStack(ModItems.cookedEgg));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata())),new ItemStack(ModItems.cookedClownfish));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())),new ItemStack(ModItems.cookedPuffer));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(Items.BONE,2)), getIngredient(new ItemStack(Items.DYE, 8, 15))),ModItems.material.getMaterial("bone_ingot"));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromStacks(ModItems.material.getMaterial("midori")),ModItems.material.getMaterial("midori_popped"));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(ModItems.groundMeat),new ItemStack(ModItems.meatballs, 1));
        BWRegistry.MILLSTONE.addMillRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.worldScaleOre, 1, 1)),new ItemStack(ModBlocks.worldScale, 1));

        //Bark
        ModBlocks.mulberryLog.barkStack = ModItems.materialJapan.getMaterial("bark_mulberry");
        ModBlocks.sakuraLog.barkStack = ModItems.materialJapan.getMaterial("bark_sakura");

        //Thorn Vines
        ItemStack rosebush = new ItemStack(Blocks.DOUBLE_PLANT, 4, BlockDoublePlant.EnumPlantType.ROSE.getMeta());
        ItemStack thornrose = ModItems.material.getMaterial("thornrose", 2);
        ItemStack soulUrn = new ItemStack(BWMBlocks.URN, 1, BlockUrn.EnumType.FULL.getMeta());
        ItemStack cactus = new ItemStack(Items.DYE,1, EnumDyeColor.GREEN.getDyeDamage());
        ItemStack dung = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG, 1);
        ItemStack midori = ModItems.material.getMaterial("midori",8);
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(getIngredient(cactus), getIngredient(rosebush), getIngredient(dung), getIngredient(soulUrn)),Lists.newArrayList(new ItemStack(ModBlocks.thornrose)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(getIngredient(midori), getIngredient(thornrose), getIngredient(dung), getIngredient(soulUrn)),Lists.newArrayList(new ItemStack(ModBlocks.thornrose)));
        BWRegistry.MILLSTONE.addMillRecipe(ModItems.material.getMaterial("midori",1),cactus);

        //Alicio Sapling
        ItemStack wheat = new ItemStack(Items.WHEAT, 16);
        ItemStack flesh = new ItemStack(Items.ROTTEN_FLESH, 4);
        ItemStack red = new ItemStack(Items.DYE, 8, EnumDyeColor.RED.getDyeDamage());
        ItemStack tree = new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata());
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(getIngredient(tree), getIngredient(wheat), getIngredient(red), getIngredient(flesh)),new ItemStack(ModBlocks.luretreeSapling));

        if (MILL_CLAY) {
            BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Blocks.HARDENED_CLAY, 1),new ItemStack(Items.BRICK, 4));

            EnumDyeColor[] dyes = EnumDyeColor.values();
            int len = dyes.length;

            for (int i = 0; i < len; ++i) {
                EnumDyeColor dye = dyes[i];
                ItemStack brick = new ItemStack(ModItems.stainedBrick, 1, dye.getMetadata());
                BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata()),new ItemStack(ModItems.stainedBrick, 4, dye.getMetadata()));
                //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.coloredBrick, 1, dye.getMetadata()), "bb", "bb", 'b', brick);
            }
        }

        if (!CHORUS_IN_CAULDRON)
            GameRegistry.addSmelting(ModItems.material.getMaterial("midori"), ModItems.material.getMaterial("midori_popped"), 0.1f);
    }

    private StackIngredient getIngredient(ItemStack stack) {
        return StackIngredient.fromStacks(stack);
    }

    @Override
    public void postInit() {
        //Fixes baked stuff showing up in the cauldron
        removeCauldronRecipe(new ItemStack(Items.BAKED_POTATO));
        removeCauldronRecipe(new ItemStack(ModItems.bakedCarrot));
        removeCauldronRecipe(new ItemStack(ModItems.bakedBeetroot));

        if (CHORUS_IN_CAULDRON)
            BetterWithAddons.removeSmeltingRecipe(new ItemStack(Items.CHORUS_FRUIT_POPPED));

        for(ItemStack stack : OreDictionary.getOres("listAllmeat")) {
            ItemStack groundMeat = new ItemStack(ModItems.groundMeat);
            ItemStack meatStack = stack.copy();
            meatStack.setCount(1);
            if(meatStack.getItem() instanceof ItemFood) {
                int amount = ((ItemFood) meatStack.getItem()).getHealAmount(meatStack) / ModItems.groundMeat.getHealAmount(groundMeat);
                groundMeat.setCount(Math.max(amount,1));
            }
            BWRegistry.MILLSTONE.addMillRecipe(meatStack,groundMeat);
        }
    }

    private static void removeCauldronRecipe(ItemStack outputs) {
        BWRegistry.CAULDRON.remove(Lists.newArrayList(outputs));
    }
}
