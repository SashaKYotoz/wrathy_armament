package net.sashakyotoz.wrathy_armament.client.models.mobs;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.animations.MoonLordAnimations;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

import java.util.List;

public class MoonLordModel<T extends MoonLord> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("moon_lord_model"), "main");
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart tencle3;
    private final ModelPart tencle2;
    private final ModelPart tencle1;
    private final ModelPart tencle;
    private final ModelPart tencle4;
    private final ModelPart tencle5;
    private final ModelPart backTencles;
    private final ModelPart tencle6;
    private final ModelPart tencle7;
    private final ModelPart body;
    private final ModelPart heartKeeper;
    private final ModelPart bones;
    private final ModelPart rightArm;
    private final ModelPart rightForearm;
    private final ModelPart rightHand;
    private final ModelPart finger3;
    private final ModelPart finger2;
    private final ModelPart finger1;
    private final ModelPart thumb;
    private final ModelPart finger;
    private final ModelPart leftArm;
    private final ModelPart leftForearm;
    private final ModelPart leftHand;
    private final ModelPart finger7;
    private final ModelPart finger6;
    private final ModelPart finger5;
    private final ModelPart thumb2;
    private final ModelPart finger4;

    public MoonLordModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.head = root.getChild("head");
        this.tencle3 = head.getChild("tencle3");
        this.tencle2 = head.getChild("tencle2");
        this.tencle1 = head.getChild("tencle1");
        this.tencle = head.getChild("tencle");
        this.tencle4 = head.getChild("tencle4");
        this.tencle5 = head.getChild("tencle5");
        this.backTencles = head.getChild("backTencles");
        this.tencle6 = backTencles.getChild("tencle6");
        this.tencle7 = backTencles.getChild("tencle7");
        this.body = root.getChild("body");
        this.heartKeeper = body.getChild("heartKeeper");
        this.bones = body.getChild("bones");
        this.rightArm = root.getChild("rightArm");
        this.rightForearm = rightArm.getChild("rightForearm");
        this.rightHand = rightForearm.getChild("rightHand");
        this.finger3 = rightHand.getChild("finger3");
        this.finger2 = rightHand.getChild("finger2");
        this.finger1 = rightHand.getChild("finger1");
        this.thumb = rightHand.getChild("thumb");
        this.finger = rightHand.getChild("finger");
        this.leftArm = root.getChild("leftArm");
        this.leftForearm = leftArm.getChild("leftForearm");
        this.leftHand = leftForearm.getChild("leftHand");
        this.finger7 = leftHand.getChild("finger7");
        this.finger6 = leftHand.getChild("finger6");
        this.finger5 = leftHand.getChild("finger5");
        this.thumb2 = leftHand.getChild("thumb2");
        this.finger4 = leftHand.getChild("finger4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(80, 78).mirror().addBox(-6.5F, -13.9F, -6.4433F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 27).mirror().addBox(-2.5F, 0.5F, -6.4433F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 0).mirror().addBox(-4.5F, -15.9F, -5.4433F, 8.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(8, 39).mirror().addBox(-1.5F, 0.0F, -5.9433F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 49).mirror().addBox(-2.0F, 4.5F, -5.9433F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 49).mirror().addBox(-1.0F, 4.0F, -6.2433F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.5F, -4.5F, -2.8F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(8, 39).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -10.5F, 0.5567F, 0.0421F, 0.1036F, 0.5525F));

        PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(8, 39).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -7.5F, -0.4433F, 0.0421F, 0.1036F, 0.5525F));

        PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(16, 34).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -3.5F, -0.9433F, 0.0421F, 0.1036F, 0.5525F));

        PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(40, 12).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -14.6F, -2.4433F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(40, 12).mirror().addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -14.6F, -2.4433F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(8, 39).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.0F, -10.5F, 0.5567F, 0.0421F, -0.1036F, -0.5525F));

        PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(8, 39).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.5F, -7.5F, -0.4433F, 0.0421F, -0.1036F, -0.5525F));

        PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(16, 34).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -3.5F, -0.9433F, 0.0421F, -0.1036F, -0.5525F));

        PartDefinition tencle3 = head.addOrReplaceChild("tencle3", CubeListBuilder.create().texOffs(0, 35).addBox(-0.4F, 1.4F, -1.6F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -2.9F, -6.9433F));

        PartDefinition cube_r9 = tencle3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(8, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3414F, -0.0741F, -0.2054F));

        PartDefinition tencle2 = head.addOrReplaceChild("tencle2", CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-1.6F, 1.4F, -1.6F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-2.0F, -2.9F, -6.9433F));

        PartDefinition cube_r10 = tencle2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(8, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3414F, 0.0741F, 0.2054F));

        PartDefinition tencle1 = head.addOrReplaceChild("tencle1", CubeListBuilder.create().texOffs(0, 35).addBox(-1.4F, 1.2F, -1.6F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.7F, -3.9F, -6.9433F));

        PartDefinition cube_r11 = tencle1.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(8, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3899F, 0.0447F, 0.1231F));

        PartDefinition tencle = head.addOrReplaceChild("tencle", CubeListBuilder.create().texOffs(0, 35).addBox(-3.0F, 2.0F, -2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.8F, -3.7F, -4.3433F));

        PartDefinition cube_r12 = tencle.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(8, 43).addBox(-1.5582F, -0.7147F, -0.8089F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, -0.2F, -0.6F, -0.1948F, 0.1093F, 0.3771F));

        PartDefinition tencle4 = head.addOrReplaceChild("tencle4", CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-0.6F, 1.2F, -1.6F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(4.7F, -3.9F, -6.9433F));

        PartDefinition cube_r13 = tencle4.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(8, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3899F, -0.0447F, -0.1231F));

        PartDefinition tencle5 = head.addOrReplaceChild("tencle5", CubeListBuilder.create().texOffs(0, 35).addBox(-0.2F, 1.2F, -1.4F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -2.9F, -4.9433F));

        PartDefinition cube_r14 = tencle5.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(8, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1948F, -0.1093F, -0.3771F));

        PartDefinition backTencles = head.addOrReplaceChild("backTencles", CubeListBuilder.create(), PartPose.offset(-5.7F, -9.9F, 5.5433F));

        PartDefinition tencle6 = backTencles.addOrReplaceChild("tencle6", CubeListBuilder.create().texOffs(0, 35).addBox(-1.4F, 1.2F, -0.4F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r15 = tencle6.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(8, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3899F, -0.0447F, 0.1231F));

        PartDefinition tencle7 = backTencles.addOrReplaceChild("tencle7", CubeListBuilder.create().texOffs(0, 35).mirror().addBox(-0.6F, 1.2F, -0.4F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(10.4F, 0.0F, 0.0F));

        PartDefinition cube_r16 = tencle7.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(8, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3899F, 0.0447F, -0.1231F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 74).mirror().addBox(-14.5F, -7.0F, -8.2433F, 14.0F, 15.0F, 12.0F, new CubeDeformation(0.025F)).mirror(false)
                .texOffs(48, 40).mirror().addBox(-11.0F, 7.0F, 3.25F, 7.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 104).mirror().addBox(-10.5F, -7.75F, -3.7433F, 6.0F, 12.0F, 12.0F, new CubeDeformation(0.4F)).mirror(false)
                .texOffs(16, 0).mirror().addBox(-10.0F, -13.0F, -5.7433F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.5F, 2.0F, 0.0F));

        PartDefinition cube_r17 = body.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(10, 28).mirror().addBox(0.97F, -0.5402F, 0.0F, 14.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, -6.0F, 4.7567F, -0.7854F, -0.2618F, 0.0873F));

        PartDefinition cube_r18 = body.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(72, 2).addBox(-7.0F, 0.0F, -7.0F, 12.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2F, 2.5F, 0.7567F, 0.0F, 0.0F, 0.0698F));

        PartDefinition cube_r19 = body.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(72, 2).mirror().addBox(-5.0F, 0.0F, -7.0F, 12.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.8F, 2.5F, 0.7567F, 0.0F, 0.0F, -0.0698F));

        PartDefinition cube_r20 = body.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(68, 0).mirror().addBox(-7.0F, -7.0F, -8.0F, 14.0F, 14.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.0F, 0.7567F, 0.0F, 0.0F, 0.0698F));

        PartDefinition cube_r21 = body.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(10, 28).mirror().addBox(-14.97F, -0.5402F, 0.0F, 14.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, 0.0F, 4.7567F, -0.7854F, 0.2618F, -0.0873F));

        PartDefinition cube_r22 = body.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(10, 28).mirror().addBox(0.97F, -0.5402F, 0.0F, 14.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, 0.0F, 4.7567F, -0.7854F, -0.2618F, 0.0873F));

        PartDefinition cube_r23 = body.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(10, 28).mirror().addBox(-14.97F, -0.5402F, 0.0F, 14.0F, 0.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.5F, -6.0F, 4.7567F, -0.7854F, 0.2618F, -0.0873F));

        PartDefinition cube_r24 = body.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(68, 0).addBox(-7.0F, -7.0F, -8.0F, 14.0F, 14.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -1.0F, 0.7567F, 0.0F, 0.0F, -0.0698F));

        PartDefinition heartKeeper = body.addOrReplaceChild("heartKeeper", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-7.0F, -5.0F, -6.0F, 14.0F, 13.0F, 12.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(-7.5F, 0.0F, -2.2433F));

        PartDefinition bones = body.addOrReplaceChild("bones", CubeListBuilder.create().texOffs(69, 30).mirror().addBox(-8.0F, -1.75F, -5.75F, 16.0F, 12.0F, 12.0F, new CubeDeformation(0.975F)).mirror(false)
                .texOffs(48, 44).mirror().addBox(-3.5F, 8.0F, 2.4933F, 7.0F, 9.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(69, 30).mirror().addBox(-8.0F, 8.0F, -5.75F, 16.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-7.5F, 6.0F, 0.2567F));

        PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create(), PartPose.offset(15.3321F, 0.767F, 1.7558F));

        PartDefinition cube_r25 = rightArm.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(84, 102).addBox(-7.0661F, -12.1532F, -3.1635F, 12.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0678F, 3.6605F, -4.8356F, -0.2705F, -0.1396F, -0.4608F));

        PartDefinition rightForearm = rightArm.addOrReplaceChild("rightForearm", CubeListBuilder.create(), PartPose.offset(2.0F, 6.0F, -16.9933F));

        PartDefinition cube_r26 = rightForearm.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(16, 0).addBox(-3.0839F, -3.5403F, -0.9161F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.1674F, 0.964F, 12.6161F, -1.9744F, -0.1393F, -0.4608F));

        PartDefinition cube_r27 = rightForearm.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(16, 14).mirror().addBox(-2.9161F, -6.6495F, -1.1635F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.1674F, 0.964F, 11.1161F, -0.2727F, -0.1393F, -0.4608F));

        PartDefinition rightHand = rightForearm.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(0, 19).mirror().addBox(0.3836F, -6.4763F, -12.9F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.7F, 4.9F, 12.1F));

        PartDefinition cube_r28 = rightHand.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 1).addBox(-2.0839F, -2.5403F, 0.0839F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.4674F, -2.936F, -0.9839F, -1.5817F, -0.1393F, -0.4608F));

        PartDefinition finger3 = rightHand.addOrReplaceChild("finger3", CubeListBuilder.create(), PartPose.offset(5.6571F, -5.242F, -11.5635F));

        PartDefinition cube_r29 = finger3.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, -2.0F, 0.0F, 0.2954F, 0.1925F, 0.3444F));

        PartDefinition cube_r30 = finger3.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(8, 35).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.7736F, -1.1894F, 0.2635F, -0.2718F, 0.1925F, 0.3444F));

        PartDefinition finger2 = rightHand.addOrReplaceChild("finger2", CubeListBuilder.create(), PartPose.offset(4.8307F, -6.4314F, -11.7999F));

        PartDefinition cube_r31 = finger2.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.4429F, 0.0869F, 0.1515F));

        PartDefinition cube_r32 = finger2.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, -1.0F, 0.5F, -0.1679F, 0.0869F, 0.1515F));

        PartDefinition finger1 = rightHand.addOrReplaceChild("finger1", CubeListBuilder.create(), PartPose.offset(2.4307F, -5.9314F, -11.2999F));

        PartDefinition cube_r33 = finger1.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r34 = finger1.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition thumb = rightHand.addOrReplaceChild("thumb", CubeListBuilder.create(), PartPose.offset(0.3F, -1.9F, -12.1F));

        PartDefinition cube_r35 = thumb.addOrReplaceChild("cube_r35", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.0F, -3.0F, 0.0F, 0.604F, -0.1586F, -0.6731F));

        PartDefinition cube_r36 = thumb.addOrReplaceChild("cube_r36", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.2F, -0.6F, 0.8F, -0.1377F, -0.1586F, -0.6731F));

        PartDefinition finger = rightHand.addOrReplaceChild("finger", CubeListBuilder.create(), PartPose.offset(1.0F, -6.0F, -12.0F));

        PartDefinition cube_r37 = finger.addOrReplaceChild("cube_r37", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -4.0F, 0.0F, 0.4363F, 0.0F, -0.2182F));

        PartDefinition cube_r38 = finger.addOrReplaceChild("cube_r38", CubeListBuilder.create().texOffs(0, 43).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, -1.5F, 0.7F, -0.2182F, 0.0F, -0.2182F));

        PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create(), PartPose.offset(-14.9628F, 0.2356F, 1.8058F));

        PartDefinition cube_r39 = leftArm.addOrReplaceChild("cube_r39", CubeListBuilder.create().texOffs(84, 102).addBox(-4.9339F, -12.1532F, -3.1635F, 12.0F, 10.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.4371F, 4.1919F, -4.8856F, -0.2705F, 0.1396F, 0.4608F));

        PartDefinition leftForearm = leftArm.addOrReplaceChild("leftForearm", CubeListBuilder.create(), PartPose.offset(-4.0693F, 1.4314F, -16.9434F));

        PartDefinition cube_r40 = leftForearm.addOrReplaceChild("cube_r40", CubeListBuilder.create().texOffs(16, 0).addBox(-2.9161F, -3.5403F, -0.9161F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4674F, 6.064F, 12.5161F, -1.9744F, 0.1393F, 0.4608F));

        PartDefinition cube_r41 = leftForearm.addOrReplaceChild("cube_r41", CubeListBuilder.create().texOffs(16, 14).addBox(-3.0839F, -6.6495F, -1.1635F, 6.0F, 8.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4674F, 6.064F, 11.0161F, -0.2727F, 0.1393F, 0.4608F));

        PartDefinition leftHand = leftForearm.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(0, 19).addBox(-6.2836F, -4.9763F, -11.6F, 6.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.1F, 8.5F, 10.7F));

        PartDefinition cube_r42 = leftHand.addOrReplaceChild("cube_r42", CubeListBuilder.create().texOffs(0, 1).addBox(-1.9161F, -2.5403F, 0.0839F, 4.0F, 13.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.3674F, -1.436F, 0.3161F, -1.5817F, 0.1393F, 0.4608F));

        PartDefinition finger7 = leftHand.addOrReplaceChild("finger7", CubeListBuilder.create(), PartPose.offset(-5.5571F, -3.742F, -10.2635F));

        PartDefinition cube_r43 = finger7.addOrReplaceChild("cube_r43", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -2.0F, 0.0F, 0.2954F, -0.1925F, -0.3444F));

        PartDefinition cube_r44 = finger7.addOrReplaceChild("cube_r44", CubeListBuilder.create().texOffs(8, 35).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7736F, -1.1894F, 0.2635F, -0.2718F, -0.1925F, -0.3444F));

        PartDefinition finger6 = leftHand.addOrReplaceChild("finger6", CubeListBuilder.create(), PartPose.offset(-4.7307F, -4.9314F, -10.4999F));

        PartDefinition cube_r45 = finger6.addOrReplaceChild("cube_r45", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, 0.4429F, -0.0869F, -0.1515F));

        PartDefinition cube_r46 = finger6.addOrReplaceChild("cube_r46", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, -1.0F, 0.5F, -0.1679F, -0.0869F, -0.1515F));

        PartDefinition finger5 = leftHand.addOrReplaceChild("finger5", CubeListBuilder.create(), PartPose.offset(-2.3307F, -4.4314F, -9.9999F));

        PartDefinition cube_r47 = finger5.addOrReplaceChild("cube_r47", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r48 = finger5.addOrReplaceChild("cube_r48", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.5F, 0.0F, -0.3054F, 0.0F, 0.0F));

        PartDefinition finger4 = leftHand.addOrReplaceChild("finger4", CubeListBuilder.create(), PartPose.offset(-0.9F, -4.5F, -10.7F));

        PartDefinition cube_r49 = finger4.addOrReplaceChild("cube_r49", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, -4.0F, 0.0F, 0.4363F, 0.0F, 0.2182F));

        PartDefinition cube_r50 = finger4.addOrReplaceChild("cube_r50", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, -1.5F, 0.7F, -0.2182F, 0.0F, 0.2182F));

        PartDefinition thumb2 = leftHand.addOrReplaceChild("thumb2", CubeListBuilder.create(), PartPose.offset(-0.2F, -0.4F, -10.8F));

        PartDefinition cube_r51 = thumb2.addOrReplaceChild("cube_r51", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -3.0F, 0.0F, 0.604F, 0.1586F, 0.6731F));

        PartDefinition cube_r52 = thumb2.addOrReplaceChild("cube_r52", CubeListBuilder.create().texOffs(0, 43).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2F, -0.6F, 0.8F, -0.1377F, 0.1586F, 0.6731F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(MoonLord pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = pNetHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = pHeadPitch / (180F / (float) Math.PI);
        this.animate(pEntity.death, MoonLordAnimations.DEATH,pAgeInTicks,0.5f);
        this.animate(pEntity.meleeAttack, MoonLordAnimations.MELEEATTACK,pAgeInTicks);
        this.animate(pEntity.eyeAttack, MoonLordAnimations.EYEATTACK,pAgeInTicks);
        this.animate(pEntity.interactive,MoonLordAnimations.INTERACTIVE,pAgeInTicks,pEntity.getAnimationScaling());
    }

    public List<ModelPart> getHeartLayerModelParts() {
        return ImmutableList.of(this.body);
    }
}