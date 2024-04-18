package com.prohitman.dragonsdungeons.core.datagen.client;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.codehaus.plexus.util.StringUtils;

import javax.swing.text.html.parser.Entity;
import java.util.LinkedList;
import java.util.List;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, DragonsDungeons.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.DRAGON_BONE);
        addItem(ModItems.GOLD_COINS);
        addItem(ModItems.RAW_ADAMANTITE);

        addItem(ModItems.WARG_SPAWN_EGG);
        add( "entity.dragonsdungeons.warg", "Warg");

        add( "effect.dragonsdungeons.intoxicated", "Intoxicated");

        addItem(ModItems.ELVEN_BRASS_INGOT);
        addItem(ModItems.DWARVEN_STEEL_INGOT);
        addItem(ModItems.STEEL_INGOT);
        addItem(ModItems.OLDFORGED_INGOT);
        addItem(ModItems.MITHRIL_INGOT);
        addItem(ModItems.BLANK_OBELISK);
        addItem(ModItems.WYVERN_STINGER);

        addAllTools();
        addAllSlabsStairsWalls();

        addBlock(ModBlocks.AGING_ADOBE);
        addBlock(ModBlocks.AGING_ADOBE_PILLAR);
        addBlock(ModBlocks.MOSSY_AGING_ADOBE);
        addBlock(ModBlocks.CHISELED_AGING_ADOBE);
        addBlock(ModBlocks.PAINTED_AGING_ADOBE);
        addBlock(ModBlocks.CRACKED_AGING_ADOBE);
        addBlock(ModBlocks.BARROW_STONES);
        addBlock(ModBlocks.THATCH_ROOF);
        addBlock(ModBlocks.HANGING_BARROW_MOSS);
        addBlock(ModBlocks.STONE_WINDOW);
        addBlock(ModBlocks.STANDING_TORCH);
        addBlock(ModBlocks.TREASURE_CHEST);
        addBlock(ModBlocks.URN);
        addBlock(ModBlocks.FOUNDRY);
        addBlock(ModBlocks.DWARVEN_STEEL_BLOCK);
        addBlock(ModBlocks.ELVEN_BRASS_BLOCK);
        addBlock(ModBlocks.STEEL_BLOCK);
        addBlock(ModBlocks.MITHRIL_BLOCK);
        addBlock(ModBlocks.FORGED_DOOR);

        addBlock(ModBlocks.GREENSCHIST);
        addBlock(ModBlocks.GREENSCHIST_BRICKS);
        addBlock(ModBlocks.GREENSCHIST_PILLAR);
        addBlock(ModBlocks.POLISHED_GREENSCHIST);
        addBlock(ModBlocks.ALGAL_GREENSCHIST_BRICKS);
        addBlock(ModBlocks.CHISELED_GREENSCHIST);

        addBlock(ModBlocks.SHALE_BRICKS);
        addBlock(ModBlocks.SHALE);
        addBlock(ModBlocks.SHALE_PILLAR);
        addBlock(ModBlocks.SHALE_SHINGLES);
        addBlock(ModBlocks.SHALE_TILES);
        addBlock(ModBlocks.CHISELED_SHALE);
        addBlock(ModBlocks.MOSSY_SHALE_BRICKS);
        addBlock(ModBlocks.CRACKED_SHALE_BRICKS);
        addBlock(ModBlocks.POLISHED_SHALE);

        addBlock(ModBlocks.RAW_IRONSTONE);
        addBlock(ModBlocks.IRONSTONE_BRICKS);
        addBlock(ModBlocks.IRONSTONE_BATTLEMENT);
        addBlock(ModBlocks.IRONSTONE_PILLAR);
        addBlock(ModBlocks.CHISELED_IRONSTONE);
        addBlock(ModBlocks.CRACKED_IRONSTONE_BRICKS);
        addBlock(ModBlocks.MOSSY_IRONSTONE_BRICKS);
        addBlock(ModBlocks.POLISHED_IRONSTONE);
        addBlock(ModBlocks.IRONSTONE_TILES);

        addBlock(ModBlocks.FORGED_BRICKS);
        addBlock(ModBlocks.FORGED_PILLAR);
        addBlock(ModBlocks.FORGED_TILES);
        addBlock(ModBlocks.LINED_FORGED_BRICKS);
        addBlock(ModBlocks.CRACKED_FORGED_BRICKS);

        addBlock(ModBlocks.LAVAROCK);
        addBlock(ModBlocks.LAVAROCK_BRICKS);
        addBlock(ModBlocks.LAVAROCK_PILLAR);
        addBlock(ModBlocks.CHISELED_LAVAROCK);
        addBlock(ModBlocks.POLISHED_LAVAROCK);
        addBlock(ModBlocks.CHARRED_LAVAROCK_BRICKS);
        addBlock(ModBlocks.CRACKED_LAVAROCK_BRICKS);

        addBlock(ModBlocks.SHINGLES);
        addBlock(ModBlocks.RUINED_SHINGLES);
        addBlock(ModBlocks.CHUNKY_RUBBLE);
        addBlock(ModBlocks.RUBBLE);

        addBlock(ModBlocks.THATCH);
        addBlock(ModBlocks.MITHRIL_CRYSTAL);

        addBlock(ModBlocks.ADAMANTITE_ORE);
        addBlock(ModBlocks.RAW_ADAMANTITE_BLOCK);
        addBlock(ModBlocks.DEEPSLATE_ADAMANTITE_ORE);

        add("itemGroup.dragonsdungeons", "Dragons & Dungeons Mod");
    }

    public void addAllSlabsStairsWalls(){
        List<RegistryObject<Block>> list = new LinkedList<>();

        list.addAll(ModBlocks.BLOCKS.getEntries().stream()
                .filter((registryObject) -> registryObject.get() instanceof SlabBlock
                        || registryObject.get() instanceof WallBlock
                        || registryObject.get() instanceof StairBlock).toList());

        list.forEach((this::addBlock));
    }

    public void addAllTools(){
        List<RegistryObject<Item>> list = new LinkedList<>();

        list.addAll(ModItems.ITEMS.getEntries().stream()
                .filter((registryObject) -> registryObject.get() instanceof SwordItem
                        || registryObject.get() instanceof AxeItem
                        || registryObject.get() instanceof ShovelItem
                        || registryObject.get() instanceof HoeItem
                        || registryObject.get() instanceof PickaxeItem).toList());

        list.forEach((this::addItem));
    }

    public void addBlock(RegistryObject<Block> key) {
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addItem(RegistryObject<Item> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }
}
