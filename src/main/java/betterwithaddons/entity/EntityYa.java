package betterwithaddons.entity;

import betterwithaddons.item.ItemGreatarrow;
import betterwithaddons.item.ItemYa;
import betterwithaddons.item.ModItems;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityYa extends EntityArrow {
    private static final DataParameter<ItemStack> ARROW_TYPE = EntityDataManager.createKey(EntityGreatarrow.class, DataSerializers.ITEM_STACK);

    public EntityYa(World worldIn) {
        super(worldIn);
    }

    public EntityYa(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    public EntityYa(World worldIn, IPosition pos) {
        super(worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ARROW_TYPE, new ItemStack(ModItems.greatarrow));
    }

    public void setArrowStack(ItemStack stack)
    {
        stack = stack.copy();
        stack.setCount(1); //Bows tend to only fire one arrow.
        dataManager.set(ARROW_TYPE, stack);
    }

    @Override
    protected ItemStack getArrowStack() {
        return dataManager.get(ARROW_TYPE);
    }

    @Override
    protected void arrowHit(EntityLivingBase living) {
        super.arrowHit(living);
        ItemYa arrowtype = getArrowType();
        arrowtype.hitEntity(this,living);
    }

    private ItemYa getArrowType() {
        ItemStack arrowstack = getArrowStack();
        ItemYa arrowtype = ModItems.ya;
        if(!arrowstack.isEmpty() && arrowstack.getItem() instanceof ItemYa) //I don't trust people like you.
            arrowtype = (ItemYa) arrowstack.getItem();
        return arrowtype;
    }
}