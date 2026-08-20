package com.qstorm.powers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.function.Function;

public class Combo {

    //the combo class handles start and end stuff
    //combo.build().key1(3T).key4(4T).Key2(72T)
    //if press key -> packet is sent that takes the keyID
    //server has player so it will turn on the correct key based on the player, this data is stored in the HashMap above
    //a combo will check if the timeSinceLastPressed of the previous key is below the minimum time of the current one
    //if true it will update to the next value


    //the combo
    public ArrayList<ComboKey> comboKeys=new ArrayList<>();
    //the amount into the combo
    int current=0;

    public Combo build(Player player){
        return new Combo(player);
    }



    Player player;

    /**
     * @param ticksForContinue the required ticks needed before the combo fails
     */
    public Combo addKey1(int ticksForContinue){
        comboKeys.add(ComboKey.key1.get(player));
        return this;
    }


    /**
     * @param ticksForContinue the required ticks needed before the combo fails
     */
    public Combo addKey2(int ticksForContinue){

        return this;
    }
    /**
     * @param ticksForContinue the required ticks needed before the combo fails
     */
    public Combo addKey3(int ticksForContinue){

        return this;
    }
    /**
     * @param ticksForContinue the required ticks needed before the combo fails
     */
    public Combo addKey4(int ticksForContinue){

        return this;
    }



    /**
     * ASSUME THAT COMBO KEYS HAVE BEEN REGISTERED AND ARE FUCNTIONAL
     */
    private Combo(Player player){
        this.player=player;
    }

    /**
     * checks if next combo key was pressed quickly enough
     * @return
     */
    private boolean checkBefore(){
        if(current==0){
            return true;
        }
        if(comboKeys[current-1].timeSinceLastPressed>){

        }
        else{
            return false;
        }
    }






    //updates combo must run every tick

    /**
     * Checks if combo has ended
     * @return returns if combo is sucessful
     */
    public boolean checkCombo(){
        switch (comboKeys[current].check()){
            //if timer is run out
            case(0) -> {
                endCombo();
            }//if key is pressed in the correct amount of time
            case(1) -> {
                //if at the end of combo
                if(current>comboKeys.length) return true;
                else current++;
            }//if nothing happens
            case(2) -> {

            }

        }
        return false;
    }

    //the client will send the server a request, that request will trigger
    public void endCombo(){

    }
    public void doAction(){
        //action.apply();
    }

    public static Combo createCombo(ComboKey... mappings){
        return new Combo(mappings);

    }

    private Combo(ComboKey[] keys){
        comboKeys=keys;
    }
}
