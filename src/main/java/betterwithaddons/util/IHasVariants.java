package betterwithaddons.util;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import java.util.List;

/**
 * Created by Christian on 17.09.2016.
 */
public interface IHasVariants {
    List<ModelResourceLocation> getVariantModels();

    String getVariantName(int meta);
}
