package com.qstorm.powers;

import com.qstorm.PracticeMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import static com.qstorm.powers.Combo.findLongestCombo;

public class ComboClient {
    static Identifier stuffInBoxIdentifier=Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"combo_hud_box");;
    static Identifier comboListIdentifier=Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"combo_list");;
    static Identifier GUIBox=Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"gui_box");;





    //the comparator mode from the settings
    public static final Comparator<Combo> alphabeticOrder = Comparator.comparing((combo -> combo.name));
    public static final Comparator<Combo> highestTickTime = Comparator.comparing(Combo::getCurrentTimeSinceLastPressed).reversed();
    public static Comparator<Combo> compMode = highestTickTime;

    public static final Identifier TEXTURECOMBO1 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/1.png");
    public static final Identifier TEXTURECOMBO2 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/2.png");
    public static final Identifier TEXTURECOMBO3 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/3.png");
    public static final Identifier TEXTURECOMBO4 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/4.png");


    public static final Identifier BLACKTEXTURECOMBO1 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/1.png");
    public static final Identifier BLACKTEXTURECOMBO2 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/2.png");
    public static final Identifier BLACKTEXTURECOMBO3 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/3.png");
    public static final Identifier BLACKTEXTURECOMBO4 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/4.png");


    public static ArrayList<String> comboNames= new ArrayList<>();
    public static ArrayList<Integer> idOfKeysToRender = new ArrayList<>();
    public static ArrayList<Integer> colors = new ArrayList<>();

    static boolean isRendered = false;


    //run on client whenever a new sorceror created
    public static void addARendererToClient(){
        drawComboList();
        renderComboBox();
        drawKeybinds();
    }





    public static void getComboBoxRenderValues(UUID playerUUID, int keyPressed, boolean isReset){
        if(FabricLoader.getInstance().getEnvironmentType()== EnvType.SERVER) return;


        ArrayList<Combo> combosPlayerHas = Combo.playerCombos.get(playerUUID);
        ArrayList<Combo> combosToRender= new ArrayList<>();

        //data to send to client
        ArrayList<String> listOfStrings = new ArrayList<>();
        ArrayList<Integer> listOfIntegers = new ArrayList<>();
        ArrayList<Integer> listOfColors = new ArrayList<>();


        //find the current biggest combo, combos with the highest current will be rendered
        //if this is a reset call, the current of the reset will be 0 therefore not being considered as the main combo
        int currentMax=findLongestCombo(playerUUID);


        //if we reset and there are no other combos working
        if(isReset&&currentMax==0){
            updateHudData(listOfStrings,listOfIntegers,listOfColors);
            return;
        }


        //make the updated list have all the combos with the highest comboKey
        if(currentMax!=0) {
            for (Combo combo : combosPlayerHas)
                if (combo.current == currentMax && combo.comboKeys.get(currentMax - 1).id == keyPressed)
                    combosToRender.add(combo);
        }

        //if there are no combos that are active, send empty data which the client will recognize as a reset call
        if(combosToRender.isEmpty()){
            updateHudData(listOfStrings,listOfIntegers,listOfColors);
            return;
        }


        //sort combo list by whatever sorting method (ex alphabetical -> A is the one at the top of the render)
        combosToRender.sort(compMode);


        //update values correctly
        for(Combo combo:combosToRender){
            listOfStrings.add(combo.name);
            listOfColors.add(combo.action.textColor);
        }
        //all the combo keys
        for(int i = combosToRender.getFirst().current; i<combosToRender.getFirst().comboKeys.size();i++){
            listOfIntegers.add(combosToRender.getFirst().comboKeys.get(i).id);
        }


        //send rendering data to server
        updateHudData(listOfStrings,listOfIntegers,listOfColors);
    }



    private static void updateHudData(ArrayList<String> listOfStrings, ArrayList<Integer> listOfIntegers, ArrayList<Integer> listOfColors){
        if(listOfIntegers.isEmpty()){
            clearScreen();
            return;
        }

        //tels renderer to render the following
        isRendered=true;

        ComboClient.comboNames=listOfStrings;
        ComboClient.idOfKeysToRender =listOfIntegers;
        ComboClient.colors=listOfColors;

    }








    public static void clearScreen(){
        isRendered=false;
    }



    private static void drawComboList(){
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,comboListIdentifier,(graphics, timeDelta)->{
            if(!isRendered){
                return;
            }

            for(int i = 0 ;i<comboNames.size()&&i<5;i++){
                graphics.drawString(Minecraft.getInstance().font, comboNames.get(i), graphics.guiWidth()/80, (int)(graphics.guiHeight()*6.0/9+i*graphics.guiHeight()/20.0), colors.get(i));

            }
        });
    }


    private static void drawKeybinds(){

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,stuffInBoxIdentifier,(graphics,timeDelta)->{
            if(!isRendered){
                return;
            }

            if(idOfKeysToRender.size()>4){
                //scroll based
                drawKeybind(graphics, idOfKeysToRender.get(0),1,false);
                drawKeybind(graphics, idOfKeysToRender.get(1),2,false);
                drawKeybind(graphics, idOfKeysToRender.get(2),3,false);
                drawKeybind(graphics, idOfKeysToRender.get(3),4,true);

            }
            else {
                //non-scroll based
                for(int i = 0; i< idOfKeysToRender.size(); i++){
                    drawKeybind(graphics, idOfKeysToRender.get(i),i+1,false);
                }

            }
            drawWorkingKeybind(graphics,idOfKeysToRender.get(0));
        });
    }



    /**
     * draws a black keybind at some part of the box
     * @param id the type of black box keybind
     * @param stage values 1,2, or 3 which represent the height of the
     * @param isTransparent is the value transparent (for top of scroll pane)
     */
    private static void drawKeybind(GuiGraphics graphics, int id, int stage, boolean isTransparent){
        final int inner;
        final int border;
        if(isTransparent) {
            inner = 0x80808080;
        }else{
            inner=0xAA808080;
        }
        border=0xFF000000;
        switch (id){
            case 1 -> drawOne(graphics,stage,inner,border);
            case 2 -> drawTwo(graphics,stage,inner,border);
            case 3 -> drawThree(graphics,stage,inner,border);
            case 4 -> drawFour(graphics,stage,inner,border);

        }

    }

    private static void drawWorkingKeybind(GuiGraphics graphics,int id){
        if (!isRendered) {
            return;
        }
        switch (id) {
            case 1 -> drawOne(graphics,1,0xFFFFFFFF,0xFF000000);
            case 2 -> drawTwo(graphics,1,0xFFFFFFFF,0xFF000000);
            case 3 -> drawThree(graphics,1,0xFFFFFFFF,0xFF000000);
            case 4 -> drawFour(graphics,1,0xFFFFFFFF,0xFF000000);
        }

        //TODO: make a xp bar underneath key representing length of time needed to press

    }

    private static void renderComboBox(){
        final int transparent_middle = 0x80A8A8A8;
        final int border_color = 0x90000000;

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,GUIBox,(graphics,timeDelta)->{
            if(!isRendered){
                return;
            }
            drawResizableRectangle(graphics,0.01,0.2,0.09,0.4,transparent_middle);
            drawResizableBorder(graphics,0.01,0.2,0.09,0.4,1,border_color);
        });


    }




    private static void drawResizableImage(GuiGraphics graphics,Identifier texture, double percentFromLeft,double percentFromTop, double percentWidth, double percentHeight){
        //texture
        // x and y
        // image offset
        // x2 y2 (or width/height idk
        // width and height of image
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

    private static void drawOne(GuiGraphics graphics, int stage,int colorInner,int colorBorder){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,colorInner);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,colorBorder);
        drawResizableLine(graphics,0.03+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,colorBorder);
        drawResizableLine(graphics,0.031+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,colorBorder);
        drawResizableLine(graphics,0.029+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,colorBorder);

    }
    private static void drawTwo(GuiGraphics graphics, int stage,int colorInner,int colorBorder){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,colorInner);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,colorBorder);
        //-
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.41-(0.1*stage),false,colorBorder);


        //  |
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.21+0.435-(0.1*stage),0.21+0.41-(0.1*stage),true,colorBorder);

        //-
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.435-(0.1*stage),false,colorBorder);

        //|
        drawResizableLine(graphics,0.017+(0.05)/2,
                0.21+0.465-(0.1*stage),0.21+0.435-(0.1*stage),true,colorBorder);
        //_
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.46-(0.1*stage),false,colorBorder);

    }
    private static void drawThree(GuiGraphics graphics, int stage,int colorInner, int colorBorder){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,colorInner);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,colorBorder);

        drawResizableLine(graphics,0.038+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,colorBorder);

        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.41-(0.1*stage),false,colorBorder);

        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.435-(0.1*stage),false,colorBorder);
        //_
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.46-(0.1*stage),false,colorBorder);

    }
    private static void drawFour(GuiGraphics graphics, int stage,int innerColor,int colorBorder){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,innerColor);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,colorBorder);
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,colorBorder);
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.435-(0.1*stage),false,colorBorder);
        drawResizableLine(graphics,0.017+(0.05)/2,
                0.21+0.435-(0.1*stage),0.21+0.41-(0.1*stage),true,colorBorder);
    }

    private static void drawImage(GuiGraphics graphics,Identifier texture, int left,int top, int width, int height){
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, left, top, 0, 0, width, height, 1, 1);
//        graphics.blit(
//                RenderPipelines.GUI_TEXTURED,
//                TEXTURECOMBO1,
//                (int)(graphics.guiWidth()*0.01),//x1 y1
//                (int)(graphics.guiHeight()*0.2),
//                (int)(graphics.guiWidth()*0.1),
//                (int)(graphics.guiHeight()*0.5),//x2 y2
//                16,//image x1 y1
//                16,
//                (int)(graphics.guiWidth()*0.1),
//                (int)(graphics.guiHeight()*0.5)//image x2 y2
//        );
    }

    //TODO: make verticle line
    private static void drawResizableLine(GuiGraphics graphics, double percent1,double percent2,double percent3,boolean isVerticle,int color){

        if(isVerticle){
            graphics.vLine((int)(graphics.guiWidth()*percent1),
                    (int)(graphics.guiHeight()*percent2),(int)( graphics.guiHeight()*percent3),
                    color);
        }
        else{
            graphics.hLine((int)(graphics.guiWidth()*percent1),
                    (int)(graphics.guiHeight()*percent2), (int)(graphics.guiHeight()*percent3),
                    color);
        }
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
}
