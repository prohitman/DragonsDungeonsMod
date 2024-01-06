package com.prohitman.dragonsdungeons.core.datagen.client;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.blocks.enums.ConnectedState;
import com.prohitman.dragonsdungeons.common.blocks.obj.StoneWindow;
import com.prohitman.dragonsdungeons.common.blocks.shaped.MithrilCrystal;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
                .renderType("cutout_mipped")
                .ao(false));
        horizontalBlock(ModBlocks.BARROW_STONES.get(),
                models().withExistingParent(name(ModBlocks.BARROW_STONES.get()) + "_dd", modLoc("block/" + name(ModBlocks.BARROW_STONES.get())))
                .renderType("cutout_mipped")
                .ao(false));
        horizontalBlock(ModBlocks.THATCH_ROOF.get(),
                models().withExistingParent(name(ModBlocks.THATCH_ROOF.get()) + "_dd", modLoc("block/" + name(ModBlocks.THATCH_ROOF.get())))
                        .renderType("cutout_mipped")
                        .ao(false));
        createBlockWithModel(ModBlocks.HANGING_BARROW_MOSS);
        createStoneWindow(ModBlocks.STONE_WINDOW);
        createDoubleBlockModel(ModBlocks.STANDING_TORCH);

        //Adobe
        simpleBlock(ModBlocks.AGING_ADOBE.get());
        createStairSlabWall(ModBlocks.AGING_ADOBE);
        logBlock((RotatedPillarBlock) ModBlocks.AGING_ADOBE_PILLAR.get());
        simpleBlock(ModBlocks.CHISELED_AGING_ADOBE.get());
        simpleBlock(ModBlocks.MOSSY_AGING_ADOBE.get());
        createStairSlabWall(ModBlocks.MOSSY_AGING_ADOBE);
        createAxisBlock((RotatedPillarBlock) ModBlocks.PAINTED_AGING_ADOBE.get(), ModBlocks.AGING_ADOBE);
        simpleBlock(ModBlocks.CRACKED_AGING_ADOBE.get());
        createStairSlabWall(ModBlocks.CRACKED_AGING_ADOBE);

        //Greenschist
        simpleBlock(ModBlocks.GREENSCHIST.get());
        createStairSlabWall(ModBlocks.GREENSCHIST);

        logBlock((RotatedPillarBlock) ModBlocks.GREENSCHIST_PILLAR.get());
        simpleBlock(ModBlocks.GREENSCHIST_BRICKS.get());
        createStairSlabWall(ModBlocks.GREENSCHIST_BRICKS);

        simpleBlock(ModBlocks.CHISELED_GREENSCHIST.get());
        simpleBlock(ModBlocks.POLISHED_GREENSCHIST.get());
        createStairSlabWall(ModBlocks.POLISHED_GREENSCHIST);

        simpleBlock(ModBlocks.ALGAL_GREENSCHIST_BRICKS.get());
        createStairSlabWall(ModBlocks.ALGAL_GREENSCHIST_BRICKS);

        //Shale
        simpleBlock(ModBlocks.SHALE.get());
        createStairSlabWall(ModBlocks.SHALE);

        logBlock((RotatedPillarBlock) ModBlocks.SHALE_PILLAR.get());
        simpleBlock(ModBlocks.SHALE_BRICKS.get());
        createStairSlabWall(ModBlocks.SHALE_BRICKS);

        simpleBlock(ModBlocks.SHALE_SHINGLES.get());
        simpleBlock(ModBlocks.SHALE_TILES.get());
        createStairSlabWall(ModBlocks.SHALE_TILES);

        simpleBlock(ModBlocks.CHISELED_SHALE.get());
        simpleBlock(ModBlocks.CRACKED_SHALE_BRICKS.get());
        createStairSlabWall(ModBlocks.CRACKED_SHALE_BRICKS);

        simpleBlock(ModBlocks.MOSSY_SHALE_BRICKS.get());
        createStairSlabWall(ModBlocks.MOSSY_SHALE_BRICKS);

        simpleBlock(ModBlocks.POLISHED_SHALE.get());
        createStairSlabWall(ModBlocks.POLISHED_SHALE);

        //Ironstone
        simpleBlock(ModBlocks.RAW_IRONSTONE.get());
        createStairSlabWall(ModBlocks.RAW_IRONSTONE);

        logBlock((RotatedPillarBlock) ModBlocks.IRONSTONE_PILLAR.get());
        logBlock((RotatedPillarBlock) ModBlocks.IRONSTONE_BATTLEMENT.get());
        simpleBlock(ModBlocks.IRONSTONE_BRICKS.get());
        createStairSlabWall(ModBlocks.IRONSTONE_BRICKS);

        simpleBlock(ModBlocks.IRONSTONE_TILES.get());
        createStairSlabWall(ModBlocks.IRONSTONE_TILES);

        simpleBlock(ModBlocks.POLISHED_IRONSTONE.get());
        createStairSlabWall(ModBlocks.POLISHED_IRONSTONE);

        simpleBlock(ModBlocks.CHISELED_IRONSTONE.get());
        simpleBlock(ModBlocks.CRACKED_IRONSTONE_BRICKS.get());
        createStairSlabWall(ModBlocks.CRACKED_IRONSTONE_BRICKS);

        simpleBlock(ModBlocks.MOSSY_IRONSTONE_BRICKS.get());
        createStairSlabWall(ModBlocks.MOSSY_IRONSTONE_BRICKS);

        //Forged Bricks
        simpleBlock(ModBlocks.FORGED_BRICKS.get());
        createStairSlabWall(ModBlocks.FORGED_BRICKS);

        logBlock((RotatedPillarBlock) ModBlocks.FORGED_PILLAR.get());
        simpleBlock(ModBlocks.FORGED_TILES.get());
        createStairSlabWall(ModBlocks.FORGED_TILES);

        simpleBlock(ModBlocks.CRACKED_FORGED_BRICKS.get());
        createStairSlabWall(ModBlocks.CRACKED_FORGED_BRICKS);

        simpleBlock(ModBlocks.LINED_FORGED_BRICKS.get());
        createStairSlabWall(ModBlocks.LINED_FORGED_BRICKS);

        //Lavarock
        simpleBlock(ModBlocks.LAVAROCK.get());
        createStairSlabWall(ModBlocks.LAVAROCK);

        logBlock((RotatedPillarBlock) ModBlocks.LAVAROCK_PILLAR.get());
        simpleBlock(ModBlocks.LAVAROCK_BRICKS.get());
        createStairSlabWall(ModBlocks.LAVAROCK_BRICKS);

        simpleBlock(ModBlocks.CHISELED_LAVAROCK.get());
        simpleBlock(ModBlocks.CRACKED_LAVAROCK_BRICKS.get());
        createStairSlabWall(ModBlocks.CRACKED_LAVAROCK_BRICKS);

        simpleBlock(ModBlocks.POLISHED_LAVAROCK.get());
        createStairSlabWall(ModBlocks.POLISHED_LAVAROCK);

        simpleBlock(ModBlocks.CHARRED_LAVAROCK_BRICKS.get());
        createStairSlabWall(ModBlocks.CHARRED_LAVAROCK_BRICKS);
    }

    public void createStairSlabWall(RegistryObject<Block> block){
        stairsBlock((StairBlock) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(DragonsDungeons.MODID, name(block) + "_stairs")),
                modLoc("block/" + name(block)));
        slabBlock((SlabBlock) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(DragonsDungeons.MODID, name(block) + "_slab")),
                modLoc("block/" + name(block)),
                modLoc("block/" + name(block)));
        wallBlock((WallBlock) ForgeRegistries.BLOCKS.getValue(new ResourceLocation(DragonsDungeons.MODID, name(block) + "_wall")),
                modLoc("block/" + name(block)));
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

    public void createDoubleBlockModel(RegistryObject<Block> block){
        createDoubleBlockModel(block.get(),
                models().getExistingFile(modLoc("block/" + name(block) + "_upper")),
                models().getExistingFile(modLoc("block/" + name(block) + "_lower")));
    }

    public void createDoubleBlockModel(Block block, ModelFile upper, ModelFile lower){
        getVariantBuilder(block).forAllStates(state -> {
            ModelFile model = lower;
            if(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER){
                model = upper;
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        });
    }

    public void createStoneWindow(RegistryObject<Block> block){
        stoneWindow(block.get(),
                models().cubeAll(name(block), modLoc("block/" + name(block))).renderType("cutout_mipped"),
                models().cubeAll(name(block) + "_top", modLoc("block/" + name(block) + "_top")).renderType("cutout_mipped"),
                models().cubeAll(name(block) + "_bottom", modLoc("block/" + name(block) + "_bottom")).renderType("cutout_mipped"),
                models().cubeAll(name(block) + "_middle", modLoc("block/" + name(block) + "_middle")).renderType("cutout_mipped"));
    }

    public void stoneWindow(Block window, ModelFile none, ModelFile top, ModelFile bottom, ModelFile both){
        getVariantBuilder(window).forAllStates(blockState -> {
            ModelFile model = none;
            if(blockState.getValue(StoneWindow.CONNECTED_STATE) == ConnectedState.TOP){
                model = top;
            } else if(blockState.getValue(StoneWindow.CONNECTED_STATE) == ConnectedState.BOTTOM){
                model = bottom;
            } else if(blockState.getValue(StoneWindow.CONNECTED_STATE) == ConnectedState.BOTH){
                model = both;
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
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
