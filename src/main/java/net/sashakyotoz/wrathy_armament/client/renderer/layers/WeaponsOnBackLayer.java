package net.sashakyotoz.wrathy_armament.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.anitexlib.utils.TextureAnimator;
import net.sashakyotoz.wrathy_armament.Config;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.technical.TransparentHumanoidLayerModel;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.RenderUtils;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;
import org.antlr.v4.runtime.misc.Triple;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

public class WeaponsOnBackLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final EntityRendererProvider.Context wrathy_armament$context;

    public WeaponsOnBackLayer(RenderLayerParent<T, M> pRenderer, EntityRendererProvider.Context context) {
        super(pRenderer);
        this.wrathy_armament$context = context;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity instanceof Player player) {
            if (Config.Client.SHOW_WEAPON_ON_PLAYER_BACK.get() && wrathy_armament$checkWeapon(player)) {
                if (Config.Client.SHOW_WEAPON_IF_ARMOR_EQUIP.get() || !player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
                    List<ItemStack> weaponsToShow = getWrathyArmamentWeapons(player);
                    HashMap<Integer, Pair<Float, Float>> weaponsLocation = new HashMap<>();
                    weaponsLocation.put(0, new Pair<>(0f, 0f));
                    weaponsLocation.put(1, new Pair<>(1f, 0f));
                    weaponsLocation.put(2, new Pair<>(0f, 1f));
                    weaponsLocation.put(3, new Pair<>(-1f, 0f));
                    weaponsLocation.put(4, new Pair<>(0f, -1f));
                    weaponsToShow.forEach(stack -> {
                        int stackIndex = weaponsToShow.indexOf(stack);
                        float offsetY = (RenderUtils.getOscillatingWithNegativeValue(player.tickCount, 6)) / 4;
                        pPoseStack.pushPose();
                        pPoseStack.scale(0.75f, 0.75f, 0.75f);
                        pPoseStack.mulPose(Axis.ZP.rotationDegrees(135));
                        Pair<Float, Float> offsetToApply = weaponsLocation.get(stackIndex) == null ? new Pair<>(0f, 0.25f + offsetY) : weaponsLocation.get(stackIndex);
                        pPoseStack.translate(offsetToApply.getFirst() + offsetY, offsetToApply.getSecond() + offsetY, 0.75f);
                        wrathy_armament$context.getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, player.level(), 1);
                        pPoseStack.popPose();
                    });
                }
            }
        }
    }

    private List<ItemStack> getWrathyArmamentWeapons(Player player) {
        List<ItemStack> weapons = player.getInventory().items.stream().filter(stack -> stack.getItem() instanceof SwordLikeItem
                && !(player.getItemInHand(InteractionHand.MAIN_HAND).equals(stack)
                || player.getItemInHand(InteractionHand.OFF_HAND).equals(stack))).toList();
        ArrayList<ItemStack> weaponsArray = new ArrayList<>(weapons);
        if (weaponsArray.size() > 5) {
            weaponsArray.sort(Comparator.comparingInt(stack -> stack.getOrCreateTag().getInt("Sparkles")));
            weaponsArray = new ArrayList<>(weaponsArray.subList(weaponsArray.size() - 5, weaponsArray.size()));
        }
        return Config.Client.SHOW_ALL_WEAPONS_ON_PLAYER_BACK.get() ? weaponsArray : (weaponsArray.isEmpty() ? List.of(ItemStack.EMPTY) : List.of(weapons.get(0)));
    }

    private boolean wrathy_armament$checkWeapon(Player player) {
        return player.getInventory().hasAnyMatching(stack -> stack.getItem() instanceof SwordLikeItem);
    }
}