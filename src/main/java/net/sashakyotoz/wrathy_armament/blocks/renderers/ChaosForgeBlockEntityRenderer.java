package net.sashakyotoz.wrathy_armament.blocks.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.WrathyArmament;
import net.sashakyotoz.wrathy_armament.blocks.ChaosForge;
import net.sashakyotoz.wrathy_armament.blocks.blockentities.ChaosForgeBlockEntity;
import net.sashakyotoz.wrathy_armament.client.models.technical.CycleFlameModel;

public class ChaosForgeBlockEntityRenderer implements BlockEntityRenderer<ChaosForgeBlockEntity> {
    public static ResourceLocation TEXTURE = new ResourceLocation(WrathyArmament.MODID, "textures/entity/decoration_like/chaotic_fire.png");
    private final CycleFlameModel flameModel;
    public ChaosForgeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.flameModel = new CycleFlameModel(context.getModelSet().bakeLayer(CycleFlameModel.LAYER_LOCATION));
    }

    @Override
    public void render(ChaosForgeBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState state = entity.getBlockState();
        long gameTime = 0;
        if (entity.getLevel() != null)
            gameTime = entity.getLevel().getGameTime();
        poseStack.pushPose();
        if (state.getValue(ChaosForge.FIRING) > 0) {
            poseStack.rotateAround(Axis.YP.rotation((gameTime % 360)* 0.05f),0.5f,-2f,0.5f);
            poseStack.scale(1.25f,1.25f,1.25f);
            this.flameModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(TEXTURE)), packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 0.4f + state.getValue(ChaosForge.FIRING)/5f);
        }
        poseStack.popPose();
    }
}
