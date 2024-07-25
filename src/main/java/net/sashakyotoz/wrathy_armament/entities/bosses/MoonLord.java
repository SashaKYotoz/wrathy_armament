package net.sashakyotoz.wrathy_armament.entities.bosses;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import net.sashakyotoz.wrathy_armament.entities.bosses.parts.MoonLordPart;

public class MoonLord extends BossLikePathfinderMob{
    private final MoonLordPart[] subEntities;
    private final MoonLordPart headEye;
    private final MoonLordPart heart;
    private final MoonLordPart rightHandEye;
    private final MoonLordPart leftHandEye;
    public MoonLord(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.xpReward = 500;
        this.setNoGravity(true);
        this.moveControl = new FlyingMoveControl(this, 25, true);
        this.headEye = new MoonLordPart(this,"headEye",1.5f,1.5f);
        this.heart = new MoonLordPart(this,"heart",3f,3f);
        this.rightHandEye = new MoonLordPart(this,"rightHandEye",1.5f,1.5f);
        this.leftHandEye = new MoonLordPart(this,"leftHandEye",1.5f,1.5f);
        this.subEntities =new MoonLordPart[]{this.headEye,this.heart,this.rightHandEye,this.leftHandEye};
    }

    @Override
    public ServerBossEvent bossInfo() {
        return new ServerBossEvent(Component.translatable("entity.wrathy_armament.moon_lord"), BossEvent.BossBarColor.WHITE,BossEvent.BossBarOverlay.NOTCHED_20);
    }
    @Override
    public PartEntity<?>[] getParts() {
        return this.subEntities;
    }
    @Override
    public boolean isMultipartEntity() {
        return true;
    }
    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.subEntities.length; i++)
            this.subEntities[i].setId(id + i + 1);
    }
    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return super.getStandingEyeHeight(pPose, pDimensions) + 2f;
    }

    private void tickPart(MoonLordPart moonLordPart, double pOffsetX, double pOffsetY, double pOffsetZ) {
        moonLordPart.setPos(this.getX() + pOffsetX, this.getY() + pOffsetY, this.getZ() + pOffsetZ);
    }
    @Override
    public void aiStep() {
        Vec3[] avec3 = new Vec3[this.subEntities.length];
        for (int j = 0; j < this.subEntities.length; ++j) {
            avec3[j] = new Vec3(this.subEntities[j].getX(), this.subEntities[j].getY(), this.subEntities[j].getZ());
        }
        float f14 = this.getYRot() * ((float) Math.PI / 180F);
        float f1 = Mth.sin(f14);
        float f15 = Mth.cos(f14);
        this.tickPart(this.headEye, -f1*0.75f, 5.25D, f15*0.75f);
        this.tickPart(this.heart, 0, 1D, 0);
        this.tickPart(this.rightHandEye, f15 * -3.75F, 1.5D, f1 * -3.75F);
        this.tickPart(this.leftHandEye, f15 * 3.75F, 1.5D, f1 * 3.75F);
        for (int l = 0; l < this.subEntities.length; ++l) {
            this.subEntities[l].xo = avec3[l].x;
            this.subEntities[l].yo = avec3[l].y;
            this.subEntities[l].zo = avec3[l].z;
            this.subEntities[l].xOld = avec3[l].x;
            this.subEntities[l].yOld = avec3[l].y;
            this.subEntities[l].zOld = avec3[l].z;
        }
        super.aiStep();
    }
    //attributes
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 750.0D)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.ARMOR, 16)
                .add(Attributes.ARMOR_TOUGHNESS, 16)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1);
    }
}