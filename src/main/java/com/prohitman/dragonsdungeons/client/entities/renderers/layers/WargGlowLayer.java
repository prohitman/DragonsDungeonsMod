package com.prohitman.dragonsdungeons.client.entities.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.entities.renderers.WargEyeColor;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import com.prohitman.dragonsdungeons.common.entities.variant.WargVariant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WargGlowLayer extends GeoRenderLayer<WargEntity> {
    private final ResourceLocation blue_eye_texture = new ResourceLocation(DragonsDungeons.MODID, "textures/entity/warg_blue_eyes.png");
    private final ResourceLocation yellow_eye_texture = new ResourceLocation(DragonsDungeons.MODID, "textures/entity/warg_yellow_eyes.png");

    public WargGlowLayer(GeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, WargEntity animatable, BakedGeoModel bakedModel, RenderType
    renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(animatable.getVariant().getEyeColor() != WargEyeColor.RED){
            RenderType glowingLayer = RenderType.eyes(blue_eye_texture);

            if(animatable.getVariant().getEyeColor() == WargEyeColor.YELLOW){
                glowingLayer = RenderType.eyes(yellow_eye_texture);
            }

            VertexConsumer vertexconsumer = bufferSource.getBuffer(glowingLayer);

            getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowingLayer, vertexconsumer, partialTick, 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
