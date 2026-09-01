package com.qstorm.powers.cursedtechnique;

import com.qstorm.PracticeMod;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

//player specific
//any players that can use cursed energy is added to the list of players
public class Sorcery {

    public boolean reversedOn=false;
    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;
    public int domainEnergyCost=0;//use for innate domain as well

    /**
     * the cursed energy used per tick innate techinque is active
     */
    int constantAbilityTickCost =0;



    /**
     * Abilities of player
     * get(0) = Innate technique
     */
    public ArrayList<Ability> abilities = new ArrayList<>();


    public PlayerInfo playerInfo;

    //The domain of this ability
    public Domain domain;


    //a global list of each players sorcery data
    public static HashMap<UUID,Sorcery> sorcerers = new HashMap<>();



    //born luck is a percentage that represents how lucky are your initial stat growths
    //increase luck represents and increase in luck
    public Sorcery(Ability innate,ServerPlayer player,int domainEnergyCost,int constantAbilityTickCost){

        //sets all the important values
        this.player=player;
        this.domainEnergyCost=domainEnergyCost;
        this.constantAbilityTickCost = constantAbilityTickCost;



        initAbilities(innate);
        //player.addTag("Cursed Energy: "+sorcery.cursedEnergy+"Cursed Output "+ sorcery.maxCursedOutput +"Cursed Energy Reserve "+sorcery.cursedEnergyReserve);



        sorcerers.put(player.getUUID(),this);
    }

    /**
     * create a player with no technique
     */
    public Sorcery(ServerPlayer player){
        sorcerers.putIfAbsent(player.getUUID(),this);
    }



    /**
     * inits starter abilities in the correct order
     */
    public void initAbilities(Ability innate){
        if(abilities.size()>1){
            abilities =new ArrayList<>();
        }
        abilities.add(innate);
    }












    static void innateDomain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used innate domain");
    }

    public void domain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used domain");
    }

    public void onChangeOutput(double percentAmount){
        playerInfo.cursedOutput+=playerInfo.maxCursedOutput*percentAmount;
        PracticeMod.LOGGER.info("changed output: "+playerInfo.cursedOutput);
    }


    public void activateInnate(){

    }

    public void deactivateInnate(){

    }

    public void onIncreaseInnate(int amountIncrease){

    }

    public void onDecreaseInnate(int amountDecrease){

    }

    static void sorceryClientInit(AbstractClientPlayer player){

    }






    //NOT RECOMMENDED TO USE
    @Deprecated
    public ServerPlayer player;



}
