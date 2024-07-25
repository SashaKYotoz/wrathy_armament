package net.sashakyotoz.wrathy_armament.blocks.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.WorldshardWorkbenchBlockEntity;
import net.sashakyotoz.wrathy_armament.client.models.technical.ForgeModel;

public class WorldshardWorkbenchEntityRenderer implements BlockEntityRenderer<WorldshardWorkbenchBlockEntity> {
    public static ResourceLocation CHAOS_FORGE = new ResourceLocation(WrathyArmament.MODID, "textures/block/chaos_forge.png");
    public static ResourceLocation TECHNOLOGISTIC_FORGE = new ResourceLocation(WrathyArmament.MODID, "textures/block/technologistic_forge.png");
    public static ResourceLocation ELEMENTAL_ANVIL = new ResourceLocation(WrathyArmament.MODID, "textures/block/elemental_anvil.png");
    public static ResourceLocation MYTHRIL_ANVIL = new ResourceLocation(WrathyArmament.MODID, "textures/block/mythril_anvil.png");
    private final ForgeModel forgeModel;
    private final ItemRenderer itemRenderer;

    public WorldshardWorkbenchEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.forgeModel = new ForgeModel(context.getModelSet().bakeLayer(ForgeModel.LAYER_LOCATION));
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(WorldshardWorkbenchBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        long gameTime = 0;
        ResourceLocation location;
        if (entity.getLevel() != null)
            gameTime = entity.getLevel().getGameTime();
        poseStack.pushPose();
        poseStack.translate(0.5f, 1.6f, 0.5f);
        poseStack.mulPose(Axis.YP.rotation((gameTime % 360) * 0.05f));
        poseStack.scale(0.5f, -0.5f, 0.5f);
        if (entity.getRecipe() != null) {
            switch (entity.getModelVariantForRecipe()) {
                default -> location = MYTHRIL_ANVIL;
                case 1 -> location = CHAOS_FORGE;
                case 2 -> location = ELEMENTAL_ANVIL;
                case 3 -> location = TECHNOLOGISTIC_FORGE;
            }
            this.forgeModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(location)), packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 0.5f);
        } else if (!entity.stackToRender().isEmpty()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.scale(1.25f,1.25f,1.25f);
            this.itemRenderer.renderStatic(entity.stackToRender(), ItemDisplayContext.GUI, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getLevel(), packedOverlay);
        }
        poseStack.popPose();
    }
}