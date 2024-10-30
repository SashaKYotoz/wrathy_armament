package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.sashakyotoz.anitexlib.client.particles.parents.options.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentSounds;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.antlr.v4.runtime.misc.Triple;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class PhantomLancer extends SwordLikeItem {
    public PhantomLancer(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        Triple<Float, Float, Float> colorSet = player.getRandom().nextBoolean() ? new Triple<>(0f, 0f, 1f) : new Triple<>(1f, 0.945f, 0.4f);
        WaveParticleOption option = new WaveParticleOption(player.getYRot(), 3f, colorSet.a, colorSet.b, colorSet.c);
        player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
                OnActionsTrigger.getXVector(1.25f, player.getYRot()),
                OnActionsTrigger.getYVector(1f, player.getXRot()),
                OnActionsTrigger.getZVector(1.25f, player.getYRot()));
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {

    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {

    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 0)
            return;
        var f = OnActionsTrigger.getPowerForTime(i);
        if (!((double) f < 0.75D)) {
            entity.playSound(SoundEvents.PHANTOM_SWOOP);
            double d0 = -Mth.sin(entity.getYRot() * ((float) Math.PI / 180F));
            double d1 = Mth.cos(entity.getYRot() * ((float) Math.PI / 180F));
            int tmp;
            int Int = stack.getEnchantmentLevel(WrathyArmamentMiscRegistries.PHANTOM_FURY.get());
            if (Int > 0)
                tmp = Int;
            else
                tmp = 1;
            float scaling = 0;
            for (int i1 = 0; i1 < 15 + tmp * 3; i1++) {
                BlockPos pos = level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
                if (!level.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).canOcclude())
                    scaling = scaling + 1;
                BlockPos pos1 = level.clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
                level.addParticle(WrathyArmamentMiscRegistries.PHANTOM_RAY.get(), pos1.getX(), pos1.getY(), pos1.getZ(), d0, 0.1, d1);
                final Vec3 center = pos1.getCenter();
                List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate((1.5 + tmp) / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();
                for (Entity entityIterator : entityList) {
                    if (!(entityIterator == entity)) {
                        if (entityIterator instanceof LivingEntity livingEntity) {
                            float damage = livingEntity.getMaxHealth() - livingEntity.getHealth() + 2;
                            if (damage > 100)
                                damage = damage / 4;
                            livingEntity.hurt(new DamageSource(livingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                                @Override
                                public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                                    return Component.translatable("death.attack.wrathy_armament.phantom_shock_message");
                                }
                            }, damage);
                            WrathyArmament.LOGGER.debug("Phantom Lancer damage:{}", damage);
                            if (entity instanceof Player player)
                                player.getCooldowns().addCooldown(stack.getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 60, 140));
                        }
                        if (stack.hurt(1, RandomSource.create(), null)) {
                            stack.shrink(1);
                            stack.setDamageValue(0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.left_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_hint").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_circular_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_hint1").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.phantom_lancer_sweep_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int pRemainingUseDuration) {
        if (entity instanceof Player player && player.tickCount % 15 == 0) {
            int i = this.getUseDuration(stack) - pRemainingUseDuration;
            var f = OnActionsTrigger.getPowerForTime(i);
            if (f < 0.75D)
                OnActionsTrigger.playPlayerAnimation(player.level(), player, "phantom_lancer_swinging");
        }
        super.onUseTick(level, entity, stack, pRemainingUseDuration);
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int i1) {
        if (entity instanceof Player player) {
            final Vec3 center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
            List<Entity> entities = player.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(6 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(entcnd -> entcnd.distanceToSqr(center))).toList();
            for (Entity entityIterator : entities) {
                if (entityIterator != null && entityIterator != entity) {
                    if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(stack.getItem()) && !player.getCooldowns().isOnCooldown(stack.getItem())) {
                        player.playSound(WrathyArmamentSounds.ITEM_LANCER_SHOT);
                        player.swing(InteractionHand.MAIN_HAND);
                        OnActionsTrigger.addParticles(ParticleTypes.SONIC_BOOM, entity.level(), entity.getX(), entity.getY() + 1, entity.getZ(), 1.25f);
                        float damage = player.getHealth() / 2;
                        if (damage <= 0.5)
                            damage = 4;
                        entityIterator.hurt(entityIterator.damageSources().generic(), damage);
                    }
                }
            }
            stack.hurtAndBreak(1,player,(entity1) -> entity1.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return stack.is(WrathyArmamentItems.PHANTOM_LANCER.get());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 7.5 + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }
}