package com.qstorm.powers.cursedtechnique.limitless.power.abstractSphere;

import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;



@Environment(EnvType.CLIENT)
public class SphereRenderer <T extends Blue.BlueInstance> extends EntityRenderer {

    protected SphereRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
    protected int getBlockLightLevel(T blue, BlockPos blockPos) {
        return 15;
    }
}