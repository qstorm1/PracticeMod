package com.qstorm.powers;

import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Player;
import org.joml.Matrix3x2fStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

public class Combo {

    //the combo class handles start and end stuff
    //combo.build().key1(3T).key4(4T).Key2(72T)
    //if press key -> packet is sent that takes the keyID
    //server has player so it will turn on the correct key based on the player, this data is stored in the HashMap above
    //a combo will check if the timeSinceLastPressed of the previous key is below the minimum time of the current one
    //if true it will update to the next value


    //the keys required for the combo to work
    public ArrayList<ComboKey> comboKeys=new ArrayList<>();
    public static ArrayList<Integer> timeRequiredToContinue= new ArrayList<>();//the time you need to press this key to count to combo
    //the amount into the combo
    int current=0;

    //if need input replace with Consumer<Input type> and then run .accept(input)
    public Runnable action;


    public static Combo build(Player player){
        return new Combo(player);
    }
    //the player this combo is attached to (used to detect ComboKey's)
    Player player;

    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey1(int ticksForContinue){
        comboKeys.add(ComboKey.key1.get(player.getUUID()));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }


    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey2(int ticksForContinue){
        comboKeys.add(ComboKey.key2.get(player.getUUID()));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }
    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey3(int ticksForContinue){
        comboKeys.add(ComboKey.key3.get(player.getUUID()));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }
    /**
     * @param ticksForContinue the required ticks of waiting it takes for the combo to end before the combo fails
     */
    public Combo addKey4(int ticksForContinue){
        comboKeys.add(ComboKey.key4.get(player.getUUID()));
        timeRequiredToContinue.add(ticksForContinue);
        return this;
    }



    //updateCombo
    //REQUIRED FOR CODE TO TICK
    //so we only want to reset the combo if
    public void updateComboStatus(){
        if(!checkBefore()){
            resetCombo();

        }

        if(comboKeys.get(current).keyDown){
            if(checkBefore()){
                if(current>=comboKeys.size()-1){
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
        PracticeMod.LOGGER.info("did combo thing ig");
        resetCombo();
    }


    public void action(Runnable runnable){
        this.action=runnable;
    }

    public void finish(){

    }

    //must run on a clientInitializer
    //if the player is in the client side, then render to screen if the key has been pressed
    //if this method is called that means the player is doing a combo with this render
    public void handleRender(Player player, String renderVal) {
        player.level().isClientSide();

    }



    public static void render(GuiGraphics graphics, DeltaTracker tracker){
        int color = 0x80A8A8A8;//red
        int targetColor = 0xFF00FF00; // Green

        drawResizableRectangle(graphics,0.01,0.2,0.05,0.4,color);


    }

    public static void drawResizableRectangle(GuiGraphics graphics,double percentFromLeft,double percentFromTop,double percentWidth,double percentHeight, int color){

        graphics.fill(
                (int)(graphics.guiWidth()*percentFromLeft),
                (int)((graphics.guiHeight()*percentFromTop)),
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth)+1,
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight)+1,
                color);

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

    //TODO: all player's keys need to be reset at the end of the tick but after all of the other stuff happens

    /**
     * ASSUME THAT COMBO KEYS HAVE BEEN REGISTERED AND ARE FUCNTIONAL
     */
    private Combo(Player player){
        this.player=player;
        //update the state of the combo every tick from the server
        ServerTickEvents.END_SERVER_TICK.register(PracticeMod.USES_DATA,server -> {
            server.execute(this::updateComboStatus);
        });
    }
}
