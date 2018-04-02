package betterwithaddons.util;

import com.google.common.collect.HashBiMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

import java.util.HashMap;
import java.util.function.Supplier;

public class NabeResult {
    public static final HashMap<String,Supplier<NabeResult>> DESERIALIZER_REGISTRY = new HashMap<>();
    public static final HashMap<Class<? extends NabeResult>,String> SERIALIZER_REGISTRY = new HashMap<>();
    public static final int MAX_FLUID_FILL = 1000;

    public static void registerResultType(Class<? extends NabeResult> clazz, String name, Supplier<NabeResult> constructor) {
        SERIALIZER_REGISTRY.put(clazz,name);
        DESERIALIZER_REGISTRY.put(name,constructor);
    }

    static {
        registerResultType(NabeResult.class,"fluid",NabeResult::new);
        registerResultType(NabeResultTea.class,"tea",NabeResultTea::new);
        registerResultType(NabeResultPoison.class,"poison",NabeResultPoison::new);
    }

    FluidStack fluid;
    ResourceLocation texture;
    int color;

    protected NabeResult() {}

    public NabeResult(FluidStack fluid) {
        this.fluid = fluid;
    }

    public NabeResult(ResourceLocation texture, int color) {
        this.texture = texture;
        this.color = color;
    }

    public ResourceLocation getTexture() {
        return fluid != null ? fluid.getFluid().getStill(fluid) : texture;
    }

    public int getColor() {
        return fluid != null ? fluid.getFluid().getColor(fluid) : color;
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public float getFillRatio() {
        return fluid != null ? fluid.amount / (float) MAX_FLUID_FILL : 1.0f;
    }

    public boolean isFull() {
        return fluid != null && fluid.amount >= MAX_FLUID_FILL;
    }

    public ItemStack take(ItemStack container) {
        if(fluid != null)
        {
            IFluidHandlerItem handler = FluidUtil.getFluidHandler(container);
            if(handler != null) {
                int amount = handler.fill(fluid, true);
                fluid.amount -= amount;
                return handler.getContainer();
            }
        }
        return ItemStack.EMPTY;
    }

    public final NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("type",SERIALIZER_REGISTRY.get(this.getClass()));
        writeToNBT(compound);
        return compound;
    }

    public static NabeResult deserializeNBT(NBTTagCompound compound){
        if(DESERIALIZER_REGISTRY.containsKey(compound.getString("type"))) {
            NabeResult result =  DESERIALIZER_REGISTRY.get(compound.getString("type")).get();
            result.readFromNBT(compound);
            return result;
        }
        return new NabeResult();
    }

    public void writeToNBT(NBTTagCompound compound) {
        if(fluid != null)
            fluid.writeToNBT(compound);
        else
            compound.setInteger("color", color);
    }

    public void readFromNBT(NBTTagCompound compound) {
        color = compound.getInteger("color");
        fluid = FluidStack.loadFluidStackFromNBT(compound);
    }

    public NabeResult copy()
    {
        NabeResult result = new NabeResult();
        result.color = color;
        result.fluid = fluid.copy();
        result.texture = texture;
        return result;
    }
}
