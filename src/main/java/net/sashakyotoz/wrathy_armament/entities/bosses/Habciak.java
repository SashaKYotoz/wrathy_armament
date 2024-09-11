package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.habciak.HabciakBypassGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.habciak.HabciakFloorAttackGoal;
import net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.habciak.HabciakMirrorCastingGoal;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

public class Habciak extends BossLikePathfinderMob {
    private final ServerBossEvent bossEvent = new ServerBossEvent(Component.translatable("boss.wrathy_armament.habciak"), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.NOTCHED_6);
    public final AnimationState death = new AnimationState();
    public final AnimationState jump = new AnimationState();
    public final AnimationState floorAttack = new AnimationState();
    public final AnimationState backFlip = new AnimationState();
    public final AnimationState mirrorCasting = new AnimationState();
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.5D, true);
    private final HabciakBypassGoal jumpGoal = new HabciakBypassGoal(this);
    private final HabciakFloorAttackGoal floorAttackGoal = new HabciakFloorAttackGoal(this);
    private final HabciakMirrorCastingGoal mirrorCastingGoal = new HabciakMirrorCastingGoal(this);
    public int backFlipRotation = 0;

    public Habciak(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.navigation = new AmphibiousPathNavigation(this, this.level());
    }

    public Player playerToRender() {
        return this.getTarget() instanceof Player player ? player : level().getNearestPlayer(this, 16);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1, true));
        super.registerGoals();
    }

    public void reassessAttackGoal(int i) {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.jumpGoal);
            this.goalSelector.removeGoal(this.floorAttackGoal);
            this.goalSelector.removeGoal(this.mirrorCastingGoal);
            switch (i) {
                default -> this.goalSelector.addGoal(3, meleeGoal);
                case 1 -> this.goalSelector.addGoal(3, jumpGoal);
                case 2 -> this.goalSelector.addGoal(3, floorAttackGoal);
                case 3 -> this.goalSelector.addGoal(3, mirrorCastingGoal);
            }
        }
    }

    public boolean doBackFlip() {
        return !onGround() && this.goalSelector.getRunningGoals().anyMatch(goal -> goal.getGoal() instanceof HabciakMirrorCastingGoal);
    }

    @Override
    protected void updateWalkAnimation(float pPartialTick) {
        float f = Math.min(pPartialTick * 4.0F, 1.0F);
        this.walkAnimation.update(this.onGround() ? f : 0, 0.4F);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isFallFlying() && this.doBackFlip())
            this.backFlip.startIfStopped(this.tickCount);
        if (backFlipRotation > 0)
            backFlipRotation--;
        if (this.tickCount % 5 == 0) {
            if (this.getTarget() instanceof Player player) {
                if (this.goalSelector.getRunningGoals().noneMatch(goal -> goal.getGoal() instanceof HabciakBypassGoal))
                    this.setItemInHand(InteractionHand.MAIN_HAND, player.getMainHandItem());
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
                if (player.getUseItem().getUseDuration() > 60) {
                    this.startUsingItem(player.getUsedItemHand());
                }
            }
            if (this.getRandom().nextInt(0, 19) == 5)
                this.reassessAttackGoal(this.getRandom().nextInt(4));
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
        this.deathTime = -100;
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
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 10)
                .add(Attributes.ARMOR_TOUGHNESS, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }
}
