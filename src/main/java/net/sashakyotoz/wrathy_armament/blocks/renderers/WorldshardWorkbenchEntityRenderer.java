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
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.entities.WorldshardWorkbenchBlockEntity;
import net.sashakyotoz.wrathy_armament.client.models.technical.ForgeModel;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.mixin.accessor.ItemRendererAccessor;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.RenderUtils;
import net.sashakyotoz.wrathy_armament.utils.WARenderTypes;

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
        ItemStack stack = entity.itemHandler.getStackInSlot(0);
        if (entity.getLevel() != null)
            gameTime = entity.getLevel().getGameTime();
        if (entity.hasRecipe()) {
            switch (entity.getModelVariantForRecipe()) {
                case 1 -> location = CHAOS_FORGE;
                case 2 -> location = ELEMENTAL_ANVIL;
                case 3 -> location = TECHNOLOGISTIC_FORGE;
                default -> location = MYTHRIL_ANVIL;
            }
            poseStack.pushPose();
            poseStack.translate(0.5f, 1.6f, 0.5f);
            poseStack.mulPose(Axis.YP.rotation((gameTime % 360) * 0.05f));
            poseStack.scale(0.5f, -0.5f, 0.5f);
            this.forgeModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(location)), packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 0.5f);
            poseStack.popPose();
        } else if (!entity.stackToRender().isEmpty() || (entity.progress > 0 && stack.getItem() instanceof SwordLikeItem)) {
            poseStack.pushPose();
            poseStack.translate(0.5f, 1.5f, 0.5f);
            poseStack.mulPose(Axis.YP.rotation((gameTime % 360) * 0.05f));
            float scaleFactor = stack.getItem() instanceof SwordLikeItem ? 1 : 0.5f;
            poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
            this.itemRenderer.renderStatic(entity.stackToRender(), ItemDisplayContext.GUI, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getLevel(), packedOverlay);
            if (stack.getItem() instanceof SwordLikeItem) {
                poseStack.translate(-0.0009f, -0.0009f, 0);
                poseStack.scale(1.0015f, 1.0015f, 1.0015f);
                this.itemRenderer.renderModelLists(((ItemRendererAccessor) this.itemRenderer).getShaper().getItemModel(stack), stack, packedLight, OverlayTexture.NO_OVERLAY, poseStack,
                        buffer.getBuffer(OnActionsTrigger.isOculusIn() ?
                                RenderType.energySwirl(WrathyArmament.createWALocation("textures/item/materials/lined_sparkle_blinking.png"),
                                        RenderUtils.getOscillatingValue(entity.progress, 10), 0)
                                : WARenderTypes.translucentSwirl(
                                WrathyArmament.createWALocation("textures/item/materials/sparkle_blinking.png"),
                                RenderUtils.getOscillatingValue(entity.progress, 4), 0)));
            }
            poseStack.popPose();
        }

    }
}