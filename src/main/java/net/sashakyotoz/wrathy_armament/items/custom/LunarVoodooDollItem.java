package net.sashakyotoz.wrathy_armament.items.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.sashakyotoz.anitexlib.client.renderer.IParticleItem;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;
import net.sashakyotoz.wrathy_armament.items.ItemWithDescription;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class LunarVoodooDollItem extends ItemWithDescription implements IParticleItem {
    public LunarVoodooDollItem(Properties properties, String s) {
        super(properties, s);
    }

    @Override
    public void addParticles(Level level, ItemEntity itemEntity) {}

    @Override
    public void serverTick(Level level, ItemEntity entity) {
        if (level.isNight()
                && level.canSeeSky(entity.getOnPos().above())
                && !level.getFluidState(entity.getOnPos()).isEmpty()) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.setDayTime(13000);
                entity.playSound(SoundEvents.CONDUIT_AMBIENT, 1.5f, 1.1f);
                OnActionsTrigger.queueServerWork(30, () -> {
                    entity.level().explode(entity, entity.getX(), entity.getY(), entity.getZ(), 2.5f, Level.ExplosionInteraction.NONE);
                    MoonLord lord = new MoonLord(WrathyArmamentEntities.MOON_LORD.get(), serverLevel);
                    lord.moveTo(entity.getOnPos().above(3).getCenter());
                    entity.level().addFreshEntity(lord);
                });
                entity.discard();
            }
        }
    }
}