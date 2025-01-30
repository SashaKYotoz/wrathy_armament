package net.sashakyotoz.wrathy_armament.items.swords;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.anitexlib.client.particles.parents.options.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.client.particles.options.FireSphereParticleOption;
import net.sashakyotoz.wrathy_armament.entities.technical.BladeOfChaosEntity;
import net.sashakyotoz.wrathy_armament.items.SwingParticleHolder;
import net.sashakyotoz.wrathy_armament.items.SwordLikeItem;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class BladeOfChaos extends SwordLikeItem {
    public BladeOfChaos(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {

    }

    @Override
    public void rightClick(Player player, ItemStack stack) {
        ItemStack stackInMainHand = player.getMainHandItem();
        ItemStack stackInOffHand = player.getOffhandItem();
        if (stackInMainHand.getItem() instanceof BladeOfChaos && stackInOffHand.getItem() instanceof BladeOfChaos && !player.level().isClientSide()) {
            float f = Mth.cos((player.getId() * 3 + player.tickCount) * 7.45F * ((float) Math.PI / 180F) + (float) Math.PI);
            int i = 1;
            float f2 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F)) * (1.2F + 0.25F * (float) i);
            float f3 = Mth.sin(player.getYRot() * ((float) Math.PI / 180F)) * (1.2F + 0.25F * (float) i);
            float f4 = (0.3F + f * 0.45F) * ((float) i * 0.2F + 1.0F);
            String behavior = BladeOfChaosEntity.PossibleBehavior.values()[stack.getOrCreateTag().getInt("StateIndex")].behaviorName;
            BladeOfChaosEntity blade = new BladeOfChaosEntity(player.level(), stackInMainHand, player, behavior);
            BladeOfChaosEntity blade1 = new BladeOfChaosEntity(player.level(), stackInOffHand, player, behavior);
            player.getCooldowns().addCooldown(stack.getItem(),10);
            OnActionsTrigger.queueServerWork(10,()->{
                switch (behavior) {
                    case "NEMEAN_CRUSH" -> {
                            OnActionsTrigger.playPlayerAnimation(player.level(),player, "nemean_crush",true);
                        blade.setDeltaMovement(OnActionsTrigger.getXVector(4, player.getYRot() - 22.5), OnActionsTrigger.getYVector(2, player.getXRot()),
                                OnActionsTrigger.getZVector(4, player.getYRot() - 22.5));
                        blade1.setDeltaMovement(OnActionsTrigger.getXVector(4, player.getYRot() + 22.5), OnActionsTrigger.getYVector(2, player.getXRot()),
                                OnActionsTrigger.getZVector(4, player.getYRot() + 22.5));
                        player.getCooldowns().addCooldown(stack.getItem(),10);
                    }
                    case "CYCLONE_OF_CHAOS" -> {
                        blade.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3, 0.5F);
                        blade1.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3, 0.5F);
                    }
                    case "WRATH_OF_ARTEMIS" -> {
                        blade.setDeltaMovement(f2 + OnActionsTrigger.getXVector(2, player.getYRot()), f4 + OnActionsTrigger.getYVector(3, player.getXRot()),
                                f3 + OnActionsTrigger.getZVector(2, player.getYRot()));
                        blade1.setDeltaMovement(-f2 + OnActionsTrigger.getXVector(2, player.getYRot()), f4 + OnActionsTrigger.getYVector(3, player.getXRot()),
                                -f3 + OnActionsTrigger.getZVector(2, player.getYRot()));
                    }
                }
                player.level().addFreshEntity(blade);
                player.level().addFreshEntity(blade1);
                player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                player.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            });
        }
    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {

    }

    @Override
    public @Nullable SwingParticleHolder getSwingHolder(LivingEntity holder, ItemStack stack) {
        return new SwingParticleHolder(ParticleTypes.FLAME,1.25f);
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 32000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        return new InteractionResultHolder(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    private void spawnLavaEffect(Level level, double x, double y, double z) {
        OnActionsTrigger.addParticles(ParticleTypes.LAVA, level, x, y + 1, z, 1.5f);
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity.isCrouching()) {
            int i = this.getUseDuration(pStack) - pTimeCharged;
            if (i < 0 || !(pLivingEntity.getMainHandItem().getItem() instanceof BladeOfChaos && pLivingEntity.getOffhandItem().getItem() instanceof BladeOfChaos))
                return;
            var f = OnActionsTrigger.getPowerForTime(i);
            if (f > 0.9 && pLivingEntity.getMainHandItem().is(WrathyArmamentItems.BLADE_OF_CHAOS.get()) &&
                    pLivingEntity.getOffhandItem().is(WrathyArmamentItems.BLADE_OF_CHAOS.get()) && pLivingEntity instanceof Player player) {
                pLevel.addParticle(new FireSphereParticleOption(3 * f, true, false, 1, 1, 1),
                        player.getX() + OnActionsTrigger.getXVector(0.4f, player.getYRot()), player.getY() - 0.5f, player.getZ() + OnActionsTrigger.getZVector(0.4f, player.getYRot()), 0, 0, 0);
                OnActionsTrigger.queueServerWork(15, () -> {
                    player.setDeltaMovement(0, 0.5f, 0);
                    spawnLavaEffect(pLevel, player.getX(), player.getY(), player.getZ());
                    playAnimAndEffects(player.level(),player, "cyclone_ability",SoundEvents.LAVA_EXTINGUISH,null,true);
                    final Vec3 center = new Vec3(player.getX(), player.getY(), player.getZ());
                    List<Entity> entities = player.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(9 + f), e -> true).stream().sorted(Comparator.comparingDouble(entcnd -> entcnd.distanceToSqr(center))).toList();
                    for (Entity entityIterator : entities) {
                        if (entityIterator != null && entityIterator != pLivingEntity) {
                            if (player.getItemBySlot(EquipmentSlot.MAINHAND).is(pStack.getItem()) && !player.getCooldowns().isOnCooldown(pStack.getItem())) {
                                player.playSound(SoundEvents.ENDER_DRAGON_SHOOT);
                                player.swing(InteractionHand.MAIN_HAND);
                                player.swing(InteractionHand.OFF_HAND);
                                entityIterator.hurt(entityIterator.damageSources().generic(), 15 + getCurrentSparkles(pStack));
                                entityIterator.setSecondsOnFire(10 + getCurrentSparkles(pStack));
                                entityIterator.setDeltaMovement(new Vec3(
                                        -OnActionsTrigger.getXVector(2, entityIterator.getYRot()),
                                        1,
                                        -OnActionsTrigger.getZVector(2, entityIterator.getYRot())));
                            }
                        }
                    }
                    player.getCooldowns().addCooldown(player.getMainHandItem().getItem(), (int) ((40 - getCurrentSparkles(player.getMainHandItem())) * f));
                    player.getCooldowns().addCooldown(player.getOffhandItem().getItem(), (int) ((40 - getCurrentSparkles(player.getOffhandItem())) * f));
                });
            }
            if (f < 0.8) {
                if (pLivingEntity instanceof Player player) {
                    pStack.getOrCreateTag().putInt("StateIndex", pStack.getOrCreateTag().getInt("StateIndex") + 1 > BladeOfChaosEntity.PossibleBehavior.values().length - 1 ? 0 : pStack.getOrCreateTag().getInt("StateIndex") + 1);
                    player.displayClientMessage(Component.translatable("item.wrathy_armament.abilities." + BladeOfChaosEntity.PossibleBehavior.values()[pStack.getOrCreateTag().getInt("StateIndex")].behaviorName), true);
                }
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity.isCrouching()) {
            if (pLivingEntity.tickCount % 4 == 0) {
                float f = Mth.cos((pLivingEntity.getId() * 3 + pLivingEntity.tickCount) * 7.45F * ((float) Math.PI / 180F) + (float) Math.PI);
                int i = 2;
                float f2 = Mth.cos(pLivingEntity.getYRot() * ((float) Math.PI / 180F)) * (1.1F + 0.21F * (float) i);
                float f3 = Mth.sin(pLivingEntity.getYRot() * ((float) Math.PI / 180F)) * (1.1F + 0.21F * (float) i);
                float f4 = (0.3F + f * 0.45F) * ((float) i * 0.2F + 1.0F);
                pLevel.addParticle(ParticleTypes.LAVA, pLivingEntity.getX() + f2, pLivingEntity.getY() + f4, pLivingEntity.getZ() + f3, 0.0D, 0.0D, 0.0D);
                pLevel.addParticle(ParticleTypes.LAVA, pLivingEntity.getX() - f2, pLivingEntity.getY() + f4, pLivingEntity.getZ() - f3, 0.0D, 0.0D, 0.0D);
            }
            if (pLivingEntity.tickCount % 10 == 0 && pLivingEntity.getMainHandItem().is(WrathyArmamentItems.BLADE_OF_CHAOS.get()) &&
                    pLivingEntity.getOffhandItem().is(WrathyArmamentItems.BLADE_OF_CHAOS.get())) {
                pLevel.addParticle(new FireSphereParticleOption(1.5f, true, false, 1, 1, 1),
                        pLivingEntity.getX(), pLivingEntity.getY() + 1.5f, pLivingEntity.getZ(), 0, 0, 0);
                pLivingEntity.setDeltaMovement(OnActionsTrigger.getXVector(0.4f, pLivingEntity.getYRot()), 0.4f, OnActionsTrigger.getZVector(0.4f, pLivingEntity.getYRot()));
            }
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getAttributeModifiers(slot, stack));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 14 + getCurrentSparkles(stack) / 2f, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.game.blade_of_chaos").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.blade_of_chaos_hint").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.blade_of_chaos_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.blade_of_chaos_hint1").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.blade_of_chaos_attack1").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }
}