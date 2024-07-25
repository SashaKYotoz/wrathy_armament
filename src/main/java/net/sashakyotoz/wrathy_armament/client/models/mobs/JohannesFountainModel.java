package net.sashakyotoz.wrathy_armament.client.models.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.animations.JohannesFountainAnimations;
import net.sashakyotoz.wrathy_armament.entities.bosses.JohannesKnight;

public class JohannesFountainModel <T extends JohannesKnight> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("johannes_fountain_model"), "main");
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    private final ModelPart root;

    public JohannesFountainModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.Head = root.getChild("Head");
        this.Body = root.getChild("Body");
        this.rightArm = root.getChild("rightArm");
        this.leftArm = root.getChild("leftArm");
        this.rightLeg = root.getChild("rightLeg");
        this.leftLeg = root.getChild("leftLeg");
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(39, 41).addBox(-5.75F, -11.0F, -5.625F, 11.0F, 12.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.25F, -12.0F, -0.0615F));

        PartDefinition helmet = Head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(25, 135).addBox(-6.0F, -36.5F, -8.9733F, 2.0F, 11.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(121, 103).addBox(-4.245F, -35.995F, -8.9783F, 9.0F, 6.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(0, 30).addBox(-1.25F, -31.5F, -8.9733F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(46, 0).addBox(-4.75F, -37.25F, -8.5983F, 9.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(82, 134).addBox(4.5F, -36.5F, -8.9733F, 2.0F, 11.0F, 2.0F, new CubeDeformation(-0.01F))
                .texOffs(0, 30).addBox(-6.75F, -36.5F, -7.4733F, 14.0F, 11.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 52).addBox(-0.75F, -37.5F, -9.8034F, 2.0F, 6.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 35).addBox(3.0F, -27.75F, -8.9733F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(41, 0).addBox(-2.75F, -26.0F, -8.8034F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(46, 5).addBox(-4.5F, -27.75F, -8.9733F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 25.0F, 2.3034F));

        PartDefinition cube_r1 = helmet.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(134, 130).addBox(4.2519F, -9.84F, -3.1253F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, 0.5802F, 0.6006F, -1.2074F));

        PartDefinition cube_r2 = helmet.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(93, 0).addBox(4.0303F, -7.7143F, -5.2629F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, 0.0131F, 0.2995F, -1.4049F));

        PartDefinition cube_r3 = helmet.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 130).addBox(2.148F, -8.9151F, -5.5584F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, -0.1355F, -0.2618F, -1.2741F));

        PartDefinition cube_r4 = helmet.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(60, 122).addBox(0.8582F, -7.6094F, -4.5772F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, -0.6109F, 0.0F, -1.309F));

        PartDefinition cube_r5 = helmet.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(11, 135).addBox(-6.7519F, -9.84F, -3.1253F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, 0.5802F, -0.6006F, 1.2074F));

        PartDefinition cube_r6 = helmet.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(134, 118).addBox(-7.0303F, -7.7143F, -5.2629F, 3.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, 0.0131F, -0.2995F, 1.4049F));

        PartDefinition cube_r7 = helmet.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(109, 130).addBox(-4.898F, -8.9151F, -5.5584F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, -0.1355F, 0.2618F, 1.2741F));

        PartDefinition cube_r8 = helmet.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(75, 126).addBox(-5.8582F, -7.6094F, -4.5772F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, -30.0F, -2.3034F, -0.6109F, 0.0F, 1.309F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 244).addBox(-5.5F, -1.0F, -3.75F, 11.0F, 6.0F, 6.0F, new CubeDeformation(0.25F))
                .texOffs(72, 37).addBox(-6.0F, -3.0F, -5.25F, 12.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(46, 13).addBox(-7.0F, -4.0F, -5.0F, 14.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(115, 62).addBox(-4.0F, -7.5F, -6.75F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(117, 29).addBox(-3.5F, -3.5F, -5.75F, 7.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(39, 22).addBox(-8.0F, -11.0F, -5.75F, 16.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(96, 77).addBox(-10.5F, -13.0F, -6.25F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(87, 95).addBox(4.5F, -13.0F, -6.25F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r9 = Body.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(18, 52).addBox(-2.65F, -1.0F, -3.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(4.0F, -2.0F, -0.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r10 = Body.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 236).addBox(-2.65F, 3.0F, -2.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(4.0F, -1.0F, -0.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r11 = Body.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(16, 236).addBox(-2.35F, 3.0F, -2.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-4.0F, -1.0F, -0.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r12 = Body.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(48, 64).addBox(-2.35F, -1.0F, -3.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-4.0F, -2.0F, -0.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r13 = Body.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(34, 247).addBox(-4.5F, -0.75F, -4.0F, 9.0F, 4.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -0.75F, -2.5F, 1.1345F, 0.0F, 0.0F));

        PartDefinition cube_r14 = Body.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(30, 62).addBox(-1.5F, -2.5F, -1.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(20, 72).addBox(0.5F, -2.5F, -1.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-0.5F, -2.5F, -1.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.1805F, -12.1843F, 0.3656F, -0.3054F, 0.0F, -0.6981F));

        PartDefinition cube_r15 = Body.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(76, 39).addBox(0.5F, -2.5F, -1.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(79, 0).addBox(-1.5F, -2.5F, -1.0F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(18, 52).addBox(-0.5F, -2.5F, -1.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.1805F, -12.1843F, 0.3656F, -0.3054F, 0.0F, 0.6981F));

        PartDefinition cube_r16 = Body.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(54, 5).addBox(4.0F, -7.5F, -1.75F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(83, 47).addBox(-1.0F, -3.5F, -6.25F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(7.0F, -9.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

        PartDefinition cube_r17 = Body.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(8, 35).addBox(-5.0F, -7.0F, -1.25F, 1.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(94, 62).addBox(-5.0F, -3.5F, -6.25F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(-7.0F, -9.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

        PartDefinition cape = Body.addOrReplaceChild("cape", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -8.0F, 0.0F, 18.0F, 25.0F, 5.0F, new CubeDeformation(0.01F)), PartPose.offset(0.0F, -2.0F, 2.0F));

        PartDefinition cube_r18 = cape.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 64).addBox(-8.0F, -1.75F, -6.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 4.0F, 0.2618F, 0.0F, 0.3491F));

        PartDefinition cube_r19 = cape.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(48, 72).addBox(0.0F, -13.0F, 0.0F, 0.0F, 25.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 5.5F, 8.75F, 1.5708F, 1.309F, 1.9199F));

        PartDefinition cube_r20 = cape.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(16, 72).addBox(0.0F, -13.0F, 8.0F, 0.0F, 25.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 5.5F, 9.5F, 1.5708F, 1.309F, 1.9199F));

        PartDefinition cube_r21 = cape.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 64).addBox(8.0F, -1.75F, -6.0F, 0.0F, 26.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 4.0F, 0.2618F, 0.0F, -0.3491F));

        PartDefinition cube_r22 = cape.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(40, 72).addBox(0.0F, -13.0F, 0.0F, 0.0F, 25.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 5.5F, 8.75F, 1.5708F, -1.309F, -1.9199F));

        PartDefinition cube_r23 = cape.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(16, 72).addBox(0.0F, -13.0F, 8.0F, 0.0F, 25.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 5.5F, 9.5F, 1.5708F, -1.309F, -1.9199F));

        PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-9.5F, -8.0F, -2.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition arm0_r1 = rightArm.addOrReplaceChild("arm0_r1", CubeListBuilder.create().texOffs(60, 104).addBox(-14.1743F, -34.9924F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.9725F, 30.8896F, 2.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition arm0_r2 = rightArm.addOrReplaceChild("arm0_r2", CubeListBuilder.create().texOffs(122, 34).addBox(-2.1702F, -9.9924F, 0.0377F, 5.0F, 10.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(-3.1516F, 8.9237F, 2.0F, 0.0F, -0.2182F, 0.0873F));

        PartDefinition arm0_r3 = rightArm.addOrReplaceChild("arm0_r3", CubeListBuilder.create().texOffs(58, 64).addBox(-4.4824F, -7.9319F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2728F, 4.2042F, 2.0F, 0.0F, 0.0F, -0.2618F));

        PartDefinition spikes1 = rightArm.addOrReplaceChild("spikes1", CubeListBuilder.create().texOffs(104, 41).addBox(-0.5F, -5.9924F, -1.6743F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F))
                .texOffs(104, 0).addBox(-0.5F, -8.9924F, -1.6743F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(96, 77).addBox(-1.5F, -10.9924F, -1.1743F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.352F, -0.3907F, 2.0F, 0.0F, 1.5708F, 0.0873F));

        PartDefinition head_r1 = spikes1.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(78, 37).addBox(-1.9132F, -10.8057F, -0.8021F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(81, 13).addBox(-2.9132F, -7.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(88, 47).addBox(-0.9132F, -10.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(63, 64).addBox(-0.4132F, -5.8057F, -1.3021F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.4363F));

        PartDefinition head_r2 = spikes1.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(79, 21).addBox(0.9132F, -10.8057F, -0.8021F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(87, 29).addBox(1.9132F, -7.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 98).addBox(-0.0868F, -10.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(99, 6).addBox(-0.5868F, -5.8057F, -1.3021F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

        PartDefinition rightArm1 = rightArm.addOrReplaceChild("rightArm1", CubeListBuilder.create().texOffs(16, 114).mirror().addBox(-2.1743F, -1.9924F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.1F)).mirror(false)
                .texOffs(106, 41).addBox(-3.1743F, -3.9924F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-3.1516F, 8.9237F, 2.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition right_fingers = rightArm1.addOrReplaceChild("right_fingers", CubeListBuilder.create(), PartPose.offset(-1.75F, 13.0F, -1.0F));

        PartDefinition arm0_r4 = right_fingers.addOrReplaceChild("arm0_r4", CubeListBuilder.create().texOffs(81, 14).addBox(-3.0076F, -4.0F, -5.8257F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-0.25F, -3.0F, 1.0F, 1.5708F, 0.0F, -1.5708F));

        PartDefinition shoulder = rightArm.addOrReplaceChild("shoulder", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.2724F, -1.2126F, 2.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition cube_r24 = shoulder.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(48, 130).addBox(-1.3617F, -3.12F, -3.7517F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, -1.0472F));

        PartDefinition cube_r25 = shoulder.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(54, 105).addBox(-1.3617F, -3.1472F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition cube_r26 = shoulder.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(132, 70).addBox(-1.3617F, -3.12F, -0.2483F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, -1.0472F));

        PartDefinition shoulder4 = shoulder.addOrReplaceChild("shoulder4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

        PartDefinition cube_r27 = shoulder4.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(128, 47).addBox(-1.8528F, -0.4005F, -4.3546F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 1.0F, 0.0F, 0.2182F, 0.0F, -1.0472F));

        PartDefinition cube_r28 = shoulder4.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(104, 37).addBox(-1.8528F, -0.3617F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-6.0F, 1.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition cube_r29 = shoulder4.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(130, 11).addBox(-1.8528F, -0.4005F, 0.3546F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 1.0F, 0.0F, -0.2182F, 0.0F, -1.0472F));

        PartDefinition cube_r30 = shoulder4.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(109, 126).addBox(-1.0681F, -1.4824F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

        PartDefinition cube_r31 = shoulder4.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(132, 93).addBox(-1.0681F, -1.4946F, -4.112F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, 0.2182F, 0.0F, -0.3491F));

        PartDefinition cube_r32 = shoulder4.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(133, 18).addBox(-1.0681F, -1.4946F, 0.112F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -1.0F, 0.0F, -0.2182F, 0.0F, -0.3491F));

        PartDefinition sword = rightArm.addOrReplaceChild("sword", CubeListBuilder.create(), PartPose.offsetAndRotation(-4.0F, 20.0F, 0.0F, 3.1416F, 0.0F, 0.0F));

        PartDefinition blade = sword.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.5F, -9.0F, 7.0F));

        PartDefinition cube_r33 = blade.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(226, 2).addBox(-0.5F, -18.0F, 10.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(242, 22).addBox(-0.5F, -19.0F, 19.0F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -21.0F, 21.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(191, 7).addBox(-0.5F, -21.0F, 11.0F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(225, 1).addBox(-0.5F, -22.0F, 13.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -18.0F, 18.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(227, 3).addBox(-0.5F, -19.0F, 12.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -17.0F, 17.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -16.0F, 16.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -17.0F, 9.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -15.0F, 15.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -16.0F, 8.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -14.0F, 14.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -15.0F, 7.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -13.0F, 13.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -14.0F, 6.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -12.0F, 12.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -13.0F, 5.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -7.0F, 7.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -8.0F, 0.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -8.0F, 8.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -9.0F, 1.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -9.0F, 9.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -10.0F, 2.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -11.0F, 3.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -10.0F, 10.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(251, 22).addBox(-0.5F, -11.0F, 11.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(226, 2).addBox(-0.5F, -12.0F, 4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(209, 10).addBox(-0.5F, -7.0F, -4.0F, 1.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle = sword.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.5F, -9.0F, 7.0F));

        PartDefinition cube_r34 = handle.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(212, 16).addBox(-1.0F, -6.0F, -9.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(222, 11).addBox(-1.0F, -10.0F, -8.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(238, 15).addBox(-1.0F, -4.0F, -8.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(238, 15).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 0).addBox(-1.0F, 1.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(246, 16).addBox(-1.0F, 6.0F, 1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(220, 0).addBox(-1.0F, 6.0F, 5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.01F))
                .texOffs(203, 10).addBox(-1.0F, 7.0F, 3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(246, 16).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(234, 21).addBox(-1.0F, -8.0F, -6.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(234, 21).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(238, 1).addBox(-2.0F, -3.5F, -3.75F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(244, 9).addBox(-2.0F, -1.5F, 1.25F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(203, 0).addBox(-2.0F, -1.5F, -3.75F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(234, 21).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(230, 11).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(230, 11).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 8).addBox(-1.0F, 4.0F, 4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 19).addBox(-1.0F, 4.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 0).addBox(-1.0F, 3.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.001F))
                .texOffs(197, 4).addBox(-0.5F, 5.0F, -9.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 24).addBox(-0.5F, 3.0F, -7.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(192, 4).addBox(-0.5F, 7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(192, 0).addBox(-1.0F, 7.0F, -9.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(9.5F, -8.0F, -2.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition arm1_r1 = leftArm.addOrReplaceChild("arm1_r1", CubeListBuilder.create().texOffs(0, 99).addBox(9.1743F, -34.9924F, -3.0F, 5.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.9725F, 30.8896F, 2.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition arm1_r2 = leftArm.addOrReplaceChild("arm1_r2", CubeListBuilder.create().texOffs(0, 117).addBox(-2.8298F, -9.9924F, 0.0377F, 5.0F, 10.0F, 3.0F, new CubeDeformation(0.15F)), PartPose.offsetAndRotation(3.1516F, 8.9237F, 2.0F, 0.0F, 0.2182F, -0.0873F));

        PartDefinition arm1_r3 = leftArm.addOrReplaceChild("arm1_r3", CubeListBuilder.create().texOffs(22, 64).addBox(-2.5176F, -5.9319F, -5.0F, 8.0F, 5.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4471F, 2.2118F, 2.0F, 0.0F, 0.0F, 0.2618F));

        PartDefinition spikes2 = leftArm.addOrReplaceChild("spikes2", CubeListBuilder.create().texOffs(98, 10).addBox(-0.5F, -5.9924F, -1.6743F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F))
                .texOffs(93, 0).addBox(-0.5F, -8.9924F, -1.6743F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(94, 62).addBox(-1.5F, -10.9924F, -1.1743F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.352F, -0.3907F, 2.0F, 0.0F, -1.5708F, -0.0873F));

        PartDefinition head_r3 = spikes2.addOrReplaceChild("head_r3", CubeListBuilder.create().texOffs(10, 52).addBox(-1.9132F, -10.8057F, -0.8021F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(10, 60).addBox(-2.9132F, -7.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(72, 39).addBox(-0.9132F, -10.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(83, 52).addBox(-0.4132F, -5.8057F, -1.3021F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, -0.4363F));

        PartDefinition head_r4 = spikes2.addOrReplaceChild("head_r4", CubeListBuilder.create().texOffs(12, 52).addBox(0.9132F, -10.8057F, -0.8021F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(16, 72).addBox(1.9132F, -7.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(75, 0).addBox(-0.0868F, -10.8057F, -1.3021F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(48, 64).addBox(-0.5868F, -5.8057F, -1.3021F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.4363F, 0.4363F));

        PartDefinition leftArm1 = leftArm.addOrReplaceChild("leftArm1", CubeListBuilder.create().texOffs(16, 114).addBox(-2.8257F, -1.9924F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.1F))
                .texOffs(106, 41).mirror().addBox(-1.8257F, -3.9924F, -3.0F, 5.0F, 8.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(3.1516F, 8.9237F, 2.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition left_fingers = leftArm1.addOrReplaceChild("left_fingers", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

        PartDefinition arm0_r5 = left_fingers.addOrReplaceChild("arm0_r5", CubeListBuilder.create().texOffs(81, 14).addBox(-5.0076F, -4.0F, -3.8257F, 8.0F, 8.0F, 7.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, -1.5708F));

        PartDefinition shoulder2 = leftArm.addOrReplaceChild("shoulder2", CubeListBuilder.create(), PartPose.offsetAndRotation(1.1095F, -2.9435F, 2.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition cube_r35 = shoulder2.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(12, 128).addBox(-3.6383F, -3.12F, -0.2483F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, -0.2182F, 0.0F, 1.0472F));

        PartDefinition cube_r36 = shoulder2.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(104, 15).addBox(-3.6383F, -3.1472F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition cube_r37 = shoulder2.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(30, 128).addBox(-3.6383F, -3.12F, -3.7517F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, 0.2182F, 0.0F, 1.0472F));

        PartDefinition shoulder3 = shoulder2.addOrReplaceChild("shoulder3", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r38 = shoulder3.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(32, 114).addBox(-3.9319F, -1.4824F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

        PartDefinition cube_r39 = shoulder3.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(127, 0).addBox(-3.1472F, -0.4005F, 0.3546F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, -0.2182F, 0.0F, 1.0472F));

        PartDefinition cube_r40 = shoulder3.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(18, 62).addBox(-3.1472F, -0.3617F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition cube_r41 = shoulder3.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(126, 86).addBox(-3.1472F, -0.4005F, -4.3546F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -2.0F, 0.0F, 0.2182F, 0.0F, 1.0472F));

        PartDefinition cube_r42 = shoulder3.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(95, 126).addBox(-3.9319F, -1.4946F, 0.112F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.2182F, 0.0F, 0.3491F));

        PartDefinition cube_r43 = shoulder3.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(126, 111).addBox(-3.9319F, -1.4946F, -4.112F, 5.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.2182F, 0.0F, 0.3491F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(66, 134).addBox(-4.5F, 7.0F, -5.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(-3.0F, 4.0F, 0.0F));

        PartDefinition cube_r44 = rightLeg.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(48, 64).addBox(-2.35F, -1.0F, -3.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -0.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition cube_r45 = rightLeg.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(16, 236).addBox(-2.35F, 3.0F, -2.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 2.0F, -0.5F, 0.0F, 0.0F, 0.5236F));

        PartDefinition leg0_r1 = rightLeg.addOrReplaceChild("leg0_r1", CubeListBuilder.create().texOffs(119, 72).mirror().addBox(-2.5F, 0.0F, -3.0F, 4.0F, 9.0F, 5.0F, new CubeDeformation(0.5F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.2618F));

        PartDefinition rightLeg1 = rightLeg.addOrReplaceChild("rightLeg1", CubeListBuilder.create().texOffs(75, 0).mirror().addBox(-2.5F, 5.0F, -4.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false)
                .texOffs(33, 135).mirror().addBox(-2.0F, 7.0F, -6.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, 10.0F, 0.0F));

        PartDefinition leg0_r2 = rightLeg1.addOrReplaceChild("leg0_r2", CubeListBuilder.create().texOffs(104, 110).addBox(-5.5F, -4.0F, -4.0F, 6.0F, 11.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(3.0F, 2.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(66, 134).addBox(-1.5F, 7.0F, -5.0F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.01F)), PartPose.offset(3.0F, 4.0F, 0.0F));

        PartDefinition leg1_r1 = leftLeg.addOrReplaceChild("leg1_r1", CubeListBuilder.create().texOffs(119, 72).addBox(-1.5F, 0.0F, -3.0F, 4.0F, 9.0F, 5.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.1309F, 0.0F, -0.2618F));

        PartDefinition cube_r46 = leftLeg.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(18, 52).addBox(-2.65F, -1.0F, -3.5F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.0F, 1.0F, -0.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition cube_r47 = leftLeg.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(0, 236).addBox(-2.65F, 3.0F, -2.0F, 5.0F, 4.0F, 3.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.0F, 2.0F, -0.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition leftLeg1 = leftLeg.addOrReplaceChild("leftLeg1", CubeListBuilder.create().texOffs(75, 0).addBox(-3.5F, 5.0F, -4.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.25F))
                .texOffs(33, 135).addBox(-3.0F, 7.0F, -6.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 10.0F, 0.0F));

        PartDefinition leg1_r2 = leftLeg1.addOrReplaceChild("leg1_r2", CubeListBuilder.create().texOffs(82, 110).addBox(-0.5F, -4.0F, -4.0F, 6.0F, 11.0F, 5.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-3.0F, 2.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.Head.xRot = headPitch / (180F / (float) Math.PI);
        this.animateWalk(JohannesFountainAnimations.WALK,limbSwing,limbSwingAmount,2.0F, 2.5F);
        this.animate(entity.attackFountain,JohannesFountainAnimations.ATTACK,ageInTicks,0.9f);
        this.animate(entity.dashFountain,JohannesFountainAnimations.DASH,ageInTicks,0.5f);
        this.animate(entity.daggerAttackFountain,JohannesFountainAnimations.DAGGER_ATTACK,ageInTicks);
        this.animate(entity.deathFountain,JohannesFountainAnimations.DEATH,ageInTicks,0.5f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}