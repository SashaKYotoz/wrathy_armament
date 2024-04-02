package net.sashakyotoz.wrathy_armament.entities.technical;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class OwnerableMob extends PathfinderMob implements Enemy {
    private LivingEntity owner;
    private boolean haveToFindOwner = false;
    public OwnerableMob(EntityType<? extends OwnerableMob> type, Level level) {
        super(type, level);
    }
    public LivingEntity getOwner(){
        return this.owner;
    }
    public boolean isHaveToFindOwner(){
        return this.haveToFindOwner;
    }
    public void setHaveToFindOwner(boolean b){
        this.haveToFindOwner = b;
    }
    public void setOwner(LivingEntity entity){
        this.owner = entity;
    }
    public Player getNearestPlayer() {
        final Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        List<Entity> entityList = this.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(24), e -> true).stream().sorted(Comparator.comparingDouble(entity -> entity.distanceToSqr(center))).toList();
        for (Entity entityiterator : entityList) {
            if (entityiterator instanceof Player player)
                return player;
        }
        return null;
    }
}
