package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentParticleTypes;

import java.util.List;

public class MistsplitterReforged extends Item {
    private int timer = 0;
    private float damageBoost;
    public MistsplitterReforged(Properties properties) {
        super(properties);
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 17 + damageBoost*2, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }
    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.translatable("item.wrathy_armament.game.mistsplitter").withStyle(WrathyArmamentItems.TITLE_FORMAT).withStyle(ChatFormatting.ITALIC));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.abilities").withStyle(WrathyArmamentItems.TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.right_hand").withStyle(WrathyArmamentItems.AQUA_TITLE_FORMAT));
        list.add(CommonComponents.EMPTY);
        list.add(Component.translatable("item.wrathy_armament.mistsplitter_hint").withStyle(WrathyArmamentItems.PURPLE_TITLE_FORMAT));
        list.add(Component.translatable("item.wrathy_armament.mistsplitter_attack").withStyle(WrathyArmamentItems.TITLE_FORMAT));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (entity instanceof Player player && player.getMainHandItem().is(stack.getItem())){
            if(damageBoost < 3){
                if(timer < 100)
                    timer++;
                else {
                    for (int i = 0; i < 360; i++) {
                        if (i % 20 == 0) {
                            level.addParticle(WrathyArmamentParticleTypes.FROST_SOUL_RAY.get(), player.getX(), player.getY() + 0.5, player.getZ(), Math.cos(i) * 0.3d, 0.25d, Math.sin(i) * 0.3d);
                        }
                    }
                    damageBoost++;
                    timer = 0;
                }
            }
        }
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (damageBoost > 0)
            damageBoost--;
        return super.onLeftClickEntity(stack,player,entity);
    }
}