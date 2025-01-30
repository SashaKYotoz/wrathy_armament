package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.HabciakMovementGoal;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentMiscRegistries;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class Habciak extends BossLikePathfinderMob {
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.habciak"), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_6);
    private static final EntityDataAccessor<HabciakPose> DATA_HABCIAK_POSE = SynchedEntityData.defineId(Habciak.class, WrathyArmamentMiscRegistries.HABCIAK_POSE.get());
    public final AnimationState death = new AnimationState();
    public final AnimationState jump = new AnimationState();
    public final AnimationState floorAttack = new AnimationState();
    public final AnimationState backFlip = new AnimationState();
    public final AnimationState mirrorCasting = new AnimationState();
    public final AnimationState splash = new AnimationState();
    public int backFlipRotation = 0;

    public Habciak(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.navigation = new AmphibiousPathNavigation(this, this.level());
    }

    public Player playerToRender() {
        return this.getTarget() instanceof Player player ? player : level().getNearestPlayer(this, 24);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new HabciakMovementGoal(this));
        super.registerGoals();
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f = Math.min(pPartialTick * 4.0F, 1.0F);
        this.walkAnimation.update(this.onGround() ? f : 0, 0.4F);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_HABCIAK_POSE, HabciakPose.IDLING);
        super.defineSynchedData();
    }

    private void setHabciakPose(HabciakPose knightPose) {
        this.entityData.set(DATA_HABCIAK_POSE, knightPose);
    }

    public HabciakPose getHabciakPose() {
        return this.entityData.get(DATA_HABCIAK_POSE);
    }

    public boolean isInPose(HabciakPose phase) {
        return this.getHabciakPose() == phase;
    }

    @Override
    public void tick() {
        super.tick();
        if (backFlipRotation > 0)
            backFlipRotation--;
        if (this.tickCount % 5 == 0) {
            if (this.getTarget() != null && this.isInPose(HabciakPose.IDLING))
                changePose();
            if (this.getHealth() < 480 && this.getItemInHand(InteractionHand.MAIN_HAND).is(Items.ENCHANTED_GOLDEN_APPLE)) {
                this.heal(1);
                this.playSound(SoundEvents.GENERIC_EAT, 1.5f, 2);
            }
            if (this.getTarget() instanceof Player player) {
                this.setItemSlot(EquipmentSlot.HEAD, player.getItemBySlot(EquipmentSlot.HEAD));
                this.setItemSlot(EquipmentSlot.CHEST, player.getItemBySlot(EquipmentSlot.CHEST));
                if (this.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ElytraItem) {
                    if (this.onGround() && this.getRandom().nextInt(0, 11) == 5)
                        this.addDeltaMovement(new Vec3(0, 1f, 0));
                    if (!this.onGround())
                        this.setSharedFlag(7, true);
                    if (this.isFallFlying())
                        this.setDeltaMovement(new Vec3(
                                OnActionsTrigger.getXVector(0.25f, this.getYRot()),
                                this.getDeltaMovement().y(),
                                OnActionsTrigger.getZVector(0.25f, this.getYRot())));
                }
                this.setItemSlot(EquipmentSlot.LEGS, player.getItemBySlot(EquipmentSlot.LEGS));
                this.setItemSlot(EquipmentSlot.FEET, player.getItemBySlot(EquipmentSlot.FEET));
                this.setItemSlot(EquipmentSlot.OFFHAND, player.getItemBySlot(EquipmentSlot.OFFHAND));
                if (!this.isInPose(HabciakPose.BYPASSING))
                    this.setItemInHand(InteractionHand.MAIN_HAND, this.isInPose(HabciakPose.MIRROR_CASTING)
                            ? WrathyArmamentItems.MIRROR_SWORD.get().getDefaultInstance()
                            : player.getMainHandItem());
            }
        }
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> dataAccessor) {
        if (DATA_HABCIAK_POSE.equals(dataAccessor)) {
            if (!this.isInPose(HabciakPose.MIRROR_CASTING))
                this.mirrorCasting.stop();
            if (!this.isInPose(HabciakPose.JUMPING))
                this.jump.stop();
            if (!this.isInPose(HabciakPose.BACKFLIPPING))
                this.backFlip.stop();
            switch (this.getHabciakPose()) {
                case JUMPING -> {
                    this.jump.start(this.tickCount);
                    this.spawnParticle(ParticleTypes.CLOUD, this.level(), this.getX(), this.getY() + 0.5f, this.getZ(), 1.5f);
                    queueServerWork(30, this::changePose);
                }
                case BACKFLIPPING -> {
                    this.backFlip.start(this.tickCount);
                    queueServerWork(20, () -> this.setDeltaMovement(
                            -this.getXVector(1, this.getYRot()),
                            0.75f,
                            -this.getZVector(1, this.getYRot())
                    ));
                    queueServerWork(50, this::changePose);
                }
                case ATTACKING -> {
                    this.floorAttack.start(this.tickCount);
                    queueServerWork(50, () -> {
                        if (this.getTarget() != null && this.getTarget().distanceToSqr(this) < 9)
                            doEnchantDamageEffects(this, this.getTarget());
                    });
                    queueServerWork(60, this::changePose);
                }
                case BYPASSING -> {
                    this.setItemInHand(InteractionHand.MAIN_HAND, Items.ENCHANTED_GOLDEN_APPLE.getDefaultInstance());
                    this.startUsingItem(InteractionHand.MAIN_HAND);
                    queueServerWork(100, this::changePose);
                }
                case MIRROR_CASTING -> {
                    this.mirrorCasting.start(this.tickCount);
                    queueServerWork(50, () -> {
                        shootTakenDamage();
                        changePose();
                    });
                }
                case SPLASH -> {
                    this.splash.start(this.tickCount);
                    this.playSound(SoundEvents.FROG_LONG_JUMP, 1.5f, 2f);
                    queueServerWork(20, () -> this.setDeltaMovement(new Vec3(
                            this.getXVector(0.75f, this.getYRot()),
                            0.65,
                            this.getZVector(0.75f, this.getYRot())
                    )));
                    queueServerWork(40, () -> {
                        this.hitNearbyMobs(12, 7);
                        changePose();
                    });
                }
            }
        }
        super.onSyncedDataUpdated(dataAccessor);
    }

    private void changePose() {
        switch (this.getHabciakPose()) {
            case IDLING, BYPASSING -> this.setHabciakPose(HabciakPose.ATTACKING);
            case ATTACKING -> this.setHabciakPose(HabciakPose.BACKFLIPPING);
            case BACKFLIPPING -> this.setHabciakPose(HabciakPose.JUMPING);
            case JUMPING -> this.setHabciakPose(HabciakPose.MIRROR_CASTING);
            case MIRROR_CASTING -> this.setHabciakPose(HabciakPose.SPLASH);
            case SPLASH -> this.setHabciakPose(HabciakPose.BYPASSING);
        }
    }

    private void shootTakenDamage() {
        if (this.isInPose(HabciakPose.MIRROR_CASTING)) {
            Vec3 eyePos = this.getEyePosition();
            Vec3 lookVec = this.getViewVector(1.0F);
            Vec3 endPos = eyePos.add(lookVec.scale(9));
            int particleCount = 20;
            Vec3 step = endPos.subtract(eyePos).scale(1.0 / particleCount);
            double radius = 0.25;
            for (int v = 0; v < 180; v++) {
                for (int j = 0; j < particleCount; j++) {
                    Vec3 basePos = eyePos.add(step.scale(j));
                    double angle = (v) * Math.PI / 20 + (j * Math.PI / 10);
                    double offsetX = radius * Math.cos(angle);
                    double offsetY = radius * Math.sin(angle);
                    Vec3 perpendicular = lookVec.cross(new Vec3(0, 1, 0)).normalize();
                    Vec3 particlePos = basePos.add(perpendicular.scale(offsetX)).add(0, offsetY, 0);
                    this.level().addParticle(
                            ParticleTypes.CLOUD,
                            particlePos.x,
                            particlePos.y,
                            particlePos.z,
                            0, 0, 0
                    );
                }
            }
            this.hitNearbyMobs(12, 9);
        }
    }

    @Override
    protected void dropEquipment() {
        this.spawnAtLocation(WrathyArmamentItems.MIRROR_SWORD.get());
    }

    @Override
    protected void tickDeath() {
        float sin = (float) Math.sin(deathTime * Math.PI / 10);
        float cos = (float) Math.cos(deathTime * Math.PI / 10);
        float tan = (float) Math.tan(deathTime * Math.PI / 10);
        if (this.level().isClientSide)
            this.level().addParticle(ParticleTypes.END_ROD,
                    this.getX() + sin, this.getY() + 0.5 + tan * cos * sin, this.getZ() + cos, 0, 0, 0);
        if (deathTime == 0)
            this.death.start(this.tickCount);
        super.tickDeath();
    }

    @Override
    public void die(DamageSource source) {
        this.setHabciakPose(HabciakPose.DYING);
        this.deathTime = -80;
        super.die(source);
    }

    @Override
    public ServerBossEvent bossInfo() {
        return bossEvent;
    }

    //attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.ARMOR_TOUGHNESS, 8)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    public enum HabciakPose {
        DYING,
        ATTACKING,
        MIRROR_CASTING,
        BACKFLIPPING,
        BYPASSING,
        JUMPING,
        SPLASH,
        IDLING
    }
}