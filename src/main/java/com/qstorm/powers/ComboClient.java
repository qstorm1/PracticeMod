package com.qstorm.powers;

import com.qstorm.PracticeMod;
import com.qstorm.packets.Packet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Comparator;

public class ComboClient {
    static Identifier stuffInBoxIdentifier=Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"combo_hud_box");;
    static Identifier comboListIdentifier=Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"combo_list");;
    static Identifier GUIBox=Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"gui_box");;





    //the comparator mode from the settings


    public static final Identifier TEXTURECOMBO1 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/1.png");
    public static final Identifier TEXTURECOMBO2 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/2.png");
    public static final Identifier TEXTURECOMBO3 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/3.png");
    public static final Identifier TEXTURECOMBO4 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/4.png");


    public static final Identifier BLACKTEXTURECOMBO1 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/1.png");
    public static final Identifier BLACKTEXTURECOMBO2 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/2.png");
    public static final Identifier BLACKTEXTURECOMBO3 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/3.png");
    public static final Identifier BLACKTEXTURECOMBO4 = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"textures/gui/combokeys/4.png");

    public static final Identifier TESTIDENTIFIER = Identifier.fromNamespaceAndPath("minecraft", "textures/blocks/deepslate.png");

    public static ArrayList<String> comboNames= new ArrayList<>();
    public static ArrayList<Integer> idOfKeysToRender = new ArrayList<>();

    static boolean isRendered = false;
    //run on client whenever
    public static void addComboRendererToClient(){
        drawComboList();
        renderComboBox();
        drawKeybinds();
        ClientPlayNetworking.registerGlobalReceiver(Packet.ComboRenderInfoS2C.TYPE,
                (payload, context) -> {
                    context.client().execute(()->{
                        ArrayList<String> comboNames = payload.comboNames();//a list of combo's
                        ArrayList<Integer> idOfKeysToRender = payload.IDs();//a list of id's from the combo chosen

                        //if the list is empty that means we need to reset (I could do with packets but im lazy af)
                        if(idOfKeysToRender.isEmpty()){
                            isRendered=false;
                            return;
                        }

                        //tels renderer to render the following
                        isRendered=true;

                        ComboClient.comboNames=comboNames;
                        ComboClient.idOfKeysToRender =idOfKeysToRender;


                    });
                });
    }



    private static void drawComboList(){
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,comboListIdentifier,(graphics, timeDelta)->{
            if(!isRendered){
                return;
            }

            for(int i = 0 ;i<comboNames.size()&&i<5;i++){
                graphics.drawString(Minecraft.getInstance().font, comboNames.get(i), graphics.guiWidth()/80, (int)(graphics.guiHeight()*6.0/9+i*graphics.guiHeight()/20.0), 0xFF000000);

            }
        });
    }


    private static void drawKeybinds(){

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,stuffInBoxIdentifier,(graphics,timeDelta)->{
            if(!isRendered){
                return;
            }
            //drawWorkingKeybind(graphics);
            if(idOfKeysToRender.size()>4){
                //scroll based
                drawKeybind(graphics, idOfKeysToRender.get(1),1,false);
                drawKeybind(graphics, idOfKeysToRender.get(2),2,false);
                drawKeybind(graphics, idOfKeysToRender.get(3),3,true);

            }
            else {
                //non-scroll based
                for(int i = 1; i< idOfKeysToRender.size(); i++){
                    drawKeybind(graphics, idOfKeysToRender.get(i),i,false);
                }

            }
        });
    }



    /**
     * draws a black keybind at some part of the box
     * @param id the type of black box keybind
     * @param stage values 1,2, or 3 which represent the height of the
     * @param isTransparent is the value transparent (for top of scroll pane)
     */
    private static void drawKeybind(GuiGraphics graphics, int id, int stage, boolean isTransparent){
        final int black;
        if(isTransparent) {
            black = 0x80000000;
        }else{
            black=0xFF000000;
        }
        drawOne(graphics,1);
        drawTwo(graphics,2);
        drawThree(graphics,3);
        drawFour(graphics,4);
        switch (id){
            //case 1 -> drawImage(graphics,TEXTURECOMBO1,150,15+10*stage,50,170);
            //case 2 -> drawImage(graphics,TEXTURECOMBO2,150,15+10*stage,100,100);
            //case 3 -> drawOne(graphics,1);
//            case 4 -> drawImage(graphics,TEXTURECOMBO4,15,15+10*stage,100,100);
//            case 1 -> drawResizableImage(graphics,TEXTURECOMBO1,0.03,0.205+stage*0.19,0.04,0.08);
//
//            case 2 -> drawResizableImage(graphics,TEXTURECOMBO2,0.03,0.205+stage*0.19,0.04,0.08);
//
//            case 3 -> drawResizableImage(graphics,TEXTURECOMBO3,0.03,0.205+stage*0.19,0.04,0.08);
//
//            case 4 -> drawResizableImage(graphics,TEXTURECOMBO4,0.03,0.205+stage*0.19,0.04,0.08);

        }

        //drawResizableRectangle(graphics,0.015,0.205+stage*0.19,0.04,0.08,black);
        //drawResizableBorder(graphics,0.015,0.205+stage*0.19,0.04,0.08,1,black);

    }

    private static void drawWorkingKeybind(GuiGraphics graphics){
        if (!isRendered) {
            return;
        }
        switch (idOfKeysToRender.getFirst()) {
            case 1 -> drawResizableImage(graphics, TEXTURECOMBO1, 0.015, 0.205, 0.04, 0.08);

            case 2 -> drawResizableImage(graphics, TEXTURECOMBO2, 0.015, 0.205, 0.04, 0.08);

            case 3 -> drawResizableImage(graphics, TEXTURECOMBO3, 0.015, 0.205, 0.04, 0.08);

            case 4 -> drawResizableImage(graphics, TEXTURECOMBO4, 0.015, 0.205, 0.04, 0.08);
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

    private static void drawOne(GuiGraphics graphics, int stage){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,0xAA808080);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,0xFF000000);
        drawResizableLine(graphics,0.03+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,0xFF000000);
        drawResizableLine(graphics,0.031+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,0xFF000000);
        drawResizableLine(graphics,0.029+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,0xFF000000);

    }
    private static void drawTwo(GuiGraphics graphics, int stage){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,0xAA808080);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,0xFF000000);
        //-
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.41-(0.1*stage),false,0xFF000000);


        //  |
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.21+0.435-(0.1*stage),0.21+0.41-(0.1*stage),true,0xFF000000);

        //-
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.435-(0.1*stage),false,0xFF000000);

        //|
        drawResizableLine(graphics,0.017+(0.05)/2,
                0.21+0.465-(0.1*stage),0.21+0.435-(0.1*stage),true,0xFF000000);
        //_
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.46-(0.1*stage),false,0xFF000000);

    }
    private static void drawThree(GuiGraphics graphics, int stage){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,0xAA808080);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,0xFF000000);

        drawResizableLine(graphics,0.038+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,0xFF000000);

        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.41-(0.1*stage),false,0xFF000000);

        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.435-(0.1*stage),false,0xFF000000);
        //_
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.46-(0.1*stage),false,0xFF000000);

    }
    private static void drawFour(GuiGraphics graphics, int stage){
        drawResizableRectangle(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,0xAA808080);
        drawResizableBorder(graphics,0.03,0.21+0.4-(0.1*stage),0.05,0.07,1,0xFF000000);
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.21+0.46-(0.1*stage),0.21+0.41-(0.1*stage),true,0xFF000000);
        drawResizableLine(graphics,0.038+(0.05)/2,
                0.055+(0.05)/2,0.21+0.435-(0.1*stage),false,0xFF000000);
        drawResizableLine(graphics,0.017+(0.05)/2,
                0.21+0.435-(0.1*stage),0.21+0.41-(0.1*stage),true,0xFF000000);
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
