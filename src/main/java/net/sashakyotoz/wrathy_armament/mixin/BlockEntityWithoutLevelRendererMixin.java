package net.sashakyotoz.wrathy_armament.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.BlackrazorModel;
import net.sashakyotoz.wrathy_armament.mixin.accessor.EntityModelSetAccessor;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Shadow
    @Final
    private EntityModelSet entityModelSet;
    @Unique
    private BlackrazorModel wrathy_armament$blackrazorModel;

    @Inject(method = "onResourceManagerReload", at = @At("TAIL"))
    public void onReload(ResourceManager pResourceManager, CallbackInfo ci) {
        Map<ModelLayerLocation, LayerDefinition> roots = ((EntityModelSetAccessor) entityModelSet).getRoots();
        if (roots.containsKey(BlackrazorModel.LAYER_LOCATION))
            this.wrathy_armament$blackrazorModel = new BlackrazorModel(entityModelSet.bakeLayer(BlackrazorModel.LAYER_LOCATION));
    }

    @Inject(method = "renderByItem", at = @At("HEAD"))
    public void renderItem(ItemStack pStack, ItemDisplayContext pDisplayContext, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay, CallbackInfo ci) {
        if (pStack.is(WrathyArmamentItems.BLACKRAZOR.get())) {
            pPoseStack.pushPose();
            pPoseStack.scale(1.0F, -1.0F, -1.0F);
            switch (pDisplayContext) {
                case THIRD_PERSON_LEFT_HAND, THIRD_PERSON_RIGHT_HAND -> pPoseStack.translate(0.5, -1.5, -1.1);
                case FIRST_PERSON_RIGHT_HAND -> pPoseStack.translate(0.4, -1.5, -1.2);
                case FIRST_PERSON_LEFT_HAND -> pPoseStack.translate(0.6, -1.5, -1.2);
                case GUI -> pPoseStack.translate(2.5, -1.1, 0);
            }
            VertexConsumer mainConsumer = pBuffer.getBuffer(RenderType.entityTranslucent(WrathyArmament.createWALocation("textures/item/blackrazor.png")));
            if (wrathy_armament$blackrazorModel != null){
                this.wrathy_armament$blackrazorModel.handle().render(pPoseStack, mainConsumer, pPackedLight, pPackedOverlay);
                this.wrathy_armament$blackrazorModel.blade().render(pPoseStack, mainConsumer, pPackedLight, pPackedOverlay, 1, pStack.getOrCreateTag().getInt("hungryTimer") > 0 ? 0 : 1, 1, 0.375f);
                Minecraft minecraft = Minecraft.getInstance();
                LocalPlayer player = minecraft.player;
                pPoseStack.scale(1.01f, 1.01f, 1.01f);
                if (player != null) {
                    float f = (player.tickCount - minecraft.getPartialTick()) * 0.25f;
                    VertexConsumer consumer = pBuffer.getBuffer(RenderType.energySwirl(TheEndPortalRenderer.END_PORTAL_LOCATION, this.wrathy_armament$getEnergySwirlX(f) % 1.0F, f * 0.01F % 1.0F));
                    this.wrathy_armament$blackrazorModel.blade().render(pPoseStack, consumer, pPackedLight, pPackedOverlay);
                }
            }
            pPoseStack.popPose();
        }
    }

    @Unique
    private float wrathy_armament$getEnergySwirlX(float partialAge) {
        return Mth.cos(partialAge * 0.02F) * 2.0F;
    }
}