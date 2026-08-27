package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.key.HandleKeybinds;
import com.qstorm.packets.Packet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
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

    private ComboKey(int id){
        this.id=id;
    }

    public static void init(){

    }

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

    /**
     * 1 = key1
     * 2 = key2
     * 3 = key3
     * 4 = key4
     * 5 = wait
     * used for rendering
     */
    public int id;
    int timeSinceLastPressed = 0;//ticks since the previous button was pressed, if it reaches max combo value then it ends
    // timeRequiredToContinue has been moved to Combo class
    // int timeRequiredToContinue=0;//ticks required for this keybind to count, basically
    //old key pressed -> timer for this key starts, if that time limit is over the time require to continue, then the combo will reset


    public int getTimeSinceLastPressed() {
        return timeSinceLastPressed;
    }

    /**
     * update the timeSinceLastPressed value of each key
     * must happen after checking
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


        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK1.TYPE,HandleKeybinds.AttackJJK1.CODEC);
        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK2.TYPE,HandleKeybinds.AttackJJK2.CODEC);
        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK3.TYPE,HandleKeybinds.AttackJJK3.CODEC);
        PayloadTypeRegistry.playC2S().register(HandleKeybinds.AttackJJK4.TYPE,HandleKeybinds.AttackJJK4.CODEC);




        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK1.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key1.get(context.player().getUUID()).keyDown();

                Combo.handleServerSideComboLogic(context);
            });
        });


        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK2.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key2.get(context.player().getUUID()).keyDown();
                Combo.handleServerSideComboLogic(context);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK3.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key3.get(context.player().getUUID()).keyDown();
                Combo.handleServerSideComboLogic(context);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(HandleKeybinds.AttackJJK4.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                key4.get(context.player().getUUID()).keyDown();
                Combo.handleServerSideComboLogic(context);
            });
        });



        //Doesn't use data so no identifier needed
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.execute(ComboKey::tickUpdateComboKeys);
        });

        ServerTickEvents.END_SERVER_TICK.register(PracticeMod.RESET,server -> {
            ComboKey.reset();
        });


    }

    /**
     * when player joins the server
     */
    public static void registerPlayer(UUID uuid){
        key1.put(uuid,new ComboKey(1));
        key2.put(uuid,new ComboKey(2));
        key3.put(uuid,new ComboKey(3));
        key4.put(uuid,new ComboKey(4));
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

    public static void reset(UUID uuid) {
        key1.get(uuid).keyUp();
        key2.get(uuid).keyUp();
        key3.get(uuid).keyUp();
        key4.get(uuid).keyUp();
    }


    public static void reset() {
        key1.forEach((uuid, comboKey) -> {
            comboKey.keyUp();
        });
        key2.forEach((uuid, comboKey) -> {
            comboKey.keyUp();
        });
        key3.forEach((uuid, comboKey) -> {
            comboKey.keyUp();
        });
        key4.forEach((uuid, comboKey) -> {
            comboKey.keyUp();
        });
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

    //TODO: idea, make certain keys not work when doing a combo and also have keys other then just the combo key's for more complex combo's
    //TODO: if you strenthen your hand using combos the tick after you hit somebody you hit a black flash

    //a list of combos that this player can render
    ArrayList<Combo> comboRenderedOptions;


    //if a key is pressed, a method is run that gets the list of combo's that can be run by that starting key. each key can only have 3 starting combos (any more aren't rendered)
    //ex: pressing 'z' with limitless gives the following list of combo's: Red
    //additionally pressing this key renders the key to the area above the combo list.
    // an image is generated and so is a xp bar

    //Key pressed, list of blackened letters that light up and have a boss bar. The letters will scroll up as the combo continues


    //options on how to choose which combo you see,
    // One, is to make a settings menu and you can set which combo you want to see.
    // The second is to make it so each combo can only have one key attached to it's start. ex if u press z and there are two options, nothing shows but if you press the next value in the combo whicho nly has one option, something shows
    // Three is to make a settings menu which says which combo should be listed first
    // Fourth is to make the combo listed by alphabetatized/length of time, where the longest lasts the longest
    // DOING FOURTH AS DEFAULT except there is a menu similar to the f3 settings menu where you choose which power and then chose weather it shows or not
    // as well as weather or not it is listed at the top
    // you can choose how to sort combo's as well but for now I'm gonna make it based on length of time
    // you can also start a combo that makes it stay at the side of your screen no matter what





}
