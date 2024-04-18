package com.prohitman.dragonsdungeons.client.armor.renderer;

import com.prohitman.dragonsdungeons.client.armor.model.SteelArmorModel;
import com.prohitman.dragonsdungeons.common.items.armor.SteelArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class SteelArmorRenderer extends GeoArmorRenderer<SteelArmorItem> {
    public SteelArmorRenderer() {
        super(new SteelArmorModel());
    }
}