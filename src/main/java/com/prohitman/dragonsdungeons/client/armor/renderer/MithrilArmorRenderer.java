package com.prohitman.dragonsdungeons.client.armor.renderer;

import com.prohitman.dragonsdungeons.client.armor.model.MithrilArmorModel;
import com.prohitman.dragonsdungeons.common.items.armor.MithrilArmorItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class MithrilArmorRenderer extends GeoArmorRenderer<MithrilArmorItem> {
    public MithrilArmorRenderer() {
        super(new MithrilArmorModel());
    }
}