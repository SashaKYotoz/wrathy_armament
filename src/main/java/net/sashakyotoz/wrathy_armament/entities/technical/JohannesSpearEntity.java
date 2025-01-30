package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;

import javax.annotation.Nullable;
import java.util.UUID;

public class JohannesSpearEntity extends Entity implements TraceableEntity {
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    public int lifeTicks = 22;
    private boolean clientSideAttackStarted;
    @Nullable
    private LivingEntity owner;
    @Nullable
    private UUID ownerUUID;

    public JohannesSpearEntity(EntityType<? extends JohannesSpearEntity> type, Level level) {
        super(type, level);
    }

    public JohannesSpearEntity(Level level, double x, double y, double z, float v, int warmupDelayTicks, LivingEntity living) {
        this(WrathyArmamentEntities.JOHANNES_SPEAR.get(), level);
        this.warmupDelayTicks = warmupDelayTicks;
        this.setOwner(living);
        this.setYRot(v * (180F / (float)Math.PI));
        this.setPos(x, y, z);
    }

    protected void defineSynchedData() {
    }

    public void setOwner(@Nullable LivingEntity living) {
        this.owner = living;
        this.ownerUUID = living == null ? null : living.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level() instanceof ServerLevel) {
            Entity entity = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity)entity;
            }
        }

        return this.owner;
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        this.warmupDelayTicks = compoundTag.getInt("Warmup");
        if (compoundTag.hasUUID("Owner"))
            this.ownerUUID = compoundTag.getUUID("Owner");
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("Warmup", this.warmupDelayTicks);
        if (this.ownerUUID != null)
            compoundTag.putUUID("Owner", this.ownerUUID);
    }

    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (this.lifeTicks == 14) {
                    for(int i = 0; i < 12; ++i) {
                        double d0 = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d1 = this.getY() + 0.05D + this.random.nextDouble();
                        double d2 = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double)this.getBbWidth() * 0.5D;
                        double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.random.nextDouble() * 0.3D;
                        double d5 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.level().addParticle(ParticleTypes.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -8) {
                for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2D, 0.0D, 0.2D))) {
                    this.dealDamageTo(livingentity);
                }
            }

            if (!this.sentSpikeEvent) {
                this.level().broadcastEntityEvent(this, (byte)4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.discard();
            }
        }

    }

    private void dealDamageTo(LivingEntity living) {
        LivingEntity livingentity = this.getOwner();
        if (living.isAlive() && !living.isInvulnerable() && living != livingentity) {
            if (livingentity == null) {
                living.hurt(this.damageSources().magic(), 6.0F);
            } else {
                if (livingentity.isAlliedTo(living)) {
                    return;
                }
                living.hurt(this.damageSources().indirectMagic(this, livingentity), 6.0F);
            }

        }
    }

    public void handleEntityEvent(byte bytes) {
        super.handleEntityEvent(bytes);
        if (bytes == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent())
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.EVOKER_FANGS_ATTACK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
        }
    }

    public float getAnimationProgress(float j) {
        if (!this.clientSideAttackStarted) {
            return 0.0F;
        } else {
            int i = this.lifeTicks - 2;
            return i <= 0 ? 1.0F : 1.0F - ((float)i - j) / 20.0F;
        }
    }
}