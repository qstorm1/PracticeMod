package com.qstorm.powers;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

//player specific
public class Sorcery {
    public int cursedEnergy = 0;//regular level 3 sorcerers have 1000-10000 cursed energy
    public int cursedEnergyReserve=0;
    public double cursedOutput = 0;
    public boolean canUseReversed=false;
    public boolean reversedOn=false;
    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;
    public int domainEnergyCost=0;//use for innate domain as well

    public static HashMap<UUID,Sorcery> playerValues = new HashMap<>();

    private Sorcery(int cursedEnergy,int cursedEnergyReserve, double cursedOutput,int domainEnergyCost){
        this.cursedEnergy=cursedEnergy;
        this.cursedEnergyReserve=cursedEnergyReserve;
        this.cursedOutput=cursedOutput;
        this.domainEnergyCost=domainEnergyCost;
    }

    protected Sorcery(ServerPlayer player,int bornLuck,int domainEnergyCost){
        if(playerValues.get(player.getUUID())==null){
            playerValues.put(player.getUUID(),new Sorcery((int)(Math.random(3,10)+1),(int)(Math.random()),(int)(Math.random()),domainEnergyCost));
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
