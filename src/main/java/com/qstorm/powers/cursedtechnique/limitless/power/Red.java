package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.Combo;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class Red extends Ability {
    public Red(UUID playerUUID, int id) {
        super(playerUUID,id,0xFFdc143c);
        this.playerInfo= PlayerInfo.playerInfoHashMap.get(playerUUID);
    }

    @Override
    public void run(ServerPlayer player) {
        PracticeMod.LOGGER.info("ran Red");
        LaserEyes.shootLaser(player);
    }
}
