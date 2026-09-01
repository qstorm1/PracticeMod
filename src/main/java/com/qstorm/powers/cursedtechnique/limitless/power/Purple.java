package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.powers.cursedtechnique.Ability;
import net.minecraft.server.level.ServerPlayer;

public class Purple extends Ability {

    public Purple(int id) {
        super(id);
    }

    @Override
    public void normal(ServerPlayer player) {

    }

    @Override
    public void reversed(ServerPlayer player) {
        normal(player);
    }
}
