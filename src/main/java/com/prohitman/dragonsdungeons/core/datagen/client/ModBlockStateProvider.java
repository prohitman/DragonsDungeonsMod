package com.prohitman.dragonsdungeons.core.datagen.client;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.blocks.MithrilCrystal;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DragonsDungeons.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //Other Blocks
        simpleBlock(ModBlocks.SHINGLES.get());
        simpleBlock(ModBlocks.RUINED_SHINGLES.get());
        simpleBlock(ModBlocks.RUBBLE.get());
        simpleBlock(ModBlocks.CHUNKY_RUBBLE.get());

        createBlockWithModel(ModBlocks.THATCH);
        //createBlockWithModel(ModBlocks.MITHRIL_CRYSTAL);
        mithrilBlock((MithrilCrystal) ModBlocks.MITHRIL_CRYSTAL.get(), models().withExistingParent(name(ModBlocks.MITHRIL_CRYSTAL.get()) + "_dd", modLoc("block/" + name(ModBlocks.MITHRIL_CRYSTAL.get())))
                .renderType("cutout")
                .ao(false));
        /*horizontalBlock(ModBlocks.MITHRIL_CRYSTAL.get(),
                models().withExistingParent(name(ModBlocks.MITHRIL_CRYSTAL.get()) + "_dd", modLoc("block/" + name(ModBlocks.MITHRIL_CRYSTAL.get())))
                .renderType("cutout")
                .ao(false));*/

        //Adobe
        simpleBlock(ModBlocks.AGING_ADOBE.get());
        logBlock((RotatedPillarBlock) ModBlocks.AGING_ADOBE_PILLAR.get());
        simpleBlock(ModBlocks.CHISELED_AGING_ADOBE.get());
        simpleBlock(ModBlocks.MOSSY_AGING_ADOBE.get());
        createAxisBlock((RotatedPillarBlock) ModBlocks.PAINTED_AGING_ADOBE.get(), ModBlocks.AGING_ADOBE);
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

    public void mithrilBlock(MithrilCrystal block, ModelFile crystal) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(ButtonBlock.FACING);
            AttachFace face = state.getValue(ButtonBlock.FACE);

            return ConfiguredModel.builder()
                    .modelFile(crystal)
                    .rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180))
                    .rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot() + (face == AttachFace.FLOOR ? 90 : 0))
                    //.uvLock(face == AttachFace.WALL)
                    .build();
        });
    }

    public void createHorizontalBlock(RegistryObject<Block> block, RegistryObject<Block> frontBlock){
        horizontalBlock(block.get(),
                modLoc("block/" + block.getId().getPath()),
                modLoc("block/" + frontBlock.getId().getPath()),
                modLoc("block/" + block.getId().getPath() + "_top"));
    }

    public void createAxisBlock(RotatedPillarBlock block, RegistryObject<Block> bottomBlock) {
        axisBlock(block,
                models().cubeBottomTop(name(block), modLoc("block/" + name(block)),
                        modLoc("block/" + name(bottomBlock)), modLoc("block/" + name(block) + "_top")),
                models().withExistingParent(name(block), modLoc("block/" + "cube_bottomtop_horizontal"))
                                .texture("side", modLoc("block/" + name(block)))
                                        .texture("top", modLoc("block/" + name(block) + "_top"))
                                                .texture("end", modLoc("block/" + name(bottomBlock))));
    }

    public void createBlockWithModel(RegistryObject<Block> block){
        simpleBlock(block.get(),
                models().withExistingParent(name(block) + "_dd", modLoc("block/" + name(block)))
                        .renderType("cutout")
                        .ao(false));
    }

    private String name(RegistryObject<Block> block) {
        return block.getId().getPath();
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }
}
