package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.cca.entityForce.EntityForceStorage;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.UUID;

public class Red extends Ability {
    public Red(UUID playerUUID, int id) {
        super(playerUUID,id,0xFFdc143c);
    }


    Vec3 position;
    Vec3 direction;
    double radius;
    final double maxRadius=1;
    double radiusOfEffect;
    double distanceFromEye=0;//TODO: add max distance



    @Override
    public void runServerClient(Player player) {
        super.runServerClient(player);
        ticksEnabled=100;
        Vec3 positionToSpawn = player.getEyePosition();
        this.radius=power/200000;
        if(radius>maxRadius)
            radius=maxRadius;


        this.radiusOfEffect=radius*3;
        distanceFromEye=radius+1;
        this.position=positionToSpawn.add(player.getLookAngle().scale(distanceFromEye));
        this.direction=player.getViewVector(0);

        startTickLoop();
    }


    @Override
    public void tick(Player player){
        if(player instanceof ServerPlayer serverPlayer){
            renderToClient(position,radius,serverPlayer);


            //updateSurroundingBlocks(shooter);//transform effected blocks into gravitational blocks and apply forces to all gravitational blocks
            updateRedPosition(player);

        }

        updateSurroundingEntities(player);

        ticksEnabled--;
        if(ticksEnabled<=0){
            if(player instanceof ServerPlayer serverPlayer)
                end(serverPlayer);
        }


    }
    public static final Integer RED_FORCE = 54;
    public static final String RED_EFFECTED_TAG = "ret";

    private void updateSurroundingEntities(Player player) {
        if(player instanceof ServerPlayer serverPlayer){
            serverPlayer.level().getAllEntities().forEach(
                    entity -> {

                        //if the entity is within 8 blocks of red's position
                        if(entity.position().distanceTo(position)<5) {
                            //if the entity is on the side red is moving
                            // red = ->, entity = . ( ->  . )
                            if (direction.dot(entity.position().subtract(position)) > 0) {
                                entity.setNoGravity(true);
                                if(!entity.getTags().contains(RED_EFFECTED_TAG))
                                    entity.addTag(RED_EFFECTED_TAG);
                                EntityForceStorage.forceData.get(entity).setForce(RED_FORCE,direction);
                            }
                            else{
                                //if the entity is on the side moving backwards it will just move opposite
                                // red = ->, entity = . (  . -> )
                                //set force to difference in 1/dPosition max of 5
                                EntityForceStorage.forceData.get(entity).setForce(RED_FORCE,
                                        new Vec3(1,1,1).scale(Math.min(4.0/position.distanceTo(entity.position()),2))
                                );
                            }
                        }
                        else {
                            entity.setNoGravity(false);
                            EntityForceStorage.forceData.get(entity).removeForce(RED_FORCE);
                            entity.removeTag(RED_EFFECTED_TAG);

                        }
                        if(entity.distanceTo(player)<3 &&ticksEnabled>99&&
                                player.getViewVector(0).dot(entity.position().subtract(player.position()))>0)
                            EntityForceStorage.forceData.get(entity).setForce(RED_FORCE,direction.scale(5));

                    }
            );
        }
    }

    public void updateRedPosition(Player player){
        position = position.add(direction);
    }

    public void end(ServerPlayer player){
        end();
        player.level().getAllEntities().forEach(
                entity -> {

                    entity.setNoGravity(false);
                    EntityForceStorage.forceData.get(entity).removeForce(RED_FORCE);
                    entity.removeTag(RED_EFFECTED_TAG);


                }
        );

    }

    //In theory, I could make this one tick behind and have a linear interpolation between position 1 and 2 though I want to get a simple version done first
    public static void renderToClient(Vec3 position1,double radius,ServerPlayer playerShooting){
        ArrayList<Double> position = new ArrayList<>();
        position.add(position1.x);
        position.add(position1.y);
        position.add(position1.z);


        //temporary particle render, replace with actual thing later cause i don't want to deal with rendering pain
        DustParticleOptions redParticle = new DustParticleOptions(0xFFff0000,1F);

        int count= 500;
        playerShooting.level().sendParticles(
                playerShooting,
                redParticle,
                false,true,
                position.getFirst(), position.get(1), position.getLast(), count,
//                radius/2, radius/2, radius/2, 0.0
                radius/4,radius/4,radius/4,0
        );


//        for(ServerPlayer player: PlayerLookup.world(playerShooting.level())){
//            ServerPlayNetworking.send(player,new Packet.RenderBlueToClient(position,radius));
//        }
    }
}
