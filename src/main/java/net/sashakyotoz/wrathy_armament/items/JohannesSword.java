package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.List;

public class JohannesSword extends SwordLikeItem {
    public JohannesSword(Properties properties) {
        super(properties);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 15.5, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2.8, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }
    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.game.johannes_sword").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.DARK_GREY_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.johannes_sword_hint").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.johannes_sword_dash").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.johannes_sword_hint1").withStyle(WrathyArmamentItems.RED_DESCRIPTION_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.johannes_sword_spear_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.startUsingItem(hand);
        if (!player.getCooldowns().isOnCooldown(player.getItemInHand(hand).getItem())){
            if (!player.isCrouching())
                johannesSwordDash(player,player.getItemInHand(hand));
            else
                johannesSwordBackwardsDash(player,player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    private void johannesSwordDash(Player player, ItemStack stack) {
        double speed = 1.5;
        double Yaw = player.getYRot();
        RandomSource random = RandomSource.create();
        player.setDeltaMovement(0, 0.25, 0);
        player.getCooldowns().addCooldown(stack.getItem(), 50);
        if (player.level() instanceof ServerLevel level){
            for (int i = 0; i < 3; i++) {
                HarmfulProjectileEntity projectile = new HarmfulProjectileEntity(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(),level,9,"axe");
                projectile.setOwner(player);
                projectile.setProjectileType("axe");
                projectile.moveTo(
                        player.getX() + OnActionsTrigger.getXVector(speed, Yaw) + random.nextIntBetweenInclusive(-2,2),
                        player.getY() + 1 + i,
                        player.getZ() + OnActionsTrigger.getZVector(speed, Yaw) + random.nextIntBetweenInclusive(-2,2));
                level.addFreshEntity(projectile);
            }
        }
        OnActionsTrigger.queueServerWork(10,()->player.setDeltaMovement(new Vec3(OnActionsTrigger.getXVector(speed, Yaw), (player.getXRot() * (-0.025)) + 0.25, OnActionsTrigger.getZVector(speed, Yaw))));
    }
    private void johannesSwordBackwardsDash(Player player, ItemStack stack){
        player.setDeltaMovement(0, 0.25, 0);
        player.getCooldowns().addCooldown(stack.getItem(), 70);
        double speed = 2;
        double Yaw = player.getYRot();
        if (player.level() instanceof ServerLevel level){
            for (int i = 0; i < 4; i++) {
                HarmfulProjectileEntity projectile = new HarmfulProjectileEntity(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(),level,7,"dagger");
                projectile.setOwner(player);
                projectile.setProjectileType("dagger");
                projectile.moveTo(
                        player.getX() + OnActionsTrigger.getXVector(speed + i, Yaw),
                        player.getY(),
                        player.getZ() + OnActionsTrigger.getZVector(speed + i, Yaw));
                level.addFreshEntity(projectile);
            }
        }
        OnActionsTrigger.queueServerWork(10,()->player.setDeltaMovement(new Vec3(OnActionsTrigger.getXVector(speed, -Yaw), 0.25, -OnActionsTrigger.getZVector(speed, Yaw))));
    }
}
