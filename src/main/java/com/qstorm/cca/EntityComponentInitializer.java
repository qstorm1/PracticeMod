package com.qstorm.cca;

import com.qstorm.PracticeMod;
import com.qstorm.powers.PlayerInfo;
import com.qstorm.powers.combo.Combo;
import net.minecraft.resources.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
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
        registry.registerForPlayers(CCAComboStorageClass.playerCombos,player ->
                new CCAComboStorageClass(),RespawnCopyStrategy.ALWAYS_COPY);
    }
}
