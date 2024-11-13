package net.sashakyotoz.wrathy_armament.client.models.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;
import net.sashakyotoz.wrathy_armament.entities.animations.TrueEyeOfCthulhuAnimations;

public class TrueEyeOfCthulhuModel <T extends TrueEyeOfCthulhu> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("true_eye_of_cthulhu"), "main");
    private final ModelPart root;
    private final ModelPart head;

    public TrueEyeOfCthulhuModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 40).addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(38, 40).addBox(-6.0F, -5.0F, 6.0F, 12.0F, 10.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(21, 1).addBox(-5.0F, -7.0F, -6.0F, 10.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(21, 14).addBox(-5.0F, 6.0F, -6.0F, 10.0F, 1.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).mirror().addBox(6.0F, -5.0F, -6.0F, 1.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 18).addBox(-7.0F, -5.0F, -6.0F, 1.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 18.0F, 0.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 56).addBox(-6.0F, -2.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 2.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(48, 56).mirror().addBox(-2.0F, -2.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition tencles = head.addOrReplaceChild("tencles", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.0F, -2.0F, 8.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition centralTencle = tencles.addOrReplaceChild("centralTencle", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r3 = centralTencle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition rightTencle = tencles.addOrReplaceChild("rightTencle", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.0F, 1.0F));

        PartDefinition rightFrontTencle = tencles.addOrReplaceChild("rightFrontTencle", CubeListBuilder.create(), PartPose.offset(2.0F, 2.0F, -1.0F));

        PartDefinition cube_r4 = rightFrontTencle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition leftBackTencle = tencles.addOrReplaceChild("leftBackTencle", CubeListBuilder.create(), PartPose.offset(-2.0F, 2.0F, 1.0F));

        PartDefinition cube_r5 = leftBackTencle.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition leftTencle = tencles.addOrReplaceChild("leftTencle", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 2.0F, -1.0F));

        PartDefinition tencles1 = head.addOrReplaceChild("tencles1", CubeListBuilder.create(), PartPose.offsetAndRotation(1.0F, 3.0F, 8.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition centralTencle2 = tencles1.addOrReplaceChild("centralTencle2", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition cube_r6 = centralTencle2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition rightTencle2 = tencles1.addOrReplaceChild("rightTencle2", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 2.0F, 1.0F));

        PartDefinition leftFrontTencle = tencles1.addOrReplaceChild("leftFrontTencle", CubeListBuilder.create(), PartPose.offset(-2.0F, 2.0F, -1.0F));

        PartDefinition cube_r7 = leftFrontTencle.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition rightBackTencle = tencles1.addOrReplaceChild("rightBackTencle", CubeListBuilder.create(), PartPose.offset(2.0F, 2.0F, 1.0F));

        PartDefinition cube_r8 = rightBackTencle.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 40).mirror().addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition leftTencle2 = tencles1.addOrReplaceChild("leftTencle2", CubeListBuilder.create().texOffs(0, 40).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 10.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 2.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = pNetHeadYaw / (180F / (float) Math.PI);
        this.root.xRot = pHeadPitch / (180F / (float) Math.PI);
        this.animate(pEntity.interactive, TrueEyeOfCthulhuAnimations.INTERACTIVE,pAgeInTicks);
    }
}