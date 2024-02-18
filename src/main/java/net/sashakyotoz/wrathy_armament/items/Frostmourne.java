package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentEnchants;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentParticleTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class Frostmourne extends Item {
    public Frostmourne(Properties properties) {
        super(properties);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 19, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.8, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
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
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        if (i < 0)
            return;
        var f = getPowerForTime(i);
        if (!((double) f < 0.75D)) {
            WrathyArmament.LOGGER.debug("Soul Attack");
            entity.playSound(SoundEvents.SOUL_ESCAPE);
            for (int j = 0; j < 3; j++) {
                double d0 = -Mth.sin(entity.getYRot() * ((float) Math.PI / 180F));
                double d1 = Mth.cos(entity.getYRot() * ((float) Math.PI / 180F));
                int tmp;
                int Int = stack.getEnchantmentLevel(WrathyArmamentEnchants.PHANTOM_FURY.get());
                if (Int > 0)
                    tmp = Int;
                else
                    tmp = 1;
                float scaling = 0;
                for (int i1 = 0; i1 < 15 + tmp * 3; i1++) {
                    if (!entity.level().getBlockState(new BlockPos(
                            entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX(),
                            entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY(),
                            entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ())).canOcclude())
                        scaling = scaling + 1;
                    entity.level().addParticle(WrathyArmamentParticleTypes.FROST_SOUL_RAY.get(),
                            (entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
                            (entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
                            (entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()), (d0 + (d0 > 0 ? (j/2.5f)*i1/5f : (-j/2.5f)*i1/5f)),
                            0.1, (d1 + (d1 > 0 ? (j/2.5f)*i1/5f : (-j/2.5f)*i1/5f)));
                    final Vec3 center = new Vec3(
                            (entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getX()),
                            (entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getY()),
                            (entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos().getZ()));
                    List<Entity> entityList = entity.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate((1.5 + tmp + (j/2.5f)*i1/5f) / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(center))).toList();
                    for (Entity entityiterator : entityList) {
                        if (!(entityiterator == entity)) {
                            if (entityiterator instanceof LivingEntity livingEntity) {
                                int damage = Mth.randomBetweenInclusive(RandomSource.create(),5,15);
                                livingEntity.hurt(new DamageSource(livingEntity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC)) {
                                    @Override
                                    public Component getLocalizedDeathMessage(@NotNull LivingEntity _msgEntity) {
                                        return Component.translatable("death.attack.wrathy_armament.frostmourne_message");
                                    }
                                }, damage);
                                WrathyArmament.LOGGER.debug("Damage:" + damage);
                                Player player = (Player) entity;
                                player.getCooldowns().addCooldown(stack.getItem(), Mth.randomBetweenInclusive(RandomSource.create(), 60, 120));
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
    }
    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        entity.startUsingItem(hand);
        return new InteractionResultHolder(InteractionResult.SUCCESS, entity.getItemInHand(hand));
    }
    private static float getPowerForTime(int i) {
        float f = (float) i / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return f;
    }
}
