package com.prohitman.dragonsdungeons.client.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.entities.models.WargModel;
import com.prohitman.dragonsdungeons.client.entities.models.ZargModel;
import com.prohitman.dragonsdungeons.client.entities.renderers.layers.GlowingLayer;
import com.prohitman.dragonsdungeons.client.entities.renderers.layers.WargGlowLayer;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import com.prohitman.dragonsdungeons.common.entities.ZargEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.ZombieVillager;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ZargRenderer extends GeoEntityRenderer<ZargEntity> {
    public ZargRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ZargModel());

        this.shadowRadius = 1F;
        this.addRenderLayer(new GlowingLayer<>(this, new ResourceLocation(DragonsDungeons.MODID, "textures/entity/zarg_eyes.png")));
    }

    @Override
    public void render(ZargEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
    public boolean isShaking(ZargEntity pEntity) {
        return super.isShaking(pEntity) || pEntity.isConverting();
    }
}
