package betterwithaddons.block;

import betterwithaddons.block.BetterRedstone.BlockPCB;
import betterwithaddons.block.BetterRedstone.BlockWirePCB;
import betterwithaddons.block.EriottoMod.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

public class ModBlocks {
    public static ArrayList<Block> LIST = new ArrayList<Block>();

    public static BlockBannerDetector bannerDetector;
    public static BlockWorldScale worldScale;
    public static BlockWorldScaleActive worldScaleActive;
    public static BlockElytraMagma elytraMagma;
    public static BlockExtraGrass grass;
    public static BlockWirePCB pcbwire;
    public static BlockPCB pcbblock;
    public static BlockLattice lattice;
    public static BlockAlchDragon alchDragon;
    public static BlockModLog mulberryLog;
    public static BlockModLog sakuraLog;
    public static BlockModPlanks mulberryPlanks;
    public static BlockModPlanks sakuraPlanks;
    public static BlockCropRush rush;
    public static BlockCropRice rice;
    public static BlockColoredBrick coloredBrick;
    public static BlockSlat bambooSlats;
    public static BlockIronSand ironSand;
    public static BlockKera kera;
    public static BlockNettedScreen nettedScreen;
    public static BlockTatara tatara;
    public static BlockCherryBox cherrybox;
    public static BlockWorldScaleOre worldScaleOre;
    public static BlockBamboo bamboo;
    public static BlockModLeaves sakuraLeaves;
    public static BlockModSapling sakuraSapling;
    public static BlockModLeaves mulberryLeaves;
    public static BlockModSapling mulberrySapling;
    public static BlockCherryLeafPile sakuraLeafPile;
    public static BlockThornRose thornrose;
    public static BlockThorns thorns;
    public static BlockModLeaves luretreeLeaves;
    public static BlockLureTreeSapling luretreeSapling;
    public static BlockModLog luretreeLog;
    public static BlockLureTree luretreeFace;
    public static BlockModPane paperWall;
    public static BlockModPane wroughtBars;
    public static BlockModPane shoji;
    public static BlockFusumaPainted fusuma;
    //public static BlockFusumaPainted fusuma2;
    public static BlockChandelier chandelier;
    public static BlockLantern paperLantern;
    public static BlockLantern wroughtLantern;
    public static BlockTatami tatami;
    public static BlockPavement pavement;
    public static BlockSoap wetSoap;
    public static BlockWhiteBrick whiteBrick;
    public static BlockChute chute;

