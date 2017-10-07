package betterwithaddons.util;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResourceProxy extends AbstractResourcePack {

    private static final String MINECRAFT = "minecraft";
    private static final Set<String> RESOURCE_DOMAINS = ImmutableSet.of(MINECRAFT,Reference.MOD_ID);

    private static final String BARE_FORMAT = "assets/%s/%s/%s/%s.%s";
    private static final String OVERRIDE_FORMAT = "/assets/%s/%s/%s/overrides/%s.%s";

    private static final Map<String, String> overrides = new HashMap();

    public ResourceProxy() {
        super(Loader.instance().activeModContainer().getSource());
        overrides.put("pack.mcmeta", "/proxypack.mcmeta");
    }

    public void addResource(String space, String dir, String file, String ext) {
        addResource(space,MINECRAFT,dir,file,ext);
    }

    public void addResource(String space, String modid, String dir, String file, String ext) {
        String bare = String.format(BARE_FORMAT, modid, space, dir, file, ext);
        String override = String.format(OVERRIDE_FORMAT, Reference.MOD_ID, space, dir, file, ext);
        overrides.put(bare, override);
    }

    @Override
    public Set<String> getResourceDomains() {
        return RESOURCE_DOMAINS;
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        if (name == null)
            return null;

        InputStream stream = BetterWithAddons.class.getResourceAsStream(overrides.get(name));

        return stream;
    }

    @Override
    protected boolean hasResourceName(String name) {
        if(overrides.containsKey(name))
        {
            System.out.write(1);
        }

        return overrides.containsKey(name);
    }

    @Override
    protected void logNameNotLowercase(String name) {
        // NO-OP
    }

    @Override
    public String getPackName() {
        return "bwa-texture-proxy";
    }

}