package com.prohitman.dragonsdungeons.client.events;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.entities.renderers.EmblemscuteRenderer;
import com.prohitman.dragonsdungeons.client.entities.renderers.WargRenderer;
import com.prohitman.dragonsdungeons.client.screen.FoundryScreen;
import com.prohitman.dragonsdungeons.client.screen.ModMenuTypes;
import com.prohitman.dragonsdungeons.client.screen.TreasureChestScreen;
import com.prohitman.dragonsdungeons.client.screen.UrnScreen;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = DragonsDungeons.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.WARG.get(), WargRenderer::new);
            EntityRenderers.register(ModEntities.EMBLEMSCUTE.get(), EmblemscuteRenderer::new);

            EntityRenderers.register(ModEntities.FLASK_OF_FIRE_PROJECTILE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.FLASK_OF_ACID_PROJECTILE.get(), ThrownItemRenderer::new);

            MenuScreens.register(ModMenuTypes.TREASURE_CHEST_MENU.get(), TreasureChestScreen::new);
            MenuScreens.register(ModMenuTypes.URN_MENU.get(), UrnScreen::new);
            MenuScreens.register(ModMenuTypes.FOUNDRY_MENU.get(), FoundryScreen::new);
        });
    }
}
