package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.alive.LichMyrmidon;
import net.sashakyotoz.wrathy_armament.entities.bosses.LichKing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

public class ParticleLikeEntity extends VanishableLikeEntity {
    private ZenithEntity zenith;
    private LichKing lich;
    private static final EntityDataAccessor<Boolean> TO_MODIFY_SIZE = SynchedEntityData.defineId(ParticleLikeEntity.class,EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> TURN_PARTICLE = SynchedEntityData.defineId(ParticleLikeEntity.class,EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ID_OF_COLOR = SynchedEntityData.defineId(ParticleLikeEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(ParticleLikeEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<String> PARTICLE_TYPE = SynchedEntityData.defineId(ParticleLikeEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<ParticleOptions> TAIL_PARTICLE = SynchedEntityData.defineId(ParticleLikeEntity.class, EntityDataSerializers.PARTICLE);
    public ParticleLikeEntity(EntityType<?> type, Level level, float size, boolean modifySize, boolean turnParticle, int idOfColor, ParticleOptions particleOptions, String particleType) {
        super(type, level);
        this.setStandardSize(size);
        this.setToModifySize(modifySize);
        this.setToTurnParticle(turnParticle);
        this.setTailParticle(particleOptions);
        this.setParticleType(particleType);
        this.entityData.set(ID_OF_COLOR,idOfColor);
        this.noPhysics = true;
    }

    public ParticleLikeEntity(EntityType<ParticleLikeEntity> type, Level level) {
        super(type, level);
        this.setStandardSize(1);
        this.entityData.set(ID_OF_COLOR,3);
        this.setToModifySize(true);
        this.setParticleType("cycle");
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(TO_MODIFY_SIZE,false);
        this.entityData.define(TURN_PARTICLE,false);
        this.entityData.define(SIZE,1f);
        this.entityData.define(PARTICLE_TYPE,"cycle");
        this.entityData.define(TAIL_PARTICLE, ParticleTypes.ASH);
        this.entityData.define(ID_OF_COLOR,0);
    }
    //getters
    public float getSize(){
        return this.entityData.get(SIZE);
    }
    public String getParticleType(){
        return this.entityData.get(PARTICLE_TYPE);
    }
    public int getIdOfColor(){
        return this.entityData.get(ID_OF_COLOR);
    }
    public boolean toModifySize(){
        return this.entityData.get(TO_MODIFY_SIZE);
    }
    public boolean toTurnParticle(){
        return this.entityData.get(TURN_PARTICLE);
    }
    private ParticleOptions tailParticle(){
        return this.entityData.get(TAIL_PARTICLE);
    }
    //setters
    private void setToModifySize(boolean b){
        this.entityData.set(TO_MODIFY_SIZE,b);
    }
    private void setToTurnParticle(boolean b){
        this.entityData.set(TURN_PARTICLE,b);
    }
    private void setParticleType(String s){
        this.entityData.set(PARTICLE_TYPE,s);
    }
    private void setStandardSize(float size){
        this.entityData.set(SIZE,size);
    }
    private void setTailParticle(ParticleOptions particle){
        this.entityData.set(TAIL_PARTICLE,particle);
    }
    //other stuff
    @Override
    public void baseTick() {
        super.baseTick();
        if (toModifySize() && this.getSize() < 3)
            this.setStandardSize(this.getSize() + 0.1f);
        if (this.timeToVanish < 20 && this.getParticleType().equals("cycle"))
            this.timeToVanish++;
        else if(this.timeToVanish < 50 && (this.getParticleType().equals("semicycle") || this.getParticleType().equals("zenith_semicycle")))
            this.timeToVanish++;
        else if (this.timeToVanish < 100 && (this.getParticleType().equals("rain") || this.getParticleType().equals("lich_rain")))
            this.timeToVanish++;
        else
            this.discard();
        if (this.getParticleType().equals("semicycle")  && this.owner != null) {
            if (this.tailParticle() != ParticleTypes.ASH && this.level().isClientSide()) {
                this.level().addParticle(this.tailParticle(), this.getX(), this.getY(), this.getZ(), this.getXVector(owner.getYRot()), this.getYVector(owner.getXRot()), this.getZVector(owner.getYRot()));
            }
            this.setDeltaMovement(new Vec3(this.getXVector(owner.getYRot())*2, this.getYVector(owner.getXRot()), this.getZVector(owner.getYRot())*2));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        if (this.getParticleType().equals("zenith_semicycle") && this.zenith != null){
            if (this.tailParticle() != ParticleTypes.ASH && this.level().isClientSide()) {
                this.level().addParticle(this.tailParticle(), this.getX(), this.getY(), this.getZ(), this.getXVector(zenith.getYRot()), this.getYVector(zenith.getXRot()), this.getZVector(zenith.getYRot()));
            }
            this.setDeltaMovement(new Vec3(this.getXVector(zenith.getYRot())*3, this.getYVector(zenith.getXRot()), this.getZVector(zenith.getYRot())*3));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
        if (this.getParticleType().equals("rain") && this.level() instanceof ServerLevel level && this.owner != null){
            level.sendParticles(this.tailParticle(), this.getX() + getXVector(this.getYRot()), this.getY() - 1, this.getZ() + getZVector(this.getYRot()), 21, 1 + getXVector(this.getYRot()), -1, 1 + getZVector(this.getYRot()), 1);
            if (this.tickCount % 10 == 0){
                this.setDeltaMovement(new Vec3(this.getXVector(owner.getYRot())*4, this.getYVector(owner.getXRot()), this.getZVector(owner.getYRot())*4));
                this.move(MoverType.SELF, this.getDeltaMovement());
                final Vec3 center = new Vec3(
                        this.getX(),this.getY()-1,this.getZ());
                List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(5), e -> true).stream().sorted(Comparator.comparingDouble(entity1 -> entity1.distanceToSqr(center))).toList();
                for (Entity entityIterator : entityList) {
                    if (entityIterator != this && entityIterator != this.owner) {
                        if (entityIterator instanceof LivingEntity livingEntity) {
                            livingEntity.setTicksFrozen(60);
                            livingEntity.hurt(this.damageSources().magic(),5);
                        }
                    }
                }
            }
        }
        if (this.level() instanceof ServerLevel level && this.getParticleType().equals("lich_rain") && this.lich != null){
            level.sendParticles(this.tailParticle(), this.getX() + getXVector(this.getYRot()), this.getY() - 1, this.getZ() + getZVector(this.getYRot()), 21, 1 + getXVector(this.getYRot()), -1, 1 + getZVector(this.getYRot()), 1);
            if (this.tickCount % 10 == 0){
                this.setDeltaMovement(new Vec3(this.getXVector(lich.getYRot())*4, this.getYVector(lich.getXRot()), this.getZVector(lich.getYRot())*4));
                this.move(MoverType.SELF, this.getDeltaMovement());
                final Vec3 center = new Vec3(
                        this.getX(),this.getY()-1,this.getZ());
                List<Entity> entityList = level.getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(5), e -> true).stream().sorted(Comparator.comparingDouble(entity1 -> entity1.distanceToSqr(center))).toList();
                for (Entity entityIterator : entityList) {
                    if (entityIterator != this && entityIterator != this.lich && !(entityIterator instanceof LichMyrmidon)) {
                        if (entityIterator instanceof LivingEntity livingEntity) {
                            livingEntity.setTicksFrozen(160);
                            livingEntity.hurt(this.damageSources().magic(),6);
                        }
                    }
                }
            }
        }
        if (toTurnParticle() && this.owner == null){
            this.owner = getNearestPlayer();
        }
        if (this.getParticleType().equals("zenith_semicycle") && this.zenith == null){
            this.zenith = getNearestZenith();
        }
        if (this.getParticleType().equals("lich_rain") && this.lich == null){
            this.lich = getNearestLich();
        }
    }
    private ZenithEntity getNearestZenith() {
        final Vec3 _center = new Vec3(this.getX(), this.getY(), this.getZ());
        List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(16), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(_center))).toList();
        for (Entity entityiterator : entityList) {
            if (entityiterator instanceof ZenithEntity entity)
                return entity;
        }
        return null;
    }
    private LichKing getNearestLich() {
        final Vec3 _center = new Vec3(this.getX(), this.getY(), this.getZ());
        List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(16), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(_center))).toList();
        for (Entity entityiterator : entityList) {
            if (entityiterator instanceof LichKing entity)
                return entity;
        }
        return null;
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setStandardSize(tag.getInt("actualSize"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putFloat("actualSize",this.getSize());
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void push(double x, double y, double z) {
    }
}