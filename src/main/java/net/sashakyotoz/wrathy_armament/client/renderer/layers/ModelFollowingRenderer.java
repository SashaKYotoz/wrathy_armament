package net.sashakyotoz.wrathy_armament.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ModelFollowingRenderer {
    <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            PoseStack matrixStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer,
            int light, float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks, float netHeadYaw,
            float headPitch);

    static void translateIfSwimming(final PoseStack matrixStack, final LivingEntity livingEntity) {
        if(livingEntity.isVisuallySwimming())
            matrixStack.translate(0.0F, 0.0F, -0.1F);
    }

    static void rotateIfSneaking(final PoseStack matrixStack, final LivingEntity livingEntity) {
        if (livingEntity.isCrouching()) {
            EntityRenderer<? super LivingEntity> render =
                    Minecraft.getInstance().getEntityRenderDispatcher()
                            .getRenderer(livingEntity);
            if (render instanceof LivingEntityRenderer) {
                @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
                        livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
                EntityModel<LivingEntity> model = livingRenderer.getModel();

                if (model instanceof HumanoidModel) {
                    matrixStack.mulPose(Axis.XP.rotation(((HumanoidModel<LivingEntity>) model).body.xRot *0.9f));
                }
            }
        }
    }

    static void followHeadRotations(final LivingEntity livingEntity,
                                    final ModelPart... renderers) {
        EntityRenderer<? super LivingEntity> render =
                Minecraft.getInstance().getEntityRenderDispatcher()
                        .getRenderer(livingEntity);

        if (render instanceof LivingEntityRenderer) {
            @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
                    livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
            EntityModel<LivingEntity> model = livingRenderer.getModel();

            if (model instanceof HumanoidModel) {

                for (ModelPart renderer : renderers) {
                    renderer.copyFrom(((HumanoidModel<LivingEntity>) model).head);
                }
            }
        }
    }

    @SafeVarargs
    static void followBodyRotations(final LivingEntity livingEntity, final HumanoidModel<LivingEntity>... models) {
        EntityRenderer<? super LivingEntity> render =
                Minecraft.getInstance().getEntityRenderDispatcher()
                        .getRenderer(livingEntity);

        if (render instanceof LivingEntityRenderer) {
            @SuppressWarnings("unchecked") LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>
                    livingRenderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>) render;
            EntityModel<LivingEntity> entityModel = livingRenderer.getModel();

            if (entityModel instanceof HumanoidModel) {
                for (HumanoidModel<LivingEntity> model : models) {
                    HumanoidModel<LivingEntity> bipedModel = (HumanoidModel<LivingEntity>) entityModel;
                    bipedModel.copyPropertiesTo(model);
                }
            }
        }
    }
}
