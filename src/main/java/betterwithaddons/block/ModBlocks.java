package betterwithaddons.block;

import betterwithaddons.block.BetterRedstone.BlockPCB;
import betterwithaddons.block.BetterRedstone.BlockWirePCB;
import betterwithaddons.block.EriottoMod.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;

public class ModBlocks
{
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


    public static void load(FMLPreInitializationEvent event) {
        bannerDetector = (BlockBannerDetector) addBlock(new BlockBannerDetector());
        worldScale = (BlockWorldScale) addBlock(new BlockWorldScale());
        worldScaleOre = (BlockWorldScaleOre) addBlock(new BlockWorldScaleOre());
        worldScaleActive = (BlockWorldScaleActive) addBlock(new BlockWorldScaleActive());
        elytraMagma = (BlockElytraMagma) addBlock(new BlockElytraMagma());
        grass = (BlockExtraGrass) addBlock( new BlockExtraGrass());
        pcbwire = (BlockWirePCB) addBlock(new BlockWirePCB());
        pcbblock = (BlockPCB) addBlock(new BlockPCB());
        lattice = (BlockLattice) addBlock(new BlockLattice());
        //alchDragon = (BlockAlchDragon) addBlock(new BlockAlchDragon());

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

        coloredBrick = (BlockColoredBrick) addBlock(new BlockColoredBrick());
    }

    private static Block addBlock(Block block)
    {
        LIST.add(block);

        return block;
    }
}