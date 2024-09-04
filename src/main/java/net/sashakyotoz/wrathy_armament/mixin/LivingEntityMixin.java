package net.sashakyotoz.wrathy_armament.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.items.MirrorSword;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "hurt", at = @At("HEAD"))
    public void manageHurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = pSource.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            LivingEntity currentEntity = livingEntity.getLastHurtMob();
            if (currentEntity != null) {
                if (currentEntity.getMainHandItem().getItem() instanceof MirrorSword sword) {
                    ItemStack stack = currentEntity.getMainHandItem();
                    sword.addDamageToKeep(pAmount / 2f, stack);
                }
                if (currentEntity.getOffhandItem().getItem() instanceof MirrorSword sword) {
                    ItemStack stack = currentEntity.getOffhandItem();
                    sword.addDamageToKeep(pAmount / 2f, stack);
                }
            }
        }
    }
}