package com.qstorm.effects;

import com.qstorm.PracticeMod;
import com.qstorm.effects.custom.SoulEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

import java.awt.*;

public class CustomEffects {

    public static Holder<MobEffect> SOUL_EFFECT = register("soul_effect",new SoulEffect(MobEffectCategory.NEUTRAL, 800080));

    private static Holder<MobEffect> register(String name, MobEffect mobEffect){
        //registers a mobEffect into a holder
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,name),mobEffect);
    }

    public static void onInitializeHolder(){

    }
}
