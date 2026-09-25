package com.qstorm.effects.custom;

import com.qstorm.cca.sorceryStorage.SorceryInfoStorage;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.cursedtechnique.Sorcery;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
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
        super.onEffectAdded(effectInstance, entity);
        entity.addTag(Sorcery.SORCERER_TAG);
        if(entity instanceof Player player){
            //set the player sorcery type to be a sorcerer, this is saved permanently
            SorceryInfoStorage.sorceryData.get(player).setSorceryType(Limitless.SORCERER_ID);
            if(player.getTags().contains("cheat"))
                PlayerInfo.setCheatMode(player);
        }
    }
}
