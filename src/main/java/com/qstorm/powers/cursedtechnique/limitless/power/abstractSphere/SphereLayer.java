package com.qstorm.powers.cursedtechnique.limitless.power.abstractSphere;

import com.qstorm.PracticeMod;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class SphereLayer {
    public static final ModelLayerLocation BLUE_INSTANCE = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(PracticeMod.MOD_ID,"sphere-instance"),
            "main");

    public static void registerLayers(){
        EntityModelLayerRegistry.registerModelLayer(SphereLayer.BLUE_INSTANCE,SphereModel::getTexturedModelData);
    }
}