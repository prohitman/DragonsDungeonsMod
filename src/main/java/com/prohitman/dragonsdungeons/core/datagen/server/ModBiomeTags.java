package com.prohitman.dragonsdungeons.core.datagen.server;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTags extends BiomeTagsProvider {
    public static final TagKey<Biome> SPOTTED_WARG_SPAWNS = createTag("spotted_warg_spawns");
    public static final TagKey<Biome> PALE_WARG_SPAWNS = createTag("pale_warg_spawns");
    public static final TagKey<Biome> SNOWY_WARG_SPAWNS = createTag("snowy_warg_spawns");
    public static final TagKey<Biome> BLACK_WARG_SPAWNS = createTag("black_warg_spawns");
    public static final TagKey<Biome> CHESTNUT_WARG_SPAWNS = createTag("chestnut_warg_spawns");
    public static final TagKey<Biome> STRIPED_WARG_SPAWNS = createTag("striped_warg_spawns");
    public static final TagKey<Biome> ASHEN_WARG_SPAWNS = createTag("ashen_warg_spawns");
    public static final TagKey<Biome> RUSTY_WARG_SPAWNS = createTag("rusty_warg_spawns");
    public static final TagKey<Biome> WOOD_WARG_SPAWNS = createTag("wood_warg_spawns");
    public static final TagKey<Biome> WARG_SPAWNS = createTag("warg_spawns");


    public ModBiomeTags(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, DragonsDungeons.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(SPOTTED_WARG_SPAWNS).addTags(BiomeTags.IS_SAVANNA);
        tag(PALE_WARG_SPAWNS).add(Biomes.TAIGA);
        tag(SNOWY_WARG_SPAWNS).addTags(BiomeTags.IS_MOUNTAIN);
        tag(BLACK_WARG_SPAWNS).add(Biomes.OLD_GROWTH_PINE_TAIGA);
        tag(CHESTNUT_WARG_SPAWNS).add(Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        tag(STRIPED_WARG_SPAWNS).add(Biomes.WOODED_BADLANDS);
        tag(ASHEN_WARG_SPAWNS).add(Biomes.SNOWY_TAIGA);
        tag(RUSTY_WARG_SPAWNS).add(Biomes.SPARSE_JUNGLE);
        tag(WOOD_WARG_SPAWNS).add(Biomes.FOREST);

        tag(WARG_SPAWNS).addTags(SPOTTED_WARG_SPAWNS)
                        .addTags(PALE_WARG_SPAWNS)
                        .addTags(SNOWY_WARG_SPAWNS)
                        .addTags(BLACK_WARG_SPAWNS)
                        .addTags(CHESTNUT_WARG_SPAWNS)
                        .addTags(STRIPED_WARG_SPAWNS)
                        .addTags(ASHEN_WARG_SPAWNS)
                        .addTags(WOOD_WARG_SPAWNS)
                        .addTags(RUSTY_WARG_SPAWNS);
    }

    private static TagKey<Biome> createTag(String name)
    {
        return TagKey.create(Registries.BIOME, new ResourceLocation(DragonsDungeons.MODID, name));
    }
}
