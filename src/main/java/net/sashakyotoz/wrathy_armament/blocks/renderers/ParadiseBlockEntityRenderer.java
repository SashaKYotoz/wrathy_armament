package net.sashakyotoz.wrathy_armament.blocks.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.sashakyotoz.wrathy_armament.blocks.ParadiseBlock;
import net.sashakyotoz.wrathy_armament.blocks.entities.ParadiseBlockEntity;

public class ParadiseBlockEntityRenderer implements BlockEntityRenderer<ParadiseBlockEntity> {

    public ParadiseBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ParadiseBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        BlockPos pos = entity.getBlockPos();
        BlockState stateToParadise = entity.getLevel().getBlockState(pos.below()).getBlock() instanceof ParadiseBlock ?
                entity.getLevel().getBlockState(pos.above()) :
                entity.getLevel().getBlockState(pos.below());
        BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
        blockRenderDispatcher.renderBatched(stateToParadise, pos, entity.getLevel(), poseStack, buffer.getBuffer(RenderType.solid()), false, entity.getLevel().random);
        poseStack.popPose();
    }
}