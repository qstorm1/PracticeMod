package com.qstorm.powers.cursedtechnique.limitless.power;

import com.qstorm.PracticeMod;
import com.qstorm.dimension.DimHandle;
import com.qstorm.powers.cursedtechnique.Domain;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class LimitlessDomain implements Domain {


    ComponentKey<Component> oldPos = ComponentRegistry.getOrCreate(Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"old-pos-after-domain"),
            Component.class)


    @Override
    public void onActivate(MinecraftServer server, ServerPlayer shooter) {
        //play animation
        onAnimationFinish(server,shooter);
    }

    public void onAnimationFinish(MinecraftServer server,ServerPlayer shooter){
        shooter.level().getAllEntities().forEach((entity)-> {
            if(shooter.position().distanceTo(entity.position())<=32){
                if(shooter==entity)
                    sendToLevel(server,entity,5,5,5);
                sendToLevel(server,entity);
            }
        });
    }

    public void sendToLevel(MinecraftServer server,Entity e){
        ServerLevel lev = (server.getLevel(DimHandle.LIMITLESS_VOID);
        if(lev==null) return;
        e.teleportTo(lev,0.0,0.0,0.0, Set.of(),0,0,false);
    }

    public void sendToLevel(MinecraftServer server,Entity e,double xNew,double yNew,double zNew){
        ServerLevel lev = (server.getLevel(DimHandle.LIMITLESS_VOID);
        if(lev==null) return;
        e.teleportTo(lev,xNew,yNew,zNew, Set.of(),0,0,false);
    }




    int x=0;
    int y=0;
    int z=0;

    Vec3 activationPosition;
    Vec3 activationDirection;//the direction the player is facing (opposite is where domain is active
    HashMap<BlockPos, BlockState> prevPos = new HashMap<>();
    //the domain making process, I'll try to make this only happen on the client
    public void initTick(ServerLevel level){
        //the back of the guy
        Vec3 vec3 = activationPosition.add(activationDirection.scale(-1)).add(x-16,y-16,z-16);
        BlockPos currentPos = new BlockPos((int)vec3.x,(int)vec3.y,(int)vec3.z);
        //save previous block state to memory
        //TODO: make this CCA
        prevPos.put(new BlockPos(x,y,z),level.getBlockState(new BlockPos(x,y,z)));
        level.setBlock(new BlockPos(x,y,z), Blocks.BLACK_CONCRETE.defaultBlockState(), Block.UPDATE_NONE);
        if(x<=32&&y<=32&&z<=32){
            int smallest=findSmallest(x,y,z);

            Random randomVal=new Random();
            int test;


            test = (int)Math.abs(randomVal.nextGaussian(0,1.8));
            if(test==x-smallest){
                x++;

            }
            else if(test==y-smallest){
                y++;
            }
            else if(test==z-smallest){
                z++;
            }

        }
    }

    public void onEnd(Level level){
        //reset previous blocks positions
        prevPos.forEach((k,v) -> {
            level.setBlock(k,v,Block.UPDATE_NONE);
        });
    }

    private static int findSmallest(int x, int y,int z){
        if(x<y&&x<z) return x;
        else if(y<x&&y<z) return y;
        else return z;
    }


}

/**
 * we have integers on each axis
 * x=1
 * y=1
 * z
 *
 * while(x<=16&&y<=16&&z<=16){
 *    subtract all vals by loewst numb ex: 3,6,4 -> 0,3,1
 *    pick one of the 3 numbers based on the values
 *    add 1 to that value and place a block at that location, save the previous block state, and re-replace once domain is complete
 *
 * }
 */