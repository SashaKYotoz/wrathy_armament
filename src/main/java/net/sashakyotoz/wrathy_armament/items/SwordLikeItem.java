package net.sashakyotoz.wrathy_armament.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SwordLikeItem extends Item implements Vanishable {
    public SwordLikeItem(Properties properties) {
        super(properties);
    }
    public void rightClickBlock(Player player, ItemStack stack){}
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

    public boolean hurtEnemy(ItemStack stack, LivingEntity livingEntity, LivingEntity entity) {
        stack.hurtAndBreak(1, entity, (entity1) -> entity1.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (!pStack.getOrCreateTag().contains("Sparkles"))
            pStack.getOrCreateTag().putInt("Sparkles",0);
        int sparkles = Math.min(pStack.getOrCreateTag().getInt("Sparkles"), 5);
        MutableComponent sparkleOnItem = Component.literal("❇".repeat(Math.max(0, sparkles))).withStyle(ChatFormatting.GOLD).append(Component.literal("❇".repeat(Math.max(0,5- sparkles))).withStyle(ChatFormatting.DARK_GRAY));
        pTooltipComponents.add(sparkleOnItem);
        pTooltipComponents.add(CommonComponents.EMPTY);
    }
}