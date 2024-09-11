package net.sashakyotoz.wrathy_armament.client.models.mobs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.sashakyotoz.wrathy_armament.entities.animations.HabciakAnimations;
import net.sashakyotoz.wrathy_armament.entities.bosses.Habciak;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HierarchicalPlayerModel<E extends LivingEntity> extends PlayerModel<E> {
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    public final ModelPart root;

    public HierarchicalPlayerModel(ModelPart pRoot, boolean pSlim) {
        super(pRoot, pSlim);
        this.root = pRoot;
    }

    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha);
    }

    public Optional<ModelPart> getAnyDescendantWithName(String pName) {
        return pName.equals("root") ? Optional.of(this.root) : this.root.getAllParts().filter((part) -> part.hasChild(pName)).findFirst().map((part) -> part.getChild(pName));
    }

    protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks) {
        this.animate(pAnimationState, pAnimationDefinition, pAgeInTicks, 1.0F);
    }

    protected void animateWalk(AnimationDefinition pAnimationDefinition, float pLimbSwing, float pLimbSwingAmount, float pMaxAnimationSpeed, float pAnimationScaleFactor) {
        long i = (long) (pLimbSwing * 50.0F * pMaxAnimationSpeed);
        float f = Math.min(pLimbSwingAmount * pAnimationScaleFactor, 1.0F);
        animate(this, pAnimationDefinition, i, f, ANIMATION_VECTOR_CACHE);
    }

    public static void animate(HierarchicalPlayerModel<?> pModel, AnimationDefinition pAnimationDefinition, long pAccumulatedTime, float pScale, Vector3f pAnimationVecCache) {
        float f = getElapsedSeconds(pAnimationDefinition, pAccumulatedTime);
        for (Map.Entry<String, List<AnimationChannel>> entry : pAnimationDefinition.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = pModel.getAnyDescendantWithName(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent((part) -> list.forEach((p_288241_) -> {
                Keyframe[] akeyframe = p_288241_.keyframes();
                int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (i1) -> f <= akeyframe[i1].timestamp()) - 1);
                int j = Math.min(akeyframe.length - 1, i + 1);
                Keyframe keyframe = akeyframe[i];
                Keyframe keyframe1 = akeyframe[j];
                float f1 = f - keyframe.timestamp();
                float f2;
                if (j != i)
                    f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                else
                    f2 = 0.0F;
                keyframe1.interpolation().apply(pAnimationVecCache, f2, akeyframe, i, j, pScale);
                p_288241_.target().apply(part, pAnimationVecCache);
            }));
        }
    }

    private static float getElapsedSeconds(AnimationDefinition pAnimationDefinition, long pAccumulatedTime) {
        float f = (float) pAccumulatedTime / 1000.0F;
        return pAnimationDefinition.looping() ? f % pAnimationDefinition.lengthInSeconds() : f;
    }

    protected void animate(AnimationState pAnimationState, AnimationDefinition pAnimationDefinition, float pAgeInTicks, float pSpeed) {
        pAnimationState.updateTime(pAgeInTicks, pSpeed);
        pAnimationState.ifStarted((p_233392_) -> {
            animate(this, pAnimationDefinition, p_233392_.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
        });
    }

    @Override
    public void setupAnim(E pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        super.setupAnim(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
        this.animateWalk(HabciakAnimations.WALK, pLimbSwing, pLimbSwingAmount, 2.0F, 2.5F);
        if (pEntity instanceof Habciak habciak) {
            this.animate(habciak.death, HabciakAnimations.DEATH, pAgeInTicks);
            this.animate(habciak.jump, HabciakAnimations.JUMP_TELEPORT, pAgeInTicks);
            this.animate(habciak.floorAttack, HabciakAnimations.FLOORS_ATTACK, pAgeInTicks);
            this.animate(habciak.backFlip, HabciakAnimations.BACK_FLIP, pAgeInTicks);
            this.animate(habciak.mirrorCasting, HabciakAnimations.MIRROR_CASTING, pAgeInTicks);
        }
    }
}