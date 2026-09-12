package com.qstorm.powers.cursedtechnique;

import com.qstorm.PracticeMod;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.packets.Packet;
import com.qstorm.powers.Ability;
import com.qstorm.powers.EffectsCursedEnergyUsage;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.ScrollableInnate;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
     * the cursed energy used per tick innate technique is active
     */
    int constantAbilityTickCost =0;


    /**
     * Abilities of player
     * get(0) = Innate technique
     */
    public ArrayList<Ability> abilities = new ArrayList<>();


    //The domain of this ability
    public Domain domain;

    public UUID playerUUID;



    //a global list of each player's sorcery data (All sorcery objects)
    public static HashMap<UUID,Sorcery> sorcerers = new HashMap<>();



    //born luck is a percentage that represents how lucky are your initial stat growths
    //increase luck represents and increase in luck
    public Sorcery(Ability innate, Player player, int domainEnergyCost, int constantAbilityTickCost){
        this(player);



        this.domainEnergyCost=domainEnergyCost;
        this.constantAbilityTickCost = constantAbilityTickCost;


        //if for whatever reason the arraylist already has stuff in it, reset it
        if(abilities.size()>1){
            abilities = new ArrayList<>();
        }
        abilities.add(innate);

    }



    private Sorcery(Player player){
        playerUUID=player.getUUID();
        sorcerers.putIfAbsent(playerUUID,this);


        if(player instanceof ServerPlayer serverPlayer) {
            this.player = serverPlayer;
            ServerPlayNetworking.send(serverPlayer, new Packet.ActivateSorceryRender(
                    PlayerInfo.playerInfoHashMap.get(playerUUID).cursedEnergy,
                    PlayerInfo.getOutputAsPercent(playerUUID)));

        }
        ServerTickEvents.END_SERVER_TICK.register(PracticeMod.USES_DATA,
                this::tick);


    }








    public boolean innateOn =false;
    public boolean reversedOn=false;
    public boolean energyKeyOn=false;

    public static void onActivateReversed(UUID playerUUID){
        if(sorcerers.get(playerUUID)!=null)
            sorcerers.get(playerUUID).changeReversed();
    }

    public static void enableInnateTechnique(UUID playerUUID){
        //if the player changes the state of their innate technique
        if(sorcerers.get(playerUUID) !=null)
            sorcerers.get(playerUUID).activateInnate();
    }

    public static void handleScroll(UUID playerUUID,double scrollAmount){
        Sorcery sorcery =sorcerers.get(playerUUID);
        if(sorcery==null) return;
        if(sorcery.energyKeyOn){
            sorcery.changeOutput(scrollAmount);
        }

        if(sorcery.innateOn&&sorcery instanceof ScrollableInnate scrollableInnate){
            scrollableInnate.changeInnateAmount(scrollAmount);
        }
        for(Ability s:sorcerers.get(playerUUID).abilities){
            if(s.isScroll){
                s.onScroll(scrollAmount);
            }
        }
    }

    public static void energyKeyChange(UUID playerUUID,boolean on){
        if(sorcerers.get(playerUUID)==null) return;
        sorcerers.get(playerUUID).energyKeyOn=on;
        //PracticeMod.LOGGER.info("Energy Key: {}" ,sorcerers.get(context.player().getUUID()).energyKeyOn);

    }

    /**
     * if a key is gotten from the client, it will activate the function associated with the key on the server
     */
    public static void registerServerNetworking(){

        //make all techniques reversed
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.ActivateReversed.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                onActivateReversed(context.player().getUUID());
            });
        });


        //Enable Innate Technique
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnableInnateTechnique.TYPE, (payload, context) -> {
            //says to run it on server
            context.server().execute(() -> {
                enableInnateTechnique(context.player().getUUID());
            });
        });

        //when a player scrolls
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.HandleScroll.TYPE,(payload, context)->{
            context.server().execute(()->{
                handleScroll(context.player().getUUID(),payload.scrollAmount());
            });
        });

        //when the energy key is pressed, handle that
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOn.TYPE,(payload, context)->{
            context.server().execute(()->{
                energyKeyChange(context.player().getUUID(), true);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.EnergyKeyOff.TYPE,((payload, context) -> {
            context.server().execute(()->{
                energyKeyChange(context.player().getUUID(), false);

            });
        }));
    }



    public void tick(MinecraftServer context){
        if(innateOn){
            abilities.getFirst().isTicked=true;
        }else{
            abilities.getFirst().isTicked=false;
        }
        for (Ability ability:abilities){
            if(ability.isTicked){
                ability.tick(context);
            }
        }
    }



    public void activateInnate(){
        innateOn=!innateOn;
        PracticeMod.LOGGER.info("Activated innate {}", innateOn);
    }




    public void changeReversed() {
        PracticeMod.LOGGER.info("Activated reversed");
        reversedOn=!reversedOn;
    }
    //TODO: remember that the cost of energy increases faster then the power but both increase exponentially



    public void changeOutputPercent(double percentAmount){
        changeOutput((int)(percentAmount*PlayerInfo.playerInfoHashMap.get(playerUUID).maxCursedOutput));
    }

    public void changeOutput(double percentAmount){
        changeOutput((int)((percentAmount/20.0F)*PlayerInfo.playerInfoHashMap.get(playerUUID).maxCursedOutput));
    }


    private void changeOutput(int amount){
        //you can only change the output on server, that means these values need to be updated through packets
        //this system already is in place but its kinda funky check PracticeModClient to see
        if(Minecraft.getInstance().player.level().isClientSide()) return;

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
        PracticeMod.LOGGER.info("Cursed Output = {}", playerInfo.cursedOutput);
        ServerPlayNetworking.send(player,new Packet.ActivateSorceryRender(
                PlayerInfo.playerInfoHashMap.get(player.getUUID()).cursedEnergy,
                PlayerInfo.getOutputAsPercent(player.getUUID())));

    }





    static void innateDomain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used innate domain");
    }

    public void domain(ServerPlayer attacker){
        PracticeMod.LOGGER.info("Used domain");
    }




    // If the player starts getting emotional they increase in power
    // To be added next update
    public void onPetDeath() {}




    //NOT RECOMMENDED TO USE
    public ServerPlayer player;


}
