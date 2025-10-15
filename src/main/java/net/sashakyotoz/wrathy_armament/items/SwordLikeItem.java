package net.sashakyotoz.wrathy_armament.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.anitexlib.client.renderer.IParticleItem;
import net.sashakyotoz.wrathy_armament.Config;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.capabilities.items.XPTiers;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SwordLikeItem extends Item implements Vanishable, IParticleItem {
    public SwordLikeItem(Properties properties) {
        super(properties);
    }

    public void rightClickBlock(Player player, ItemStack stack) {
    }

    public abstract void leftClickAttack(Player player, ItemStack stack);

    public abstract void rightClick(Player player, ItemStack stack);

    public abstract void rightClickOnShiftClick(Player player, ItemStack stack);

    public boolean canAttackBlock(BlockState state, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(2, entity, (entity1) -> entity1.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    public boolean isCorrectToolForDrops(BlockState state) {
        return state.is(Blocks.COBWEB);
    }

    public abstract @Nullable SwingParticleHolder getSwingHolder(LivingEntity holder, ItemStack stack);

    public boolean hurtEnemy(ItemStack stack, LivingEntity livingEntity, LivingEntity entity) {
        stack.hurtAndBreak(1, entity, (entity1) -> entity1.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        if (getCurrentSparkles(stack) < 6 && getStoredXP(stack) < XPTiers.XP_FOR_FIFTH_TIER.getNeededXP() * 1.5f)
            setStoredXP(stack, getStoredXP(stack) + entity.getRandom().nextInt(1, 11));
        return true;
    }

    public void playAnimAndEffects(Level level, Player player, String animName, SoundEvent event, @Nullable ParticleOptions options, boolean flag) {
        OnActionsTrigger.playPlayerAnimation(level, player, animName, flag);
        player.playSound(event);
        if (options != null)
            OnActionsTrigger.addParticles(options, level, player.getX(), player.getY() + 0.25, player.getZ(), player.getRandom().nextInt(12) / 10f + 0.5f);
    }

    public int getCurrentSparkles(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Sparkles");
    }

    public int getStoredXP(ItemStack stack) {
        return stack.getOrCreateTag().getInt("CombatExperience");
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) && Config.Common.CAN_SHARPNESS_BE_APPLIED.get();
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return EnchantmentHelper.getEnchantments(book).containsKey(Enchantments.SHARPNESS) ? Config.Common.CAN_SHARPNESS_BE_APPLIED.get() : true;
    }

    public void setStoredXP(ItemStack stack, int value) {
        stack.getOrCreateTag().putInt("CombatExperience", value);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        if (!pStack.getOrCreateTag().contains("Sparkles"))
            pStack.getOrCreateTag().putInt("Sparkles", 0);
        int sparkles = Math.min(getCurrentSparkles(pStack), 5);
        MutableComponent sparkleOnItem = Component.literal("❇".repeat(Math.max(0, sparkles))).withStyle(ChatFormatting.GOLD).append(Component.literal("❇".repeat(Math.max(0, 5 - sparkles))).withStyle(ChatFormatting.DARK_GRAY));
        pTooltipComponents.add(sparkleOnItem);
        pTooltipComponents.add(CommonComponents.EMPTY);
        if (Screen.hasShiftDown())
            appendSwordsDesc(pStack, pLevel, pTooltipComponents);
        else
            pTooltipComponents.add(getShiftTooltip());
    }

    public abstract void appendSwordsDesc(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents);

    @Override
    public void addParticles(Level level, ItemEntity itemEntity) {
        if (getStoredXP(itemEntity.getItem()) > XPTiers.values()[getCurrentSparkles(itemEntity.getItem())].getNeededXP() && itemEntity.tickCount % 10 == 0)
            OnActionsTrigger.addParticles(new ColorableParticleOption("sparkle", 1f, 1f, 1f), level, itemEntity.getX(), itemEntity.getY() + 0.25f, itemEntity.getZ(), 1.5f);
    }

    private Component getShiftTooltip() {
        return Component.translatable("tooltip.press_shift").withStyle(ChatFormatting.DARK_GRAY);
    }
}