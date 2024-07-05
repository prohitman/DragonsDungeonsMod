package com.prohitman.dragonsdungeons.core.datagen.client;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.stringtemplate.v4.ST;

import java.util.LinkedList;
import java.util.List;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DragonsDungeons.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Items
        createSingleWithToolset(ModItems.DWARVEN_STEEL_INGOT);
        createSingleWithToolset(ModItems.DRAGON_BONE);
        createSingleWithToolset(ModItems.MITHRIL_INGOT);
        createSingleWithToolset(ModItems.STEEL_INGOT);
        createSingleWithToolset(ModItems.ELVEN_BRASS_INGOT);
        createSingle(ModItems.OLDFORGED_INGOT);
        createSingle(ModItems.MAGE_LOOT);
        createSingle(ModItems.LOOT_BAG);
        createSingle(ModItems.BEARDED_AXE);
        createSingle(ModItems.FLASK_OF_FIRE);
        createSingle(ModItems.FLASK_OF_ACID);


        //Armors
        createSingle(ModItems.STEEL_HELMET);
        createSingle(ModItems.STEEL_CHESTPLATE);
        createSingle(ModItems.STEEL_LEGGINGS);
        createSingle(ModItems.STEEL_BOOTS);

        createSingle(ModItems.ELVEN_BRASS_HELMET);
        createSingle(ModItems.ELVEN_BRASS_CHESTPLATE);
        createSingle(ModItems.ELVEN_BRASS_LEGGINGS);
        createSingle(ModItems.ELVEN_BRASS_BOOTS);

        createSingle(ModItems.MITHRIL_HELMET);
        createSingle(ModItems.MITHRIL_CHESTPLATE);
        createSingle(ModItems.MITHRIL_LEGGINGS);
        createSingle(ModItems.MITHRIL_BOOTS);

        createSingle(ModItems.DRAGONBONE_HELMET);
        createSingle(ModItems.DRAGONBONE_CHESTPLATE);
        createSingle(ModItems.DRAGONBONE_LEGGINGS);
        createSingle(ModItems.DRAGONBONE_BOOTS);

        createSingle(ModItems.GOLD_COINS);
        createSingle(ModItems.RAW_ADAMANTITE);
        createSingle(ModItems.BLANK_OBELISK);
        createSingle(ModItems.WYVERN_STINGER);

        withExistingParent(ModItems.WARG_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.EMBLEMSCUTE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.ZARG_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        //Other Blocks
        createParent(ModBlocks.SHINGLES);
        createParent(ModBlocks.RUINED_SHINGLES);
        createParent(ModBlocks.RUBBLE);
        createParent(ModBlocks.CHUNKY_RUBBLE);

        createParent(ModBlocks.THATCH);
        singleTexture((ModBlocks.MITHRIL_CRYSTAL.getId().getPath()),
                mcLoc("item/generated"),
                "layer0", modLoc("item/mithril_crystal"));
        createParent(ModBlocks.BARROW_STONES);
        createParent(ModBlocks.THATCH_ROOF);
        createParent(ModBlocks.HANGING_BARROW_MOSS);
        createParent(ModBlocks.STONE_WINDOW);
        createBlockSingleHandheld(ModBlocks.STANDING_TORCH, "item/standing_torch");
        createParent(ModBlocks.TREASURE_CHEST);
        createParent(ModBlocks.URN);
        createParent(ModBlocks.FOUNDRY);

        createParent(ModBlocks.MITHRIL_BLOCK);
        createParent(ModBlocks.ELVEN_BRASS_BLOCK);
        createParent(ModBlocks.DWARVEN_STEEL_BLOCK);
        createParent(ModBlocks.STEEL_BLOCK);
        createBlockSingle(ModBlocks.FORGED_DOOR, "item/forged_door");

        createStairWallSlabParents();

        //Adamantite
        createParent(ModBlocks.ADAMANTITE_ORE);
        createParent(ModBlocks.DEEPSLATE_ADAMANTITE_ORE);
        createParent(ModBlocks.RAW_ADAMANTITE_BLOCK);

        //Adobe
        createParent(ModBlocks.AGING_ADOBE);
        createParent(ModBlocks.AGING_ADOBE_PILLAR);
        createParent(ModBlocks.CHISELED_AGING_ADOBE);
        createParent(ModBlocks.CRACKED_AGING_ADOBE);
        createParent(ModBlocks.PAINTED_AGING_ADOBE);
        createParent(ModBlocks.MOSSY_AGING_ADOBE);

        //Greenschist
        createParent(ModBlocks.GREENSCHIST);
        createParent(ModBlocks.GREENSCHIST_BRICKS);
        createParent(ModBlocks.GREENSCHIST_PILLAR);
        createParent(ModBlocks.CHISELED_GREENSCHIST);
        createParent(ModBlocks.POLISHED_GREENSCHIST);
        createParent(ModBlocks.ALGAL_GREENSCHIST_BRICKS);

        //Shale
        createParent(ModBlocks.SHALE);
        createParent(ModBlocks.SHALE_BRICKS);
        createParent(ModBlocks.SHALE_PILLAR);
        createParent(ModBlocks.SHALE_SHINGLES);
        createParent(ModBlocks.SHALE_TILES);
        createParent(ModBlocks.CHISELED_SHALE);
        createParent(ModBlocks.CRACKED_SHALE_BRICKS);
        createParent(ModBlocks.MOSSY_SHALE_BRICKS);
        createParent(ModBlocks.POLISHED_SHALE);

        //Ironstone
        createParent(ModBlocks.RAW_IRONSTONE);
        createParent(ModBlocks.IRONSTONE_PILLAR);
        createParent(ModBlocks.IRONSTONE_BRICKS);
        createParent(ModBlocks.IRONSTONE_BATTLEMENT);
        createParent(ModBlocks.IRONSTONE_TILES);
        createParent(ModBlocks.POLISHED_IRONSTONE);
        createParent(ModBlocks.CRACKED_IRONSTONE_BRICKS);
        createParent(ModBlocks.CHISELED_IRONSTONE);
        createParent(ModBlocks.MOSSY_IRONSTONE_BRICKS);

        //Forged Bricks
        createParent(ModBlocks.FORGED_BRICKS);
        createParent(ModBlocks.FORGED_PILLAR);
        createParent(ModBlocks.FORGED_TILES);
        createParent(ModBlocks.CRACKED_FORGED_BRICKS);
        createParent(ModBlocks.LINED_FORGED_BRICKS);

        //Lavarock
        createParent(ModBlocks.LAVAROCK);
        createParent(ModBlocks.LAVAROCK_BRICKS);
        createParent(ModBlocks.LAVAROCK_PILLAR);
        createParent(ModBlocks.CHISELED_LAVAROCK);
        createParent(ModBlocks.CHARRED_LAVAROCK_BRICKS);
        createParent(ModBlocks.CRACKED_LAVAROCK_BRICKS);
        createParent(ModBlocks.POLISHED_LAVAROCK);

    }

    private void createBlockSingle(RegistryObject<Block> block, String location){
        singleTexture((block.getId().getPath()),
                mcLoc("item/generated"),
                "layer0", modLoc(location));
    }

    private void createBlockSingleHandheld(RegistryObject<Block> block, String location){
        singleTexture((block.getId().getPath()),
                mcLoc("item/handheld"),
                "layer0", modLoc(location));
    }

    private void createParent(RegistryObject<Block> handler) {
        withExistingParent(handler.getId().getPath(), modLoc( "block/" + handler.getId().getPath()));
    }

    private void createParentBlock(RegistryObject<Block> handler) {
        withExistingParent(handler.getId().getPath(), modLoc( "block/" + handler.getId().getPath()));
    }

    private void createStairWallSlabParents(){
        List<RegistryObject<Block>> list = new LinkedList<>();

        list.addAll(ModBlocks.BLOCKS.getEntries().stream()
                .filter((registryObject) -> registryObject.get() instanceof SlabBlock
                        || registryObject.get() instanceof WallBlock
                        || registryObject.get() instanceof StairBlock).toList());

        list.forEach((registryObject -> {
            if(registryObject.get() instanceof WallBlock){
                withExistingParent(name(registryObject.get()), mcLoc("block/wall_inventory"))
                        .texture("wall", modLoc("block/" + name(registryObject).replaceAll("_wall", "")));
            }else {
                createParent(registryObject);
            }
        }));
    }

