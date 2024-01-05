package com.prohitman.dragonsdungeons.core.datagen.client;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DragonsDungeons.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //Adobe
        simpleBlock(ModBlocks.AGING_ADOBE.get());
        logBlock((RotatedPillarBlock) ModBlocks.AGING_ADOBE_PILLAR.get());
        simpleBlock(ModBlocks.CHISELED_AGING_ADOBE.get());
        simpleBlock(ModBlocks.MOSSY_AGING_ADOBE.get());
        logBlock((RotatedPillarBlock) ModBlocks.PAINTED_AGING_ADOBE.get());
        simpleBlock(ModBlocks.CRACKED_AGING_ADOBE.get());

        //Greenschist
        simpleBlock(ModBlocks.GREENSCHIST.get());
        logBlock((RotatedPillarBlock) ModBlocks.GREENSCHIST_PILLAR.get());
        simpleBlock(ModBlocks.GREENSCHIST_BRICKS.get());
        simpleBlock(ModBlocks.CHISELED_GREENSCHIST.get());
        simpleBlock(ModBlocks.POLISHED_GREENSCHIST.get());
        simpleBlock(ModBlocks.ALGAL_GREENSCHIST_BRICKS.get());

        //Shale
        simpleBlock(ModBlocks.SHALE.get());
        logBlock((RotatedPillarBlock) ModBlocks.SHALE_PILLAR.get());
        simpleBlock(ModBlocks.SHALE_BRICKS.get());
        simpleBlock(ModBlocks.SHALE_SHINGLES.get());
        simpleBlock(ModBlocks.SHALE_TILES.get());
        simpleBlock(ModBlocks.CHISELED_SHALE.get());
        simpleBlock(ModBlocks.CRACKED_SHALE_BRICKS.get());
        simpleBlock(ModBlocks.MOSSY_SHALE_BRICKS.get());
        simpleBlock(ModBlocks.POLISHED_SHALE.get());

        //Ironstone
        simpleBlock(ModBlocks.RAW_IRONSTONE.get());
        logBlock((RotatedPillarBlock) ModBlocks.IRONSTONE_PILLAR.get());
        logBlock((RotatedPillarBlock) ModBlocks.IRONSTONE_BATTLEMENT.get());
        simpleBlock(ModBlocks.IRONSTONE_BRICKS.get());
        simpleBlock(ModBlocks.IRONSTONE_TILES.get());
        simpleBlock(ModBlocks.POLISHED_IRONSTONE.get());
        simpleBlock(ModBlocks.CHISELED_IRONSTONE.get());
        simpleBlock(ModBlocks.CRACKED_IRONSTONE_BRICKS.get());
        simpleBlock(ModBlocks.MOSSY_IRONSTONE_BRICKS.get());

        //Forged Bricks
        simpleBlock(ModBlocks.FORGED_BRICKS.get());
        logBlock((RotatedPillarBlock) ModBlocks.FORGED_PILLAR.get());
        simpleBlock(ModBlocks.FORGED_TILES.get());
        simpleBlock(ModBlocks.CRACKED_FORGED_BRICKS.get());
        simpleBlock(ModBlocks.LINED_FORGED_BRICKS.get());

        //Lavarock
        simpleBlock(ModBlocks.LAVAROCK.get());
        logBlock((RotatedPillarBlock) ModBlocks.LAVAROCK_PILLAR.get());
        simpleBlock(ModBlocks.LAVAROCK_BRICKS.get());
        simpleBlock(ModBlocks.CHISELED_LAVAROCK.get());
        simpleBlock(ModBlocks.CRACKED_LAVAROCK_BRICKS.get());
        simpleBlock(ModBlocks.POLISHED_LAVAROCK.get());
        simpleBlock(ModBlocks.CHARRED_LAVAROCK_BRICKS.get());
    }
}
