package com.prohitman.dragonsdungeons.client.entities.models;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.Nullable;

public class EmblemscuteModel extends GeoModel<EmblemscuteEntity> {
    ResourceLocation TEXTURE_LOCATION = new ResourceLocation(DragonsDungeons.MODID, "textures/entity/emblemscute.png");
    ResourceLocation MODEL_LOCATION = new ResourceLocation(DragonsDungeons.MODID, "geo/emblemscute.geo.json");
    ResourceLocation ANIMATION_LOCATION = new ResourceLocation(DragonsDungeons.MODID, "animations/emblemscute.animation.json");

    @Override
    public ResourceLocation getModelResource(EmblemscuteEntity object) {
        return MODEL_LOCATION;
    }

    @Override
    public ResourceLocation getTextureResource(EmblemscuteEntity object) {
        return TEXTURE_LOCATION;
    }

    @Override
    public ResourceLocation getAnimationResource(EmblemscuteEntity animatable) {
        return ANIMATION_LOCATION;
    }

    @Override
    public void setCustomAnimations(EmblemscuteEntity animatable, long instanceId, AnimationState<EmblemscuteEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("Head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * ((float) Math.PI / 270));
            head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 270));
        }
    }
}
