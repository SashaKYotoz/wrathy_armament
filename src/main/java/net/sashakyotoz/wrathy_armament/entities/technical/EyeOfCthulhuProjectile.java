package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class EyeOfCthulhuProjectile extends AbstractArrow implements ItemSupplier {

    public EyeOfCthulhuProjectile(EntityType<EyeOfCthulhuProjectile> type, Level level) {
        super(type, level);
    }

    public EyeOfCthulhuProjectile(EntityType<EyeOfCthulhuProjectile> type, LivingEntity entity, Level level) {
        super(type, entity, level);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void doPostHurtEffects(LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround)
            this.discard();
        else if (this.tickCount % 2 == 0)
            this.level().addParticle(new ColorableParticleOption("sparkle",0f,0.35f,1f),this.getX(),this.getY(),this.getZ(),0,0,0);
    }

    @Override
    public ItemStack getItem() {
        return Items.GHAST_TEAR.getDefaultInstance();
    }
}
