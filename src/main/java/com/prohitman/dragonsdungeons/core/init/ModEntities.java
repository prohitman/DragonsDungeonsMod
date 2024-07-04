package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import com.prohitman.dragonsdungeons.common.entities.projectiles.FlaskOfAcidProjectile;
import com.prohitman.dragonsdungeons.common.entities.projectiles.FlaskOfFireProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DragonsDungeons.MODID);

    //Entities
    public static final RegistryObject<EntityType<WargEntity>> WARG =
            ENTITY_TYPES.register("warg",
                    () -> EntityType.Builder.of(WargEntity::new, MobCategory.CREATURE)
                            .sized(1.3f, 1.7f).build("warg"));

    public static final RegistryObject<EntityType<EmblemscuteEntity>> EMBLEMSCUTE = ENTITY_TYPES.register(
            "emblemscute",
            () -> EntityType.Builder.of(EmblemscuteEntity::new, MobCategory.AMBIENT)
                    .sized(1.25F, 1.25F)
                    .clientTrackingRange(8)
                    .setShouldReceiveVelocityUpdates(false)
                    .build("emblemscute"));


    //Projectiles
    public static final RegistryObject<EntityType<FlaskOfAcidProjectile>> FLASK_OF_ACID_PROJECTILE = ENTITY_TYPES.register(
            "flask_of_acid_projectile",
            () -> EntityType.Builder.<FlaskOfAcidProjectile>of(FlaskOfAcidProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .build("flask_of_acid_projectile"));

    public static final RegistryObject<EntityType<FlaskOfFireProjectile>> FLASK_OF_FIRE_PROJECTILE = ENTITY_TYPES.register(
            "flask_of_fire_projectile",
            () -> EntityType.Builder.<FlaskOfFireProjectile>of(FlaskOfFireProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .build("flask_of_fire_projectile"));
}
