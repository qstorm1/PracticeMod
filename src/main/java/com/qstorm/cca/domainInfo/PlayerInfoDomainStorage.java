package com.qstorm.cca.domainInfo;

import com.qstorm.PracticeMod;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

public class PlayerInfoDomainStorage implements DomainComponent{

    Vec3 prevPos;
    int x;
    int y;
    int z;
    public static final ComponentKey<Component> oldPos = ComponentRegistry.getOrCreate(
            Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"old-pos-after-domain"),
            Component.class);


    @Override
    public Vec3 getPos() {
        return prevPos;
    }

    @Override
    public void setPos(Vec3 input) {
        this.prevPos=input;
    }

    @Override
    public void readData(ValueInput readView) {
        x=readView.getIntOr("x",0);
        y=readView.getIntOr("y",0);
        z=readView.getIntOr("z",0);
        prevPos=new Vec3(x,y,z);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putInt("x",(int)prevPos.x);
        writeView.putInt("y",(int)prevPos.y);
        writeView.putInt("z",(int)prevPos.z);
    }
}
