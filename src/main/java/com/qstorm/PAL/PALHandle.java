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
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class PALHandle{

    public static final Identifier ANIMATION_NAME_ID = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"domain-one-jjk");
    public static final Identifier ANIMATION_FACTORY_ID = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"factory-one-jjk");
    public static RawAnimation DOMAIN_ONE;


    public static void init(){

        //when reloading resource pack run this
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
                Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "reloader"),
                (sharedState, exectutor, barrier, applyExectutor) ->
                        barrier.wait(Unit.INSTANCE).thenRunAsync(()->{
                            PracticeMod.LOGGER.info("Reloaded Assets for {}",PracticeMod.MOD_ID);


                            DOMAIN_ONE = PlayerRawAnimationBuilder.begin().then(ANIMATION_NAME_ID, Animation.LoopType.PLAY_ONCE).build();
                            Ability.initAnimate(Blue.BLUE_ANIMATION_TAG,DOMAIN_ONE,1510);
                                },applyExectutor)
        );



        setClientAnimationDetection();

    }



    public static void setClientAnimationDetection(){
        ClientPlayNetworking.registerGlobalReceiver(AnimationPacket.SendAnimationUpdate.TYPE, ((payload, context) -> {
            switch(payload.ID()){
                case AnimationPacket.SendAnimationUpdate.blueInit -> {
                    PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                            context.player(), Blue.BLUE_ANIMATION_TAG);
                    if(controller!=null) controller.triggerAnimation(DOMAIN_ONE);
                }
            }
        }));
    }


}
