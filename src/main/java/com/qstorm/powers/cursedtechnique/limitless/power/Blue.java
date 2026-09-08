package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.mob.Mass;
import com.qstorm.packets.Packet;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.UUID;

public class Blue extends Ability {



    double radius;
    final double maxRadius=5;
    double radiusOfEffect;

    int ticksEnabled=80;


    Vec3 position;
    double currentVelocity;

    Vec3 prevEyeVector;

    //debug
    ArrayList<Double> powerList = new ArrayList<>();
    public static ArrayList<Integer> energyList = new ArrayList<>();



    public Blue(UUID playerUUID,int id) {
        super(playerUUID,id,100000,1,0xFF0000ff);
        this.playerInfo=PlayerInfo.playerInfoHashMap.get(playerUUID);
    }




    @Override
    protected void run(ServerPlayer player) {
        //start timer (set to 0 on reset)
        ticksEnabled=100;
        Vec3 positionToSpawn = player.getEyePosition();
        this.radius=power/100000;
        if(radius>maxRadius)
            radius=maxRadius;

        this.radiusOfEffect=radius*4;
        prevEyeVector=positionToSpawn;
        this.position=positionToSpawn.add(player.getLookAngle().scale(radiusOfEffect+1));

        powerList.add(this.power);
        this.isTicked=true;
    }


    @Override
    public void tick(MinecraftServer context){
        ServerPlayer shooter = context.getPlayerList().getPlayer(playerUUID);
        ticksEnabled--;
        if(ticksEnabled<=0){
            end();
            return;
        }
        renderToClient(position,radius,shooter);
        pullEntities(context.getPlayerList().getPlayer(playerUUID));//create a gravitation pull for each entity
        updateSurroundingBlocks();//transform effected blocks into gravitational blocks and apply forces to all gravitational blocks

        updatePosition(shooter);

    }

    public void updatePosition(ServerPlayer player){
        Vec3 positionToSpawn = player.getEyePosition();
        prevEyeVector=positionToSpawn;
        this.position=positionToSpawn.add(player.getLookAngle().scale(radius+3));;
    }

    public void pullEntities(ServerPlayer player){
        player.level().getAllEntities().forEach((entity)->{

            if(entity==player) return;

            double radFromCenter = entity.position().distanceTo(position);
            if(radFromCenter<=radiusOfEffect){
                double acceleration;
                if(radFromCenter<radius){
                    acceleration=1;
                }
                else if(radFromCenter<=(radiusOfEffect-radFromCenter)/3+radius){
                    acceleration=(power/40000)/(radFromCenter*radFromCenter);
                }
                else{
                    acceleration=(power/10000)/(radFromCenter*radFromCenter*radFromCenter);
                }
                Vec3 direction = entity.position().subtract(position).normalize();
//                double entityMass=1;
//                if(Mass.mass.get(entity.getClass())!=null) entityMass=Mass.mass.get(entity.getClass());


                if(acceleration<3)
                    entity.setDeltaMovement(direction.scale(-acceleration));
                else{
                    return;
                }
            }
        });
    }

    public void updateSurroundingBlocks(){

    }

    public void move(Vec3 move){
        this.position.add(move);
    }

    public void setPosition(Vec3 position){
        this.position=position;
    }


    /**
     * ran on death, level change, and on end
     */
    public void end(){

        this.isTicked=false;
    }


    class GravityBlock{
        Vec3 acceleration;
        Vec3 velocity;
        int terminalVelocity;
        Display.BlockDisplay blockDisplay;
        GravityBlock(BlockState block){

        }
        public void applyForce(Vec3 force){

        }

        /**
         * MAKE SURE TO RUN ON BOTH CLIENT AND SERVER FOR EFFICIENCY?
         * lokey idk we'll try to do with just server but if it doesn't work we can do client 2
         * we might need a separate renderer for client
         */
        public void tick(){
            blockDisplay.addDeltaMovement(velocity);
            velocity.add(acceleration);
            if(velocity.x > terminalVelocity){
                velocity.add(terminalVelocity,0,0);
            }
            if(velocity.y > terminalVelocity){
                velocity.add(0,terminalVelocity,0);
            }
            if(velocity.z > terminalVelocity){
                velocity.add(0,0,terminalVelocity);
            }
        }
    }







    //TODO: make work with Ability
    public static void clientInit(){

    }

    //In theory, I could make this one tick behind and have a linear interpolation between position 1 and 2 though I want to get a simple version done first
    public static void renderToClient(Vec3 position1,double radius,ServerPlayer playerShooting){
        ArrayList<Double> position = new ArrayList<>();
        position.add(position1.x);
        position.add(position1.y);
        position.add(position1.z);
        Vec3 test = playerShooting.getEyePosition();
        Vec3 test2 = playerShooting.getLookAngle();
        Vec3 test3 = playerShooting.getViewVector(0);
        Vec3 test4 = playerShooting.getViewVector(1);



        //temporary particle render, replace with actual thing later cause i don't want to deal with rendering pain
        DustParticleOptions blueParticle = new DustParticleOptions(0xFF000080,1F);

        int count= 500;
        playerShooting.level().sendParticles(
                playerShooting,
                blueParticle,
                false,true,
                position.getFirst(), position.get(1), position.getLast(), count,
//                radius/2, radius/2, radius/2, 0.0
                radius/4,radius/4,radius/4,0
        );


//        for(ServerPlayer player: PlayerLookup.world(playerShooting.level())){
//            ServerPlayNetworking.send(player,new Packet.RenderBlueToClient(position,radius));
//        }
    }


    private double findArkLength(Vec3 first,Vec3 second){
        //find the angle between the vectors
        double theta=Math.acos(first.dot(second)/first.length()*second.length());
        //find the ark length
        return(theta*Math.PI/180);
    }



}
