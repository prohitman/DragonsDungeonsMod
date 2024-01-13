package com.prohitman.dragonsdungeons.client.events;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.screen.FoundryScreen;
import com.prohitman.dragonsdungeons.client.screen.ModMenuTypes;
import com.prohitman.dragonsdungeons.client.screen.TreasureChestScreen;
import com.prohitman.dragonsdungeons.client.screen.UrnScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DragonsDungeons.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        MenuScreens.register(ModMenuTypes.TREASURE_CHEST_MENU.get(), TreasureChestScreen::new);
        MenuScreens.register(ModMenuTypes.URN_MENU.get(), UrnScreen::new);
        MenuScreens.register(ModMenuTypes.FOUNDRY_MENU.get(), FoundryScreen::new);

    }
}
