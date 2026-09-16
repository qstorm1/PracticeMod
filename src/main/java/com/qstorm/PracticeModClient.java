package com.qstorm;

import com.qstorm.PAL.PALHandle;
import com.qstorm.key.InitializeBindings;
import com.qstorm.packets.Packet;
import com.qstorm.powers.ComboClient;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
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
        PayloadTypeRegistry.playS2C().register(Packet.LimitlessInit.TYPE, Packet.LimitlessInit.CODEC);
        initJJKClientRender();


//        ClientTickEvents.END_CLIENT_TICK.register((client)->{
//            PALHandle.testResource();
//        });
//
//        PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
//                player, ANIMATION_LAYER_ID);
//        controller.triggerAnimation(animationID);
//
//        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(PALHandle.ANIMATION_NAME_ID, 1000,
//                player -> new PlayerAnimationController(player,
//                        (controller, state, animSetter) -> {
//                            if (state.isMoving()) return animSetter.setAnimation(PALHandle.DOMAIN_ONE);
//
//                            return PlayState.STOP;
//                )
//        );
        //when we recieve a thing saying to make this player a sorceror, render client
        ClientPlayNetworking.registerGlobalReceiver(Packet.LimitlessInit.TYPE,(packet, context)->{
            Limitless.initLimitlessPlayer(context.player());
        });

        ClientPlayNetworking.registerGlobalReceiver(Packet.SendClientMessage.TYPE,(packet,context)->{
            context.player().displayClientMessage(Component.literal(packet.message()),false);
        });

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
