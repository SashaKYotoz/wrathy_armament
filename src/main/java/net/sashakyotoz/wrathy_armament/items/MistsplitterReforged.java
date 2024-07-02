package net.sashakyotoz.wrathy_armament.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.anitexlib.client.particles.parents.types.WaveParticleOption;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.capabilities.ModCapabilities;

import java.util.List;

public class MistsplitterReforged extends SwordLikeItem {
    private int timer = 0;
    private float damageBoost;
    public MistsplitterReforged(Properties properties) {
        super(properties);
    }

    @Override
    public void leftClickAttack(Player player, ItemStack stack) {
        WaveParticleOption option = new WaveParticleOption(player.getYRot(), 2f, 0.75f, 0, 0.5f);
        player.level().addParticle(option, player.getX(), player.getY() + 4f, player.getZ(),
                OnActionsTrigger.getXVector(2, player.getYRot()),
                OnActionsTrigger.getYVector(1, player.getXRot()),
                OnActionsTrigger.getZVector(2, player.getYRot()));
    }

    @Override
    public void rightClick(Player player, ItemStack stack) {

    }

    @Override
    public void rightClickOnShiftClick(Player player, ItemStack stack) {
        player.getCapability(ModCapabilities.MISTSPLITTER_DEFENCE).ifPresent(context -> {
            context.setDefenseModeFlag(!context.isDefenseModeOn());
            context.setDefenceType(player.getRandom().nextBoolean() ? "fire" : "earth");
        });
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 17 + damageBoost, AttributeModifier.Operation.ADDITION));
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
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i1, boolean p_41408_) {
        if (entity instanceof Player player && player.getMainHandItem().is(stack.getItem())){
            if(damageBoost < 6){
                if(timer < 100)
                    timer++;
                else {
                    if (entity.level() instanceof ServerLevel serverLevel){
                        ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(),serverLevel,0.8f,true,false,5, WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(),"cycle");
                        particleEntity.moveTo(new Vec3(entity.getX(),entity.getY() +1,entity.getZ()));
                        entity.level().addFreshEntity(particleEntity);
                    }
                    for (int i = 0; i < 360; i++) {
                        if (i % 20 == 0) {
                            level.addParticle(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), player.getX(), player.getY() + 1, player.getZ(), Math.cos(i) * 0.4d, 0.25d, Math.sin(i) * 0.4d);
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