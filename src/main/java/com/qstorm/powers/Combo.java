package com.qstorm.powers;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class Combo {
    //5 key
    public ComboKey[] comboKeys;
    int current=0;

    //updates combo
    public void checkCombo(){
        switch (comboKeys[current].check()){
            //if timer is run out
            case(0) -> {
                endCombo();
            }//if key is pressed in the correct amount of time
            case(1) -> {
                //if at the end of combo
                if(current>comboKeys.length) doAction();
                else current++;
            }//if nothing happens
            case(2) -> {

            }

        }
    }

    public void endCombo(){

    }
    public void doAction(){

    }

    public static Combo createCombo(ComboKey... mappings){


    }

    private Combo(){

    }
}
