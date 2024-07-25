package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.registers.WrathyArmamentItems;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class BladeOfChaosEntity extends AbstractArrow {
    public int timer;
    private boolean movementFlag;
    private static final EntityDataAccessor<Byte> RETURNING_SPEED = SynchedEntityData.defineId(BladeOfChaosEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<String> BEHAVIOR = SynchedEntityData.defineId(BladeOfChaosEntity.class, EntityDataSerializers.STRING);
    private boolean dealtDamage;
    public int clientSideReturnBladeTickCount;
    private ItemStack blade = new ItemStack(WrathyArmamentItems.BLADE_OF_CHAOS.get());

    public BladeOfChaosEntity(Level level, ItemStack stack, LivingEntity livingEntity, String behavior) {
        super(WrathyArmamentEntities.BLADE_OF_CHAOS.get(), livingEntity, level);
        this.entityData.set(RETURNING_SPEED, (byte) 5);
        this.blade = stack.copy();
        this.entityData.set(BEHAVIOR, behavior);
        this.setNoGravity(!behavior.equals(PossibleBehavior.CRUSH.behaviorName));
        timer = 150;
    }

    public BladeOfChaosEntity(EntityType<BladeOfChaosEntity> type, Level level) {
        super(type, level);
        timer = 150;
        this.entityData.set(RETURNING_SPEED, (byte) 5);
        this.setNoGravity(true);
    }

    public String getBehavior() {
        return this.entityData.get(BEHAVIOR);
    }

    public void tick() {
        if (timer > 0)
            timer--;
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        if (!this.level().isLoaded(this.getOnPos()))
            this.level().setBlockAndUpdate(this.getOnPos(), this.level().getBlockState(this.getOnPos()));
        if (!this.isAcceptibleReturnOwner())
            this.discard();
        Entity entity = this.getOwner();
        int i = this.entityData.get(RETURNING_SPEED);
        double speed = 1.5;
        this.level().addParticle(ParticleTypes.LAVA, this.getX(), this.getY(), this.getZ(), OnActionsTrigger.getXVector(speed, this.getYRot()), OnActionsTrigger.getYVector(speed, this.getXRot()), OnActionsTrigger.getZVector(speed, this.getYRot()));
        if (timer > 100) {
            switch (getBehavior()) {
                case "NEMEAN_CRUSH" -> {

                }
                case "CYCLONE_OF_CHAOS" -> {
                    if (entity != null) {
                        BlockPos pos = entity.getOnPos().above(2);
                        float radius = 4;
                        float angle = (this.tickCount % 360) * 5f;
                        if (angle >= 360.0f)
                            angle -= 180.0f;
                        double x = pos.getX() + radius * Math.cos(angle);
                        double z = pos.getZ() + radius * Math.sin(angle);
                        this.setPos(x, pos.getY(), z);
                        if (this.tickCount % 2 == 0) {
                            this.level().addParticle(ParticleTypes.FLAME, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
                            final Vec3 center = new Vec3(x, pos.getY(), z);
                            List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(2d), e -> true).stream().sorted(Comparator.comparingDouble(entity1 -> entity1.distanceToSqr(center))).toList();
                            for (Entity entityI : entityList) {
                                if (entityI != entity) {
                                    if (entityI instanceof LivingEntity livingEntity) {
                                        livingEntity.hurt(entity.damageSources().thrown(this, entity), 6);
                                        livingEntity.setSecondsOnFire(5);
                                    }
                                }
                            }
                        }
                    }
                }
                case "WRATH_OF_ARTEMIS" -> {
                    if (this.getOwner() != null) {
                        if (this.distanceToSqr(this.getOwner()) > 40 && this.getY() > this.getOwner().getY() + 3)
                            movementFlag = true;
                        if (!movementFlag)
                            this.setDeltaMovement(this.getDeltaMovement().x, 1, this.getDeltaMovement().z);
                        else
                            this.setDeltaMovement(0,-1,0);
                    }
                }
            }
        }

        if (entity != null && timer < 120) {
            this.setNoPhysics(true);
            Vec3 vec3 = entity.getEyePosition().subtract(this.position());
            this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
            if (this.level().isClientSide)
                this.yOld = this.getY();
            if (this.getBehavior().equals(PossibleBehavior.CRUSH.behaviorName)){
                List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(this.getOnPos().getCenter(), this.getOnPos().getCenter()).inflate(2d), e -> true).stream().sorted(Comparator.comparingDouble(entity1 -> entity1.distanceToSqr(this.getOnPos().getCenter()))).toList();
                for (Entity entityI : entityList) {
                    if (entityI != entity) {
                        if (entityI instanceof LivingEntity livingEntity) {
                            livingEntity.hurt(entity.damageSources().thrown(this, entity), 6);
                            livingEntity.setSecondsOnFire(5);
                        }
                    }
                }
            }
            double d0 = 0.05D * (double) i;
            this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
            if (this.clientSideReturnBladeTickCount == 0) {
                this.playSound(SoundEvents.LAVA_EXTINGUISH, 2.0F, 0.5F);
            }
            ++this.clientSideReturnBladeTickCount;
        }
        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        return entity != null && entity.isAlive();
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 vec3, Vec3 vec31) {
        return this.dealtDamage ? null : super.findHitEntity(vec3, vec31);
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float f = 15f;
        if (entity instanceof LivingEntity livingEntity)
            f += EnchantmentHelper.getDamageBonus(this.blade, livingEntity.getMobType());
        Entity owner = this.getOwner();
        DamageSource damagesource = this.damageSources().thrown(this, owner == null ? this : owner);
        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (owner instanceof LivingEntity)
                    entity.hurt(this.damageSources().lava(), f);
                this.doPostHurtEffects(livingEntity);
                this.playSound(SoundEvents.LAVA_EXTINGUISH, 2.0F, 0.5F);
            }
        }
        if (!this.getBehavior().equals(PossibleBehavior.CRUSH.behaviorName))
            this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (this.getBehavior().equals(PossibleBehavior.WRATH.behaviorName) && movementFlag){
            this.level().explode(this,this.getX(),this.getY(),this.getZ(),3,false, Level.ExplosionInteraction.NONE);
            OnActionsTrigger.addParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,this.level(),this.getX(),this.getY()+1,this.getZ(),1.5f);
            this.setDeltaMovement(0,0.5f,0);
            this.movementFlag = false;
        }
        else
            super.onHitBlock(pResult);
    }

    @Override
    protected ItemStack getPickupItem() {
        return blade.copy();
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.LAVA_POP;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player))
            super.playerTouch(player);
        else if (this.getOwner() == null)
            this.spawnAtLocation(blade.copy());
    }

    @Override
    public boolean tryPickup(Player pPlayer) {
        return switch (this.pickup) {
            case ALLOWED -> addItemToPlayer(pPlayer);
            case CREATIVE_ONLY -> pPlayer.getAbilities().instabuild;
            default -> false;
        };
    }

    private boolean addItemToPlayer(Player player) {
        if (player.getMainHandItem().isEmpty()) {
            player.setItemSlot(EquipmentSlot.MAINHAND, this.getPickupItem());
            return true;
        } else if (player.getOffhandItem().isEmpty()) {
            player.setItemSlot(EquipmentSlot.OFFHAND, this.getPickupItem());
            return true;
        } else
            return player.getInventory().add(this.getPickupItem());
    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RETURNING_SPEED, (byte) 0);
        this.entityData.define(BEHAVIOR, "");
    }

    public enum PossibleBehavior {
        CRUSH("NEMEAN_CRUSH"),
        CYCLONE("CYCLONE_OF_CHAOS"),
        WRATH("WRATH_OF_ARTEMIS");
        public final String behaviorName;

        PossibleBehavior(String behaviorName) {
            this.behaviorName = behaviorName;
        }
    }
}
