package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.JohannesKnightAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;
import net.sashakyotoz.wrathy_armament.entities.technical.JohannesSpearEntity;
import net.sashakyotoz.wrathy_armament.registers.*;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class JohannesKnight extends BossLikePathfinderMob implements RangedAttackMob {
    private static final EntityDataAccessor<Boolean> IS_IN_SECOND_PHASE = SynchedEntityData.defineId(JohannesKnight.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<KnightPose> DATA_KNIGHT_POSE = SynchedEntityData.defineId(JohannesKnight.class, WrathyArmamentEntityDataSerializer.KNIGHT_POSE.get());
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.johannes_knight"), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_10);
    public static final EntityDimensions KNIGHT_DIMENSIONS = EntityDimensions.fixed(0.8F, 1.9f);
    public static final EntityDimensions FOUNTAIN_DIMENSIONS = EntityDimensions.fixed(1.5F, 2.6F);
    public final AnimationState jumpKnight = new AnimationState();
    public final AnimationState attackKnight = new AnimationState();
    public final AnimationState deathKnight = new AnimationState();
    public final AnimationState attackFountain = new AnimationState();
    public final AnimationState dashFountain = new AnimationState();
    public final AnimationState daggerAttackFountain = new AnimationState();
    public final AnimationState deathFountain = new AnimationState();

    public JohannesKnight(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.setMaxUpStep(1.5f);
        this.xpReward = 50;
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(WrathyArmamentItems.JOHANNES_SWORD.get()));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(IS_IN_SECOND_PHASE, false);
        this.entityData.define(DATA_KNIGHT_POSE, KnightPose.ATTACKING);
        super.defineSynchedData();
    }

    private void setIsInSecondPhase(boolean b) {
        this.entityData.set(IS_IN_SECOND_PHASE, b);
    }

    public boolean isInSecondPhase() {
        return this.entityData.get(IS_IN_SECOND_PHASE);
    }

    public void johannesSwordAbility() {
        if (this.level() instanceof ServerLevel level) {
            double Yaw = this.getYRot();
            double d0 = this.getXVector(3, Yaw);
            double d1 = this.getXRot() * (-0.025) + 0.5f;
            double d2 = this.getZVector(3, Yaw);
            for (int l = 0; l < 16; ++l) {
                createSpellEntity(level, d0 * 2, d1, d2 * 2, l);
            }
        }
        OnActionsTrigger.queueServerWork(20, this::setRandomPose);
    }

    private void createSpellEntity(ServerLevel level, double vx, double vy, double vz, int i) {
        level.addFreshEntity(new JohannesSpearEntity(level, this.getX() + vx * i, this.getY() + vy, this.getZ() + vz * i, 0, i, this));
        level.gameEvent(GameEvent.ENTITY_PLACE, new Vec3(this.getX() + vx * i, this.getY() + vy, this.getZ() + vz * i), GameEvent.Context.of(this));
    }

    private void setKnightPose(KnightPose knightPose) {
        this.entityData.set(DATA_KNIGHT_POSE, knightPose);
    }

    public KnightPose getKnightPose() {
        return this.entityData.get(DATA_KNIGHT_POSE);
    }

    private void applyDarknessAround(ServerLevel level, Vec3 vec3, @Nullable Entity entity, int i) {
        MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false);
        MobEffectUtil.addEffectToPlayersAround(level, entity, vec3, i, mobeffectinstance, 200);
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> dataAccessor) {
        if (DATA_KNIGHT_POSE.equals(dataAccessor)) {
            if (this.getKnightPose() == KnightPose.DYING) {
                if (!this.isInSecondPhase()) {
                    if (!this.deathKnight.isStarted())
                        this.deathKnight.start(this.tickCount);
                    if (this.level() instanceof ServerLevel level)
                        this.applyDarknessAround(level, Vec3.atCenterOf(this.getOnPos()), this, 20);
                    refreshDimensions();
                    OnActionsTrigger.queueServerWork(40, () -> this.setIsInSecondPhase(true));
                    OnActionsTrigger.queueServerWork(50, () -> this.dashFountain.start(this.tickCount));
                    OnActionsTrigger.queueServerWork(70, () -> this.setKnightPose(KnightPose.ATTACKING));
                } else {
                    if (!this.deathFountain.isStarted())
                        this.deathFountain.start(this.tickCount);
                }
            } else if (this.getKnightPose() == KnightPose.IDLING && this.getTarget() != null)
                OnActionsTrigger.queueServerWork(50, this::setRandomPose);
            else {
                if (this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 16) {
                    switch (this.getKnightPose()) {
                        case JUMPING -> {
                            if (!this.jumpKnight.isStarted()) this.jumpKnight.start(this.tickCount);
                        }
                        case DASHING -> {
                            if (!this.dashFountain.isStarted()) this.dashFountain.start(this.tickCount);
                        }
                        case SHOOTING -> {
                            if (!this.daggerAttackFountain.isStarted()) this.daggerAttackFountain.start(this.tickCount);
                        }
                    }
                }
            }
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    @Override
    public void onEnterCombat() {
        refreshDimensions();
        if (this.getKnightPose() == KnightPose.ATTACKING)
            this.setRandomPose();
        super.onEnterCombat();
    }

    public @NotNull EntityDimensions getDimensions(Pose pose) {
        return !this.isInSecondPhase() ? FOUNTAIN_DIMENSIONS : KNIGHT_DIMENSIONS;
    }

    @Override
    public void baseTick() {
        if (this.isAttacking() && this.attackAnimationTimeout <= 0) {
            this.attackAnimationTimeout = 20;
            if (this.isInSecondPhase()) this.attackFountain.start(this.tickCount);
            else this.attackKnight.start(this.tickCount);
        } else
            this.attackAnimationTimeout--;
        this.bossEvent.setName(this.isInSecondPhase() ? Component.translatable("boss.wrathy_armament.johannes_fountain") : Component.translatable("boss.wrathy_armament.johannes_knight"));
        this.bossEvent.setColor(this.isInSecondPhase() ? BossEvent.BossBarColor.RED : BossEvent.BossBarColor.WHITE);
        this.bossEvent.setDarkenScreen(this.getKnightPose() == KnightPose.DYING);
        super.baseTick();
    }

    @Override
    public void onLeaveCombat() {
        if (this.getHealth() < this.getMaxHealth() && this.random.nextBoolean())
            OnActionsTrigger.queueServerWork(30, () -> this.setKnightPose(KnightPose.IDLING));
        super.onLeaveCombat();
    }

    public void setRandomPose() {
        int random = this.random.nextIntBetweenInclusive(0, 2);
        switch (this.getKnightPose()) {
            case SHOOTING -> {
                switch (random) {
                    default -> this.setKnightPose(KnightPose.ATTACKING);
                    case 1 -> this.setKnightPose(KnightPose.DASHING);
                    case 2 -> this.setKnightPose(KnightPose.JUMPING);
                }
            }
            case ATTACKING -> {
                switch (random) {
                    default -> this.setKnightPose(KnightPose.SHOOTING);
                    case 1 -> this.setKnightPose(KnightPose.DASHING);
                    case 2 -> this.setKnightPose(KnightPose.JUMPING);
                }
            }
            case DASHING -> {
                switch (random) {
                    default -> this.setKnightPose(KnightPose.SHOOTING);
                    case 1 -> this.setKnightPose(KnightPose.ATTACKING);
                    case 2 -> this.setKnightPose(KnightPose.JUMPING);
                }
            }
            case JUMPING -> {
                switch (random) {
                    default -> this.setKnightPose(KnightPose.SHOOTING);
                    case 1 -> this.setKnightPose(KnightPose.ATTACKING);
                    case 2 -> this.setKnightPose(KnightPose.DASHING);
                }
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new JohannesKnightAttackGoal(this, 1f, true));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && JohannesKnight.this.getKnightPose() != KnightPose.DYING;
            }
        });
        super.registerGoals();
    }

    public void dashing() {
        double speed = 1.5;
        double Yaw = this.getYRot();
        int randomOffset = this.random.nextIntBetweenInclusive(-2, 2);
        if (this.level() instanceof ServerLevel level) {
            HarmfulProjectileEntity projectile = new HarmfulProjectileEntity(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(), level, 12, "knight_axe");
            projectile.setOwner(JohannesKnight.this);
            projectile.setProjectileType("huge_sword");
            projectile.moveTo(
                    this.getX() + OnActionsTrigger.getXVector(speed, Yaw) + random.nextIntBetweenInclusive(-2, 2),
                    this.getY() + 1,
                    this.getZ() + OnActionsTrigger.getZVector(speed, Yaw) + random.nextIntBetweenInclusive(-2, 2));
            level.addFreshEntity(projectile);
        }
        this.setDeltaMovement(new Vec3(randomOffset, 0.25f, -randomOffset));
        if (!this.isInSecondPhase() && !this.hasEffect(MobEffects.MOVEMENT_SPEED))
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 3));
        OnActionsTrigger.queueServerWork(20, this::setRandomPose);
    }

    public void johannesSwordDash() {
        double speed = 1.5;
        double Yaw = this.getYRot();
        RandomSource random = RandomSource.create();
        this.setDeltaMovement(0, 0.25, 0);
        if (this.level() instanceof ServerLevel level) {
            for (int i = 0; i < 4; i++) {
                HarmfulProjectileEntity projectile = new HarmfulProjectileEntity(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(), level, 12, "knight_axe");
                projectile.setOwner(JohannesKnight.this);
                projectile.setProjectileType("knight_axe");
                projectile.moveTo(
                        this.getX() + OnActionsTrigger.getXVector(speed, Yaw) + random.nextIntBetweenInclusive(-2, 2),
                        this.getY() + 1 + i,
                        this.getZ() + OnActionsTrigger.getZVector(speed, Yaw) + random.nextIntBetweenInclusive(-2, 2));
                level.addFreshEntity(projectile);
            }
        }
        OnActionsTrigger.queueServerWork(10, () -> this.setDeltaMovement(new Vec3(OnActionsTrigger.getXVector(speed, Yaw), (this.getXRot() * (-0.025)) + 0.25, OnActionsTrigger.getZVector(speed, Yaw))));
        OnActionsTrigger.queueServerWork(20, this::setRandomPose);
    }

    public void johannesSwordBackwardsDash() {
        this.setDeltaMovement(0, 0.5, 0);
        double speed = 2;
        double Yaw = this.getYRot();
        if (this.level() instanceof ServerLevel level) {
            for (int i = 0; i < 4; i++) {
                HarmfulProjectileEntity projectile = new HarmfulProjectileEntity(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(), level, 10, "knight_dagger");
                projectile.setOwner(JohannesKnight.this);
                projectile.setProjectileType("knight_dagger");
                projectile.moveTo(
                        this.getX() + OnActionsTrigger.getXVector(speed + i, Yaw),
                        this.getY() + (this.isInSecondPhase() ? i : 0),
                        this.getZ() + OnActionsTrigger.getZVector(speed + i, Yaw));
                level.addFreshEntity(projectile);
            }
        }
        OnActionsTrigger.queueServerWork(10, () -> this.setDeltaMovement(new Vec3(OnActionsTrigger.getXVector(speed, -Yaw), 0.25, -OnActionsTrigger.getZVector(speed, Yaw))));
        OnActionsTrigger.queueServerWork(20, this::setRandomPose);
    }

    @Override
    protected void tickDeath() {
        if (this.getKnightPose() != KnightPose.DYING)
            this.setKnightPose(KnightPose.DYING);
        if (deathTime == 19)
            this.spawnParticle(WrathyArmamentParticleTypes.FIRE_TRAIL.get(), this.level(), this.getX(), this.getY(), this.getZ(), 2);
        super.tickDeath();
    }

    @Override
    public void die(DamageSource source) {
        this.deathTime = -120;
        if (source.getEntity() instanceof Player player)
            player.displayClientMessage(Component.translatable("boss.wrathy_armament.johannes_fountain.dying"), true);
        super.die(source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getHealth() > 25) {
            if (source.getEntity() instanceof LivingEntity livingEntity) {
                this.setTarget(livingEntity);
                if (this.random.nextFloat() > 0.75f) {
                    if (this.random.nextBoolean())
                        this.johannesSwordBackwardsDash();
                    else
                        this.johannesSwordDash();
                }
            }
            if (source.getEntity() instanceof Projectile && this.random.nextBoolean()) {
                this.dashing();
                this.playSound(WrathyArmamentSounds.SOUL_SWING);
                return false;
            }
        }
        if (this.getKnightPose() == KnightPose.IDLING)
            this.setRandomPose();
        if (this.getKnightPose() != KnightPose.DYING && !this.isInSecondPhase() && this.getHealth() < this.getMaxHealth() / 2f)
            this.setKnightPose(KnightPose.DYING);
        if (this.getKnightPose() == KnightPose.DYING)
            return false;
        return super.hurt(source, amount);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("isInSecondPhase", this.isInSecondPhase());
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.getBoolean("isInSecondPhase"))
            this.setIsInSecondPhase(true);
        super.readAdditionalSaveData(tag);
    }

    @Override
    public ServerBossEvent bossInfo() {
        return this.bossEvent;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 16)
                .add(Attributes.ARMOR_TOUGHNESS, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.KNOCKBACK_RESISTANCE, 5);
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float p_33318_) {
        setRandomPose();
    }

    public enum KnightPose {
        DYING,
        ATTACKING,
        DASHING,
        JUMPING,
        SHOOTING,
        IDLING
    }
}