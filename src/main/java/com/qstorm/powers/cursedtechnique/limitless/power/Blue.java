package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.mob.Mass;
import com.qstorm.packets.Packet;
import com.qstorm.powers.Ability;
import com.qstorm.powers.PlayerInfo;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class Blue extends Ability {



    double radius;
    final double maxRadius=5;
    double radiusOfEffect;
    double distanceFromEye=0;//TODO: add max distance

    int ticksEnabled=100;


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
        ticksEnabled=200;
        Vec3 positionToSpawn = player.getEyePosition();
        this.radius=power/200000;
        if(radius>maxRadius)
            radius=maxRadius;

        this.radiusOfEffect=radius*4;
        prevEyeVector=positionToSpawn;
        distanceFromEye=radius+3;
        this.position=positionToSpawn.add(player.getLookAngle().scale(distanceFromEye));

        powerList.add(this.power);
        this.isTicked=true;
        this.isScroll=true;
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
        updateSurroundingBlocks(context);//transform effected blocks into gravitational blocks and apply forces to all gravitational blocks
        if(ticksEnabled%10==0) {
            context.getPlayerList().getPlayer(playerUUID).level().explode(context.getPlayerList().getPlayer(playerUUID),
                    position.x,position.y,position.z,
                    3,Level.ExplosionInteraction.MOB);
        }
        updatePosition(shooter);

    }


    public void updatePosition(ServerPlayer player){
        Vec3 positionToSpawn = player.getEyePosition();
        this.position=positionToSpawn.add(player.getLookAngle().scale(distanceFromEye));;
        prevEyeVector=positionToSpawn;
    }



    public void pullEntities(ServerPlayer player){
        player.level().getAllEntities().forEach((entity)->{

            if(entity==player) return;

            double radFromCenter = entity.position().distanceTo(position);
            Vec3 direction = entity.position().subtract(position).normalize();

            if(radFromCenter<=radiusOfEffect){
                double acceleration;
                //don't let anything inside of blue
                if(radFromCenter<radius){
                    entity.setDeltaMovement(0,0,0);
                    entity.addDeltaMovement(direction.scale(0.1));

                }
                else{
                    //acceleration is proportional to power
                    acceleration=(Math.pow(power,2)/1000000000)/(radFromCenter*radFromCenter*radFromCenter);
                    if(acceleration<3)
                        entity.addDeltaMovement(direction.scale(-acceleration));
                    else{
                        return;
                    }
                }

//                double entityMass=1;
//                if(Mass.mass.get(entity.getClass())!=null) entityMass=Mass.mass.get(entity.getClass());



            }
        });
    }

    public void updateSurroundingBlocks(MinecraftServer server){
        //for each blocks around position, turn them to graivty blocks
        //for each gravity block if the block.isFlying==false and block.position-position<radius
        //gravity block:
        //add movement based on gravity which is based on radius
        //rotate based on point closest to the force (only if not laggy)
        BlockPos.betweenClosedStream(new AABB(
                position.subtract(radius),
                position.add(radius)
        ));


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


    class GravityBlock extends Display.BlockDisplay{
        Vec3 acceleration;
        Vec3 velocity;
        int terminalVelocity;
        Display.BlockDisplay blockDisplay;

        public GravityBlock(EntityType<?> entityType, Level level) {
            super(entityType, level);
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

        @Override
        protected void updateRenderSubState(boolean interpolate, float partialTick) {

        }
    }

    @Override
    public void onScroll(double amount){
        this.distanceFromEye+=amount;
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


    private void testFunction() {

        //Var 5 and 9 don't exist lmao

        //var 1: the source/ the entity doing the explode
        //Var 2: the damage source
        //Var 3: the damage calculator which detects if a block should disapear, or how much damage happens
        //Var 4, 5,6,7: x,y,z,radius
        //Var 8: fire
        //Var 9: idk check ExplosionInteraction
        //Var 10: Particle Type
        //Var 11: Another Particle Type idk?
        //Var 12: IDK

        //Abstract explosion thing in Level
        //new Level().explode();
    }



}
