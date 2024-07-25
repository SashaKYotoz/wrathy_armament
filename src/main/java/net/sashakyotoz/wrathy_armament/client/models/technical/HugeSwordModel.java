package net.sashakyotoz.wrathy_armament.client.models.technical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;

public class HugeSwordModel <T extends HarmfulProjectileEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("huge_sword_model"), "main");
    private final ModelPart handle;
    private final ModelPart blade;

    public HugeSwordModel(ModelPart root) {
        this.handle = root.getChild("handle");
        this.blade = root.getChild("blade");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(6, 57).addBox(-1.0F, -5.0F, 0.0F, 2.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 59).addBox(-1.0F, -5.0F, 1.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 41).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(44, 41).addBox(-1.0F, -14.0F, 5.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(16, 0).addBox(-1.0F, -12.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(8, 0).addBox(-1.0F, -12.0F, 7.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-1.0F, -15.0F, 4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 35).addBox(-1.0F, -10.0F, 1.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.01F))
                .texOffs(54, 41).addBox(-1.0F, -9.0F, 0.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(54, 35).addBox(-1.0F, -11.0F, 2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(-0.01F))
                .texOffs(48, 0).addBox(-1.0F, -7.0F, -7.0F, 2.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(52, 25).addBox(-1.0F, -8.0F, -5.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.5F, 4.25F, 0.7854F, 0.0F, 0.0F));

        PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(-0.5F, 20.0F, 0.0F));

        PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 54).addBox(-1.0F, 1.0F, -4.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-1.0F, -4.0F, -8.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 0).addBox(-1.0F, -5.0F, -8.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 0).addBox(-1.0F, 0.0F, -2.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -15.5F, 4.25F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(52, 14).addBox(0.0F, -2.0F, -4.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(40, 4).addBox(0.0F, -5.0F, 4.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 40).addBox(0.0F, -11.0F, 7.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(16, 4).addBox(0.0F, -11.0F, 10.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 36).addBox(0.0F, -10.0F, 6.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(20, 4).addBox(0.0F, -10.0F, 9.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(0.0F, -9.0F, 5.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(24, 4).addBox(0.0F, -9.0F, 8.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 28).addBox(0.0F, -8.0F, 4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 4).addBox(0.0F, -8.0F, 7.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(0.0F, -7.0F, 3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(32, 4).addBox(0.0F, -7.0F, 6.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(0.0F, -6.0F, 2.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 4).addBox(0.0F, -6.0F, 5.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 16).addBox(0.0F, -5.0F, 1.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(0.0F, -14.0F, 13.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 7).addBox(0.0F, -14.0F, 7.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(12, 58).addBox(-0.5F, -13.0F, 12.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 30).addBox(-0.5F, -13.0F, 8.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(18, 58).addBox(-0.5F, -12.0F, 11.0F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(52, 20).addBox(-0.5F, -12.0F, 7.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 4).addBox(0.0F, -4.0F, 3.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 49).addBox(0.0F, -4.0F, -1.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(12, 4).addBox(0.0F, -3.0F, 2.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(0.0F, -3.0F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(4, 4).addBox(0.0F, -2.0F, 1.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 52).addBox(0.0F, -1.0F, -5.0F, 1.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
