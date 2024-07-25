package net.sashakyotoz.wrathy_armament.client.models.technical;


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class LancerBackModel<T extends LivingEntity> extends AgeableListModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("lancer_back_model"), "main");
	private final ModelPart Body;

	public LancerBackModel(ModelPart root) {
		this.Body = root.getChild("Body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -2.1F));

		PartDefinition handle = Body.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, -1.5708F, 1.5708F, -0.6109F));

		PartDefinition cube_r1 = handle.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 5).addBox(-0.5F, 5.0607F, 4.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, 6.0607F, 5.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 5).addBox(-0.5F, 3.0607F, 2.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, 4.0607F, 3.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(-0.5F, 1.0607F, 0.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, 2.0607F, 1.9393F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(-0.5F, -0.9393F, -1.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-0.5F, -7.9393F, -8.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 22).addBox(-0.5F, -3.9393F, -14.0607F, 1.0F, 1.0F, 9.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 5).addBox(-0.5F, -5.9393F, -6.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(-0.5F, -8.9393F, -9.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -6.9393F, -7.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-0.5F, -3.9393F, -4.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(-0.5F, -4.9393F, -5.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-0.5F, -1.9393F, -2.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(-0.5F, -2.9393F, -3.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-0.5F, 0.0607F, -0.0607F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-1.0F, 1.4042F, 2.8679F, 0.7854F, 0.0F, 0.0F));

		PartDefinition blade = handle.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(7.0F, 3.9042F, -5.1321F));

		PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(26, 11).addBox(-0.5F, 18.636F, 6.636F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(27, 12).addBox(-0.5F, 19.636F, 8.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 12).addBox(-0.5F, 20.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 12).addBox(-0.5F, 21.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 12).addBox(-0.5F, 22.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 12).addBox(-0.5F, 23.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 8).addBox(-0.5F, 22.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 8).addBox(-0.5F, 21.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 6).addBox(-0.5F, 19.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 10).addBox(-0.5F, 20.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 8).addBox(-0.5F, 18.636F, 8.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 0).addBox(-0.5F, 17.636F, 5.636F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.001F))
		.texOffs(27, 6).addBox(-0.5F, 20.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 6).addBox(-0.5F, 19.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 6).addBox(-0.5F, 18.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 6).addBox(-0.5F, 17.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 6).addBox(-0.5F, 14.636F, 8.636F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.001F))
		.texOffs(27, 4).addBox(-0.5F, 19.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 6).addBox(-0.5F, 20.636F, 13.636F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 6).addBox(-0.5F, 18.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 4).addBox(-0.5F, 17.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 4).addBox(-0.5F, 15.636F, 9.636F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 0).addBox(-0.5F, 21.636F, 12.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 2).addBox(-0.5F, 20.636F, 11.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 2).addBox(-0.5F, 19.636F, 10.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 0).addBox(-0.5F, 18.636F, 9.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 0).addBox(-0.5F, 17.636F, 8.636F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(-8.0F, -9.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition scythe = handle.addOrReplaceChild("scythe", CubeListBuilder.create(), PartPose.offset(-2.0F, -7.0958F, -5.1321F));

		PartDefinition cube_r3 = scythe.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 4).addBox(0.5F, -22.4939F, -22.2218F, 1.0F, 4.0F, 3.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(0.5F, -18.4939F, -24.2218F, 1.0F, 2.0F, 3.0F, new CubeDeformation(-0.01F))
		.texOffs(24, 22).addBox(0.5F, -21.4939F, -23.2218F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 13).addBox(0.5F, -12.5F, -24.2218F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(22, 0).addBox(0.5F, -15.4939F, -25.2218F, 1.0F, 3.0F, 1.0F, new CubeDeformation(-0.01F))
		.texOffs(15, 22).addBox(0.5F, -18.4939F, -26.2218F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(0.5F, -20.4939F, -25.2218F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(28, 22).addBox(0.5F, -21.4939F, -24.2218F, 1.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 16).addBox(0.5F, -24.4939F, -25.2218F, 1.0F, 3.0F, 3.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(0.5F, -23.4939F, -22.2218F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(6, 12).addBox(0.5F, -24.4939F, -21.2218F, 1.0F, 1.0F, 5.0F, new CubeDeformation(-0.01F))
		.texOffs(15, 11).addBox(0.5F, -23.4939F, -20.2218F, 1.0F, 1.0F, 4.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 0).addBox(0.5F, -22.4939F, -19.2218F, 1.0F, 1.0F, 2.0F, new CubeDeformation(-0.01F))
		.texOffs(8, 0).addBox(0.5F, -21.4939F, -19.2218F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F))
		.texOffs(26, 14).addBox(0.5F, -20.4939F, -19.2218F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 9).addBox(0.5F, -18.75F, -18.25F, 1.0F, 2.0F, 2.0F, new CubeDeformation(-0.011F)), PartPose.offsetAndRotation(0.0F, 9.0F, 20.0F, 0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entity.isCrouching())
			this.Body.xRot = 0.35f;
		else
			this.Body.xRot = 0;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(Body);
	}
}