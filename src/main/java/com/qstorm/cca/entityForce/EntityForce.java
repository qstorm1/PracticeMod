package com.qstorm.cca.entityForce;

import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.HashMap;

public interface EntityForce extends Component {
    Vec3 findNetForce();
    HashMap<Integer,Vec3> getMap();
    void setForce(Integer key,Vec3 value);
    void clearForces();
    void removeForce(Integer key);
}
