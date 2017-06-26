package betterwithaddons.tileentity;

import betterwithaddons.block.Factorization.BlockBrine;
import betterwithaddons.block.Factorization.BlockPondBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class TileEntityBrine extends TileEntityBase implements ITickable {
    int progress = 0;
    public static int MAX_PROGRESS = 6000;
    boolean isValid = false;
    int nextCheck = 80;

    @Override
    protected void setWorldCreate(World worldIn) {
        super.setWorldCreate(worldIn);
        nextCheck = worldIn.rand.nextInt(80);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void writeDataToNBT(NBTTagCompound compound) {
        compound.setInteger("progress",progress);
        compound.setBoolean("isValid",isValid);
        compound.setInteger("nextCheck",nextCheck);
    }

    @Override
    public void readDataFromNBT(NBTTagCompound compound) {
        progress = compound.getInteger("progress");
        isValid = compound.getBoolean("isValid");
        nextCheck = compound.getInteger("nextCheck") % 80; //sanity maybe
    }

    @Override
    public void update() {
        if(world.isRemote || nextCheck-- > 0)
            return;

        nextCheck = 80;

        IBlockState state = world.getBlockState(pos);
        IBlockState bottomState = world.getBlockState(pos.down());

        if(state.getBlock() instanceof BlockBrine && bottomState.getBlock() instanceof BlockPondBase)
        {
            BlockBrine blockBrine = (BlockBrine) state.getBlock();
            isValid = ((BlockPondBase) bottomState.getBlock()).isExposedToSun(world,pos);
            if(isValid && !blockBrine.isValidOnAllSides(world,pos)) {
                progress = 0;
                isValid = false;
            }
            if(isValid) {
                progress += 80;

                IBlockState nextState = null;
                while (progress > MAX_PROGRESS) {
                    nextState = blockBrine.getNextState(state);
                    progress -= MAX_PROGRESS;
                }

                if(nextState != null)
                    world.setBlockState(pos,nextState,3);
            }
        }
        else
        {
            world.setBlockToAir(pos);
        }
    }
}
