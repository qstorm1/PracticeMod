package com.qstorm.cca;

import com.qstorm.powers.PlayerInfo;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class EntityComponentInitializer implements org.ladysnake.cca.api.v3.entity.EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PlayerInfo.playerInfoData,player ->
                new PlayerInfo(player,0,0), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(SorceryInfoStorage.sorceryData, player ->
                        new SorceryInfoStorage()
        ,RespawnCopyStrategy.ALWAYS_COPY);
    }
}
