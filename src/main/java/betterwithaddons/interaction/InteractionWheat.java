package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.recipes.ThreshingRecipe;
import betterwithaddons.handler.WheatHandler;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.registry.bulk.manager.MillManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.ForgeRegistry;

public class InteractionWheat extends Interaction {
    public static boolean ENABLED = false;
    public static boolean CHANGE_HAY_BALES = true;
    public static boolean REPLACE_WHEAT_DROPS = true;
    public static boolean THRESH_WHEAT = true;
    public static boolean THRESH_WHEAT_MILL = true;
    public static boolean MILL_GRAIN = true;
    public static boolean DIG_UP_CROPS = true;
    public static boolean TEXTURE_CHANGES = true;

    @Override
    protected String getName() {
        return "addons.BetterWithWheat";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled", "Whether the Better With Wheat module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.", ENABLED);
        MILL_GRAIN = loadPropBool("MillGrain", "Instead of milling wheat into flour, wheat seeds must be milled instead.", MILL_GRAIN);
        REPLACE_WHEAT_DROPS = loadPropBool("ReplaceWheatDrops", "Wheat, when harvested, will drop wheat and hay instead.", REPLACE_WHEAT_DROPS);
        CHANGE_HAY_BALES = loadPropBool("ChangeHayBales", "Haybales require hay instead of wheat.", CHANGE_HAY_BALES);
        THRESH_WHEAT = loadPropBool("ThreshWheat", "Wheat can be threshed into seeds using shovels.", THRESH_WHEAT);
        THRESH_WHEAT_MILL = loadPropBool("ThreshWheatMill", "Wheat can be threshed into seeds using a millstone.", THRESH_WHEAT_MILL);
        DIG_UP_CROPS = loadPropBool("DigUpCrops", "Carrots and Potatoes cannot be instantly harvested, and instead have a breaktime and a preferred tool (hoe)", DIG_UP_CROPS);
        TEXTURE_CHANGES = loadPropBool("ChangeTextures", "Changes textures of Wheat and Wheat Seeds.", TEXTURE_CHANGES);
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
    public void preInit() {
        if (TEXTURE_CHANGES) {
            Items.WHEAT_SEEDS.setUnlocalizedName("seed_grain"); //Keep compatible with Botania's stupid way of checking for seeds >_>
            ModItems.materialBag.subItemUnlocalizedNames[0] = "grain"; //Very ugly but y'know
        }

        MinecraftForge.EVENT_BUS.register(WheatHandler.class);

        ConditionModule.MODULES.put("HayBales", () -> CHANGE_HAY_BALES);
    }

    @Override
    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>) event.getRegistry();

        if (THRESH_WHEAT) {
            event.getRegistry().register(new ThreshingRecipe(new ResourceLocation(Reference.MOD_ID, "thresh_wheat"), new ItemStack(Items.WHEAT_SEEDS), new OreIngredient("cropWheat")).setRegistryName(new ResourceLocation(Reference.MOD_ID, "thresh_wheat")));
        }

        if (CHANGE_HAY_BALES) {
            removeRecipeByOutput(reg, new ItemStack(Blocks.HAY_BLOCK), "minecraft");
            removeRecipeByOutput(reg, new ItemStack(Items.WHEAT, 9), "minecraft");

            if (Loader.isModLoaded("quark")) {
                Block thatch = Block.getBlockFromName("quark:thatch");
                if (thatch != null) {
                    removeRecipeByOutput(reg, new ItemStack(thatch), "quark");
                    removeRecipeByOutput(reg, new ItemStack(Items.WHEAT, 4), "quark");
                }
            }
        }
    }

    @Override
    void init() {
        OreDictionary.registerOre("hay", ModItems.materialWheat.getMaterial("hay"));

        if (DIG_UP_CROPS) {
            Blocks.POTATOES.setHardness(1.0f).setHarvestLevel("hoe", 0);
            Blocks.CARROTS.setHardness(1.0f).setHarvestLevel("hoe", 0);
            Blocks.BEETROOTS.setHardness(1.0f).setHarvestLevel("hoe", 0);
        }

        if (MILL_GRAIN) {
            MillManager.getInstance().removeRecipe(new ItemStack(BWMBlocks.RAW_PASTRY, 1, BlockRawPastry.EnumType.BREAD.getMetadata()), ItemStack.EMPTY);
            MillManager.getInstance().addRecipe(0, new ItemStack(BWMBlocks.RAW_PASTRY, 1, BlockRawPastry.EnumType.BREAD.getMetadata()), ItemStack.EMPTY, new Object[]{new ItemStack(Items.WHEAT_SEEDS)});
        }

        if (THRESH_WHEAT_MILL) {
            MillManager.getInstance().addRecipe(0, new ItemStack(Items.WHEAT_SEEDS), ModItems.materialWheat.getMaterial("hay"), new Object[]{new ItemStack(Items.WHEAT)});
        }
    }

    @Override
    void preInitEnd() {
        if (TEXTURE_CHANGES) {
            BetterWithAddons.proxy.addResourceOverride("textures", "items", "seeds_wheat", "png");
            BetterWithAddons.proxy.addResourceOverride("textures", "items", "wheat", "png");
            BetterWithAddons.proxy.addResourceOverride(Reference.MOD_ID, "textures", "items", "bag_seed", "png");
        }
    }
}
