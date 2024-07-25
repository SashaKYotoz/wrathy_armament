package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.LichKingAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.LichKingRangedAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.technical.ParticleLikeEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class LichKing extends BossLikePathfinderMob implements RangedAttackMob {
    public static final EntityDataAccessor<String> AI_PHASE = SynchedEntityData.defineId(LichKing.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<Integer> ID_OF_COMBO = SynchedEntityData.defineId(LichKing.class, EntityDataSerializers.INT);
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.lich_king"), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10);
    private final LichKingRangedAttackGoal rangedAttackGoal = new LichKingRangedAttackGoal(this, 1.5D, 20, 20.0F);
    private final LichKingAttackGoal meleeGoal = new LichKingAttackGoal(this, 1f, true);
    private final TargetingConditions targetCountTargeting = TargetingConditions.forNonCombat().range(48.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
    public final AnimationState spawn = new AnimationState();
    public final AnimationState death = new AnimationState();
    public final AnimationState combo_attack = new AnimationState();
    public final AnimationState combo_attack1 = new AnimationState();
    public final AnimationState combo_attack2 = new AnimationState();
    public final AnimationState spin_attack = new AnimationState();
    public final AnimationState rain_cast = new AnimationState();
    public final AnimationState summon = new AnimationState();
    public final AnimationState transition_to_heal = new AnimationState();
    public int changePhaseTimeout = 0;
    private int healingTimeout = 360;

    public LichKing(EntityType<? extends BossLikePathfinderMob> type, Level level) {
        super(type, level);
        this.setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, new ItemStack(WrathyArmamentItems.FROSTMOURNE.get()));
        this.setMaxUpStep(1.25f);
        this.xpReward = 50;
    }

    @Override
    public void onAddedToWorld() {
        this.spawn.start(this.tickCount);
        super.onAddedToWorld();
    }

    private boolean isPoseStopLich() {
        return this.getPose() == Pose.SPIN_ATTACK || this.getPose() == Pose.USING_TONGUE || this.getPose() == Pose.ROARING;
    }

    //attack things
    @Override
    public void onLeaveCombat() {
        if (this.getHealth() < this.getMaxHealth() && this.random.nextBoolean()) {
            this.transition_to_heal.start(this.tickCount);
            OnActionsTrigger.queueServerWork(30, () -> {
                this.setAiPhase("idling");
                this.healingTimeout = this.random.nextIntBetweenInclusive(500, 1000);
            });
        }
        super.onLeaveCombat();
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> dataAccessor) {
        if (AI_PHASE.equals(dataAccessor)) {
            if (this.getAIPhase().equals("idling") && this.getTarget() != null && this.healingTimeout > 0)
                OnActionsTrigger.queueServerWork(20, this::setAiAction);
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    public void reassessAttackGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.rangedAttackGoal);
            if (this.getAIPhase().equals("rain_cast") || this.getAIPhase().equals("summon")) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD)
                    i = 40;
                this.rangedAttackGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.rangedAttackGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }
    }

    @Override
    protected void tickDeath() {
        if (!this.death.isStarted())
            this.death.start(this.tickCount);
        if (deathTime == 19)
            this.spawnParticle(this.level(), this.getX(), this.getY(), this.getZ(), 2);
        super.tickDeath();
    }

    @Override
    public void die(DamageSource source) {
        this.deathTime = -40;
        super.die(source);
    }

    @Override
    public void onEnterCombat() {
        if (getAIPhase().equals("spawn") || getAIPhase().equals("idling"))
            setAiAction();
        super.onEnterCombat();
    }

    private void clientDiggingParticles(AnimationState time) {
        if ((float) time.getAccumulatedTime() < 1000.0F) {
            RandomSource randomsource = this.getRandom();
            BlockState blockstate = this.getBlockStateOn();
            if (blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                for (int i = 0; i < 30; ++i) {
                    double d0 = this.getX() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    double d1 = this.getY() + 0.5f;
                    double d2 = this.getZ() + (double) Mth.randomBetween(randomsource, -0.7F, 0.7F);
                    this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate), d0, d1, d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    public void setAiAction() {
        if (this.changePhaseTimeout <= 0) {
            boolean toMeleeAttack = this.random.nextBoolean();
            float chanceToSetInHeal = this.getHealth() < this.getMaxHealth() / 2 ? 0.25f : 0.125f;
            if (toMeleeAttack)
                setAiPhase("combo_attack");
            else {
                if (this.random.nextFloat() < chanceToSetInHeal && this.healingTimeout <= 0) {
                    this.setAiPhase("idling");
                    this.healingTimeout = this.random.nextIntBetweenInclusive(600, 1200);
                } else {
                    if (this.random.nextBoolean())
                        setAiPhase("summon");
                    else
                        setAiPhase("rain_cast");
                }
            }
            reassessAttackGoal();
            this.changePhaseTimeout = this.random.nextInt(5, 16) * 10;
        }
    }

    @Override
    public void baseTick() {
        if (this.spawn.isStarted() || this.death.isStarted())
            this.clientDiggingParticles(this.spawn.isStarted() ? this.spawn : this.death);
        if (this.isInHealingState() && this.tickCount % 20 == 0) {
            if (this.getHealth() != this.getMaxHealth()) {
                this.spawnParticle(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(), this.level(), this.getX(), this.getY() + 1, this.getZ(), 2.5f * this.random.nextInt(1, 4));
                this.heal(5);
            }
        }
        if (this.isAttacking() && this.attackAnimationTimeout <= 0) {
            this.attackAnimationTimeout = 20;
            switch (this.getIdOfCombo()) {
                default -> this.combo_attack.start(this.tickCount);
                case 1 -> this.combo_attack1.start(this.tickCount);
                case 2 -> this.combo_attack2.start(this.tickCount);
            }
        } else
            this.attackAnimationTimeout--;
        if (!this.isAttacking() || this.getIdOfCombo() > 3) {
            this.combo_attack.stop();
            this.combo_attack1.stop();
            this.combo_attack2.stop();
        }
        if (this.isPoseStopLich())
            this.navigation.stop();
        if (healingTimeout > 0 && !this.isInHealingState())
            healingTimeout--;
        if (this.changePhaseTimeout > 0)
            changePhaseTimeout--;
        switch (getPose()) {
            case ROARING -> {
                if (!this.summon.isStarted()) this.summon.start(this.tickCount);
            }
            case USING_TONGUE -> {
                if (!this.rain_cast.isStarted()) this.rain_cast.start(this.tickCount);
            }
            case SPIN_ATTACK -> {
                if (!this.spin_attack.isStarted()) this.spin_attack.start(this.tickCount);
            }
        }
        super.baseTick();
    }

    public void updateWalkAnimation(float updateTick) {
        float f;
        f = this.getAIPhase().equals("idling") ? 0 : Math.min(updateTick * 6.0F, 1.0F);
        this.walkAnimation.update(f, 0.2F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && !LichKing.this.isInHealingState();
            }
        });
        super.registerGoals();
    }

    private boolean isOrNotMyrmidonsAround() {
        int i = this.level().getNearbyEntities(LichMyrmidon.class, this.targetCountTargeting, this, this.getBoundingBox().inflate(48.0D)).size();
        return this.getRandom().nextInt(5) + 1 > i;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getAIPhase().equals("idling") && this.random.nextBoolean())
            this.setAiAction();
        if (this.random.nextBoolean() && !this.isAttacking()) {
            if (source.getEntity() != null && this.distanceToSqr(source.getEntity()) < 16)
                playSpinAnimation();
        }
        if (this.random.nextBoolean() && this.getIdOfCombo() > 0){
            if (source.getEntity() instanceof Projectile projectile && projectile.getOwner() != null && this.distanceToSqr(projectile.getOwner()) > 20){
                shootSoulRay();
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    private void shootSoulRay() {
        this.playSound(SoundEvents.SOUL_ESCAPE);
        this.transition_to_heal.start(this.tickCount);
        double d0 = -Mth.sin(this.getYRot() * ((float) Math.PI / 180F));
        double d1 = Mth.cos(this.getYRot() * ((float) Math.PI / 180F));
        float scaling = 0;
        for (int i1 = 0; i1 < 24; i1++) {
                scaling = scaling + 1;
            this.level().addParticle(WrathyArmamentMiscRegistries.FROST_SOUL_RAY.get(),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getX()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getY()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getZ()), d0,
                    0.1, d1);
            final Vec3 center = new Vec3(
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getX()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getY()),
                    (this.level().clip(new ClipContext(this.getEyePosition(1f), this.getEyePosition(1f).add(this.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this)).getBlockPos().getZ()));
            List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(2D), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
            for (Entity entityIterator : entityList) {
                if (!(entityIterator == this)) {
                    if (entityIterator instanceof LivingEntity livingEntity) {
                        livingEntity.hurt(this.damageSources().magic(),8);
                        livingEntity.setDeltaMovement(0,0.5,0);
                        this.level().setBlock(livingEntity.blockPosition().above(2),this.level().getBlockState(livingEntity.blockPosition()),2);
                        this.level().setBlock(livingEntity.blockPosition(),Blocks.AIR.defaultBlockState(),2);
                    }
                }
            }
        }
    }

    private void playSpinAnimation() {
        this.setPose(Pose.SPIN_ATTACK);
        BlockState blockstate = this.getBlockStateOn();
        for (int i = 0; i < 360; i++) {
            if (i % 20 == 0)
                this.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockstate),
                        this.getX(), this.getY() + 0.25f, this.getZ(),
                        this.getXVector(3, this.getYRot()), 1, this.getZVector(3, this.getYRot()));
        }
        OnActionsTrigger.queueServerWork(15, () -> {
            if (this.getTarget() != null && this.spin_attack.isStarted())
                this.hitNearbyMobs(15, 12);
            this.setPose(Pose.STANDING);
        });
    }

    @Override
    public void performRangedAttack(LivingEntity entity, float p_33318_) {
        if (this.getAIPhase().equals("summon")) {
            this.setPose(Pose.ROARING);
            if (this.healingTimeout <= 0)
                this.setAiPhase("idling");
            if (this.isOrNotMyrmidonsAround() && this.level() instanceof ServerLevel level) {
                int randomCountOfMyrmidons = this.random.nextIntBetweenInclusive(1, 4);
                for (int i = 0; i < randomCountOfMyrmidons; i++) {
                    LichMyrmidon lichMyrmidon = new LichMyrmidon(WrathyArmamentEntities.LICH_MYRMIDON.get(), level);
                    lichMyrmidon.moveTo(this.getX() + this.getZVector(2, this.getYRot()),
                            this.getY() + 1,
                            this.getZ() + this.getZVector(2, this.getYRot()));
                    lichMyrmidon.setOwner(LichKing.this);
                    level.addFreshEntity(lichMyrmidon);
                }
            }
            OnActionsTrigger.queueServerWork(15, () -> this.setPose(Pose.STANDING));
        }
        if (this.getAIPhase().equals("rain_cast")) {
            this.setPose(Pose.USING_TONGUE);
            rainCast();
        }
        OnActionsTrigger.queueServerWork(30, this::setAiAction);
    }

    private void rainCast() {
        if (this.level() instanceof ServerLevel level) {
            OnActionsTrigger.queueServerWork(20, () -> {
                this.setPose(Pose.STANDING);
                ParticleLikeEntity particleEntity = new ParticleLikeEntity(WrathyArmamentEntities.PARTICLE_LIKE_ENTITY.get(), level, 1f, true, true, 4,
                        ParticleTypes.SOUL, "lich_rain");
                particleEntity.moveTo(new Vec3(this.getX(), this.getY() + 1f, this.getZ()));
                level.addFreshEntity(particleEntity);
            });
        }
    }

    //logic
    public boolean isInHealingState() {
        return this.getAIPhase().equals("idling");
    }

    private void setAiPhase(String s) {
        this.entityData.set(AI_PHASE, s);
    }

    public String getAIPhase() {
        return this.entityData.get(AI_PHASE);
    }

    public void setIdOfCombo() {
        if (this.getIdOfCombo() < 3)
            this.entityData.set(ID_OF_COMBO, this.getIdOfCombo() + 1);
        else {
            this.entityData.set(ID_OF_COMBO, 0);
            this.setAiAction();
        }
    }

    private int getIdOfCombo() {
        return this.entityData.get(ID_OF_COMBO);
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(AI_PHASE, "spawn");
        entityData.define(ID_OF_COMBO, 0);
        super.defineSynchedData();
    }

    @Override
    public ServerBossEvent bossInfo() {
        return bossEvent;
    }

    //attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 600.0D)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 5);
    }
}