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
import net.sashakyotoz.wrathy_armament.WrathyArmament;


public class TerrabladeModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("terrablade_model"), "main");
	private final ModelPart blade;
	private final ModelPart handle;

	public TerrabladeModel(ModelPart root) {
		this.blade = root.getChild("blade");
		this.handle = root.getChild("handle");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create().texOffs(8, 10).addBox(-0.5F, -10.0F, 8.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.01F))
		.texOffs(45, 14).addBox(-0.5F, -15.0F, 6.0F, 1.0F, 5.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(24, 0).addBox(-0.5F, -14.0F, 11.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(54, 23).addBox(-0.5F, -15.0F, 8.0F, 1.0F, 5.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(4, 10).addBox(-0.5F, -15.0F, 13.0F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(52, 16).addBox(-0.5F, -16.0F, 8.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.01F))
		.texOffs(0, 10).addBox(-0.5F, -16.0F, 14.0F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 27).addBox(-0.5F, -17.0F, 10.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(31, 28).addBox(-0.5F, -17.0F, 14.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-0.5F, -15.0F, 15.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 19).addBox(-0.5F, -16.0F, 15.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(4, 30).addBox(-0.5F, -17.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 30).addBox(-0.5F, -19.0F, 17.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 30).addBox(-0.5F, -20.0F, 16.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(4, 30).addBox(-0.5F, -18.0F, 18.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 28).addBox(-0.5F, -20.0F, 18.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(4, 29).addBox(-0.5F, -22.0F, 20.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 25).addBox(-0.5F, -28.0F, 21.0F, 1.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 26).addBox(-0.5F, -28.0F, 20.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 29).addBox(-0.5F, -27.0F, 19.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(56, 12).addBox(-0.5F, -26.0F, 18.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(22, 26).addBox(-0.5F, -25.0F, 17.0F, 1.0F, 2.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(20, 11).addBox(-0.5F, -23.0F, 16.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(20, 11).addBox(-0.5F, -22.0F, 15.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(52, 1).addBox(-0.5F, -21.0F, 15.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(38, 0).addBox(-0.5F, -20.0F, 13.0F, 1.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(31, 10).addBox(-0.5F, -18.0F, 12.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(46, 17).addBox(-0.5F, -11.0F, 5.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(34, 17).addBox(-0.5F, -10.0F, 4.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.01F))
		.texOffs(25, 30).addBox(-0.5F, -8.0F, 8.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F))
		.texOffs(24, 17).addBox(-0.5F, -9.0F, 7.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.25F, -2.3562F, 0.0F, -3.1416F));

		PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create().texOffs(0, 1).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 5).addBox(-0.5F, -3.0F, 0.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 5).addBox(-0.5F, -5.0F, 1.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(12, 20).addBox(-0.5F, -5.0F, 3.0F, 1.0F, 2.0F, 5.0F, new CubeDeformation(0.01F))
		.texOffs(8, 17).addBox(-0.5F, -9.0F, 3.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.01F))
		.texOffs(32, 0).addBox(-0.5F, -3.0F, 5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(14, 1).addBox(-0.5F, -2.0F, 4.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(45, 0).addBox(-0.5F, -5.0F, 8.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 4).addBox(-0.5F, -6.0F, 9.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(38, 5).addBox(-0.5F, -13.0F, 2.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.01F))
		.texOffs(8, 2).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 2).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 2).addBox(-1.0F, -5.0F, 1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, -6.0F, 2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(43, 21).addBox(-1.0F, -10.0F, 1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-1.0F, -8.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(15, 6).addBox(-1.0F, -8.0F, -2.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-1.0F, -9.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 6).addBox(-1.0F, -12.0F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.0F, 0.25F, -2.3562F, 0.0F, 3.1416F));

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