package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class FixedDeathAnimationMobRenderer<T extends Mob, M extends EntityModel<T>> extends MobRenderer<T , M> {
    public FixedDeathAnimationMobRenderer(EntityRendererProvider.Context context, M model, float shadowSize) {
        super(context, model, shadowSize);
    }
    @Override
    protected void setupRotations(Mob entity, PoseStack stack, float p_115319_, float p_115320_, float p_115321_) {
        if (this.isShaking((T) entity))
            p_115320_ += (float)(Math.cos((double)entity.tickCount * 3.25D) * Math.PI * (double)0.4F);
        if (!entity.hasPose(Pose.SLEEPING))
            stack.mulPose(Axis.YP.rotationDegrees(180.0F - p_115320_));
        if (entity.isAutoSpinAttack()) {
            stack.mulPose(Axis.XP.rotationDegrees(-90.0F - entity.getXRot()));
            stack.mulPose(Axis.YP.rotationDegrees(((float)entity.tickCount + p_115321_) * -75.0F));
        }
        else if (isEntityUpsideDown(entity)) {
            stack.translate(0.0F, entity.getBbHeight() + 0.1F, 0.0F);
            stack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }
}
