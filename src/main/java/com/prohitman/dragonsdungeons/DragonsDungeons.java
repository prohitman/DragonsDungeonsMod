package com.prohitman.dragonsdungeons;

import com.mojang.logging.LogUtils;
import com.prohitman.dragonsdungeons.client.screen.ModMenuTypes;
import com.prohitman.dragonsdungeons.core.events.CommonForgeEvents;
import com.prohitman.dragonsdungeons.core.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
        ModParticleTypes.PARTICLES.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ModBiomeModifiers.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        ModParticles.PARTICLES.register(modEventBus);
        ModRecipeSerializers.SERIALIZERS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
