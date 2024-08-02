package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.sashakyotoz.anitexlib.client.particles.parents.options.ColorableParticleOption;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord.MoonLordLasering;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord.MoonLordMeleeAttack;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord.MoonLordShooting;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord.MoonLordTeleport;
import net.sashakyotoz.wrathy_armament.entities.bosses.parts.MoonLordPart;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MoonLord extends BossLikePathfinderMob {
    private static final EntityDataAccessor<Float> ANIMATION_SCALING = SynchedEntityData.defineId(MoonLord.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> IS_LASER_ACTIVATED = SynchedEntityData.defineId(MoonLord.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<MoonLord.LordPose> DATA_LORD_POSE = SynchedEntityData.defineId(MoonLord.class, WrathyArmamentMiscRegistries.LORD_POSE.get());
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("entity.wrathy_armament.moon_lord"), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_20);
    private final MoonLordPart[] subEntities;
    private final MoonLordPart headEye;
    private final MoonLordPart heart;
    private final MoonLordPart rightHandEye;
    private final MoonLordPart leftHandEye;
    public final AnimationState death = new AnimationState();
    public final AnimationState meleeAttack = new AnimationState();
    public final AnimationState eyeAttack = new AnimationState();
    public final AnimationState interactive = new AnimationState();
    private static final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = (entity) -> entity.getMobType() != MobType.UNDEAD && entity.attackable();

    public MoonLord(EntityType<? extends BossLikePathfinderMob> type, Level level) {
        super(type, level);
        this.xpReward = 500;
        this.moveControl = new FlyingMoveControl(this, 15, true);
        this.headEye = new MoonLordPart(this, "headEye", 1.5f, 1.5f);
        this.heart = new MoonLordPart(this, "heart", 2.5f, 2.5f);
        this.rightHandEye = new MoonLordPart(this, "rightHandEye", 1.5f, 1.5f);
        this.leftHandEye = new MoonLordPart(this, "leftHandEye", 1.5f, 1.5f);
        this.subEntities = new MoonLordPart[]{this.headEye, this.heart, this.rightHandEye, this.leftHandEye};
        this.setId(ENTITY_COUNTER.getAndAdd(this.subEntities.length + 1) + 1);
    }

    @Override
    public int getHeadRotSpeed() {
        return 8;
    }

    //entity data
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_SCALING, 1f);
        this.entityData.define(IS_LASER_ACTIVATED, false);
        this.entityData.define(DATA_LORD_POSE, LordPose.IDLING);
    }

    public float getAnimationScaling() {
        return this.entityData.get(ANIMATION_SCALING);
    }

    public void setAnimationScaling(float f) {
        this.entityData.set(ANIMATION_SCALING, f);
    }

    public boolean isLaserActivated() {
        return this.entityData.get(IS_LASER_ACTIVATED);
    }

    public void setIsLaserActivated(boolean b) {
        this.entityData.set(IS_LASER_ACTIVATED, b);
    }

    public LordPose getDataLordPose() {
        return this.entityData.get(DATA_LORD_POSE);
    }

    public void setDataLordPose(LordPose pose) {
        this.entityData.set(DATA_LORD_POSE, pose);
    }

    //combat management

    @Override
    public void onEnterCombat() {
        if (this.getDataLordPose().equals(LordPose.IDLING))
            this.setRandomLordPose();
    }

    @Override
    public void onLeaveCombat() {
        if (this.getHealth() < this.getMaxHealth() && this.random.nextBoolean())
            OnActionsTrigger.queueServerWork(30, () -> this.setDataLordPose(LordPose.IDLING));
    }
    @Override
    protected PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }

    public void applyBrightnessAround(ServerLevel level, Vec3 vec3, @Nullable Entity sourceEntity, int i) {
        MobEffectInstance mobeffectinstance = new MobEffectInstance(WrathyArmamentMiscRegistries.BRIGHTNESS.get(), 60, 1, false, false);
        MobEffectUtil.addEffectToPlayersAround(level, sourceEntity, vec3, i, mobeffectinstance, 50);
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
        if (this.tickCount % 5 == 0 && this.getDataLordPose().equals(LordPose.IDLING))
            this.setRandomLordPose();
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
    }

    private void tickPart(MoonLordPart moonLordPart, double pOffsetX, double pOffsetY, double pOffsetZ) {
        moonLordPart.setPos(this.getX() + pOffsetX, this.getY() + pOffsetY, this.getZ() + pOffsetZ);
    }

    public void setRandomLordPose() {
        int random = this.random.nextIntBetweenInclusive(0, 2);
        switch (this.getDataLordPose()) {
            case SHOOTING -> {
                switch (random) {
                    default -> this.setDataLordPose(LordPose.ATTACKING);
                    case 1 -> this.setDataLordPose(LordPose.LASERING);
                    case 2 -> this.setDataLordPose(LordPose.TELEPORTING);
                }
            }
            case ATTACKING -> {
                switch (random) {
                    default -> this.setDataLordPose(LordPose.SHOOTING);
                    case 1 -> this.setDataLordPose(LordPose.LASERING);
                    case 2 -> this.setDataLordPose(LordPose.TELEPORTING);
                }
            }
            case LASERING -> {
                switch (random) {
                    default -> this.setDataLordPose(LordPose.SHOOTING);
                    case 1 -> this.setDataLordPose(LordPose.ATTACKING);
                    case 2 -> this.setDataLordPose(LordPose.TELEPORTING);
                }
            }
            case TELEPORTING -> {
                switch (random) {
                    default -> this.setDataLordPose(LordPose.SHOOTING);
                    case 1 -> this.setDataLordPose(LordPose.ATTACKING);
                    case 2 -> this.setDataLordPose(LordPose.LASERING);
                }
            }
            case IDLING -> this.setDataLordPose(LordPose.ATTACKING);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getDataLordPose().equals(LordPose.ATTACKING) && !this.meleeAttack.isStarted())
            this.meleeAttack.start(this.tickCount);
        if (level().isClientSide()){
            if (this.getDataLordPose().equals(LordPose.IDLING) ||
                    this.getDataLordPose().equals(LordPose.LASERING))
                this.interactive.startIfStopped(this.tickCount);
            else
                this.interactive.stop();
            if (this.getDataLordPose().equals(LordPose.SHOOTING) && !this.eyeAttack.isStarted())
                this.eyeAttack.start(this.tickCount);
            if (this.getDataLordPose().equals(LordPose.ATTACKING) && !this.meleeAttack.isStarted())
                this.meleeAttack.start(this.tickCount);
        }
    }

    @Override
    public boolean isDeadOrDying() {
        return super.isDeadOrDying() || this.getDataLordPose().equals(LordPose.DYING);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return super.getStandingEyeHeight(pPose, pDimensions) + 2.75f;
    }

    @Override
    public void push(double pX, double pY, double pZ) {
    }

    @Override
    public void registerGoals() {
        this.goalSelector.addGoal(1,new LookAtPlayerGoal(this, Player.class,32,0.5f));
        this.goalSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class,0, false, false,LIVING_ENTITY_SELECTOR));
        this.goalSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new MoonLordMeleeAttack(this));
        this.targetSelector.addGoal(3, new MoonLordTeleport(this));
        this.targetSelector.addGoal(3, new MoonLordLasering(this));
        this.targetSelector.addGoal(3, new MoonLordShooting(this));
    }

    @Override
    protected void setRot(float pYRot, float pXRot) {
        super.setRot(pYRot * (this.getDataLordPose().equals(LordPose.LASERING) ? 0.65f : 1), pXRot);
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
    protected void tickDeath() {
        if (!this.death.isStarted())
            this.death.start(this.tickCount);
        if (deathTime == 10 && this.level() instanceof ServerLevel level) {
            this.spawnParticle(new ColorableParticleOption("sparkle", 0.25f, 0.35f, 1f), this.level(), this.getX(), this.getY(), this.getZ(), 5);
            this.applyBrightnessAround(level, this.getOnPos().getCenter(), this, 16);
        }
        super.tickDeath();
    }

    @Override
    public void die(DamageSource source) {
        this.deathTime = -100;
        this.setDataLordPose(LordPose.DYING);
        super.die(source);
    }

    //attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 750.0D)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 16)
                .add(Attributes.ARMOR_TOUGHNESS, 16)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }
    //poses
    public enum LordPose {
        DYING,
        ATTACKING,
        LASERING,
        SHOOTING,
        TELEPORTING,
        IDLING;
    }
}