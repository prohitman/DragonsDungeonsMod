package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.blocks.entity.FoundryBlockEntity;
import com.prohitman.dragonsdungeons.common.blocks.entity.TreasureChestBlockEntity;
import com.prohitman.dragonsdungeons.common.blocks.entity.UrnBlockEntity;
import com.prohitman.dragonsdungeons.common.blocks.obj.TreasureChestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, DragonsDungeons.MODID);

    public static final RegistryObject<BlockEntityType<TreasureChestBlockEntity>> TREASURE_CHEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("treasure_chest_be", () -> BlockEntityType.Builder.of(TreasureChestBlockEntity::new, ModBlocks.TREASURE_CHEST.get()).build(null));
    public static final RegistryObject<BlockEntityType<UrnBlockEntity>> URN_BLOCK_ENTITY = BLOCK_ENTITIES.register("urn_be", () -> BlockEntityType.Builder.of(UrnBlockEntity::new, ModBlocks.URN.get()).build(null));

    public static final RegistryObject<BlockEntityType<FoundryBlockEntity>> FOUNDRY_BLOCK_ENTITY = BLOCK_ENTITIES.register("foundry_be", () -> BlockEntityType.Builder.of(FoundryBlockEntity::new, ModBlocks.FOUNDRY.get()).build(null));

}
