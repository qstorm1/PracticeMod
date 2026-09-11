package com.qstorm.powers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
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
    protected int initial=0;
    /**
     * The how much the power increases as a result of the cost increasing
     * higher rate = higher cost per power
     * power=rate*cost+initial
     */
    protected double rate=0;
    /**
     * How much power this ability will have, each ability can handle this uniquely
     */
    protected double power=0;
    protected double percentPower=0;

    public boolean isScroll=false;


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
        playerInfo=PlayerInfo.playerInfoHashMap.get(playerUUID);
        power=(rate*playerInfo.cursedOutput);
        percentPower=power/rate*playerInfo.maxCursedOutput;
        if(power<initial){
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
    public void startTickLoop(){
        isTicked=true;
    }
    public void endTickLoop(){
        isTicked=false;
    }

    public void end(ServerLevel context){
        this.isTicked=false;
        disableScroll();
    };

    /**
     *
     * @param amount assume = 1
     */
    public void onScroll(double amount){

    }

    public void enableScroll(){
        this.isScroll=true;
    }
    public void disableScroll(){
        this.isScroll=false;

    }


    public void notEnoughEnergy(){

    }

    public void notEnoughOutput(){

    }






}
