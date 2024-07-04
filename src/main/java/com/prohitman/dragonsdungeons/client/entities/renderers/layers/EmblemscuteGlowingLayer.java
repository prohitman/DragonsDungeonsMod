package com.prohitman.dragonsdungeons.client.entities.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class EmblemscuteGlowingLayer extends GeoRenderLayer<EmblemscuteEntity> {
    private static final ResourceLocation texture = new ResourceLocation(DragonsDungeons.MODID, "textures/entity/emblemscute_glowing_layer.png");
    private static final RenderType EMBLEMSCUTE_LAYER = RenderType.eyes(texture);

    public EmblemscuteGlowingLayer(GeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, EmblemscuteEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        VertexConsumer vertexconsumer = bufferSource.getBuffer(EMBLEMSCUTE_LAYER);

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, EMBLEMSCUTE_LAYER, vertexconsumer, partialTick, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        //this.getParentModel().renderToBuffer(pMatrixStack, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

   /* @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, EmblemscuteEntity animatable, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        VertexConsumer vertexconsumer = bufferSource.getBuffer(EMBLEMSCUTE_LAYER);


        this.getRenderer().render(this.getEntityModel().getModel(this.getEntityModel().getModelLocation(entityLivingBaseIn)), entityLivingBaseIn, 0, this.getRenderType(texture), matrixStackIn, bufferIn, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.8F);
        //this.renderModel(this.getEntityModel(), texture, matrixStackIn, bufferIn, 15728640, entityLivingBaseIn, 0, );
        //renderToBuffer(matrixStackIn, vertexconsumer, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }*/
}
