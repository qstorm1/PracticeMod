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

    //JJK Specific
    public PlayerInfo playerInfo;
    /**
     * the amount of energy needed for the ability to run,
     * ex: if your output is 1000 energy and this value is 1100 then the ability will not run and you will not lose any energy,
     * it will also trigger a NotEnoughOutputCall
     */
    int initial=0;
    /**
     * The how much the power increases as a result of the cost increasing
     * higher rate = higher cost per power
     * power=rate*cost+initial
     */
    double rate=0;
    /**
     * How much power this ability will have, each ability can handle this uniquely
     */
    double power=0;


    //the cursedEnergy system works linearly at the moment, power=increaseRate*energy+initial



    public Ability(UUID playerUUID, int id,int textColor){
        this.id=id;
        this.playerUUID = playerUUID;
        this.textColor = textColor;
    }
    public Ability(UUID playerUUID, int id){
        this(playerUUID,id,0xFF000000);
    }

    public Ability(UUID playerUUID, int id, int initial,int rate,int textColor){
        this(playerUUID,id,textColor);
        this.initial=initial;
        this.rate=rate;
    }

    public void Do(ServerPlayer player){
        power=rate*playerInfo.cursedOutput-initial;
        if(power<0){
            return;
        }
        playerInfo.useEnergy(player,playerInfo.cursedOutput);
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

    public void notEnoughEnergy(){

    }

    public void notEnoughOutput(){

    }




}
