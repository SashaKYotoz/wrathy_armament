package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses.moon_lord;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

import java.util.Comparator;
import java.util.List;

public class MoonLordLasering extends Goal {

    private final MoonLord lord;
    private int attackTime = 0;

    public MoonLordLasering(MoonLord lord) {
        this.lord = lord;
    }
    @Override
    public boolean canUse() {
        return this.lord.getTarget() != null && this.lord.getTarget().isAlive() && this.lord.getDataLordPose().equals(MoonLord.LordPose.LASERING);
    }

    @Override
    public void start() {
        this.lord.setIsLaserActivated(true);
        this.lord.interactive.stop();
    }
    public void tick() {
        LivingEntity target = this.lord.getTarget();
        if (target != null && target.distanceToSqr(this.lord) < 256.0D && this.lord.hasLineOfSight(target)) {
            if (attackTime == 100) {
                attackTime = 0;
                this.lord.setIsLaserActivated(false);
                this.lord.setRandomLordPose();
            } else if (attackTime < 100) {
                attackTime++;
                int scaling = 0;
                for (int i = 0; i < 36; i++) {
                    BlockPos pos2 = this.lord.level().clip(new ClipContext(this.lord.getEyePosition(1f), this.lord.getEyePosition(1f).add(this.lord.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.lord)).getBlockPos();
                    if (!this.lord.level().getBlockState(new BlockPos(pos2.getX(), pos2.getY(), pos2.getZ())).canOcclude())
                        scaling = scaling + 1;
                    BlockPos pos = this.lord.level().clip(new ClipContext(this.lord.getEyePosition(1f), this.lord.getEyePosition(1f).add(this.lord.getViewVector(1f).scale(scaling)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.lord)).getBlockPos();
                    Vec3 center = pos.getCenter();
                    List<Entity> entityList = this.lord.level().getEntitiesOfClass(Entity.class, new AABB(center, center).inflate(0.5), e -> true).stream().sorted(Comparator.comparingDouble(t -> t.distanceToSqr(center))).toList();
                    for (Entity entity : entityList) {
                        if (entity instanceof LivingEntity livingEntity && this.lord.tickCount % 5 == 0)
                            livingEntity.hurt(this.lord.damageSources().magic(),this.lord.level().getDifficulty().equals(Difficulty.HARD) ? 8 : 5);
                    }
                }
            }
            this.lord.getNavigation().stop();
        } else {
            if (target != null)
                this.lord.getNavigation().moveTo(target, 0.5);
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }
}