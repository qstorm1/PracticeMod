package com.qstorm.powers;

import com.qstorm.item.armor.LaserEyes;
import com.qstorm.key.HandleKeybinds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

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

    private ComboKey(){}

    //each key currently instantiated, once per player, to add to a combo do .key.get(player) maybe switch to UUID or something idk
    public static HashMap<UUID,ComboKey> key1= new HashMap<>();
    public static HashMap<UUID,ComboKey> key2= new HashMap<>();
    public static HashMap<UUID,ComboKey> key3= new HashMap<>();
    public static HashMap<UUID,ComboKey> key4= new HashMap<>();

    //the combo class handles start and end stuff
    //combo.build().key1(3T).key4(4T).Key2(72T)
    //if press key -> packet is sent that takes the keyID
    //server has player so it will turn on the correct key based on the player, this data is stored in the HashMap above
    //a combo will check if the timeSinceLastPressed of the previous key is below the minimum time of the current one
    //if true it will update to the next value


    boolean keyDown;
    int timeSinceLastPressed = 0;//ticks since the previous button was pressed, if it reaches max combo value then it ends
    // timeRequiredToContinue has been moved to Combo class
    // int timeRequiredToContinue=0;//ticks required for this keybind to count, basically
    //old key pressed -> timer for this key starts, if that time limit is over the time require to continue, then the combo will reset
    //TODO: if a player starts a combo but then stops typing, they probably expect that combo to automatically reset, make sure this happens


    /**
     * update the timeSinceLastPressed value of each key
     */
    public static void tickComboKeys(){
        key1.forEach((uuid, comboKey) -> {
            comboKey.timeSinceLastPressed++;
        });
        key2.forEach((uuid, comboKey) -> {
            comboKey.timeSinceLastPressed++;
        });
        key3.forEach((uuid, comboKey) -> {
            comboKey.timeSinceLastPressed++;
        });
        key4.forEach((uuid, comboKey) -> {
            comboKey.timeSinceLastPressed++;
        });
    }



    public static void setupServersideManagement(){
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK1.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key1.get(context.player().getUUID()).keyDown();
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK2.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key2.get(context.player().getUUID()).keyDown();
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK3.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key3.get(context.player().getUUID()).keyDown();
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK4.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key4.get(context.player().getUUID()).keyDown();
            });
        });

    }

    /**
     * when player joins the server
     */
    public static void registerPlayer(UUID uuid){
        key1.put(uuid,new ComboKey());
        key2.put(uuid,new ComboKey());
        key3.put(uuid,new ComboKey());
        key4.put(uuid,new ComboKey());
    }

    /**
     * when player leaves the server
     */
    public static void deregisterPlayer(UUID uuid){
        key1.remove(uuid);
        key2.remove(uuid);
        key3.remove(uuid);
        key4.remove(uuid);
    }

    public void tick(){
        timeSinceLastPressed++;
    }







    //key on/off management
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
