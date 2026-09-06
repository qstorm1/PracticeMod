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
    public ArrayList<Integer> times = new ArrayList<>();
    public final int textColor;
    public PlayerInfo playerInfo;//this is used for JJK related things



    public Ability(UUID playerUUID, int id,int textColor){
        this.id=id;
        this.playerUUID = playerUUID;
        this.textColor = textColor;
    }
    public Ability(UUID playerUUID, int id){
        this(playerUUID,id,0xFF000000);
    }

    public void Do(ServerPlayer player){
         if(times.isEmpty()) {
            run(player);
        }else{
            run(times, player);
        }
    }

    public void setCombo(Combo combo){
        this.abilityCombo=combo;
        //abilityCombo.setAction(()->Do());
    }


    protected void run(ServerPlayer player) {

    }
    protected void run(ArrayList<Integer> times,ServerPlayer player) {

    }

    public void tick(MinecraftServer context){

    }

}
