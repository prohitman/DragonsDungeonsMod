package com.prohitman.dragonsdungeons.client.entities.renderers;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.entities.models.EmblemscuteModel;
import com.prohitman.dragonsdungeons.client.entities.renderers.layers.GlowingLayer;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EmblemscuteRenderer extends GeoEntityRenderer<EmblemscuteEntity> {
    public EmblemscuteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EmblemscuteModel());
        this.addRenderLayer(new GlowingLayer<>(this, new ResourceLocation(DragonsDungeons.MODID, "textures/entity/emblemscute_glowing_layer.png")));
        this.shadowRadius = 0.8F;
    }
}
