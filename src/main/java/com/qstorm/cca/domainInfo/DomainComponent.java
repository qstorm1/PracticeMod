package com.qstorm.cca.domainInfo;

import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.Component;

public interface DomainComponent extends Component {
    Vec3 getPos();
    void setPos();
}
