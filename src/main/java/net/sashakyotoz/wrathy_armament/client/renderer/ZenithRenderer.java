package net.sashakyotoz.wrathy_armament.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.wrathy_armament.client.models.technical.CopperSwordModel;
import net.sashakyotoz.wrathy_armament.client.models.technical.MeowmereModel;
import net.sashakyotoz.wrathy_armament.client.models.technical.TerrabladeModel;
import net.sashakyotoz.wrathy_armament.client.models.technical.ZenithModel;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;

public class ZenithRenderer extends AdvancedEntityRenderer<ZenithEntity,EntityModel<ZenithEntity>> {
    private static final ResourceLocation zenithTexture = new ResourceLocation("wrathy_armament:textures/item/zenith.png");
    private static final ResourceLocation terrabladeTexture = new ResourceLocation("wrathy_armament:textures/entity/projectile_like/terrablade.png");
    private static final ResourceLocation meowmereTexture = new ResourceLocation("wrathy_armament:textures/entity/projectile_like/meowmere.png");
    private static final ResourceLocation copperSwordTexture = new ResourceLocation("wrathy_armament:textures/entity/projectile_like/copper_sword.png");
    private final ZenithModel<ZenithEntity> zenith;
    private final TerrabladeModel<ZenithEntity> terrablade;
    private final MeowmereModel<ZenithEntity> meowmere;
    private final CopperSwordModel<ZenithEntity> copperSword;

    public ZenithRenderer(EntityRendererProvider.Context context) {
        super(context, null,0.5f);
        zenith = new ZenithModel<>(context.bakeLayer(ZenithModel.LAYER_LOCATION));
        terrablade = new TerrabladeModel<>(context.bakeLayer(TerrabladeModel.LAYER_LOCATION));
        meowmere = new MeowmereModel<>(context.bakeLayer(MeowmereModel.LAYER_LOCATION));
        copperSword = new CopperSwordModel<>(context.bakeLayer(CopperSwordModel.LAYER_LOCATION));
    }

    @Override
    public void render(ZenithEntity zenithEntity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer vb = bufferIn.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(zenithEntity)));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, zenithEntity.yRotO, zenithEntity.getYRot()) - 90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(90 + Mth.lerp(partialTicks, zenithEntity.xRotO, zenithEntity.getXRot())));
        if(this.model != null)
            this.model.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 0.5f);
        else
            this.zenith.renderToBuffer(poseStack, vb, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1f);
        poseStack.popPose();
        switch (zenithEntity.getIndex()){
            default -> this.model = this.copperSword;
            case 1 -> this.model = this.terrablade;
            case 2 -> this.model = this.meowmere;
            case 3,4-> this.model = this.zenith;
        }
        super.render(zenithEntity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(ZenithEntity entity) {
        switch (entity.getIndex()){
            default -> {
                return copperSwordTexture;
            }
            case 1 ->{
                return terrabladeTexture;
            }
            case 2 ->{
                return meowmereTexture;
            }
            case 3,4->{
                return zenithTexture;
            }
        }
    }
}