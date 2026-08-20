package com.qstorm.powers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.function.Function;

public class Combo {
    //5 key
    public ComboKey[] comboKeys;
    int current=0;

    //curently 4 keys to start combos
    public ComboKey key1;
    public ComboKey key2;
    public ComboKey key3;
    public ComboKey key4;

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
