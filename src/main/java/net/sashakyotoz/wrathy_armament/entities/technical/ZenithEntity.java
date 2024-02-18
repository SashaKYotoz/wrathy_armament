package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentEntities;
import net.sashakyotoz.wrathy_armament.utils.WrathyArmamentParticleTypes;

import javax.annotation.Nullable;

public class ZenithEntity extends AbstractArrow {
    public int timer;
    private static final EntityDataAccessor<Byte> RETURNING_SPEED = SynchedEntityData.defineId(ZenithEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> INDEX = SynchedEntityData.defineId(ZenithEntity.class, EntityDataSerializers.INT);
    private boolean dealtDamage;
    public int clientSideReturnBladeTickCount;

    public ZenithEntity(Level level, LivingEntity livingEntity,int Index) {
        super(WrathyArmamentEntities.ZENITH.get(), livingEntity, level);
        this.entityData.set(RETURNING_SPEED, (byte) 4);
        this.entityData.set(INDEX,Index);
        this.setNoGravity(true);
        timer = 120;
    }

    public ZenithEntity(EntityType<ZenithEntity> type, Level world) {
        super(type, world);
        timer = 120;
    }
    public int getIndex(){
        return this.entityData.get(INDEX);
    }

    public void tick() {
        if (timer > 0)
            timer--;
        else
            this.discard();
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }
        Entity entity = this.getOwner();
        int i = this.entityData.get(RETURNING_SPEED);
        if(timer > 50){
            double speed = 1.5;
            double Yaw = this.getYRot();
            this.level().addParticle(WrathyArmamentParticleTypes.ZENITH_WAY.get(),this.getX(),this.getY(),this.getZ(),(speed * Math.cos((Yaw + 90) * (Math.PI / 180))),(this.getXRot() * (-0.025)),(speed * Math.sin((Yaw + 90) * (Math.PI / 180))));
        }
        if (entity != null && timer < 100) {
            if (!this.isAcceptibleReturnOwner()) {
                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }
                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnBladeTickCount == 0) {
                    this.playSound(SoundEvents.ELYTRA_FLYING, 5.0F, 0.5F);
                }
                ++this.clientSideReturnBladeTickCount;
            }
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

    @Override
    protected void onInsideBlock(BlockState state) {
        super.onInsideBlock(state);
        setNoPhysics(!state.is(Blocks.AIR));
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        setNoPhysics(!level().getBlockState(blockHitResult.getBlockPos()).is(Blocks.AIR));
    }

    protected void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        float f = 15f;
        Entity owner = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, owner == null ? this : owner);
        this.dealtDamage = true;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity) {
                if (owner instanceof LivingEntity) {
                    entity.hurt(this.damageSources().magic(),f);
                }
                this.doPostHurtEffects(livingEntity);
            }
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
    }
    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.ANVIL_HIT;
    }

    public void playerTouch(Player player) {
        if (this.ownedBy(player) || this.getOwner() == null) {
            super.playerTouch(player);
        }
    }
    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double p_37588_, double p_37589_, double p_37590_) {
        return true;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(RETURNING_SPEED, (byte) 0);
        this.entityData.define(INDEX, 0);
    }
}
