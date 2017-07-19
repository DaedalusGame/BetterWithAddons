package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.item.rbdtools.ItemAxeConvenient;
import betterwithaddons.item.rbdtools.ItemPickaxeConvenient;
import betterwithaddons.item.rbdtools.ItemSpadeConvenient;
import betterwithaddons.item.rbdtools.ItemSwordConvenient;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.BWMItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Random;

public class ModItems {
    public static ArrayList<Item> LIST = new ArrayList<Item>();

    public static Item.ToolMaterial bambooToolMaterial = EnumHelper.addToolMaterial("bamboo", 0, 51, 0.5f, -4.0f, 1);

    {
        bambooToolMaterial.setRepairItem(materialJapan.getMaterial("bamboo_slats"));
    }

    public static Item.ToolMaterial tamahaganeToolMaterial = EnumHelper.addToolMaterial("tamahagane", 3, 1210, 8.0f, 2.5f, 9);

    {
        tamahaganeToolMaterial.setRepairItem(materialJapan.getMaterial("tamahagane_finished"));
    }

    public static Item.ToolMaterial japansteelToolMaterial = EnumHelper.addToolMaterial("japansteel", 3, 1710, 8.0f, 4.0f, 10);

    {
        japansteelToolMaterial.setRepairItem(materialJapan.getMaterial("tamahagane_finished"));
    }

    public static ItemArmor.ArmorMaterial samuraiArmorMaterial = EnumHelper.addArmorMaterial("SAMURAI", "samurai", 16,
            new int[]{2, 6, 5, 2}, 18, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0);

    public static ItemGreatbow greatbow;
    public static ItemGreatarrow greatarrow;
    public static ItemMonument monument;
    public static ItemArtifactFrame artifactFrame;
    public static ItemModFood bakedMushroom;
    public static ItemModFood bakedAmanita;
    public static ItemModFood cookedBeetroot;
    public static ItemModFood bakedBeetroot;
    public static ItemModFood cookedCarrot;
    public static ItemModFood bakedCarrot;
    public static ItemModFood cookedPotato;
    public static ItemModFood pieMelon;
    public static ItemModFood pieMeat;
    public static ItemModFood pieMushroom;
    public static ItemModFood pieAmanita;
    public static ItemModFood meatballs;
    public static ItemModFood cookedEgg;
    public static ItemModFood groundMeat;
    public static Item soulSandPile;

    public static ItemMaterial bowls;

    public static ItemModFood cookedClownfish;
    public static ItemModFood cookedPuffer;
    public static ItemModFood preparedPuffer;
    public static ItemModFood preparedCookedPuffer;
    public static ItemModFood fuguSac;
    public static ItemWorldScale worldShard;

    public static ItemAncestryBottle ancestryBottle;
    public static ItemModFood sashimi;
    public static ItemModFood rice;
    public static ItemModFood riceBowl;
    public static ItemStainedBrick stainedBrick;
    public static ItemMaterial material;
    public static ItemMaterial materialJapan;
    public static ItemMaterial materialBolt;
    public static ItemMaterial materialCongealed;
    public static ItemMaterial materialCrate;
    public static ItemMaterial materialBag;
    public static ItemMaterial materialBundle;
    public static ItemMaterial materialDeco;
    public static ItemKatana katana;
    public static ItemWakizashi wakizashi;
    public static ItemTanto tanto;
    public static ItemShinai shinai;
    public static ItemYumi yumi;
    public static ItemYa ya;
    public static ItemSamuraiArmor samuraiHelm;
    public static ItemSamuraiArmor samuraiChestplate;
    public static ItemSamuraiArmor samuraiLeggings;
    public static ItemSamuraiArmor samuraiBoots;

    public static ItemToolShard brokenArtifact;

    public static ItemSpadeConvenient spade;
    public static ItemSwordConvenient machete;
    public static ItemPickaxeConvenient matchPick;
    public static ItemAxeConvenient kukri;
    public static ItemAxeConvenient carpenterSaw;
    public static ItemPickaxeConvenient masonPick;

    public static Item explosion;

    private static boolean isDirt(ItemStack tool, IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.GROUND || material == Material.SAND || material == Material.GRASS || material == Material.CLAY;
    }

