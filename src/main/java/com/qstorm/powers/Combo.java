package com.qstorm.powers;

import com.qstorm.PracticeMod;
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
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey1(int ticksForContinue){
        comboKeys.add(ComboKey.key1.get(player.getUUID()));
        return this;
    }


    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey2(int ticksForContinue){
        comboKeys.add(ComboKey.key2.get(player.getUUID()));
        return this;
    }
    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey3(int ticksForContinue){
        comboKeys.add(ComboKey.key3.get(player.getUUID()));
        return this;
    }
    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey4(int ticksForContinue){
        comboKeys.add(ComboKey.key4.get(player.getUUID()));
        return this;
    }

    //updateCombo
    public void updateComboStatus(){
        if(comboKeys.get(current).keyDown){
            if(checkBefore()){
                if(current==comboKeys.size()){
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

    public void nextKey(){
        current++;
    }

    public void resetCombo(){
        current=0;
        PracticeMod.LOGGER.info("reset combo");
    }

    public void doComboAction(){
        current=0;
        PracticeMod.LOGGER.info("did combo thing ig");
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
        if(comboKeys.get(current-1).timeSinceLastPressed<=comboKeys.get(current).timeRequiredToContinue){
            return true;
        }
        else{
            return false;
        }
    }



    /**
     * ASSUME THAT COMBO KEYS HAVE BEEN REGISTERED AND ARE FUCNTIONAL
     */
    private Combo(Player player){
        this.player=player;
    }
}
