package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class Red extends Ability {
    public Red(UUID playerUUID, int id) {
        super(playerUUID,id,0xFFdc143c);
    }

    @Override
    public void run(Player player) {
        PracticeMod.LOGGER.info("ran Red");
        LaserEyes.shootLaser(player);
    }
}
