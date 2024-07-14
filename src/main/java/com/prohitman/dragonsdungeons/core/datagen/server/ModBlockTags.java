package com.prohitman.dragonsdungeons.core.datagen.server;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends BlockTagsProvider {
    public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DragonsDungeons.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.BEACON_BASE_BLOCKS)
                .add(ModBlocks.DWARVEN_STEEL_BLOCK.get())
                .add(ModBlocks.STEEL_BLOCK.get())
                .add(ModBlocks.MITHRIL_BLOCK.get())
                .add(ModBlocks.ELVEN_BRASS_BLOCK.get());

        createStairSlabWallTags();
    }

    public void createStairSlabWallTags(){
        List<RegistryObject<Block>> list = new LinkedList<>();

        list.addAll(ModBlocks.BLOCKS.getEntries().stream()
                .filter((registryObject) -> registryObject.get() instanceof SlabBlock
                        || registryObject.get() instanceof WallBlock
                        || registryObject.get() instanceof StairBlock).toList());

        list.forEach((registryObject -> {
            if(registryObject.get() instanceof WallBlock){
                tag(BlockTags.WALLS)
                        .add(registryObject.get());
            }else if(registryObject.get() instanceof StairBlock) {
                tag(BlockTags.STAIRS)
                        .add(registryObject.get());
            }else{
                tag(BlockTags.SLABS)
                        .add(registryObject.get());
            }
        }));
    }
}
