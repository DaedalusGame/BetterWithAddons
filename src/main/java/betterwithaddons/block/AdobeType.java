package betterwithaddons.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class AdobeType {
    private float hardness;
    private float resistance;
    private boolean dropsBlock;

    public IBlockState wetBlock;
    public IBlockState dryBlock;

    public ItemStack wetBlockItem;
    public ItemStack dryBlockItem;

    public static final AdobeType MOSTLY_CLAY = new AdobeType(1.25F, 7.0F,true);
    public static final AdobeType MOSTLY_SAND = new AdobeType(0.25F, 0.0F,false);
    public static final AdobeType MOSTLY_DUNG = new AdobeType(1.25F, 3.0F,true);
    public static final AdobeType MOSTLY_STRAW = new AdobeType(1.0F, 5.0F,false);
    public static final AdobeType CLAYSAND = new AdobeType(1.25F, 3.0F,true);
    public static final AdobeType SANDCLAY = new AdobeType(1.0F, 7.0F,true);
    public static final AdobeType DARK = new AdobeType(8.0F, 7.0F,true);
    public static final AdobeType LIGHT = new AdobeType(5.0F, 14.0F,true);

    public AdobeType(float hardness, float resistance, boolean dropsBlock)
    {
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
}
