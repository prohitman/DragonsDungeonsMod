package com.prohitman.dragonsdungeons.client.armor.model;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.items.armor.ElvenArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ElvenArmorModel extends GeoModel<ElvenArmorItem> {
    @Override
    public ResourceLocation getModelResource(ElvenArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "geo/elven_armor_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ElvenArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "textures/armor/elven_armor_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ElvenArmorItem animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "animations/elven_armor.animation.json");
    }
}