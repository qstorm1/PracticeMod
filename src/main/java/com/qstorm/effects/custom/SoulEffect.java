package com.qstorm.effects.custom;

import com.qstorm.PracticeMod;
import com.qstorm.powers.cursedtechnique.Limitless;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class SoulEffect extends MobEffect {
    public SoulEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectAdded(MobEffectInstance effectInstance, LivingEntity entity) {
        entity.addTag(Limitless.tag);
        if(entity instanceof Player player){
            Limitless.initLimitlessPlayer(player);

        }
        super.onEffectAdded(effectInstance, entity);
    }
}
