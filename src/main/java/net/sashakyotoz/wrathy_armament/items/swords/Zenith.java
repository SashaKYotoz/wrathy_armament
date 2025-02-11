package net.sashakyotoz.wrathy_armament.items.swords;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.client.particles.parents.options.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;
import net.sashakyotoz.wrathy_armament.items.SwingParticleHolder;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Zenith extends SwordLikeItem {
    private int timer;

    public Zenith(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {

    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        if (player.level() instanceof ServerLevel serverLevel && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            if (stack.getOrCreateTag().getInt("ZenithIndex") > 2) {
                stack.getOrCreateTag().putDouble("CustomModelData", 0);
                stack.getOrCreateTag().putInt("ZenithIndex", 0);
                player.getCooldowns().addCooldown(stack.getItem(), 60);
            } else {
                zenithAbility(player, stack, serverLevel);
                stack.getOrCreateTag().putDouble("CustomModelData", 1);
                stack.getOrCreateTag().putInt("ZenithIndex", stack.getOrCreateTag().getInt("ZenithIndex") + 1);
                timer = 30;
                player.getCooldowns().addCooldown(stack.getItem(), 10);
            }
        }
        playAnimAndEffects(player.level(), player, "zenith_in_hand_rolling",SoundEvents.BEACON_POWER_SELECT,null,true);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            if (timer > 0)
                timer--;
            if (player.getInventory().contains(new ItemStack(WrathyArmamentItems.ZENITH.get()))) {
                ItemStack stack = player.getInventory().getItem(player.getInventory().findSlotMatchingItem(new ItemStack(WrathyArmamentItems.ZENITH.get())));
                if (stack.getOrCreateTag().getDouble("CustomModelData") == 1 && timer <= 0)
                    stack.getOrCreateTag().putDouble("CustomModelData", 0);
            }
        }
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {

    }

    @Override
    public @Nullable SwingParticleHolder getSwingHolder(LivingEntity holder, ItemStack stack) {
        return new SwingParticleHolder(WrathyArmamentMiscRegistries.ZENITH_WAY.get(), 1.6f);
    }

    private void zenithAbility(Player player, ItemStack stack, ServerLevel level) {
//        WrathyArmament.LOGGER.debug("Zenith index: {} \r", stack.getOrCreateTag().getInt("ZenithIndex"));
        ZenithEntity zenith = new ZenithEntity(level, player, stack.getOrCreateTag().getInt("ZenithIndex"));
        zenith.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
        level.addFreshEntity(zenith);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 15 + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        itemstack.getOrCreateTag().putDouble("CustomModelData", 0);
        list.add(Component.translatable("item.wrathy_armament.game.zenith").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.zenith_hint").withStyle(WrathyArmamentItems.PURPLE_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.zenith_ability").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
    }
}