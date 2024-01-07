package com.prohitman.dragonsdungeons;

import com.mojang.logging.LogUtils;
import com.prohitman.dragonsdungeons.client.screen.ModMenuTypes;
import com.prohitman.dragonsdungeons.core.events.CommonForgeEvents;
import com.prohitman.dragonsdungeons.core.init.ModBlockEntities;
import com.prohitman.dragonsdungeons.core.init.ModBlocks;
import com.prohitman.dragonsdungeons.core.init.ModCreativeTab;
import com.prohitman.dragonsdungeons.core.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(DragonsDungeons.MODID)
public class DragonsDungeons
{
    public static final String MODID = "dragonsdungeons";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DragonsDungeons()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(CommonForgeEvents::commonSetup);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTab.CREATIVE_MODE_TABS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
