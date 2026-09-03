package com.qstorm.powers.cursedtechnique;

import com.qstorm.powers.Ability;
import com.qstorm.powers.Combo;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class CursedStrengthening extends Ability {

    public CursedStrengthening(ServerPlayer player, int id) {
        super(player.getUUID(),id);
        this.setCombo(Combo.build(player,"strengthen").addKey4(5).addKey4(3));
    }

    @Override
    protected void run(ServerPlayer player) {

    }
}