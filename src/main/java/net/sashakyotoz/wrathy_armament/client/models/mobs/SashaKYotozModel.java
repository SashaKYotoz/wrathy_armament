package net.sashakyotoz.wrathy_armament.client.models.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;
import net.sashakyotoz.wrathy_armament.entities.animations.SashaKYotozAnimations;
import org.jetbrains.annotations.NotNull;

public class SashaKYotozModel<T extends SashaKYotoz> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("sashakyotoz_model"), "main");
    private final ModelPart generic;
    private final ModelPart head;
    private final ModelPart root;

    public SashaKYotozModel(ModelPart root) {
        this.generic = root.getChild("generic");
        this.head = generic.getChild("Head");
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition generic = partdefinition.addOrReplaceChild("generic", CubeListBuilder.create(), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition Head = generic.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(64, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.51F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition HatLayer_r1 = Head.addOrReplaceChild("HatLayer_r1", CubeListBuilder.create().texOffs(71, 7).mirror().addBox(-12.0F, -4.0F, 0.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, 0.0F, -0.2618F, 0.3491F));

        PartDefinition HatLayer_r2 = Head.addOrReplaceChild("HatLayer_r2", CubeListBuilder.create().texOffs(71, 7).addBox(4.0F, -4.0F, 0.0F, 8.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 1.0F, 0.0F, 0.2618F, -0.3491F));

        PartDefinition Body = generic.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.2505F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition chestplate = Body.addOrReplaceChild("chestplate", CubeListBuilder.create().texOffs(64, 60).addBox(-1.5F, 9.25F, -3.25F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(72, 59).addBox(-1.0F, 5.25F, -3.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition BodyLayer_r1 = chestplate.addOrReplaceChild("BodyLayer_r1", CubeListBuilder.create().texOffs(16, 35).addBox(-4.0F, -6.5F, -1.94F, 8.0F, 6.0F, 4.0F, new CubeDeformation(0.29F)), PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition cube_r1 = chestplate.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(64, 56).addBox(1.0F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -3.1416F, 0.1309F, 3.1416F));

        PartDefinition cube_r2 = chestplate.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-2.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -8.0F, 0.5F, -3.1416F, -0.1309F, 3.1416F));

        PartDefinition cube_r3 = chestplate.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(65, 56).mirror().addBox(1.25F, 0.5F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.0543F, 0.1309F, -3.0543F));

        PartDefinition cube_r4 = chestplate.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 39).addBox(-3.25F, 0.5F, -3.25F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 5.0F, -5.0F, 3.0543F, -0.1309F, 3.0543F));

        PartDefinition cube_r5 = chestplate.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(22, 39).mirror().addBox(4.25F, 0.75F, -3.5F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.0F, 5.0F, -5.0F, 3.0543F, 0.1309F, -3.0543F));

        PartDefinition cube_r6 = chestplate.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(64, 56).addBox(-4.25F, 0.5F, -3.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(65, 56).addBox(-2.25F, 0.5F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.0543F, -0.1309F, 3.0543F));

        PartDefinition cube_r7 = chestplate.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(64, 56).addBox(2.25F, 0.5F, -3.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 56).addBox(1.25F, 0.5F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 56).addBox(2.25F, 0.5F, -3.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 3.0543F, 0.1309F, -3.0543F));

        PartDefinition cube_r8 = chestplate.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(64, 56).addBox(2.75F, 0.5F, -3.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(65, 56).addBox(1.75F, 0.5F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.0543F, 0.1309F, -3.0543F));

        PartDefinition cube_r9 = chestplate.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(65, 56).mirror().addBox(-4.75F, 0.5F, -3.0F, 2.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(65, 56).addBox(-2.75F, 0.5F, -3.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 3.0543F, -0.1309F, 3.0543F));

        PartDefinition cube_r10 = chestplate.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(64, 56).addBox(-0.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.25F, -3.1416F, 0.1309F, 3.1416F));

        PartDefinition cube_r11 = chestplate.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-2.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -5.0F, 0.25F, -3.1416F, -0.1309F, 3.1416F));

        PartDefinition cube_r12 = chestplate.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(64, 56).addBox(-0.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -0.25F, -3.1416F, 0.1309F, 3.1416F));

        PartDefinition cube_r13 = chestplate.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-2.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -2.0F, -0.25F, -3.1416F, -0.1309F, 3.1416F));

        PartDefinition cube_r14 = chestplate.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(64, 56).addBox(-0.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.5F, -3.1416F, 0.1309F, 3.1416F));

        PartDefinition cube_r15 = chestplate.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-4.0F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.0F, 0.0F, -3.1416F, -0.1309F, 3.1416F));

        PartDefinition cube_r16 = chestplate.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-4.0F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, -0.1309F, 3.1416F));

        PartDefinition cube_r17 = chestplate.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(116, 58).addBox(-3.5F, -0.5F, -3.0F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 3.0543F, 0.0F, 2.8798F));

        PartDefinition cube_r18 = chestplate.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(116, 58).mirror().addBox(0.5F, -0.5F, -3.0F, 3.0F, 5.0F, 1.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 3.0543F, 0.0F, -2.8798F));

        PartDefinition cube_r19 = chestplate.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(116, 58).addBox(-2.5F, 0.5F, -3.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 3.0543F, 0.0F, 3.1416F));

        PartDefinition cube_r20 = chestplate.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(64, 56).addBox(1.0F, 0.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 3.0543F, 0.1309F, 3.1416F));

        PartDefinition cube_r21 = chestplate.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-4.0F, 0.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 3.0543F, -0.1309F, 3.1416F));

        PartDefinition cube_r22 = chestplate.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(64, 56).addBox(1.0F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -3.1416F, 0.1309F, 3.1416F));

        PartDefinition cube_r23 = chestplate.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(64, 56).addBox(0.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(64, 56).addBox(1.0F, 11.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.75F, -0.25F, 0.0F, -0.1309F, 0.0F));

        PartDefinition cube_r24 = chestplate.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(64, 56).mirror().addBox(-3.5F, 8.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(64, 56).mirror().addBox(-4.0F, 11.0F, -3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.75F, -0.25F, 0.0F, 0.1309F, 0.0F));

        PartDefinition cube_r25 = chestplate.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(72, 59).mirror().addBox(-1.75F, 8.0F, 1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -0.25F, 0.1309F, -0.0873F, 0.0F));

        PartDefinition cube_r26 = chestplate.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(72, 59).addBox(-0.25F, 8.0F, 1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -0.25F, 0.1309F, 0.0873F, 0.0F));

        PartDefinition RightArm = generic.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(78, 54).mirror().addBox(-3.5F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offset(-5.0F, -4.0F, 0.0F));

        PartDefinition wingtip0_r1 = RightArm.addOrReplaceChild("wingtip0_r1", CubeListBuilder.create().texOffs(56, 37).mirror().addBox(-24.0F, -2.25F, -3.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, -4.0F, 0.0F, -1.6581F, 0.0F, -0.2618F));

        PartDefinition RightArmLayer_r1 = RightArm.addOrReplaceChild("RightArmLayer_r1", CubeListBuilder.create().texOffs(64, 47).mirror().addBox(-4.5F, -13.25F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.3F)).mirror(false), PartPose.offsetAndRotation(1.0F, 4.0F, 0.0F, -0.1745F, 0.0F, -0.5236F));

        PartDefinition RightArmLayer_r2 = RightArm.addOrReplaceChild("RightArmLayer_r2", CubeListBuilder.create().texOffs(78, 54).mirror().addBox(-4.5F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.31F)).mirror(false), PartPose.offsetAndRotation(1.0F, 4.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

        PartDefinition LeftArm = generic.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(78, 54).addBox(-0.5F, -2.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.3F)), PartPose.offset(5.0F, -4.0F, 0.0F));

        PartDefinition wingtip1_r1 = LeftArm.addOrReplaceChild("wingtip1_r1", CubeListBuilder.create().texOffs(56, 37).addBox(11.0F, -2.25F, -3.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -4.0F, 0.0F, -1.6581F, 0.0F, 0.2618F));

        PartDefinition LeftArmLayer_r1 = LeftArm.addOrReplaceChild("LeftArmLayer_r1", CubeListBuilder.create().texOffs(64, 47).addBox(0.5F, -13.25F, -1.0F, 4.0F, 7.0F, 2.0F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(-1.0F, 4.0F, 0.0F, -0.1745F, 0.0F, 0.5236F));

        PartDefinition LeftArmLayer_r2 = LeftArm.addOrReplaceChild("LeftArmLayer_r2", CubeListBuilder.create().texOffs(78, 54).addBox(0.5F, -6.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.31F)), PartPose.offsetAndRotation(-1.0F, 4.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

        PartDefinition RightLeg = generic.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(94, 54).mirror().addBox(-2.25F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.275F)).mirror(false), PartPose.offset(-1.9F, 6.0F, 0.0F));

        PartDefinition LeftLeg = generic.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(94, 54).addBox(-1.75F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.275F)), PartPose.offset(1.9F, 6.0F, 0.0F));

        PartDefinition Lancer = generic.addOrReplaceChild("Lancer", CubeListBuilder.create(), PartPose.offset(-7.0F, -2.0F, 5.0F));

        PartDefinition cube_r27 = Lancer.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(104, 4).addBox(0.5F, -22.4939F, -22.2218F, 1.0F, 4.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(0.5F, -18.4939F, -24.2218F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(120, 22).addBox(0.5F, -21.4939F, -23.2218F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 13).addBox(0.5F, -12.5F, -24.2218F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(118, 0).addBox(0.5F, -15.4939F, -25.2218F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F))
                .texOffs(111, 22).addBox(0.5F, -18.4939F, -26.2218F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(0.5F, -20.4939F, -25.2218F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(124, 22).addBox(0.5F, -21.4939F, -24.2218F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(112, 16).addBox(0.5F, -24.4939F, -25.2218F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(0.5F, -23.4939F, -22.2218F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(102, 12).addBox(0.5F, -24.4939F, -21.2218F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.01F))
                .texOffs(111, 11).addBox(0.5F, -23.4939F, -20.2218F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(0.5F, -22.4939F, -19.2218F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(104, 0).addBox(0.5F, -21.4939F, -19.2218F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F))
                .texOffs(122, 14).addBox(0.5F, -20.4939F, -19.2218F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 9).addBox(0.5F, -18.75F, -18.25F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.011F)), PartPose.offsetAndRotation(0.0F, 9.0F, 8.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r28 = Lancer.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(122, 11).addBox(-0.5F, 18.636F, 6.636F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(123, 12).addBox(-0.5F, 19.636F, 8.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 12).addBox(-0.5F, 20.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 12).addBox(-0.5F, 21.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 12).addBox(-0.5F, 22.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 12).addBox(-0.5F, 23.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 8).addBox(-0.5F, 22.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 8).addBox(-0.5F, 21.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 6).addBox(-0.5F, 19.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 10).addBox(-0.5F, 20.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 8).addBox(-0.5F, 18.636F, 8.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(108, 0).addBox(-0.5F, 17.636F, 5.636F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.001F))
                .texOffs(123, 6).addBox(-0.5F, 20.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 6).addBox(-0.5F, 19.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 6).addBox(-0.5F, 18.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 6).addBox(-0.5F, 17.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 6).addBox(-0.5F, 14.636F, 8.636F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.001F))
                .texOffs(123, 4).addBox(-0.5F, 19.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(113, 6).addBox(-0.5F, 20.636F, 13.636F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 6).addBox(-0.5F, 18.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 4).addBox(-0.5F, 17.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 4).addBox(-0.5F, 15.636F, 9.636F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 0).addBox(-0.5F, 21.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 2).addBox(-0.5F, 20.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 2).addBox(-0.5F, 19.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 0).addBox(-0.5F, 18.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 0).addBox(-0.5F, 17.636F, 8.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(1.0F, 2.0F, -12.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r29 = Lancer.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(96, 0).addBox(-0.5F, -8.9393F, -9.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 5).addBox(-0.5F, -7.9393F, -8.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 5).addBox(-0.5F, 5.0607F, 4.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-0.5F, 6.0607F, 5.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 5).addBox(-0.5F, 3.0607F, 2.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-0.5F, 4.0607F, 3.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(-0.5F, 1.0607F, 0.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 0).addBox(-0.5F, 2.0607F, 1.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(-0.5F, -0.9393F, -1.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 5).addBox(-0.5F, -5.9393F, -6.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(-0.5F, -6.9393F, -7.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 5).addBox(-0.5F, -3.9393F, -4.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(-0.5F, -4.9393F, -5.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 5).addBox(-0.5F, -1.9393F, -2.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(96, 0).addBox(-0.5F, -2.9393F, -3.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(96, 5).addBox(-0.5F, 0.0607F, -0.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(1.0F, 8.5F, -4.0F, 0.7854F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }
    @Override
    public ModelPart root() {
        return this.root;
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.animateWalk(SashaKYotozAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        this.animate(entity.idle, SashaKYotozAnimations.IDLE, ageInTicks);
        this.animate(entity.death, SashaKYotozAnimations.DEATH, ageInTicks, 0.5f);
        this.animate(entity.attackByBlade, SashaKYotozAnimations.ATTACK_BY_BLADE, ageInTicks);
        this.animate(entity.attackByScythe, SashaKYotozAnimations.ATTACK, ageInTicks);
        this.animate(entity.attackPhantomRay, SashaKYotozAnimations.ATTACK_PHANTOM_RAY, ageInTicks,0.5f);
        this.animate(entity.attackCircleOfPhantoms, SashaKYotozAnimations.ATTACK_PHANTOM_CIRCLE, ageInTicks);
        this.animate(entity.flying, SashaKYotozAnimations.FLY, ageInTicks);
        this.animate(entity.takeOff, SashaKYotozAnimations.TAKEOFF, ageInTicks);
        this.animate(entity.landing, SashaKYotozAnimations.LANDING, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        generic.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}