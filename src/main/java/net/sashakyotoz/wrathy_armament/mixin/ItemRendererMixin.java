package net.sashakyotoz.wrathy_armament.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.items.swords.MasterSword;
import net.sashakyotoz.wrathy_armament.items.swords.MirrorSword;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.RenderUtils;
import net.sashakyotoz.wrathy_armament.utils.WARenderTypes;
import net.sashakyotoz.wrathy_armament.utils.capabilities.items.XPTiers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Shadow
    public abstract void renderModelLists(BakedModel pModel, ItemStack pStack, int pCombinedLight, int pCombinedOverlay, PoseStack pPoseStack, VertexConsumer pBuffer);

    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private ItemColors itemColors;

    @Inject(method = "renderQuadList", at = @At(value = "HEAD"), cancellable = true)
    public void renderQuadList(PoseStack pPoseStack, VertexConsumer pBuffer, List<BakedQuad> pQuads, ItemStack pItemStack, int pCombinedLight, int pCombinedOverlay, CallbackInfo ci) {
        boolean flag = !pItemStack.isEmpty();
        PoseStack.Pose posestack$pose = pPoseStack.last();
        for (BakedQuad bakedquad : pQuads) {
            int i = -1;
            if (flag && bakedquad.isTinted()) {
                i = this.itemColors.getColor(pItemStack, bakedquad.getTintIndex());
            }
            if (pItemStack.getItem() instanceof MasterSword) {
                float lights = pItemStack.getOrCreateTag().getInt("Lights");
                float f = ((i >> 16 & 255) / 255.0F);
                float f1 = ((i >> 8 & 255) / 255.0F) - (lights * 0.05f);
                float f2 = (i & 255) / 255.0F - (lights * 0.1f);
                f1 = Math.max(f1, 0.2F);
                f2 = Math.max(f2, 0.0F);
                pBuffer.putBulkData(posestack$pose, bakedquad, f, f1, f2, 1.0F, pCombinedLight, pCombinedOverlay, true);
            } else if (pItemStack.getItem() instanceof MirrorSword) {
                float damage = pItemStack.getOrCreateTag().getInt("damageKeep") / 10f;
                float f = (float) (i >> 16 & 255) / 255.0F - (damage / 8f);
                float f1 = (float) (i >> 8 & 255) / 255.0F - damage;
                float f2 = (float) (i & 255) / 255.0F;
                f = Math.max(f, 0);
                f1 = Math.max(f1, 0);
                f2 = Math.max(f2, 0);
                pBuffer.putBulkData(posestack$pose, bakedquad, f, f1, f2, 1.0F, pCombinedLight, pCombinedOverlay, true);
            } else {
                float f = (float) (i >> 16 & 255) / 255.0F;
                float f1 = (float) (i >> 8 & 255) / 255.0F;
                float f2 = (float) (i & 255) / 255.0F;
                pBuffer.putBulkData(posestack$pose, bakedquad, f, f1, f2, 1.0F, pCombinedLight, pCombinedOverlay, true);
            }
        }
        ci.cancel();
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V"))
    private void renderOutline(ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay, BakedModel pModel, CallbackInfo ci) {
        if (pItemStack.getItem() instanceof SwordLikeItem item && this.minecraft.getCameraEntity() != null) {
            if (item.getCurrentSparkles(pItemStack) < 5 && item.getStoredXP(pItemStack) > XPTiers.values()[item.getCurrentSparkles(pItemStack)].getNeededXP()) {
                pPoseStack.translate(-0.00077f, 0.0009f, 0.0009);
                pPoseStack.scale(1.0015f, 1.0015f, 1.0015f);
                renderModelLists(this.itemModelShaper.getItemModel(pItemStack), pItemStack, pCombinedLight, pCombinedOverlay, pPoseStack,
                        pBuffer.getBuffer(OnActionsTrigger.isOculusIn() ?
                                RenderType.energySwirl(WrathyArmament.createWALocation("textures/item/materials/lined_sparkle_blinking.png"),
                                        RenderUtils.getOscillatingValue(this.minecraft.getCameraEntity().tickCount, 8), 0)
                                : WARenderTypes.translucentSwirl(
                                WrathyArmament.createWALocation("textures/item/materials/sparkle_blinking.png"),
                                RenderUtils.getOscillatingValue(this.minecraft.getCameraEntity().tickCount, 24), 0)));
            }
        }
    }
}