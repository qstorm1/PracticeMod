package com.qstorm.mob;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.animal.sheep.Sheep;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;

public record Mass() {

    public static final HashMap<Class<?>,Integer> mass = new HashMap<>();


    static {
        mass.put(Sheep.class,10);
        mass.put(Cow.class,4);
        mass.put(Pig.class,3);
        mass.put(Chicken.class,1);
    }
}
