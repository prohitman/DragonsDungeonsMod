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
        simpleBlock(ModBlocks.PAINTED_AGING_ADOBE.get());
        simpleBlock(ModBlocks.CRACKED_AGING_ADOBE.get());

        //Greenschist
        simpleBlock(ModBlocks.GREENSCHIST.get());
        logBlock((RotatedPillarBlock) ModBlocks.GREENSCHIST_PILLAR.get());
        simpleBlock(ModBlocks.GREENSCHIST_BRICKS.get());
        simpleBlock(ModBlocks.CHISELED_GREENSCHIST.get());
        simpleBlock(ModBlocks.POLISHED_GREENSCHIST.get());
        simpleBlock(ModBlocks.ALGAL_GREENSCHIST_BRICKS.get());
    }
}
