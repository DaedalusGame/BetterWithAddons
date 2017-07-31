package betterwithaddons.handler;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockPlanter;
import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class PlantCrossbreedHandler {
    private static ArrayList<PlantMutation> mutationTree = new ArrayList<>();
    private static ArrayList<PlantMutation> mutationTreeFungi = new ArrayList<>();

    private static void addEvolution(PlantMutation parent, PlantMutation child, int weight) {
        NextMutation mutation = new NextMutation(child, weight);
        parent.addMutation(mutation);
    }

    private static void addEvolution(PlantMutation parent, PlantMutation child) {
        addEvolution(parent, child, 1);
        addEvolution(child, parent, 5);
    }

    public static void placeDirt(World world, BlockPos pos) {
        IBlockState soil = world.getBlockState(pos.down());
        Block block = soil.getBlock();

        if (block == BWMBlocks.FERTILE_FARMLAND)
            world.setBlockState(pos.down(), Blocks.FARMLAND.getDefaultState(), 3);
        else if (block == BWMBlocks.PLANTER && soil.getValue(BlockPlanter.TYPE) == BlockPlanter.EnumType.FERTILE)
            world.setBlockState(pos.down(), BWMBlocks.PLANTER.getDefaultState().withProperty(BlockPlanter.TYPE, BlockPlanter.EnumType.DIRT), 3);
    }

    public static void placeGrass(World world, BlockPos pos) {
        IBlockState soil = world.getBlockState(pos.down());
        Block block = soil.getBlock();

        if (block == BWMBlocks.FERTILE_FARMLAND || block == Blocks.DIRT || block == Blocks.FARMLAND)
            world.setBlockState(pos.down(), Blocks.GRASS.getDefaultState(), 3);
        else if (block == BWMBlocks.PLANTER && (soil.getValue(BlockPlanter.TYPE) == BlockPlanter.EnumType.FERTILE || soil.getValue(BlockPlanter.TYPE) == BlockPlanter.EnumType.DIRT))
            world.setBlockState(pos.down(), BWMBlocks.PLANTER.getDefaultState().withProperty(BlockPlanter.TYPE, BlockPlanter.EnumType.GRASS), 3);
    }

    public static void placeMycelium(World world, BlockPos pos) {
        IBlockState soil = world.getBlockState(pos.down());
        Block block = soil.getBlock();

        if (block == BWMBlocks.FERTILE_FARMLAND || block == Blocks.DIRT || block == Blocks.FARMLAND)
            world.setBlockState(pos.down(), Blocks.MYCELIUM.getDefaultState(), 3);
    }

    public static void placeFarmland(World world, BlockPos pos) {
        IBlockState soil = world.getBlockState(pos.down());
        Block block = soil.getBlock();

        if (block == BWMBlocks.FERTILE_FARMLAND)
            world.setBlockState(pos.down(), Blocks.FARMLAND.getDefaultState(), 3);
        else if (block == BWMBlocks.PLANTER && (soil.getValue(BlockPlanter.TYPE) == BlockPlanter.EnumType.FERTILE))
            world.setBlockState(pos.down(), BWMBlocks.PLANTER.getDefaultState().withProperty(BlockPlanter.TYPE, BlockPlanter.EnumType.DIRT), 3);
    }

    public static boolean placePlant(World world, BlockPos pos, Block block) {
        return placePlant(world, pos, block.getDefaultState());
    }

    public static boolean placePlant(World world, BlockPos pos, IBlockState state) {
        if (!state.getBlock().canPlaceBlockAt(world, pos)) {
            IBlockState soil = world.getBlockState(pos.down());
            Block block = soil.getBlock();
            //Try turning the block to dirt
            placeDirt(world, pos);
            if (!state.getBlock().canPlaceBlockAt(world, pos)) {
                world.setBlockState(pos.down(), soil, 3); //revert
                return false;
            }
        }

        world.setBlockState(pos, state);
        placeFarmland(world, pos);

        return true;
    }

    public static boolean matchTallPlant(World world, BlockPos pos, IBlockState state, BlockDoublePlant.EnumPlantType type) {
        return state.getBlock() == Blocks.DOUBLE_PLANT && state.getValue(BlockDoublePlant.VARIANT) == type;
    }

    public static boolean placeTallPlant(World world, BlockPos pos, BlockDoublePlant.EnumPlantType type) {
        if (!Blocks.DOUBLE_PLANT.canPlaceBlockAt(world, pos))
            return false;

        Blocks.DOUBLE_PLANT.placeAt(world, pos, type, 3);
        placeFarmland(world,pos);
        return true;
    }

    public static boolean placeCocoa(World world, BlockPos pos) {
        Random rand = world.rand;
        int initial = rand.nextInt(4);
        for (int i = 0; i < 4; i++) {
            EnumFacing facing = EnumFacing.getHorizontal((initial + i) % 4);
            IBlockState state = Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.FACING, facing);
            if (Blocks.COCOA instanceof BlockCocoa && ((BlockCocoa) Blocks.COCOA).canBlockStay(world, pos, state)) {
                world.setBlockState(pos, state);
                placeFarmland(world,pos);
                return true;
            }
        }
        return false;
    }

    public static boolean placeVines(World world, BlockPos pos) {
        IBlockState state = Blocks.VINE.getDefaultState();
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (Blocks.VINE.canPlaceBlockOnSide(world, pos, facing))
                state = state.withProperty(BlockVine.getPropertyFor(facing), true);
        }
        world.setBlockState(pos, state);
        placeFarmland(world,pos);
        return true;
    }

    public static void initialize() {
        IBlockState grassState = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
        IBlockState fernState = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
        IBlockState dandelionState = Blocks.YELLOW_FLOWER.getDefaultState().withProperty(Blocks.YELLOW_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.DANDELION);
        IBlockState poppyState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.POPPY);
        IBlockState oakState = Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.OAK);
        IBlockState spruceState = Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.SPRUCE);
        IBlockState birchState = Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.BIRCH);
        IBlockState jungleState = Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.JUNGLE);
        IBlockState acaciaState = Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.ACACIA);
        IBlockState darkoakState = Blocks.SAPLING.getDefaultState().withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.DARK_OAK);
        IBlockState blueOrchidState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.BLUE_ORCHID);
        IBlockState aliumState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.ALLIUM);
        IBlockState daisyState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.OXEYE_DAISY);
        IBlockState bluetState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.HOUSTONIA);
        IBlockState tulipRedState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.RED_TULIP);
        IBlockState tulipOrangeState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.ORANGE_TULIP);
        IBlockState tulipPinkState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.PINK_TULIP);
        IBlockState tulipWhiteState = Blocks.RED_FLOWER.getDefaultState().withProperty(Blocks.RED_FLOWER.getTypeProperty(), BlockFlower.EnumFlowerType.WHITE_TULIP);

        PlantMutation grass = new PlantMutation().setCropMatcher(grassState).setCropPlacer((world, pos) -> {
            if (placePlant(world, pos, grassState)) placeGrass(world, pos);
        });
        PlantMutation wheat = new PlantMutation().setCropMatcher(Blocks.WHEAT).setCropPlacer(Blocks.WHEAT);
        PlantMutation hemp = new PlantMutation().setCropMatcher(BWMBlocks.HEMP).setCropPlacer(BWMBlocks.HEMP);
        PlantMutation dandelion = new PlantMutation().setCropMatcher(dandelionState).setCropPlacer(dandelionState);

        PlantMutation pumpkin = new PlantMutation().setCropMatcher(Blocks.PUMPKIN_STEM).setCropPlacer(Blocks.PUMPKIN_STEM);
        PlantMutation melon = new PlantMutation().setCropMatcher(Blocks.MELON_STEM).setCropPlacer(Blocks.MELON_STEM);

        PlantMutation sugarcane = new PlantMutation().setCropMatcher(Blocks.REEDS).setCropPlacer(Blocks.REEDS);
        PlantMutation cactus = new PlantMutation().setCropMatcher(Blocks.CACTUS).setCropPlacer(Blocks.CACTUS);

        PlantMutation poppy = new PlantMutation().setCropMatcher(poppyState).setCropPlacer(poppyState);
        PlantMutation fern = new PlantMutation().setCropMatcher(fernState).setCropPlacer((world, pos) -> {
            if (placePlant(world, pos, fernState)) placeGrass(world, pos);
        });
        PlantMutation lilypad = new PlantMutation().setCropMatcher(Blocks.WATERLILY).setCropPlacer(Blocks.WATERLILY);
        PlantMutation vines = new PlantMutation().setCropMatcher(Blocks.VINE).setCropPlacer(PlantCrossbreedHandler::placeVines);
        PlantMutation cocoa = new PlantMutation().setCropMatcher(Blocks.COCOA).setCropPlacer(PlantCrossbreedHandler::placeCocoa);

        PlantMutation oak = new PlantMutation().setCropMatcher(oakState).setCropPlacer(oakState);
        PlantMutation spruce = new PlantMutation().setCropMatcher(spruceState).setCropPlacer(spruceState);
        PlantMutation birch = new PlantMutation().setCropMatcher(birchState).setCropPlacer(birchState);
        PlantMutation jungle = new PlantMutation().setCropMatcher(jungleState).setCropPlacer(jungleState);
        PlantMutation acacia = new PlantMutation().setCropMatcher(acaciaState).setCropPlacer(acaciaState);
        PlantMutation darkoak = new PlantMutation().setCropMatcher(darkoakState).setCropPlacer(darkoakState);

        PlantMutation carrots = new PlantMutation().setCropMatcher(Blocks.CARROTS).setCropPlacer(Blocks.CARROTS);
        PlantMutation potatoes = new PlantMutation().setCropMatcher(Blocks.POTATOES).setCropPlacer(Blocks.POTATOES);
        PlantMutation beets = new PlantMutation().setCropMatcher(Blocks.BEETROOTS).setCropPlacer(Blocks.BEETROOTS);

        PlantMutation bigGrass = new PlantMutation()
                .setCropMatcher((world, pos, state) -> matchTallPlant(world, pos, state, BlockDoublePlant.EnumPlantType.GRASS))
                .setCropPlacer((world, pos) -> {
                    if (placeTallPlant(world, pos, BlockDoublePlant.EnumPlantType.GRASS)) placeGrass(world, pos);
                });
        PlantMutation bigFern = new PlantMutation()
                .setCropMatcher((world, pos, state) -> matchTallPlant(world, pos, state, BlockDoublePlant.EnumPlantType.FERN))
                .setCropPlacer((world, pos) -> {
                    if (placeTallPlant(world, pos, BlockDoublePlant.EnumPlantType.FERN)) placeGrass(world, pos);
                });
        PlantMutation sunflower = new PlantMutation()
                .setCropMatcher((world, pos, state) -> matchTallPlant(world, pos, state, BlockDoublePlant.EnumPlantType.SUNFLOWER))
                .setCropPlacer((world, pos) -> placeTallPlant(world, pos, BlockDoublePlant.EnumPlantType.SUNFLOWER));
        PlantMutation peony = new PlantMutation()
                .setCropMatcher((world, pos, state) -> matchTallPlant(world, pos, state, BlockDoublePlant.EnumPlantType.PAEONIA))
                .setCropPlacer((world, pos) -> placeTallPlant(world, pos, BlockDoublePlant.EnumPlantType.PAEONIA));
        PlantMutation lilac = new PlantMutation()
                .setCropMatcher((world, pos, state) -> matchTallPlant(world, pos, state, BlockDoublePlant.EnumPlantType.SYRINGA))
                .setCropPlacer((world, pos) -> placeTallPlant(world, pos, BlockDoublePlant.EnumPlantType.SYRINGA));
        PlantMutation rose = new PlantMutation()
                .setCropMatcher((world, pos, state) -> matchTallPlant(world, pos, state, BlockDoublePlant.EnumPlantType.ROSE))
                .setCropPlacer((world, pos) -> placeTallPlant(world, pos, BlockDoublePlant.EnumPlantType.ROSE));

        PlantMutation blueOrchid = new PlantMutation().setCropMatcher(blueOrchidState).setCropPlacer(blueOrchidState);
        PlantMutation alium = new PlantMutation().setCropMatcher(aliumState).setCropPlacer(aliumState);
        PlantMutation daisy = new PlantMutation().setCropMatcher(daisyState).setCropPlacer(daisyState);
        PlantMutation bluet = new PlantMutation().setCropMatcher(bluetState).setCropPlacer(bluetState);
        PlantMutation tulipRed = new PlantMutation().setCropMatcher(tulipRedState).setCropPlacer(tulipRedState);
        PlantMutation tulipOrange = new PlantMutation().setCropMatcher(tulipOrangeState).setCropPlacer(tulipOrangeState);
        PlantMutation tulipPink = new PlantMutation().setCropMatcher(tulipPinkState).setCropPlacer(tulipPinkState);
        PlantMutation tulipWhite = new PlantMutation().setCropMatcher(tulipWhiteState).setCropPlacer(tulipWhiteState);

        mutationTree.addAll(Lists.newArrayList(grass, wheat, hemp, dandelion, pumpkin, melon, sugarcane, cactus, poppy, fern, lilypad, vines, cocoa, oak, spruce, birch, jungle, acacia, darkoak, carrots, potatoes, beets, bigGrass, bigFern, sunflower, peony, lilac, rose, blueOrchid, alium, daisy, bluet, tulipRed, tulipOrange, tulipPink, tulipWhite));
        addEvolution(grass, wheat);
        addEvolution(grass, hemp);
        addEvolution(grass, dandelion);
        addEvolution(hemp, pumpkin);
        addEvolution(pumpkin, melon);
        addEvolution(hemp, sugarcane);
        addEvolution(sugarcane, cactus);
        addEvolution(dandelion, poppy);
        addEvolution(poppy, fern);
        addEvolution(fern, lilypad);
        addEvolution(fern, vines);
        addEvolution(vines, cocoa);
        addEvolution(dandelion, oak);
        addEvolution(oak, birch);
        addEvolution(birch, spruce);
        addEvolution(spruce, jungle);
        addEvolution(oak, darkoak);
        addEvolution(jungle, acacia);
        addEvolution(wheat, potatoes);
        addEvolution(potatoes, carrots);
        addEvolution(potatoes, beets);
        addEvolution(grass, bigGrass);
        addEvolution(fern, bigFern);
        addEvolution(dandelion, sunflower); //This is based in real botany what???
        addEvolution(bluet, peony);
        addEvolution(alium, lilac);
        addEvolution(dandelion, daisy);
        addEvolution(daisy, bluet);
        addEvolution(bluet, blueOrchid);
        addEvolution(blueOrchid, alium);
        addEvolution(poppy, rose);
        addEvolution(poppy, tulipRed);
        addEvolution(tulipRed, tulipOrange);
        addEvolution(tulipRed, tulipPink);
        addEvolution(tulipPink, tulipWhite);

        PlantMutation mushroom = new PlantMutation().setCropMatcher(Blocks.BROWN_MUSHROOM).setCropPlacer((world, pos) -> {
            if (placePlant(world, pos, Blocks.BROWN_MUSHROOM)) placeMycelium(world, pos);
        });
        PlantMutation amanita = new PlantMutation().setCropMatcher(Blocks.RED_MUSHROOM).setCropPlacer((world, pos) -> {
            if (placePlant(world, pos, Blocks.RED_MUSHROOM)) placeMycelium(world, pos);
        });
        PlantMutation netherwart = new PlantMutation().setCropMatcher(Blocks.NETHER_WART).setCropPlacer(Blocks.NETHER_WART);
        PlantMutation chorus = new PlantMutation().setCropMatcher(Blocks.CHORUS_FLOWER).setCropPlacer(Blocks.CHORUS_FLOWER);

        mutationTreeFungi.addAll(Lists.newArrayList(mushroom, amanita, netherwart, chorus));
        addEvolution(mushroom, amanita);
        addEvolution(amanita, netherwart);
        addEvolution(netherwart, chorus);
    }

    public interface ICropMatcher {
        boolean matches(World world, BlockPos pos, IBlockState state);
    }

    public interface ICropPlacer {
        void place(World world, BlockPos pos);
    }

    public static class NextMutation extends WeightedRandom.Item {
        PlantMutation mutation;

        public PlantMutation getMutation() {
            return mutation;
        }

        public NextMutation(PlantMutation mutation, int itemWeightIn) {
            super(itemWeightIn);
            this.mutation = mutation;
        }

        public NextMutation copy() {
            return new NextMutation(mutation, itemWeight);
        }

        public boolean add(NextMutation other) {
            if (mutation.equals(other.mutation)) {
                itemWeight += other.itemWeight;
                return true;
            }

            return false;
        }

        public void mergeInto(List<NextMutation> list) {
            boolean added = false;
            for (NextMutation other : list) {
                added |= other.add(this);
            }
            if (!added)
                list.add(this);
        }
    }

    public static class PlantMutation {
        ICropMatcher block;
        ICropPlacer createdBlock;
        ArrayList<NextMutation> nextMutations = new ArrayList<>();

        public PlantMutation() {
        }

        public PlantMutation setCropPlacer(Block block) {
            return setCropPlacer(block.getDefaultState());
        }

        public PlantMutation setCropPlacer(IBlockState state) {
            createdBlock = (world, pos) -> placePlant(world, pos, state);
            return this;
        }

        public PlantMutation setCropPlacer(ICropPlacer placer) {
            createdBlock = placer;
            return this;
        }

        public PlantMutation setCropMatcher(Block block) {
            this.block = (world, pos, state) -> block == state.getBlock();
            return this;
        }

        public PlantMutation setCropMatcher(IBlockState checkstate) {
            this.block = (world, pos, state) -> state.equals(checkstate);
            return this;
        }

        public PlantMutation setCropMatcher(ICropMatcher matcher) {
            block = matcher;
            return this;
        }

        public boolean matches(World world, BlockPos pos, IBlockState state) {
            return this.block.matches(world, pos, state);
        }

        public void grow(World world, BlockPos pos) {
            createdBlock.place(world, pos);
        }

        public void addMutation(NextMutation mutation) {
            mutation.mergeInto(nextMutations);
        }
    }

    public static PlantMutation getMutation(List<PlantMutation> tree, World world, BlockPos pos, IBlockState state) {
        for (PlantMutation plant : tree) {
            if (plant.matches(world, pos, state)) {
                return plant;
            }
        }

        return null;
    }

    @SubscribeEvent
    public void crossBreedPlants(RandomBlockTickEvent event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos().up();
        IBlockState state = event.getState();
        IBlockState soil = world.getBlockState(pos.down());
        Random random = event.getRandom();

        if (world.isRemote || !state.getBlock().isReplaceable(world, pos) || !isValidSoil(soil))
            return;

        List<PlantMutation> tree = mutationTree;
        int lightlevel = world.getLight(pos);
        if (lightlevel <= 7) //TODO: config
            tree = mutationTreeFungi;

        HashSet<PlantMutation> nearby = new HashSet<>();
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++) {
                BlockPos checkpos = pos.add(x, 0, z);
                PlantMutation plant = getMutation(tree, world, checkpos, world.getBlockState(checkpos));

                if (plant != null)
                    nearby.add(plant);
            }

        if (nearby.size() < 2)
            return;

        ArrayList<NextMutation> nextMutations = Lists.newArrayList(new NextMutation(mutationTree.get(0), 10));

        for (PlantMutation plant : nearby) {
            for (NextMutation mutation : plant.nextMutations) {
                mutation.mergeInto(nextMutations);
            }
        }

        NextMutation pickedMutation = WeightedRandom.getRandomItem(random, nextMutations);

        if (pickedMutation != null)
            pickedMutation.mutation.grow(world, pos);
    }

    private boolean isValidSoil(IBlockState state) {
        Block block = state.getBlock();

        return block == Blocks.SOUL_SAND || block == Blocks.END_STONE || block == BWMBlocks.FERTILE_FARMLAND || block == BWMBlocks.PLANTER;
    }
}
