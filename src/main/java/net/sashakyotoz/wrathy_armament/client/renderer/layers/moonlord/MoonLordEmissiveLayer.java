package net.sashakyotoz.wrathy_armament.client.renderer.layers.moonlord;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sashakyotoz.wrathy_armament.client.models.mobs.MoonLordModel;
import net.sashakyotoz.wrathy_armament.entities.bosses.MoonLord;

import java.util.List;
@OnlyIn(Dist.CLIENT)
public class MoonLordEmissiveLayer <T extends MoonLord, M extends MoonLordModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation texture;
    private final MoonLordEmissiveLayer.AlphaFunction<T> alphaFunction;
    private final MoonLordEmissiveLayer.DrawSelector<T, M> drawSelector;

    public MoonLordEmissiveLayer(RenderLayerParent<T, M> pRenderer, ResourceLocation pTexture, MoonLordEmissiveLayer.AlphaFunction<T> pAlphaFunction, MoonLordEmissiveLayer.DrawSelector<T, M> pDrawSelector) {
        super(pRenderer);
        this.texture = pTexture;
        this.alphaFunction = pAlphaFunction;
        this.drawSelector = pDrawSelector;
    }
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (!pLivingEntity.isInvisible()) {
            this.onlyDrawSelectedParts();
            VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(this.texture));
            this.getParentModel().renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, LivingEntityRenderer.getOverlayCoords(pLivingEntity, 0.0F), 1.0F, 1.0F, 1.0F, this.alphaFunction.apply(pLivingEntity, pPartialTick, pAgeInTicks));
            this.resetDrawForAllParts();
        }
    }

    private void onlyDrawSelectedParts() {
        List<ModelPart> list = this.drawSelector.getPartsToDraw(this.getParentModel());
        this.getParentModel().root().getAllParts().forEach((part) -> part.skipDraw = true);
        list.forEach((modelPart) -> modelPart.skipDraw = false);
    }

    private void resetDrawForAllParts() {
        this.getParentModel().root().getAllParts().forEach((modelPart) -> modelPart.skipDraw = false);
    }

    @OnlyIn(Dist.CLIENT)
    public interface AlphaFunction<T extends MoonLord> {
        float apply(T pLivingEntity, float pPartialTick, float pAgeInTicks);
    }

    @OnlyIn(Dist.CLIENT)
    public interface DrawSelector<T extends MoonLord, M extends EntityModel<T>> {
        List<ModelPart> getPartsToDraw(M pParentModel);
    }
}