/*
    private Block getBlock(RegistryObject<Block> block){
        return ForgeRegistries.BLOCKS.(new ResourceLocation(DragonsDungeons.MODID, name(block)));
    }*/

    private String name(RegistryObject<Block> block) {
        return block.getId().getPath();
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    private void createSingle(RegistryObject<Item> item) {
        ModelFile generated = getExistingFile(mcLoc("item/generated"));
        getBuilder(item.getId().getPath()).parent(generated).texture("layer0", modLoc( "item/" + item.getId().getPath()));
    }

    private void createSingleWithToolset(RegistryObject<Item> item) {
        ModelFile generated = getExistingFile(mcLoc("item/generated"));
        ModelFile handheld = getExistingFile(mcLoc("item/handheld"));
        String material = item.getId().getPath().replace("_ingot", "");

        getBuilder(item.getId().getPath()).parent(generated).texture("layer0", modLoc( "item/" + item.getId().getPath()));

        getBuilder(material + "_sword").parent(handheld).texture("layer0", modLoc( "item/" + material + "_sword") );
        getBuilder(material + "_pickaxe").parent(handheld).texture("layer0", modLoc( "item/" + material + "_pickaxe"));
        getBuilder(material + "_axe").parent(handheld).texture("layer0", modLoc( "item/" + material + "_axe"));
        getBuilder(material + "_shovel").parent(handheld).texture("layer0", modLoc( "item/" + material + "_shovel"));
        getBuilder(material + "_hoe").parent(handheld).texture("layer0", modLoc( "item/" + material + "_hoe"));
    }
}
