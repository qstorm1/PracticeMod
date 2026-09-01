package com.qstorm.powers.cursedtechnique;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class PlayerInfo {
    //basic information
    public int cursedEnergy = 0;
    public int cursedEnergyReserve=0;
    public double cursedOutput = 0;
    public double maxCursedOutput = 0;

    public boolean canUseReversed=false;








    public static HashMap<UUID,PlayerInfo> playerInfoHashMap = new HashMap<>();


    public static void initNewPlayer(UUID playerUUID){
        playerInfoHashMap.put(playerUUID,new PlayerInfo(0,0));
    }

    public static void initNewPlayer(UUID playerUUID,int bornLuck,int increaseLuck){
        playerInfoHashMap.put(playerUUID,new PlayerInfo(bornLuck,increaseLuck));
    }



    /**
     * for debug or aura farming
     */
    public void cheatMode(){
        this.cursedEnergyReserve=Integer.MAX_VALUE;
        this.cursedEnergy=99999;
    }





    public PlayerInfo(int bornLuck, int increaseLuck){

        //a random number bettween 0-1 with a higher chance of being close to one based on bornLuck stat
        //tbh this is kinda more vibes based
        Random random = new Random();


        //how much cursed energy the player has
        this.cursedEnergyReserve= (int)Math.abs(((random.nextGaussian()*10)+5+bornLuck)*(20000+increaseLuck));
        this.cursedEnergy=cursedEnergyReserve;

        //how much cursed energy the player can output (0-100%) default to 0.1% its realisticly impossible to go past 5%
        // (you can run a thing that costs a default of 1/1000 of maxed cursed energy 1000 times)
        // I might change this from a percentage to an amount (ie you use scroll bar to output 1 energy-10000 energy)
        this.maxCursedOutput = (int)Math.abs(random.nextGaussian(0.05,0.30));
        this.cursedOutput=maxCursedOutput;

    }
}
