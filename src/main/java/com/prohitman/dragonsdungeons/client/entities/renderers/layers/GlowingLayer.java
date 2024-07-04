package com.prohitman.dragonsdungeons.client.entities.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ScreenEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GlowingLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {
    private final ResourceLocation texture;
    public GlowingLayer(GeoRenderer entityRendererIn, ResourceLocation layerLocation) {
        super(entityRendererIn);
        this.texture = layerLocation;
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowingLayer = RenderType.eyes(texture);

        VertexConsumer vertexconsumer = bufferSource.getBuffer(glowingLayer);

        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowingLayer, vertexconsumer, partialTick, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
