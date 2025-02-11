package net.sashakyotoz.wrathy_armament.items.swords;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.client.particles.parents.options.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.items.SwingParticleHolder;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentSounds;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class Murasama extends SwordLikeItem {
    public static final UUID SWIFTNESS = UUID.fromString("91AEAA56-376B-4498-935B-2F7F68070635");

    public Murasama(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable SwingParticleHolder getSwingHolder(LivingEntity holder, ItemStack stack) {
        return new SwingParticleHolder(ParticleTypes.ENCHANTED_HIT, 1.4f);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        if (!player.isCrouching()) {
//            WaveParticleOption option = new WaveParticleOption(player.getYRot(), 2f, 0.75f, 0, 0);
//            player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
//                    OnActionsTrigger.getXVector(2, player.getYRot()),
//                    OnActionsTrigger.getYVector(1, player.getXRot()),
//                    OnActionsTrigger.getZVector(2, player.getYRot()));
        } else {
            Level level = player.level();
            float scaling = 0;
            double d0 = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
            double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
            for (int i = 0; i < 12 + getCurrentSparkles(stack) * 2; i++) {
                BlockPos pos = level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                if (!level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                    scaling++;
                BlockPos pos1 = level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                if (!ModList.get().isLoaded("physicsmod"))
                    OnActionsTrigger.queueServerWork(20, () -> level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()), pos1.getX(), pos1.getY(), pos1.getZ(), d0, 0.1, d1));
                final Vec3 center = new Vec3(pos1.getX(), pos1.getY(), pos1.getZ());
                List<LivingEntity> entityList = level.getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(3d), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
                for (LivingEntity entity : entityList) {
                    if (entity != player) {
                        entity.hurt(player.damageSources().mobAttack(player), 5 + getCurrentSparkles(stack));
                        entity.setDeltaMovement(new Vec3(
                                OnActionsTrigger.getXVector(1.5f, player.getYRot()),
                                0.75f,
                                OnActionsTrigger.getZVector(1.5f, player.getYRot())));
                        player.swing(player.getMainHandItem().is(stack.getItem()) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
                        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 50, 4));
                        player.teleportTo(entity.getX(), entity.getY(), entity.getZ());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
            playAnimAndEffects(player.level(), player, "zenith_turn", SoundEvents.IRON_GOLEM_ATTACK, null, true);
            OnActionsTrigger.queueServerWork(20, () -> {
                OnActionsTrigger.playerCameraData.computeIfAbsent(player.getStringUUID(), k -> new Triple<>(0, 0, 0));
                OnActionsTrigger.playerCameraData.put(player.getStringUUID(), new Triple<>(
                        OnActionsTrigger.playerCameraData.get(player.getStringUUID()).a,
                        OnActionsTrigger.playerCameraData.get(player.getStringUUID()).b,
                        OnActionsTrigger.playerCameraData.get(player.getStringUUID()).c + 360
                ));
            });
            final Vec3 center = new Vec3(player.getX(), player.getY(), player.getZ());
            List<LivingEntity> entityList = player.level().getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(5d), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
            for (int i = 0; i < 4; i++) {
                for (LivingEntity entity : entityList) {
                    if (entity != player)
                        OnActionsTrigger.queueServerWork(14 * (i + 1), () -> entity.hurt(player.damageSources().mobAttack(player), 6 + getCurrentSparkles(stack)));
                }
                int finalI = i;
                OnActionsTrigger.queueServerWork(14 * (i + 1), () -> player.playSound(WrathyArmamentSounds.KATANA_SWING, 1.5f - (finalI / 10f), 1f));
            }
            player.getCooldowns().addCooldown(stack.getItem(), 45);
        }
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {

    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 12.5 + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -1.8, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SWIFTNESS, "Weapon modifier", stack.getOrCreateTag().getFloat("RestSpeed"), AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!player.isCrouching() && stack.getOrCreateTag().getFloat("RestSpeed") < 0.3)
            stack.getOrCreateTag().putFloat("RestSpeed", stack.getOrCreateTag().getFloat("RestSpeed") + 0.1f);
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add((itemstack.getHoverName().getString().contains("Blue") ? Component.translatable("item.wrathy_armament.game.zenith") : Component.translatable("item.wrathy_armament.game.murasama")).withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.murasama_hint").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.murasama_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int pSlotId, boolean pIsSelected) {
        if (entity.tickCount % 100 == 0) {
            if (stack.getOrCreateTag().getFloat("RestSpeed") >= 0.1f)
                stack.getOrCreateTag().putFloat("RestSpeed", stack.getOrCreateTag().getFloat("RestSpeed") - 0.1f);
            if (stack.getOrCreateTag().getFloat("RestSpeed") < 0)
                stack.getOrCreateTag().putFloat("RestSpeed", 0);
        }
        stack.getOrCreateTag().putDouble("CustomModelData", stack.getHoverName().getString().contains("Muramasa") ? 1 : 0);
    }
}