package com.prohitman.dragonsdungeons.client.armor.model;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.items.armor.MithrilArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class MithrilArmorModel extends GeoModel<MithrilArmorItem> {
    @Override
    public ResourceLocation getModelResource(MithrilArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "geo/mithril_armor_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MithrilArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "textures/armor/mithril_armor_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(MithrilArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "animations/mithril_armor.animation.json");
    }
}