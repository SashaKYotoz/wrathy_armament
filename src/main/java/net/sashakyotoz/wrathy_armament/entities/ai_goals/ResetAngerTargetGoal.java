package net.sashakyotoz.wrathy_armament.entities.ai_goals;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.AABB;
import net.sashakyotoz.wrathy_armament.entities.bosses.core.PersistentAngerMob;

import java.util.List;

public class ResetAngerTargetGoal<T extends Mob & PersistentAngerMob> extends Goal {
    private final T mob;
    private final boolean alertOthersOfSameType;
    private int lastHurtByPlayerTimestamp;

    public ResetAngerTargetGoal(T t, boolean b) {
        this.mob = t;
        this.alertOthersOfSameType = b;
    }

    public boolean canUse() {
        return this.mob.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER) && this.wasHurtByPlayer();
    }

    private boolean wasHurtByPlayer() {
        return this.mob.getLastHurtByMob() != null && this.mob.getLastHurtByMob().getType() == EntityType.PLAYER && this.mob.getLastHurtByMobTimestamp() > this.lastHurtByPlayerTimestamp;
    }

    public void start() {
        this.lastHurtByPlayerTimestamp = this.mob.getLastHurtByMobTimestamp();
        this.mob.forgetCurrentTargetAndRefreshUniversalAnger();
        if (this.alertOthersOfSameType) {
            this.getNearbyMobsOfSameType().stream().filter((mob) -> mob != this.mob).map((mob1) -> (PersistentAngerMob)mob1).forEach(PersistentAngerMob::forgetCurrentTargetAndRefreshUniversalAnger);
        }

        super.start();
    }

    private List<? extends Mob> getNearbyMobsOfSameType() {
        double d0 = this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(d0, 10.0D, d0);
        return this.mob.level().getEntitiesOfClass(this.mob.getClass(), aabb, EntitySelector.NO_SPECTATORS);
    }
}