package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.key.InitializeBindings;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
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

    //each key currently instantiated, once per player, to add to a combo do .key.get(player) maybe switch to UUID or something idk
    public static HashMap<Player,ComboKey> key1= new HashMap<>();
    public static HashMap<Player,ComboKey> key2= new HashMap<>();
    public static HashMap<Player,ComboKey> key3= new HashMap<>();
    public static HashMap<Player,ComboKey> key4= new HashMap<>();

    //the combo class handles start and end stuff
    //combo.build().key1(3T).key4(4T).Key2(72T)
    //if press key -> packet is sent that takes the keyID
    //server has player so it will turn on the correct key based on the player, this data is stored in the HashMap above
    //a combo will check if the timeSinceLastPressed of the previous key is below the minimum time of the current one
    //if true it will update to the next value


    boolean keyDown;
    int timeSinceLastPressed = 0;//ticks since the previous button was pressed, if it reaches max combo value then it ends
    int timeRequiredToContinue=0;//ticks required for this keybind to count, basically
    //old key pressed -> timer for this key starts, if that time limit is over the time require to continue, then the combo will reset
    //TODO: if a player starts a combo but then stops typing, they probably expect that combo to automatically reset, make sure this happens


    /**
     * when player joins the server
     */
    public void registerPlayer(Player player){

    }

    /**
     * when player leaves the server
     */
    public void deregisterPlayer(){

    }

    public void tick(){
        timeSinceLastPressed++;
    }



    public void keyChange(){
        if(keyDown){
            keyUp();
        }
        else{
            keyDown();
        }
    }
    public void keyDown(){
        timeSinceLastPressed=0;
        keyDown=false;
    }

    public void keyUp(){
        keyDown=true;
    }

}
