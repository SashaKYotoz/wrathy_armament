package net.sashakyotoz.wrathy_armament.entities.move_controls;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.sashakyotoz.wrathy_armament.entities.bosses.SashaKYotoz;

public class SashaKYotozMoveController extends MoveControl {
    private final SashaKYotoz sashaKYotoz;
    public SashaKYotozMoveController(SashaKYotoz sashaKYotoz) {
        super(sashaKYotoz);
        this.sashaKYotoz = sashaKYotoz;
    }

    @Override
    public void tick() {
        if (!this.sashaKYotoz.isPhantomCycleAttack() && !this.sashaKYotoz.isPhantomRayAttack()){
            if (this.sashaKYotoz.getFlyPhase() == 0)
                super.tick();
            else
                flyingTick();
        }
    }
    private void flyingTick(){
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < (double)2.5000003E-7F) {
                this.mob.setYya(0.0F);
                this.mob.setZza(0.0F);
                return;
            }
            float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
            float f1;
            if (this.mob.onGround()) {
                f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            } else {
                f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));
            }
            this.mob.setSpeed(f1);
            double d4 = Math.sqrt(d0 * d0 + d2 * d2);
            if (Math.abs(d1) > (double)1.0E-5F || Math.abs(d4) > (double)1.0E-5F) {
                float f2 = (float)(-(Mth.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f2, 30));
                this.mob.setYya(d1 > 0.0D ? f1 : -f1);
            }
        } else {
            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }
    }
}
