package com.qstorm.PAL;

import com.qstorm.PracticeMod;
import com.qstorm.packets.AnimationPacket;
import com.qstorm.powers.Ability;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import com.qstorm.powers.cursedtechnique.limitless.power.LimitlessDomain;
import com.qstorm.powers.cursedtechnique.limitless.power.Purple;
import com.qstorm.powers.cursedtechnique.limitless.power.Red;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.animation.PlayerRawAnimationBuilder;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.Unit;

public class PALHandle{

    public static final Identifier ANIMATION_FACTORY_ID = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"factory-one-jjk");
    public static final Identifier DOMAIN_ANIMATION_IDENTIFIER = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"domain-one-jjk");
    public static RawAnimation DOMAIN_ONE;
    public static final Identifier BLUE_ANIMATION_IDENTIFIER = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"blue");
    public static RawAnimation BLUE_SUMMON;
    public static final Identifier RED_ANIMATION_IDENTIFIER = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"red");
    public static RawAnimation RED_SUMMON;
    public static final Identifier PURPLE_ANIMATION_IDENTIFIER = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"purple");
    public static RawAnimation PURPLE_SUMMON;


    public static void init(){

        //when reloading resource pack run this
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
                Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID, "reloader"),
                (sharedState, exectutor, barrier, applyExectutor) ->
                        barrier.wait(Unit.INSTANCE).thenRunAsync(()->{
                            PracticeMod.LOGGER.info("Reloaded Assets for {}",PracticeMod.MOD_ID);

                            initAnimate(ANIMATION_FACTORY_ID,1521);
                            BLUE_SUMMON = PlayerRawAnimationBuilder.begin().then(BLUE_ANIMATION_IDENTIFIER, Animation.LoopType.HOLD_ON_LAST_FRAME).build();

                            RED_SUMMON = PlayerRawAnimationBuilder.begin().then(RED_ANIMATION_IDENTIFIER, Animation.LoopType.HOLD_ON_LAST_FRAME).build();

                            PURPLE_SUMMON = PlayerRawAnimationBuilder.begin().then(PURPLE_ANIMATION_IDENTIFIER, Animation.LoopType.PLAY_ONCE).build();


                            DOMAIN_ONE = PlayerRawAnimationBuilder.begin().then(DOMAIN_ANIMATION_IDENTIFIER, Animation.LoopType.PLAY_ONCE).build();





                            },applyExectutor
                        )
        );



        setClientAnimationDetection();

    }

    public static RawAnimation currentAnimation;

    public static void initAnimate(Identifier factoryID,int priority){
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(factoryID,priority,
                player ->new PlayerAnimationController(player,
                        (animationController, animationState, animationSetter) ->{
                    if(currentAnimation!=null) animationSetter.setAnimation(currentAnimation);
                    return null;
                })

        );
    }


    public static void setClientAnimationDetection(){
        ClientPlayNetworking.registerGlobalReceiver(AnimationPacket.SendAnimationUpdate.TYPE, ((payload, context) -> {
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    context.player(), ANIMATION_FACTORY_ID);
            if(controller!=null) {
                switch (payload.ID()) {
                    case AnimationPacket.SendAnimationUpdate.blueInit -> {
                        controller.triggerAnimation(BLUE_SUMMON);
                    }

                    case AnimationPacket.SendAnimationUpdate.redInit -> {
                        controller.triggerAnimation(RED_SUMMON);
                    }

                    case AnimationPacket.SendAnimationUpdate.purpleInit -> {
                        controller.triggerAnimation(PURPLE_SUMMON);
                    }
                    case AnimationPacket.SendAnimationUpdate.domainInit -> {
                        controller.triggerAnimation(DOMAIN_ONE);
                    }
                    case AnimationPacket.SendAnimationUpdate.blueEnd, AnimationPacket.SendAnimationUpdate.domainEnd,
                         AnimationPacket.SendAnimationUpdate.redEnd, AnimationPacket.SendAnimationUpdate.stop -> {
                        controller.stop();
                    }
                }
            }
        }));
    }


}
