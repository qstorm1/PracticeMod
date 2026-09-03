package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.powers.Ability;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class LimitlessInnate extends Ability {


    public LimitlessInnate(UUID playerUUID, int id) {
        super(playerUUID,id);
    }

    @Override
    public void run(ServerPlayer player) {

    }

    @Override
    public void tick(MinecraftServer context){
        ServerPlayer player = context.getPlayerList().getPlayer(playerUUID);
        player.level().getAllEntities().forEach((entity)->{
            if(player.getUUID()!=entity.getUUID()&&player.distanceTo(entity)<=5){
                entity.setDeltaMovement(0,0,0);

            }
        });
    }
}
