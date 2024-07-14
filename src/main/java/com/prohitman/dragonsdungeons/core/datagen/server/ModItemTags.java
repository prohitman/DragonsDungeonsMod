package com.prohitman.dragonsdungeons.core.datagen.server;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemTags extends ItemTagsProvider {
    public static final TagKey<Item> LIGHTABLES = bind("lightables");

    public ModItemTags(PackOutput generator, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, pLookupProvider, blockTags, DragonsDungeons.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(LIGHTABLES).add(Items.TORCH).add(Items.FLINT_AND_STEEL).add(Items.FIRE_CHARGE).add(ModBlocks.STANDING_TORCH.get().asItem()).add(Blocks.CAMPFIRE.asItem());

        loadAxeTags();
        loadShovelTags();
        loadHoeTags();
        loadSwordTags();
    }

    public void loadShovelTags(){
        List<Item> shovels = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item1 -> item1 instanceof ShovelItem).toList();
        shovels.forEach(item -> tag(ItemTags.SHOVELS).add(item));
    }

    public void loadSwordTags(){
        List<Item> swords = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item1 -> item1 instanceof SwordItem).toList();
        swords.forEach(item -> tag(ItemTags.SWORDS).add(item));
    }

    public void loadAxeTags(){
        List<Item> axes = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item1 -> item1 instanceof AxeItem).toList();
        axes.forEach(item -> tag(ItemTags.AXES).add(item));
    }

    public void loadHoeTags(){
        List<Item> hoes = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)
                .filter(item1 -> item1 instanceof HoeItem).toList();
        hoes.forEach(item -> tag(ItemTags.HOES).add(item));
    }

    private static TagKey<Item> bind(String pName) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(DragonsDungeons.MODID, pName));
    }

    public static TagKey<Item> create(final ResourceLocation name) {
        return TagKey.create(Registries.ITEM, name);
    }
}
