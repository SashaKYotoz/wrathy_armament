package net.sashakyotoz.wrathy_armament.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.bettercombat.api.MinecraftClient_BetterCombat;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.items.SwingParticleHolder;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.networking.WANetworkingManager;
import net.sashakyotoz.wrathy_armament.networking.packets.UpdateParticlePacket;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
    @Inject(method = "renderArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.AFTER))
    public void renderArmWithItem(LivingEntity pLivingEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, HumanoidArm pArm, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        //Note: goes by MIT License Copyright (c) 2023 The Celestial Workshop https://github.com/AquexTheSeal/Celestisynth/tree/forge-1.20.1
        if (OnActionsTrigger.isBetterCombatIn() && !Minecraft.getInstance().isPaused()) {
            if (pLivingEntity instanceof Player player && player.getUUID() == Minecraft.getInstance().player.getUUID() && pItemStack.getItem() instanceof SwordLikeItem item) {
                if (item.getSwingHolder(pLivingEntity, pItemStack) != null
                        && ((Minecraft.getInstance() instanceof MinecraftClient_BetterCombat bcPlayer && bcPlayer.isWeaponSwingInProgress())
                        || (OnActionsTrigger.animationPlayersMap.get(player.getUUID()) != null && OnActionsTrigger.animationPlayersMap.get(player.getUUID()).isActive()))) {
                    SwingParticleHolder swingContainer = item.getSwingHolder(pLivingEntity, pItemStack);
                    CameraType cameraType = Minecraft.getInstance().options.getCameraType();
                    Matrix4f localMatrix = new Matrix4f(pPoseStack.last().pose());
                    double xR = cameraType == CameraType.THIRD_PERSON_FRONT ? Math.toRadians(pLivingEntity.getXRot()) : -Math.toRadians(pLivingEntity.getXRot());
                    double yR = cameraType == CameraType.THIRD_PERSON_FRONT ? -Math.toRadians(pLivingEntity.getYRot()) : -Math.toRadians(pLivingEntity.getYRot() - 180);
                    Vector4f relativePosition = new Vector4f().mulProject(localMatrix).rotateX((float) xR).rotateY((float) yR).mul(swingContainer.sizeMult());
                    double x = pLivingEntity.getX() + relativePosition.x();
                    double y = pLivingEntity.getY() + relativePosition.y() + 2 + ((swingContainer.sizeMult() - 1.5) * 0.3333);
                    double z = pLivingEntity.getZ() + relativePosition.z();
                    Vec3 lookAngle;
                    if (cameraType == CameraType.THIRD_PERSON_BACK) {
                        lookAngle = pLivingEntity.getLookAngle().scale(swingContainer.sizeMult() * 4);
                        x -= lookAngle.x();
                        y -= lookAngle.y();
                        z -= lookAngle.z();
                    } else if (cameraType == CameraType.THIRD_PERSON_FRONT) {
                        lookAngle = pLivingEntity.getLookAngle().scale((swingContainer.sizeMult() * 4.3333) - (swingContainer.sizeMult() - 3));
                        x += lookAngle.x();
                        y += lookAngle.y();
                        z += lookAngle.z();
                    }
                    Vector3d positions = new Vector3d(x, y, z);
                    WANetworkingManager.sendToServer(new UpdateParticlePacket(swingContainer.particleType(), positions.x(), positions.y(), positions.z(), 0, 0, 0));
                }
            }
        }
    }
}