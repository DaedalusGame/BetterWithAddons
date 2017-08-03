package betterwithaddons.interaction;

import betterwithaddons.crafting.OreStack;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.manager.CraftingManagerSpindle;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;
import org.apache.http.config.Registry;

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
        ConditionModule.MODULES.put("DecoAddon", this::isActive);
    }

    @Override
    public void init() {
        if(!LOSE_BINDER) {
            ModItems.materialBag.setContainer(bagStack);
            ModItems.materialCrate.setContainer(crateStack);
            ModItems.materialCongealed.setContainer(congealedStack);
            ModItems.materialBolt.setContainer(boltStack);
            ModItems.materialBundle.setContainer(bundleStack);
        }

        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.loom)," g ","pip","ppp",'g',"gearWood", 'p', "plankWood", 'i', "nuggetIron"));
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.loom)," g ","pip","ppp",'g',"gearWood", 'p', "sidingWood", 'i', "nuggetIron"));
        //GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.spindle,3),"s","s","s",'s',new ItemStack(BWMBlocks.WOOD_MOULDING,1));

        CauldronManager.getInstance().addRecipe(new ItemStack(BWMBlocks.AESTHETIC,1,BlockAesthetic.EnumType.DUNG.getMeta()),new Object[]{new betterwithmods.common.registry.OreStack("dung",9)});

        CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH)},new OreStack("fiberHemp",9),false);
        CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{new ItemStack(BWMBlocks.AESTHETIC,1, BlockAesthetic.EnumType.ROPE.getMeta())},new ItemStack(BWMBlocks.ROPE,9),false);
    }

    @Override
    public void postInit() {

    }

    @Override
    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) event.getRegistry();

        bagStack = betterwithmods.common.items.ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH,1);
        crateStack = new ItemStack(Blocks.PLANKS,1);
        congealedStack = new ItemStack(Items.SLIME_BALL,1);
        boltStack = new ItemStack(BWMBlocks.WOOD_MOULDING,1);
        bundleStack = betterwithmods.common.items.ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS,1);

        addBaggingRecipe(registry,"seed",new ItemStack(Items.WHEAT_SEEDS));
        addBaggingRecipe(registry,"seed_hemp",new ItemStack(BWMBlocks.HEMP));
        addBaggingRecipe(registry,"seed_melon",new ItemStack(Items.MELON_SEEDS));
        addBaggingRecipe(registry,"seed_pumpkin",new ItemStack(Items.PUMPKIN_SEEDS));
        addBaggingRecipe(registry,"seed_beets",new ItemStack(Items.BEETROOT_SEEDS));
        addBaggingRecipe(registry,"cocoa",new ItemStack(Items.DYE,1,EnumDyeColor.BROWN.getDyeDamage()));
        addBaggingRecipe(registry,"redstone",new ItemStack(Items.REDSTONE));
        addBaggingRecipe(registry,"glowstone",new ItemStack(Items.GLOWSTONE_DUST));
        addBaggingRecipe(registry,"sugar",new ItemStack(Items.SUGAR));
        addBaggingRecipe(registry,"gunpowder",new ItemStack(Items.GUNPOWDER));
        addBaggingRecipe(registry,"flour",new ItemStack(BWMBlocks.RAW_PASTRY,1, BlockRawPastry.EnumType.BREAD.getMetadata()));
        addBaggingRecipe(registry,"sulfur",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BRIMSTONE));
        addBaggingRecipe(registry,"nitre",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NITER));
        addBaggingRecipe(registry,"sawdust",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));
        addBaggingRecipe(registry,"sawdust_soul",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST));
        addBaggingRecipe(registry,"potash",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH));
        addBaggingRecipe(registry,"hellfire",ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        addBaggingRecipe(registry,"kibble", new ItemStack(BWMItems.KIBBLE));

        addCratingRecipe(registry,"pork",new ItemStack(Items.COOKED_PORKCHOP));
        addCratingRecipe(registry,"pork_raw",new ItemStack(Items.PORKCHOP));
        addCratingRecipe(registry,"steak",new ItemStack(Items.COOKED_BEEF));
        addCratingRecipe(registry,"steak_raw",new ItemStack(Items.BEEF));
        addCratingRecipe(registry,"chicken",new ItemStack(Items.COOKED_CHICKEN));
        addCratingRecipe(registry,"chicken_raw",new ItemStack(Items.CHICKEN));
        addCratingRecipe(registry,"mutton",new ItemStack(Items.COOKED_MUTTON));
        addCratingRecipe(registry,"mutton_raw",new ItemStack(Items.MUTTON));
        addCratingRecipe(registry,"rabbit",new ItemStack(Items.COOKED_RABBIT));
        addCratingRecipe(registry,"rabbit_raw",new ItemStack(Items.RABBIT));
        addCratingRecipe(registry,"egg",new ItemStack(Items.EGG));
        addCratingRecipe(registry,"slime",new ItemStack(Items.SLIME_BALL));
        addCratingRecipe(registry,"enderpearl",new ItemStack(Items.ENDER_PEARL));

        addCongealingRecipe(registry,"mushroom",new ItemStack(Blocks.BROWN_MUSHROOM));
        addCongealingRecipe(registry,"amanita",new ItemStack(Blocks.RED_MUSHROOM));
        addCongealingRecipe(registry,"bone",new ItemStack(Items.BONE));
        addCongealingRecipe(registry,"flesh",new ItemStack(Items.ROTTEN_FLESH));
        addCongealingRecipe(registry,"eye",new ItemStack(Items.SPIDER_EYE));
        addCongealingRecipe(registry,"wart",new ItemStack(Items.NETHER_WART));

        addRollupRecipe(registry,"fabric",new OreStack("fabricHemp",8));
        addRollupRecipe(registry,"vine",new ItemStack(Blocks.VINE));
        addRollupRecipe(registry,"paper",new OreStack("paper",8));
        addRollupRecipe(registry,"leather",new OreStack("leather",8));
        addRollupRecipe(registry,"scoured_leather",new OreStack("hideScoured",8));
        addRollupRecipe(registry,"tanned_leather",new OreStack("hideTanned",8));
        addRollupRecipe(registry,"string",new OreStack("string",8));

        addBundlingRecipe(registry,"feather",new ItemStack(Items.FEATHER));
        addBundlingRecipe(registry,"blazerods",new ItemStack(Items.BLAZE_ROD));
        addBundlingRecipe(registry,"arrows",new ItemStack(Items.ARROW));
        addBundlingRecipe(registry,"oak",new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.OAK.getMetadata()));
        addBundlingRecipe(registry,"birch",new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        addBundlingRecipe(registry,"spruce",new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        addBundlingRecipe(registry,"jungle",new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        addBundlingRecipe(registry,"acacia",new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.ACACIA.getMetadata()));
        addBundlingRecipe(registry,"darkoak",new ItemStack(Blocks.SAPLING,1, BlockPlanks.EnumType.DARK_OAK.getMetadata()));

    }

    private void addBaggingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material)
    {
        ItemStack output = ModItems.materialBag.getMaterial(id);

        addCondensingRecipe(registry,id,output, material, bagStack);
    }

    private void addCratingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material)
    {
        ItemStack output = ModItems.materialCrate.getMaterial(id);

        addCondensingRecipe(registry,id,output, material, crateStack);
    }

    private void addCongealingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material)
    {
        ItemStack output = ModItems.materialCongealed.getMaterial(id);

        addCondensingRecipe(registry,id,output, material, congealedStack);

        ItemStack material8 = material.copy();
        material8.setCount(8);
        CauldronManager.getInstance().addRecipe(output,new Object[]{material8,congealedStack.copy()});
    }

    private void addRollupRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material)
    {
        ItemStack output = ModItems.materialBolt.getMaterial(id);

        addCondensingRecipe(registry,id,output, material, boltStack);

        ItemStack material8 = material.copy();
        material8.setCount(8);
        CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{output},material8,true);
    }

    private void addRollupRecipe(ForgeRegistry<IRecipe> registry, String id, OreStack material)
    {
        ItemStack output = ModItems.materialBolt.getMaterial(id);

        addCondensingRecipe(registry,id,output, material, boltStack);
        OreStack material8 = material.copy();
        material8.setCount(8);
        CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{output},material8,true);
    }

    private void addBundlingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material)
    {
        ItemStack output = ModItems.materialBundle.getMaterial(id);

        addCondensingRecipe(registry,id,output, material, bundleStack);
    }

    private void addCondensingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack output, ItemStack material, ItemStack frame)
    {
        ItemStack outmaterial = material.copy();
        outmaterial.setCount(8);
        ResourceLocation compressLoc = new ResourceLocation(Reference.MOD_ID,"compress_"+id);
        ResourceLocation uncompressLoc = new ResourceLocation(Reference.MOD_ID,"uncompress_"+id);

        registry.register(new ShapedOreRecipe(compressLoc,output,"aaa","aba","aaa",'a',material,'b',frame).setRegistryName(compressLoc));
        registry.register(new ShapelessOreRecipe(uncompressLoc,outmaterial,output).setRegistryName(uncompressLoc));
    }


    private void addCondensingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack output, OreStack material, ItemStack frame)
    {
        ItemStack outmaterial = ItemStack.EMPTY;
        List<ItemStack> orestacks = material.getOres();
        if(!orestacks.isEmpty())
        {
            outmaterial = orestacks.get(0).copy();
            outmaterial.setCount(8);
        }
        ResourceLocation compressLoc = new ResourceLocation(Reference.MOD_ID,"compress_"+id);
        ResourceLocation uncompressLoc = new ResourceLocation(Reference.MOD_ID,"uncompress_"+id);

        registry.register(new ShapedOreRecipe(compressLoc,output,"aaa","aba","aaa",'a',material.getOreName(),'b',frame).setRegistryName(compressLoc));
        registry.register(new ShapelessOreRecipe(uncompressLoc,outmaterial, output).setRegistryName(uncompressLoc));
    }
}
