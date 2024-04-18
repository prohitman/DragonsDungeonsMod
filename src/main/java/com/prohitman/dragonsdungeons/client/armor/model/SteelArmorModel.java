package com.prohitman.dragonsdungeons.client.armor.model;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.items.armor.SteelArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SteelArmorModel extends GeoModel<SteelArmorItem> {
    @Override
    public ResourceLocation getModelResource(SteelArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "geo/steel_armor_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SteelArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "textures/armor/steel_armor_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SteelArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "animations/steel_armor.animation.json");
    }
}