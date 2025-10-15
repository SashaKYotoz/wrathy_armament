package net.sashakyotoz.wrathy_armament.client.models.technical;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class SphereLikeEntityModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("sphere_like_entity_model"), "main");
	private final ModelPart circle;

	public SphereLikeEntityModel(ModelPart root) {
		this.circle = root.getChild("circle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition circle = partdefinition.addOrReplaceChild("circle", CubeListBuilder.create().texOffs(0, 2).addBox(-15.0F, 1.5F, -15.0F, 30.0F, 1.0F, 30.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition down_part = circle.addOrReplaceChild("down_part", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

		PartDefinition down_right_corner2 = down_part.addOrReplaceChild("down_right_corner2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = down_right_corner2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 49).addBox(-1.0F, 1.0F, -4.0F, 2.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.5F, -0.75F, -11.5F, 0.6545F, -0.3578F, -1.1345F));

		PartDefinition cube_r2 = down_right_corner2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 42).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -0.25F, 0.0F, 0.0F, 0.0F, -1.0472F));

		PartDefinition cube_r3 = down_right_corner2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(96, 39).addBox(-7.0F, 0.0F, -1.0F, 14.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, 14.0F, -1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r4 = down_right_corner2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(18, 49).addBox(-1.0F, 1.0F, -4.0F, 2.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.75F, -0.75F, 11.25F, -0.6545F, 0.3578F, -1.1345F));

		PartDefinition down_left_corner2 = down_part.addOrReplaceChild("down_left_corner2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = down_left_corner2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(18, 49).addBox(-1.0F, 1.0F, -4.0F, 2.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.75F, -0.75F, 11.25F, -0.6545F, -0.3578F, 1.1345F));

		PartDefinition cube_r6 = down_left_corner2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(12, 42).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -0.25F, 0.0F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r7 = down_left_corner2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(96, 49).addBox(-7.0F, 0.0F, -1.0F, 14.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.25F, -15.0F, 1.0472F, 0.0F, 0.0F));

		PartDefinition cube_r8 = down_left_corner2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(18, 49).addBox(-1.0F, 1.0F, -4.0F, 2.0F, 7.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.75F, -0.75F, -11.25F, 0.6545F, 0.3578F, 1.1345F));

		return LayerDefinition.create(meshdefinition, 128, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.circle.xRot = ageInTicks%360;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.circle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}