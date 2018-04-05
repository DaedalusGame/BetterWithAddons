package betterwithaddons.util;

import betterwithmods.common.registry.block.recipe.BlockIngredient;
import betterwithmods.util.InvUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MappedBlockIngredient extends BlockIngredient {
    private NonNullList<ItemStack> stacks;
    private IBlockMatcher matcher;

    public MappedBlockIngredient(IBlockMatcher matcher,String ore) {
        super(ore);
        this.stacks = OreDictionary.getOres(ore);
        this.matcher = matcher;
    }

    public MappedBlockIngredient(IBlockMatcher matcher,List<ItemStack> stacks) {
        super(stacks);
        this.stacks = InvUtils.asNonnullList(stacks);
        this.matcher = matcher;
    }

    public MappedBlockIngredient(IBlockMatcher matcher,ItemStack... stacks) {
        super(stacks);
        this.stacks = InvUtils.asNonnullList(stacks);
        this.matcher = matcher;
    }

    public MappedBlockIngredient(IBlockMatcher matcher,Ingredient ingredient) {
        super(ingredient);
        this.stacks = InvUtils.asNonnullList(ingredient.getMatchingStacks());
        this.matcher = matcher;
    }

    @Nonnull
    @Override
    public ItemStack[] getMatchingStacks() {
        return stacks.toArray(new ItemStack[stacks.size()]);
    }

    @Override
    public boolean apply(World world, BlockPos pos, @Nullable IBlockState state) {
        return matcher.apply(world, pos, state);
    }
}
