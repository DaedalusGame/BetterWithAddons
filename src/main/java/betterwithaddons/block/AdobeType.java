package betterwithaddons.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class AdobeType {
    private int meta;
    private float hardness;
    private float resistance;
    private boolean dropsBlock;

    public BlockAdobe block;
    public IBlockState wetBlock;
    public IBlockState dryBlock;

    public static final AdobeType MOSTLY_CLAY = new AdobeType(0,1.25F, 7.0F,true);
    public static final AdobeType CLAYSAND = new AdobeType(1,1.25F, 3.0F,true);
    public static final AdobeType SANDCLAY = new AdobeType(2,1.0F, 7.0F,true);
    public static final AdobeType MOSTLY_SAND = new AdobeType(3,0.25F, 0.0F,false);
    public static final AdobeType MOSTLY_STRAW = new AdobeType(4,1.0F, 5.0F,false);
    public static final AdobeType LIGHT = new AdobeType(5,5.0F, 14.0F,true);
    public static final AdobeType DARK = new AdobeType(6,8.0F, 7.0F,true);
    public static final AdobeType MOSTLY_DUNG = new AdobeType(7,1.25F, 3.0F,true);

    public AdobeType(int meta, float hardness, float resistance, boolean dropsBlock)
    {
        this.meta = meta;
        this.hardness = hardness;
        this.resistance = resistance;
        this.dropsBlock = dropsBlock;
    }

    public float getHardness() {
        return hardness;
    }

    public float getResistance() {
        return resistance;
    }

    public boolean dropsBlock() {
        return dropsBlock;
    }

    public ItemStack getBlockStack(int amt,boolean wet)
    {
        return new ItemStack(block,amt,meta|(wet?0:8));
    }
}
