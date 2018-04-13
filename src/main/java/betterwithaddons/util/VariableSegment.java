package betterwithaddons.util;

import betterwithmods.manual.client.manual.segment.Segment;
import betterwithmods.manual.client.manual.segment.TextSegment;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Supplier;

public class VariableSegment extends TextSegment {
    public static HashMap<String,Supplier<String>> VARIABLE_SUPPLIERS = new HashMap<>();

    public static void addVariableSupplier(ResourceLocation location, Supplier<String> supplier) {
        VARIABLE_SUPPLIERS.put(location.toString(),supplier);
    }

    public static void addVariableSupplierLocalized(ResourceLocation location, Supplier<Object> supplier, String translationKey) {
        VARIABLE_SUPPLIERS.put(location.toString(),() -> I18n.format(translationKey,supplier.get()));
    }

    public static String getVariable(String location) {
        Supplier<String> supplier = VARIABLE_SUPPLIERS.get(location);
        return supplier != null ? supplier.get() : location;
    }

    public VariableSegment(@Nullable Segment parent, String location) {
        super(parent, getVariable(location));
    }
}
