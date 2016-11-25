package betterwithaddons.util;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;

import java.util.List;

public interface IHasVariants {
    List<ModelResourceLocation> getVariantModels();

    String getVariantName(int meta);
}
