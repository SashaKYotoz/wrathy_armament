package net.sashakyotoz.wrathy_armament.entities.alive;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sashakyotoz.wrathy_armament.entities.bosses.BossLikePathfinderMob;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;

public class Guide extends PathfinderMob implements RangedAttackMob {
    private final RangedBowAttackGoal<Guide> bowGoal = new RangedBowAttackGoal<>(this, 1.0D, 20, 15.0F);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2D, false);

    public Guide(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        GuideNames[] poses = GuideNames.values();
        GuideNames currentPose = poses[random.nextInt(poses.length)];
        setCustomName(Component.literal(currentPose.gName));
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, BossLikePathfinderMob.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(4, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true));
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        return pEntity instanceof Guide || pEntity instanceof Player;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount % 500 == 0) {
            this.setItemInHand(InteractionHand.MAIN_HAND, this.random.nextBoolean() ? new ItemStack(Items.BOW) : new ItemStack(Items.IRON_SWORD));
            this.reassessWeaponGoal();
        }
        if (this.getTarget() != null) {
            if (this.tickCount % 60 == 0 && this.getHealth() < this.getMaxHealth() && !this.hasEffect(MobEffects.REGENERATION)) {
                if (!this.level().isClientSide())
                    this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 1));
            }
            if (this.tickCount % 5 == 0) {
                Player player = this.level().getNearestPlayer(this, 48);
                if (player != null && player.getLastHurtByMob() != null)
                    this.setTarget(player.getLastHurtByMob());
            }
        }
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        ItemStack itemstack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
        AbstractArrow abstractarrow = ProjectileUtil.getMobArrow(this, itemstack, pDistanceFactor);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem)
            abstractarrow = ((BowItem) this.getMainHandItem().getItem()).customArrow(abstractarrow);
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.34D) - abstractarrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem));
            if (itemstack.is(Items.BOW)) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }
                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }
    }

    @Override
    public void die(DamageSource source) {
        if (source.getEntity() instanceof Player player
                && this.level().canSeeSky(this.getOnPos().above())
                && this.level().isNight())
            player.drop(WrathyArmamentItems.LUNAR_VOODOO_DOLL.get().getDefaultInstance(), true, false);
        super.die(source);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return super.hurt(pSource, pAmount) && pSource.getEntity() instanceof Player;
    }

    enum GuideNames {
        ANDREW("Andrew"),
        ASHER("Asher"),
        BRANDON("Brandon"),
        BRETT("Brett"),
        BRIAN("Brian"),
        CODY("Cody"),
        COLIN("Colin"),
        CONNOR("Connor"),
        DYLAN("Dylan"),
        DANIEL("Daniel"),
        JACK("Jack"),
        KYLE("Kyle"),
        MARTY("Marty"),
        RYAN("Ryan"),
        TANNER("Tanner"),
        ZACH("Zach"),
        WYATT("Wyatt"),
        SCOT("Scot"),
        BRADLY("Bradly");

        GuideNames(String gName) {
            this.gName = gName;
        }

        public final String gName;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.ARMOR_TOUGHNESS, 2)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }
}