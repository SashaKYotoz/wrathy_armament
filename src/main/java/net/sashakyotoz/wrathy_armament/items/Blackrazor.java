package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.sashakyotoz.wrathy_armament.client.particles.options.CapturedSoulParticleOption;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

import java.util.List;

public class Blackrazor extends SwordLikeItem {
    public Blackrazor(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {

    }

    @Override
    public void rightClick(Player player, ItemStack stack) {

    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {
        if (player.getMainHandItem().getOrCreateTag().getInt("charge") > 1) {
            player.getMainHandItem().getOrCreateTag().putInt("charge", player.getMainHandItem().getOrCreateTag().getInt("charge") - 1);
            player.playSound(SoundEvents.FOX_EAT, 2, player.getRandom().nextIntBetweenInclusive(15, 35) * 0.1f);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1));
            player.getCooldowns().addCooldown(stack.getItem(), 300);
            CapturedSoulParticleOption option = new CapturedSoulParticleOption(new EntityPositionSource(player, 10), 50, 0.25f, 0.25f, 0.25f);
            if (player.level() instanceof ServerLevel level)
                level.sendParticles(option, player.getX(), player.getY(), player.getZ(), 3, 0, 0, 0, 1);
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 12 + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof LivingEntity entity && entity.tickCount % 20 == 0) {
            if (pStack.getOrCreateTag().getInt("hungryTimer") > 0)
                pStack.getOrCreateTag().putInt("hungryTimer", pStack.getOrCreateTag().getInt("hungryTimer") - 1);
            else {
                if (pStack.getOrCreateTag().getInt("charge") > 2) {
                    pStack.getOrCreateTag().putInt("charge", pStack.getOrCreateTag().getInt("charge") - 2);
                    pStack.getOrCreateTag().putInt("hungryTimer", 180);
                } else {
                    entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 2));
                    entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 2));
                    entity.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
                    pStack.getOrCreateTag().putInt("hungryTimer", 180);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        MutableComponent charge = Component.translatable("item.wrathy_armament.frostmourne_charge");
        charge.withStyle(WrathyArmamentItems.TITLE_FORMAT);
        charge.append(CommonComponents.SPACE).append(Component.translatable("" + itemstack.getOrCreateTag().getInt("charge")));
        MutableComponent hungryTimer = Component.translatable("item.wrathy_armament.blackrazor_hungry_timer");
        hungryTimer.withStyle(WrathyArmamentItems.TITLE_FORMAT);
        hungryTimer.append(CommonComponents.SPACE).append(Component.translatable("" + itemstack.getOrCreateTag().getInt("hungryTimer")));
        list.add(Component.translatable("item.wrathy_armament.game.blackrazor").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.left_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.blackrazor_hint").withStyle(WrathyArmamentItems.PURPLE_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.blackrazor_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(charge);
        list.add(CommonComponents.EMPTY);
        list.add(hungryTimer);
    }
}