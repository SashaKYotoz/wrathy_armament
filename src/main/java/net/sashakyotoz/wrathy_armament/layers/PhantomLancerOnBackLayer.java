package net.sashakyotoz.wrathy_armament.layers;

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
import net.sashakyotoz.wrathy_armament.client.models.technical.LancerBackModel;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentItems;

public class PhantomLancerOnBackLayer <T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation LANCER_LOCATION = new ResourceLocation("wrathy_armament:textures/item/phantom_lancer.png");
    private final LancerBackModel<T> lancerBackModel;

    public PhantomLancerOnBackLayer(RenderLayerParent<T, M> layerParent, EntityModelSet modelSet) {
        super(layerParent);
        this.lancerBackModel = new LancerBackModel<>(modelSet.bakeLayer(LancerBackModel.LAYER_LOCATION));
    }

    public void render(PoseStack stack, MultiBufferSource bufferSource, int p_116953_, T entity, float p_116955_, float p_116956_, float p_116957_, float p_116958_, float p_116959_, float p_116960_) {
        ItemStack itemstack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(itemstack, entity)) {
            ResourceLocation resourcelocation;
            resourcelocation = getElytraTexture(itemstack, entity);
            stack.pushPose();
            stack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.lancerBackModel);
            this.lancerBackModel.setupAnim(entity, p_116955_, p_116956_, p_116958_, p_116959_, p_116960_);
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferSource, RenderType.entityCutoutNoCull(resourcelocation), false, itemstack.hasFoil());
            this.lancerBackModel.renderToBuffer(stack, vertexconsumer, p_116953_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            stack.popPose();
        }
    }

    public boolean shouldRender(ItemStack stack, T entity) {
        Player player = (Player) entity;
        if((player.getInventory().contains(new ItemStack(WrathyArmamentItems.PHANTOM_LANCER.get())) && (!player.getItemInHand(InteractionHand.OFF_HAND).is(WrathyArmamentItems.PHANTOM_LANCER.get()) && !player.getItemInHand(InteractionHand.MAIN_HAND).is(WrathyArmamentItems.PHANTOM_LANCER.get()))) && (!stack.isEmpty() && Config.SHOW_IF_ARMOR_EQUIP.get() || stack.isEmpty()))
            return Config.SHOW_LANCER_ON_PLAYER_BACK.get();
        else
            return false;
    }

    public ResourceLocation getElytraTexture(ItemStack stack, T entity) {
        return LANCER_LOCATION;
    }
}
