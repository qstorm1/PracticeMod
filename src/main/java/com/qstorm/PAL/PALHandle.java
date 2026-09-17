package com.qstorm.PAL;

import com.qstorm.PracticeMod;
import com.qstorm.packets.AnimationPacket;
import com.qstorm.powers.Ability;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.animation.PlayerRawAnimationBuilder;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PALHandle{

    public static final Identifier ANIMATION_NAME_ID = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"domain-one-jjk");
    public static final Identifier ANIMATION_FACTORY_ID = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"factory-one-jjk");
    public static RawAnimation DOMAIN_ONE;


    public static void init(){

        DOMAIN_ONE = PlayerRawAnimationBuilder.begin().thenPlay(ANIMATION_NAME_ID).build();
        Ability.initAnimate(Blue.BLUE_ANIMATION_TAG,DOMAIN_ONE,1510);
        setClientAnimationDetection();

    }



    public static void setClientAnimationDetection(){
        ClientPlayNetworking.registerGlobalReceiver(AnimationPacket.SendAnimationUpdate.TYPE, ((payload, context) -> {
            switch(payload.ID()){
                case AnimationPacket.SendAnimationUpdate.blueInit -> {
                    PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                            context.player(), Blue.BLUE_ANIMATION_TAG);
                }
            }
        }));
    }


}
