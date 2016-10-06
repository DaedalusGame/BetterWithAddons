package betterwithaddons.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import net.minecraft.block.BlockSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by Christian on 09.08.2016.
 */
public class TileEntityAlchDragon extends TileEntity implements ITickable {
    private int skullRotation;
    private int dragonAnimatedTicks;
    private boolean dragonAnimated;

    public TileEntityAlchDragon() {
    }

    public NBTTagCompound writeToNBT(NBTTagCompound p_writeToNBT_1_) {
        super.writeToNBT(p_writeToNBT_1_);
        p_writeToNBT_1_.setByte("Rot", (byte)(this.skullRotation & 255));

        return p_writeToNBT_1_;
    }

    public void readFromNBT(NBTTagCompound p_readFromNBT_1_) {
        super.readFromNBT(p_readFromNBT_1_);
        this.skullRotation = p_readFromNBT_1_.getByte("Rot");
    }

    public void update() {
        if(this.worldObj.isBlockPowered(this.pos)) {
            this.dragonAnimated = true;
            ++this.dragonAnimatedTicks;
        } else {
            this.dragonAnimated = false;
        }
    }

    @SideOnly(Side.CLIENT)
    public float getAnimationProgress(float p_getAnimationProgress_1_) {
        return this.dragonAnimated?(float)this.dragonAnimatedTicks + p_getAnimationProgress_1_:(float)this.dragonAnimatedTicks;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 4, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @SideOnly(Side.CLIENT)
    public int getSkullRotation() {
        return this.skullRotation;
    }

    public void setSkullRotation(int p_setSkullRotation_1_) {
        this.skullRotation = p_setSkullRotation_1_;
    }

    public void func_189668_a(Mirror p_189668_1_) {
        if(this.worldObj != null && this.worldObj.getBlockState(this.getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP) {
            this.skullRotation = p_189668_1_.mirrorRotation(this.skullRotation, 16);
        }

    }

    public void func_189667_a(Rotation p_189667_1_) {
        if(this.worldObj != null && this.worldObj.getBlockState(this.getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP) {
            this.skullRotation = p_189667_1_.rotate(this.skullRotation, 16);
        }

    }
}