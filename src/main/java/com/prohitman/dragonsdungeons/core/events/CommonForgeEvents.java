package com.prohitman.dragonsdungeons.core.events;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = DragonsDungeons.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonForgeEvents {

    @SubscribeEvent
    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {});
    }
}
