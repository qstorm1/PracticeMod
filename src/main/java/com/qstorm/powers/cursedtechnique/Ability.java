package com.qstorm.powers.cursedtechnique;

import com.qstorm.powers.Combo;
import net.minecraft.server.level.ServerPlayer;

public abstract class Ability {
    int id;
    Combo abilitiesCombo;

    public Ability(int id){
        this.id=id;
    }

    public void Do(ServerPlayer player,boolean isReversed){
        if (!isReversed) {
            normal(player);
        } else {
            reversed(player);
        }
    }


    public abstract void normal(ServerPlayer player);
    public abstract void reversed(ServerPlayer player);

}
