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

public class CopperSwordModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("copper_sword_model"), "main");
	private final ModelPart sword;

    public CopperSwordModel(ModelPart root) {
		this.sword = root.getChild("sword");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition sword = partdefinition.addOrReplaceChild("sword", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition blade = sword.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.5F, 5.0F, -5.0F));

		PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(11, -1).mirror().addBox(-0.001F, 2.999F, 1.001F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(26, 12).mirror().addBox(0.0F, -4.0F, -4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, 12).mirror().addBox(0.0F, -6.0F, -6.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(14, 12).mirror().addBox(0.0F, 2.0F, 2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(7, 11).mirror().addBox(-0.001F, 0.999F, 1.001F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(8, 12).mirror().addBox(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(7, 11).mirror().addBox(-0.001F, -1.001F, -0.999F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(14, 12).mirror().addBox(0.0F, -2.0F, -2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(13, 11).mirror().addBox(-0.001F, -3.001F, -2.999F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(25, 11).mirror().addBox(-0.001F, -5.001F, -4.999F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(15, -1).mirror().addBox(-0.001F, -7.001F, -5.999F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(5, -1).mirror().addBox(-0.001F, -8.001F, -7.999F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(11, -1).mirror().addBox(-0.001F, -6.001F, -6.999F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(15, -1).mirror().addBox(-0.001F, 0.999F, 3.001F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(23, 11).mirror().addBox(-0.001F, -6.001F, -3.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -4.001F, -5.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(23, 11).mirror().addBox(-0.001F, -5.001F, -2.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -3.001F, -4.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(23, 11).mirror().addBox(-0.001F, -4.001F, -1.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -2.001F, -3.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -3.001F, -0.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -1.001F, -2.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -2.001F, 0.001F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -1.001F, 1.001F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -0.001F, -1.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, 0.999F, -0.999F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, 1.999F, 0.001F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(17, 11).mirror().addBox(-0.001F, 3.999F, 2.001F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(17, 11).mirror().addBox(-0.001F, 1.999F, 4.001F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(17, 11).mirror().addBox(-0.001F, 2.999F, 3.001F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(20, -1).mirror().addBox(-0.001F, -0.001F, 2.001F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, -5.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition handle = sword.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 9.5F, 0.0F));

		PartDefinition cube_r2 = handle.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(19, -1).mirror().addBox(-0.501F, 4.999F, 2.001F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(19, -1).mirror().addBox(-0.501F, 3.999F, 3.001F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(19, -1).mirror().addBox(-0.501F, 1.999F, 5.001F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(19, -1).mirror().addBox(-0.501F, 2.999F, 4.001F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(23, 3).mirror().addBox(-0.501F, 3.999F, 4.001F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(23, 3).mirror().addBox(-0.501F, 4.999F, 5.001F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(24, 0).mirror().addBox(-0.5F, 6.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.5F, -9.5F, 0.0F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		sword.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}