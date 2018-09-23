package betterwithaddons.block;

// Original Copyright Notice provided as required by the License.
// ==========================================================================
// Copyright (C)2013 by Aaron Suen <warr1024@gmail.com>
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
// THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
// OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
// ---------------------------------------------------------------------------

import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBetterLilyPad extends BlockLilyPad {
    public BlockBetterLilyPad() {
        setTickRandomly(true);
        setHardness(0.0F);
        setSoundType(SoundType.PLANT);
        setRegistryName("minecraft","waterlily");
        setUnlocalizedName("waterlily");
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && canBlockStay(worldIn,pos);
    }

    @Override
    protected boolean canSustainBush(IBlockState state)
    {
        return state.getMaterial() == Material.WATER || state.getMaterial() == Material.ICE;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return canBlockStay(worldIn, pos);
    }

    public boolean canBlockStay(World world, BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256)
        {
            IBlockState bottomState = world.getBlockState(pos.down());
            Material material = bottomState.getMaterial();

            boolean isLit = world.getLight(pos) > 7 || world.canSeeSky(pos);
            return (isLit && material == Material.WATER && (bottomState.getValue(BlockLiquid.LEVEL) & 7) <= 1 || material == Material.ICE) || bottomState.getBlock().canSustainPlant(bottomState, world, pos.down(), net.minecraft.util.EnumFacing.UP, this);
        }
        else
        {
            return false;
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);

        int neighbors = 3;

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1)))
        {
            if (worldIn.getBlockState(blockpos).getBlock() == this)
            {
                --neighbors;

                if (neighbors <= 0)
                {
                    return;
                }
            }
        }

        int light = worldIn.getLightFromNeighbors(pos);

        if(rand.nextInt(32) < light)
        {
            BlockPos growPos = pos.add(rand.nextInt(3)-1, 0, rand.nextInt(3)-1);

            if(worldIn.isAirBlock(growPos) && canBlockStay(worldIn,growPos,state))
                worldIn.setBlockState(growPos,state);
        }
    }
}
