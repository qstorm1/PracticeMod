package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.cursedtechnique.Ability;
import net.minecraft.server.level.ServerPlayer;

public class BlueAndRed extends Ability {
    public BlueAndRed(int id) {
        super(id);
    }

    @Override
    public void normal(ServerPlayer player) {
        LaserEyes.shootLaser(player);
    }

    @Override
    public void reversed(ServerPlayer player) {

    }


}
