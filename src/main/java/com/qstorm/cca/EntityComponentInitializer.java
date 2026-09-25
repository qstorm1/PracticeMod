package com.qstorm.cca;

import com.qstorm.cca.comboStorage.CCAComboStorageClass;
import com.qstorm.cca.entityForce.EntityForceStorage;
import com.qstorm.cca.sorceryStorage.SorceryInfoStorage;
import com.qstorm.powers.PlayerInfo;
import net.minecraft.world.entity.Entity;
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
        registry.registerForPlayers(CCAComboStorageClass.playerCombos, player ->
                new CCAComboStorageClass(),RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(Entity.class,EntityForceStorage.forceData,entity -> new EntityForceStorage());
    }
}
