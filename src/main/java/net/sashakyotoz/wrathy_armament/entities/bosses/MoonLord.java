package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.MoonLordMovementGoal;
import net.sashakyotoz.wrathy_armament.entities.alive.TrueEyeOfCthulhu;
import net.sashakyotoz.wrathy_armament.entities.bosses.core.MoonLordPart;
import net.sashakyotoz.wrathy_armament.entities.technical.EyeOfCthulhuProjectile;
import net.sashakyotoz.wrathy_armament.entities.technical.HarmfulProjectileEntity;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentSounds;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;
import net.sashakyotoz.wrathy_armament.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class MoonLord extends BossLikePathfinderMob {
    private static final EntityDataAccessor<Float> ANIMATION_SCALING = SynchedEntityData.defineId(MoonLord.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<MoonLord.LordPose> DATA_LORD_POSE = SynchedEntityData.defineId(MoonLord.class, WrathyArmamentMiscRegistries.LORD_POSE.get());
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("entity.wrathy_armament.moon_lord"), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_20);
    private final MoonLordPart[] subEntities;
    private final MoonLordPart headEye;
    private final MoonLordPart heart;
    private final MoonLordPart rightHandEye;
    private final MoonLordPart leftHandEye;
    public final HashMap<String, Integer> damageTakenByPart = new HashMap<>();
    public final AnimationState death = new AnimationState();
    public final AnimationState eyeShooting = new AnimationState();
    public final AnimationState eyeAttack = new AnimationState();
    public final AnimationState interactive = new AnimationState();
    public int deathTicks = 0;
    private int timeOfAbility = 0;

    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (entity) -> entity.getMobType() != MobType.UNDEAD && entity.attackable();
    private final TargetingConditions eyesCountTargeting = TargetingConditions.forNonCombat().range(32.0D).ignoreLineOfSight().ignoreInvisibilityTesting();

    public MoonLord(EntityType<? extends BossLikePathfinderMob> type, Level level) {
        super(type, level);
        this.xpReward = 500;
        this.moveControl = new LordMoveControl(this);
        this.headEye = new MoonLordPart(this, "headEye", 1.5f, 1.5f);
        this.heart = new MoonLordPart(this, "heart", 2.5f, 2.5f);
        this.rightHandEye = new MoonLordPart(this, "rightHandEye", 1.5f, 1.5f);
        this.leftHandEye = new MoonLordPart(this, "leftHandEye", 1.5f, 1.5f);
        this.subEntities = new MoonLordPart[]{this.headEye, this.heart, this.rightHandEye, this.leftHandEye};
        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);
    }

    //entity data
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_SCALING, 1f);
        this.entityData.define(DATA_LORD_POSE, LordPose.IDLING);
    }

    public float getAnimationScaling() {
        return this.entityData.get(ANIMATION_SCALING);
    }

    public void setAnimationScaling(float f) {
        this.entityData.set(ANIMATION_SCALING, f);
    }

    public LordPose getLordPose() {
        return this.entityData.get(DATA_LORD_POSE);
    }

    public void setLordPose(LordPose pose) {
        this.entityData.set(DATA_LORD_POSE, pose);
        this.timeOfAbility = pose.getAbilityTime;
    }

    public boolean isInPose(LordPose phase) {
        return this.getLordPose() == phase;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    public void applyBrightnessAround(Level level, Vec3 vec3, @Nullable Entity sourceEntity, int i) {
        if (level instanceof ServerLevel serverLevel) {
            MobEffectInstance mobeffectinstance = new MobEffectInstance(WrathyArmamentMiscRegistries.BRIGHTNESS.get(), 40, 1, false, false);
            MobEffectUtil.addEffectToPlayersAround(serverLevel, sourceEntity, vec3, i, mobeffectinstance, 40);
        }
    }

    @Override
    public ServerBossEvent bossInfo() {
        return bossEvent;
    }

    //sub entities
    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subEntities.length; i++)
            this.subEntities[i].setId(id + i + 1);
    }

    public List<MoonLordPart> getMainParts() {
        return Arrays.stream(subEntities).toList();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 30 == 0)
            this.setAnimationScaling(this.getRandom().nextIntBetweenInclusive(-10, 10) / 10f);
        //sub entities stuff
        Vec3[] avec3 = new Vec3[this.subEntities.length];
        for (int j = 0; j < this.subEntities.length; ++j) {
            avec3[j] = new Vec3(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
        }
        float f14 = this.getYHeadRot() * ((float) Math.PI / 180F);
        float f1 = Mth.sin(f14);
        float f15 = Mth.cos(f14);
        this.tickPart(this.headEye, -f1 * 0.75f, 5.25D, f15 * 0.75f);
        this.tickPart(this.heart, 0, 2D, 0);
        this.tickPart(this.rightHandEye, f15 * -3.75F, 1.5D, f1 * -3.75F);
        this.tickPart(this.leftHandEye, f15 * 3.75F, 1.5D, f1 * 3.75F);
        for (int l = 0; l < this.subEntities.length; ++l) {
            this.subEntities[l].xo = avec3[l].x;
            this.subEntities[l].yo = avec3[l].y;
            this.subEntities[l].zo = avec3[l].z;
            this.subEntities[l].xOld = avec3[l].x;
            this.subEntities[l].yOld = avec3[l].y;
            this.subEntities[l].zOld = avec3[l].z;
        }
        if (this.tickCount % 10 == 0)
            this.getMainParts().forEach(moonLordPart -> {
                if (this.damageTakenByPart.get(moonLordPart.name) != null &&
                        this.damageTakenByPart.get(moonLordPart.name) > 0)
                    this.damageTakenByPart.put(moonLordPart.name, this.damageTakenByPart.get(moonLordPart.name) - 1);
            });
        if (this.timeOfAbility > 0)
            this.timeOfAbility--;
        if (this.getTarget() != null && this.timeOfAbility <= 0)
            setLordPose();
        if (this.isInPose(LordPose.LASERING)) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                double d0 = target.getX() - this.getX();
                double d2 = target.getZ() - this.getZ();
                double d1 = target.getEyeY() - (this.getEyeY() + 2.9f);
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
                float f2 = (float) (-(Mth.atan2(d1, d3) * (double) (180F / (float) Math.PI)));
                float getRotYToTarget = this.rotlerp(this.getYRot(), f, 40);
                float getRotXToTarget = this.rotlerp(this.getXRot(), f2, 25);
                float delta = RenderUtils.getOscillatingValue(this.tickCount, 8);
                this.setXRot(Mth.lerp(delta, getRotXToTarget - 30, getRotXToTarget + 30));
                this.setYRot(Mth.lerp(delta, getRotYToTarget - 50, getRotYToTarget + 50));
            }
            if (this.tickCount % 5 == 0) {
                if (this.tickCount % 4 == 0)
                    this.playSound(WrathyArmamentSounds.BEAM_SHOOTING, 1.5f, 0.9f);
                for (int i = 0; i < 33; i++) {
                    Vec3 center = posOfViewing(this, BlockPos.containing(this.getX(), this.getY(), this.getZ())).getCenter();
                    List<LivingEntity> entityList = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(1.5f), e -> true).stream().sorted(Comparator.comparingDouble(t -> t.distanceToSqr(center))).toList();
                    for (LivingEntity entity : entityList) {
                        if (entity != this && !(entity instanceof TrueEyeOfCthulhu))
                            entity.hurt(this.damageSources().magic(), this.level().getDifficulty().equals(Difficulty.HARD) ? 8 : 5);
                    }
                }
            }
        }
    }

    private float rotlerp(float pAngle, float pTargetAngle, float pMaxIncrease) {
        float f = Mth.wrapDegrees(pTargetAngle - pAngle);
        if (f > pMaxIncrease)
            f = pMaxIncrease;
        if (f < -pMaxIncrease)
            f = -pMaxIncrease;
        return pAngle + f;
    }

    @Override
    public int getMaxHeadXRot() {
        return 55;
    }

    private void tickPart(MoonLordPart moonLordPart, double pOffsetX, double pOffsetY, double pOffsetZ) {
        moonLordPart.setPos(this.getX() + pOffsetX, this.getY() + pOffsetY, this.getZ() + pOffsetZ);
    }

    public void setLordPose() {
        switch (this.getLordPose()) {
            case IDLING, TELEPORTING, EYE_BLADES -> this.setLordPose(LordPose.ATTACKING);
            case ATTACKING -> this.setLordPose(LordPose.SHOOTING);
            case SHOOTING -> {
                if (this.getTarget().distanceToSqr(this) > 441)
                    this.setLordPose(LordPose.TELEPORTING);
                else
                    this.setLordPose(LordPose.LASERING);
            }
            case LASERING -> this.setLordPose(LordPose.EYE_BLADES);
        }
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> dataAccessor) {
        if (DATA_LORD_POSE.equals(dataAccessor)) {
            if (!(getLordPose().equals(LordPose.IDLING)
                    && getLordPose().equals(LordPose.LASERING)))
                this.interactive.stop();
            switch (this.getLordPose()) {
                case DYING -> this.death.start(this.tickCount);
                case ATTACKING -> {
                    this.eyeAttack.start(this.tickCount);
                    if (this.getTarget() != null && !this.getTarget().hasEffect(MobEffects.WEAKNESS)) {
                        this.getTarget().addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                        this.getTarget().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
                    }
                    int i = this.level().getNearbyEntities(TrueEyeOfCthulhu.class, this.eyesCountTargeting, this, this.getBoundingBox().inflate(32.0D)).size();
                    queueServerWork(20, () -> {
                        if (i < 3) {
                            TrueEyeOfCthulhu eyeOfCthulhu = new TrueEyeOfCthulhu(WrathyArmamentEntities.TRUE_EYE_OF_CTHULHU.get(), this.level());
                            eyeOfCthulhu.setOwner(this);
                            eyeOfCthulhu.moveTo(this.getOnPos().getCenter());
                            eyeOfCthulhu.setDeltaMovement(
                                    getXVector(2, this.getYRot()),
                                    OnActionsTrigger.getYVector(1, this.getXRot()),
                                    getZVector(2, this.getYRot())
                            );
                            this.level().addFreshEntity(eyeOfCthulhu);
                        } else
                            this.applyBrightnessAround(this.level(), this.getEyePosition(), this, 32);
                    });
                }
                case IDLING -> this.interactive.startIfStopped(this.tickCount);
                case LASERING -> {
                    this.navigation.stop();
                    this.interactive.startIfStopped(this.tickCount);
                }
                case EYE_BLADES -> {
                    if (this.getTarget() != null)
                        this.lookAt(this.getTarget(), 15, 15);
                    this.eyeAttack.start(this.tickCount);
                    if (this.getTarget() != null && this.level() instanceof ServerLevel serverLevel) {
                        for (int i = 0; i < 4; i++) {
                            int randomOffset = this.getRandom().nextInt(-2, 3);
                            HarmfulProjectileEntity projectile = new HarmfulProjectileEntity(WrathyArmamentEntities.HARMFUL_PROJECTILE_ENTITY.get(), serverLevel, 10, "vertical_circle");
                            projectile.setOwner(MoonLord.this);
                            projectile.setProjectileType("vertical_circle");
                            projectile.moveTo(
                                    this.getTarget().getX() + randomOffset,
                                    this.getTarget().getY() + 2.5f + i,
                                    this.getTarget().getZ() + randomOffset);
                            serverLevel.addFreshEntity(projectile);
                        }
                    }
                }
                case SHOOTING -> {
                    this.eyeShooting.start(this.tickCount);
                    OnActionsTrigger.queueServerWork(14, () -> {
                        for (int i = -1; i < 2; i++) {
                            EyeOfCthulhuProjectile projectile = new EyeOfCthulhuProjectile(WrathyArmamentEntities.EYE_OF_CTHULHU_PROJECTILE.get(), this, this.level());
                            projectile.setOwner(this);
                            projectile.setOwner(this);
                            projectile.shootFromRotation(this.leftHandEye, this.getXRot() - 10, this.getYRot(), 0, 3F, 0);
                            this.level().addFreshEntity(projectile);
                        }
                    });
                    OnActionsTrigger.queueServerWork(28, () -> {
                        for (int i = -1; i < 2; i++) {
                            EyeOfCthulhuProjectile projectile = new EyeOfCthulhuProjectile(WrathyArmamentEntities.EYE_OF_CTHULHU_PROJECTILE.get(), this, this.level());
                            projectile.setOwner(this);
                            projectile.shootFromRotation(this.rightHandEye, this.getXRot() - 10, this.getYRot(), 0, 3F, 0);
                            this.level().addFreshEntity(projectile);
                        }
                    });
                }
                case TELEPORTING -> {
                    this.applyBrightnessAround(this.level(), this.getEyePosition(), this, 48);
                    this.playSound(SoundEvents.CONDUIT_AMBIENT_SHORT, 1.5f, 2f);
                    LivingEntity target = this.getTarget();
                    if (target != null)
                        this.teleportTo(target.getRandomX(2), target.getY() + 1, target.getRandomZ(2));
                }
            }
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    @Override
    public boolean isDeadOrDying() {
        return super.isDeadOrDying() || this.getLordPose().equals(LordPose.DYING);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return super.getStandingEyeHeight(pPose, pDimensions) + 2.95f;
    }

    @Override
    public void push(double pX, double pY, double pZ) {
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 0, false, false, LIVING_ENTITY_SELECTOR));
        this.goalSelector.addGoal(2, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(3, new MoonLordMovementGoal(this));
    }

    public boolean isEyesActive() {
        return this.getMainParts().get(0).getHealthPoints() > 0 &&
                this.getMainParts().get(2).getHealthPoints() > 0 &&
                this.getMainParts().get(3).getHealthPoints() > 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return !isEyesActive() && super.hurt(pSource, pAmount);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return WrathyArmamentSounds.MOON_LORD_HURT;
    }

    @Override
    protected void tickDeath() {
        this.deathTicks--;
        if (deathTime == 10) {
            this.spawnParticle(new ColorableParticleOption("sparkle", 0.25f, 0.35f, 1f), this.level(), this.getX(), this.getY(), this.getZ(), 5);
            this.playSound(SoundEvents.CONDUIT_DEACTIVATE, 2f, 1.5f);
            this.applyBrightnessAround(this.level(), this.getOnPos().getCenter(), this, 16);
        }
        if (deathTime == 19 && this.level() instanceof ServerLevel level)
            level.setDayTime(3000);
        super.tickDeath();
    }

    private BlockPos posOfViewing(LivingEntity entity, BlockPos pos) {
        BlockPos pos1 = pos;
        int scaling = 0;
        for (int i = 0; i < 32; i++) {
            BlockPos pos2 = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
            if (!entity.level().getBlockState(new BlockPos(pos2.getX(), pos2.getY(), pos2.getZ())).canOcclude())
                scaling = scaling + 1;
            else {
                if (this.tickCount % 4 == 0)
                    entity.level().addParticle(WrathyArmamentMiscRegistries.BEAM_SPARKLES.get(), pos2.getX(), pos2.getY() + 0.1f, pos2.getZ(), 0, 0.125f, 0);
            }
            pos1 = entity.level().clip(new ClipContext(entity.getEyePosition(1f), entity.getEyePosition(1f).add(entity.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getBlockPos();
        }
        return pos1;
    }

    @Override
    public void die(DamageSource source) {
        this.deathTime = -100;
        this.deathTicks = 200;
        this.setLordPose(LordPose.DYING);
        super.die(source);
    }

    public void travel(Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                this.moveRelative(this.getSpeed(), pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.91F));
            }
        }
        this.calculateEntityAnimation(false);
    }

    //attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 750.0D)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 12)
                .add(Attributes.ARMOR_TOUGHNESS, 12)
                .add(Attributes.MOVEMENT_SPEED, 0.15)
                .add(Attributes.FLYING_SPEED, 0.15)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }

    @Override
    public void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    //poses
    public enum LordPose {
        DYING(120),
        ATTACKING(100),
        LASERING(160),
        SHOOTING(50),
        EYE_BLADES(50),
        TELEPORTING(50),
        IDLING(80);
        public final int getAbilityTime;

        LordPose(int actionTime) {
            this.getAbilityTime = actionTime;
        }
    }

    //move control
    static class LordMoveControl extends MoveControl {
        private final MoonLord moonLord;
        private int floatDuration;

        public LordMoveControl(MoonLord moonLord) {
            super(moonLord);
            this.moonLord = moonLord;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration = this.floatDuration + this.moonLord.getRandom().nextInt(6) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.moonLord.getX(), this.wantedY - this.moonLord.getY(), this.wantedZ - this.moonLord.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d0))) {
                        this.moonLord.setDeltaMovement(this.moonLord.getDeltaMovement().add(vec3.scale(0.15)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 pos, int length) {
            AABB aabb = this.moonLord.getBoundingBox();

            for (int i = 1; i < length; i++) {
                aabb = aabb.move(pos);
                if (!this.moonLord.level().noCollision(this.moonLord, aabb)) {
                    return false;
                }
            }
            return true;
        }
    }
}