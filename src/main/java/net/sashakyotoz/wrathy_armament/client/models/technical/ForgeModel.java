package net.sashakyotoz.wrathy_armament.client.models.technical;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.sashakyotoz.wrathy_armament.WrathyArmament;

public class ForgeModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("forge_model"), "main");
	private final ModelPart anvil;

	public ForgeModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.anvil = root.getChild("anvil");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition anvil = partdefinition.addOrReplaceChild("anvil", CubeListBuilder.create().texOffs(0, 1).addBox(-14.0F, -4.0F, 2.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(28, 22).addBox(-12.0F, -5.0F, 3.0F, 8.0F, 1.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(40, 0).addBox(-10.0F, -10.0F, 4.0F, 4.0F, 5.0F, 8.0F, new CubeDeformation(0.01F))
		.texOffs(12, 42).addBox(-13.0F, -16.0F, 0.0F, 10.0F, 6.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 24.0F, -8.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		anvil.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}