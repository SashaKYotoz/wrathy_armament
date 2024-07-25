package net.sashakyotoz.wrathy_armament.client.models.technical;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.BladeOfChaosEntity;

public class BladeOfChaosModel<T extends BladeOfChaosEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("blade_of_chaos_model"), "main");
	private final ModelPart blade;
	private final ModelPart handle;
	private final ModelPart chain;

	public BladeOfChaosModel(ModelPart root) {
		this.blade = root.getChild("blade");
		this.handle = root.getChild("handle");
		this.chain = root.getChild("chain");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(13, 0).addBox(-0.5F, -12.0F, 4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(14, 13).addBox(-0.5F, -12.0F, 10.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(28, 0).addBox(-0.5F, -15.0F, 5.0F, 1.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(58, 52).addBox(-0.5F, -26.0F, 25.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(31, 12).addBox(-0.5F, -20.0F, 29.0F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(31, 12).addBox(-0.5F, -20.0F, 29.0F, 1.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 19).addBox(-0.5F, -24.0F, 27.0F, 1.0F, 10.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 18).addBox(-0.5F, -28.0F, 23.0F, 1.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 14).addBox(-0.5F, -30.0F, 20.0F, 1.0F, 15.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(22, 10).addBox(-0.5F, -20.0F, 19.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(-0.5F, -18.0F, 18.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(10, 12).addBox(-0.5F, -16.0F, 17.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(23, 14).addBox(-0.5F, -16.0F, 14.0F, 1.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(52, 4).addBox(-0.5F, -25.0F, 2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 4).addBox(-0.5F, -21F, 7F, 1F, 1F, 1F, new CubeDeformation(0.0F))
		.texOffs(52, 4).addBox(-0.5F, -20F, 4F, 1F, 1F, 1F, new CubeDeformation(0.0F))
		.texOffs(52, 4).addBox(-0.5F, -20F, 5F, 1F, 1F, 1F, new CubeDeformation(0.0F))
		.texOffs(52, 4).addBox(-0.5F, -19F, 5F, 1F, 1F, 1F, new CubeDeformation(0.0F))
		.texOffs(13, 0).addBox(-0.5F, -20F, 6F, 1F, 3F, 2F, new CubeDeformation(0.0F))
		.texOffs(20, 23).addBox(-0.5F, -21F, 8F, 1F, 6F, 2F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -25.0F, 3.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(13, 0).addBox(-0.5F, -26.0F, 4.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(20, 23).addBox(-0.5F, -27.0F, 6.0F, 1.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 32).addBox(-0.5F, -30.0F, 16.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 46).addBox(-0.5F, -29.0F, 12.0F, 1.0F, 9.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(10, 32).addBox(-0.5F, -28.0F, 8.0F, 1.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(36, 0).addBox(-0.5F, -20.0F, 9.0F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(-0.5F, -18.0F, 8.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(28, 12).addBox(-0.5F, -17.0F, 8.0F, 1.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(6, 11).addBox(-0.5F, -15.0F, 13.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(17, 22).addBox(-0.5F, -16.0F, 5.0F, 1.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
		.texOffs(6, 11).addBox(-0.5F, -14.0F, 12.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 0).addBox(-0.5F, -14.0F, 5.0F, 1.0F, 1.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(6, 11).addBox(-0.5F, -13.0F, 11.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(13, 0).addBox(-0.5F, -13.0F, 5.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(42, 58).addBox(-0.5F, -11.0F, 9.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 14).addBox(-1.0F, -11.0F, 4.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -11.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(18, 12).addBox(-0.5F, -20.5F, 29.5F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -9.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition cube_r3 = handle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-0.501F, -5.001F, 0.999F, 1.002F, 3.002F, 4.002F, new CubeDeformation(0.0F))
		.texOffs(52, 6).addBox(-0.5F, -6.0F, 11.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 5).addBox(-0.5F, -6.0F, 10.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-0.5F, -6.0F, 9.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 58).addBox(-0.5F, -6.0F, 8.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 57).addBox(-0.5F, -6.0F, 7.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(46, 58).addBox(-0.5F, -6.0F, 6.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 4).addBox(-0.5F, -3.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(20, 33).addBox(-0.501F, -12.001F, -0.001F, 1.002F, 3.002F, 3.002F, new CubeDeformation(0.0F))
		.texOffs(21, 0).addBox(-0.501F, -14.001F, -4.001F, 1.002F, 2.002F, 2.002F, new CubeDeformation(0.0F))
		.texOffs(16, 7).addBox(-0.5F, -13.0F, -3.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(38, 0).addBox(-0.5F, -14.0F, -1.0F, 1.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(20, 33).addBox(-0.5F, -11.0F, 1.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(10, 44).addBox(-0.5F, -8.0F, 2.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.5F, -4.0F, 0.0F, 1.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(50, 32).addBox(-1.0F, -2.0F, -2.0F, 2.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(48, 24).addBox(-1.5F, 0.0F, -3.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -11.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition chain = partdefinition.addOrReplaceChild("chain", CubeListBuilder.create().texOffs(58, 9).addBox(-1.5F, 12.5F, -9.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(40, 14).addBox(0.0F, 12.5F, -10.5F, 0.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition cube_r4 = chain.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(58, 4).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(40, 9).addBox(0.0F, 0.0F, -1.5F, 0.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 8.0F, -11.0F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(BladeOfChaosEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		chain.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}