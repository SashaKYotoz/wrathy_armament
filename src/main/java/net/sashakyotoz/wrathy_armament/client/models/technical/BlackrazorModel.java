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

public class BlackrazorModel extends Model {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(WrathyArmament.createWALocation("blackrazor_model"), "main");
    private final ModelPart root;
    private final ModelPart blade;
    private final ModelPart handle;

    public BlackrazorModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.blade = root.getChild("blade");
        this.handle = root.getChild("handle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition blade = partdefinition.addOrReplaceChild("blade", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition cube_r1 = blade.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 14).addBox(-1.0F, -12.0F, -13.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(6, 10).addBox(-1.0F, -24.0F, -24.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-1.0F, -24.0F, -23.0F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -23.0F, -23.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -22.0F, -23.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -26.0F, -26.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 6).addBox(-1.0F, -25.0F, -26.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -25.0F, -25.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -24.0F, -25.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -22.0F, -22.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -21.0F, -22.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -21.0F, -21.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -20.0F, -21.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -20.0F, -20.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -19.0F, -20.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -19.0F, -19.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -18.0F, -19.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -18.0F, -18.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -17.0F, -18.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -17.0F, -17.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(-1.0F, -16.0F, -17.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(34, 26).addBox(-1.0F, -14.0F, -14.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(14, 18).addBox(-1.0F, -13.0F, -14.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -13.0F, -13.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 21.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r2 = blade.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(34, 26).addBox(-1.0F, -29.0F, -29.0F, 2.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(30, 18).addBox(-1.0F, -28.0F, -26.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(14, 18).addBox(-1.0F, -28.0F, -29.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 0).addBox(-1.0F, -28.0F, -28.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 27).addBox(-1.0F, -26.0F, -26.0F, 2.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(8, 6).addBox(-1.0F, -25.0F, -26.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.75F, 5.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition handle = partdefinition.addOrReplaceChild("handle", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

        PartDefinition cube_r3 = handle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(56, 6).addBox(-0.999F, -13.999F, -9.001F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(14, 10).addBox(-1.0F, -12.0F, -12.0F, 2.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(50, 25).addBox(-1.5F, -16.0F, -14.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(52, 12).addBox(-1.49F, -15.99F, -16.01F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(-0.999F, -15.999F, -6.001F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(50, 6).addBox(-0.999F, -17.999F, -8.001F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(-0.999F, -16.999F, -7.001F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(56, 0).mirror().addBox(-0.999F, -11.999F, -8.001F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(0, 6).addBox(-0.999F, -7.999F, -8.001F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-1.009F, -7.009F, -6.991F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 21.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r4 = handle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(56, 6).mirror().addBox(-0.999F, -13.999F, 7.001F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 0).mirror().addBox(-0.999F, -11.999F, 5.999F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(28, 0).mirror().addBox(-0.999F, -15.999F, 4.999F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(50, 0).mirror().addBox(-0.999F, -16.999F, 5.999F, 2.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(50, 6).mirror().addBox(-0.999F, -17.999F, 7.001F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 21.0F, 5.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r5 = handle.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 6).addBox(-0.999F, -6.999F, -7.001F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 22.25F, 5.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r6 = handle.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 19.25F, 5.0F, -0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r7 = handle.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(20, 18).addBox(-1.001F, -2.001F, -1.999F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 18.0F, 5.0F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    public ModelPart blade() {
        return this.blade;
    }

    public ModelPart handle() {
        return this.handle;
    }
}