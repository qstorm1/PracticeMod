package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.packets.Packet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

public class Combo {




    //the combo class handles start and end stuff
    //combo.build().key1(3T).key4(4T).Key2(72T)
    //if press key -> packet is sent that takes the keyID
    //server has player so it will turn on the correct key based on the player, this data is stored in the HashMap above
    //a combo will check if the timeSinceLastPressed of the previous key is below the minimum time of the current one
    //if true it will update to the next value

    //Basic Elements
    //the keys required for the combo to work
    public ArrayList<ComboKey> comboKeys=new ArrayList<>();
    public String name;
    //the time you need to press this key to count to combo
    public static ArrayList<Integer> timeRequiredToContinue= new ArrayList<>();
    //the amount into the combo
    int current=0;

    //server side only
    public static HashMap<UUID,ArrayList<Combo>> playerCombos = new HashMap<>();


    //the player this combo is attached to (used to detect ComboKey's)
    UUID playerUUID;
    ServerPlayer serverPlayer;

    //TODO: all player's keys need to be reset at the end of the tick but after all of the other stuff happens

    /**
     * ASSUME THAT COMBO KEYS HAVE BEEN REGISTERED AND ARE FUCNTIONAL
     */
    private Combo(Player player,String name){
        playerUUID =player.getUUID();



        this.name=name;
        if(player instanceof ServerPlayer) {
            this.serverPlayer = (ServerPlayer) player;
        }

        //add to the servers list of players
        playerCombos.computeIfAbsent(playerUUID, k -> new ArrayList<>());
        playerCombos.get(playerUUID).add(this);


        //update the state of the combo every tick from the server
        ServerTickEvents.END_SERVER_TICK.register(PracticeMod.USES_DATA,server -> {
            server.execute(this::updateComboStatus);
        });
    }

    //TODO: replace name with translatable
    public static Combo build(Player player,String name){
        return new Combo(player,name);
    }


    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey1(int ticksForContinue){
        comboKeys.add(ComboKey.key1.get(playerUUID));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }


    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey2(int ticksForContinue){
        comboKeys.add(ComboKey.key2.get(playerUUID));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }
    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey3(int ticksForContinue){
        comboKeys.add(ComboKey.key3.get(playerUUID));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }
    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey4(int ticksForContinue){
        comboKeys.add(ComboKey.key4.get(playerUUID));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }



    //updateCombo
    //REQUIRED FOR CODE TO TICK
    //so we only want to reset the combo if
    //ALL STUFF HERE HAPPENS IN SERVER TICK
    public void updateComboStatus(){
        if(!checkBefore()){
            resetCombo();

        }


        if(comboKeys.get(current).keyDown){
            if(checkBefore()){
                if(current>=comboKeys.size()-1){
                    doComboAction();
                }
                else {
                    nextKey();
                }
            }
            else{
                resetCombo();
            }

        }



    }



    /**
     * TODO: waiting ticks
     * TODO: when combo ends say so in chat
     * checks if next combo key was pressed quickly enough
     * @return if the combo should continue
     */
    private boolean checkBefore(){
        if(current==0){
            return true;
        }
        //if the current combo key was pressed before the time limit ended
        //ex current=1, key[0] starts ticking up from 0, if key[0] goes past key[1]'s limit (timeRequiredToContinue)
        //then false is returned and the combo ends
        //this usually happens automatically but is only run just in case
        if(comboKeys.get(current-1).timeSinceLastPressed<=timeRequiredToContinue.get(current)){
            return true;
        }
        else{
            return false;
        }
    }



    public void nextKey(){
        current++;

    }

    public void resetCombo(){
        current=0;
        resetClientRenderFromServer();
        PracticeMod.LOGGER.info("reset combo");
    }

    public void doComboAction(){
        PracticeMod.LOGGER.info("did combo thing ig");
        resetCombo();
    }
    public void finish(){

    }
    public int getCurrentTimeSinceLastPressed() {
        return this.comboKeys.get(current).timeSinceLastPressed;
    }




    public static final Comparator<Combo> alphabeticOrder = Comparator.comparing((combo -> combo.name));
    public static final Comparator<Combo> highestTickTime = Comparator.comparing(Combo::getCurrentTimeSinceLastPressed).reversed();
    public static Comparator<Combo> compMode = highestTickTime;


    //the clients last key state
    private static int lastKeyID=1;
    private static Combo lastCombo;
    public void resetClientRenderFromServer(){
        Combo.handleServerSideComboRenderingLogic(serverPlayer,lastKeyID,Combo.playerCombos.get(playerUUID));
    }

    public static void handleServerSideComboRenderingLogic(ServerPlayer player, int keyPressed, ArrayList<Combo> combosPlayerHas){


        //find max current
        ArrayList<Combo> updatedComboList= new ArrayList<>();
        int currentMax=0;
        for(Combo combo:combosPlayerHas)
            if(combo.current>currentMax) currentMax=combo.current;

        boolean resetCall=false;
        //reset
        if (keyPressed==0) {
            keyPressed=lastKeyID;
            resetCall=true;
        }
        else
            lastKeyID=keyPressed;

        for(Combo combo:combosPlayerHas)
            if(combo.current==currentMax&&combo.comboKeys.get(currentMax).id==keyPressed)
                updatedComboList.add(combo);

        if(resetCall&&updatedComboList.contains(lastCombo)) updatedComboList.remove(lastCombo);

        //we handle this
//        if(updatedComboList.isEmpty()){
//            return;
//        }


        updatedComboList.sort(compMode);

        ArrayList<String> listOfStrings = new ArrayList<>();
        ArrayList<Integer> listOfIntegers = new ArrayList<>();

        if(updatedComboList.isEmpty()){
            ServerPlayNetworking.send(player, new Packet.ComboRenderInfoS2C(listOfStrings,listOfIntegers));
            return;
        }

        for(Combo combo:updatedComboList){
            listOfStrings.add(combo.name);
        }
        for(int i = updatedComboList.getFirst().current; i<updatedComboList.getFirst().comboKeys.size();i++){
            listOfIntegers.add(updatedComboList.getFirst().comboKeys.get(i).id);
        }

        lastCombo=updatedComboList.getFirst();

        ServerPlayNetworking.send(player, new Packet.ComboRenderInfoS2C(listOfStrings,listOfIntegers));
    }










}
