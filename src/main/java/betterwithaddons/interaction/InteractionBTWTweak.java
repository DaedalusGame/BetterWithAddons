package betterwithaddons.interaction;

import betterwithaddons.handler.StumpingHandler;
import betterwithaddons.util.BlockMeta;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

import java.util.Arrays;
import java.util.List;

public class InteractionBTWTweak implements IInteraction {
    public static boolean ENABLED = true;
    public static boolean HARD_STUMPS = true;
    public static boolean SOFT_WOODS = true;

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
    }

    @Override
    public List<IInteraction> getDependencies() {
        return Arrays.asList(new IInteraction[]{ ModInteractions.bwm });
    }

    @Override
    public List<IInteraction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        if(HARD_STUMPS || SOFT_WOODS)
            MinecraftForge.EVENT_BUS.register(new StumpingHandler());
    }

    @Override
    public void init() {
        if(SOFT_WOODS)
        {
            StumpingHandler.addSoftWood(Blocks.LOG,BlockPlanks.EnumType.SPRUCE.getMetadata(),1.3f);
            StumpingHandler.addSoftWood(Blocks.LOG,BlockPlanks.EnumType.JUNGLE.getMetadata(),1.0f);
            StumpingHandler.addSoftWood(Blocks.LOG2,BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4,1.3f);
        }
    }

    @Override
    public void postInit() {

    }
}
