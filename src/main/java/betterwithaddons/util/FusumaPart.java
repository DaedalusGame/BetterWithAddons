package betterwithaddons.util;

import net.minecraft.block.Block;

public class FusumaPart extends BlockMeta {
    private int y;
    private FusumaPicture picture;

    public FusumaPart(FusumaPicture picture, Block block, int meta, int y) {
        super(block, meta);
        this.y = y;
        this.picture = picture;
    }

    public int getY() {
        return y;
    }

    public FusumaPicture getPicture() {
        return picture;
    }
}
