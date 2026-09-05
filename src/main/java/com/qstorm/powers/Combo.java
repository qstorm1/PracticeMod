package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.packets.Packet;
import com.qstorm.powers.cursedtechnique.Sorcery;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    public ArrayList<Integer> timeRequiredToContinue= new ArrayList<>();
    //the amount into the combo
    int current=0;



    //server side only
    public static HashMap<UUID,ArrayList<Combo>> playerCombos = new HashMap<>();
    ArrayList<Integer> times = new ArrayList<>();

    Sorcery attachment;

    //the player this combo is attached to (used to detect ComboKey's)
    UUID playerUUID;
    ServerPlayer serverPlayer;

    BiConsumer<ArrayList<Integer>,ServerPlayer> action;



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
        ServerTickEvents.END_SERVER_TICK.register(PracticeMod.USES_DATA, server -> {
            server.execute(this::tick);
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
    public Combo addDetectedKey1(int ticksForContinue){
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

    public Combo addDetectedKey2(int ticksForContinue){
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

    public Combo addDetectedKey3(int ticksForContinue){
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

    public Combo addDetectedKey4(int ticksForContinue){
        comboKeys.add(ComboKey.key4.get(playerUUID));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }

    public Combo addWaitKey(int ticksForContinue){
        return this;
    }
    public Combo addDetectedWaitKey(int ticksForContinue){
        return this;
    }


    public Combo setAction(Consumer<ServerPlayer> runnable){
        this.action= (integers, player) -> {
            runnable.accept(player);
        };
        return this;
    }

    public Combo setAction(BiConsumer<ArrayList<Integer>,ServerPlayer> runnable){
        this.action=runnable;
        return this;
    }


    public Combo setAction(Ability ability){
        this.setAction(ability::Do);
        return this;
    }



    public void tick(){
        //if the combo ran out of time, reset it
        if(!checkBefore()){
            resetCombo();
        }
    }


    //updateCombo
    //REQUIRED TO RUN ON EVERY COMBO WHEN A KEY IS PRESSED
    //so we only want to reset the combo if
    //ALL STUFF HERE HAPPENS IN SERVER TICK
    public void checkIfContinue(int keyPressed){
        lastKeyID = keyPressed;


        //if the comboKey doesn't match the key pressed
        if(comboKeys.get(current).id==keyPressed){
            //TODO: remove if everything works to see if neccessary (I don't think it is)
            if(checkBefore()){

                //this could be replaced with ==, but >= just in case some wierd thread error
                //if the current value that was pressed is the last value, do the action
                if(current>=comboKeys.size()-1){
                    doComboAction();
                }
                else {
                    //move to the next state of the combo
                    nextKey();
                }
            }
            else{
                resetCombo();
            }

        }
        else{
            //reset the combo if the key pressed was wrong
            resetCombo();
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
        action.accept(times,serverPlayer);
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



    //HUD RENDERING

    /**
     * the last key that was pressed on this server?
     */
    private int lastKeyID=0;

    public void resetClientRenderFromServer(){
        Combo.handleServerSideComboRendering(serverPlayer,lastKeyID,true);
    }



    /**
     * @param player the player that is going to render the HUD
     * @param keyPressed the key that the player pressed
     */

    public static void handleServerSideComboRendering(ServerPlayer player, int keyPressed,boolean isReset){


        ArrayList<Combo> combosPlayerHas = playerCombos.get(player.getUUID());
        ArrayList<Combo> updatedComboList= new ArrayList<>();



        //find the current biggest combo, combos with the highest current will be rendered
        //if this is a reset call, the current of the reset will be 0 therefore not being considered as the main combo
        int currentMax=0;
        for(Combo combo:combosPlayerHas) {
            if (combo.current > currentMax) currentMax = combo.current;
        }


        //make the updated list have all the combos with the highest comboKey
        for(Combo combo:combosPlayerHas)
            if(combo.current==currentMax&&combo.comboKeys.get(currentMax).id==keyPressed)
                updatedComboList.add(combo);





        //sort combo list by whatever sorting method (ex alphabetical -> A is the one at the top of the render)
        updatedComboList.sort(compMode);

        //data to send to client
        ArrayList<String> listOfStrings = new ArrayList<>();
        ArrayList<Integer> listOfIntegers = new ArrayList<>();

        //if we reset and there are no other combos working
        if(isReset&&currentMax==0){
            ServerPlayNetworking.send(player, new Packet.ComboRenderInfoS2C(listOfStrings,listOfIntegers));
            return;
        }

        //if the data is empty, send empty data which the client will recognize as a reset call
        if(updatedComboList.isEmpty()){
            ServerPlayNetworking.send(player, new Packet.ComboRenderInfoS2C(listOfStrings,listOfIntegers));
            return;
        }

        //update values correctly
        for(Combo combo:updatedComboList){
            listOfStrings.add(combo.name);
        }
        for(int i = updatedComboList.getFirst().current; i<updatedComboList.getFirst().comboKeys.size();i++){
            listOfIntegers.add(updatedComboList.getFirst().comboKeys.get(i).id);
        }



        //send rendering data to server
        ServerPlayNetworking.send(player, new Packet.ComboRenderInfoS2C(listOfStrings,listOfIntegers));
    }



}
