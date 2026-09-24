package com.qstorm;

import com.qstorm.PAL.PALHandle;
import com.qstorm.cca.SorceryInfoStorage;
import com.qstorm.key.InitializeBindings;
import com.qstorm.packets.AnimationPacket;
import com.qstorm.packets.Packet;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import static com.qstorm.powers.cursedtechnique.JJKClientRender.initJJKClientRender;

public class PracticeModClient implements ClientModInitializer {

    public static boolean startedRender = false;
    public static Integer cursedEnergy=0;
    public static Double cursedOutput=0.0;


    @Override
    public void onInitializeClient() {
        //initialize all keyboard stuff
        InitializeBindings.init();
        initJJKClientRender();
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
        //HudElementRegistry.attachElementBefore(VanillaHudElements.CROSSHAIR, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"before-thing"),Combo::render);
    }

}