    private static boolean isItemDirt(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            IBlockState state = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stack.getMetadata(), player, hand);
            return isDirt(stack, state);
        }
        return false;
    }

    private static boolean isItemMasonry(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            IBlockState state = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stack.getMetadata(), player, hand);
            return isMasonry(stack, state);
        }
        return false;
    }

    private static boolean isItemCarpentry(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock) item).getBlock();
            IBlockState state = block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, stack.getMetadata(), player, hand);
            return isCarpentry(stack, state);
        }
        return false;
    }

    private static boolean isMasonry(ItemStack tool, IBlockState state) {
        return state.getMaterial() == Material.ROCK && (!state.isFullCube() || !isOre(tool, state));
    }

    private static boolean isOre(ItemStack tool, IBlockState state) {
        Block block = state.getBlock();
        ItemStack oreItem = new ItemStack(block.getItemDropped(state, new Random(), 0), 1, block.damageDropped(state));
        return isItemOre(oreItem);
    }

    private static boolean isItemOre(ItemStack ore) {
        if (ore.isEmpty()) return false;
        if (ore.getItem() instanceof ItemBlock)
            for (int oreid : OreDictionary.getOreIDs(ore))
                if (OreDictionary.getOreName(oreid).startsWith("ore"))
                    return true;
        return false;
    }

    private static boolean isCarpentry(ItemStack tool, IBlockState state) {
        return state.getMaterial() == Material.WOOD && !isTree(tool, state);
    }

    private static boolean isTree(ItemStack tool, IBlockState state) {
        Block block = state.getBlock();

        return block instanceof BlockLog || block instanceof BlockLeaves;
    }

    private static boolean isFoliage(ItemStack tool, IBlockState state) {
        Material material = state.getMaterial();
        return material == Material.VINE || material == Material.LEAVES || material == Material.PLANTS;
    }

    private static boolean isWeb(ItemStack tool, IBlockState state) {
        return state.getMaterial() == Material.WEB;
    }

    public static void load(FMLPreInitializationEvent event) {
        /*arrowhead = registerItem("arrowhead",new Item());
        midori = registerItem("midori",new Item());
        midori_popped = registerItem("midori_popped",new Item());
        stone_brick = registerItem("stone_brick",new Item());
        bone_ingot = registerItem("bone_ingot",new Item());
        ender_cream = registerItem("ender_cream",new Item());
        thornrose = registerItem("thornrose",new Item());*/

        spade = (ItemSpadeConvenient) registerItem("steel_spade", new ItemSpadeConvenient(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean canCollect(ItemStack stack, IBlockState state) {
                return isDirt(stack, state);
            }

            @Override
            public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
                return isItemDirt(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            }

            @Override
            public boolean canHarvestEfficiently(ItemStack stack, IBlockState state) {
                return isDirt(stack, state);
            }

            @Override
            public float getEfficiency(ItemStack stack, IBlockState state) {
                return isDirt(stack, state) ? 1.5f : 1.0f;
            }
        });
        matchPick = (ItemPickaxeConvenient) registerItem("steel_matchpick", new ItemPickaxeConvenient(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
                return ItemUtil.matchesOreDict(stack, "torch");
            }
        });
        machete = (ItemSwordConvenient) registerItem("steel_machete", new ItemSwordConvenient(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean canCollect(ItemStack stack, IBlockState state) {
                return isFoliage(stack, state);
            }

            @Override
            public boolean canShear(ItemStack stack, IBlockState state) {
                return isFoliage(stack, state);
            }

            @Override
            public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
                return ItemUtil.matchesOreDict(stack, "vine");
            }

            @Override
            public boolean canHarvestEfficiently(ItemStack stack, IBlockState state) {
                return isFoliage(stack, state) || isWeb(stack,state);
            }

            @Override
            public float getEfficiency(ItemStack stack, IBlockState state) {
                return isFoliage(stack, state) ? 15.0f : (isWeb(stack,state) ? 1.5f : 1.0f);
            }
        });
        kukri = (ItemAxeConvenient) registerItem("steel_kukri", new ItemAxeConvenient(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean canCollect(ItemStack stack, IBlockState state) {
                return isTree(stack, state);
            }

            @Override
            public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
                return ItemUtil.matchesOreDict(stack, "treeSapling");
            }

            @Override
            public boolean canHarvestEfficiently(ItemStack stack, IBlockState state) {
                return isTree(stack, state);
            }

            @Override
            public float getEfficiency(ItemStack stack, IBlockState state) {
                return isTree(stack, state) ? 1.5f : 1.0f;
            }
        });
        carpenterSaw = (ItemAxeConvenient) registerItem("steel_carpentersaw", new ItemAxeConvenient(BWMItems.SOULFORGED_STEEL, 8.0F, -3.0F) {
            @Override
            public boolean canCollect(ItemStack stack, IBlockState state) {
                return isCarpentry(stack, state);
            }

            @Override
            public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
                return isItemCarpentry(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            }

            @Override
            public float getEfficiency(ItemStack stack, IBlockState state) {
                return isCarpentry(stack, state) ? 1.5f : 0.25f;
            }
        });
        masonPick = (ItemPickaxeConvenient) registerItem("steel_masonpick", new ItemPickaxeConvenient(BWMItems.SOULFORGED_STEEL) {
            @Override
            public boolean canCollect(ItemStack stack, IBlockState state) {
                return isMasonry(stack, state);
            }

            @Override
            public boolean canPlace(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
                return isItemMasonry(stack, player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
            }

            @Override
            public float getEfficiency(ItemStack stack, IBlockState state) {
                return isMasonry(stack, state) ? 1.5f : 0.25f;
            }
        });

        material = (ItemMaterial) registerItem("material", new ItemMaterial(
                new String[]{"arrowhead", "midori", "midori_popped", "thornrose", "stone_brick", "bone_ingot", "ender_cream"}
        ));

        worldShard = (ItemWorldScale) registerItem("worldshard", new ItemWorldScale());
        greatbow = (ItemGreatbow) registerItem("greatbow", new ItemGreatbow());
        greatarrow = (ItemGreatarrow) registerItem("greatarrow", new ItemGreatarrow());
        monument = (ItemMonument) registerItem("monument", new ItemMonument());
        //Food
        bakedMushroom = (ItemModFood) registerItem("food_mushroom_baked", new ItemModFood(3, 0.2F, false));
        bakedAmanita = (ItemModFood) registerItem("food_amanita_baked", new ItemModFood(3, 0.2F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        cookedBeetroot = (ItemModFood) registerItem("food_beetroot_cooked", new ItemModFood(4, 0.5F, false));
        bakedBeetroot = (ItemModFood) registerItem("food_beetroot_baked", new ItemModFood(5, 0.4F, false));
        cookedCarrot = (ItemModFood) registerItem("food_carrot_cooked", new ItemModFood(3, 0.6F, false));
        bakedCarrot = (ItemModFood) registerItem("food_carrot_baked", new ItemModFood(4, 0.5F, false));
        cookedPotato = (ItemModFood) registerItem("food_potato_cooked", new ItemModFood(4, 0.7F, false));
        cookedEgg = (ItemModFood) registerItem("food_egg_cooked", new ItemModFood(4, 0.3F, false));
        meatballs = (ItemModFood) registerItem("food_meatballs", new ItemModFood(6, 0.6F, true));

        groundMeat = (ItemModFood) registerItem("food_ground_meat", new ItemModFood(2, 0.1F, true));

        pieMushroom = (ItemModFood) registerItem("food_pie_mushroom", new ItemModFood(8, 0.3F, false));
        pieAmanita = (ItemModFood) registerItem("food_pie_amanita", new ItemModFood(8, 0.3F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 0), 1.0F));
        pieMeat = (ItemModFood) registerItem("food_pie_meat", new ItemModFood(9, 0.5F, true));
        pieMelon = (ItemModFood) registerItem("food_pie_melon", new ItemModFood(8, 0.4F, false));

        cookedClownfish = (ItemModFood) registerItem("food_clownfish_cooked", new ItemModFood(6, 0.5F, false));
        cookedPuffer = (ItemModFood) registerItem("food_pufferfish_baked", new ItemModFood(6, 0.6F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 500, 1), 0.1F));
        preparedPuffer = (ItemModFood) registerItem("food_pufferfish_prepared", new ItemModFood(2, 0.1F, false));
        preparedCookedPuffer = (ItemModFood) registerItem("food_pufferfish_cooked", new ItemModFood(4, 0.5F, false));
        fuguSac = (ItemModFood) registerItem("food_fugu_sac", new ItemModFood(2, 0.1F, false).setPotionEffect(new PotionEffect(MobEffects.WITHER, 2000, 1), 1.0F));

        sashimi = (ItemModFood) registerItem("food_sashimi", new ItemModFood(2, 0.1F, false));
        rice = (ItemModFood) registerItem("food_cooked_rice", new ItemModFood(2, 0.3F, false));
        riceBowl = (ItemModFood) registerItem("food_bowl_rice", new ItemModFood(9, 0.6F, false).setMaxStackSize(1));

        soulSandPile = registerItem("soulsand_pile", new Item());

        bowls = (ItemMaterial) registerItem("bowl", new ItemMaterial(
                new String[]{"salt"}
        ));
        bowls.setContainer(new ItemStack(Items.BOWL));
        OreDictionary.registerOre("foodSalt", bowls.getMaterial("salt"));

        artifactFrame = (ItemArtifactFrame) registerItem("artifact_frame", new ItemArtifactFrame());
        brokenArtifact = (ItemToolShard) registerItem("tool_shard", new ItemToolShard().setMaxStackSize(1));

        stainedBrick = (ItemStainedBrick) registerItem("brick_stained", new ItemStainedBrick());

        shinai = (ItemShinai) registerItem("shinai", new ItemShinai());
        katana = (ItemKatana) registerItem("katana", new ItemKatana());
        wakizashi = (ItemWakizashi) registerItem("wakizashi", new ItemWakizashi());
        tanto = (ItemTanto) registerItem("tanto", new ItemTanto());
        yumi = (ItemYumi) registerItem("yumi", new ItemYumi());
        ya = (ItemYa) registerItem("ya", new ItemYa());
        ancestryBottle = (ItemAncestryBottle) registerItem("ancestry_bottle", new ItemAncestryBottle());

        materialJapan = (ItemMaterial) registerItem("japanmat", new ItemMaterial(
                new String[]{"rice", "soaked_rice", "rice_stalk", "rice_hay", "rice_ash", "rush",
                        "soaked_bamboo", "bamboo_slats", "soaked_mulberry", "mulberry_paste", "mulberry_sheet", "washi",
                        "iron_scales", "lamellar", "paper_lamellar", "tsuka", "half_katana_blade", "ya_head", "yumi_top", "yumi_bottom",
                        "tamahagane", "tamahagane_heated", "tamahagane_folded", "tamahagane_reheated", "tamahagane_finished", "tamahagane_wrapped",
                        "hocho_tetsu", "hocho_tetsu_heated", "hocho_tetsu_fold_1", "hocho_tetsu_fold_2", "hocho_tetsu_finished",
                        "helmet_undecorated", "chest_undecorated", "legs_undecorated", "boots_undecorated", "bark_sakura", "bark_mulberry"}
        ));

        samuraiHelm = (ItemSamuraiArmor) registerItem("helmet_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.HEAD));
        samuraiChestplate = (ItemSamuraiArmor) registerItem("chest_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.CHEST));
        samuraiLeggings = (ItemSamuraiArmor) registerItem("legs_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.LEGS));
        samuraiBoots = (ItemSamuraiArmor) registerItem("boots_samurai", new ItemSamuraiArmor(EntityEquipmentSlot.FEET));

        materialBag = (ItemMaterial) registerItem("bag", new ItemMaterial(
                new String[]{"seed", "seed_hemp", "seed_melon", "seed_pumpkin", "seed_beets", "cocoa", "redstone", "glowstone", "sugar", "gunpowder", "flour", "sulfur", "nitre", "sawdust", "sawdust_soul", "potash", "hellfire", "kibble"}
        ));
        materialCrate = (ItemMaterial) registerItem("crate", new ItemMaterial(
                new String[]{"pork", "pork_raw", "chicken", "chicken_raw", "steak", "steak_raw", "mutton", "mutton_raw", "rabbit", "rabbit_raw", "egg", "slime", "enderpearl"}
        ));
        materialCongealed = (ItemMaterial) registerItem("congealed", new ItemMaterial(
                new String[]{"bone", "flesh", "eye", "amanita", "mushroom", "wart"}
        ));
        materialBolt = (ItemMaterial) registerItem("bolt", new ItemMaterial(
                new String[]{"fabric", "vine", "paper", "leather", "scoured_leather", "tanned_leather", "string"}
        ));
        materialBundle = (ItemMaterial) registerItem("bundle", new ItemMaterial(
                new String[]{"feather", "arrows", "blazerods", "oak", "birch", "spruce", "jungle", "acacia", "darkoak"}
        ));

        materialDeco = (ItemMaterial) registerItem("decomat", new ItemMaterial(
                new String[]{"hemp_oil", "wood_bleach", "wood_stain", "glass_chunk"}
        ));

        explosion = registerItem("explosion", new Item() {
            @Override
            public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
                //NOOP
            }
        });
    }

    private static Item registerItem(String name, Item item) {
        item.setUnlocalizedName(name);
        item.setCreativeTab(BetterWithAddons.instance.creativeTab);
        GameRegistry.register(item, new ResourceLocation(Reference.MOD_ID, name));
        LIST.add(item);

        return item;
    }
}
