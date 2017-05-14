package betterwithaddons.util;

import betterwithaddons.block.BlockFusumaPainted;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.HashMap;

public class FusumaPicture {
    int pictureID;
    ArrayList<FusumaPart> subblocks = new ArrayList<>();

    private static ArrayList<FusumaPicture> fusumaPictures = new ArrayList<>();
    private static HashMap<Integer,FusumaPart> fusumaBlocks = new HashMap<>();
    public static FusumaPart EMPTY_FUSUMA;

    public FusumaPicture(int id)
    {
        pictureID = id;
    }

    public ArrayList<FusumaPart> getSubblocks() {
        return subblocks;
    }

    public FusumaPart getSubblock(int y)
    {
        return y >= 0 && y < subblocks.size() ? subblocks.get(y) : EMPTY_FUSUMA;
    }

    public static void addPicture(FusumaPicture picture)
    {
        fusumaPictures.add(picture);
    }

    public void addSubblock(BlockFusumaPainted block, int meta)
    {
        FusumaPart part = new FusumaPart(this, block, meta, subblocks.size());
        fusumaBlocks.put(block.getOffset() * 16 + meta,part);
        subblocks.add(part);
        if(EMPTY_FUSUMA == null)
            EMPTY_FUSUMA = part;
    }

    public FusumaPicture withSubblock(BlockFusumaPainted block, int meta)
    {
        addSubblock(block,meta);
        return this;
    }

    public int getPictureID() {
        return pictureID;
    }

    public int getHeight()
    {
        return subblocks.size();
    }

    public static int getTotalPictures()
    {
        return fusumaPictures.size();
    }

    public static FusumaPicture getPicture(int index) {
        return (index >= 0 && index < fusumaPictures.size()) ? fusumaPictures.get(index) : null;
    }

    public static FusumaPart getFusumaBlock(BlockFusumaPainted block, int meta) {
        return fusumaBlocks.get(block.getOffset() * 16 + meta);
    }
}
