package com.qstorm;

import com.qstorm.PAL.PALHandle;
import com.qstorm.cca.sorceryStorage.SorceryInfoStorage;
import com.qstorm.key.InitializeBindings;
import com.qstorm.packets.Packet;
import com.qstorm.powers.combo.ComboClient;
import com.qstorm.powers.cursedtechnique.JJKClientRender;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

public class PracticeModClient implements ClientModInitializer {

    public static boolean startedRender = false;
    public static Integer cursedEnergy=0;
    public static Double cursedOutput=0.0;

    public static boolean startedRenderInnate = false;
    public static boolean isInnate = false;

    @Override
    public void onInitializeClient() {
        //initialize all keyboard stuff
        InitializeBindings.init();
        JJKClientRender.initJJKClientRender();
        PALHandle.init();






        //when we recieve a thing saying to make this player a sorceror, render client
        ClientPlayNetworking.registerGlobalReceiver(Packet.LimitlessInit.TYPE,(packet, context)->{
            Limitless.initLimitlessPlayer(context.player());
        });

        //client message sending
        ClientPlayNetworking.registerGlobalReceiver(Packet.SendClientMessage.TYPE,(packet,context)->{
            context.player().displayClientMessage(Component.literal(packet.message()),false);
        });


        //the Mixin LLInnateMovementHandle needs to have info from the server only Limitless.findClosestPlayer method on the client side,
        //it is sent from the server and put into the value closestLimitlessPosition
        ClientPlayNetworking.registerGlobalReceiver(Packet.SetClosestPlayer.TYPE,(packet,context)->{
            Sorcery value = SorceryInfoStorage.sorceryData.get(context.player()).getSorcerer();
            if(value instanceof Limitless limitless)
                limitless.closestLimitlessPosition=new Vec3(packet.x(),packet.y(),packet.z());

        });

        //draw the thing in the bottom left
        ClientPlayNetworking.registerGlobalReceiver(Packet.ActivateSorceryRender.TYPE,(packet, context)->{
            context.client().execute(()->{
                cursedEnergy=packet.energy();
                cursedOutput=packet.output();
                if(!startedRender){

                    HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"started-render-eehectevct-im-too-lazy-to-name-bruh"),
                            (graphics,tracker)->{
                        //TODO: when output is max yell MAX in chate
                                int percentColor=0xFA8d8d8d;
                                graphics.drawString(Minecraft.getInstance().font, "Energy: " + cursedEnergy,(int)(graphics.guiWidth()*0.005),(int)(graphics.guiHeight()*0.88),0xFA8d8d8d);
                                if((int)(cursedOutput*100)==100) percentColor = 0xFFee4a4a;
                                graphics.drawString(Minecraft.getInstance().font, "Output: " + (int)(cursedOutput*100) + "%",(int)(graphics.guiWidth()*0.005),(int)(graphics.guiHeight()*0.93),percentColor);
                            });
                    startedRender=true;
                }
            });
        });

        //draw the thing that says if innate or not
        ClientPlayNetworking.registerGlobalReceiver(Packet.RenderIfInnate.TYPE,(packet,context)->{
            isInnate=packet.isInnate();
        });
        if(!startedRenderInnate){
            HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT,Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"innate-name-ya"),
                    (graphics,tracker)->{
                if(isInnate) {


                    ComboClient.drawResizableRectangle(graphics, 0.9, 0.94, 0.09, 0.05, 0xFF808080);
                    //ComboClient.drawResizableBorder(graphics, 0.9, 0.94, 0.09, 0.05, 1, 0xFF000000);
                }
            });
            startedRenderInnate=true;
        }

    }

}
