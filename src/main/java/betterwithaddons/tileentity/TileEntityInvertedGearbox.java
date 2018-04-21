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
        tick++;
        if (tick < 20)
            return;
        tick = 0;

        EnumFacing gearfacing = getFacing();
        int inputPower = this.getMechanicalInput(getFacing());
        int findPower = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            if(facing == gearfacing || facing.getOpposite() == gearfacing)
                continue;
            int power = getMechanicalInput(facing);
            if (power > findPower) {
                findPower = power;
            }
        }
        if(findPower > 0)
            inputPower = 0;

        if (inputPower != this.power) {
            setPower(inputPower);
            unchanged = 0;
        } else {
            unchanged++;
        }

        if (isOverpowered() && unchanged > 30) {
            overpower();
        }

        markDirty();
    }
}
