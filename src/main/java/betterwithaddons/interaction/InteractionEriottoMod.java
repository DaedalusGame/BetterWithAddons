package betterwithaddons.interaction;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.OreStack;
import betterwithaddons.crafting.recipes.ArmorDecorateRecipe;
import betterwithaddons.crafting.manager.*;
import betterwithaddons.crafting.recipes.SmeltingRecipe;
import betterwithaddons.item.ModItems;
import betterwithaddons.util.IDisableable;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.blockmeta.managers.SawManager;
import betterwithmods.common.registry.bulk.manager.CauldronManager;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InteractionEriottoMod extends Interaction {
    public static boolean ENABLED = true;
    public static boolean GRASS_DROPS_SEEDS = true;
    public static boolean INFUSER_REPAIRS = true;
    public static boolean ALTERNATE_INFUSER_RECIPE = false;
    public static int MAX_SPIRITS = 128;
    public static int SPIRIT_PER_BOTTLE = 8;
    public static boolean JAPANESE_RANDOM_SPAWN;
    public final ArrayList<Item> REPAIRABLE_TOOLS;

    public InteractionEriottoMod() {
        associatedItems = new IDisableable[]{
                ModItems.materialJapan, ModItems.katana, ModItems.wakizashi, ModItems.tanto, ModItems.shinai, ModItems.samuraiBoots, ModItems.samuraiLeggings, ModItems.samuraiHelm, ModItems.samuraiChestplate,
                ModItems.fuguSac, ModItems.sashimi, ModItems.preparedPuffer, ModItems.preparedCookedPuffer, ModItems.rice, ModItems.riceBowl, ModItems.yumi, ModItems.ya,
                ModBlocks.bamboo, ModBlocks.bambooSlats, ModBlocks.rice, ModBlocks.rush, ModBlocks.shoji, ModBlocks.fusuma, ModBlocks.tatami,
                ModBlocks.cherrybox, ModBlocks.tatara, ModBlocks.nettedScreen, ModBlocks.kera, ModBlocks.ironSand,
                ModBlocks.mulberryLog, ModBlocks.mulberryLeaves, ModBlocks.mulberryPlanks, ModBlocks.mulberrySapling,
                ModBlocks.sakuraLog, ModBlocks.sakuraLeaves, ModBlocks.sakuraPlanks, ModBlocks.sakuraSapling, ModBlocks.sakuraLeafPile
        };

        REPAIRABLE_TOOLS = Lists.newArrayList(ModItems.katana, ModItems.wakizashi, ModItems.tanto, ModItems.shinai, ModItems.yumi, ModItems.samuraiBoots, ModItems.samuraiLeggings, ModItems.samuraiHelm, ModItems.samuraiChestplate);
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
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        CraftingManagerSandNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModBlocks.ironSand, 1)}, new ItemStack(Blocks.IRON_BLOCK, 1), 8);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(Blocks.IRON_BLOCK, 1), new ItemStack(Blocks.SAND, 8)}, new ItemStack(ModBlocks.ironSand, 1), 0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, new ItemStack(ModItems.sashimi, 3)}, new ItemStack(Items.FISH, 1), 0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{new ItemStack(ModItems.fuguSac, 1), new ItemStack(ModItems.preparedPuffer, 3)}, new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 0);
        CraftingManagerFireNet.getInstance().addRecipe(new ItemStack[]{ModItems.materialJapan.getMaterial("iron_scales", 27)}, new ItemStack(ModBlocks.ironSand, 1), 0);
        CraftingManagerWaterNet.getInstance().addRecipe(new ItemStack[]{ItemStack.EMPTY, ModItems.materialJapan.getMaterial("washi", 9)}, ModItems.materialJapan.getMaterial("mulberry_sheet"), 0);

        OreDictionary.registerOre("listAllSeeds", new ItemStack(Items.WHEAT_SEEDS)); //I'll beat the shit out of HarvestCraft
        OreDictionary.registerOre("listAllSeeds", new ItemStack(Items.PUMPKIN_SEEDS));
        OreDictionary.registerOre("listAllSeeds", new ItemStack(Items.MELON_SEEDS));
        OreDictionary.registerOre("listAllSeeds", new ItemStack(Items.BEETROOT_SEEDS));
        if (ModInteractions.bwm.isActive())
            OreDictionary.registerOre("listAllSeeds", new ItemStack(BWMBlocks.HEMP));
        OreDictionary.registerOre("listAllSeeds", new ItemStack(ModBlocks.rice));
        OreDictionary.registerOre("listAllSeeds", new ItemStack(ModBlocks.rush));

        OreDictionary.registerOre("cropRice", ModItems.materialJapan.getMaterial("rice"));
        OreDictionary.registerOre("seedRice", new ItemStack(ModBlocks.rice));
        OreDictionary.registerOre("cropRush", ModItems.materialJapan.getMaterial("rush"));
        OreDictionary.registerOre("seedRush", new ItemStack(ModBlocks.rush));
        OreDictionary.registerOre("materialBamboo", new ItemStack(ModBlocks.bamboo));

        OreDictionary.registerOre("logWood", ModBlocks.sakuraLog);
        OreDictionary.registerOre("logWood", ModBlocks.mulberryLog);
        OreDictionary.registerOre("plankWood", ModBlocks.sakuraPlanks);
        OreDictionary.registerOre("plankWood", ModBlocks.mulberryPlanks);

        OreDictionary.registerOre("ingotTamahagane", ModItems.materialJapan.getMaterial("tamahagane_finished"));
        OreDictionary.registerOre("ingotHochoTetsu", ModItems.materialJapan.getMaterial("hocho_tetsu_finished"));

        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sakuraPlanks, 4), new ItemStack(ModBlocks.sakuraLog));
        GameRegistry.addShapedRecipe(new ItemStack(Items.PAPER), "aaa", 'a', new ItemStack(ModBlocks.mulberryLog));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.mulberryPlanks), new ItemStack(ModBlocks.mulberryLog));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("mulberry_sheet"), "aa", "aa", 'a', ModItems.materialJapan.getMaterial("mulberry_paste"));

        if (ModInteractions.bwm.isActive()) {
            SawManager.INSTANCE.removeRecipes(new ItemStack(ModBlocks.mulberryLog));
            SawManager.INSTANCE.removeRecipes(new ItemStack(ModBlocks.sakuraLog));
            SawManager.INSTANCE.addRecipe(ModBlocks.mulberryLog, OreDictionary.WILDCARD_VALUE, new ItemStack(ModBlocks.mulberryPlanks, 2), ModItems.materialJapan.getMaterial("bark_mulberry"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));
            SawManager.INSTANCE.addRecipe(ModBlocks.sakuraLog, OreDictionary.WILDCARD_VALUE, new ItemStack(ModBlocks.sakuraPlanks, 6), ModItems.materialJapan.getMaterial("bark_sakura"), ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));

            //ItemStack dung = ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.DUNG,1);
            //GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sakuraSapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.PINK.getDyeDamage()),dung);
            //GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.mulberrySapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()),dung);
            //GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bamboo), new ItemStack(Items.REEDS, 1), new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()),dung);
            //BWMRecipeHelper.addCauldronRecipe(new ItemStack(ModItems.preparedCookedPuffer),new Object[]{new ItemStack(ModItems.preparedPuffer)}); automatically added
            CauldronManager.getInstance().addRecipe(new ItemStack(ModItems.rice), new Object[]{ModItems.materialJapan.getMaterial("soaked_rice")});
        }
        /*else {
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.sakuraSapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.PINK.getDyeDamage()));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.mulberrySapling), new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata()), new ItemStack(Items.DYE, 1, EnumDyeColor.YELLOW.getDyeDamage()));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.bamboo), new ItemStack(Items.REEDS, 1), new ItemStack(Items.DYE, 1, EnumDyeColor.WHITE.getDyeDamage()));
        }*/

        GameRegistry.addSmelting(new ItemStack(ModItems.preparedPuffer), new ItemStack(ModItems.preparedCookedPuffer), 0.35f);
        GameRegistry.addSmelting(ModItems.materialJapan.getMaterial("soaked_rice"), new ItemStack(ModItems.rice), 0.35f);

        if (GRASS_DROPS_SEEDS) {
            MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rice), 2);
            MinecraftForge.addGrassSeed(new ItemStack(ModBlocks.rush), 2);
        }

        GameRegistry.addShapedRecipe(new ItemStack(ModItems.riceBowl), "r", "r", "b", 'r', ModItems.rice, 'b', new ItemStack(Items.BOWL));

        addFoldingRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu_fold_1"), ModItems.materialJapan.getMaterial("hocho_tetsu_heated"));
        addFoldingRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu_fold_2"), ModItems.materialJapan.getMaterial("hocho_tetsu_fold_1"));
        addFoldingRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu_finished"), ModItems.materialJapan.getMaterial("hocho_tetsu_fold_2"));

        addFoldingRecipe(ModItems.materialJapan.getMaterial("tamahagane_folded"), ModItems.materialJapan.getMaterial("tamahagane_heated"));
        addFoldingRecipe(ModItems.materialJapan.getMaterial("tamahagane_finished"), ModItems.materialJapan.getMaterial("tamahagane_reheated"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("tamahagane_wrapped"), " w ", "wtw", " w ", 't', ModItems.materialJapan.getMaterial("tamahagane_folded"), 'w', ModItems.materialJapan.getMaterial("washi"));

        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("lamellar"), "iii", "sls", "iii", 'i', ModItems.materialJapan.getMaterial("iron_scales"), 'l', new ItemStack(Items.LEATHER), 's', new ItemStack(Items.STRING));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("paper_lamellar"), "pwp", "sws", "pwp", 'w', ModItems.materialJapan.getMaterial("washi"), 'p', new ItemStack(Items.PAPER), 's', new ItemStack(Items.STRING));

        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("tsuka"), "lll", "ssp", 'l', new ItemStack(Items.LEATHER), 's', new ItemStack(Items.STICK), 'p', new ItemStack(ModBlocks.sakuraPlanks));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("half_katana_blade"), "t", "h", "t", 't', ModItems.materialJapan.getMaterial("tamahagane_finished"), 'h', ModItems.materialJapan.getMaterial("hocho_tetsu_finished"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("ya_head", 3), "t  ", "hh ", "ttt", 't', ModItems.materialJapan.getMaterial("tamahagane_finished"), 'h', ModItems.materialJapan.getMaterial("hocho_tetsu_finished"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("yumi_top"), " b", "bs", "bl", 'l', new ItemStack(Items.LEATHER), 's', new ItemStack(Items.STICK), 'b', ModItems.materialJapan.getMaterial("bamboo_slats"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("yumi_bottom"), "bl", "bs", " b", 'l', new ItemStack(Items.LEATHER), 's', new ItemStack(Items.STICK), 'b', ModItems.materialJapan.getMaterial("bamboo_slats"));

        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.katana), "l", "l", "w", 'l', ModItems.materialJapan.getMaterial("half_katana_blade"), 'w', ModItems.materialJapan.getMaterial("tsuka")));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.wakizashi), "l", "l", "w", 'l', ModItems.materialJapan.getMaterial("tamahagane_finished"), 'w', ModItems.materialJapan.getMaterial("tsuka")));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.tanto), "l", "w", 'l', ModItems.materialJapan.getMaterial("tamahagane_finished"), 'w', ModItems.materialJapan.getMaterial("tsuka")));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.shinai), "l", "l", "w", 'l', ModItems.materialJapan.getMaterial("bamboo_slats"), 'w', ModItems.materialJapan.getMaterial("tsuka")));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.ya, 12), "h", "l", "f", 'h', ModItems.materialJapan.getMaterial("ya_head"), 'l', ModItems.materialJapan.getMaterial("bamboo_slats"), 'f', new ItemStack(Items.FEATHER)));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.yumi), " ns", "l s", " us", 'n', ModItems.materialJapan.getMaterial("yumi_top"), 'u', ModItems.materialJapan.getMaterial("yumi_bottom"), 'l', new ItemStack(Items.LEATHER), 's', new ItemStack(Items.STRING)));

        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("helmet_undecorated"), "lll", "l l", 'l', ModItems.materialJapan.getMaterial("lamellar"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("chest_undecorated"), "l l", "lll", "lll", 'l', ModItems.materialJapan.getMaterial("lamellar"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("legs_undecorated"), "lll", "l l", "l l", 'l', ModItems.materialJapan.getMaterial("lamellar"));
        GameRegistry.addShapedRecipe(ModItems.materialJapan.getMaterial("boots_undecorated"), "l l", "l l", 'l', ModItems.materialJapan.getMaterial("lamellar"));

        addArmorFinishRecipe(new ItemStack(ModItems.samuraiHelm), ModItems.materialJapan.getMaterial("helmet_undecorated"));
        addArmorFinishRecipe(new ItemStack(ModItems.samuraiChestplate), ModItems.materialJapan.getMaterial("chest_undecorated"));
        addArmorFinishRecipe(new ItemStack(ModItems.samuraiLeggings), ModItems.materialJapan.getMaterial("legs_undecorated"));
        addArmorFinishRecipe(new ItemStack(ModItems.samuraiBoots), ModItems.materialJapan.getMaterial("boots_undecorated"));

        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.nettedScreen), "bsb", "sss", "bsb", 's', new ItemStack(Items.STRING), 'b', ModItems.materialJapan.getMaterial("bamboo_slats")));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bambooSlats), "bb", "bb", 'b', ModItems.materialJapan.getMaterial("bamboo_slats"));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.tatara), "idi", "g g", "ini", 'i', new ItemStack(Items.IRON_INGOT), 'g', new ItemStack(Items.GOLD_INGOT), 'd', new ItemStack(Items.DIAMOND), 'n', new ItemStack(Blocks.NETHERRACK)));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cherrybox, 1, 0), "pxp", "x x", "pxp", 'p', new ItemStack(ModBlocks.sakuraPlanks), 'x', new ItemStack(Blocks.IRON_BARS)));
        CraftingManagerInfuser.getInstance().addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.cherrybox, 1, 1), "pxp", "p p", "ppp", 'p', new ItemStack(ModBlocks.sakuraPlanks), 'x', new ItemStack(Blocks.GLASS_PANE)));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.shoji, 4), "bwb", "wbw", "bwb", 'b', ModItems.materialJapan.getMaterial("bamboo_slats"), 'w', ModItems.materialJapan.getMaterial("washi"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.fusuma, 4), "bwb", "wpw", "bwb", 'b', ModItems.materialJapan.getMaterial("bamboo_slats"), 'w', ModItems.materialJapan.getMaterial("washi"), 'p', new ItemStack(ModBlocks.sakuraPlanks));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.tatami, 2), "rrr", "www", 'r', ModItems.materialJapan.getMaterial("rush"), 'w', ModItems.materialJapan.getMaterial("rice_hay"));

        GameRegistry.addSmelting(ModItems.materialJapan.getMaterial("rice_stalk"), ModItems.materialJapan.getMaterial("rice_ash"), 0.1f);

        if (ALTERNATE_INFUSER_RECIPE)
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.infuser), " a ", "waw", "dod", 'o', new ItemStack(Blocks.OBSIDIAN), 'd', new ItemStack(Items.DIAMOND), 'w', new ItemStack(Blocks.WOOL, 1, EnumDyeColor.YELLOW.getMetadata()), 'a', new ItemStack(ModItems.ancestryBottle));
        else
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.infuser), " a ", " a ", "wtw", 't', new ItemStack(Blocks.ENCHANTING_TABLE), 'w', new ItemStack(Blocks.WOOL, 1, EnumDyeColor.YELLOW.getMetadata()), 'a', new ItemStack(ModItems.ancestryBottle));

        //Random seeds
        CraftingManagerInfuserTransmutation.instance().addRecipe(new SmeltingRecipe(new OreStack("listAllSeeds"), ItemStack.EMPTY) {
            Random random = new Random();

            @Override
            public ItemStack getOutput(ItemStack input) {
                if (random.nextInt(2) == 0)
                    return new ItemStack(ModBlocks.rice);
                else
                    return new ItemStack(ModBlocks.rush);
            }

            @Override
            public boolean matchesInput(ItemStack item) {
                if (item.getItem() == Item.getItemFromBlock(ModBlocks.rice) || item.getItem() == Item.getItemFromBlock(ModBlocks.rush))
                    return false;

                return super.matchesInput(item);
            }

            @Override
            public List<ItemStack> getRecipeOutputs() {
                return Lists.newArrayList(new ItemStack(ModBlocks.rice), new ItemStack(ModBlocks.rush));
            }
        });
        //Random saplings
        CraftingManagerInfuserTransmutation.instance().addRecipe(new SmeltingRecipe(new OreStack("treeSapling"), ItemStack.EMPTY) {
            Random random = new Random();

            @Override
            public ItemStack getOutput(ItemStack input) {
                if (random.nextInt(2) == 0)
                    return new ItemStack(ModBlocks.sakuraSapling);
                else
                    return new ItemStack(ModBlocks.mulberrySapling);
            }

            @Override
            public boolean matchesInput(ItemStack item) {
                if (item.getItem() == Item.getItemFromBlock(ModBlocks.sakuraSapling) || item.getItem() == Item.getItemFromBlock(ModBlocks.mulberrySapling))
                    return false;

                return super.matchesInput(item);
            }

            @Override
            public List<ItemStack> getRecipeOutputs() {
                return Lists.newArrayList(new ItemStack(ModBlocks.sakuraSapling), new ItemStack(ModBlocks.mulberrySapling));
            }
        });
        //Bamboo
        CraftingManagerInfuserTransmutation.instance().addRecipe(new OreStack("sugarcane"), new ItemStack(ModBlocks.bamboo));
        //Repair tools and armor
        if (INFUSER_REPAIRS)
            CraftingManagerInfuserTransmutation.instance().addRecipe(new SmeltingRecipe(new ItemStack(ModItems.katana), ItemStack.EMPTY) {
                @Override
                public boolean matchesInput(ItemStack item) {
                    return isRepairableTool(item);
                }

                @Override
                public List<ItemStack> getRecipeInputs() {
                    ArrayList<ItemStack> inputs = new ArrayList<>();

                    for (Item tool : REPAIRABLE_TOOLS) {
                        ItemStack basestack = new ItemStack(tool);
                        if (tool.isDamageable()) {
                            int maxdamage = basestack.getMaxDamage();
                            int durability = maxdamage / 20;
                            for (int i = 20; i >= 1; i--) {
                                ItemStack damaged = new ItemStack(tool, 1, Math.min(durability * i, maxdamage - 1));
                                inputs.add(damaged);
                            }
                        }
                    }

                    return inputs;
                }

                @Override
                public ItemStack getOutput(ItemStack input) {
                    if (input.isItemStackDamageable())
                        input.setItemDamage(Math.max(0, input.getItemDamage() - input.getMaxDamage() / 20));
                    return input;
                }
            });

        CraftingManagerSoakingBox.instance().addRecipe(new ItemStack(ModBlocks.bamboo), ModItems.materialJapan.getMaterial("soaked_bamboo"));
        CraftingManagerSoakingBox.instance().addRecipe(ModItems.materialJapan.getMaterial("rice"), ModItems.materialJapan.getMaterial("soaked_rice"));
        if (ModInteractions.bwm.isActive())
            CraftingManagerSoakingBox.instance().addRecipe(ModItems.materialJapan.getMaterial("bark_mulberry"), ModItems.materialJapan.getMaterial("soaked_mulberry"));
        CraftingManagerSoakingBox.instance().addRecipe(new ItemStack(ModBlocks.mulberryLog), ModItems.materialJapan.getMaterial("soaked_mulberry"));
        CraftingManagerSoakingBox.instance().addRecipe(new ItemStack(Blocks.SPONGE, 1, 0), new ItemStack(Blocks.SPONGE, 1, 1));

        CraftingManagerDryingBox.instance().addRecipe(ModItems.materialJapan.getMaterial("rice_stalk"), ModItems.materialJapan.getMaterial("rice_hay"));
        CraftingManagerDryingBox.instance().addRecipe(ModItems.materialJapan.getMaterial("soaked_mulberry"), ModItems.materialJapan.getMaterial("mulberry_paste"));
        CraftingManagerDryingBox.instance().addRecipe(ModItems.materialJapan.getMaterial("soaked_bamboo"), ModItems.materialJapan.getMaterial("bamboo_slats"));
        CraftingManagerDryingBox.instance().addRecipe(new ItemStack(Blocks.SPONGE, 1, 1), new ItemStack(Blocks.SPONGE, 1, 0));

        CraftingManagerTatara.instance().addRecipe(new ItemStack(ModBlocks.ironSand), new ItemStack(ModBlocks.kera));
        CraftingManagerTatara.instance().addRecipe(ModItems.materialJapan.getMaterial("tamahagane"), ModItems.materialJapan.getMaterial("tamahagane_heated"));
        CraftingManagerTatara.instance().addRecipe(ModItems.materialJapan.getMaterial("tamahagane_wrapped"), ModItems.materialJapan.getMaterial("tamahagane_reheated"));
        CraftingManagerTatara.instance().addRecipe(ModItems.materialJapan.getMaterial("hocho_tetsu"), ModItems.materialJapan.getMaterial("hocho_tetsu_heated"));
    }

    private boolean isRepairableTool(ItemStack stack) {
        return REPAIRABLE_TOOLS.contains(stack.getItem()) && stack.isItemDamaged();
    }

    private void addArmorFinishRecipe(ItemStack out, ItemStack in) {
        //ItemStack gold = new ItemStack(Items.GOLD_NUGGET);
        //ItemStack dye = new ItemStack(Items.DYE,1,14);
        //ItemStack washi = ModItems.materialJapan.getMaterial("washi");
        //GameRegistry.addShapelessRecipe(out,gold,gold,gold,dye,in,dye,washi,washi,washi);
        CraftingManagerInfuser.getInstance().addRecipe(new ArmorDecorateRecipe(out, "ggg", "dad", "www", 'g', "nuggetGold", 'd', "dye", 'a', in, 'w', ModItems.materialJapan.getMaterial("washi")));
    }

    private void addFoldingRecipe(ItemStack out, ItemStack in) {
        GameRegistry.addShapedRecipe(out, "t", "s", 't', in, 's', new ItemStack(Items.STICK));
    }

    @Override
    public void postInit() {

    }
}
