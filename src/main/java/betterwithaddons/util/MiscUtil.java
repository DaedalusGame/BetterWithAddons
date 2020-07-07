package betterwithaddons.util;

import betterwithmods.common.BWMBlocks;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockLight;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MiscUtil {
    public static boolean hasLitLight(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() instanceof BlockLight && state.getValue(BlockLight.ACTIVE);
    }

    public static boolean hasPadding(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == BWMBlocks.AESTHETIC && state.getValue(BlockAesthetic.TYPE).equals(BlockAesthetic.EnumType.PADDING);
    }

    public static double lerpAngle(double a, double b, double slide) {
        return a + angleDistance(a,b)*slide;
    }

    public static double angleDistance(double a, double b) {
        double max = 360;
        double da = (b - a) % max;
        return 2*da % max - da;
    }

    public static <T> List<List<T>> splitIntoBoxes(List<T> stacks, int boxes) {
        ArrayList<List<T>> splitStacks = new ArrayList<>();
        for (int i = 0; i < boxes; i++) {
            final int finalI = i;
            splitStacks.add(IntStream.range(0, stacks.size()).filter(index -> index % boxes == finalI).mapToObj(stacks::get).collect(Collectors.toList()));
        }
        return splitStacks;
    }
}
