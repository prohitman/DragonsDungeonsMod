package com.prohitman.dragonsdungeons.client.entities.models;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import com.prohitman.dragonsdungeons.common.entities.ZargEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ZargModel extends GeoModel<ZargEntity> {
    @Override
    public ResourceLocation getModelResource(ZargEntity animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "geo/warg.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZargEntity animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "textures/entity/zarg_warg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZargEntity animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "animations/warg.animation.json");
    }

    @Override
    public void setCustomAnimations(ZargEntity animatable, long instanceId, AnimationState<ZargEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
