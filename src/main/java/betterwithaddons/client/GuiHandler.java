package betterwithaddons.client;

import betterwithaddons.lib.GuiIds;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class GuiHandler implements IGuiHandler
{
    private enum GuiContainerConnection
    {
        BANNER_DETECTOR(GuiIds.BANNER_DETECTOR, "betterwithaddons.client.gui.GuiBannerDetector", "betterwithaddons.container.ContainerBannerDetector"),
        TATARA(GuiIds.TATARA, "betterwithaddons.client.gui.GuiTatara", "betterwithaddons.container.ContainerTatara"),
        SOAKING_BOX(GuiIds.SOAKING_BOX, "betterwithaddons.client.gui.GuiSoakingBox", "betterwithaddons.container.ContainerCherryBox"),
        DRYING_BOX(GuiIds.DRYING_BOX, "betterwithaddons.client.gui.GuiDryingBox", "betterwithaddons.container.ContainerCherryBox");

        int guiID;
        String guiClass;
        String containerClass;

        GuiContainerConnection(int guiID, String guiClass, String containerClass)
        {
            this.guiID = guiID;
            this.guiClass = guiClass;
            this.containerClass = containerClass;
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        for (GuiContainerConnection registered : GuiContainerConnection.values())
        {
            if (ID == registered.guiID)
            {
                Class containerClass = null;
                try
                {
                    containerClass = Class.forName(registered.containerClass);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }

                if (containerClass != null)
                {
                    Constructor constructor = null;
                    try
                    {
                        constructor = containerClass.getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class);
                    }
                    catch (NoSuchMethodException e)
                    {
                        e.printStackTrace();
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }

                    if (constructor != null)
                    {
                        try
                        {
                            Container container = (Container) constructor.newInstance(player, world, x, y, z);
                            return container;
                        }
                        catch (InstantiationException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                        catch (InvocationTargetException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        for (GuiContainerConnection registered : GuiContainerConnection.values())
        {
            if (ID == registered.guiID)
            {
                Class guiClass = null;
                try
                {
                    guiClass = Class.forName(registered.guiClass);
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }

                if (guiClass != null)
                {

                    Constructor constructor = null;
                    try
                    {
                        constructor = guiClass.getConstructor(EntityPlayer.class, World.class, int.class, int.class, int.class);
                    }
                    catch (NoSuchMethodException e)
                    {
                        e.printStackTrace();
                    }
                    catch (SecurityException e)
                    {
                        e.printStackTrace();
                    }

                    if (constructor != null)
                    {
                        try
                        {
                            GuiContainer guiContainer = (GuiContainer) constructor.newInstance(player, world, x, y, z);
                            return guiContainer;
                        }
                        catch (InstantiationException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IllegalAccessException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IllegalArgumentException e)
                        {
                            e.printStackTrace();
                        }
                        catch (InvocationTargetException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}