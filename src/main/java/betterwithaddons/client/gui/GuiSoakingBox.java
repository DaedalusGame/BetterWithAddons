package betterwithaddons.client.gui;

import betterwithaddons.container.ContainerTatara;
import betterwithaddons.lib.Reference;
import betterwithaddons.tileentity.TileEntityCherryBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Christian on 28.09.2016.
 */
public class GuiSoakingBox extends GuiCherryBox {
    public GuiSoakingBox(EntityPlayer player, World world, int x, int y, int z) {
        super(player,world,x,y,z);
    }

    @Override
    public ResourceLocation getGuiLocation()
    {
        return new ResourceLocation(Reference.MOD_ID,"textures/gui/soakingbox.png");
    }

    @Override
    public String getDisplayName()
    {
        return "tile.cherrybox.soaking.name";
    }
}
