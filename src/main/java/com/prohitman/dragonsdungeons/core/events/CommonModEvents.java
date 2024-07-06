package com.prohitman.dragonsdungeons.core.events;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.common.entities.EmblemscuteEntity;
import com.prohitman.dragonsdungeons.common.entities.WargEntity;
import com.prohitman.dragonsdungeons.common.entities.ZargEntity;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = DragonsDungeons.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.WARG.get(), WargEntity.createAttributes().build());
        event.put(ModEntities.ZARG.get(), ZargEntity.createAttributes().build());
        event.put(ModEntities.EMBLEMSCUTE.get(), EmblemscuteEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawns(@NotNull SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.ZARG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ZargEntity::checkZargSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.WARG.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, WargEntity::checkWargSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.EMBLEMSCUTE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EmblemscuteEntity::checkEmblemscuteSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}