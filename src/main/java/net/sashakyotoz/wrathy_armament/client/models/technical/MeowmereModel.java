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


public class MeowmereModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("meowmere_model"), "main");
	private final ModelPart blade;
	private final ModelPart handle;
	private final ModelPart bb_main;

	public MeowmereModel(ModelPart root) {
		this.blade = root.getChild("blade");
		this.handle = root.getChild("handle");
		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 1).addBox(1.0F, -8.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(6.0F, -3.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(1.0F, -6.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(4.0F, -3.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 0.0F, -0.75F, -1.5708F, 0.7854F, -1.5708F));

		PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r2 = handle.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 1).addBox(0.0F, 0.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 1).addBox(-2.0F, -2.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 1).addBox(1.0F, -4.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.001F))
		.texOffs(0, 30).addBox(4.0F, -12.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 15).addBox(6.0F, -11.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(15, 4).addBox(3.0F, -11.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(5.0F, -13.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(11.0F, -7.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 15).addBox(10.0F, -9.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 15).addBox(8.0F, -8.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(16, 0).addBox(8.0F, -7.0F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 4).addBox(10.0F, -14.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 7).addBox(10.0F, -11.0F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 25).addBox(21.0F, -21.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(16, 25).addBox(20.0F, -20.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 25).addBox(19.0F, -20.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 24).addBox(18.0F, -20.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 24).addBox(17.0F, -17.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 24).addBox(16.0F, -16.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 24).addBox(15.0F, -14.0F, -0.5F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(12, 28).addBox(14.0F, -14.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 20).addBox(13.0F, -14.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 10).addBox(12.0F, -17.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 12).addBox(11.0F, -16.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 8).addBox(10.0F, -15.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(9.0F, -14.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 0).addBox(8.0F, -13.0F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 0).addBox(7.0F, -11.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 8).addBox(2.0F, -9.0F, -0.5F, 6.0F, 6.0F, 1.0F, new CubeDeformation(-0.01F))
		.texOffs(0, 4).addBox(-1.0F, -3.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 0.0F, -0.75F, -1.5708F, 0.7854F, -1.5708F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r3 = bb_main.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(8, 24).addBox(22.0F, -27.0F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 24).addBox(21.0F, -28.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(7, 22).addBox(20.0F, -27.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(56, 30).addBox(19.0F, -26.0F, -0.5F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(54, 28).addBox(18.0F, -25.0F, -0.5F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 26).addBox(17.0F, -24.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 21).addBox(16.0F, -23.0F, -0.5F, 6.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 23).addBox(15.0F, -22.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 19).addBox(14.0F, -21.0F, -0.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 14).addBox(13.0F, -20.0F, -0.5F, 5.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 0.0F, -0.75F, -1.5708F, 0.7854F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		blade.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		handle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		bb_main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}