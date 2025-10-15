package net.sashakyotoz.wrathy_armament.client.models.technical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;

public class ShieldDashModel<T extends HarmfulProjectileEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("shield_dash_model"), "main");
	private final ModelPart shield;

	public ShieldDashModel(ModelPart root) {
		this.shield = root.getChild("shield");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition shield = partdefinition.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(0, 47).addBox(-8.0F, -8.0F, -9.0F, 16.0F, 16.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = shield.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 13).addBox(-8.0F, -4.0F, -9.0F, 16.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = shield.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -9.0F, 16.0F, 12.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(HarmfulProjectileEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		shield.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}