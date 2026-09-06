package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.Ability;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.UUID;

public class Blue extends Ability {


    public Blue(UUID playerUUID, int id) {
        super(playerUUID,id,0xFF0000ff);
    }

    @Override
    protected void run(ServerPlayer player) {
        LaserEyes.shootLaser(player);
    }

    @Override
    protected void run(ArrayList<Integer> ints, ServerPlayer player){
        LaserEyes.shootLaser(player);
        //NOTE: the int values only state how long its been since the key was last pressed, to get how long since the key before was pressed do
        //Key2.time-Key1.time
    }


}
