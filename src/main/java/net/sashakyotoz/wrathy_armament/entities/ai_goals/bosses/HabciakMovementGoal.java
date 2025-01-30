package net.sashakyotoz.wrathy_armament.entities.ai_goals.bosses;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.sashakyotoz.wrathy_armament.entities.bosses.Habciak;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

import java.util.EnumSet;

public class HabciakMovementGoal extends Goal {
    private final Habciak habciak;

    public HabciakMovementGoal(Habciak habciak) {
        this.habciak = habciak;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return habciak.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return habciak.getTarget() != null && habciak.getTarget().isAlive();
    }

    @Override
    public void tick() {
        LivingEntity target = this.habciak.getTarget();
        if (target != null) {
            double distanceSqr = this.habciak.distanceToSqr(target);
            if (this.habciak.hasLineOfSight(target)) {
                if (!this.habciak.isInPose(Habciak.HabciakPose.DYING))
                    this.habciak.lookAt(target, 15, 15);
                switch (this.habciak.getHabciakPose()) {
                    case JUMPING -> {
                        this.habciak.getNavigation().moveTo(target, 1f);
                        if (this.habciak.tickCount % 20 == 0)
                            this.jump();
                    }
                    case ATTACKING,SPLASH -> {
                        if (distanceSqr > 3)
                            this.habciak.getNavigation().moveTo(target, 1.25f);
                    }
                    case MIRROR_CASTING -> this.habciak.getMoveControl().strafe(-0.35f, 0f);
                    case BACKFLIPPING -> this.habciak.getMoveControl().strafe(-0.25f, 0f);
                    case BYPASSING -> this.habciak.getMoveControl().strafe(-1f, 0f);
                }
            } else
                this.habciak.getNavigation().moveTo(target, 1f);
        }
    }

    private void jump() {
        Vec3 vec3 = this.habciak.getDeltaMovement();
        this.habciak.setDeltaMovement(vec3.x, 0.35f, vec3.z);
        if (this.habciak.isSprinting()) {
            float f = this.habciak.getYRot() * ((float) Math.PI / 180F);
            this.habciak.setDeltaMovement(this.habciak.getDeltaMovement().add(-Mth.sin(f) * 0.2F, 0.0D, Mth.cos(f) * 0.2F));
        }
        this.habciak.hasImpulse = true;
        ForgeHooks.onLivingJump(this.habciak);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}