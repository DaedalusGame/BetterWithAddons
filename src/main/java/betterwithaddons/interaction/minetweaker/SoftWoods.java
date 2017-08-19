package betterwithaddons.interaction.minetweaker;

import betterwithaddons.handler.StumpingHandler;
import betterwithaddons.handler.StumpingHandler.WoodHardness;
import betterwithaddons.interaction.InteractionCraftTweaker;
import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import com.google.common.collect.Lists;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.NotNull;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass(SoftWoods.clazz)
public class SoftWoods {
    public static final String clazz = "mods.betterwithaddons.SoftWoods";

    @ZenMethod
    public static void add(@NotNull IItemStack input, float hardness) {
        if(!InputHelper.isABlock(input))
        {
            CraftTweakerAPI.logError("Could not add soft wood "+input.toString()+"; not recognized as a block.");
            return;
        }
        ItemStack stack = InputHelper.toStack(input);
        WoodHardness r = new WoodHardness(Block.getBlockFromItem(stack.getItem()),stack.getMetadata(),hardness);
        InteractionCraftTweaker.LATE_ADDITIONS.add(new Add(r));
    }

    @ZenMethod
    public static void remove(@NotNull IItemStack input)
    {
        if(!InputHelper.isABlock(input))
        {
            CraftTweakerAPI.logError("Could not remove soft wood "+input.toString()+"; not recognized as a block.");
            return;
        }
        ItemStack stack = InputHelper.toStack(input);
        InteractionCraftTweaker.LATE_REMOVALS.add(new Remove(Block.getBlockFromItem(stack.getItem()),stack.getMetadata()));
    }

    public static class Add extends BaseListAddition<WoodHardness>
    {
        public Add(WoodHardness softWood) {
            super("SoftWoods", StumpingHandler.getSoftWoods(), Lists.newArrayList(softWood));
        }

        @Override
        protected String getRecipeInfo(WoodHardness recipe) {
            return recipe.getBlock() + "@" + recipe.getMeta();
        }
    }

    public static class Remove extends BaseListRemoval<WoodHardness>
    {
        protected Remove(Block block, int meta) {
            super("SoftWoods", StumpingHandler.getSoftWoods(), Lists.newArrayList(StumpingHandler.getSoftWood(block,meta)));
        }

        @Override
        protected String getRecipeInfo(WoodHardness recipe) {
            return recipe.getBlock() + "@" + recipe.getMeta();
        }
    }
}
