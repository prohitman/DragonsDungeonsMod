package com.prohitman.dragonsdungeons.client.armor.model;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.items.armor.DragonPlateArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class DragonPlateArmorModel extends GeoModel<DragonPlateArmorItem> {
    @Override
    public ResourceLocation getModelResource(DragonPlateArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "geo/dragon_plate_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DragonPlateArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "textures/armor/dragon_plate_armor_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DragonPlateArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "animations/dragon_plate_armor.animation.json");
    }
}