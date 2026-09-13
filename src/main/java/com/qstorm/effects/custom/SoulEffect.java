package com.qstorm.effects.custom;

import com.qstorm.packets.Packet;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.cursedtechnique.limitless.Limitless;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
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
        entity.addTag(Limitless.tag);
        if(entity instanceof Player player){
            Limitless.initLimitlessPlayer(entity.level(),player);
            if(player.getTags().contains("cheat"))
                PlayerInfo.playerInfoHashMap.get(player.getUUID()).cheatMode();
            if(player instanceof ServerPlayer serverPlayer){
                ServerPlayNetworking.send(serverPlayer,new Packet.LimitlessInit());
            }
        }
    }
}
