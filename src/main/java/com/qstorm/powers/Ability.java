package com.qstorm.powers;

import net.minecraft.server.level.ServerPlayer;

public abstract class Ability {
    int id;
    Combo abilityCombo;

    public Ability(int id){
        this.id=id;
    }

    public void Do(ServerPlayer player){
        run(player);
    }

    public void setCombo(Combo combo){
        this.abilityCombo=combo;
    }


    protected abstract void run(ServerPlayer player);

}
