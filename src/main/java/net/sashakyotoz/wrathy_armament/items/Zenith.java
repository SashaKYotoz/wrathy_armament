package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.sashakyotoz.anitexlib.client.particles.parents.types.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.antlr.v4.runtime.misc.Triple;

import java.util.List;

public class Zenith extends SwordLikeItem {
    private int timer;
    private int zenithIndex = 0;

    public Zenith(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        Triple<Float,Float,Float> colorSet = player.getRandom().nextBoolean() ? new Triple<>(0.25f,1f,0.25f) : new Triple<>(0.9f,0f,0.9f);
        WaveParticleOption option = new WaveParticleOption(player.getYRot(), 2.5f, colorSet.a, colorSet.b, colorSet.c);
        player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
                OnActionsTrigger.getXVector(1, player.getYRot()),
                OnActionsTrigger.getYVector(1, player.getXRot()),
                OnActionsTrigger.getZVector(1, player.getYRot()));
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        if (player.level() instanceof ServerLevel serverLevel && !player.getCooldowns().isOnCooldown(stack.getItem())) {
            zenithAbility(player, serverLevel);
            if (zenithIndex > 3) {
                stack.getOrCreateTag().putDouble("CustomModelData", 0);
                player.getCooldowns().addCooldown(stack.getItem(), 60);
            } else {
                zenithIndex++;
                timer = 30;
                stack.getOrCreateTag().putDouble("CustomModelData", zenithIndex > 0 ? 1 : 0);
                player.getCooldowns().addCooldown(stack.getItem(), 10);
            }
        }
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

    private void zenithAbility(Player player, ServerLevel level) {
        if (zenithIndex > 3)
            zenithIndex = 0;
        else
            zenithIndex++;
        WrathyArmament.LOGGER.debug("{} \r", zenithIndex);
        ZenithEntity zenith = new ZenithEntity(level, player, zenithIndex);
        zenith.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
        level.addFreshEntity(zenith);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 17.5, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.8, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        itemstack.getOrCreateTag().putDouble("CustomModelData", 0);
        list.add(Component.translatable("item.wrathy_armament.game.zenith").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.zenith_hint").withStyle(WrathyArmamentItems.PURPLE_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.zenith_ability").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
    }
}
