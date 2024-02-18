package net.sashakyotoz.wrathy_armament.client.models.technical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;


public class ZenithModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("wrathy_armament", "zenith_model"), "main");
	private final ModelPart blade;
	private final ModelPart handle;

	public ZenithModel(ModelPart root) {
		this.blade = root.getChild("blade");
		this.handle = root.getChild("handle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(52, 22).addBox(-0.5F, -23.0F, -23.0F, 1.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-1.0F, 0.0F, -7.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 0).addBox(-0.5F, -18.0F, -14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 0).addBox(-0.5F, -17.0F, -13.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 4).addBox(-0.5F, -16.0F, -13.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(36, 6).addBox(-0.5F, -19.0F, -18.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(38, 0).addBox(-0.5F, -18.0F, -19.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-0.5F, -16.0F, -20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(21, 12).addBox(-0.5F, -18.0F, -21.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 2).addBox(-0.5F, -18.0F, -22.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 0).addBox(-0.5F, -20.0F, -16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 6).addBox(-0.5F, -21.0F, -18.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(10, 0).addBox(-0.5F, -22.0F, -18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 4).addBox(-0.5F, -14.0F, -9.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(28, 0).addBox(-0.5F, -9.0F, -14.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-0.5F, -10.0F, -14.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(40, 24).addBox(-0.5F, -17.0F, -17.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(21, 6).addBox(-0.5F, -9.0F, -12.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(21, 0).addBox(-0.5F, -12.0F, -9.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(54, 14).addBox(-0.5F, -13.0F, -13.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(10, 0).addBox(-0.5F, -14.0F, -10.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(6, 18).addBox(-0.5F, -15.0F, -13.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(15, 4).addBox(-0.5F, -12.0F, -6.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 13).addBox(-0.5F, -11.0F, -6.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-1.0F, -10.0F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 4).addBox(-1.0F, -9.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 4).addBox(-1.0F, -2.0F, -9.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-1.0F, -3.0F, -10.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-0.5F, -14.0F, -18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-0.5F, -13.0F, -17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-0.5F, -13.0F, -16.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 18).addBox(-0.5F, -13.0F, -15.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(15, 0).addBox(-0.5F, -7.0F, -13.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 4).addBox(-0.5F, -13.0F, -7.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-0.5F, -6.0F, -12.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(-0.5F, -6.0F, -11.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-1.0F, -7.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-0.75F, -9.0F, -9.0F, 1.5F, 7.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-1.0F, -2.0F, -8.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(8, 8).addBox(-1.0F, -8.0F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, -5.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 4).addBox(-1.0F, -4.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 4).addBox(-1.0F, -2.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r2 = handle.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(-0.5F, -1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-1.0F, 3.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-1.0F, 2.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-0.5F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 30).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(-0.5F, -3.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(-0.5F, -2.0F, -3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 30).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(-0.5F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 30).addBox(-0.5F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 30).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-0.5F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, 0.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, 0.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(9, 0).addBox(-1.0F, 1.0F, 3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 3).addBox(-1.0F, 1.0F, 1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(9, 2).addBox(-1.0F, 3.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}