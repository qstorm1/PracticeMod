package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
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
        distanceFromEye=radius+3;
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

        ticksEnabled--;
        if(ticksEnabled<=0){
            end();
        }


    }

    public void updateRedPosition(Player player){
        position = position.add(direction);
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
