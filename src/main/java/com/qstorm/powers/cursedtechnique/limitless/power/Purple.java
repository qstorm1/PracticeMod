package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class Purple extends Ability {


    public Purple(UUID playerUUID, int id) {
        super(playerUUID,id,0xFF9400d3);
    }

    @Override
    public void run(Player player) {
        PracticeMod.LOGGER.info("ran purple");
        LaserEyes.shootLaser(player);
    }
}
