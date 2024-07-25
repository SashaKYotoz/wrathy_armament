package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import net.sashakyotoz.wrathy_armament.entities.bosses.JohannesKnight;
import net.sashakyotoz.wrathy_armament.utils.OnActionsTrigger;

import java.util.Comparator;
import java.util.List;

public class HarmfulProjectileEntity extends VanishableLikeEntity {
    private static final EntityDataAccessor<String> PROJECTILE_TYPE = SynchedEntityData.defineId(HarmfulProjectileEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DAMAGE = SynchedEntityData.defineId(HarmfulProjectileEntity.class, EntityDataSerializers.INT);
    private Vec3 vec3 = null;
    public static final EntityDimensions LARGE_SIZE = EntityDimensions.fixed(2.5F, 2.5F);
    public static final EntityDimensions HUGE_SIZE = EntityDimensions.fixed(3F, 3F);

    public HarmfulProjectileEntity(EntityType<?> type, Level level, int damage, String s) {
        super(type, level);
        this.entityData.set(DAMAGE, damage);
        this.setProjectileType(s);
    }

    public HarmfulProjectileEntity(EntityType<HarmfulProjectileEntity> type, Level level) {
        super(type, level);
    }

    public String getProjectileType() {
        return this.entityData.get(PROJECTILE_TYPE);
    }

    private int getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setProjectileType(String s) {
        this.entityData.set(PROJECTILE_TYPE, s);
    }

    private void onHit(HitResult result) {
        HitResult.Type resultType = result.getType();
        if (resultType == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) result);
            this.level().gameEvent(GameEvent.PROJECTILE_LAND, result.getLocation(), GameEvent.Context.of(this, null));
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return switch (this.getProjectileType()){
            default -> super.getDimensions(pose);
            case "huge_sword" -> LARGE_SIZE;
            case "shield_dash" -> HUGE_SIZE;
        };
    }

    private void onHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        if (entity instanceof LivingEntity livingEntity && this.owner != null) {
            livingEntity.hurt(this.owner.damageSources().mobAttack(this.owner), this.getDamage());
            if (this.getProjectileType().equals("shield_dash")) {
                livingEntity.setDeltaMovement(this.getXVector(this.getYRot()), this.getYVector(this.getXRot()) + 0.25f, this.getZVector(this.getYRot()));
                if (!this.level().isClientSide())
                    this.level().explode(this, this.getX(), this.getY(), this.getZ(), 4, Level.ExplosionInteraction.NONE);
            }
            this.discard();
        }
    }

    private boolean canHitEntity(Entity entity1) {
        if (!entity1.canBeHitByProjectile()) {
            return false;
        } else {
            Entity entity = this.owner;
            return entity == null || !entity.isPassengerOfSameVehicle(entity1);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.timeToVanish < 80 && this.getProjectileType().equals("axe"))
            this.timeToVanish++;
        else if (this.timeToVanish < 100 && (this.getProjectileType().equals("dagger") || this.getProjectileType().equals("knight_dagger")))
            this.timeToVanish++;
        else if (this.timeToVanish < 90 && this.getProjectileType().equals("knight_axe"))
            this.timeToVanish++;
        else if (this.timeToVanish < 200 && this.getProjectileType().equals("huge_sword"))
            this.timeToVanish++;
        else if (this.timeToVanish < 30 && this.getProjectileType().equals("shield_dash"))
            this.timeToVanish++;
        else
            discard();
        if (this.timeToVanish > 15) {
            this.refreshDimensions();
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.owner == null && (this.getProjectileType().equals("dagger") || this.getProjectileType().equals("axe") || this.getProjectileType().equals("shield_dash")))
                this.owner = this.getNearestPlayer();
            if (this.owner == null && (this.getProjectileType().equals("knight_dagger") || this.getProjectileType().equals("knight_axe")))
                this.owner = this.getNearestKnight();
            if (this.tickCount % 5 == 0){
                Vec3 vec31 = this.getDeltaMovement();
                this.level().addParticle(ParticleTypes.CRIT, this.getX() - vec31.x, this.getY() - vec31.y + 0.5D, this.getZ() - vec31.z, 0.0D, 0.0D, 0.0D);
            }
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            boolean flag = false;
            if (hitresult.getType() == HitResult.Type.BLOCK) {
                BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
                BlockState blockstate = this.level().getBlockState(blockpos);
                if (blockstate.is(Blocks.NETHER_PORTAL)) {
                    this.handleInsidePortal(blockpos);
                    flag = true;
                } else if (blockstate.is(Blocks.END_GATEWAY)) {
                    BlockEntity blockentity = this.level().getBlockEntity(blockpos);
                    if (blockentity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                        TheEndGatewayBlockEntity.teleportEntity(this.level(), blockpos, blockstate, this, (TheEndGatewayBlockEntity) blockentity);
                    }
                    flag = true;
                } else
                    this.discard();
            }
            if (hitresult.getType() != HitResult.Type.MISS && !flag)
                this.onHit(hitresult);
            switch (this.getProjectileType()) {
                case "axe", "knight_axe" -> {
                    if (this.vec3 == null && this.owner != null)
                        this.vec3 = new Vec3(this.getXVector(this.owner.getYRot()), -0.16D, this.getZVector(this.owner.getYRot()));
                    if (this.vec3 != null)
                        this.setDeltaMovement(vec3);
                }
                case "shield_dash"->{
                    if (this.vec3 == null && this.owner != null)
                        this.vec3 = new Vec3(this.getXVector(this.owner.getYRot()), 0, this.getZVector(this.owner.getYRot()));
                    if (this.vec3 != null) {
                        this.setDeltaMovement(vec3);
                        if (this.tickCount % 10 == 0 && this.level() instanceof ServerLevel level)
                            level.sendParticles(ParticleTypes.END_ROD,this.getX(),this.getY(),this.getZ(),3,this.getXVector(-this.getYRot()),this.getYVector(this.getXRot()),this.getZVector(-this.getYRot()),1.5f);
                    }
                }
                case "dagger", "knight_dagger" -> {
                    if (this.vec3 == null && this.owner != null)
                        this.vec3 = new Vec3(this.getXVector(this.owner.getYRot()), 0f, this.getZVector(this.owner.getYRot()));
                    if (this.vec3 != null)
                        this.setDeltaMovement(vec3);
                }
                case "huge_sword" -> {
                    swordKnockback();
                    this.setDeltaMovement(0,-0.15,0);
                    if (this.timeToVanish < 120 && this.tickCount % 5 == 0)
                        this.clientDiggingParticles();
                }
            }
        }
    }

    private void clientDiggingParticles() {
        RandomSource randomsource = this.random;
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

    private void swordKnockback() {
        if (this.owner != null) {
            final Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
            List<LivingEntity> entityList = this.level().getEntitiesOfClass(LivingEntity.class, new AABB(center, center).inflate(3), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
            for (LivingEntity entityIterator : entityList) {
                if (!(entityIterator instanceof JohannesKnight)) {
                    entityIterator.hurt(this.damageSources().mobAttack(this.owner), 8);
                    entityIterator.setDeltaMovement(OnActionsTrigger.getXVector(2, 180 - entityIterator.getYRot()), 0.5f, OnActionsTrigger.getZVector(2, 180 - entityIterator.getYRot()));
                }
            }
        }
    }

    public JohannesKnight getNearestKnight() {
        final Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(24), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
        for (Entity entityIterator : entityList) {
            if (entityIterator instanceof JohannesKnight johannesKnight)
                return johannesKnight;
        }
        return null;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PROJECTILE_TYPE, "dagger");
        this.entityData.define(DAMAGE, 5);
    }
}