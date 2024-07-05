package com.prohitman.dragonsdungeons.client.entities.renderers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.entities.models.WargModel;
import com.prohitman.dragonsdungeons.client.entities.renderers.layers.GlowingLayer;
import com.prohitman.dragonsdungeons.client.entities.renderers.layers.WargGlowLayer;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import com.prohitman.dragonsdungeons.common.entities.variant.WargVariant;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class WargRenderer extends GeoEntityRenderer<WargEntity> {
    private static final Map<WargVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(WargVariant.class), map -> {
                map.put(WargVariant.PALE,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/pale_warg.png"));
                map.put(WargVariant.SNOWY,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/snowy_warg.png"));
                map.put(WargVariant.BLACK,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/black_warg.png"));
                map.put(WargVariant.CHESTNUT,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/chestnut_warg.png"));
                map.put(WargVariant.STRIPED,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/striped_warg.png"));
                map.put(WargVariant.ASHEN,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/ashen_warg.png"));
                map.put(WargVariant.RUSTY,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/rusty_warg.png"));
                map.put(WargVariant.WOOD,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/wood_warg.png"));
                map.put(WargVariant.SPOTTED,
                        new ResourceLocation(DragonsDungeons.MODID, "textures/entity/spotted_warg.png"));
            });


    public WargRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WargModel());

        this.shadowRadius = 1F;
        this.addRenderLayer(new WargGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(WargEntity pEntity) {
        return LOCATION_BY_VARIANT.get(pEntity.getVariant());
    }

    @Override
    public void render(WargEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
