package betterwithaddons.handler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;

import java.util.ArrayList;
import java.util.HashMap;

public class MobTerritoryType {
    public static HashMap<String,MobTerritoryType> typeList = new HashMap<String, MobTerritoryType>();

    String identifier;
    ArrayList<Class<EntityLiving>> mobClasses;
    boolean canReplaceAnimals;

    public MobTerritoryType(String identifier)
    {
        typeList.put(identifier,this);
    }

    public void addEntityClass(Class<EntityLiving> entityclass)
    {
        if(!mobClasses.contains(entityclass))
            mobClasses.add(entityclass);
    }

    public void removeEntityClass(Class<EntityLiving> entityclass)
    {
        if(mobClasses.contains(entityclass))
            mobClasses.remove(entityclass);
    }

    public void setCanReplaceAnimals(boolean newval)
    {
        canReplaceAnimals = newval;
    }

    public boolean getCanReplaceAnimals() { return canReplaceAnimals; }

    public String getIdentifier() {
        return identifier;
    }

    public boolean shouldReplace(EntityLiving living)
    {
        if(living instanceof EntityAnimal && !canReplaceAnimals)
            return false;

        boolean shouldReplace = true;

        for (Class<EntityLiving> entityclass: mobClasses) {
            if(entityclass.isInstance(living))
            {
                shouldReplace = false;
                break;
            }
        }

        return shouldReplace;
    }
}
