package net.sashakyotoz.wrathy_armament.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.Config;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.LancerBackModel;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

public class PhantomLancerOnBackLayer <T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation LANCER_LOCATION = WrathyArmament.createWALocation("textures/item/phantom_lancer.png");
    private final LancerBackModel<T> lancerBackModel;

    public PhantomLancerOnBackLayer(RenderLayerParent<T, M> layerParent, EntityModelSet modelSet) {
        super(layerParent);
        this.lancerBackModel = new LancerBackModel<>(modelSet.bakeLayer(LancerBackModel.LAYER_LOCATION));
    }

    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        ItemStack itemstack = pLivingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemstack, pLivingEntity)) {
            ResourceLocation resourcelocation;
            resourcelocation = getLancerTexture();
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.lancerBackModel);
            this.lancerBackModel.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(pBuffer, RenderType.entitySmoothCutout(resourcelocation), false, itemstack.hasFoil());
            this.lancerBackModel.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();
        }
    }

    public boolean shouldRender(ItemStack stack, T entity) {
        Player player = (Player) entity;
        if((player.getInventory().contains(new ItemStack(WrathyArmamentItems.PHANTOM_LANCER.get())) && (!player.getItemInHand(InteractionHand.OFF_HAND).is(WrathyArmamentItems.PHANTOM_LANCER.get()) && !player.getItemInHand(InteractionHand.MAIN_HAND).is(WrathyArmamentItems.PHANTOM_LANCER.get()))) && (!stack.isEmpty() && Config.SHOW_IF_ARMOR_EQUIP.get() || stack.isEmpty()))
            return Config.SHOW_LANCER_ON_PLAYER_BACK.get();
        else
            return false;
    }

    public ResourceLocation getLancerTexture() {
        return LANCER_LOCATION;
    }
}
