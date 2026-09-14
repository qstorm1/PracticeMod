package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.UUID;

public class LimitlessInnate extends Ability {


    public int innateDistance = 5;
    public static final String TAG = "LLI-EFFECTED-JJK-MOD";


    public LimitlessInnate(UUID playerUUID, int id) {
        super(playerUUID,id);
        this.playerInfo= PlayerInfo.playerInfoHashMap.get(playerUUID);
    }

    @Override
    public void run(ServerPlayer player) {
        player.canBeHitByProjectile();
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

    public double startDistance=4;
    public double endDistance=2;
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



            //Don't need to deal with:
            // Eye of ender
            //Living entities

            if(player.getUUID()!=entity.getUUID()&&player.distanceTo(entity)<=startDistance){
                if(entity instanceof PrimedTnt){
                    //PracticeMod.LOGGER.info("is primed TNT");
                }

                if(entity instanceof Projectile){
                    //PracticeMod.LOGGER.info("is projectile");
                }
                if(entity instanceof LightningBolt){
                    //PracticeMod.LOGGER.info("is Lightning Bolt");
                }
                if(entity instanceof EvokerFangs evokerFangs){
                    //PracticeMod.LOGGER.info("this is a tihng");
                }
                if(entity instanceof ExperienceOrb){

                }
                if(player.distanceTo(entity)>=endDistance){
                    if(entity instanceof LivingEntity)
                        entity.setDeltaMovement(entity.getDeltaMovement().scale((((player.distanceTo(entity)-endDistance)*(player.distanceTo(entity)-endDistance))/((startDistance-endDistance)*(startDistance-endDistance)))));
                }
                else{
                    if(entity instanceof LivingEntity)
                        entity.setDeltaMovement(0,0,0);
                }
                entity.needsSync = true;
            }
        });

    }

    @Override
    public void end(ServerLevel level){
        super.end(level);
        level.getAllEntities().forEach(entity-> {

        });

    }



}
