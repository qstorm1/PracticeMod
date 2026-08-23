package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.key.HandleKeybinds;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

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

    //@ERROR TODO: keyDown doesn't go up
    boolean keyDown;
    int timeSinceLastPressed = 0;//ticks since the previous button was pressed, if it reaches max combo value then it ends
    // timeRequiredToContinue has been moved to Combo class
    // int timeRequiredToContinue=0;//ticks required for this keybind to count, basically
    //old key pressed -> timer for this key starts, if that time limit is over the time require to continue, then the combo will reset
    //TODO: if a player starts a combo but then stops typing, they probably expect that combo to automatically reset, make sure this happens


    /**
     * update the timeSinceLastPressed value of each key
     */
    public static void tickUpdateComboKeys(){
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
                PracticeMod.LOGGER.info("attack-key-1.isDown()");
            });
        });
//{UUID@33615} "19487181-3859-391a-bf75-143cc8396d12" -> {ComboKey@33616}
        //{UUID@33617} "24b1aadd-92ce-4423-8517-114eab65e1d5" -> {ComboKey@33618}
        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK2.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key2.get(context.player().getUUID()).keyDown();
                PracticeMod.LOGGER.info("attack-key-2.isDown()");
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK3.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key3.get(context.player().getUUID()).keyDown();
                PracticeMod.LOGGER.info("attack-key-3.isDown()");
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK4.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key4.get(context.player().getUUID()).keyDown();
                PracticeMod.LOGGER.info("attack-key-4.isDown()");
            });
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.execute(ComboKey::tickUpdateComboKeys);
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
        keyDown=true;
    }

    public void keyUp(){
        keyDown=false;
    }

}
