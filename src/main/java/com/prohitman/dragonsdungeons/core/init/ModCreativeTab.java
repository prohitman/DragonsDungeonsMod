package com.prohitman.dragonsdungeons.core.init;

import com.prohitman.dragonsdungeons.DragonsDungeons;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DragonsDungeons.MODID);

    public static final RegistryObject<CreativeModeTab> DRAGONS_DUNGEONS_TAB = CREATIVE_MODE_TABS.register(DragonsDungeons.MODID + "_tab", () -> CreativeModeTab.builder()
            .icon(() -> ModItems.DRAGON_BONE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.DRAGON_BONE.get());
                output.accept(ModBlocks.AGING_ADOBE.get().asItem());
            }).build());
}
