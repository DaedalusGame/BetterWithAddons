package betterwithaddons.crafting.recipes;

import betterwithaddons.block.AdobeType;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.registry.OreStack;
import betterwithmods.common.registry.bulk.recipes.CauldronRecipe;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdobeRecipe extends CauldronRecipe {
    public AdobeRecipe() {
        super(ItemStack.EMPTY, ItemStack.EMPTY);
    }

    @Override
    public boolean matches(ItemStackHandler inv) {
        int clay = 0;
        int sand = 0;

        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            clay += getClay(stack);
            sand += getSand(stack);
        }

        return clay > 0 && sand > 0;
    }

    private int getSand(ItemStack stack) {
        if (ItemUtil.matchesOreDict(stack, "pileSand"))
            return stack.getCount();
        else if (ItemUtil.matchesOreDict(stack, "sand"))
            return 4 * stack.getCount();

        return 0;
    }

    private int getClay(ItemStack stack) {
        if (stack.getItem() == Items.CLAY_BALL)
            return stack.getCount();
        else if (Block.getBlockFromItem(stack.getItem()) == Blocks.CLAY)
            return 4 * stack.getCount();

        return 0;
    }

    private int getHay(ItemStack stack) {
        if (ItemUtil.matchesOreDict(stack, "hay") || ItemUtil.matchesOreDict(stack, "straw") || ItemUtil.matchesOreDict(stack, "cropWheat") || ItemUtil.matchesOreDict(stack, "cropBarley") || ItemUtil.matchesOreDict(stack, "cropRye"))
            return stack.getCount();
        if (stack.getItem() == Item.getItemFromBlock(Blocks.HAY_BLOCK))
            return 9 * stack.getCount();

        return 0;
    }

    private int getDung(ItemStack stack) {
        if (ItemUtil.matchesOreDict(stack, "dung"))
            return stack.getCount();
        else if (ItemUtil.matchesOreDict(stack, "blockDung"))
            return 9 * stack.getCount();

        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ArrayList<Object> getRecipeInput() {
        return Lists.newArrayList(new ItemStack(Items.CLAY_BALL), new OreStack("sand", 1), new OreStack("dung", 1), new OreStack("cropWheat", 1));
    }

    @Nonnull
    @Override
    public ItemStack getOutput() {
        ItemStack output = new ItemStack(ModBlocks.adobe, 1, 0);

        output.setTranslatableName("tooltip.adobe.crafting");

        return output;
    }

    @Nonnull
    @Override
    public ArrayList<List<ItemStack>> getInputs() {
        ArrayList<ItemStack> clayStacks = new ArrayList<>();
        ArrayList<ItemStack> sandStacks = new ArrayList<>();
        ArrayList<ItemStack> dungStacks = new ArrayList<>();
        ArrayList<ItemStack> hayStacks = new ArrayList<>();

        clayStacks.add(new ItemStack(Items.CLAY_BALL));
        clayStacks.add(new ItemStack(Blocks.CLAY));
        addOreStacks(sandStacks, "pileSand");
        addOreStacks(sandStacks, "sand");
        addOreStacks(dungStacks, "dung");
        addOreStacks(dungStacks, "blockDung");
        addOreStacks(hayStacks, "hay");
        addOreStacks(hayStacks, "straw");
        addOreStacks(hayStacks, "cropWheat");
        addOreStacks(hayStacks, "cropBarley");
        addOreStacks(hayStacks, "cropRye");

        return Lists.newArrayList(clayStacks,sandStacks,dungStacks,hayStacks);
    }

    private void addOreStacks(List<ItemStack> list, String ore)
    {
        NonNullList<ItemStack> ores = OreDictionary.getOres(ore,false);
        list.addAll(ores.stream().collect(Collectors.toList()));
    }

    @Override
    public List<ItemStack> getOutputs() {
        return Lists.newArrayList(getOutput());
    }

    @Override
    public boolean consumeInvIngredients(ItemStackHandler inv) {
        int clay = 0;
        int sand = 0;

        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            clay += getClay(stack);
            sand += getSand(stack);

            if (getClay(stack) > 0 || getSand(stack) > 0 || getDung(stack) > 0 || getHay(stack) > 0) {
                ItemStack container = stack.getItem().getContainerItem(stack);
                container.setCount(stack.getCount());
                inv.setStackInSlot(i,container);
            }
        }

        return clay > 0 && sand > 0;
    }

    private ArrayList<ItemStack> getMixedResult(ItemStackHandler inv) {
        int clay = 0;
        int sand = 0;
        int dung = 0;
        int wheat = 0;

        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            clay += getClay(stack);
            sand += getSand(stack);
            dung += getDung(stack);
            wheat += getHay(stack);
        }

        int total = clay + sand + dung + wheat;
        float clayPart = clay / (float) total;
        float sandPart = sand / (float) total;
        float dungPart = dung / (float) total;
        float wheatPart = wheat / (float) total;

        AdobeType type;

        if (clayPart > 0.5 && clayPart > sandPart + dungPart + wheatPart)
            type = AdobeType.MOSTLY_CLAY;
        else if (sandPart > 0.5 && sandPart > clayPart + dungPart + wheatPart)
            type = AdobeType.MOSTLY_SAND;
        else if (dungPart > 0.5)
            type = AdobeType.MOSTLY_DUNG;
        else if (wheatPart > 0.5)
            type = AdobeType.MOSTLY_STRAW;
        else if (dungPart + wheatPart < 0.1) {
            if (clayPart > sandPart)
                type = AdobeType.CLAYSAND;
            else
                type = AdobeType.SANDCLAY;
        }
        else if (sandPart > dungPart + wheatPart)
            type = AdobeType.LIGHT;
        else
            type = AdobeType.DARK;

        ArrayList<ItemStack> stacks = new ArrayList<>();

        for(int i = total/4; i > 0; i -= 64)
            stacks.add(type.getBlockStack(Math.min(i,64),true));

        return stacks;
    }

    @Override
    public NonNullList<ItemStack> onCraft(World world, TileEntity tile, ItemStackHandler inv) {
        NonNullList<ItemStack> list = NonNullList.create();
        for(ItemStack stack : getMixedResult(inv))
            list.add(0, stack);
        this.consumeInvIngredients(inv);
        return list;
    }
}
