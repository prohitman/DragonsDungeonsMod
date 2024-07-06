package com.prohitman.dragonsdungeons.client.entities.renderers;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.entities.models.AutomatonModel;
import com.prohitman.dragonsdungeons.client.entities.models.EmblemscuteModel;
import com.prohitman.dragonsdungeons.client.entities.renderers.layers.GlowingLayer;
import com.prohitman.dragonsdungeons.common.entities.AutomatonEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AutomatonRenderer extends GeoEntityRenderer<AutomatonEntity> {
    public AutomatonRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AutomatonModel());
        this.addRenderLayer(new GlowingLayer<>(this, new ResourceLocation(DragonsDungeons.MODID, "textures/entity/automaton_eyes.png")));
        this.shadowRadius = 0.5F;
    }
}