    public static void load(FMLPreInitializationEvent event) {
        bannerDetector = (BlockBannerDetector) addBlock(new BlockBannerDetector());
        worldScale = (BlockWorldScale) addBlock(new BlockWorldScale());
        worldScaleOre = (BlockWorldScaleOre) addBlock(new BlockWorldScaleOre());
        worldScaleActive = (BlockWorldScaleActive) addBlock(new BlockWorldScaleActive());
        elytraMagma = (BlockElytraMagma) addBlock(new BlockElytraMagma());
        grass = (BlockExtraGrass) addBlock(new BlockExtraGrass());
        pcbwire = (BlockWirePCB) addBlock(new BlockWirePCB());
        pcbblock = (BlockPCB) addBlock(new BlockPCB());
        lattice = (BlockLattice) addBlock(new BlockLattice());
        thornrose = (BlockThornRose) addBlock(new BlockThornRose());
        thorns = (BlockThorns) addBlock(new BlockThorns());
        chute = (BlockChute) addBlock(new BlockChute());
        //alchDragon = (BlockAlchDragon) addBlock(new BlockAlchDragon());

        luretreeLeaves = (BlockModLeaves) addBlock(new BlockModLeaves(ModWoods.LURETREE));
        luretreeSapling = (BlockLureTreeSapling) addBlock(new BlockLureTreeSapling());
        luretreeLog = (BlockModLog) addBlock(new BlockModLog(ModWoods.LURETREE));
        luretreeFace = (BlockLureTree) addBlock(new BlockLureTree());
        luretreeSapling.setLeaves(luretreeLeaves.getDefaultState()).setLog(luretreeLog.getDefaultState()).setBig(true);
        luretreeLeaves.setSapling(new ItemStack(luretreeSapling));

        mulberryLeaves = (BlockModLeaves) addBlock(new BlockModLeaves(ModWoods.MULBERRY));
        mulberrySapling = (BlockModSapling) addBlock(new BlockModSapling(ModWoods.MULBERRY));
        mulberryLog = (BlockModLog) addBlock(new BlockModLog(ModWoods.MULBERRY));
        mulberryPlanks = (BlockModPlanks) addBlock(new BlockModPlanks(ModWoods.MULBERRY));
        mulberrySapling.setLeaves(mulberryLeaves.getDefaultState()).setLog(mulberryLog.getDefaultState());
        mulberryLeaves.setSapling(new ItemStack(mulberrySapling));

        sakuraLeaves = (BlockModLeaves) addBlock(new BlockCherryLeaves(ModWoods.SAKURA));
        sakuraSapling = (BlockModSapling) addBlock(new BlockModSapling(ModWoods.SAKURA));
        sakuraLog = (BlockModLog) addBlock(new BlockModLog(ModWoods.SAKURA));
        sakuraPlanks = (BlockModPlanks) addBlock(new BlockModPlanks(ModWoods.SAKURA));
        sakuraSapling.setLeaves(sakuraLeaves.getDefaultState()).setLog(sakuraLog.getDefaultState()).setBig(true);
        sakuraLeaves.setSapling(new ItemStack(sakuraSapling));

        sakuraLeafPile = (BlockCherryLeafPile) addBlock(new BlockCherryLeafPile());
        rush = (BlockCropRush) addBlock(new BlockCropRush());
        rice = (BlockCropRice) addBlock(new BlockCropRice());
        bamboo = (BlockBamboo) addBlock(new BlockBamboo());
        bambooSlats = (BlockSlat) addBlock(new BlockSlat());
        nettedScreen = (BlockNettedScreen) addBlock(new BlockNettedScreen());
        ironSand = (BlockIronSand) addBlock(new BlockIronSand());
        kera = (BlockKera) addBlock(new BlockKera());
        tatara = (BlockTatara) addBlock(new BlockTatara());
        cherrybox = (BlockCherryBox) addBlock(new BlockCherryBox());
        shoji = (BlockModPane) addBlock(new BlockModPane("shoji", Material.WOOD).setHardness(1.0f));
        fusuma = (BlockFusumaPainted) addBlock(new BlockFusumaPainted("fusuma").setHardness(1.0f));
        //fusuma2 = (BlockFusumaPainted) addBlock(new BlockFusumaPainted("fusuma2", 1).setHardness(1.0f));
        tatami = (BlockTatami) addBlock(new BlockTatami().setHardness(1.0f));

        /*FusumaPicture.addPicture(new FusumaPicture(0).withSubblock(fusuma, 0));
        FusumaPicture.addPicture(new FusumaPicture(1).withSubblock(fusuma, 2).withSubblock(fusuma, 1));
        FusumaPicture.addPicture(new FusumaPicture(2).withSubblock(fusuma, 4).withSubblock(fusuma, 3));
        FusumaPicture.addPicture(new FusumaPicture(3).withSubblock(fusuma, 5));
        FusumaPicture.addPicture(new FusumaPicture(4).withSubblock(fusuma, 6));
        FusumaPicture.addPicture(new FusumaPicture(5).withSubblock(fusuma, 7));
        FusumaPicture.addPicture(new FusumaPicture(6).withSubblock(fusuma, 10).withSubblock(fusuma, 8));
        FusumaPicture.addPicture(new FusumaPicture(7).withSubblock(fusuma, 11).withSubblock(fusuma, 9));
        FusumaPicture.addPicture(new FusumaPicture(8).withSubblock(fusuma, 12));
        FusumaPicture.addPicture(new FusumaPicture(9).withSubblock(fusuma, 13));
        FusumaPicture.addPicture(new FusumaPicture(10).withSubblock(fusuma, 14));
        FusumaPicture.addPicture(new FusumaPicture(11).withSubblock(fusuma2, 1).withSubblock(fusuma, 15));
        FusumaPicture.addPicture(new FusumaPicture(12).withSubblock(fusuma2, 2).withSubblock(fusuma2, 0));
        FusumaPicture.addPicture(new FusumaPicture(13).withSubblock(fusuma2, 5).withSubblock(fusuma2, 3));
        FusumaPicture.addPicture(new FusumaPicture(14).withSubblock(fusuma2, 6).withSubblock(fusuma2, 4));*/

        connectPanes(shoji, fusuma);
        //connectPanes(shoji, fusuma2);
        //connectPanes(fusuma, fusuma2);

        wetSoap = (BlockSoap) addBlock(new BlockSoap());

        chandelier = (BlockChandelier) addBlock(new BlockChandelier().setLightLevel(0.9375F));
        paperWall = (BlockModPane) addBlock(new BlockModPane("paper_wall", Material.WOOD).setHardness(1.0f));
        wroughtBars = (BlockModPane) addBlock(new BlockModPane("wrought_bars", Material.IRON).setHardness(5.0f));
        paperLantern = (BlockLantern) addBlock(new BlockLantern("wood_lamp", Material.WOOD).setHardness(1.0f));
        wroughtLantern = (BlockLantern) addBlock(new BlockLantern("wrought_lamp", Material.IRON).setHardness(5.0f));
        pavement = (BlockPavement) addBlock(new BlockPavement());
        whiteBrick = (BlockWhiteBrick) addBlock(new BlockWhiteBrick());

        coloredBrick = (BlockColoredBrick) addBlock(new BlockColoredBrick(),ItemCloth.class,true);
    }

    protected static Block register(Block block)
    {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlockMeta(block).setRegistryName(block.getRegistryName()));

        return block;
    }

    protected static Block register(Block block, Class<? extends ItemBlock> itemBlock, boolean hasSubtypes)
    {
        GameRegistry.register(block);
        try {
            GameRegistry.register(itemBlock.getConstructor(Block.class).newInstance(block).setRegistryName(block.getRegistryName()).setHasSubtypes(hasSubtypes));
        } catch (Exception e) {
            System.out.println("Error Registering ItemBlock for " + block.getRegistryName());
            e.printStackTrace();
        }

        return block;
    }

    private static void connectPanes(BlockModPane pane1, BlockModPane pane2) {
        pane1.addCompatiblePane(pane2);
        pane2.addCompatiblePane(pane1);
    }

    private static Block addBlock(Block block) {
        LIST.add(block);
        register(block);

        return block;
    }

    private static Block addBlock(Block block, Class<? extends ItemBlock> itemBlock, boolean hasSubtypes) {
        LIST.add(block);
        register(block,itemBlock,hasSubtypes);

        return block;
    }
}