package com.prohitman.dragonsdungeons.client.events;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import com.prohitman.dragonsdungeons.client.entities.renderers.AutomatonRenderer;
import com.prohitman.dragonsdungeons.client.entities.renderers.EmblemscuteRenderer;
import com.prohitman.dragonsdungeons.client.entities.renderers.WargRenderer;
import com.prohitman.dragonsdungeons.client.entities.renderers.ZargRenderer;
import com.prohitman.dragonsdungeons.client.screen.*;
import com.prohitman.dragonsdungeons.common.particles.MithrilCrystalParticle;
import com.prohitman.dragonsdungeons.core.init.ModEntities;
import com.prohitman.dragonsdungeons.core.init.ModParticles;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.SoulParticle;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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
            EntityRenderers.register(ModEntities.ZARG.get(), ZargRenderer::new);
            EntityRenderers.register(ModEntities.AUTOMATON.get(), AutomatonRenderer::new);

            EntityRenderers.register(ModEntities.FLASK_OF_FIRE_PROJECTILE.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.FLASK_OF_ACID_PROJECTILE.get(), ThrownItemRenderer::new);

            MenuScreens.register(ModMenuTypes.TREASURE_CHEST_MENU.get(), TreasureChestScreen::new);
            MenuScreens.register(ModMenuTypes.URN_MENU.get(), UrnScreen::new);
            MenuScreens.register(ModMenuTypes.FOUNDRY_MENU.get(), FoundryScreen::new);

        });
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event){
        event.registerSpriteSet(ModParticles.ACID_FLASK_PARTICLE.get(), SoulParticle.EmissiveProvider::new);
        event.registerSpriteSet(ModParticles.MITHRIL_CRYSTAL_PARTICLE.get(), MithrilCrystalParticle.Provider::new);
    }
}
