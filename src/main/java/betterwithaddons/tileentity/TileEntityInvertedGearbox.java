package betterwithaddons.tileentity;

import betterwithmods.api.BWMAPI;
import betterwithmods.common.blocks.mechanical.tile.TileGearbox;
import net.minecraft.util.EnumFacing;

public class TileEntityInvertedGearbox extends TileGearbox {
    public TileEntityInvertedGearbox()
    {
        super(1);
    }

    @Override
    public int getMechanicalOutput(EnumFacing facing) {
        if (facing == getFacing().getOpposite() && BWMAPI.IMPLEMENTATION.isAxle(world, pos.offset(facing), facing.getOpposite()))
            return getPower();
        return -1;
    }

    public void onChanged() {
        if (this.getBlockWorld().getTotalWorldTime() % 20L != 0L)
            return;

        EnumFacing gearfacing = getFacing();
        int findPower = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            if(facing == gearfacing || facing.getOpposite() == gearfacing)
                continue;
            int power = getMechanicalInput(facing);
            if (power > findPower) {
                findPower = power;
            }
        }
        if (overpowerChance() && findPower > getMaximumInput(EnumFacing.UP)) {
            overpower();
            return;
        }

        if (findPower > 0) {
            setPower(0);
            markDirty();
            return;
        }

        super.onChanged();
    }
}
