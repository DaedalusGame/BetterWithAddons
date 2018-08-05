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
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IngredientSized;
import betterwithaddons.util.ItemUtil;
import betterwithmods.BWMod;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockBDispenser;
import betterwithmods.common.blocks.BlockBUD;
import betterwithmods.common.blocks.BlockUrn;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.block.recipe.IngredientSpecial;
import betterwithmods.common.registry.crafting.RecipeShapedColor;
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
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;

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
    public static int WOOL_BATCH = 8;
    public static int GLASS_BATCH = 1;
    public static int TERRACOTTA_BATCH = 4;
    public static int FLEECE_BATCH = 32;
    public static boolean DYE_WOOL_IN_CAULDRON = true;
    public static boolean DYE_FLEECE_IN_CAULDRON = true;
    public static boolean DYE_GLASS_IN_CAULDRON = true;
    public static boolean DYE_TERRACOTTA_IN_CAULDRON = true;
    public static boolean REMOVE_REGULAR_DYE_RECIPES = true;
    public static boolean HIDDEN_ENCHANTS = false;
    public static boolean CHEAP_WOOL_ARMOR = true;

    public static String[] SHEARS_WHITELIST = new String[]{
            "tconstruct:kama",
            "tconstruct:scythe" //This is normally AoE and wlll lose the property
    };

    public static String[] WOOL_DYING_RECIPES = new String[]{
            "minecraft:black_wool",
            "minecraft:blue_wool",
            "minecraft:white_wool",
            "minecraft:cyan_wool",
            "minecraft:gray_wool",
            "minecraft:green_wool",
            "minecraft:light_blue_wool",
            "minecraft:light_gray_wool",
            "minecraft:lime_wool",
            "minecraft:magenta_wool",
            "minecraft:orange_wool",
            "minecraft:pink_wool",
            "minecraft:purple_wool",
            "minecraft:red_wool",
            "minecraft:yellow_wool",
            "minecraft:brown_wool",
    };
    public static String[] GLASS_DYING_RECIPES = new String[]{
            "minecraft:black_stained_glass",
            "minecraft:blue_stained_glass",
            "minecraft:white_stained_glass",
            "minecraft:cyan_stained_glass",
            "minecraft:gray_stained_glass",
            "minecraft:green_stained_glass",
            "minecraft:light_blue_stained_glass",
            "minecraft:light_gray_stained_glass",
            "minecraft:lime_stained_glass",
            "minecraft:magenta_stained_glass",
            "minecraft:orange_stained_glass",
            "minecraft:pink_stained_glass",
            "minecraft:purple_stained_glass",
            "minecraft:red_stained_glass",
            "minecraft:yellow_stained_glass",
            "minecraft:brown_stained_glass",
    };
    public static String[] TERRACOTTA_DYING_RECIPES = new String[]{
            "minecraft:black_stained_hardened_clay",
            "minecraft:blue_stained_hardened_clay",
            "minecraft:white_stained_hardened_clay",
            "minecraft:cyan_stained_hardened_clay",
            "minecraft:gray_stained_hardened_clay",
            "minecraft:green_stained_hardened_clay",
            "minecraft:light_blue_stained_hardened_clay",
            "minecraft:light_gray_stained_hardened_clay",
            "minecraft:lime_stained_hardened_clay",
            "minecraft:magenta_stained_hardened_clay",
            "minecraft:orange_stained_hardened_clay",
            "minecraft:pink_stained_hardened_clay",
            "minecraft:purple_stained_hardened_clay",
            "minecraft:red_stained_hardened_clay",
            "minecraft:yellow_stained_hardened_clay",
            "minecraft:brown_stained_hardened_clay",
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
        BUTCHER_BLOCKS = loadPropBool("ButcherBlocks", "Striking an enemy on chopping blocks will bloody them and bestow a short strength buff.", BUTCHER_BLOCKS);
        HARDCORE_SHEARING = loadPropBool("HardcoreShearing", "Sheep will only be sheared into wool items, which must be crafted into wool blocks. This does not work with machines that use onSheared, but should work with Fake Players.", HARDCORE_SHEARING);
        HARDCORE_PACKING = loadPropBool("HardcorePacking", "Items can be compressed in world with a piston pushing them into an enclosed space.", HARDCORE_PACKING);
        WOOL_MULTIPLIER = loadPropInt("WoolMultiplier", "Adjusts how much wool a sheep drops if Hardcore Shearing is enabled.", WOOL_MULTIPLIER);
        DYE_WOOL_IN_CAULDRON = loadPropBool("DyeInCauldron", "Wool can be dyed in batches of 8 in a cauldron and bleached with potash.", DYE_WOOL_IN_CAULDRON); //TODO: 1.13 - Change name to DyeWoolInCauldron
        WOOL_BATCH = loadPropInt("DyeWoolBatchSize", "How much wool can be dyed at once in a cauldron.", WOOL_BATCH);
        DYE_FLEECE_IN_CAULDRON = loadPropBool("DyeFleeceInCauldron", "Fleece can be dyed in batches of 32 in a cauldron and bleached with potash.", DYE_FLEECE_IN_CAULDRON);
        FLEECE_BATCH = loadPropInt("DyeFleeceBatchSize", "How much fleece can be dyed at once in a cauldron.", FLEECE_BATCH);
        DYE_GLASS_IN_CAULDRON = loadPropBool("DyeGlassInCauldron", "Glass can be dyed in a stoked cauldron.", DYE_GLASS_IN_CAULDRON);
        GLASS_BATCH = loadPropInt("DyeGlassBatchSize", "How much glass can be dyed at once in a stoked cauldron.", GLASS_BATCH);
        DYE_TERRACOTTA_IN_CAULDRON = loadPropBool("DyeTerracottaInCauldron", "Terracotta can be dyed in batches of 4 in a stoked cauldron.", DYE_TERRACOTTA_IN_CAULDRON);
        TERRACOTTA_BATCH = loadPropInt("DyeTerracottaBatchSize", "How much terracotta can be dyed at once in a stoked cauldron.", TERRACOTTA_BATCH);
        REMOVE_REGULAR_DYE_RECIPES = loadPropBool("RemoveDyeRecipes", "Removes dye recipes from the crafting table if the relevant cauldron dying recipes exist.", REMOVE_REGULAR_DYE_RECIPES);
        SHEARS_WHITELIST = loadPropStringList("ShearWhitelist", "Extra items that are functionally shears but don't extend ItemShears.", SHEARS_WHITELIST);
        CHEAP_WOOL_ARMOR = loadPropBool("CheapWoolArmor", "Wool Armor is made from Fleece.", CHEAP_WOOL_ARMOR);
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
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation("minecraft", "string_to_wool"));
        }
        if (HARDCORE_PACKING)
            MinecraftForge.EVENT_BUS.register(new HardcorePackingHandler());
        if (REMOVE_REGULAR_DYE_RECIPES) {
            if (DYE_WOOL_IN_CAULDRON)
                Arrays.stream(WOOL_DYING_RECIPES).forEach(resloc -> BetterWithAddons.removeCraftingRecipe(new ResourceLocation(resloc)));
            if (DYE_GLASS_IN_CAULDRON)
                Arrays.stream(GLASS_DYING_RECIPES).forEach(resloc -> BetterWithAddons.removeCraftingRecipe(new ResourceLocation(resloc)));
            if (DYE_TERRACOTTA_IN_CAULDRON)
                Arrays.stream(TERRACOTTA_DYING_RECIPES).forEach(resloc -> BetterWithAddons.removeCraftingRecipe(new ResourceLocation(resloc)));
        }
        if(CHEAP_WOOL_ARMOR) {
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation(BWMod.MODID,"wool_boots"));
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation(BWMod.MODID,"wool_chest"));
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation(BWMod.MODID,"wool_helmet"));
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation(BWMod.MODID,"wool_pants"));
        }

        ConditionModule.MODULES.put("HardcoreDiamond", () -> ModuleLoader.isFeatureEnabled(HCDiamond.class));
        ConditionModule.MODULES.put("HardcoreShearing", () -> HARDCORE_SHEARING);
        ConditionModule.MODULES.put("HardcoreHunger", () -> ModuleLoader.isFeatureEnabled(HCCooking.class));

        HardcoreWoolHandler.EXTRA_SHEARS = new HashSet<>(Arrays.asList(SHEARS_WHITELIST)); //Populate the set of extra shears.
    }

    @Override
    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        registry.register(new RecipeShapedColor(new ResourceLocation(Reference.MOD_ID,"wool_boots"),BWMItems.WOOL_BOOTS,"C C","C C",'C',new ItemStack(ModItems.WOOL,1,OreDictionary.WILDCARD_VALUE)).setRegistryName(new ResourceLocation(Reference.MOD_ID,"wool_boots")));
        registry.register(new RecipeShapedColor(new ResourceLocation(Reference.MOD_ID,"wool_pants"),BWMItems.WOOL_PANTS,"CCC","C C","C C",'C',new ItemStack(ModItems.WOOL,1,OreDictionary.WILDCARD_VALUE)).setRegistryName(new ResourceLocation(Reference.MOD_ID,"wool_pants")));
        registry.register(new RecipeShapedColor(new ResourceLocation(Reference.MOD_ID,"wool_chest"),BWMItems.WOOL_CHEST,"C C","CCC","CCC",'C',new ItemStack(ModItems.WOOL,1,OreDictionary.WILDCARD_VALUE)).setRegistryName(new ResourceLocation(Reference.MOD_ID,"wool_chest")));
        registry.register(new RecipeShapedColor(new ResourceLocation(Reference.MOD_ID,"wool_helmet"),BWMItems.WOOL_HELMET,"CCC","C C",'C',new ItemStack(ModItems.WOOL,1,OreDictionary.WILDCARD_VALUE)).setRegistryName(new ResourceLocation(Reference.MOD_ID,"wool_helmet")));
    }

    public static NonNullList<ItemStack> convertShearedWool(List<ItemStack> sheared) {
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

    public static ItemStack convertOneWool(ItemStack stack) {
        if (stack.getItem() == Item.getItemFromBlock(Blocks.WOOL))
            return new ItemStack(ModItems.WOOL, stack.getCount() * WOOL_MULTIPLIER, stack.getMetadata());
        else if (ItemUtil.matchesOreDict(stack, "blockWool"))
            return new ItemStack(ModItems.WOOL, stack.getCount() * WOOL_MULTIPLIER, 0);
        else
            return stack;
    }

    @Override
    public void init() {
        if (!isActive())
            return;

        String[] dyeOredictTags = new String[]{"White", "Orange", "Magenta", "LightBlue", "Yellow", "Lime", "Pink", "Gray", "LightGray", "Cyan", "Purple", "Blue", "Brown", "Green", "Red", "Black"};

        if (HARDCORE_SHEARING) {
            BlockBDispenser.ENTITY_COLLECT_REGISTRY.putObject(new ResourceLocation("sheep"), (world, pos, entity, itemStack) -> {
                EntitySheep sheep = (EntitySheep) entity;
                if (sheep.isShearable(new ItemStack(Items.SHEARS), world, pos)) {
                    return convertShearedWool(sheep.onSheared(new ItemStack(Items.SHEARS), world, pos, 0));
                }
                return NonNullList.create();
            });
        }

        ItemStack whiteWool = new ItemStack(Blocks.WOOL, WOOL_BATCH, EnumDyeColor.WHITE.getMetadata());
        ItemStack whiteFleece = new ItemStack(ModItems.WOOL, FLEECE_BATCH, EnumDyeColor.WHITE.getMetadata());

        //Dyeing
        for (EnumDyeColor color : EnumDyeColor.values()) {
            OreIngredient dye = new OreIngredient("dye" + dyeOredictTags[color.ordinal()]);
            if (color != EnumDyeColor.WHITE) {
                if (DYE_WOOL_IN_CAULDRON)
                    BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(whiteWool), dye), new ItemStack(Blocks.WOOL, WOOL_BATCH, color.getMetadata()));
                if (DYE_FLEECE_IN_CAULDRON)
                    BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(whiteFleece), dye), new ItemStack(ModItems.WOOL, FLEECE_BATCH, color.getMetadata()));
            }
        }

        StackIngredient anyColoredWool = StackIngredient.fromIngredient(WOOL_BATCH, Ingredient.fromStacks(Arrays.stream(EnumDyeColor.values()).filter(color -> color != EnumDyeColor.WHITE).map(color -> new ItemStack(Blocks.WOOL, 1, color.getMetadata())).collect(Collectors.toList()).toArray(new ItemStack[15])));
        StackIngredient anyColoredFleece = StackIngredient.fromIngredient(FLEECE_BATCH, Ingredient.fromStacks(Arrays.stream(EnumDyeColor.values()).filter(color -> color != EnumDyeColor.WHITE).map(color -> new ItemStack(ModItems.WOOL, 1, color.getMetadata())).collect(Collectors.toList()).toArray(new ItemStack[15])));

        //Bleaching
        Ingredient potash = Ingredient.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH));
        if (DYE_WOOL_IN_CAULDRON)
            BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(anyColoredWool, potash), whiteWool);
        if (DYE_FLEECE_IN_CAULDRON)
            BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(anyColoredFleece, potash), whiteFleece);

        int glassPaneBatch = GLASS_BATCH * (InteractionDecoAddon.GLASS_PANE_REBALANCE ? 2 : 8);

        if (DYE_GLASS_IN_CAULDRON)
            for (EnumDyeColor color : EnumDyeColor.values()) {
                OreIngredient dye = new OreIngredient("dye" + dyeOredictTags[color.ordinal()]);
                BWRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(Blocks.GLASS, GLASS_BATCH)), dye), Lists.newArrayList(new ItemStack(Blocks.STAINED_GLASS, GLASS_BATCH, color.getMetadata())));
                BWRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(Blocks.GLASS_PANE, glassPaneBatch)), dye), Lists.newArrayList(new ItemStack(Blocks.STAINED_GLASS_PANE, glassPaneBatch, color.getMetadata())));
            }

        if (DYE_TERRACOTTA_IN_CAULDRON)
            for (EnumDyeColor color : EnumDyeColor.values()) {
                OreIngredient dye = new OreIngredient("dye" + dyeOredictTags[color.ordinal()]);
                BWRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(Blocks.HARDENED_CLAY, TERRACOTTA_BATCH)), dye), Lists.newArrayList(new ItemStack(Blocks.STAINED_HARDENED_CLAY, TERRACOTTA_BATCH, color.getMetadata())));
            }

        ModBlocks.ZEN_SAND.setBaseState(Blocks.SAND.getDefaultState());
        ModBlocks.ZEN_RED_SAND.setBaseState(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND));
        ModBlocks.ZEN_SOUL_SAND.setBaseState(Blocks.SOUL_SAND.getDefaultState());
        ModBlocks.ZEN_IRON_SAND.setBaseState(ModBlocks.IRON_SAND.getDefaultState());

        HCPiles.registerPile(ModBlocks.ZEN_SAND, new ItemStack(BWMItems.SAND_PILE, 3));
        HCPiles.registerPile(ModBlocks.ZEN_RED_SAND, new ItemStack(BWMItems.RED_SAND_PILE, 3));
        HCPiles.registerPile(ModBlocks.ZEN_SOUL_SAND, new ItemStack(ModItems.SOUL_SAND_PILE, 3));

        //Temporary until we PR soulsand piles
        HCPiles.registerPile(Blocks.SOUL_SAND, new ItemStack(ModItems.SOUL_SAND_PILE, 3));

        OreDictionary.getOres("cookedCarrot").clear();
        OreDictionary.registerOre("cookedCarrot", new ItemStack(ModItems.COOKED_CARROT));
        OreDictionary.registerOre("cookedCarrot", new ItemStack(ModItems.BAKED_CARROT));
        OreDictionary.registerOre("cookedPotato", new ItemStack(ModItems.COOKED_POTATO));

        OreDictionary.registerOre("listAllExplosives", new ItemStack(Blocks.TNT));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(Items.GUNPOWDER));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMItems.DYNAMITE));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMBlocks.MINING_CHARGE));
        OreDictionary.registerOre("listAllExplosives", new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.HELLFIRE.getMeta()));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BLASTING_OIL));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        OreDictionary.registerOre("listAllExplosives", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE));
        OreDictionary.registerOre("listAllExplosives", ModItems.MATERIAL_BAG.getMaterial("gunpowder"));
        OreDictionary.registerOre("listAllExplosives", ModItems.MATERIAL_BAG.getMaterial("hellfire"));

        OreDictionary.registerOre("listAllmeat", Items.RABBIT);
        OreDictionary.registerOre("listAllmeatcooked", ModItems.COOKED_CLOWNFISH);
        OreDictionary.registerOre("blockDung", BlockAesthetic.getStack(BlockAesthetic.EnumType.DUNG));

        OreDictionary.registerOre("book", Items.WRITTEN_BOOK);
        OreDictionary.registerOre("book", Items.BOOK);

        //Hardcore Packing
        if (HARDCORE_PACKING) {
            CraftingManagerPacking.getInstance().addRecipe(Blocks.DIRT.getDefaultState(), new ItemStack(Blocks.DIRT), IngredientSized.fromItem(BWMItems.DIRT_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SAND.getDefaultState(), new ItemStack(Blocks.SAND), IngredientSized.fromItem(BWMItems.SAND_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), new ItemStack(Blocks.SAND, 1, 1), IngredientSized.fromItem(BWMItems.RED_SAND_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.GRAVEL.getDefaultState(), new ItemStack(Blocks.GRAVEL), IngredientSized.fromItem(BWMItems.GRAVEL_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SOUL_SAND.getDefaultState(), new ItemStack(Blocks.SOUL_SAND), IngredientSized.fromItem(ModItems.SOUL_SAND_PILE, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.CLAY.getDefaultState(), new ItemStack(Blocks.CLAY), IngredientSized.fromItem(Items.CLAY_BALL, 4));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.NETHERCLAY), BlockAesthetic.getStack(BlockAesthetic.EnumType.NETHERCLAY), IngredientSized.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHER_SLUDGE, 4)));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SNOW.getDefaultState(), new ItemStack(Blocks.SNOW), IngredientSized.fromItem(Items.SNOWBALL, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.BRICK_BLOCK.getDefaultState(), new ItemStack(Blocks.BRICK_BLOCK), IngredientSized.fromItem(Items.BRICK, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.NETHER_BRICK.getDefaultState(), new ItemStack(Blocks.NETHER_BRICK), IngredientSized.fromItem(Items.NETHERBRICK, 4));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.FLINT), BlockAesthetic.getStack(BlockAesthetic.EnumType.FLINT), IngredientSized.fromItem(Items.FLINT, 9));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.DUNG), BlockAesthetic.getStack(BlockAesthetic.EnumType.DUNG), IngredientSized.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG, 9)));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.SOAP), BlockAesthetic.getStack(BlockAesthetic.EnumType.SOAP), IngredientSized.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOAP, 9)));
            CraftingManagerPacking.getInstance().addRecipe(BlockAesthetic.getVariant(BlockAesthetic.EnumType.HELLFIRE), BlockAesthetic.getStack(BlockAesthetic.EnumType.HELLFIRE), IngredientSized.fromStacks(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.CONCENTRATED_HELLFIRE, 9)));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.NETHER_WART_BLOCK.getDefaultState(), new ItemStack(Blocks.NETHER_WART_BLOCK), IngredientSized.fromItem(Items.NETHER_WART, 9));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.SLIME_BLOCK.getDefaultState(), new ItemStack(Blocks.SLIME_BLOCK), IngredientSized.fromItem(Items.SLIME_BALL, 9));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.MAGMA.getDefaultState(), new ItemStack(Blocks.MAGMA), IngredientSized.fromItem(Items.MAGMA_CREAM, 4));
            CraftingManagerPacking.getInstance().addRecipe(Blocks.PACKED_ICE.getDefaultState(), new ItemStack(Blocks.PACKED_ICE), IngredientSized.fromStacks(new ItemStack(Blocks.ICE, 4)));
        }
        //BlockBUD.addBlacklistBlock(ModBlocks.PCB_WIRE);

        ItemStack arrowhead = ModItems.MATERIAL.getMaterial("arrowhead");
        ItemStack haft = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT);
        ItemStack string = new ItemStack(BWMBlocks.ROPE);
        ItemStack feather = new ItemStack(Items.FEATHER);
        String oreIronIngot = "ingotIron";
        BWRegistry.CAULDRON.addStokedRecipe(Lists.newArrayList(Ingredient.fromItem(Items.FERMENTED_SPIDER_EYE), Ingredient.fromStacks(new ItemStack(Blocks.STONEBRICK)), new OreIngredient("dustPotash")), Lists.newArrayList(new ItemStack(ModBlocks.PCB_BLOCK)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.BEETROOT), new ItemStack(ModItems.COOKED_BEETROOT));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.CARROT), new ItemStack(ModItems.COOKED_CARROT));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.POTATO), new ItemStack(ModItems.COOKED_POTATO));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(Items.EGG), new ItemStack(ModItems.COOKED_EGG));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata())), new ItemStack(ModItems.COOKED_CLOWNFISH));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromStacks(new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata())), new ItemStack(ModItems.COOKED_PUFFER));

        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(StackIngredient.fromStacks(new ItemStack(Items.BONE, 2)), getIngredient(new ItemStack(Items.DYE, 8, 15))), ModItems.MATERIAL.getMaterial("bone_ingot"));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromStacks(ModItems.MATERIAL.getMaterial("midori")), ModItems.MATERIAL.getMaterial("midori_popped"));
        BWRegistry.CAULDRON.addUnstokedRecipe(Ingredient.fromItem(ModItems.GROUND_MEAT), new ItemStack(ModItems.MEATBALLS, 1));
        BWRegistry.MILLSTONE.addMillRecipe(Ingredient.fromStacks(new ItemStack(ModBlocks.WORLD_SCALE_ORE, 1, 1)), new ItemStack(ModBlocks.WORLD_SCALE, 1));

        //Bark
        ModBlocks.MULBERRY_LOG.barkStack = ModItems.MATERIAL_JAPAN.getMaterial("bark_mulberry");
        ModBlocks.SAKURA_LOG.barkStack = ModItems.MATERIAL_JAPAN.getMaterial("bark_sakura");

        //Thorn Vines
        ItemStack rosebush = new ItemStack(Blocks.DOUBLE_PLANT, 4, BlockDoublePlant.EnumPlantType.ROSE.getMeta());
        ItemStack thornrose = ModItems.MATERIAL.getMaterial("thornrose", 2);
        ItemStack soulUrn = new ItemStack(BWMBlocks.URN, 1, BlockUrn.EnumType.FULL.getMeta());
        ItemStack cactus = new ItemStack(Items.DYE, 1, EnumDyeColor.GREEN.getDyeDamage());
        ItemStack dung = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG, 1);
        ItemStack midori = ModItems.MATERIAL.getMaterial("midori", 8);
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(getIngredient(cactus), getIngredient(rosebush), getIngredient(dung), getIngredient(soulUrn)), Lists.newArrayList(new ItemStack(ModBlocks.THORN_ROSE)));
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(getIngredient(midori), getIngredient(thornrose), getIngredient(dung), getIngredient(soulUrn)), Lists.newArrayList(new ItemStack(ModBlocks.THORN_ROSE)));
        BWRegistry.MILLSTONE.addMillRecipe(ModItems.MATERIAL.getMaterial("midori", 1), cactus);

        //Alicio Sapling
        ItemStack wheat = new ItemStack(Items.WHEAT, 16);
        ItemStack flesh = new ItemStack(Items.ROTTEN_FLESH, 4);
        ItemStack red = new ItemStack(Items.DYE, 8, EnumDyeColor.RED.getDyeDamage());
        ItemStack tree = new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata());
        BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(getIngredient(tree), getIngredient(wheat), getIngredient(red), getIngredient(flesh)), new ItemStack(ModBlocks.LURETREE_SAPLING));

        if (MILL_CLAY) {
            BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Blocks.HARDENED_CLAY, 1), new ItemStack(Items.BRICK, 4));

            EnumDyeColor[] dyes = EnumDyeColor.values();
            int len = dyes.length;

            for (int i = 0; i < len; ++i) {
                EnumDyeColor dye = dyes[i];
                ItemStack brick = new ItemStack(ModItems.STAINED_BRICK, 1, dye.getMetadata());
                BWRegistry.MILLSTONE.addMillRecipe(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, dye.getMetadata()), new ItemStack(ModItems.STAINED_BRICK, 4, dye.getMetadata()));
                //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.coloredBrick, 1, dye.getMetadata()), "bb", "bb", 'b', brick);
            }
        }

        if (!CHORUS_IN_CAULDRON)
            GameRegistry.addSmelting(ModItems.MATERIAL.getMaterial("midori"), ModItems.MATERIAL.getMaterial("midori_popped"), 0.1f);
    }

    private StackIngredient getIngredient(ItemStack stack) {
        return StackIngredient.fromStacks(stack);
    }

    @Override
    public void postInit() {
        //Fixes baked stuff showing up in the cauldron
        removeCauldronRecipe(new ItemStack(Items.BAKED_POTATO));
        removeCauldronRecipe(new ItemStack(ModItems.BAKED_CARROT));
        removeCauldronRecipe(new ItemStack(ModItems.BAKED_BEETROOT));

        if (CHORUS_IN_CAULDRON)
            BetterWithAddons.removeSmeltingRecipe(new ItemStack(Items.CHORUS_FRUIT_POPPED));

        for (ItemStack stack : OreDictionary.getOres("listAllmeat")) {
            ItemStack groundMeat = new ItemStack(ModItems.GROUND_MEAT);
            ItemStack meatStack = stack.copy();
            meatStack.setCount(1);
            if (meatStack.getItem() instanceof ItemFood) {
                int amount = ((ItemFood) meatStack.getItem()).getHealAmount(meatStack) / ModItems.GROUND_MEAT.getHealAmount(groundMeat);
                groundMeat.setCount(Math.max(amount, 1));
            }
            BWRegistry.MILLSTONE.addMillRecipe(meatStack, groundMeat);
        }
    }

    private static void removeCauldronRecipe(ItemStack outputs) {
        BWRegistry.CAULDRON.remove(Lists.newArrayList(outputs));
    }
}
