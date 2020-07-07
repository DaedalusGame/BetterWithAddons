package betterwithaddons.crafting.recipes;

import betterwithaddons.item.ItemTea;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityNabe;
import betterwithaddons.util.ItemUtil;
import betterwithaddons.util.NabeResult;
import betterwithaddons.util.NabeResultTea;
import betterwithaddons.util.TeaType;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class TeaNabeRecipe implements INabeRecipe {
    public static final ResourceLocation RESOURCE_LOCATION = new ResourceLocation(Reference.MOD_ID, "tea");
    public static boolean ENABLE_CEREMONIAL_RECIPE = true;

    public static final HashMap<Potion,Potion> OPPOSITES = new HashMap<>();
    public static Ingredient SUGAR = Ingredient.fromItems(Items.SUGAR);
    public static Ingredient MILK = Ingredient.fromItems(Items.MILK_BUCKET);

    static {
        addOpposite(MobEffects.INSTANT_HEALTH,MobEffects.INSTANT_DAMAGE);
        addOpposite(MobEffects.REGENERATION,MobEffects.POISON);
        addOpposite(MobEffects.NIGHT_VISION,MobEffects.BLINDNESS);
        addOpposite(MobEffects.SATURATION,MobEffects.HUNGER);
        addOpposite(MobEffects.SPEED,MobEffects.SLOWNESS);
        addOpposite(MobEffects.STRENGTH,MobEffects.WEAKNESS);
        addOpposite(MobEffects.INVISIBILITY,MobEffects.GLOWING);
        addOpposite(MobEffects.LUCK,MobEffects.UNLUCK);
    }

    public static void addOpposite(Potion a, Potion b)
    {
        OPPOSITES.put(a,b);
        OPPOSITES.put(b,a);
    }

    @Override
    public int getBoilingTime(TileEntityNabe tile) {
        return 1000;
    }

    @Override
    public boolean isValidItem(ItemStack stack) {
        return stack.getItem() instanceof ItemTea || isMilk(stack) || isSugar(stack);
    }

    private boolean isSugar(ItemStack stack) {
        return SUGAR.apply(stack);
    }

    private boolean isMilk(ItemStack stack)
    {
        return MILK.apply(stack);
    }

    @Override
    public boolean matches(TileEntityNabe tile, List<ItemStack> stacks) {
        int itemCount = tile.countIngredients();
        boolean hasTea = stacks.stream().anyMatch(stack -> stack.getItem() instanceof ItemTea && stack.getCount() >= 2);
        return itemCount >= 3 && hasTea;
    }

    @Override
    public NabeResult craft(TileEntityNabe tile, List<ItemStack> stacks) {
        ItemStack mainTea = ItemStack.EMPTY;
        ItemStack secondaryTea = ItemStack.EMPTY;
        int milk = 0;
        int sugar = 0;
        int totalAmount = 0;
        for (ItemStack stack : stacks) {
            Item item = stack.getItem();
            int count = stack.getCount();
            if (item instanceof ItemTea) {
                if (mainTea == null || mainTea.getCount() < count) {
                    secondaryTea = mainTea;
                    mainTea = stack;
                } else if (secondaryTea == null || secondaryTea.getCount() < count) {
                    secondaryTea = stack;
                }
            }
            if (isMilk(stack))
                milk += count;
            if (isSugar(stack))
                sugar += count;
            totalAmount += count;
        }
        TeaType mainType = ItemTea.getType(mainTea);
        TeaType secondaryType = secondaryTea.isEmpty() ? null : ItemTea.getType(secondaryTea);
        boolean isCeremonial = mainTea.getCount() == 6 && mainType == TeaType.MATCHA; //Matcha is special and gives random effects but only positive
        int strength = Math.max(getStrength(mainTea),getStrength(secondaryTea));
        Color color = new Color(mainType.getTypeColor(TeaType.ItemType.Powder));
        if (milk > 1)
            color.brighter();
        if (sugar > 1)
            color.darker();
        NabeResultTea result = new NabeResultTea(color.getRGB(), totalAmount, totalAmount);
        result.mainType = mainType;
        result.sugar = sugar;
        result.milk = milk;
        result.strength = strength;
        if (isCeremonial) {
            result.isCeremonial = true;
            Random random = tile.random;
            ArrayList<PotionEffect> positives = new ArrayList<>(mainType.getPositiveEffects());
            ArrayList<PotionEffect> negatives = new ArrayList<>(mainType.getNegativeEffects());
            for (int i = 0; i < 3; i++) //Pick three good effects
            {
                PotionEffect effect = positives.get(random.nextInt(positives.size()));
                result.effects.add(effect);
                positives.remove(effect);
            }
            result.effects.add(negatives.get(random.nextInt(negatives.size()))); //Pick one negative effect
        } else {
            ArrayList<PotionEffect> effects = new ArrayList<>(mainType.getNegativeEffects(ItemTea.getItemType(mainTea)));
            if (secondaryType != null)
                effects.addAll(secondaryType.getNegativeEffects(ItemTea.getItemType(secondaryTea)));
            while(milk > 0 && effects.size() > 0) { //Milk removes negative effects
                effects.remove(0);
                milk--;
            }
            effects.addAll(mainType.getPositiveEffects(ItemTea.getItemType(mainTea)));
            if (secondaryType != null)
                effects.addAll(secondaryType.getPositiveEffects(ItemTea.getItemType(secondaryTea)));
            for(int i = 0; i < effects.size(); i++){
                PotionEffect effect = effects.get(i);
                effect = extendEffect(effect,duration -> (duration * strength) / 2);
                if(sugar > 0) { //Sugar powers up positive effects
                    effect = powerUpEffect(effect, existing -> existing.getAmplifier() < 2 && !existing.getPotion().isBadEffect() ? existing.getAmplifier() + 1 : existing.getAmplifier());
                    sugar--;
                }
                effects.set(i,effect);
            }
            //Pop Quiz, what the fuck
            for (PotionEffect effect : effects) {
                Optional<PotionEffect> existing = result.effects.stream().filter(x -> x.getPotion() == effect.getPotion() || isOpposite(x.getPotion(),effect.getPotion())).findFirst();
                if (existing.isPresent()) {
                    PotionEffect other = existing.get();
                    if(isOpposite(effect.getPotion(),other.getPotion())) //If we find an opposite, we overwrite. It's guaranteed that it was a negative effect.
                    {
                        result.effects.remove(other);
                        result.effects.add(effect);
                    }
                    else
                        other.combine(effect);
                }
                else
                    result.effects.add(effect);
            }
        }
        ItemStackHandler inventory = tile.inventory;
        for(int i = 0; i < inventory.getSlots(); i++)
        {
            ItemStack stack = inventory.getStackInSlot(i);
            if(isValidItem(stack))
            {
                stack = tile.consumeItem(stack);
                inventory.setStackInSlot(i,stack);
            }
        }
        return result;
    }

    private PotionEffect extendEffect(PotionEffect effect, Function<Integer,Integer> transform)
    {
        PotionEffect newEffect = new PotionEffect(effect.getPotion(),transform.apply(effect.getDuration()),effect.getAmplifier(), effect.getIsAmbient(),effect.doesShowParticles());
        newEffect.setCurativeItems(effect.getCurativeItems());
        return newEffect;
    }

    private PotionEffect powerUpEffect(PotionEffect effect, Function<PotionEffect,Integer> transform)
    {
        PotionEffect newEffect = new PotionEffect(effect.getPotion(),effect.getDuration(),transform.apply(effect), effect.getIsAmbient(),effect.doesShowParticles());
        newEffect.setCurativeItems(effect.getCurativeItems());
        return newEffect;
    }

    private int getStrength(ItemStack tea)
    {
        if(tea.isEmpty())
            return 0;

        switch (ItemTea.getItemType(tea))
        {
            case Leaves:
                return (int)Math.round(MathHelper.clampedLerp(0,3,tea.getCount() / 6.0));
            case Soaked:
            case Wilted:
                return (int)Math.round(MathHelper.clampedLerp(0,4,tea.getCount() / 5.0));
            case Powder:
                return (int)Math.round(MathHelper.clampedLerp(0,4,tea.getCount() / 4.0));
        }

        return 0;
    }

    //TODO: Config
    private static boolean isOpposite(Potion a, Potion b) {
        return OPPOSITES.get(a) == b || OPPOSITES.get(b) == a;
    }

    @Override
    public ResourceLocation getName() {
        return RESOURCE_LOCATION;
    }

    @Override
    public int getPriority() {
        return 100; //Always do this one first of all
    }
}
