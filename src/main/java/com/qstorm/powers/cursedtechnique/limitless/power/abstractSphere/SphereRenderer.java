package com.qstorm.powers.cursedtechnique.limitless.power.abstractSphere;

import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import org.jspecify.annotations.NonNull;


@Environment(EnvType.CLIENT)
public class SphereRenderer <T extends Blue.BlueInstance> extends EntityRenderer<Blue.BlueInstance,SphereRenderState> {


    public SphereRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NonNull SphereRenderState createRenderState() {
        return new SphereRenderState();
    }

    protected int getBlockLightLevel(T blue, BlockPos blockPos) {
        return 15;
    }

}