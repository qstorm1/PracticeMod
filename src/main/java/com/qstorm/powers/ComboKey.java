package com.qstorm.powers;

import com.qstorm.key.InitializeBindings;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

/**
 * A key that can be used in a combo
 * When ClientTickEnds.register(){
 *
 * }
 * Map (player -> ComboKey -> each combo key)
 * Player presses -> server(player) -> ComboKey.Key = value
 * one instance per client side
 */
public class ComboKey{


    double ticksSincePrevKey =0;//ticks since the previous button was pressed, if it reaches max combo value then it ends
    double minimumTimeRequired;
    KeyMapping value;
    boolean keyDown;

    //each key is only instanciated once
//    public final ComboKey key1=new ComboKey(InitializeBindings.attack1);
//    public final ComboKey key2=new ComboKey(InitializeBindings.attack2);
//    public final ComboKey key3=new ComboKey(InitializeBindings.attack3);
//    public final ComboKey key4=new ComboKey(InitializeBindings.attack4);

    public static HashMap<Player,ComboKey> key1= new HashMap<>();
    public static HashMap<Player,ComboKey> key2= new HashMap<>();
    public static HashMap<Player,ComboKey> key3= new HashMap<>();
    public static HashMap<Player,ComboKey> key4= new HashMap<>();


    /**
     * @param value key needed to press for combo to continue
     * @param minimumTickRequired the ticks it takes for the key to count to the combo
     */
    private ComboKey(KeyMapping value,double minimumTickRequired){
        this.value=value;
        this.minimumTimeRequired=minimumTickRequired;
    }

    private ComboKey(KeyMapping value){
        this.value=value;
    }



    public ComboKey setTick(double ticksSincePrevKey){
        this.minimumTimeRequired=ticksSincePrevKey;
        return this;
    }

    public ComboKey setStartKey(){
        return this;
    }

    public ComboKey setEndKey(){
        return this;
    }



    public void keyChange(){
        keyDown=!keyDown;
    }
    public void keyDown(){
        keyDown=false;
    }

    public void keyUp(){
        keyDown=true;
    }





    /**
     * checks if combo has moved forward or ended
     * @return 0 if the combo ends, 1 if the combo moves next, and 2 if the combo stays how it is
     */
    public int check(){
        if(ticksSincePrevKey >=minimumTimeRequired){
            return 0;
        }

        ticksSincePrevKey++;
        if(keyDown){
            return 1;
        }
        return 2;
    }

}
