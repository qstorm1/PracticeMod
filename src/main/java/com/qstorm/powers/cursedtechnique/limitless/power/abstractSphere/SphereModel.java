package com.qstorm.powers.cursedtechnique.limitless.power.abstractSphere;

import com.qstorm.powers.cursedtechnique.limitless.power.Blue;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class SphereModel extends EntityModel<SphereRenderState> {

    public final ModelPart body;

    protected SphereModel(ModelPart root) {
        super(root);
        body=root.getChild(PartNames.BODY);
    }
    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition root = modelData.getRoot();
        root.addOrReplaceChild(
                PartNames.BODY,
                CubeListBuilder.create().addBox(
                        /* x */ -6,
                        /* y */ -6,
                        /* z */ -6,
                        /* width */ 12,
                        /* height */ 12,
                        /* depth */ 12
                ),
                PartPose.offset(0, 8, 0)
        );
        return LayerDefinition.create(modelData,64,32);
    }
}
