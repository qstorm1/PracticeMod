package com.qstorm.block.custom.compressedcobblestone;

import net.minecraft.block.Block;

public class BlackHole extends Block {
    //A blackhole forms when 2GM>c^2 (r is 1 meter cubed so it's factored out)
    //the mass must be equal to 6.73295462e26
    //We need to convert it 28 times to form a black hole
    //this takes 5.23347633027e+26 cobblestone total


    //GM/r^2=g

    public BlackHole(Settings settings) {
        super(settings);
    }
}
