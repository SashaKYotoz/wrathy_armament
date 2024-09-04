package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.client.particles.parents.options.WaveParticleOption;
import net.sashakyotoz.anitexlib.registries.ModParticleTypes;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;

import java.util.List;
import java.util.UUID;

public class HalfZatoichi extends SwordLikeItem {
    private int timer;
    private final UUID speedModifierUUID = UUID.randomUUID();
    private final UUID gravityModifierUUID = UUID.randomUUID();
    public HalfZatoichi(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        WaveParticleOption option = new WaveParticleOption(player.getYRot(), 2f, 1, 1, 1);
        player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
                OnActionsTrigger.getXVector(1, player.getYRot()),
                OnActionsTrigger.getYVector(1, player.getXRot()),
                OnActionsTrigger.getZVector(1, player.getYRot()));
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        if (stack.getOrCreateTag().getInt("charge") > 0) {
            for (int i = -3; i < 3; i++) {
                player.level().addParticle(new ColorableParticleOption("sparkle",0.1f,0.1f,0.1f), player.getX() + i / 2f, player.getY() + 1 + i / 2f, player.getZ() + i / 2f,
                        OnActionsTrigger.getXVector(3, player.getYRot()), 0.125f, OnActionsTrigger.getZVector(3, player.getYRot()));
            }
            player.setDeltaMovement(OnActionsTrigger.getXVector(3, player.getYRot()), 0.125f, OnActionsTrigger.getZVector(3, player.getYRot()));
            stack.getOrCreateTag().putInt("charge", stack.getOrCreateTag().getInt("charge") - 1);
            player.getCooldowns().addCooldown(stack.getItem(), 100);
        }
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {
        player.getCapability(ModCapabilities.HALF_ZATOICHI_ABILITIES).ifPresent(context -> {
            if (stack.getOrCreateTag().getInt("charge") > 2){
                OnActionsTrigger.addParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, player.level(),
                        player.getX(), player.getY() + 1, player.getZ(), 2);
                stack.getOrCreateTag().putBoolean("InAdrenalinMode", !context.isInAdrenalinMode());
                context.setInAdrenalinMode(!context.isInAdrenalinMode());
                if(!context.isInAdrenalinMode())
                    player.heal(1);
                else
                    stack.getOrCreateTag().putInt("charge",stack.getOrCreateTag().getInt("charge")-2);
                player.getCooldowns().addCooldown(stack.getItem(), 200);
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean variable) {
        int charge = stack.getOrCreateTag().getInt("charge");
        if (entity instanceof Player player) {
            if (player.getMainHandItem().is(WrathyArmamentItems.HALF_ZATOICHI.get()))
                timer++;
            if (timer > 5 && charge > 0 && !player.getMainHandItem().is(WrathyArmamentItems.HALF_ZATOICHI.get())) {
                player.hurt(player.damageSources().generic(), 2 + charge);
                timer = 0;
                stack.getOrCreateTag().putInt("charge", 0);
            }
        }
        stack.getOrCreateTag().putDouble("CustomModelData",charge > 0 ? 1 : 0);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (stack.getOrCreateTag().getInt("charge") < 7 + stack.getOrCreateTag().getInt("Sparkles"))
            stack.getOrCreateTag().putInt("charge", stack.getOrCreateTag().getInt("charge") + 1);
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 10.5 + stack.getOrCreateTag().getInt("Sparkles") / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.2, AttributeModifier.Operation.ADDITION));
            if (stack.getOrCreateTag().getBoolean("InAdrenalinMode")){
                builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(speedModifierUUID, "Weapon modifier", 0.2 + stack.getOrCreateTag().getInt("Sparkles")/20f, AttributeModifier.Operation.ADDITION));
                if (ForgeMod.ENTITY_GRAVITY.isPresent())
                    builder.put(ForgeMod.ENTITY_GRAVITY.get(),new AttributeModifier(gravityModifierUUID,"Weapon Modifier",-0.02f, AttributeModifier.Operation.ADDITION));
            }
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        MutableComponent charge = Component.translatable("item.wrathy_armament.half_zatoichi_charge");
        charge.withStyle(WrathyArmamentItems.TITLE_FORMAT);
        charge.append(CommonComponents.SPACE).append(Component.translatable("" + itemstack.getOrCreateTag().getInt("charge")));
        list.add(Component.translatable("item.wrathy_armament.game.half_zatoichi").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.half_zatoichi_hint").withStyle(WrathyArmamentItems.GOLD_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.half_zatoichi_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(charge);
    }
}