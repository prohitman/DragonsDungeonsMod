package com.prohitman.dragonsdungeons.core.datagen.client;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.codehaus.plexus.util.StringUtils;

public class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, DragonsDungeons.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addItem(ModItems.DRAGON_BONE);

        addBlock(ModBlocks.AGING_ADOBE);
        addBlock(ModBlocks.AGING_ADOBE_PILLAR);
        addBlock(ModBlocks.MOSSY_AGING_ADOBE);
        addBlock(ModBlocks.CHISELED_AGING_ADOBE);
        addBlock(ModBlocks.PAINTED_AGING_ADOBE);
        addBlock(ModBlocks.CRACKED_AGING_ADOBE);

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

        add("itemGroup.dragonsdungeons", "Dragons & Dungeons Mod");
    }

    public void addBlock(RegistryObject<Block> key) {
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }

    public void addItem(RegistryObject<Item> key){
        add(key.get().getDescriptionId(), StringUtils.capitaliseAllWords(key.getId().getPath().replaceAll("_", " ")));
    }
}
