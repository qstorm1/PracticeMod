package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.item.armor.LaserEyes;
import com.qstorm.packets.AnimationPacket;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.UUID;

public class Purple extends Ability {

    Vec3 position;
    Vec3 direction;
    double radius;
    static final double maxRadius=10;
    double radiusOfEffect;
    double distanceFromEye=0;//TODO: add max distance

    public static final Identifier PURPLE_ANIMATION_TAG = Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"purple-animation-tag-jjk");


    public Purple(UUID playerUUID, int id) {
        super(playerUUID,id,0xFF9400d3);
        this.rate=1;
    }

    @Override
    public void runServerClient(Player player) {
        super.runServerClient(player);
        ticksEnabled=(int)Math.min(power/5000,150);
        maxTicksEnabled=ticksEnabled;
        Vec3 positionToSpawn = player.getEyePosition();
        this.radius=power/100000;
        if(radius>maxRadius)
            radius=maxRadius;


        this.radiusOfEffect=radius*3;
        distanceFromEye=radius;
        this.position=positionToSpawn.add(player.getLookAngle().scale(distanceFromEye));
        this.direction=player.getViewVector(0);

        startTickLoop();
    }

    @Override
    public void runServer(ServerPlayer player){
        super.runServer(player);

        ServerPlayNetworking.send(player,new AnimationPacket.SendAnimationUpdate(AnimationPacket.SendAnimationUpdate.domainInit));
    }



    @Override
    public void tick(Player player){
        if(player instanceof ServerPlayer serverPlayer){
            renderToClient(position,radius,serverPlayer);

            //updateSurroundingBlocks(shooter);//transform effected blocks into gravitational blocks and apply forces to all gravitational blocks
            updatePurplePosition(player);
            updateSurroundings(player);
        }



        ticksEnabled--;
        if(ticksEnabled<=0){
            if(player instanceof ServerPlayer)
                end();
        }


    }

    public void updateSurroundings(Player player){

        Vec3 val;
        for(int x = -8; x < 8;x++){
            for(int y = -8; y < 8; y++){
                for(int z = -8; z < 8; z++){
                    val = position.add(x, y, z);
                    //if within 8 blocks
                    if(position.distanceTo(val)<radius-3) {

                        player.level().setBlockAndUpdate(new BlockPos((int) val.x, (int) val.y, (int) val.z), Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }

    }

    public void updatePurplePosition(Player player){
        position = position.add(direction.scale(1/2.0));
    }

    public static void renderToClient(Vec3 position1,double radius,ServerPlayer playerShooting){
        ArrayList<Double> position = new ArrayList<>();
        position.add(position1.x);
        position.add(position1.y);
        position.add(position1.z);


        //temporary particle render, replace with actual thing later cause i don't want to deal with rendering pain
        DustParticleOptions purpleParticle = new DustParticleOptions(0xFF800080,1F);

        int count= 500;
        for(ServerPlayer p:playerShooting.level().players())
            playerShooting.level().sendParticles(
                    p,
                    purpleParticle,
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
