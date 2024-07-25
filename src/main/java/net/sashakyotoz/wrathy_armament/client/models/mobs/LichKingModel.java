package net.sashakyotoz.wrathy_armament.client.models.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.animations.LichKingAnimations;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;

public class LichKingModel<T extends LichKing> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("lich_king_model"), "main");
    private final ModelPart Head;
    private final ModelPart body;
    private final ModelPart cloak;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart frostmourne;
    private final ModelPart root;

    public LichKingModel(ModelPart root) {
        this.Head = root.getChild("Head");
        this.body = root.getChild("body");
        this.cloak = body.getChild("cloak");
        this.rightArm = body.getChild("rightArm");
        this.leftArm = body.getChild("leftArm");
        this.frostmourne = root.getChild("frostmourne");
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 77).addBox(-4.0F, -14.0F, -3.5F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(4, 57).mirror().addBox(-0.5F, -16.0F, -3.75F, 1.0F, 12.0F, 8.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(18, 43).addBox(-0.5F, -17.0F, -3.5F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.51F))
                .texOffs(0, 97).addBox(-5.0F, -15.0F, -3.25F, 10.0F, 9.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -7.0F, -2.0F));

        PartDefinition head_r1 = Head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(0, 36).mirror().addBox(-0.5F, -46.0F, -5.5F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(0.0F, 29.0F, 2.0F, 0.0F, -0.2182F, 0.0F));

        PartDefinition head_r2 = Head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(0, 36).addBox(-3.5F, -46.0F, -5.5F, 4.0F, 1.0F, 7.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, 29.0F, 2.0F, 0.0F, 0.2182F, 0.0F));

        PartDefinition head_r3 = Head.addOrReplaceChild("head_r3", CubeListBuilder.create().texOffs(0, 57).mirror().addBox(0.5F, -7.0F, -2.5F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.65F)).mirror(false), PartPose.offsetAndRotation(0.0F, -9.0F, -1.25F, 0.0F, -0.1309F, 0.0F));

        PartDefinition head_r4 = Head.addOrReplaceChild("head_r4", CubeListBuilder.create().texOffs(0, 57).addBox(-4.5F, -7.0F, -2.5F, 4.0F, 12.0F, 8.0F, new CubeDeformation(0.65F)), PartPose.offsetAndRotation(0.0F, -9.0F, -1.25F, 0.0F, 0.1309F, 0.0F));

        PartDefinition spikes = Head.addOrReplaceChild("spikes", CubeListBuilder.create().texOffs(4, 30).addBox(-0.5F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F))
                .texOffs(4, 30).addBox(-0.5F, -7.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 120).addBox(-1.5F, -9.0F, -1.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.0F, -2.0F));

        PartDefinition head_r5 = spikes.addOrReplaceChild("head_r5", CubeListBuilder.create().texOffs(22, 120).addBox(-2.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(-3.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(-1.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 30).addBox(-1.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.4363F));

        PartDefinition head_r6 = spikes.addOrReplaceChild("head_r6", CubeListBuilder.create().texOffs(22, 120).addBox(1.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(2.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(0.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 30).addBox(0.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(34, 105).addBox(-9.0F, -4.0F, -6.0F, 18.0F, 12.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(1, 0).addBox(-9.0F, -4.0F, -6.0F, 18.0F, 12.0F, 11.0F, new CubeDeformation(0.25F))
                .texOffs(94, 117).addBox(-5.5F, 8.0F, -3.0F, 11.0F, 5.0F, 6.0F, new CubeDeformation(0.5F))
                .texOffs(90, 107).addBox(-6.5F, 12.0F, -3.0F, 13.0F, 4.0F, 6.0F, new CubeDeformation(1.01F))
                .texOffs(38, 71).addBox(-9.0F, -3.75F, -1.0F, 18.0F, 26.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.0F, 1.5F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 51).addBox(-9.0F, -4.0F, 1.5F, 9.0F, 1.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -0.2182F, 0.0F, -0.0873F));

        PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 51).addBox(0.0F, -4.0F, 1.5F, 9.0F, 1.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, -0.2182F, 0.0F, 0.0873F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(102, 62).addBox(-4.25F, -2.75F, -5.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-4.0F, -3.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition body_r2 = body.addOrReplaceChild("body_r2", CubeListBuilder.create().texOffs(102, 62).addBox(-0.75F, -2.75F, -5.0F, 5.0F, 2.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(4.0F, -3.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition body_r3 = body.addOrReplaceChild("body_r3", CubeListBuilder.create().texOffs(90, 73).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 7.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition body_r4 = body.addOrReplaceChild("body_r4", CubeListBuilder.create().texOffs(100, 80).addBox(0.0F, -1.0F, -5.0F, 9.0F, 7.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.1745F, -0.0873F, 0.0F));

        PartDefinition body_r5 = body.addOrReplaceChild("body_r5", CubeListBuilder.create().texOffs(100, 80).mirror().addBox(-9.0F, -1.0F, -5.0F, 9.0F, 7.0F, 5.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.1745F, 0.0873F, 0.0F));

        PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -9.0F, 0.0F, -0.2182F, 0.0F, 0.3491F));

        PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(7.0F, -9.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition cube_r6 = body.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -9.0F, 0.0F, 0.2182F, 0.0F, 0.3491F));

        PartDefinition arm1_r1 = body.addOrReplaceChild("arm1_r1", CubeListBuilder.create().texOffs(92, 92).mirror().addBox(-2.0F, -4.0F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(5.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition cube_r7 = body.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -9.0F, 0.0F, 0.2182F, 0.0F, -0.3491F));

        PartDefinition cube_r8 = body.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -9.0F, 0.0F, -0.2182F, 0.0F, -0.3491F));

        PartDefinition cube_r9 = body.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-7.0F, -9.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition arm0_r1 = body.addOrReplaceChild("arm0_r1", CubeListBuilder.create().texOffs(92, 92).addBox(-5.0F, -6.0F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-5.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cloak = body.addOrReplaceChild("cloak", CubeListBuilder.create().texOffs(38, 71).addBox(-9.0F, -1.75F, -1.0F, 18.0F, 26.0F, 8.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition cube_r10 = cloak.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(38, 71).addBox(0.0F, -13.0F, 8.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 13.0F, 9.5F, 1.5708F, -1.309F, -1.9199F));

        PartDefinition cube_r11 = cloak.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(38, 45).addBox(-0.25F, -13.0F, 0.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 13.0F, 9.75F, 1.5708F, -1.309F, -1.9199F));

        PartDefinition cube_r12 = cloak.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(38, 71).addBox(8.0F, -1.75F, -6.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 5.0F, 0.2618F, 0.0F, -0.3491F));

        PartDefinition cube_r13 = cloak.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(38, 71).addBox(0.0F, -13.0F, 8.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(38, 45).addBox(0.0F, -13.0F, 0.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 13.0F, 9.75F, 1.5708F, 1.309F, 1.9199F));

        PartDefinition cube_r14 = cloak.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(38, 71).addBox(-8.0F, -1.75F, -6.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 5.0F, 0.2618F, 0.0F, 0.3491F));

        PartDefinition rightArm = body.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(106, 0).addBox(-14.0F, -4.0F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 108).addBox(-11.0F, -4.0F, 3.0F, 0.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition arm0_r2 = rightArm.addOrReplaceChild("arm0_r2", CubeListBuilder.create().texOffs(92, 92).addBox(-5.0F, -6.0F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 3.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition arm0_r3 = rightArm.addOrReplaceChild("arm0_r3", CubeListBuilder.create().texOffs(109, 3).addBox(-2.0F, -8.0F, 0.0F, 5.0F, 10.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(-12.0F, 8.0F, 0.0F, 0.0F, -0.2182F, 0.0F));

        PartDefinition shoulder = rightArm.addOrReplaceChild("shoulder", CubeListBuilder.create(), PartPose.offset(-14.0F, -2.0F, 0.0F));

        PartDefinition cube_r15 = shoulder.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, -1.0472F));

        PartDefinition cube_r16 = shoulder.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition cube_r17 = shoulder.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, -1.0472F));

        PartDefinition cube_r18 = shoulder.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cube_r19 = shoulder.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.2182F, 0.0F, -0.3491F));

        PartDefinition cube_r20 = shoulder.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, -0.2182F, 0.0F, -0.3491F));

        PartDefinition shoulder4 = shoulder.addOrReplaceChild("shoulder4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r21 = shoulder4.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 1.0F, 0.0F, 0.2182F, 0.0F, -1.0472F));

        PartDefinition cube_r22 = shoulder4.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-6.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition cube_r23 = shoulder4.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 1.0F, 0.0F, -0.2182F, 0.0F, -1.0472F));

        PartDefinition cube_r24 = shoulder4.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cube_r25 = shoulder4.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, 0.2182F, 0.0F, -0.3491F));

        PartDefinition cube_r26 = shoulder4.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 44).addBox(-3.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, -0.2182F, 0.0F, -0.3491F));

        PartDefinition rightArm1 = rightArm.addOrReplaceChild("rightArm1", CubeListBuilder.create().texOffs(106, 0).mirror().addBox(-2.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(2, 109).addBox(1.0F, 1.0F, 1.0F, 0.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.0F, 8.0F, 0.0F));

        PartDefinition right_fingers = rightArm1.addOrReplaceChild("right_fingers", CubeListBuilder.create(), PartPose.offset(-1.75F, 13.0F, -1.0F));

        PartDefinition arm0_r4 = right_fingers.addOrReplaceChild("arm0_r4", CubeListBuilder.create().texOffs(98, 23).addBox(-5.0F, -4.0F, -6.0F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-0.25F, -3.0F, 1.0F, 1.5708F, 0.0F, -1.5708F));

        PartDefinition spikes1 = rightArm.addOrReplaceChild("spikes1", CubeListBuilder.create().texOffs(4, 30).addBox(-0.5F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F))
                .texOffs(4, 30).addBox(-0.5F, -7.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 120).addBox(-1.5F, -9.0F, -1.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, -1.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        PartDefinition head_r7 = spikes1.addOrReplaceChild("head_r7", CubeListBuilder.create().texOffs(22, 120).addBox(-2.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(-3.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(-1.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 30).addBox(-1.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.4363F));

        PartDefinition head_r8 = spikes1.addOrReplaceChild("head_r8", CubeListBuilder.create().texOffs(22, 120).addBox(1.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(2.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(19, 30).addBox(0.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 30).addBox(0.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

        PartDefinition leftArm = body.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(106, 0).addBox(9.0F, -4.0F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 108).addBox(11.0F, -4.0F, 3.0F, 0.0F, 14.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition arm1_r2 = leftArm.addOrReplaceChild("arm1_r2", CubeListBuilder.create().texOffs(92, 92).mirror().addBox(-2.0F, -4.0F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition arm1_r3 = leftArm.addOrReplaceChild("arm1_r3", CubeListBuilder.create().texOffs(109, 3).addBox(-3.0F, -8.0F, 0.0F, 5.0F, 10.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(12.0F, 8.0F, 0.0F, 0.0F, 0.2182F, 0.0F));

        PartDefinition shoulder2 = leftArm.addOrReplaceChild("shoulder2", CubeListBuilder.create(), PartPose.offset(11.0F, -4.0F, 0.0F));

        PartDefinition cube_r27 = shoulder2.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition cube_r28 = shoulder2.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, -0.2182F, 0.0F, 1.0472F));

        PartDefinition cube_r29 = shoulder2.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition cube_r30 = shoulder2.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, 0.2182F, 0.0F, 1.0472F));

        PartDefinition cube_r31 = shoulder2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.3491F));

        PartDefinition cube_r32 = shoulder2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.3491F));

        PartDefinition shoulder3 = shoulder2.addOrReplaceChild("shoulder3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r33 = shoulder3.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition cube_r34 = shoulder3.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, -0.2182F, 0.0F, 1.0472F));

        PartDefinition cube_r35 = shoulder3.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 26).addBox(-2.0F, -2.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition cube_r36 = shoulder3.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.2182F, 0.0F, 1.0472F));

        PartDefinition cube_r37 = shoulder3.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, 0.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 0.3491F));

        PartDefinition cube_r38 = shoulder3.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 44).addBox(-2.0F, -2.0F, -4.0F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.2182F, 0.0F, 0.3491F));

        PartDefinition leftArm1 = leftArm.addOrReplaceChild("leftArm1", CubeListBuilder.create().texOffs(106, 0).addBox(-3.0F, 0.0F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.1F))
                .texOffs(2, 109).addBox(-1.0F, 1.0F, 1.0F, 0.0F, 12.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, 8.0F, 0.0F));

        PartDefinition left_fingers = leftArm1.addOrReplaceChild("left_fingers", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition arm0_r5 = left_fingers.addOrReplaceChild("arm0_r5", CubeListBuilder.create().texOffs(98, 23).mirror().addBox(-7.0F, -4.0F, -4.0F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition spikes2 = leftArm.addOrReplaceChild("spikes2", CubeListBuilder.create().texOffs(4, 30).addBox(-0.5F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F))
                .texOffs(4, 30).addBox(-0.5F, -7.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 120).addBox(-1.5F, -9.0F, -1.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.0F, -1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition head_r9 = spikes2.addOrReplaceChild("head_r9", CubeListBuilder.create().texOffs(18, 120).addBox(-2.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 30).addBox(-3.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(12, 30).addBox(-1.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 30).addBox(-1.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.4363F));

        PartDefinition head_r10 = spikes2.addOrReplaceChild("head_r10", CubeListBuilder.create().texOffs(20, 120).addBox(1.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 30).addBox(2.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(15, 30).addBox(0.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 30).addBox(0.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

        PartDefinition rightLeg = body.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(112, 72).addBox(-3.5F, 0.0F, -5.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(-4.0F, 18.0F, 0.0F));

        PartDefinition leg0_r1 = rightLeg.addOrReplaceChild("leg0_r1", CubeListBuilder.create().texOffs(88, 56).addBox(-5.5F, 0.0F, -3.0F, 6.0F, 9.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(2.0F, -5.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition rightLeg1 = rightLeg.addOrReplaceChild("rightLeg1", CubeListBuilder.create().texOffs(58, 60).addBox(-2.5F, 5.0F, -4.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.25F))
                .texOffs(114, 52).addBox(-2.0F, 7.0F, -6.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 3.0F, 0.0F));

        PartDefinition leg0_r2 = rightLeg1.addOrReplaceChild("leg0_r2", CubeListBuilder.create().texOffs(88, 56).addBox(-5.5F, -2.0F, -4.0F, 6.0F, 9.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition leftLeg = body.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(112, 72).mirror().addBox(-3.5F, 0.0F, -5.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(5.0F, 18.0F, 0.0F));

        PartDefinition leg1_r1 = leftLeg.addOrReplaceChild("leg1_r1", CubeListBuilder.create().texOffs(84, 42).mirror().addBox(-0.5F, 0.0F, -3.0F, 6.0F, 9.0F, 5.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -5.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition leftLeg1 = leftLeg.addOrReplaceChild("leftLeg1", CubeListBuilder.create().texOffs(58, 60).mirror().addBox(-3.5F, 5.0F, -4.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false)
                .texOffs(114, 52).mirror().addBox(-3.0F, 7.0F, -6.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 3.0F, 0.0F));

        PartDefinition leg1_r2 = leftLeg1.addOrReplaceChild("leg1_r2", CubeListBuilder.create().texOffs(106, 38).mirror().addBox(-0.5F, -2.0F, -4.0F, 6.0F, 9.0F, 5.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(-3.0F, 2.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition frostmourne = partdefinition.addOrReplaceChild("frostmourne", CubeListBuilder.create(), PartPose.offsetAndRotation(-6.0F, 24.0F, -10.0F, 1.5708F, 0.0F, 0.0873F));

        PartDefinition blade = frostmourne.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(-8.0F, -8.0F, 8.0F));

        PartDefinition cube_r39 = blade.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(212, 22).addBox(-1.0F, 1.0F, 12.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(204, 15).addBox(-1.0F, -1.0F, 10.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(212, 17).addBox(-1.0F, -2.0F, 8.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 20).addBox(-1.0F, -3.0F, 6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(218, 12).addBox(-1.0F, -4.0F, 4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(196, 12).addBox(-1.0F, -14.0F, -2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(220, 32).addBox(-1.0F, -12.0F, -2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(210, 37).addBox(-1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(210, 32).addBox(-1.0F, -8.0F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(206, 4).addBox(-0.75F, -20.0F, 15.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(214, 11).addBox(-0.75F, -23.0F, 22.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(192, 50).addBox(-0.75F, -20.0F, 20.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(225, 0).addBox(-0.75F, -24.0F, 20.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(192, 11).addBox(-0.75F, -23.0F, 23.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(213, 0).addBox(-0.75F, -23.0F, 18.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(218, 4).addBox(-0.75F, -22.0F, 17.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(192, 45).addBox(-0.75F, -18.0F, 18.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(226, 13).addBox(-0.75F, -18.0F, 13.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(192, 4).addBox(-0.75F, -16.0F, 16.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(219, 0).addBox(-1.25F, -21.0F, 16.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(192, 33).addBox(-1.25F, -20.0F, 14.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(250, 47).addBox(-1.25F, -9.0F, 12.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(250, 47).addBox(-1.25F, -7.0F, 11.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(239, 11).addBox(-1.25F, -11.0F, 14.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(250, 47).addBox(-1.25F, -11.0F, 15.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(219, 0).addBox(-1.25F, -15.0F, 19.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(219, 0).addBox(-1.25F, -17.0F, 20.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(204, 25).addBox(-1.25F, -13.0F, 17.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 33).addBox(-1.25F, -15.0F, 18.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(250, 21).addBox(-1.25F, -13.0F, 16.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(239, 11).addBox(-1.25F, -7.0F, 10.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(192, 42).addBox(-1.25F, -5.0F, 8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(192, 33).addBox(-1.25F, -16.0F, 10.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(235, 1).addBox(-1.25F, -17.0F, 10.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(226, 13).addBox(-0.75F, -16.0F, 11.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(192, 4).addBox(-0.75F, -14.0F, 14.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(212, 25).addBox(-1.25F, -13.0F, 6.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(229, 44).addBox(-1.25F, -12.0F, 3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(250, 40).addBox(-1.25F, -14.0F, 4.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(192, 33).addBox(-1.25F, -12.0F, 5.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(192, 33).addBox(-1.25F, -12.0F, 6.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(192, 33).addBox(-1.25F, -14.0F, 8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 13).addBox(-0.75F, -14.0F, 9.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(192, 4).addBox(-0.75F, -12.0F, 12.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(226, 13).addBox(-0.75F, -12.0F, 7.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(192, 4).addBox(-0.75F, -10.0F, 10.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(226, 13).addBox(-0.75F, -10.0F, 5.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(192, 4).addBox(-0.75F, -8.0F, 8.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(242, 57).addBox(-1.0F, -8.0F, 3.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(204, 20).addBox(-1.0F, -6.0F, 6.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(238, 11).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(242, 4).addBox(-1.0F, 1.0F, 2.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(192, 28).addBox(-0.5F, -2.0F, 2.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(220, 20).addBox(-1.0F, -7.0F, -3.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(226, 37).addBox(-0.5F, -4.0F, -1.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition handle = frostmourne.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(249, 13).addBox(-9.0F, -1.5F, 18.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(198, 0).addBox(-9.0F, -2.5F, 19.25F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F))
                .texOffs(192, 0).addBox(-9.0F, -2.5F, -4.25F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F))
                .texOffs(233, 13).addBox(-9.0F, -1.5F, -3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r40 = handle.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(210, 0).addBox(-1.0F, 8.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(238, 21).addBox(-1.0F, 6.0F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(238, 21).addBox(-1.0F, 4.0F, -3.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(225, 5).addBox(-0.5F, 5.0F, 1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 36).addBox(-1.0F, 5.0F, 5.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(230, 32).addBox(-1.0F, 6.0F, 2.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(230, 5).addBox(-1.0F, -7.0F, -9.0F, 2.0F, 2.0F, 4.0F, new CubeDeformation(-0.01F))
                .texOffs(192, 15).addBox(-1.0F, -5.0F, -8.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(248, 36).addBox(-1.0F, -2.0F, -6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(225, 5).addBox(-0.5F, -3.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(248, 32).addBox(-1.0F, 4.0F, 0.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(240, 20).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 6.0F, 6.0F, new CubeDeformation(0.1F))
                .texOffs(210, 0).addBox(-1.0F, -1.0F, -9.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(220, 27).addBox(-1.0F, 0.0F, -9.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(220, 27).addBox(-1.0F, 1.0F, -7.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(240, 32).addBox(-1.0F, 3.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(205, 4).addBox(-0.5F, 4.0F, -6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(213, 5).addBox(-0.5F, 5.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(205, 4).addBox(-0.5F, 6.0F, -8.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(198, 7).addBox(-1.0F, 7.0F, -9.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(230, 26).addBox(-1.0F, 8.0F, -11.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 8.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r41 = handle.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(208, 11).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-8.0F, 1.0F, -3.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r42 = handle.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(204, 0).addBox(-1.0F, -0.75F, 0.25F, 2.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-8.0F, 1.0F, 18.75F, -0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r43 = handle.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(212, 28).addBox(-1.0F, -9.0F, -10.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 9.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r44 = handle.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(204, 28).addBox(-1.0F, 8.75F, 7.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-8.0F, -1.0F, 6.75F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.Head.xRot = headPitch / (180F / (float) Math.PI);
        animateIdlePose(ageInTicks,entity);
        if (entity.isInHealingState() && !entity.transition_to_heal.isStarted() && !entity.isDeadOrDying()) {
            this.rightArm.xRot = -1.5f;
            this.rightArm.yRot = -0.5f;
            this.rightArm.zRot = 0.3f;
            this.leftArm.xRot = -1.5f;
            this.leftArm.yRot = 0.5f;
            this.leftArm.zRot = -0.3f;
            this.frostmourne.xRot = -3f;
            this.frostmourne.zRot = -0.05f;
            this.frostmourne.x = 8.5f;
            this.frostmourne.y = -5;
        }
        this.animateWalk(LichKingAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        this.animate(entity.combo_attack, LichKingAnimations.COMBO_ATTACK, ageInTicks, 1.1f);
        this.animate(entity.combo_attack1, LichKingAnimations.COMBO_ATTACK1, ageInTicks);
        this.animate(entity.combo_attack2, LichKingAnimations.COMBO_ATTACK2, ageInTicks);
        this.animate(entity.spin_attack, LichKingAnimations.SPIN_ATTACK, ageInTicks);
        this.animate(entity.rain_cast, LichKingAnimations.RAIN_CAST, ageInTicks, 0.5f);
        this.animate(entity.transition_to_heal, LichKingAnimations.TRANSITION_TO_HEAL, ageInTicks);
        this.animate(entity.summon, LichKingAnimations.SUMMON, ageInTicks, 0.5f);
        this.animate(entity.spawn, LichKingAnimations.SPAWN, ageInTicks, 0.5f);
        this.animate(entity.death, LichKingAnimations.DEATH, ageInTicks, 0.25f);
    }
    private void animateIdlePose(float ageInTicks,T entity) {
        float f = ageInTicks * 0.1F;
        float f1 = Mth.cos(f);
        float f2 = Mth.sin(f);
        float modifier =(float)entity.getDeltaMovement().horizontalDistanceSqr();
        this.Head.zRot += 0.06F * f1;
        this.Head.xRot += 0.06F * f2;
        this.cloak.xRot += 0.05F * f1 * modifier * 10;
    }
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        frostmourne.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}