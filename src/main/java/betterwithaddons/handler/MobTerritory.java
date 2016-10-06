package betterwithaddons.handler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

/**
 * Created by Christian on 10.08.2016.
 */
public class MobTerritory {
    int id;
    ItemStack banner;

    MobTerritoryType territoryType;

    public MobTerritory(int id,MobTerritoryType type)
    {
        this.id = id;
        territoryType = type;
    }

    public int getId() {
        return id;
    }

    public MobTerritoryType getTerritoryType() {
        return territoryType;
    }

    public ItemStack getBanner() {
        return banner;
    }

    public void setBanner(ItemStack banner) {
        this.banner = banner;
    }

    public boolean shouldReplace(EntityLiving living)
    {
        return territoryType.shouldReplace(living);
    }
}
