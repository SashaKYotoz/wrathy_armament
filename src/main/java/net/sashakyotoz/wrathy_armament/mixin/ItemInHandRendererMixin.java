package net.sashakyotoz.wrathy_armament.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.items.Murasama;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    @Inject(method = "renderItem",at=@At("HEAD"))
    public void renderItems(LivingEntity pEntity, ItemStack pItemStack, ItemDisplayContext pDisplayContext, boolean pLeftHand, PoseStack pPoseStack, MultiBufferSource pBuffer, int pSeed, CallbackInfo ci){
        ItemStack stackInMainHand = pEntity.getMainHandItem();
        if (stackInMainHand.is(WrathyArmamentItems.BLADE_OF_CHAOS.get())
                && pEntity.getOffhandItem().is(WrathyArmamentItems.BLADE_OF_CHAOS.get())
                && pEntity.getTicksUsingItem() > 10 && pEntity.isCrouching()) {
            pPoseStack.mulPose(Axis.XP.rotationDegrees(200));
            if (pDisplayContext.firstPerson())
                pPoseStack.translate(0,1,1);
            else
                pPoseStack.translate(0,0.25,0);
        }
    }
}