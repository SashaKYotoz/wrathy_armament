package net.sashakyotoz.wrathy_armament.client.renderer;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sashakyotoz.wrathy_armament.entities.technical.ZenithEntity;

import java.util.List;
@OnlyIn(Dist.CLIENT)
public abstract class AdvancedEntityRenderer <T extends ZenithEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {
    protected M model;
    protected final List<RenderLayer<T, M>> layers = Lists.newArrayList();

    public AdvancedEntityRenderer(EntityRendererProvider.Context context, M model, float p_174291_) {
        super(context);
        this.model = model;
        this.shadowRadius = p_174291_;
    }

    public final boolean addLayer(RenderLayer<T, M> renderLayer) {
        return this.layers.add(renderLayer);
    }

    public M getModel() {
        return this.model;
    }

    public void render(T entity, float p_115309_, float p_115310_, PoseStack poseStack, MultiBufferSource bufferSource, int p_115313_) {
        poseStack.pushPose();
        float f = Mth.rotLerp(p_115310_, entity.xRotO, entity.yRotO);
        float f1 = Mth.rotLerp(p_115310_, entity.xRotO, entity.yRotO);
        float f2 = f1 - f;
        float f6 = Mth.lerp(p_115310_, entity.xRotO, entity.getXRot());
        float f7 = this.getBob(entity, p_115310_);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, poseStack, p_115310_);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        float f8 = 0.0F;
        float f5 = 0.0F;
        this.model.prepareMobModel(entity, f5, f8, p_115310_);
        this.model.setupAnim(entity, f5, f8, f7, f2, f6);
        poseStack.popPose();
        super.render(entity, p_115309_, p_115310_, poseStack, bufferSource, p_115313_);
    }


    protected float getBob(T entity, float p_115306_) {
        return (float)entity.tickCount + p_115306_;
    }


    protected void scale(T entity, PoseStack p_115315_, float p_115316_) {
    }
}
