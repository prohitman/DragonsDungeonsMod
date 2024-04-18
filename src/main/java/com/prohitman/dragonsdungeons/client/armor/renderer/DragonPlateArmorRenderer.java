package com.prohitman.dragonsdungeons.client.armor.renderer;

import com.prohitman.dragonsdungeons.client.armor.model.DragonPlateArmorModel;
import com.prohitman.dragonsdungeons.common.items.armor.DragonPlateArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class DragonPlateArmorRenderer extends GeoArmorRenderer<DragonPlateArmorItem> {
    public DragonPlateArmorRenderer() {
        super(new DragonPlateArmorModel());
    }
}