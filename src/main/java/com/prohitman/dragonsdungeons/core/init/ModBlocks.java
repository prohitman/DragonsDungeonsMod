package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DragonsDungeons.MODID);

    public static final RegistryObject<Block> AGING_ADOBE = createRegistry("aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> AGING_ADOBE_PILLAR = createRegistry("aging_adobe_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CHISELED_AGING_ADOBE = createRegistry("chiseled_aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> CRACKED_AGING_ADOBE = createRegistry("cracked_aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> PAINTED_AGING_ADOBE = createRegistry("painted_aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());
    public static final RegistryObject<Block> MOSSY_AGING_ADOBE = createRegistry("mossy_aging_adobe", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SAND)), new Item.Properties());

    public static <T extends Block> RegistryObject<Block> createRegistry(String name, Supplier<T> block, Item.Properties properties) {
        RegistryObject<Block> object = BLOCKS.register(name, block);
        ModItems.ITEMS.register(name, () -> new BlockItem(object.get(), properties));

        return object;
    }

    public static <T extends Block> RegistryObject<Block> createRegistryWithoutBlockItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }
}
