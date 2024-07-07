package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, DragonsDungeons.MODID);

    public static RegistryObject<SimpleParticleType> ACID_FLASK_PARTICLE = PARTICLES.register("acid_flask_particle", () -> new SimpleParticleType(false));
    public static RegistryObject<SimpleParticleType> MITHRIL_CRYSTAL_PARTICLE = PARTICLES.register("mithril_crystal_particle", () -> new SimpleParticleType(false));

}
