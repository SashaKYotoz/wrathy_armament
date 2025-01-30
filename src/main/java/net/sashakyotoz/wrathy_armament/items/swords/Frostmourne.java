package net.sashakyotoz.wrathy_armament.items.swords;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.items.SwingParticleHolder;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class Frostmourne extends SwordLikeItem {
    public Frostmourne(Properties properties) {
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
        if (stack.getOrCreateTag().getInt("charge") > 1 && !player.getCooldowns().isOnCooldown(stack.getItem()) && !player.isUsingItem() && player.level() instanceof ServerLevel serverLevel) {
            stack.getOrCreateTag().putInt("charge", stack.getOrCreateTag().getInt("charge") - 1);
            LichMyrmidon lichMyrmidon = new LichMyrmidon(WrathyArmamentEntities.LICH_MYRMIDON.get(), serverLevel);
            lichMyrmidon.moveTo(player.getX(), player.getY() + 1, player.getZ());
            lichMyrmidon.setHaveToFindOwner(true);
            serverLevel.addFreshEntity(lichMyrmidon);
            player.playSound(SoundEvents.SOUL_ESCAPE,1.5f,0.9f);
            player.getCooldowns().addCooldown(stack.getItem(), 30);
        }
    }

    @Override
    public @Nullable SwingParticleHolder getSwingHolder(LivingEntity holder, ItemStack stack) {
        return new SwingParticleHolder(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), 1.1f);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 15.5 + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        MutableComponent charge = Component.translatable("item.wrathy_armament.frostmourne_charge");
        charge.withStyle(WrathyArmamentItems.TITLE_FORMAT);
        charge.append(CommonComponents.SPACE).append(Component.translatable("" + itemstack.getOrCreateTag().getInt("charge")));
        list.add(Component.translatable("item.wrathy_armament.game.frostmourne").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.frostmourne_hint").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.frostmourne_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.frostmourne_hint1").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.frostmourne_attack1").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(charge);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity pLivingEntity, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 0)
            return;
        var f = OnActionsTrigger.getPowerForTime(i);
        if (!pLivingEntity.isShiftKeyDown()) {
            if (f > 0.9D) {
                pLivingEntity.playSound(SoundEvents.SOUL_ESCAPE,1.5f, 1.5f);
                if (level instanceof ServerLevel serverLevel) {
                    ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), serverLevel, 0.6f, true, false, 4,
                            ParticleTypes.SOUL, "rain");
                    particleEntity.setOwner(pLivingEntity);
                    particleEntity.moveTo(new Vec3(pLivingEntity.getX(), pLivingEntity.getY() + 1.5f, pLivingEntity.getZ()));
                    level.addFreshEntity(particleEntity);
                }
            } else {
                if (pLivingEntity instanceof Player player && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                    playAnimAndEffects(player.level(), player, "frostmourne_somersault", SoundEvents.GOAT_LONG_JUMP, null, true);
                    OnActionsTrigger.playerCameraData.computeIfAbsent(player.getStringUUID(), k -> new Triple<>(0, 0, 0));
                    OnActionsTrigger.playerCameraData.put(player.getStringUUID(), new Triple<>(
                            OnActionsTrigger.playerCameraData.get(player.getStringUUID()).a,
                            OnActionsTrigger.playerCameraData.get(player.getStringUUID()).b + 180,
                            OnActionsTrigger.playerCameraData.get(player.getStringUUID()).c
                    ));
                    OnActionsTrigger.queueServerWork(8, () -> player.setDeltaMovement(0f, 0.5f, 0f));
                    OnActionsTrigger.queueServerWork(20, () -> {
                        float scaling = 0;
                        double d0 = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
                        double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
                        for (int j = 0; j < 4 + getCurrentSparkles(stack) * 2; j++) {
                            BlockPos pos = level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                            if (!level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                                scaling++;
                            BlockPos pos1 = level.clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player)).getBlockPos();
                            if (!ModList.get().isLoaded("physicsmod")) {
                                for (int k = -2; k < 3; k++) {
                                    for (int l = -1; l < 1; l++) {
                                        level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(pos1.below(2))), pos1.getX() + k / 10f, pos1.getY() + l / 10f, pos1.getZ() + k / 10f, d0, 0.1 + l / 10f, d1);
                                    }
                                }
                            }
                            final Vec3 center = new Vec3(pos1.getX(), pos1.getY(), pos1.getZ());
                            List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(3d), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
                            for (Entity entity : entityList) {
                                if (entity != player) {
                                    if (entity instanceof LivingEntity livingEntity) {
                                        livingEntity.hurt(player.damageSources().mobAttack(player), 8 + getCurrentSparkles(stack));
                                        livingEntity.setTicksFrozen(200 + getCurrentSparkles(stack) * 20);
                                    }
                                }
                            }
                        }
                        OnActionsTrigger.addParticlesWithDelay(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(player.getOnPos().below(2))), level, player.getX(), player.getY() + 1.5f, player.getZ(), 3, 7);
                    });
                    player.getCooldowns().addCooldown(stack.getItem(), 70 - this.getCurrentSparkles(stack) * 5);
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 32000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);
        return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int pRemainingUseDuration) {
        if (pRemainingUseDuration % 4 == 0 && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(),
                    entity.getX() + OnActionsTrigger.getXVector(-2, entity.getYRot()),
                    entity.getY() + 2,
                    entity.getZ() + OnActionsTrigger.getZVector(-2, entity.getYRot()),
                    9,
                    OnActionsTrigger.getXVector(2, entity.getYRot()),
                    -0.25f,
                    OnActionsTrigger.getZVector(2, entity.getYRot()),
                    0.5f
            );
        }
        if (entity instanceof Player player && player.tickCount % 20 == 0) {
            player.playSound(SoundEvents.SOUL_ESCAPE, 1.8f, 1);
            int i = this.getUseDuration(stack) - pRemainingUseDuration;
            var f = OnActionsTrigger.getPowerForTime(i);
            if (!entity.isShiftKeyDown()) {
                if (f > 0.9D)
                    playAnimAndEffects(player.level(), player, "rain_casting", SoundEvents.SOUL_ESCAPE, null, false);
            }
        }
        super.onUseTick(level, entity, stack, pRemainingUseDuration);
    }
}