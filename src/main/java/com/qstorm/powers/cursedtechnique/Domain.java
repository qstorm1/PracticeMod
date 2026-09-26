package com.qstorm.powers.cursedtechnique;

import com.qstorm.cca.domainInfo.DomainComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface Domain {
    void onActivate(MinecraftServer server, ServerPlayer shooter);
}
