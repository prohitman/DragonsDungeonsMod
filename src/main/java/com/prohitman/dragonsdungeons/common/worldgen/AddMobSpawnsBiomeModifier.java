package com.prohitman.dragonsdungeons.common.worldgen;

import com.mojang.serialization.Codec;
import com.prohitman.dragonsdungeons.core.datagen.server.ModBiomeTags;
import com.prohitman.dragonsdungeons.core.init.ModBiomeModifiers;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class AddMobSpawnsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase.equals(Phase.ADD)) {
            addMobSpawn(builder, biome, ModBiomeTags.WARG_SPAWNS, MobCategory.CREATURE, ModEntities.WARG.get(), 2, 3, 5);
            addMobSpawn(builder, biome, BiomeTags.IS_OVERWORLD, MobCategory.MONSTER, ModEntities.ZARG.get(), 1, 2, 4);
            addMobSpawn(builder, biome, BiomeTags.IS_OVERWORLD, MobCategory.MONSTER, ModEntities.EMBLEMSCUTE.get(), 1, 1, 1);
        }
    }

    void addMobSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder, Holder<Biome> biome, TagKey<Biome> tag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        if (biome.is(tag)) {
            builder.getMobSpawnSettings().addSpawn(mobCategory, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return ModBiomeModifiers.ADD_MOB_SPAWNS_CODEC.get();
    }
}
