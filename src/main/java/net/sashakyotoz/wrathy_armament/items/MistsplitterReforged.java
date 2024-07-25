package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.anitexlib.client.particles.parents.types.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;
import org.antlr.v4.runtime.misc.Triple;

import java.util.List;

public class MistsplitterReforged extends SwordLikeItem {
    public MistsplitterReforged(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        Triple<Float, Float, Float> colors;
        switch (stack.getOrCreateTag().getInt("StateIndex")) {
            default -> colors = new Triple<>(1f, 1f, 1f);
            case 0 -> colors = new Triple<>(1f, 0.25f, 0f);
            case 1 -> colors = new Triple<>(0f, 0.15f, 1f);
            case 2 -> colors = new Triple<>(0.15f, 1f, 0.25f);
            case 3 -> colors = new Triple<>(1f, 0f, 0.5f);
        }
        WaveParticleOption option = new WaveParticleOption(player.getYRot(), 2f, colors.a, colors.b, colors.c);
        player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
                OnActionsTrigger.getXVector(2, player.getYRot()),
                OnActionsTrigger.getYVector(1, player.getXRot()),
                OnActionsTrigger.getZVector(2, player.getYRot()));
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(context -> {
            stack.getOrCreateTag().putInt("StateIndex", stack.getOrCreateTag().getInt("StateIndex") + 1 > States.values().length - 1 ? 0 : stack.getOrCreateTag().getInt("StateIndex") + 1);
            player.displayClientMessage(Component.translatable("item.wrathy_armament.abilities." + States.values()[stack.getOrCreateTag().getInt("StateIndex")].stateName), true);
            context.setDefenceType(States.values()[stack.getOrCreateTag().getInt("StateIndex")].stateName);
            OnActionsTrigger.playPlayerAnimation(player.level(),player,"mistsplitter_mode_switch");
        });
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {
        player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(context -> {
            ParticleOptions option;
            switch (context.getDefenceType()) {
                default ->
                        option = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.QUARTZ_BLOCK.defaultBlockState());
                case "fire" ->
                        option = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MAGMA_BLOCK.defaultBlockState());
                case "water" -> option = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.WATER.defaultBlockState());
                case "earth" -> option = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState());
                case "elemental" ->
                        option = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.PURPUR_BLOCK.defaultBlockState());
            }
            OnActionsTrigger.addParticles(option, player.level(),
                    player.getX(), player.getY() + 1, player.getZ(), 2);
            context.setDefenseModeFlag(!context.isDefenseModeOn());
            player.getCooldowns().addCooldown(stack.getItem(), 200);
        });
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 12.5 + stack.getOrCreateTag().getInt("RestPower") + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.wrathy_armament.game.mistsplitter").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.mistsplitter_hint").withStyle(WrathyArmamentItems.PURPLE_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.mistsplitter_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i1, boolean isSelected) {
        if (entity instanceof Player player && player.getMainHandItem().is(stack.getItem())) {
            if (stack.getOrCreateTag().getInt("RestPower") < 5 && player.tickCount % 100 == 0) {
                if (entity.level() instanceof ServerLevel serverLevel) {
                    ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), serverLevel, 0.8f, true, false, 3, WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), "cycle");
                    particleEntity.moveTo(new Vec3(entity.getX(), entity.getY() + 1, entity.getZ()));
                    entity.level().addFreshEntity(particleEntity);
                }
                for (int i = 0; i < 360; i++) {
                    if (i % 20 == 0) {
                        level.addParticle(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), player.getX(), player.getY() + 1, player.getZ(), Math.cos(i) * 0.4d, 0.25d, Math.sin(i) * 0.4d);
                    }
                }
                stack.getOrCreateTag().putInt("RestPower", stack.getOrCreateTag().getInt("RestPower") + 1);
            }
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (stack.getOrCreateTag().getInt("RestPower") > 1)
            stack.getOrCreateTag().putInt("RestPower", stack.getOrCreateTag().getInt("RestPower") - 2);
        return super.onLeftClickEntity(stack, player, entity);
    }

    enum States {
        FIRE("fire"),
        WATER("water"),
        EARTH("earth"),
        ELEMENTAL("elemental"),
        AIR("air");
        final String stateName;

        States(String stateName) {
            this.stateName = stateName;
        }
    }
}