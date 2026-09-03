package com.qstorm.powers.cursedtechnique;

import com.qstorm.PracticeMod;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.powers.Ability;
import com.qstorm.powers.EffectsCursedEnergyUsage;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

//player specific
//any players that can use cursed energy is added to the list of players
public class Sorcery {

    //data on weather the player can use things
    public boolean canUseInnateDomain=false;
    public boolean canUseDomain=false;

    //Sorcery specific data
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


    //The domain of this ability
    public Domain domain;




    //a global list of each player's sorcery data (All sorcery objects)
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

        registerServerNetworking();

        sorcerers.put(player.getUUID(),this);
    }

    /**
     * create a player with no technique
     */
    public Sorcery(ServerPlayer player){
        sorcerers.putIfAbsent(player.getUUID(),this);
    }








    public static void init(){
        registerServerNetworking();
    }


    public boolean innateOn =false;
    public boolean reversedOn=false;
    public boolean energyKeyOn=false;


    /**
     * if a key is gotten from the client, it will activate the function associated with the key on the server
     */
    public static void registerServerNetworking(){

        //make all techniques reversed
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.ActivateReversed.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                if(sorcerers.get(context.player().getUUID())!=null)
                    sorcerers.get(context.player().getUUID()).changeReversed();
            });
        });


        //Enable Innate Technique
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnableInnateTechnique.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {

                //if the player changes the state of their innate technique
                if(sorcerers.get(context.player().getUUID()) !=null)
                    sorcerers.get(context.player().getUUID()).activateInnate();
            });
        });

        //when a player scrolls
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.HandleScroll.TYPE,(payload, context)->{
            context.server().execute(()->{
                Sorcery sorcery =Limitless.sorcerers.get(context.player().getUUID());
                if(sorcery==null) return;
                if(sorcery.energyKeyOn){
                    sorcery.changeOutput(payload.scrollAmount());
                }

                if(sorcery.innateOn){
                    sorcery.changeInnateAmount(payload.scrollAmount());
                }
            });
        });

        //when the energy key is pressed, handle that
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOn.TYPE,(payload, context)->{
            context.server().execute(()->{
                if(sorcerers.get(context.player().getUUID())==null) return;
                sorcerers.get(context.player().getUUID()).energyKeyOn=true;
                PracticeMod.LOGGER.info("Energy Key: {}" ,sorcerers.get(context.player().getUUID()).energyKeyOn);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOff.TYPE,((payload, context) -> {
            context.server().execute(()->{
                if(sorcerers.get(context.player().getUUID())==null) return;
                sorcerers.get(context.player().getUUID()).energyKeyOn=false;
                PracticeMod.LOGGER.info("Energy Key: {}" ,sorcerers.get(context.player().getUUID()).energyKeyOn);
            });
        }));
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

    public void changeOutputPercent(double percentAmount){
        changeOutput((int)(percentAmount*PlayerInfo.playerInfoHashMap.get(player.getUUID()).maxCursedOutput));
    }

    public void changeOutput(double percentAmount){
        changeOutput((int)((percentAmount/20.0F)*PlayerInfo.playerInfoHashMap.get(player.getUUID()).maxCursedOutput));
    }



    public void activateInnate(){
        innateOn=!innateOn;
        PracticeMod.LOGGER.info("Activated innate {}", innateOn);
    }


    public void changeInnateAmount(double amountIncrease){
        PracticeMod.LOGGER.info("Changed Innate Amount by {}", amountIncrease);
    }



    public void changeReversed() {
        PracticeMod.LOGGER.info("Activated reversed");
        reversedOn=!reversedOn;
    }


    private void changeOutput(int amount){
        PlayerInfo playerInfo = PlayerInfo.playerInfoHashMap.get(player.getUUID());

        if(playerInfo.cursedOutput+amount>=playerInfo.maxCursedOutput){
            playerInfo.cursedOutput= playerInfo.maxCursedOutput;
        }
        else if(playerInfo.cursedOutput+amount<0){
            playerInfo.cursedOutput=0;
        }
        else {
            playerInfo.cursedOutput += amount;
        }
        PracticeMod.LOGGER.info("Amount {}", playerInfo.cursedOutput);
    }



    public void onUseEnergy(UUID playerUUID,int energyUsed){
        PlayerInfo playerInfo = PlayerInfo.playerInfoHashMap.get(player.getUUID());
        int addedValue=0;
        double multipliedValue=1;

        for(Ability abi:sorcerers.get(playerUUID).abilities){
            if(abi instanceof EffectsCursedEnergyUsage ability) {
                addedValue+=ability.getEnergyAdd();
                multipliedValue*=ability.getEnergyMultiply();
            };
        }

        playerInfo.cursedOutput-=(int)((energyUsed*(playerInfo.cursedEfficiency/100.0)*multipliedValue)+addedValue);

    }

    // If the player starts getting emotional they increase in power
    // To be added next update
    public void onPetDeath(){

    }










    static void sorceryClientInit(AbstractClientPlayer player){

    }




    //NOT RECOMMENDED TO USE
    @Deprecated
    public ServerPlayer player;


}
