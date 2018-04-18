package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.recipes.DoorSawRecipe;
import betterwithaddons.handler.BurnHandler;
import betterwithaddons.handler.EggIncubationHandler;
import betterwithaddons.handler.SoapHandler;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.mechanical.BlockMechMachines;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.module.gameplay.CrucibleRecipes;
import betterwithmods.module.gameplay.miniblocks.MiniBlocks;
import betterwithmods.module.hardcore.world.HCBonemeal;
import betterwithmods.module.tweaks.MineshaftGeneration;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Arrays;
import java.util.List;

public class InteractionBTWTweak extends Interaction {
    public static boolean ENABLED = true;
    public static boolean SAW_RECYCLING = true;
    //public static boolean KILN_DOUBLING = true;
    public static boolean EGG_INCUBATION = true;
    public static boolean SLIPPERY_WHEN_WET = true;
    public static boolean ASH_FERTILIZER = true;
    public static boolean WOOL_RECYCLING = true;
    public static boolean LOGS_SMELT_TO_ASH = true;
    public static boolean LOGS_BURN_TO_ASH = true;
    public static boolean REPLACE_WRITABLE_BOOK_RECIPE = true;
    public static boolean RUSTY_MINESHAFTS = true;
    public static boolean INFESTED_MINESHAFTS = true;
    public static int WRITING_TABLE_COST = 1;
    public static int EGG_INCUBATION_TIME = 5400;

    @Override
    protected String getName() {
        return "addons.BTWTweak";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled","Whether the BTWTweak module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.",ENABLED);
        SAW_RECYCLING = loadPropBool("SawRecycling","Many wooden blocks can be recycled by putting them infront of a saw, at a bit of a loss.",SAW_RECYCLING);
        EGG_INCUBATION = loadPropBool("EggIncubation","Allows eggs to be incubated into chicken by placing them on a Block of Padding with a lit Light Block above.",EGG_INCUBATION);
        SLIPPERY_WHEN_WET = loadPropBool("SlipperyWhenWet","Water running over blocks of soap will make them slippery.",SLIPPERY_WHEN_WET);
        ASH_FERTILIZER = loadPropBool("AshFertilizer","Potash is a valid fertilizer.",ASH_FERTILIZER);
        WOOL_RECYCLING = loadPropBool("WoolRecycling","Wool can be rendered back into it's components. You might want to disable this if you use mods that violate Hardcore Shearing.",WOOL_RECYCLING);
        LOGS_SMELT_TO_ASH = loadPropBool("LogsSmeltToAsh","Logs burn into ash in a furnace. This only works if they wouldn't burn into anything else.",LOGS_SMELT_TO_ASH);
        LOGS_BURN_TO_ASH = loadPropBool("LogsBurnToAsh","Logs burn into ash in world.",LOGS_BURN_TO_ASH);
        REPLACE_WRITABLE_BOOK_RECIPE = loadPropBool("ReplaceWritableBookRecipe","Changes writable books to require the Ink and Quill item.",REPLACE_WRITABLE_BOOK_RECIPE);
        RUSTY_MINESHAFTS = loadPropBool("RustedMineshafts","Rails in Mineshafts are rusted and melt down into much less iron.",RUSTY_MINESHAFTS);
        INFESTED_MINESHAFTS = loadPropBool("InfestedMineshafts","Logs in Mineshafts are infested by Termites and crumble into sawdust when harvested.",INFESTED_MINESHAFTS);
        doesNotNeedRestart(() -> {
            WRITING_TABLE_COST = loadPropInt("WritingTableCost","How many levels it costs to rename an item or create a nametag.",WRITING_TABLE_COST);
            EGG_INCUBATION_TIME = loadPropInt("EggIncubationTime","How long it takes for an egg to hatch using incubation, in ticks.",EGG_INCUBATION_TIME);
        });
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
        if(SLIPPERY_WHEN_WET)
            MinecraftForge.EVENT_BUS.register(new SoapHandler(BWMBlocks.AESTHETIC.getDefaultState().withProperty(BlockAesthetic.TYPE, BlockAesthetic.EnumType.SOAP)));
        if(EGG_INCUBATION)
            MinecraftForge.EVENT_BUS.register(new EggIncubationHandler());
        if(REPLACE_WRITABLE_BOOK_RECIPE)
            BetterWithAddons.removeCraftingRecipe(new ResourceLocation("minecraft","writable_book"));
        if(LOGS_BURN_TO_ASH)
            MinecraftForge.EVENT_BUS.register(BurnHandler.class);

        ConditionModule.MODULES.put("ReplaceWritableBookRecipe",() -> REPLACE_WRITABLE_BOOK_RECIPE);
    }

