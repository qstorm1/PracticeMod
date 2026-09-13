package com.qstorm;

import com.qstorm.key.InitializeBindings;
import com.qstorm.packets.Packet;
import com.qstorm.powers.ComboClient;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

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



        ClientPlayNetworking.registerGlobalReceiver(Packet.SendClientMessage.TYPE,(packet,context)->{
            context.player().displayClientMessage(Component.literal(packet.message()),false);
        });
        ClientPlayNetworking.registerGlobalReceiver(Packet.LimitlessInit.TYPE,(packet,context)->{
            context.client().execute(()->{
                Limitless.initLimitlessPlayer(context.player().level(), context.player(),Minecraft.getInstance().isSingleplayer());
            });
        });


        //handles the little thing in the bottom left of the screen
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
    }

}
