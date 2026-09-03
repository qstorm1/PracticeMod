package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.Ability;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class Blue extends Ability {


    public Blue(UUID playerUUID, int id) {
        super(playerUUID,id);
    }

    @Override
    protected void run(ServerPlayer player) {
        LaserEyes.shootLaser(player);
    }


}
