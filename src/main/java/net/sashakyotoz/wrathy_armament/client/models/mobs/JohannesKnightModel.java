package net.sashakyotoz.wrathy_armament.client.models.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.animations.JohannesKnightAnimations;
import net.sashakyotoz.wrathy_armament.entities.bosses.JohannesKnight;

public class JohannesKnightModel <T extends JohannesKnight> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("johannes_knight_model"), "main");
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;
    private final ModelPart root;

    public JohannesKnightModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.Head = root.getChild("Head");
        this.Body = root.getChild("Body");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.RightLeg = root.getChild("RightLeg");
        this.LeftLeg = root.getChild("LeftLeg");
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition HatLayer_r1 = Head.addOrReplaceChild("HatLayer_r1", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-4.0F, -5.0F, 0.0F, 4.0F, 8.0F, 1.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, -3.0F, -5.0F, 0.0F, 0.1745F, 0.0F));

        PartDefinition HatLayer_r2 = Head.addOrReplaceChild("HatLayer_r2", CubeListBuilder.create().texOffs(0, 48).addBox(0.0F, -5.0F, 0.0F, 4.0F, 8.0F, 1.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -3.0F, -5.0F, 0.0F, -0.1745F, 0.0F));

        PartDefinition HatLayer_r3 = Head.addOrReplaceChild("HatLayer_r3", CubeListBuilder.create().texOffs(38, 48).addBox(-3.0F, -11.5F, -0.75F, 5.0F, 3.0F, 8.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(0.5F, -7.0F, -3.5F, -1.5708F, 0.0F, 0.0F));

        PartDefinition HatLayer_r4 = Head.addOrReplaceChild("HatLayer_r4", CubeListBuilder.create().texOffs(39, 49).addBox(-3.0F, -8.0F, 3.0F, 5.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -7.0F, -3.5F, -0.6109F, 0.0F, 0.0F));

        PartDefinition HatLayer_r5 = Head.addOrReplaceChild("HatLayer_r5", CubeListBuilder.create().texOffs(38, 48).addBox(-3.0F, -2.5F, 0.0F, 5.0F, 3.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -7.5F, -3.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cape = Body.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = cape.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(106, 47).addBox(-5.0F, 0.0F, 0.0F, 10.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 2.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.26F))
                .texOffs(40, 32).mirror().addBox(-3.5F, -2.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)).mirror(false), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition sword = RightArm.addOrReplaceChild("sword", CubeListBuilder.create(), PartPose.offset(-1.0F, 10.0F, 0.0F));

        PartDefinition blade = sword.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, -14.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(98, 2).addBox(-0.5F, -18.0F, 10.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(114, 22).addBox(-0.5F, -19.0F, 19.0F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -21.0F, 21.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(63, 7).addBox(-0.5F, -21.0F, 11.0F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(97, 1).addBox(-0.5F, -22.0F, 13.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -18.0F, 18.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(99, 3).addBox(-0.5F, -19.0F, 12.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -17.0F, 17.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -16.0F, 16.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -17.0F, 9.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -15.0F, 15.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -16.0F, 8.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -14.0F, 14.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -15.0F, 7.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -13.0F, 13.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -14.0F, 6.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -12.0F, 12.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -13.0F, 5.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -7.0F, 7.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -8.0F, 0.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -8.0F, 8.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -9.0F, 1.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -9.0F, 9.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -10.0F, 2.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -11.0F, 3.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -10.0F, 10.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(123, 22).addBox(-0.5F, -11.0F, 11.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(98, 2).addBox(-0.5F, -12.0F, 4.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(81, 10).addBox(-0.5F, -7.0F, -4.0F, 1.0F, 11.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition handle = sword.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 1.0F, -14.5F, 1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r3 = handle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(84, 16).addBox(-1.0F, -6.0F, -9.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(94, 11).addBox(-1.0F, -10.0F, -8.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(110, 15).addBox(-1.0F, -4.0F, -8.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.01F))
                .texOffs(110, 15).addBox(-1.0F, -2.0F, -7.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 0).addBox(-1.0F, 1.0F, -5.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(118, 16).addBox(-1.0F, 6.0F, 1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(92, 0).addBox(-1.0F, 6.0F, 5.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.01F))
                .texOffs(75, 10).addBox(-1.0F, 7.0F, 3.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(118, 16).addBox(-1.0F, 5.0F, -1.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(106, 21).addBox(-1.0F, -8.0F, -6.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(106, 21).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(110, 1).addBox(-2.0F, -3.5F, -3.75F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(116, 9).addBox(-2.0F, -1.5F, 1.25F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(75, 0).addBox(-2.0F, -1.5F, -3.75F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(106, 21).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(102, 11).addBox(-1.0F, -2.0F, 0.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(102, 11).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 8).addBox(-1.0F, 4.0F, 4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 19).addBox(-1.0F, 4.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 0).addBox(-1.0F, 3.0F, -3.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.001F))
                .texOffs(69, 4).addBox(-0.5F, 5.0F, -9.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 24).addBox(-0.5F, 3.0F, -7.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(64, 4).addBox(-0.5F, 7.0F, -7.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(64, 0).addBox(-1.0F, 7.0F, -9.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(40, 32).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.26F)).mirror(false)
                .texOffs(40, 32).addBox(-0.5F, -2.0F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.45F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition rightLap = RightLeg.addOrReplaceChild("rightLap", CubeListBuilder.create().texOffs(0, 38).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.26F))
                .texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 32).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition leftLap = LeftLeg.addOrReplaceChild("leftLap", CubeListBuilder.create().texOffs(0, 38).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.26F)).mirror(false)
                .texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.Head.xRot = headPitch / (180F / (float) Math.PI);
        this.animateWalk(JohannesKnightAnimations.WALK,limbSwing,limbSwingAmount,2.0F, 2.5F);
        this.animate(entity.attackKnight,JohannesKnightAnimations.ATTACK,ageInTicks);
        this.animate(entity.jumpKnight,JohannesKnightAnimations.JUMP,ageInTicks);
        this.animate(entity.deathKnight,JohannesKnightAnimations.DEATH,ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
