package net.sashakyotoz.wrathy_armament.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void handleTick(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) ((Object) this);
        if (entity.getItem().is(WrathyArmamentItems.LUNAR_VOODOO_DOLL.get())) {
            if (entity.level().isNight()
                    && entity.level().canSeeSky(entity.getOnPos().above())
                    && !entity.level().getFluidState(entity.getOnPos()).isEmpty()) {
                if (!entity.level().isClientSide() && entity.level() instanceof ServerLevel level) {
                    level.setDayTime(13000);
                    entity.playSound(SoundEvents.CONDUIT_AMBIENT, 1.5f, 1.1f);
                    OnActionsTrigger.queueServerWork(30, () -> {
                        entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 2.5f, Level.ExplosionInteraction.NONE);
                        MoonLord lord = new MoonLord(WrathyArmamentEntities.MOON_LORD.get(), level);
                        lord.moveTo(entity.getOnPos().above(3).getCenter());
                        entity.level().addFreshEntity(lord);
                    });
                    entity.discard();
                }
            }
        }
    }
}