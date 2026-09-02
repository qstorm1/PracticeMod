package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.Ability;
import net.minecraft.server.level.ServerPlayer;

public class Blue extends Ability {


    public Blue(int id) {
        super(id);
    }

    @Override
    protected void run(ServerPlayer player) {
        LaserEyes.shootLaser(player);
    }


}
