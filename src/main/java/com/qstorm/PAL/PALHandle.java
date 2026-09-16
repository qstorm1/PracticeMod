package com.qstorm.PAL;

import com.qstorm.PracticeMod;
import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import com.zigythebird.playeranim.animation.PlayerRawAnimationBuilder;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;

public class PALHandle{

    public static final Identifier ANIMATION_NAME_ID = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"domain-one-jjk");
    public static final Identifier ANIMATION_FACTORY_ID = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"factory-one-jjk");
    public static RawAnimation DOMAIN_ONE;


    public static void init(){
        DOMAIN_ONE = PlayerRawAnimationBuilder.begin().thenPlay(ANIMATION_NAME_ID).build();
        //PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(Blue.BLUE_ANIMATION_TAG);

    }

    public static void testResource(){
        ArrayList<String> test = new ArrayList<>();
        for (var resource : Minecraft.getInstance().getResourceManager().listResources("player_animations", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()){
            test.add(resource.getKey().getNamespace());

        }
    }


}
