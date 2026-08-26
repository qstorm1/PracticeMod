package com.qstorm.powers;

import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class Combo {

    //the combo class handles start and end stuff
    //combo.build().key1(3T).key4(4T).Key2(72T)
    //if press key -> packet is sent that takes the keyID
    //server has player so it will turn on the correct key based on the player, this data is stored in the HashMap above
    //a combo will check if the timeSinceLastPressed of the previous key is below the minimum time of the current one
    //if true it will update to the next value


    //the keys required for the combo to work
    public ArrayList<ComboKey> comboKeys=new ArrayList<>();
    public String name;
    public static ArrayList<Integer> timeRequiredToContinue= new ArrayList<>();//the time you need to press this key to count to combo
    //the amount into the combo
    int current=0;

    //if need input replace with Consumer<Input type> and then run .accept(input)
    public Runnable action;


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
    //ALL STUFF HERE HAPPENS IN SERVER TICK
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

    static Identifier stuffInBoxIdentifier = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"Combo-HUD-Box");
    static Identifier comboListIdentifier = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"Combo-List");
    static Identifier GUIBox = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"GUI-Box");

    public static final Identifier TEXTURECOMBO1 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/1.png");
    public static final Identifier TEXTURECOMBO2 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/2.png");
    public static final Identifier TEXTURECOMBO3 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/3.png");
    public static final Identifier TEXTURECOMBO4 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/4.png");


    public static final Identifier BLACKTEXTURECOMBO1 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/1.png");
    public static final Identifier BLACKTEXTURECOMBO2 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/2.png");
    public static final Identifier BLACKTEXTURECOMBO3 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/3.png");
    public static final Identifier BLACKTEXTURECOMBO4 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/4.png");



    static void getRender(){
        ClientPlayNetworking.registerGlobalReceiver(new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "test")),
                (payload, context) -> {
            context.client().execute(()->{
                String[] comboNames = null;
                int[] idOfKeysToRender = new int[5];


                if(idOfKeysToRender.length<1){
                    resetHUD();
                    return;
                }

                resetHUD();
                drawComboList(comboNames);
                renderComboBox();


                drawWorkingKeybind(idOfKeysToRender[0]);

                if(idOfKeysToRender.length>4){
                    //scroll based
                    drawKeybind(idOfKeysToRender[1],1,false);
                    drawKeybind(idOfKeysToRender[2],2,false);
                    drawKeybind(idOfKeysToRender[3],3,true);

                }
                else {
                    //non-scroll based
                    for(int i = 1;i<idOfKeysToRender.length;i++){
                        drawKeybind(idOfKeysToRender[i],i,false);
                    }

                }

            });
        });
    }




    private static void resetHUD(){

    }



    private static void drawComboList(String[] comboNames){
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,comboListIdentifier,(graphics,timeDelta)->{
            for(int i = 0 ;i<comboNames.length&&i<5;i++){
                graphics.drawString(Minecraft.getInstance().font, comboNames[i], graphics.guiWidth()/80, (int)(graphics.guiHeight()*3.0/4+i*graphics.guiHeight()/20.0), 0xFF000000);

            }
        });
    }


    /**
     * draws a black keybind at some part of the box
     * @param id the type of black box keybind
     * @param stage values 1,2, or 3 which represent the height of the
     * @param isTransparent is the value transparent (for top of scroll pane)
     */
    private static void drawKeybind(final int id,final int stage,final boolean isTransparent){
        final int black;
        if(isTransparent) {
            black = 0x80000000;
        }else{
            black=0xFF000000;
        }
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, stuffInBoxIdentifier,(graphics,timeDelta)->{
            switch (id){
                case 1 -> drawResizableImage(graphics,TEXTURECOMBO1,0.015,0.205+stage*0.19,0.04,0.08);

                case 2 -> drawResizableImage(graphics,TEXTURECOMBO2,0.015,0.205+stage*0.19,0.04,0.08);

                case 3 -> drawResizableImage(graphics,TEXTURECOMBO3,0.015,0.205+stage*0.19,0.04,0.08);

                case 4 -> drawResizableImage(graphics,TEXTURECOMBO4,0.015,0.205+stage*0.19,0.04,0.08);

            }

            drawResizableRectangle(graphics,0.015,0.205+stage*0.19,0.04,0.08,black);
            drawResizableBorder(graphics,0.015,0.205+stage*0.19,0.04,0.08,1,black);

        });
    }

    private static void drawWorkingKeybind(final int id){
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,stuffInBoxIdentifier,(graphics,tick)-> {
            switch (id) {
                case 1 -> drawResizableImage(graphics, TEXTURECOMBO1, 0.015, 0.205, 0.04, 0.08);

                case 2 -> drawResizableImage(graphics, TEXTURECOMBO2, 0.015, 0.205, 0.04, 0.08);

                case 3 -> drawResizableImage(graphics, TEXTURECOMBO3, 0.015, 0.205, 0.04, 0.08);

                case 4 -> drawResizableImage(graphics, TEXTURECOMBO4, 0.015, 0.205, 0.04, 0.08);
            }

            //TODO: make a xp bar underneath key representing length of time needed to press
        });
    }

    private static void renderComboBox(){
        final int transparent_middle = 0x80A8A8A8;
        final int border_color = 0x90000000;

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,GUIBox,(graphics,timeDelta)->{
            drawResizableRectangle(graphics,0.01,0.2,0.05,0.4,transparent_middle);
            drawResizableBorder(graphics,0.01,0.2,0.05,0.4,1,border_color);
        });


    }




    private static void drawResizableImage(GuiGraphics graphics,Identifier texture, double percentFromLeft,double percentFromTop, double percentWidth, double percentHeight){
        graphics.blit(
                texture,
                (int)(graphics.guiWidth()*percentFromLeft),
                (int)((graphics.guiHeight()*percentFromTop)),
                0,0,
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth)+1,
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight)+1,
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth)+1,
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight)+1
        );
    }

    private static void drawResizableRectangle(GuiGraphics graphics,double percentFromLeft,double percentFromTop,double percentWidth,double percentHeight, int color){
        graphics.fill(
                (int)(graphics.guiWidth()*percentFromLeft),
                (int)((graphics.guiHeight()*percentFromTop)),
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth)+1,
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight)+1,
                color);

    }

    private static void drawResizableBorder(GuiGraphics graphics,double percentFromLeft,double percentFromTop, double percentWidth,double percentHeight, int borderSize, int color){
        //|
        graphics.fill(
                (int)(graphics.guiWidth()*percentFromLeft-borderSize/2F),
                (int)(graphics.guiHeight()*percentFromTop-borderSize/2F),
                (int)(graphics.guiWidth()*percentFromLeft+borderSize/2F)+1,
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight+borderSize/2F)+1,
                color);
        // |
        graphics.fill(
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth-borderSize/2F),
                (int)(graphics.guiHeight()*percentFromTop-borderSize/2F),
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth+borderSize/2F)+1,
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight+borderSize/2F)+1,
                color);
        //-
        graphics.fill(
                (int)(graphics.guiWidth()*percentFromLeft-borderSize/2F),
                (int)(graphics.guiHeight()*percentFromTop-borderSize/2F),
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth+borderSize/2F)+1,
                (int)(graphics.guiHeight()*percentFromTop+borderSize/2F)+1,
                color);
        //_
        graphics.fill(
                (int)(graphics.guiWidth()*percentFromLeft-borderSize/2F),
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight-borderSize/2F),
                (int)(graphics.guiWidth()*percentFromLeft+graphics.guiWidth()*percentWidth+borderSize/2F)+1,
                (int)(graphics.guiHeight()*percentFromTop+graphics.guiHeight()*percentHeight+borderSize/2F)+1,
                color);
    }







    public static final Comparator<Combo> alphabeticOrder = Comparator.comparing((combo -> combo.name));
    public static final Comparator<Combo> highestTickTime = Comparator.comparing(Combo::getCurrentTimeSinceLastPressed).reversed();

    private int getCurrentTimeSinceLastPressed() {
        return this.comboKeys.get(current).timeSinceLastPressed;
    }

    //the comparator mode from the settings
    static Comparator<Combo> compMode = highestTickTime;
}
