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
    public boolean canUseReversed=false;
    public boolean reversedOn=false;
    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;
    public int domainEnergyCost=0;//use for innate domain as well

    public static HashMap<UUID,Sorcery> playerValues = new HashMap<>();

    private Sorcery(int cursedEnergyReserve, double cursedOutput,int domainEnergyCost){
        this.cursedEnergy=cursedEnergyReserve;
        this.cursedEnergyReserve=cursedEnergyReserve;
        this.cursedOutput=cursedOutput;
        this.domainEnergyCost=domainEnergyCost;
    }

    //born luck is a percentage that represents how lucky are your initial stat growths
    //increase luck represents and increase in luck
    protected Sorcery(ServerPlayer player,int bornLuck,int increaseLuck,int domainEnergyCost){
        if(playerValues.get(player.getUUID())==null){
            //a random number bettween 0-1 with a higher chance of being close to one based on bornLuck stat
            //tbh this is kinda more vibes based
            Random random=new Random();
            playerValues.put(player.getUUID(),new Sorcery(
                    (int)Math.abs(((random.nextGaussian()*10)+5+bornLuck)*(20000+increaseLuck)),
                    (int)Math.abs(((random.nextGaussian()*3)+5+bornLuck)*(10000+increaseLuck)),
                    domainEnergyCost));
        }
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
