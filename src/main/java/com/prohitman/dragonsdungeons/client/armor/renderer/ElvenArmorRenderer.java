package com.prohitman.dragonsdungeons.client.armor.renderer;

import com.prohitman.dragonsdungeons.client.armor.model.ElvenArmorModel;
import com.prohitman.dragonsdungeons.common.items.armor.ElvenArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ElvenArmorRenderer extends GeoArmorRenderer<ElvenArmorItem> {
    public ElvenArmorRenderer() {
        super(new ElvenArmorModel());
    }
}