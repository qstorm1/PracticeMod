package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.powers.Ability;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.UUID;

public class LimitlessInnate extends Ability {


    public int innateDistance = 5;

    public LimitlessInnate(UUID playerUUID, int id) {
        super(playerUUID,id);
    }

    @Override
    public void run(ServerPlayer player) {

    }

    /**
     * There are a number of ways I can handle this innate techinique
     * 1) possibly the most accurate method, and the one that I'm going to try at right now, where I use a mixin and if the entity has a certin tag
     * then their velocity is
     * if(entity is in limitless.startpoint with an innate distance of X then as entity approches X velocity *= oos-x)
     * if entity is fully within limitless then velocity is set to 0
     * limitless.startpoint = the power value
     * limitless.endpoint = startpoint-1
     *
     */

    public double startDistance=5;
    public double endDistance=4;
    public void updateDistance(int power){
        startDistance=power;
        endDistance=startDistance-3;
    }

    class EntityInfo{
        Vec3 velocity;
        boolean hasGravity;

        public static HashMap<UUID,EntityInfo> prevEntityInfo = new HashMap<>();

        public EntityInfo(Vec3 velocity,UUID playerUUID,boolean hasGravity) {
            this.velocity=velocity;
            this.hasGravity=hasGravity;
            prevEntityInfo.putIfAbsent(playerUUID,this);
        }

    }


    @Override
    public void tick(MinecraftServer context){
        ServerPlayer player = context.getPlayerList().getPlayer(playerUUID);
        player.level().getAllEntities().forEach((entity)->{
            if(player.getUUID()!=entity.getUUID()&&player.distanceTo(entity)<=startDistance){
                if(player.distanceTo(entity)>=endDistance){
                    entity.setDeltaMovement(entity.getDeltaMovement().scale( (((player.distanceTo(entity)-endDistance)*(player.distanceTo(entity)-endDistance))/((startDistance-endDistance)*(startDistance-endDistance)))));
                }
                else{
                    entity.setDeltaMovement(0,0,0);
                }
            }
        });


    }



}
