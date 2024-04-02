package net.sashakyotoz.wrathy_armament.entities.alive;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.MobCopyOwnerTargetGoal;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;
import net.sashakyotoz.wrathy_armament.entities.technical.OwnerableMob;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentParticleTypes;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.EnumSet;

public class LichMyrmidon extends OwnerableMob {
    public static final EntityDataAccessor<Integer> TIME_TO_VANISH = SynchedEntityData.defineId(LichMyrmidon.class, EntityDataSerializers.INT);
    public final AnimationState spawn = new AnimationState();
    public final AnimationState despawn = new AnimationState();
    public final AnimationState attack = new AnimationState();

    public LichMyrmidon(EntityType<? extends LichMyrmidon> type, Level level) {
        super(type, level);
        this.moveControl = new FlyingMoveControl(this,25,false);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TIME_TO_VANISH, 300);
        super.defineSynchedData();
    }

    @Override
    public void onAddedToWorld() {
        this.spawn.start(this.tickCount);
        super.onAddedToWorld();
    }

    private int timeToVanish() {
        return this.entityData.get(TIME_TO_VANISH);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        if (this.timeToVanish() > 0)
            tag.putInt("timeToVanish", this.timeToVanish());
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.getInt("timeToVanish") > 0)
            this.entityData.set(TIME_TO_VANISH, tag.getInt("timeToVanish"));
        super.readAdditionalSaveData(tag);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new Goal() {
            {
                this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            }
            public boolean canUse() {
                return LichMyrmidon.this.getTarget() != null && !LichMyrmidon.this.getMoveControl().hasWanted();
            }
            @Override
            public boolean canContinueToUse() {
                return LichMyrmidon.this.getMoveControl().hasWanted() && LichMyrmidon.this.getTarget() != null && LichMyrmidon.this.getTarget().isAlive();
            }
            @Override
            public void start() {
                LivingEntity livingentity = LichMyrmidon.this.getTarget();
                Vec3 vec3d = livingentity.getEyePosition(1);
                LichMyrmidon.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 3);
            }
            @Override
            public void tick() {
                LivingEntity livingentity = LichMyrmidon.this.getTarget();
                if (LichMyrmidon.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    LichMyrmidon.this.setPose(Pose.ROARING);
                    OnActionsTrigger.queueServerWork(10,()-> {
                        LichMyrmidon.this.doHurtTarget(livingentity);
                        LichMyrmidon.this.setPose(Pose.STANDING);
                    });
                } else {
                    double d0 = LichMyrmidon.this.distanceToSqr(livingentity);
                    if (d0 < 32) {
                        Vec3 vec3d = livingentity.getEyePosition(1);
                        LichMyrmidon.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 3);
                    }
                }
            }
        });
        this.targetSelector.addGoal(1, new MobCopyOwnerTargetGoal(this));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        return this.getOwner() instanceof Player ? entity instanceof Player : entity instanceof LichKing;
    }
    @Override
    public boolean doHurtTarget(Entity entity) {
        float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float f1 = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        if (entity instanceof LivingEntity livingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), livingEntity.getMobType());
            f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
            livingEntity.setTicksFrozen(200);
        }
        boolean flag = entity.hurt(this.damageSources().mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && entity instanceof LivingEntity livingEntity) {
                livingEntity.knockback(f1 * 0.5F, Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180F)));
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
            }
            this.doEnchantDamageEffects(this, entity);
            this.setLastHurtMob(entity);
        }
        return flag;
    }

    @Override
    public void tick() {
        if (this.isHaveToFindOwner() && this.getOwner() == null)
            this.setOwner(this.getNearestPlayer());
        if (this.timeToVanish() > 0) {
            this.entityData.set(TIME_TO_VANISH, this.timeToVanish() - 1);
            if (this.getTarget() == null && this.getOwner() != null)
                this.setTarget(this.getOwner().getLastHurtMob());
        } else if (this.timeToVanish() <= 0 || this.isOnFire()) {
            this.despawn.start(this.tickCount);
            if (this.level() instanceof ServerLevel level)
                level.sendParticles(WrathyArmamentParticleTypes.FROST_SOUL_RAY.get(), this.getZ(), this.getY(), this.getZ(), 18, 1, -1, 1, 0.5f);
            OnActionsTrigger.queueServerWork(10, this::discard);
        }
        if (this.getPose() == Pose.ROARING)
            this.attack.start(this.tickCount);
        super.tick();
    }
    @Override
    protected void tickDeath() {
        if (!this.despawn.isStarted())
            this.despawn.start(this.tickCount);
        if (deathTime == 19) {
            if (this.level() instanceof ServerLevel level)
                level.sendParticles(WrathyArmamentParticleTypes.FROST_SOUL_RAY.get(), this.getZ(), this.getY(), this.getZ(), 18, 1, -1, 1, 0.5f);
        }
        super.tickDeath();
    }

    @Override
    public void die(DamageSource source) {
        this.deathTime = -10;
        super.die(source);
    }
    @Override
    protected boolean shouldDespawnInPeaceful() {
        return this.getOwner() != null && this.getOwner() instanceof Player;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }
    @Override
    public void travel(Vec3 vec3) {
        this.move(MoverType.SELF, this.getDeltaMovement().scale(2));
        super.travel(vec3);
    }
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.ATTACK_KNOCKBACK, 1)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ARMOR, 6)
                .add(Attributes.ARMOR_TOUGHNESS, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.FLYING_SPEED, 0.3);
    }
}