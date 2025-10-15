package net.sashakyotoz.wrathy_armament.client.models.technical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;

public class AxeProjectileModel <T extends HarmfulProjectileEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("axe_projectile_model"), "main");
    private final ModelPart blade;
    private final ModelPart blade1;
    private final ModelPart stick;

    public AxeProjectileModel(ModelPart root) {
        this.blade = root.getChild("blade");
        this.blade1 = root.getChild("blade1");
        this.stick = root.getChild("stick");
    }
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 3.0F));

        PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, -9.0F, -8.0F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(46, 0).addBox(-1.0F, -9.0F, -10.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-1.0F, -2.0F, -11.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(32, 9).addBox(-1.0F, -1.0F, -12.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 8).addBox(-1.0F, 1.0F, -13.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(46, 26).addBox(-1.0F, -4.0F, -14.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 17).addBox(-1.0F, -6.0F, -13.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 17).addBox(-1.0F, -8.0F, -12.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 9).addBox(-1.0F, -9.0F, -11.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 24).addBox(-1.0F, -10.0F, -10.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 19).addBox(-1.0F, -11.0F, -9.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-1.0F, -12.0F, -8.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-1.0F, -13.0F, -6.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(24, 15).addBox(-1.0F, -14.0F, -4.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(-1.0F, -13.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(-1.0F, -11.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 8).addBox(-1.0F, -10.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 9).addBox(-1.0F, -11.0F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition blade1 = partdefinition.addOrReplaceChild("blade1", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, -3.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r2 = blade1.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(36, 0).addBox(-1.0F, -9.0F, -8.0F, 2.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(54, 0).addBox(-1.0F, -9.0F, -10.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 11).addBox(-1.0F, -2.0F, -11.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 26).addBox(-1.0F, -1.0F, -12.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(18, 13).addBox(-1.0F, 1.0F, -13.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 26).addBox(-1.0F, -4.0F, -14.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 9).addBox(-1.0F, -6.0F, -13.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(44, 9).addBox(-1.0F, -8.0F, -12.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(38, 17).addBox(-1.0F, -9.0F, -11.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 18).addBox(-1.0F, -10.0F, -10.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -11.0F, -9.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -12.0F, -8.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(18, 0).addBox(-1.0F, -13.0F, -6.0F, 2.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-1.0F, -14.0F, -4.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(-1.0F, -13.0F, 1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(14, 24).addBox(-1.0F, -12.0F, -1.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(24, 12).addBox(-1.0F, -11.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 8).addBox(-1.0F, -10.0F, -3.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 17).addBox(-1.0F, -11.0F, -5.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition stick = partdefinition.addOrReplaceChild("stick", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r3 = stick.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, 1.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(14, 0).mirror().addBox(-1.0F, -3.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false)
                .texOffs(24, 8).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(24, 8).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(14, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        blade1.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        stick.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
