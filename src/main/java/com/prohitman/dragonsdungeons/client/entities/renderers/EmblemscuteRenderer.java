package com.prohitman.dragonsdungeons.client.entities.renderers;

import com.prohitman.dragonsdungeons.client.entities.models.EmblemscuteModel;
import com.prohitman.dragonsdungeons.client.entities.renderers.layers.EmblemscuteGlowingLayer;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EmblemscuteRenderer extends GeoEntityRenderer<EmblemscuteEntity> {
    public EmblemscuteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EmblemscuteModel());
        this.addRenderLayer(new EmblemscuteGlowingLayer(this));
        this.shadowRadius = 0.8F; //change 0.7 to the desired shadow size.
    }
}
