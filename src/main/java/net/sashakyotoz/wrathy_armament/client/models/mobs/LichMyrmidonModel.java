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
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.animations.LichMyrmidonAnimations;

public class LichMyrmidonModel <T extends LichMyrmidon> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("lich_myrmidon_model"), "main");
    private final ModelPart Head;
    private final ModelPart Body;
    private final ModelPart RightArm;
    private final ModelPart LeftArm;
    private final ModelPart root;

    public LichMyrmidonModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.Head = root.getChild("Head");
        this.Body = root.getChild("Body");
        this.RightArm = root.getChild("RightArm");
        this.LeftArm = root.getChild("LeftArm");
        this.root = root;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(52, 60).addBox(-0.5F, -11.0F, -2.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F))
                .texOffs(52, 60).addBox(-0.5F, -14.0F, -2.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(58, 48).addBox(-1.5F, -16.0F, -2.0F, 3.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_r1 = Head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(62, 56).addBox(-2.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(56, 58).addBox(-3.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 58).addBox(-1.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 60).addBox(-1.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, -7.0F, -1.0F, 0.0F, 0.4363F, -0.4363F));

        PartDefinition head_r2 = Head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(60, 56).addBox(1.75F, -9.0F, -1.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(56, 58).addBox(2.75F, -6.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 58).addBox(0.75F, -9.0F, -1.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 60).addBox(0.25F, -4.0F, -1.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(0.0F, -7.0F, -1.0F, 0.0F, -0.4363F, 0.4363F));

        PartDefinition HatLayer_r1 = Head.addOrReplaceChild("HatLayer_r1", CubeListBuilder.create().texOffs(32, 0).mirror().addBox(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offsetAndRotation(0.0F, -6.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 11.0F, 4.0F, new CubeDeformation(0.3F))
                .texOffs(0, 52).mirror().addBox(-4.0F, 0.0F, -2.15F, 8.0F, 7.0F, 5.0F, new CubeDeformation(0.1F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition cube_r1 = Body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-4.0F, -6.0F, -1.9F, 8.0F, 7.0F, 5.0F, new CubeDeformation(0.2F)).mirror(false), PartPose.offsetAndRotation(0.0F, 11.0F, 0.0F, 0.0436F, 0.0F, 0.0F));

        PartDefinition tail = Body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 11.5F, -0.25F, -0.0873F, 0.0F, 0.0F));

        PartDefinition cube_r2 = tail.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -1.0F, -1.45F, 8.0F, 11.0F, 5.0F, new CubeDeformation(0.26F))
                .texOffs(0, 48).mirror().addBox(-4.0F, -1.0F, -1.75F, 8.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 40).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.26F))
                .texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F))
                .texOffs(40, 32).addBox(-3.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F))
                .texOffs(44, 28).addBox(-1.0F, 6.0F, 2.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 28).addBox(-1.0F, 2.0F, 1.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, 0.0F, -1.309F, 0.2618F, 0.2618F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 40).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.26F)).mirror(false)
                .texOffs(40, 32).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)).mirror(false)
                .texOffs(40, 32).mirror().addBox(-1.0F, 6.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.5F)).mirror(false)
                .texOffs(44, 28).mirror().addBox(1.0F, 6.0F, 1.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(44, 28).mirror().addBox(1.0F, 2.0F, 0.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.0F, 2.0F, 0.0F, -1.309F, -0.2618F, -0.2618F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.Head.xRot = headPitch / (180F / (float) Math.PI);
        this.animate(entity.attack, LichMyrmidonAnimations.ATTACK,ageInTicks);
        this.animate(entity.spawn, LichMyrmidonAnimations.SPAWN,ageInTicks);
        this.animate(entity.despawn, LichMyrmidonAnimations.DESPAWN,ageInTicks);
        this.animateWalk(LichMyrmidonAnimations.FLY,limbSwing,limbSwingAmount,2.0F, 2.5F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}