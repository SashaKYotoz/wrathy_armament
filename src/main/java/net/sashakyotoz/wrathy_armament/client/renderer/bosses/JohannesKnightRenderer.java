package net.sashakyotoz.wrathy_armament.client.renderer.bosses;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.client.models.mobs.JohannesFountainModel;
import net.sashakyotoz.wrathy_armament.client.models.mobs.JohannesKnightModel;
import net.sashakyotoz.wrathy_armament.entities.bosses.JohannesKnight;

public class JohannesKnightRenderer extends FixedDeathAnimationMobRenderer<JohannesKnight, HierarchicalModel<JohannesKnight>>{
    private final ResourceLocation KNIGHT = WrathyArmament.createWALocation("textures/entity/bosses/johannes_knight.png");
    private final ResourceLocation FOUNTAIN = WrathyArmament.createWALocation("textures/entity/bosses/johannes_fountain.png");
    private final JohannesKnightModel<JohannesKnight> knightModel;
    private final JohannesFountainModel<JohannesKnight> fountainModel;
    public JohannesKnightRenderer(EntityRendererProvider.Context context) {
        super(context,new JohannesKnightModel<>(context.bakeLayer(JohannesKnightModel.LAYER_LOCATION)),1.0f);
        this.knightModel = new JohannesKnightModel<>(context.bakeLayer(JohannesKnightModel.LAYER_LOCATION));
        this.fountainModel = new JohannesFountainModel<>(context.bakeLayer(JohannesFountainModel.LAYER_LOCATION));
    }
    @Override
    public void render(JohannesKnight knight, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn) {
        float f7 = this.getBob(knight, partialTicks);
        float f = Mth.rotLerp(partialTicks, knight.yBodyRotO, knight.yBodyRot);
        float f1 = Mth.rotLerp(partialTicks, knight.yHeadRotO, knight.yHeadRot);
        float f2 = f1 - f;
        float f8 = 0;
        float f5 = 0.0F;
        float f6 = Mth.lerp(partialTicks, knight.xRotO, knight.getXRot());
        if (isEntityUpsideDown(knight)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }
        if (knight.isAlive()) {
            f8 = knight.walkAnimation.speed(partialTicks);
            f5 = knight.walkAnimation.position(partialTicks);
            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.knightModel.renderType(KNIGHT));
        PoseStack poseStack1 = new PoseStack();
        if (knight.getLastHurtByMob() != null)
            poseStack1.translate(knight.getXVector(3,knight.getLastHurtByMob().getYRot()),knight.deathTime/10f,knight.getZVector(3,knight.getLastHurtByMob().getYRot()));
        else
            poseStack1.translate(0.25f,knight.deathTime/10f,0.25f);
        this.model = knight.isInSecondPhase() ? fountainModel : knightModel;
        if (knight.isInSecondPhase() && knight.isDeadOrDying()){
            poseStack.pushPose();
            this.knightModel.setupAnim(knight, f5, f8, f7, f2, f6);
            this.knightModel.renderToBuffer(poseStack1, vertexconsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F,0.5f);
            poseStack.popPose();
        }
        super.render(knight, entityYaw, partialTicks, poseStack, bufferSource, packedLightIn);
    }

    @Override
    public boolean isBodyVisible(JohannesKnight knight) {
        return knight.getKnightPose() != JohannesKnight.KnightPose.DASHING;
    }

    @Override
    public ResourceLocation getTextureLocation(JohannesKnight knight) {
        return knight.isInSecondPhase() ? FOUNTAIN : KNIGHT;
    }
}