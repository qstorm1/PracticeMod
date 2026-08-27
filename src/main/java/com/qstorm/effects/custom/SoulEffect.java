package com.qstorm.effects.custom;

import com.qstorm.PracticeMod;
import com.qstorm.powers.cursedtechnique.Limitless;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;

public class SoulEffect extends MobEffect {



    public SoulEffect(MobEffectCategory category, int color) {
        super(category, color);
    }



    @Override
    public void onEffectAdded(MobEffectInstance effectInstance, LivingEntity entity) {
        super.onEffectAdded(effectInstance, entity);
        entity.addTag(Limitless.tag);
        if(entity instanceof Player player){
            Limitless.initLimitlessPlayer(player);

        }
    }
}
