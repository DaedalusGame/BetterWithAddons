package betterwithaddons.client;

import betterwithaddons.block.Factorization.BlockBrine;
import betterwithaddons.lib.Reference;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public class BrineStateMapper extends StateMapperBase {
    public final ModelResourceLocation waterLocation;
    public final ModelResourceLocation brineLocation;
    public final ModelResourceLocation salinatedBrineLocation;

    public BrineStateMapper() {
        this.waterLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "brine"), "water");
        this.brineLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "brine"), "brine");
        this.salinatedBrineLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "brine"), "salinated_brine");
    }

    @Nonnull
    @Override
    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
        switch(state.getValue(BlockBrine.STATE))
        {
            case(0):return waterLocation;
            case(1):return brineLocation;
            case(2):return salinatedBrineLocation;
            default:return null;
        }
    }
}
