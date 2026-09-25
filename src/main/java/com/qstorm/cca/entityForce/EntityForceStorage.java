package com.qstorm.cca.entityForce;

import com.qstorm.PracticeMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

//Need to be on client side so that we can apply forces
public class EntityForceStorage implements EntityForce{
    public static ComponentKey<EntityForce> forceData = ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"entity-force"),
            EntityForce.class);



    public static final Integer blueForce = 67;


    /**
     * an arrayList of forces that are applied from the object
     */
    HashMap<Integer,Vec3> forces = new HashMap<>();




    @Override
    public Vec3 findNetForce() {
        Vec3 finalVal=Vec3.ZERO;
        for (Vec3 val : forces.values()) {
            finalVal = finalVal.add(val);
        }
        return finalVal;
    }

    @Override
    public HashMap<Integer, Vec3> getMap() {
        return forces;
    }

    @Override
    public void setForce(Integer key,Vec3 value) {
        forces.put(key,value);
    }

    @Override
    public void clearForces() {
        Set<Integer> k= forces.keySet();
        for(Integer key:k) forces.remove(key);
    }

    @Override
    public void removeForce(Integer key) {
        forces.remove(key);
    }


    @Override
    public void readData(ValueInput readView) {

    }

    @Override
    public void writeData(ValueOutput writeView) {

    }
}
