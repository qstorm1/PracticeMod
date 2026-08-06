package com.qstorm.potion;

import com.qstorm.PracticeMod;
import com.qstorm.effects.CustomEffects;
import com.qstorm.effects.custom.SoulEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;

public class ModPotions {

    public static Holder<Potion> SOUL_POTION =register("soul_potion",new Potion("soul_potion",
            new MobEffectInstance(CustomEffects.SOUL_EFFECT,9600),new MobEffectInstance(MobEffects.ABSORPTION,9600,10)
    ));

    public static Holder<Potion> register(String name, Potion potion ){
        return Registry.registerForHolder(BuiltInRegistries.POTION, Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,name),potion);
    }
    public static void onInitialize(){
    }
}