    @Override
    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) event.getRegistry();

        //Temp conversion recipe
        ResourceLocation resloc = new ResourceLocation(Reference.MOD_ID, "ink_and_quill_conversion");
        registry.register(new ShapelessOreRecipe(resloc,new ItemStack(ModItems.inkAndQuill),new Object[]{ModItems.materialTweak.getMaterial("ink_and_quill")}).setRegistryName(resloc));
    }

    @Override
    public void init() {
        if(ASH_FERTILIZER) {
            HCBonemeal.registerFertilzier(ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH));
            HCBonemeal.registerFertilzier(ModItems.materialTweak.getMaterial("ash"));
        }

        ModItems.materialTweak.setDisabled("ink_and_quill"); //Deprecated

        if(RUSTY_MINESHAFTS)
            MineshaftGeneration.rail = piece -> ModBlocks.rustyRail.getDefaultState();
        if(INFESTED_MINESHAFTS)
            MineshaftGeneration.supports = piece -> ModBlocks.termiteLog.getDefaultState();
        BWRegistry.CRUCIBLE.addStokedRecipe(new ItemStack(ModBlocks.rustyRail,2),new ItemStack(Items.IRON_NUGGET));
        BWRegistry.WOOD_SAW.addRecipe(new ItemStack(ModBlocks.termiteLog,1,OreDictionary.WILDCARD_VALUE), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST,2));

        if(WOOL_RECYCLING && InteractionBWM.HARDCORE_SHEARING)
        {
            for (EnumDyeColor color: EnumDyeColor.values()) {
                BWRegistry.CAULDRON.addStokedRecipe(new ItemStack(Blocks.WOOL,1,color.getMetadata()), new ItemStack(BWMBlocks.AESTHETIC,1,BlockAesthetic.EnumType.WICKER.getMeta()), ModItems.wool.getByColor(color,4)).setPriority(-1);
            }
        }

        if(SAW_RECYCLING)
        {
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.BOOKSHELF), Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,4),new ItemStack(Items.BOOK,3)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.CHEST),getSiding(BlockPlanks.EnumType.OAK,6));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.JUKEBOX),Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,6),new ItemStack(Items.DIAMOND,1)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.LADDER),new ItemStack(Items.STICK,2));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.NOTEBLOCK),Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,6),new ItemStack(Items.REDSTONE,1)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.TRAPDOOR),getSiding(BlockPlanks.EnumType.OAK,2));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.WOODEN_AXLE),Lists.newArrayList(getCorner(BlockPlanks.EnumType.OAK,2),new ItemStack(BWMBlocks.ROPE,1)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.BELLOWS),Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,2), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER_CUT,3)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.WOODEN_GEARBOX),Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,3), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR,3), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH)));
            BWRegistry.WOOD_SAW.addRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.HOPPER),Lists.newArrayList(getMoulding(BlockPlanks.EnumType.OAK,3), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR,1), new ItemStack(Blocks.WOODEN_PRESSURE_PLATE,1)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.PLATFORM),Lists.newArrayList(getMoulding(BlockPlanks.EnumType.OAK,3), new ItemStack(BWMBlocks.WICKER,2)));
            BWRegistry.WOOD_SAW.addRecipe(BlockMechMachines.getStack(BlockMechMachines.EnumType.PULLEY),Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,3), new ItemStack(Items.IRON_INGOT), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.REDSTONE_LATCH)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.SAW),Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,1), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.LEATHER_BELT), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.GEAR), new ItemStack(Items.IRON_INGOT, 2)));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.PUMP),Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,3), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCREW), new ItemStack(BWMBlocks.GRATE, 1, BlockPlanks.EnumType.OAK.getMetadata())));

            BlockPlanks.EnumType[] woodtypes = BlockPlanks.EnumType.values();

            for (BlockPlanks.EnumType woodtype : woodtypes) {
                BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.WOOD_BENCH, 1, woodtype.getMetadata()), getCorner(woodtype,2));
                BWRegistry.WOOD_SAW.addRecipe(new ItemStack(BWMBlocks.WOOD_TABLE, 1, woodtype.getMetadata()), getCorner(woodtype,3));
            }

            BWRegistry.WOOD_SAW.addRecipe(new DoorSawRecipe(Blocks.OAK_DOOR,Lists.newArrayList(getSiding(BlockPlanks.EnumType.OAK,4)),new ItemStack(Items.OAK_DOOR)));
            BWRegistry.WOOD_SAW.addRecipe(new DoorSawRecipe(Blocks.BIRCH_DOOR,Lists.newArrayList(getSiding(BlockPlanks.EnumType.BIRCH,4)),new ItemStack(Items.BIRCH_DOOR)));
            BWRegistry.WOOD_SAW.addRecipe(new DoorSawRecipe(Blocks.SPRUCE_DOOR,Lists.newArrayList(getSiding(BlockPlanks.EnumType.SPRUCE,4)),new ItemStack(Items.SPRUCE_DOOR)));
            BWRegistry.WOOD_SAW.addRecipe(new DoorSawRecipe(Blocks.JUNGLE_DOOR,Lists.newArrayList(getSiding(BlockPlanks.EnumType.JUNGLE,4)),new ItemStack(Items.JUNGLE_DOOR)));
            BWRegistry.WOOD_SAW.addRecipe(new DoorSawRecipe(Blocks.ACACIA_DOOR,Lists.newArrayList(getSiding(BlockPlanks.EnumType.ACACIA,4)),new ItemStack(Items.ACACIA_DOOR)));
            BWRegistry.WOOD_SAW.addRecipe(new DoorSawRecipe(Blocks.DARK_OAK_DOOR,Lists.newArrayList(getSiding(BlockPlanks.EnumType.DARK_OAK,4)),new ItemStack(Items.DARK_OAK_DOOR)));

            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.OAK_FENCE_GATE),getMoulding(BlockPlanks.EnumType.OAK,3));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.BIRCH_FENCE_GATE),getMoulding(BlockPlanks.EnumType.BIRCH,3));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.SPRUCE_FENCE_GATE),getMoulding(BlockPlanks.EnumType.SPRUCE,3));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.JUNGLE_FENCE_GATE),getMoulding(BlockPlanks.EnumType.JUNGLE,3));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.ACACIA_FENCE_GATE),getMoulding(BlockPlanks.EnumType.ACACIA,3));
            BWRegistry.WOOD_SAW.addRecipe(new ItemStack(Blocks.DARK_OAK_FENCE_GATE),getMoulding(BlockPlanks.EnumType.DARK_OAK,3));
        }
    }

    private ItemStack getSiding(BlockPlanks.EnumType type, int count) {
        return MiniBlocks.fromParent(MiniBlocks.SIDINGS.get(Material.WOOD),Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT,type),count);
    }

    private ItemStack getMoulding(BlockPlanks.EnumType type, int count) {
        return MiniBlocks.fromParent(MiniBlocks.MOULDINGS.get(Material.WOOD),Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT,type),count);
    }

    private ItemStack getCorner(BlockPlanks.EnumType type, int count) {
        return MiniBlocks.fromParent(MiniBlocks.CORNERS.get(Material.WOOD),Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT,type),count);
    }

    @Override
    public void postInit() {
        if(LOGS_SMELT_TO_ASH) {
            for (ItemStack log : OreDictionary.getOres("logWood")) {
                ItemStack result = FurnaceRecipes.instance().getSmeltingResult(log);
                if(result.isEmpty())
                    GameRegistry.addSmelting(log,ModItems.materialTweak.getMaterial("ash"),0.1f);
            }
        }

        /*if(KILN_DOUBLING && ModuleLoader.isFeatureEnabled(KilnSmelting.class))
        {
            for (ItemStack ore : BWOreDictionary.oreNames) {
                if(ore.getItem() instanceof ItemBlock)
                {
                    BlockMetaRecipe recipe = KilnManager.INSTANCE.getRecipe(ore);
                    List<ItemStack> outputs = recipe.getOutputs();
                    if(outputs.size() > 0)
                    {
                        ItemStack output = outputs.get(0).copy();
                        output.setCount(MathHelper.clamp(output.getCount() * 2,0,output.getMaxStackSize()));
                        outputs.set(0,output);
                    }
                }
            }
        }*/
    }
}
