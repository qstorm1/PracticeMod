package com.qstorm.powers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

//player specific
public class Sorcery {
    public int cursedEnergy = 0;
    public int cursedEnergyReserve=0;
    public double cursedOutput = 0;
    public double maxCursedOutput = 0;
    public boolean canUseReversed=false;
    public boolean reversedOn=false;
    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;
    public int domainEnergyCost=0;//use for innate domain as well
    int innateTickCost =0;

    public static HashMap<UUID,Sorcery> cursedUsers = new HashMap<>();

    public static HashMap<UUID,Sorcery> playerValues = new HashMap<>();

    private Sorcery(int cursedEnergyReserve, double maxCursedOutput, int domainEnergyCost,int innateTickCost){
        this.cursedEnergy=cursedEnergyReserve;
        this.cursedEnergyReserve=cursedEnergyReserve;
        this.maxCursedOutput = maxCursedOutput;
        this.cursedOutput=maxCursedOutput;
        this.domainEnergyCost=domainEnergyCost;
        this.innateTickCost=innateTickCost;
    }

    //born luck is a percentage that represents how lucky are your initial stat growths
    //increase luck represents and increase in luck
    protected Sorcery(ServerPlayer player,int bornLuck,int increaseLuck,int innateTickCost,int domainEnergyCost){
        //a random number bettween 0-1 with a higher chance of being close to one based on bornLuck stat
        //tbh this is kinda more vibes based
        Random random=new Random();
        Sorcery sorcery = new Sorcery(
                (int)Math.abs(((random.nextGaussian()*10)+5+bornLuck)*(20000+increaseLuck)),
                (int)Math.abs(((random.nextGaussian()*3)+5+bornLuck)*(10000+increaseLuck)),
                domainEnergyCost,
                innateTickCost);
        playerValues.putIfAbsent(player.getUUID(), sorcery);

        //player.addTag("Cursed Energy: "+sorcery.cursedEnergy+"Cursed Output "+ sorcery.maxCursedOutput +"Cursed Energy Reserve "+sorcery.cursedEnergyReserve);
    }

    public static void regenStats(ServerPlayer player,int bornLuck,int increaseLuck,int innateTickCost,int domainEnergyCost){
        Random random=new Random();
        Sorcery sorcery = new Sorcery(
                (int)Math.abs(((random.nextGaussian()*10)+5+bornLuck)*(20000+increaseLuck)),
                (int)Math.abs(((random.nextGaussian()*3)+5+bornLuck)*(10000+increaseLuck)),
                domainEnergyCost,
                innateTickCost);
        playerValues.put(player.getUUID(), sorcery);
    }

    /**
     * for debug or aura farming
     */
    public void cheatMode(){

    }

    static void innateDomain(){

    }

    static void domain(){

    }

    static void sorceryClientInit(Player player){

    }
}
