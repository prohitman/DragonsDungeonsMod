package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.blocks.obj.*;
import com.prohitman.dragonsdungeons.common.blocks.shaped.BarrowStones;
import com.prohitman.dragonsdungeons.common.blocks.shaped.MithrilCrystal;
import com.prohitman.dragonsdungeons.common.blocks.shaped.ThatchRoof;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DragonsDungeons.MODID);

    public static final RegistryObject<Block> CHUNKY_RUBBLE = createRegistry("chunky_rubble", () -> new Block(BlockBehaviour.Properties.copy(Blocks.ROOTED_DIRT)), new Item.Properties());
    public static final RegistryObject<Block> RUBBLE = createRegistry("rubble", () -> new Block(BlockBehaviour.Properties.copy(Blocks.ROOTED_DIRT)), new Item.Properties());
    public static final RegistryObject<Block> THATCH = createRegistry("thatch", () -> new Block(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).noOcclusion()), new Item.Properties());
    public static final RegistryObject<Block> BARROW_STONES = createRegistry("barrow_stones", () -> new BarrowStones(BlockBehaviour.Properties.copy(Blocks.STONE).noOcclusion()), new Item.Properties());
    public static final RegistryObject<Block> HANGING_BARROW_MOSS = createRegistry("hanging_barrow_moss", () -> new HangingBarrowMoss(BlockBehaviour.Properties.copy(Blocks.MOSS_BLOCK).noOcclusion()), new Item.Properties());
    public static final RegistryObject<Block> THATCH_ROOF = createRegistry("thatch_roof", () -> new ThatchRoof(BlockBehaviour.Properties.copy(ModBlocks.THATCH.get()).noOcclusion()), new Item.Properties());
    public static final RegistryObject<Block> STONE_WINDOW = createRegistry("stone_window", () -> new StoneWindow(BlockBehaviour.Properties.copy(Blocks.GLASS)), new Item.Properties());
    public static final RegistryObject<Block> STANDING_TORCH = createRegistry("standing_torch", () -> new StandingTorch(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)), new Item.Properties());
    public static final RegistryObject<Block> TREASURE_CHEST = createRegistry("treasure_chest", () -> new TreasureChestBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()), new Item.Properties());
    public static final RegistryObject<Block> URN = createRegistry("urn", () -> new UrnBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD).ignitedByLava()), new Item.Properties());
    public static final RegistryObject<Block> FOUNDRY = createRegistry("foundry", () -> new FoundryBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)), new Item.Properties());

    public static final RegistryObject<Block> MITHRIL_CRYSTAL = createRegistry("mithril_crystal", () -> new MithrilCrystal(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK).lightLevel((blockState) -> 6).noOcclusion()), new Item.Properties());
    public static final RegistryObject<Block> RUINED_SHINGLES = createRegistryWithStairSlabWall("ruined_shingles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)), new Item.Properties(), false);
    public static final RegistryObject<Block> SHINGLES = createRegistryWithStairSlabWall("shingles", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS)), new Item.Properties(), false);
    public static final RegistryObject<Block> PALISADE = createRegistry("palisade", () -> new PalisadeBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)), new Item.Properties());

    public static final RegistryObject<Block> ADAMANTITE_ORE = createRegistry("adamantite_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE).mapColor(MapColor.COLOR_CYAN)), new Item.Properties());
    public static final RegistryObject<Block> DEEPSLATE_ADAMANTITE_ORE = createRegistry("deepslate_adamantite_ore", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_COPPER_ORE).mapColor(MapColor.COLOR_CYAN)), new Item.Properties());
    public static final RegistryObject<Block> RAW_ADAMANTITE_BLOCK = createRegistry("raw_adamantite_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.RAW_IRON_BLOCK).mapColor(MapColor.COLOR_CYAN)), new Item.Properties());
    public static final RegistryObject<Block> DWARVEN_STEEL_BLOCK = createRegistry("dwarven_steel_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), new Item.Properties());
    public static final RegistryObject<Block> ELVEN_BRASS_BLOCK = createRegistry("elven_brass_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.GOLD)), new Item.Properties());
    public static final RegistryObject<Block> MITHRIL_BLOCK = createRegistry("mithril_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIAMOND_BLOCK).mapColor(MapColor.DIAMOND)), new Item.Properties());
    public static final RegistryObject<Block> STEEL_BLOCK = createRegistry("steel_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), new Item.Properties());
    public static final RegistryObject<Block> FORGED_DOOR = createRegistry("forged_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_DOOR).mapColor(MapColor.COLOR_BLACK), BlockSetType.IRON), new Item.Properties());
    public static final RegistryObject<Block> BRAZIER = createRegistry("brazier", () -> new BrazierBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion().lightLevel(litBlockEmission(15)), false), new Item.Properties());
    public static final RegistryObject<Block> SOUL_BRAZIER = createRegistry("soul_brazier", () -> new BrazierBlock(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN).noOcclusion().lightLevel(litBlockEmission(10)), true), new Item.Properties());

    public static final RegistryObject<Block> AGING_ADOBE = createRegistryWithStairSlabWall("aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> AGING_ADOBE_PILLAR = createRegistry("aging_adobe_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CHISELED_AGING_ADOBE = createRegistry("chiseled_aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CRACKED_AGING_ADOBE = createRegistryWithStairSlabWall("cracked_aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> PAINTED_AGING_ADOBE = createRegistry("painted_aging_adobe", () -> new ModDirectionalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> MOSSY_AGING_ADOBE = createRegistryWithStairSlabWall("mossy_aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);

    public static final RegistryObject<Block> GREENSCHIST = createRegistryWithStairSlabWall("greenschist", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> GREENSCHIST_PILLAR = createRegistry("greenschist_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> ALGAL_GREENSCHIST_BRICKS = createRegistryWithStairSlabWall("algal_greenschist_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> CHISELED_GREENSCHIST = createRegistry("chiseled_greenschist", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> GREENSCHIST_BRICKS = createRegistryWithStairSlabWall("greenschist_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> POLISHED_GREENSCHIST = createRegistryWithStairSlabWall("polished_greenschist", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);

    public static final RegistryObject<Block> SHALE = createRegistryWithStairSlabWall("shale", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> SHALE_PILLAR = createRegistry("shale_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CHISELED_SHALE = createRegistry("chiseled_shale", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CRACKED_SHALE_BRICKS = createRegistryWithStairSlabWall("cracked_shale_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> MOSSY_SHALE_BRICKS = createRegistryWithStairSlabWall("mossy_shale_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> POLISHED_SHALE = createRegistryWithStairSlabWall("polished_shale", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> SHALE_BRICKS = createRegistryWithStairSlabWall("shale_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> SHALE_SHINGLES = createRegistry("shale_shingles", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> SHALE_TILES = createRegistryWithStairSlabWall("shale_tiles", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);

    public static final RegistryObject<Block> RAW_IRONSTONE = createRegistryWithStairSlabWall("raw_ironstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> IRONSTONE_PILLAR = createRegistry("ironstone_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CHISELED_IRONSTONE = createRegistry("chiseled_ironstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CRACKED_IRONSTONE_BRICKS = createRegistryWithStairSlabWall("cracked_ironstone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> IRONSTONE_BATTLEMENT = createRegistry("ironstone_battlement", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> IRONSTONE_BRICKS = createRegistryWithStairSlabWall("ironstone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> IRONSTONE_TILES = createRegistryWithStairSlabWall("ironstone_tiles", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> MOSSY_IRONSTONE_BRICKS = createRegistryWithStairSlabWall("mossy_ironstone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> POLISHED_IRONSTONE = createRegistryWithStairSlabWall("polished_ironstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);

    public static final RegistryObject<Block> FORGED_BRICKS = createRegistryWithStairSlabWall("forged_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> FORGED_PILLAR = createRegistry("forged_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> FORGED_TILES = createRegistryWithStairSlabWall("forged_tiles", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> CRACKED_FORGED_BRICKS = createRegistryWithStairSlabWall("cracked_forged_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> LINED_FORGED_BRICKS = createRegistryWithStairSlabWall("lined_forged_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);

    public static final RegistryObject<Block> LAVAROCK = createRegistryWithStairSlabWall("lavarock", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> LAVAROCK_PILLAR = createRegistry("lavarock_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> LAVAROCK_BRICKS = createRegistryWithStairSlabWall("lavarock_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> POLISHED_LAVAROCK = createRegistryWithStairSlabWall("polished_lavarock", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> CHARRED_LAVAROCK_BRICKS = createRegistryWithStairSlabWall("charred_lavarock_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);
    public static final RegistryObject<Block> CHISELED_LAVAROCK = createRegistry("chiseled_lavarock", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CRACKED_LAVAROCK_BRICKS = createRegistryWithStairSlabWall("cracked_lavarock_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties(), true);

    public static <T extends Block> RegistryObject<Block> createRegistry(String name, Supplier<T> block, Item.Properties properties) {
        RegistryObject<Block> object = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(object.get(), properties));

        return object;
    }

    public static <T extends Block> RegistryObject<Block> createRegistryWithStairSlabWall(String name, Supplier<T> block, Item.Properties properties, boolean withWalls) {
        RegistryObject<Block> object = BLOCKS.register(name, block);
        RegistryObject<Block> object_stair = BLOCKS.register(name+"_stairs", () -> new StairBlock(() -> object.get().defaultBlockState(), BlockBehaviour.Properties.copy(object.get())));
        RegistryObject<Block> object_slab = BLOCKS.register(name+"_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(object.get())));

        ModItems.ITEMS.register(name, () -> new BlockItem(object.get(), properties));
        ModItems.ITEMS.register(name+"_stairs", () -> new BlockItem(object_stair.get(), properties));
        ModItems.ITEMS.register(name+"_slab", () -> new BlockItem(object_slab.get(), properties));

        if(withWalls){
            RegistryObject<Block> object_wall = BLOCKS.register(name+"_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(object.get())));
            ModItems.ITEMS.register(name+"_wall", () -> new BlockItem(object_wall.get(), properties));
        }

        return object;
    }

    public static <T extends Block> RegistryObject<Block> createRegistryWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static StairBlock getStairFromBlock(Block block){
        return (StairBlock) BLOCKS.getEntries().stream().map(RegistryObject::get)
                .filter((blockIn -> blockIn.getDescriptionId()
                        .equals(block.getDescriptionId().concat("_stairs"))))
                .findFirst().get();
    }

    public static SlabBlock getSlabFromBlock(Block block){
        return (SlabBlock) BLOCKS.getEntries().stream().map(RegistryObject::get)
                .filter((blockIn -> blockIn.getDescriptionId()
                        .equals(block.getDescriptionId().concat("_slab"))))
                .findFirst().get();
    }

    public static WallBlock getWallFromBlock(Block block){
        return (WallBlock) BLOCKS.getEntries().stream().map(RegistryObject::get)
                .filter((blockIn -> blockIn.getDescriptionId()
                        .equals(block.getDescriptionId().concat("_wall"))))
                .findFirst().get();
    }

    private static ToIntFunction<BlockState> litBlockEmission(int pLightValue) {
        return (state) -> state.getValue(BlockStateProperties.LIT) ? pLightValue : 0;
    }
}
