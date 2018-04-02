package betterwithaddons.client;

import betterwithaddons.config.ModConfiguration;
import betterwithaddons.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {
        // NO-OP
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new BWAGuiConfig(parentScreen);
    }


    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }


    public static class BWAGuiConfig extends GuiConfig {

        public BWAGuiConfig(GuiScreen parentScreen) {
            super(parentScreen, getAllElements(), Reference.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ModConfiguration.configuration.toString()));
        }

        public static List<IConfigElement> getAllElements() {
            List<IConfigElement> list = new ArrayList<>();

            Set<String> categories = ModConfiguration.configuration.getCategoryNames();
            list.addAll(categories.stream().filter(s -> !s.contains(".")).map(s -> new DummyConfigElement.DummyCategoryElement(s, s, new ConfigElement(ModConfiguration.configuration.getCategory(s)).getChildElements())).collect(Collectors.toList()));

            return list;
        }
    }

}
