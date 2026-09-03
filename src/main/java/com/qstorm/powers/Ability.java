package com.qstorm.powers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Ability {
    int id;
    Combo abilityCombo;
    public boolean isTicked=false;
    public UUID playerUUID;



    public Ability(UUID playerUUID, int id){
        this.id=id;
        this.playerUUID = playerUUID;
    }

    public void Do(ArrayList<Integer> times,ServerPlayer player){
        if(times.isEmpty()) {
            run(player);
        }else{
            run(times, player);
        }
    }

    public void setCombo(Combo combo){
        this.abilityCombo=combo;
    }


    protected void run(ServerPlayer player) {

    }

    protected void run(ArrayList<Integer> times, ServerPlayer player) {

    }

    public void tick(MinecraftServer context){

    }

}
