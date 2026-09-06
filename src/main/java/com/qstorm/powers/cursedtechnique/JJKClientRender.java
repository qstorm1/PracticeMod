package com.qstorm.powers.cursedtechnique;

import com.mojang.blaze3d.vertex.PoseStack;
import com.qstorm.packets.Packet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.mixin.client.rendering.LevelRendererMixin;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;

@Environment(value= EnvType.CLIENT)
public class JJKClientRender {
    public static void initJJKClientRender(){
        //registerBlue();
        ClientPlayNetworking.registerGlobalReceiver(Packet.RenderBlueToClient.TYPE,((payload, context) -> {
            positionsToRender=payload.position();
            blueRadius=payload.radius();
        }));
    }

    static ArrayList<Double> positionsToRender;
    static double blueRadius;

    public static void registerBlue(){
        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
//            //just used for matrix math?
//            PoseStack currentLocation = context.matrices();
//            currentLocation.pushPose();
//            currentLocation.translate(positionsToRender.get(0),positionsToRender.get(1),positionsToRender.get(2));
//
//
//            //manages all of the vertexes(points) rendered, groups them by type like entity, block, or outline
//            MultiBufferSource mbTest = context.consumers();
//
//            AABB testBox=new AABB(0,0,0,0,0,0);
//
//            ShapeRenderer.renderShape();
        });
    }
}
