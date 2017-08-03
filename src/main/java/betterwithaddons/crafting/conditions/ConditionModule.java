package betterwithaddons.crafting.conditions;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.HashMap;
import java.util.function.BooleanSupplier;

public class ConditionModule implements IConditionFactory {
    public static HashMap<String,BooleanSupplier> MODULES = new HashMap<>();

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        String type = json.get("type").getAsString();

        if(type.equals("betterwithaddons:module"))
        {
            String module = json.get("module").getAsString();
            boolean invert = module.startsWith("!");
            if(invert) module = module.substring(1);

            final String finalModule = module;
            return () -> invert != (MODULES.containsKey(finalModule) ? MODULES.get(finalModule).getAsBoolean() : false);
        }

        return () -> false;
    }
}
