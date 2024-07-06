package com.prohitman.dragonsdungeons.client.entities.models;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.entities.AutomatonEntity;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AutomatonModel extends GeoModel<AutomatonEntity> {
    @Override
    public ResourceLocation getModelResource(AutomatonEntity animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "geo/automaton.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AutomatonEntity animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "textures/entity/automaton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AutomatonEntity animatable) {
        return new ResourceLocation(DragonsDungeons.MODID, "animations/automaton.animation.json");
    }

    @Override
    public void setCustomAnimations(AutomatonEntity animatable, long instanceId, AnimationState<AutomatonEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
