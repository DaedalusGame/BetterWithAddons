package betterwithaddons.client.gui;

import betterwithaddons.lib.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Christian on 28.09.2016.
 */
public class GuiDryingBox extends GuiCherryBox {
    public GuiDryingBox(EntityPlayer player, World world, int x, int y, int z) {
        super(player,world,x,y,z);
    }

    @Override
    public ResourceLocation getGuiLocation()
    {
        return new ResourceLocation(Reference.MOD_ID,"textures/gui/dryingbox.png");
    }

    @Override
    public String getDisplayName()
    {
        return "tile.cherrybox.drying.name";
    }
}
