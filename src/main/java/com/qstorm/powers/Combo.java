package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.packets.Packet;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

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

    //when you run .addDetectionKey() if a detection key is pressed it will add ticksSinceLastPressed to this
    //on reset, the arraylist is cleared
    public ArrayList<Integer> detectionKey = new ArrayList<>();


    //do an action whenever current=Integer
    public HashMap<Integer,Ability> actionOnKey = new HashMap<>();
    //the amount into the combo
    int current=0;



    //server side only
    /**
     * All combos are stored in this hash map
     */
    public static HashMap<UUID,ArrayList<Combo>> playerCombos = new HashMap<>();





    //the player this combo is attached to (used to detect ComboKey's)
    UUID playerUUID;
    ServerPlayer serverPlayer;

    Ability action;



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
            server.execute(()->{
                this.tick(server.getPlayerList().getPlayer(playerUUID).level());
            });
        });

        ClientTickEvents.END_CLIENT_TICK.register(PracticeMod.USES_DATA,client ->{
            client.execute(()->{
                this.tick(client.level);
            });
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

    /**
     * A detected key is a key that does an action when played
     */
    public Combo addDetectedKey1(int ticksForContinue){
        this.addKey1(ticksForContinue);
        //when current = 3 check if detection key has the value 3, if it does then add it to the abilities detection array
        this.detectionKey.add(comboKeys.size()-1);
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
        this.addKey2(ticksForContinue);
        //when current = 3 check if detection key has the value 3, if it does then add it to the abilities detection array
        this.detectionKey.add(comboKeys.size()-1);
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
        this.addKey3(ticksForContinue);
        //when current = 3 check if detection key has the value 3, if it does then add it to the abilities detection array
        this.detectionKey.add(comboKeys.size()-1);
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
        this.addKey4(ticksForContinue);
        //when current = 3 check if detection key has the value 3, if it does then add it to the abilities detection array
        this.detectionKey.add(comboKeys.size()-1);
        return this;
    }



    public Combo addWaitKey(int ticksForContinue){
        return this;
    }
    public Combo addDetectedWaitKey(int ticksForContinue){
        return this;
    }





    public boolean actionNull(){
        return action==null;
    }
    public Combo setAction(Ability ability){
        this.action=ability;
        return this;
    }

    public Combo keyAction(Ability ability){
        this.actionOnKey.put(comboKeys.size() - 1,ability);
        return this;
    }



    public void tick(Level level){
        //if the combo ran out of time, reset it
        if(!checkBefore()){
            boolean isClient = FabricLoader.getInstance().getEnvironmentType()==EnvType.CLIENT;
            resetCombo();
            resetClientHUD(level);
        }
    }


    /**
     * Checks if the combo should continue after pressing ComboKey keyPressed.id
     * Note that this doesn't handle tick specific combo endings
     * @param keyPressed the key pressed
     */
    public void checkIfContinue(Level level,int keyPressed){
        lastKeyID = keyPressed;
        boolean isClient = level.isClientSide();

        //if the comboKey doesn't match the key pressed
        if(comboKeys.get(current).id==keyPressed){
            //this could be replaced with ==, but >= just in case some wierd thread error
            //if the current value that was pressed is the last value, do the action
            if(current>=comboKeys.size()-1){
                PracticeMod.LOGGER.info("Did action for Combo {} | is client: {}",this.name,isClient);
                doComboAction(level);
            }
            else {
                //move to the next state of the combo
                PracticeMod.LOGGER.info("Move to next key in Combo {} | is client: {}",this.name,isClient);
                nextKey();
            }

        }
        else{
            //reset the combo if the key pressed was wrong
            PracticeMod.LOGGER.info("Reset Combo because key pressed was wrong {} | is client: {}",this.name,isClient);
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
        for (Integer i : this.detectionKey) {
            if(i==current){
                this.action.times.add(this.comboKeys.get(current).timeSinceLastPressed);
            }
        }
        if(this.actionOnKey.get(current)!=null){
            this.actionOnKey.get(current).Do(serverPlayer);
        }
        current++;

    }

    static boolean isClientStatic = FabricLoader.getInstance().getEnvironmentType()==EnvType.CLIENT;
    public void resetCombo(){
        current=0;
        if(!this.detectionKey.isEmpty())
            this.action.times.clear();


        PracticeMod.LOGGER.info("reset combo");
    }

    public void doComboAction(Level level){
        action.Do(serverPlayer);
        resetCombo();
        resetClientHUD(level);
    }

    /**
     * returns the highest current value of this player
     * @param playerUUID
     */
    public static int findLongestCombo(UUID playerUUID){
        int max = 0;
        for(Combo combo:playerCombos.get(playerUUID)){
            if(combo.current>max){
                max=combo.current;
            }
        }
        return max;
    }


    public int getCurrentTimeSinceLastPressed() {
        return this.comboKeys.get(current).timeSinceLastPressed;
    }








    //HUD RENDERING

    /**
     * the last key that was pressed on this server?
     * lokey idk i don't remember
     */
    private int lastKeyID=1;

    public void resetClientHUD(Level level){
        ComboClient.getComboBoxRenderValues(level,playerUUID,lastKeyID,true);
    }





}
